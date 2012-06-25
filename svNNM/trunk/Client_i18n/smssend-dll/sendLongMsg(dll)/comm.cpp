//comm.cpp
#include "stdafx.h"
#include "comm.h"

CComm::CComm()
{
	m_strPort = _T("COM1");
	m_strRate = _T("38400");
}

CComm::~CComm()
{

}

BOOL CComm::OpenComm(int nParity, int nByteSize, int nStopBits)
{
	int nBaudRate;
	swscanf( m_strRate.GetBuffer(m_strRate.GetLength()), _T("%d"),&nBaudRate);

	BOOL b = OpenComm(m_strPort.GetBuffer(m_strPort.GetLength()),nBaudRate,nParity,nByteSize,nStopBits);

	m_strPort.ReleaseBuffer();
	
	return b;
}
// open port
// pPort: name��"COM1" or "\\.\COM1"
// nBaudRate: 
// nParity: 
// nByteSize: 
// nStopBits: 
BOOL CComm::OpenComm(const TCHAR* pszPortName, int nBaudRate, int nParity, int nByteSize, int nStopBits )
{
    DCB dcb;        // ���ڿ��ƿ�
    COMMTIMEOUTS timeouts = {    // ���ڳ�ʱ���Ʋ���
        MAXDWORD,        // ���ַ������ʱʱ��: 100 ms
        0,          // ������ʱÿ�ַ���ʱ��: 1 ms (n���ַ��ܹ�Ϊn ms)
        0,        // ������(�����)����ʱʱ��: 500 ms
        1,          // д����ʱÿ�ַ���ʱ��: 1 ms (n���ַ��ܹ�Ϊn ms)
        200};       // ������(�����)д��ʱʱ��: 100 ms
    
//	::EnterCriticalSection(&m_csComm);
	CString tempPort;
	tempPort.Format(_T("\\\\.\\%s"),pszPortName);
	printf("tempPort=%s\n",tempPort.GetBuffer(tempPort.GetLength()));
    m_hComm = CreateFile(tempPort,    // �������ƻ��豸·��
						GENERIC_READ | GENERIC_WRITE,		// ��д��ʽ
					    0,									// ����ʽ����ռ
						NULL,								// Ĭ�ϵİ�ȫ������
						OPEN_EXISTING,						// ������ʽ
						0,									// ���������ļ�����
						NULL);								// �������ģ���ļ�
    
    if(m_hComm == INVALID_HANDLE_VALUE) return FALSE;        // �򿪴���ʧ��
    
    GetCommState(m_hComm, &dcb);        // ȡDCB
    
    dcb.BaudRate = nBaudRate;
    dcb.ByteSize = nByteSize;
    dcb.Parity   = nParity;
    dcb.StopBits = (int)nStopBits;
	dcb.XonLim = 0;
	dcb.XoffLim = 0;
	dcb.XonChar = (char) 11;
	dcb.XoffChar= (char) 13;
    
    if(SetCommState(m_hComm, &dcb)==0)
	{
		return FALSE;	// ����DCB
	}        
    if(SetupComm(m_hComm, 8192, 8192)==0)
	{
		return FALSE;	// �������������������С
	}
    if(SetCommTimeouts(m_hComm, &timeouts)==0)
	{
		return FALSE;	// ���ó�ʱ
	}
	if(PurgeComm(m_hComm,PURGE_TXABORT|PURGE_RXABORT|PURGE_TXCLEAR|PURGE_RXCLEAR)==0)
	{					//��ɾ�
		return FALSE;
	}
//	::LeaveCriticalSection(&m_csComm);
	    
    return TRUE;
}
    
// �رմ���
BOOL CComm::CloseComm()
{
	BOOL b = CloseHandle(m_hComm);
	return b;
}

// д����
// pData: ��д�����ݻ�����ָ��
// nLength: ��д�����ݳ���
void CComm::WriteComm(void* pData, int nLength)
{
    DWORD dwNumWrite;    // ���ڷ��������ݳ���
    
    WriteFile(m_hComm, pData, (DWORD)nLength, &dwNumWrite, NULL);
}
    
// ������
// pData: ���������ݻ�����ָ��
// nLength: ������������ݳ���
// ����: ʵ�ʶ�������ݳ���
int CComm::ReadComm(void* pData, int nLength)
{
    DWORD dwNumRead;    // �����յ������ݳ���

    if( ReadFile(m_hComm, pData, (DWORD)nLength, &dwNumRead, NULL) == FALSE )
		return 0;

    return (int)dwNumRead;
}

void CComm::SetPortName(CString strPortName)
{
	this->m_strPort = strPortName;
}

void CComm::SetRate(CString strRate)
{
	this->m_strRate = strRate;
}

void CComm::ClearPort(BOOL bInBuffer)
{
	if( bInBuffer )
		PurgeComm(m_hComm,PURGE_RXCLEAR);
	else
		PurgeComm(m_hComm,PURGE_TXCLEAR);

}

CString CComm::GetPortName()
{
	return m_strPort;
}

//int CComm::ListPorts(CComboBox &combox)
//{
//	int j = 0;
//	for( int i = 1; i <= 256; i++ )
//	{
//		CString strPort;
//		strPort.Format( _T("COM%d"), i );
//		TCHAR wszPath[MAX_PATH];
//		if( ::QueryDosDevice( strPort, wszPath, MAX_PATH ) )
//		{
//			combox.InsertString( j++, strPort );
//		}
//	}
//	return j;
//}

void CComm::WriteUnicodeString(CString str)
{

    char *psz = NULL;
	psz = new char[ str.GetLength() + 2 ];
	if( psz == NULL )
		return;

	int iLength = ::WideCharToMultiByte( CP_ACP, 0, str, str.GetLength(),
								(char*)psz, str.GetLength(), NULL, NULL );

	DWORD dwNumWrite;    // ���ڷ��������ݳ���

	WriteFile(m_hComm, psz, (DWORD)iLength, &dwNumWrite, NULL);

	delete psz;
	psz = NULL;
}
