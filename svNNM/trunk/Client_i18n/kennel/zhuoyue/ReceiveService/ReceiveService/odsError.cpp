//////////////////////////////////////////////////////////////
//
// odsError.cpp
//
//		�ʹ�������صĺ���
//
// by Wang Yong Gang, 1999-10-15
// �� �ž� 1998/05/06 �汾���������
//
//////////////////////////////////////////////////////////////

#include "StdAfx.h"
#include "odsError.h"

//-------------------------------------------------------------
//	��ȱʡ������ȡ��ϵͳ�������Ӧ�Ĵ�����Ϣ( ��ʽ 1 )
//-------------------------------------------------------------
void odsGetSysErrMsg( DWORD dwErrCode, CString& strMsg )
{
	LANGID dwDefaultLanguageID;		//ϵͳȱʡ������ID
	TCHAR strBuf[ LEN_ERR_MSG + 1 ];
	memset( strBuf, 0, sizeof(TCHAR) * ( LEN_ERR_MSG + 1 ) );

	dwDefaultLanguageID = GetSystemDefaultLangID();
	
	DWORD dwReturnCode = FormatMessage
				(
				 FORMAT_MESSAGE_FROM_SYSTEM |
				 FORMAT_MESSAGE_MAX_WIDTH_MASK,	//Flags
				 NULL,							//No message source
				 dwErrCode,						//Message ID
				 dwDefaultLanguageID,			//Language ID
				 strBuf,						//Pointer of message buffer
				 LEN_ERR_MSG,					//Message buffer size
				 NULL							//No arguments
				);

	if( dwReturnCode == 0 )
		strMsg = _T("ȡ��ϵͳ������Ϣʧ�ܣ���û����Ӧ�Ĵ�����Ϣ!");
	else
		strMsg = strBuf;
}

//-------------------------------------------------------------
//	��ȱʡ������ȡ��ϵͳ�������Ӧ�Ĵ�����Ϣ( ��ʽ 2 )
//-------------------------------------------------------------
void odsGetSysErrMsg( DWORD dwErrCode, LPTSTR strBuf, int lenMsg )
{
	LANGID dwDefaultLanguageID;		//ϵͳȱʡ������ID
	if (strBuf == NULL) 
		return;

	memset( strBuf, 0, lenMsg );

	dwDefaultLanguageID = GetSystemDefaultLangID();
	
	DWORD dwReturnCode = FormatMessage
				(
				 FORMAT_MESSAGE_FROM_SYSTEM |
				 FORMAT_MESSAGE_MAX_WIDTH_MASK,	//Flags
				 NULL,							//No message source
				 dwErrCode,						//Message ID
				 dwDefaultLanguageID,			//Language ID
				 strBuf,						//Pointer of message buffer
				 lenMsg,						//Message buffer size
				 NULL							//No arguments
				);

	if( dwReturnCode == 0 )
		lstrcpyn( strBuf, _T("ȡ��ϵͳ������Ϣʧ�ܣ���û����Ӧ�Ĵ�����Ϣ!"), lenMsg );	
}

//-------------------------------------------------------------
// �ڴ�����Ϣ�����������ʹ�����Ϣ��strMsgΪNULLʱֻ���������
//-------------------------------------------------------------
void odsMakeError( STODSERROR* pstError, DWORD nCode, LPCTSTR strMsg )
{
	if (pstError == NULL)
		return;
	pstError->nCode = nCode;
	if (strMsg != NULL)
		lstrcpyn( pstError->strMsg, strMsg, LEN_ERR_MSG );
}

//-------------------------------------------------------------
// ��ָ���ķ�ʽ���ô�����Ϣ����������Ϣ�ṹ
//
// uFlag ����ȡ��ֵ�У�
//		FORMAT_WITH_SYSTEM						- ȡϵͳ������Ϣ
//												- ( pstError->nCode �����û���ϵͳ������� )
//		FORMAT_WITH_STRING						- �Լ����ɴ�����Ϣ
//												- ( pstError->nCode ���� )
//		FORMAT_WITH_SYSTEM_AND_STRING			- ��ϵͳ������Ϣ���Լ����ɵĴ�����Ϣ�������
//												- ( pstError->nCode �����û���ϵͳ������� )
// ���� lpszFormat, ... �Ķ���ͬ printf �еĸ�ʽ������
//-------------------------------------------------------------
void odsFormatError( STODSERROR* pstError, UINT uFlag, LPCTSTR lpszFormat, ...)
{ 
	TCHAR lpszTempBuffer[LEN_ERR_MSG + 1];
	TCHAR lpszTempSystemBuffer[LEN_ERR_MSG + 1];
	
	va_list arg_list;

	if (pstError == NULL)
		return;

	// pstError->nCode = 0;
	pstError->strMsg[0] = _T('\0');

	lpszTempBuffer[0] = _T('\0');
	lpszTempSystemBuffer[0] = _T('\0');

	if (uFlag & FORMAT_WITH_STRING)
	{
		if (lpszFormat != NULL)
		{
			va_start(arg_list, lpszFormat);
			_vsntprintf(lpszTempBuffer, LEN_ERR_MSG, lpszFormat, arg_list);
			lpszTempBuffer[LEN_ERR_MSG] = _T('\0');
			va_end(arg_list);
		}
		if (uFlag & FORMAT_WITH_SYSTEM)			
			lstrcat(lpszTempBuffer, _T(":"));
	}

	if (uFlag & FORMAT_WITH_SYSTEM)
	{
		pstError->nCode = GetLastError();
		odsGetSysErrMsg( pstError->nCode, lpszTempSystemBuffer, LEN_ERR_MSG );				
	}

	if ( lstrlen( lpszTempBuffer ) + lstrlen( lpszTempSystemBuffer ) > LEN_ERR_MSG )
		lpszTempSystemBuffer[LEN_ERR_MSG - lstrlen(lpszTempBuffer)] = _T('\0');	
	lstrcat(lpszTempBuffer, lpszTempSystemBuffer);	

	lstrcpyn(pstError->strMsg, lpszTempBuffer, LEN_ERR_MSG);
}
