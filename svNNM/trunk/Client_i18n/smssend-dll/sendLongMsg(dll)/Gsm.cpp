// Gsm.cpp: implementation of the CGsm class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"

#include "Gsm.h"
#include "time.h"
#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
//#define new DEBUG_NEW
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CGsm::CGsm()
{
	m_comm.SetPortName( _T( "COM3" ) );
	ResetMsgID();
}

CGsm::~CGsm()
{

}

int CGsm::CommandAT()
{
	CString strCommand, strAns;
	strCommand = _T( "AT\r" );
	int iSuccess = FreeCommand( strCommand, strAns, 2 );
	if( iSuccess == FALSE )
		iSuccess = FreeCommand( strCommand, strAns, 2 );
	return iSuccess;
}

//strCommand:		in unicode;
//strAns:			in unicode;
//strKeySuccess:	in unicode;
//strKeyFailure:	in unicode;
int CGsm::FreeCommand(CString strCommand, CString &strAns, int iWaitingTime, CString strKeySuccess, CString strKeyFailure)
{

	char *psz = NULL;
	psz = new char[ strCommand.GetLength() + 2 ];
	if( psz == NULL )
		return RETURN_OTHERERROR;

	int iLength = ::WideCharToMultiByte( CP_ACP, 0, strCommand, strCommand.GetLength(),
		(char*)psz, strCommand.GetLength(), NULL, NULL );

	if( m_comm.OpenComm( ) == FALSE )
	{
		delete psz;
		psz = NULL;
		return RETURN_PORTERROR;
	}
	m_comm.WriteComm( psz, iLength );

	// read data
	int iTimeOut = iWaitingTime * 10;	// ֵΪ0ʱ��ʾ��ʱ��
	const BUFFER_LENGTH = 1000;
	char buffer[BUFFER_LENGTH];
	ZeroMemory( buffer, BUFFER_LENGTH );
	int iIndex = 0;
	while( strAns.Find( strKeySuccess ) < 0 && strAns.Find( strKeyFailure ) < 0 )
	{
		// read;
		BYTE byte;

		iTimeOut = iWaitingTime * 10;	// ֵΪ0ʱ��ʾ��ʱ��
		while( m_comm.ReadComm( &byte, 1 ) == FALSE && iTimeOut > 0 )
		{
			iTimeOut --;
			::Sleep( 100 );
		}

		if( iTimeOut <= 0 )
		{
			break;
		}

		if( iIndex < BUFFER_LENGTH - 1 )
		{
			buffer[ iIndex++ ] = byte;
		}
		WCHAR wchar[ BUFFER_LENGTH * 2 ];
		::MultiByteToWideChar(CP_ACP, 0, buffer, -1, wchar, BUFFER_LENGTH * 2);
		strAns = wchar;
	}
	m_comm.ClearPort( );
	m_comm.CloseComm();
	delete psz;
	psz = NULL;


	if( iTimeOut <= 0 )
	{
		return RETURN_TIMEOUT;
	}
	if( strAns.Find( strKeySuccess ) >= 0 )
	{
		return RETURN_SUCCESS;
	}
	if( strAns.Find( strKeyFailure ) >= 0 )
	{
		return RETURN_FAILURE;
	}
	return RETURN_OTHERERROR;
}

BOOL CGsm::IfSupportPdu()
{
	CString strCommand, strAns;
	strCommand = _T( "AT+CMGF=0\r" );
	BOOL bSuccess = FreeCommand( strCommand, strAns, 1 );
	return bSuccess;
}

// PDU���룬���ڱ��ơ����Ͷ���Ϣ
// sm: ԴPDU����ָ��
// pDst: Ŀ��PDU��ָ��
// ����: Ŀ��PDU������
int CGsm::EncodePdu(struct SM_param *pSm, char *pDst)
{
	if( pSm == NULL )
	{
		return 0;
	}
    int nLength = 0;			// �ڲ��õĴ�����
    int nDstLength = 0;			// Ŀ��PDU������
    BYTE buf[256];				// �ڲ��õĻ�����

    // SMSC��ַ��Ϣ��
    nLength = pSm->strSMSC.GetLength();    // SMSC��ַ�ַ����ĳ���
    if(nLength>0)
	{
		buf[0] = (char)((nLength & 1) == 0 ? nLength : nLength + 1) / 2 + 1;    // SMSC��ַ��Ϣ����
		buf[1] = 0x91;        // �̶�: �ù��ʸ�ʽ����
		nDstLength = Bytes2String(buf, pDst, 2);        // ת��2���ֽڵ�Ŀ��PDU��
		nDstLength += InvertNumbers(pSm->strSMSC, &pDst[nDstLength] );    // ת��SMSC��Ŀ��PDU��
	}
	else
	{
		buf[0]=0;
		nDstLength = Bytes2String(buf, pDst, 1);        // ת��2���ֽڵ�Ŀ��PDU��
	}

	// TPDU�λ���������Ŀ���ַ��
	nLength = pSm->strNumber.GetLength();				// TP-DA��ַ�ַ����ĳ���
	buf[0] = 0x11;										// �Ƿ��Ͷ���(TP-MTI=01)��TP-VP����Ը�ʽ(TP-VPF=10)
	buf[1] = 0;											// TP-MR=0
	buf[2] = (char)nLength;								// Ŀ���ַ���ָ���(TP-DA��ַ�ַ�����ʵ����)
	buf[3] = 0x81;										// �̶�: ���ù��ʸ�ʽ����
	nDstLength += Bytes2String( buf, &pDst[nDstLength], 4 );			// ת��4���ֽڵ�Ŀ��PDU��
	nDstLength += InvertNumbers( pSm->strNumber, &pDst[nDstLength] );	// ת��TP-DA��Ŀ��PDU��

//    // TPDU��Э���ʶ�����뷽ʽ���û���Ϣ��
	int iCodeLength = 0;
	int iStrLength  = 0;
	buf[0] = 0;        // Э���ʶ(TP-PID)
	buf[1] = EncodeText(pSm->strText, GetDCS( pSm->strText ), &buf[4], iCodeLength, iStrLength );        // �û���Ϣ���뷽ʽ(TP-DCS)
	buf[2] = pSm->ucValidTime;            // ��Ч��(TP-VP)
	buf[3] = iStrLength;

	nDstLength += Bytes2String(buf, &pDst[nDstLength], iCodeLength + 4 );        // ת���ö����ݵ�Ŀ��PDU��
    
	return nDstLength;
}

// �ֽ�����ת��Ϊ�ɴ�ӡ�ַ���
// �磺{0xC8, 0x32, 0x9B, 0xFD, 0x0E, 0x01} --> "C8329BFD0E01" 
// pSrc: Դ����ָ��
// pDst: Ŀ���ַ���ָ��
// nSrcLength: Դ���ݳ���
// ����: Ŀ���ַ�������
int CGsm::Bytes2String(const unsigned char *pSrc, char *pDst, int nSrcLength)
{
	if( pSrc == NULL || pDst == NULL )
	{
		return 0;
	}

	CString strHex, strTemp;
	for( int i = 0; i < nSrcLength; i++ )
	{
		strTemp.Format( _T("%.2X"), (unsigned char)pSrc[ i ] );
		strHex += strTemp;
	}

	int iLength = ::WideCharToMultiByte( CP_ACP, 0, strHex, strHex.GetLength(),
								(char*)pDst, strHex.GetLength() + 1, NULL, NULL );
	return iLength;
}

// ����˳����ַ���ת��Ϊ�����ߵ����ַ�����������Ϊ��������'F'�ճ�ż��
// �磺"8613851872468" --> "683158812764F8"
// strNum: Դ�ַ���
// pDst: Ŀ���ַ���ָ��
// ����: Ŀ���ַ�������
int CGsm::InvertNumbers(CString strNum, char *pDst)
{
	if( pDst == NULL )
	{
		return 0;
	}

    char ch;          // ���ڱ���һ���ַ�

	if( strNum.GetLength() & 1 )
	{
		strNum += _T( "F" );
	}
    
    // �����ߵ�
    for(int i=0; i< strNum.GetLength() ;i+=2)
    {
        ch		= (char)strNum[i];		// �����ȳ��ֵ��ַ�
        *pDst++ = (char)strNum[i+1];	// ���ƺ���ֵ��ַ�
        *pDst++ = ch;					// �����ȳ��ֵ��ַ�
    }
    
    // ����ַ����Ӹ�������
    *pDst = '\0';
    
    // ����Ŀ���ַ�������
    return strNum.GetLength();
}

//#define GSM_7BIT        0
//#define GSM_8BIT        4
//#define GSM_UCS2        8
//return: �û���Ϣ���뷽ʽ(TP-DCS)
char CGsm::EncodeText(CString strSrc, int iCodeType, BYTE *pDst, int &iCodeLength, int &iStrLength )
{
	if( pDst == NULL )
		return 0;

	iCodeLength = 0;
	iStrLength  = 0;
	char cTP_DCS = GSM_7BIT;
	switch( iCodeType )
	{ // 1: unicode; 2: 7bit
	case 1:
		cTP_DCS = GSM_UCS2;
		break;
	case 2:
		cTP_DCS = GSM_7BIT;
		break;
	default:
		// auto
		break;
	}

	for( int i = 0; i < strSrc.GetLength(); i ++ )
	{
		TCHAR tc = strSrc.GetAt( i );
		if( tc >= 128 || tc < 0 )
		{
			cTP_DCS = GSM_UCS2;

			if( strSrc.GetLength() > 70 )
			{
				strSrc = strSrc.Left( 70 );
			}
			break;
		}
	}

	if( cTP_DCS == GSM_UCS2 )
	{
		for( int i = 0; i < strSrc.GetLength(); i ++ )
		{
			USHORT tc = strSrc.GetAt( i );
			BYTE byHi = HIBYTE( tc );
			BYTE byLo = LOBYTE( tc );
			
			*(pDst++) = byHi;
			*(pDst++) = byLo;

		}
		iCodeLength = strSrc.GetLength() * 2;
		iStrLength  = iCodeLength;
	}
	else if( cTP_DCS == GSM_7BIT )
	{
		char *pSrc = NULL;
		pSrc = new char[ strSrc.GetLength() + 1 ];
		if( pSrc != NULL )
		{
			ZeroMemory( pSrc, strSrc.GetLength() + 1 );
			int iLength = ::WideCharToMultiByte( CP_ACP, 0, strSrc, strSrc.GetLength(),
										pSrc, strSrc.GetLength() + 1, NULL, NULL );
			iCodeLength = Encode7bit( pSrc, pDst, iLength + 1 );

			delete pSrc;
		}
		iStrLength = strSrc.GetLength();
	}
	return cTP_DCS;
}

//return: if it equals 0, writing is unsuccessful, otherwise it indecade d order
int CGsm::WriteMsg(SM_param *pSm)
{
	if( pSm == NULL )
	{
		return 0;
	}

    int nPduLength;        // PDU������
    unsigned char nSmscLength;    // SMSC������
    char pdu[512];         // PDU��
	ZeroMemory( pdu, 512 );
	int index=0;				//if it equals 0, writing is unsuccessful, otherwise it indecade d order
	CString strCommand, strAns;

	nPduLength = EncodePdu(pSm, pdu);		// ����PDU����������PDU��

    nSmscLength = pSm->strSMSC.GetLength();
	if( nSmscLength & 1 )
	{ // ����
		nSmscLength ++;
	}
	nSmscLength ++;			// ������

    // �����еĳ��ȣ�������SMSC��Ϣ���ȣ��������ֽڼ�
	strCommand.Format( _T("AT+CMGW=%d\r"), nPduLength / 2 - nSmscLength );

	if( FreeCommand( strCommand, strAns, 5, _T("\r\n> ") ) != RETURN_SUCCESS )
	{
		return index;
	}

	::MultiByteToWideChar(CP_ACP, 0, pdu , -1, strCommand.GetBuffer( nPduLength * 2 ), nPduLength * 2 );
	strCommand.ReleaseBuffer();

    strCommand += _T("\x01a\r");					// ��Ctrl-Z����

	if( FreeCommand( strCommand, strAns, 6 ) != RETURN_SUCCESS )
	{
		return index;
	}

    if( strAns.Find( _T("CMGW:") ) >= 0 )
	{
		strAns.Delete( 0, strAns.Find( _T("CMGW:") ) + 5 );
//		while( strAns.Right( 1 ) >= '9' || strAns.Right( 1 ) <= '0' )
//		{
//			strAns.Delete( strAns.GetLength() - 1 );
//		}
		swscanf( strAns, _T("%d"), &index );
	}
	return index;
}

BOOL CGsm::SendCardMsg(int iIndex)
{
	CString strCommand, strAns;
	strCommand.Format( _T("AT+CMSS=%d\r"), iIndex );

	if( FreeCommand( strCommand, strAns, 10 ) == RETURN_SUCCESS )
	{
		return TRUE;
	}
	else
	{
		return FALSE;
	}

}

BOOL CGsm::DeleteCardMsg( int iIndex )
{
	CString strCommand, strAns;
	strCommand.Format( _T("AT+CMGD=%d\r"), iIndex );
	if( FreeCommand( strCommand, strAns, 6 ) == RETURN_SUCCESS )
	{
		return TRUE;
	}
	else
	{
		return FALSE;
	}
}

// 7-bit����
// pSrc: Դ�ַ���ָ��
// pDst: Ŀ����봮ָ��
// nSrcLength: Դ�ַ�������
// ����: Ŀ����봮����
int CGsm::Encode7bit(const char *pSrc, unsigned char *pDst, int nSrcLength)
{
    int nSrc;        // Դ�ַ����ļ���ֵ
    int nDst;        // Ŀ����봮�ļ���ֵ
    int nChar;       // ��ǰ���ڴ���������ַ��ֽڵ���ţ���Χ��0-7
    unsigned char nLeft;    // ��һ�ֽڲ��������
    
    // ����ֵ��ʼ��
    nSrc = 0;
    nDst = 0;
    
    // ��Դ��ÿ8���ֽڷ�Ϊһ�飬ѹ����7���ֽ�
    // ѭ���ô�����̣�ֱ��Դ����������
    // ������鲻��8�ֽڣ�Ҳ����ȷ����
    while(nSrc<nSrcLength)
    {
        // ȡԴ�ַ����ļ���ֵ�����3λ
        nChar = nSrc & 7;
    
        // ����Դ����ÿ���ֽ�
        if(nChar == 0)
        {
            // ���ڵ�һ���ֽڣ�ֻ�Ǳ�����������������һ���ֽ�ʱʹ��
            nLeft = *pSrc;
        }
        else
        {
            // ���������ֽڣ������ұ߲��������������ӣ��õ�һ��Ŀ������ֽ�
            *pDst = (*pSrc << (8-nChar)) | nLeft;
    
            // �����ֽ�ʣ�µ���߲��֣���Ϊ�������ݱ�������
            nLeft = *pSrc >> nChar;
            // �޸�Ŀ�괮��ָ��ͼ���ֵ 
			pDst++;
            nDst++; 
        } 
        
        // �޸�Դ����ָ��ͼ���ֵ
        pSrc++; nSrc++;
    }
    
    // ����Ŀ�괮����
    return nDst; 
}

BOOL CGsm::GetPhoneCorp(CString &str)
{
	str.Empty();
	BOOL bRe = FALSE;

	CString strCommand, strAns;
	strCommand = _T( "AT+CGMI\r" );

	//��str��ȥ��+CGMI �� OK��Ȼ���ڰ����еĻس����з��Ŷ�ȥ����
	if( FreeCommand( strCommand, strAns, 2 ) == RETURN_SUCCESS )
	{
		CString strKey;
		strKey = _T("CGMI");
		if( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( 0, strAns.Find( strKey ) + strKey.GetLength() );
		}

		strKey = _T("OK");
		if( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( strAns.Find( strKey ), strKey.GetLength() );
		}

		strKey = _T("\r");
		while( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( strAns.Find( strKey ), strKey.GetLength() );
		}

		strKey = _T("\n");
		while( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( strAns.Find( strKey ), strKey.GetLength() );
		}

		str = strAns;
		bRe = TRUE;
	}

	return bRe;
}

BOOL CGsm::GetPhoneModel(CString &str)
{
	str.Empty();
	BOOL bRe = FALSE;

	CString strCommand, strAns;
	strCommand = _T( "AT+CGMM\r" );

	//��str��ȥ��+CGMI �� OK��Ȼ���ڰ����еĻس����з��Ŷ�ȥ����
	if( FreeCommand( strCommand, strAns, 2 ) == RETURN_SUCCESS )
	{
		CString strKey;
		strKey = _T("CGMM");
		if( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( 0, strAns.Find( strKey ) + strKey.GetLength() );
		}

		strKey = _T("OK");
		if( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( strAns.Find( strKey ), strKey.GetLength() );
		}

		strKey = _T("\r");
		while( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( strAns.Find( strKey ), strKey.GetLength() );
		}

		strKey = _T("\n");
		while( strAns.Find( strKey ) >= 0 )
		{
			strAns.Delete( strAns.Find( strKey ), strKey.GetLength() );
		}

		str = strAns;
		bRe = TRUE;
	}

	return bRe;
}



//void CGsm::ErrorMsg(int iErrorNo)
//{
//	switch( iErrorNo )
//	{
//	case CGsm::RETURN_SUCCESS:
//		break;
//	case CGsm::RETURN_FAILURE:
//		::AfxMessageBox( _T("δ֪�豸��") );
//		break;
//	case CGsm::RETURN_PORTERROR:
//		::AfxMessageBox( _T("�򿪶˿�ʧ�ܣ�") );
//		break;
//	case CGsm::RETURN_TIMEOUT:
//		::AfxMessageBox( _T("ͨѶ��ʱ��") );
//		break;
//	default:
//		::AfxMessageBox( _T("δ֪����") );
//		break;
//	}
//}

BOOL CGsm::SendMsg(SM_param *pSm)
{
	if( pSm == NULL )
	{
		return FALSE;
	}

    int nPduLength;        // PDU������
    unsigned char nSmscLength;    // SMSC������
    char pdu[512];         // PDU��
	ZeroMemory( pdu, 512 );
	CString strCommand, strAns;

	nPduLength = EncodePdu(pSm, pdu);		// ����PDU����������PDU��

    nSmscLength = pSm->strSMSC.GetLength();
	if( nSmscLength & 1 )
	{ // ����
		nSmscLength ++;
	}
	nSmscLength ++;			// ������

    // �����еĳ��ȣ�������SMSC��Ϣ���ȣ��������ֽڼ�
	strCommand.Format( _T("AT+CMGS=%d\r"), nPduLength / 2 - nSmscLength );

	if( FreeCommand( strCommand, strAns, 5, _T("\r\n> ") ) != RETURN_SUCCESS )
	{
		return FALSE;
	}

	::MultiByteToWideChar(CP_ACP, 0, pdu , -1, strCommand.GetBuffer( nPduLength * 2 ), nPduLength * 2 );
	strCommand.ReleaseBuffer();

    strCommand += _T("\x01a\r");					// ��Ctrl-Z����

	if( FreeCommand( strCommand, strAns, 6 ) != RETURN_SUCCESS )
	{
		return FALSE;
	}
	else
	{
		return TRUE;	
	}
}

void CGsm::ResetMsgID()
{
	srand( (unsigned)time( NULL ) );
	m_iMsgID = rand();
}

BYTE CGsm::GetMsgSectionCount(CString strMsg)
{
	int iSingleLength  = 70;
	int iSectionLength = iSingleLength - 3;
	switch( GetDCS( strMsg ) )
	{
	case GSM_7BIT:
		iSingleLength  = 160;
		iSectionLength = iSingleLength - 7;
		break;
	case GSM_UCS2:
		iSingleLength  = 70;
		iSectionLength = iSingleLength - 3;
		break;
	default:
		break;
	}

	if( strMsg.GetLength() <= iSingleLength )
	{
		return 1;
	}
	else
	{
		return BYTE( ( strMsg.GetLength() + iSectionLength - 1 ) / iSectionLength );
	}
}

//return: �û���Ϣ���뷽ʽ(TP-DCS)
char CGsm::GetDCS(CString strMsg)
{
	char cTP_DCS = GSM_7BIT;

	for( int i = 0; i < strMsg.GetLength(); i ++ )
	{
		TCHAR tc = strMsg.GetAt( i );
		if( tc >= 128 || tc < 0 )
		{
			cTP_DCS = GSM_UCS2;
			break;
		}
	}
	
	return cTP_DCS;
}

CString CGsm::GetMsgSection( CString strMsg, int iIndex )
{
	CString strSec;

	if( iIndex <= 0 || iIndex > GetMsgSectionCount( strMsg ) )
	{
		return strSec;
	}

	int iSingleLength  = 70;
	int iSectionLength = iSingleLength - 3;
	switch( GetDCS( strMsg ) )
	{
	case GSM_7BIT:
		iSingleLength  = 160;
		iSectionLength = iSingleLength - 7;
		break;
	case GSM_UCS2:
		iSingleLength  = 70;
		iSectionLength = iSingleLength - 3;
		break;
	default:
		break;
	}

	if( strMsg.GetLength() <= iSingleLength )
	{
		return strSec;
	}

	strSec = strMsg.Mid( ( iIndex - 1 ) * iSectionLength, iSectionLength );

	return strSec;

}

// return: HeadLength
int CGsm::MakeLongMsgHead(BYTE *pBuf, char cDCS, UINT uiID, BYTE byIndex, BYTE byMax)
{
	int iLength = 0;
	if( pBuf == NULL )
	{
		return iLength;
	}
	if( byMax == 1 )
	{
		return iLength;
	}

	iLength ++;
	if( cDCS == GSM_7BIT )
	{
		pBuf[ iLength++ ] = 0x00;
		pBuf[ iLength++ ] = 0x03;
		pBuf[ iLength++ ] = (BYTE)uiID;
	}
	else if( cDCS == GSM_UCS2 )
	{
//		pBuf[ iLength++ ] = 0x08;
//		pBuf[ iLength++ ] = 0x04;
		pBuf[ iLength++ ] = 0x00;
		pBuf[ iLength++ ] = 0x03;
		pBuf[ iLength++ ] = (BYTE)uiID;
//		*( USHORT *)(&pBuf[ iLength ]) = uiID;
//		iLength += 2;
	}
	else
	{
		return 0;
	}

	pBuf[ iLength++ ] = byMax;
	pBuf[ iLength++ ] = byIndex;
	pBuf[ 0 ] = iLength - 1;
	return iLength;
}

int CGsm::WriteLongMsg(SM_param *pSm, BYTE byIndex, BYTE byMax)
{
// 05-05-18 Add start // bug: byMax == 1ʱ����Ϣ����Ϊ�ա�
	if( byMax == 1 )
	{
		return WriteMsg( pSm );
	}
// 05-05-18 Add End
	if( pSm == NULL )
	{
		return 0;
	}

    int nPduLength;        // PDU������
    unsigned char nSmscLength;    // SMSC������
    char pdu[512];         // PDU��
	ZeroMemory( pdu, 512 );
	int index=0;				//if it equals 0, writing is unsuccessful, otherwise it indecade d order
	CString strCommand, strAns;

	nPduLength = EncodePdu(pSm, pdu, byIndex, byMax );		// ����PDU����������PDU��

    nSmscLength = pSm->strSMSC.GetLength();
	if( nSmscLength & 1 )
	{ // ����
		nSmscLength ++;
	}
	nSmscLength ++;			// ������

    // �����еĳ��ȣ�������SMSC��Ϣ���ȣ��������ֽڼ�
	strCommand.Format( _T("AT+CMGW=%d\r"), nPduLength / 2 - nSmscLength );

	if( FreeCommand( strCommand, strAns, 5, _T("\r\n> ") ) != RETURN_SUCCESS )
	{
		return index;
	}

	::MultiByteToWideChar(CP_ACP, 0, pdu , -1, strCommand.GetBuffer( nPduLength * 2 ), nPduLength * 2 );
	strCommand.ReleaseBuffer();

    strCommand += _T("\x01a\r");					// ��Ctrl-Z����

	if( FreeCommand( strCommand, strAns, 6 ) != RETURN_SUCCESS )
	{
		return index;
	}

    if( strAns.Find( _T("CMGW:") ) >= 0 )
	{
		strAns.Delete( 0, strAns.Find( _T("CMGW:") ) + 5 );
//		while( strAns.Right( 1 ) >= '9' || strAns.Right( 1 ) <= '0' )
//		{
//			strAns.Delete( strAns.GetLength() - 1 );
//		}
		swscanf( strAns, _T("%d"), &index );
	}
	return index;
}

// PDU���룬���ڱ��ơ����Ͷ���Ϣ
// sm: ԴPDU����ָ��
// pDst: Ŀ��PDU��ָ��
// ����: Ŀ��PDU������
int CGsm::EncodePdu(SM_param *pSm, char *pDst, BYTE byIndex, BYTE byMax)
{
	if( pSm == NULL )
	{
		return 0;
	}
    int nLength = 0;			// �ڲ��õĴ�����
    int nDstLength = 0;			// Ŀ��PDU������
    BYTE buf[256];				// �ڲ��õĻ�����

    // SMSC��ַ��Ϣ��
    nLength = pSm->strSMSC.GetLength();    // SMSC��ַ�ַ����ĳ���
    if(nLength>0)
	{
		buf[0] = (char)((nLength & 1) == 0 ? nLength : nLength + 1) / 2 + 1;    // SMSC��ַ��Ϣ����
		buf[1] = 0x91;        // �̶�: �ù��ʸ�ʽ����
		nDstLength = Bytes2String(buf, pDst, 2);        // ת��2���ֽڵ�Ŀ��PDU��
		nDstLength += InvertNumbers(pSm->strSMSC, &pDst[nDstLength] );    // ת��SMSC��Ŀ��PDU��
	}
	else
	{
		buf[0]=0;
		nDstLength = Bytes2String(buf, pDst, 1);        // ת��2���ֽڵ�Ŀ��PDU��
	}

	// TPDU�λ���������Ŀ���ַ��
	nLength = pSm->strNumber.GetLength();				// TP-DA��ַ�ַ����ĳ���
//	buf[0] = 0x11;										// �Ƿ��Ͷ���(TP-MTI=01)��TP-VP����Ը�ʽ(TP-VPF=10)
//	buf[1] = 0;											// TP-MR=0
	buf[0] = 0x51;										// �Ƿ��Ͷ���(TP-MTI=**** **01)��TP-VP����Ը�ʽ(TP-VPF=***1 0***)
														// ������Ϣ( TP-MMS = *1** **** )
	buf[1] = 0xFF;										// TP-MR
	buf[2] = (char)nLength;								// Ŀ���ַ���ָ���(TP-DA��ַ�ַ�����ʵ����)
	buf[3] = 0x81;										// �̶�: ���ù��ʸ�ʽ����
	nDstLength += Bytes2String( buf, &pDst[nDstLength], 4 );			// ת��4���ֽڵ�Ŀ��PDU��
	nDstLength += InvertNumbers( pSm->strNumber, &pDst[nDstLength] );	// ת��TP-DA��Ŀ��PDU��

//    // TPDU��Э���ʶ�����뷽ʽ���û���Ϣ��
	int iCodeLength = 0;
	int iStrLength  = 0;

//	int iHeadLength = MakeLongMsgHead( &buf[4], GetDCS( pSm->strText ), m_iMsgID, byIndex, byMax );
//	EncodeText( GetMsgSection( pSm->strText, byIndex ), 
//				GetDCS( pSm->strText ), 
//				&buf[ 4 + iHeadLength ], 
//				iCodeLength, 
//				iStrLength );
//	iCodeLength += iHeadLength;
//	iStrLength  += iHeadLength;

	iCodeLength = EncodeTPUD( GetMsgSection( pSm->strText, byIndex ), 
								GetDCS( pSm->strText ), 
								&buf[ 4 ], 
								iStrLength,
								byIndex,
								byMax );

	buf[0] = 0;        // Э���ʶ(TP-PID)
	buf[1] = GetDCS( pSm->strText );        // �û���Ϣ���뷽ʽ(TP-DCS)
	buf[2] = pSm->ucValidTime;            // ��Ч��(TP-VP)
	buf[3] = iStrLength;
	nDstLength += Bytes2String(buf, &pDst[nDstLength], iCodeLength + 4 );        // ת���ö����ݵ�Ŀ��PDU��
    
	return nDstLength;
}

// function: �����ֲ��ֽ��б��룬��������Ϣ�ı�־��
// param:
//		strSrc: �������ݡ�С�ڵ���һ�����ŵĳ��ȡ�����ж��࣬��ᱻ���˵���
//		cDCS:   ���뷽ʽ
//		pDst:   Ŀ���ڴ��ָ��
//		iStrLength: �������ַ������۳��ȣ����ڲ�����
//		byIndex:    ����Ϣ����µ���Ϣ���
//		byMax:      ����Ϣ����µ������Ϣ����
//return:     ����ĳ���
//�߼���
//
//
int CGsm::EncodeTPUD(CString strSrc, char cDCS, BYTE *pDst, int &iStrLength, BYTE byIndex, BYTE byMax )
{
	if( pDst == NULL )
		return 0 ;
	iStrLength = 0;
	int iCodeLength = 0;

	if( byMax > 1 )
	{ // ���ַ�������һЩ�������ݣ�������ó���Ϣ���ݿ鸲�ǵ��������ݡ�
		if( cDCS == GSM_7BIT )
		{
			strSrc = _T("0000000") + strSrc;
		}
		else
		{ // GSM_UCS2
			strSrc = _T("000") + strSrc;
		}
	}

	if( byIndex < 1 || byIndex > byMax )
	{
		byIndex = 1;
	}

	EncodeText( strSrc, 
				cDCS, 
				pDst, 
				iCodeLength, 
				iStrLength );

	MakeLongMsgHead( pDst, cDCS, m_iMsgID, byIndex, byMax );
	return iCodeLength;
}

// Return:	<0:	error!
//			other :	Free SMS space.
int CGsm::GetSimCardFreeSpaceInfo()
{
	int iRe = -1;
	int iTotal, iUsed;
	iTotal = iUsed = 0;

	CString strCommand, strAns;
	strCommand = _T( "AT+CPMS=\"SM\"\r" );
	int iCommandSuccess = FALSE;
	for( int i = 0; i < 3 && iCommandSuccess != RETURN_SUCCESS; i ++ )
	{
		iCommandSuccess = FreeCommand( strCommand, strAns, 2 );
	}

	if( iCommandSuccess == RETURN_SUCCESS )
	{
		strAns = strAns.Mid( strAns.Find( _T("+CPMS:") ) + 6 );
		if( strAns.Find( _T("\"SM\"") ) >= 0 )
		{
			strAns = strAns.Mid( strAns.Find( _T("\"SM\"") ) + 4 );
			swscanf( strAns, _T(",%d,%d"), &iUsed, &iTotal );
			iRe = iTotal - iUsed;
		}
	}

	return iRe;
}

BOOL CGsm::SendLongMsg(SM_param *pSm, BYTE byIndex, BYTE byMax)
{
	if( pSm == NULL )
	{
		return FALSE;
	}

	int nPduLength;        // PDU������
	unsigned char nSmscLength;    // SMSC������
	char pdu[512];         // PDU��
	ZeroMemory( pdu, 512 );
	int index=0;				//if it equals 0, writing is unsuccessful, otherwise it indecade d order
	CString strCommand, strAns;

	nPduLength = EncodePdu(pSm, pdu, byIndex, byMax );		// ����PDU����������PDU��

	nSmscLength = pSm->strSMSC.GetLength();
	if( nSmscLength & 1 )
	{ // ����
		nSmscLength ++;
	}
	nSmscLength ++;			// ������

	// �����еĳ��ȣ�������SMSC��Ϣ���ȣ��������ֽڼ�
	strCommand.Format( _T("AT+CMGS=%d\r"), nPduLength / 2 - nSmscLength );

	if( FreeCommand( strCommand, strAns, 5, _T("\r\n> ") ) != RETURN_SUCCESS )
	{
		return FALSE;
	}

	::MultiByteToWideChar(CP_ACP, 0, pdu , -1, strCommand.GetBuffer( nPduLength * 2 ), nPduLength * 2 );
	strCommand.ReleaseBuffer();

	strCommand += _T("\x01a\r");					// ��Ctrl-Z����

	if( FreeCommand( strCommand, strAns, 10 ) == RETURN_SUCCESS )
	{
		return TRUE;
	}
	else
	{
		return FALSE;
	}
}
