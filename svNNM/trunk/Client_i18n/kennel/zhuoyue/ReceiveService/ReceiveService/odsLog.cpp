//////////////////////////////////////////////////////////////////////////
//
// odsLog.cpp 
//
//		�йس�����־��ʹ�����ݿ���־��NTϵͳ��־���ַ�ʽ���Ĳ���
//
// by Wang Yong Gang, 1999-10-27
// �� �ž� 1998/05/31 ���������
//
//////////////////////////////////////////////////////////////////////////


#include "StdAfx.h"
#include "odsLog.h"

//-------------------------------------------------------------------
// odsAddToSystemLog			- �� NT ����־��������һ����Ϣ
//
// ������
//		strSourceName			- �¼�Դ����
//		strMsg					- ��Ϣ����
//		wType					- ���ͣ�����Ϊ���¼���
//								- EVENTLOG_ERROR_TYPE
//								- EVENTLOG_WARNING_TYPE
//								- EVENTLOG_INFORMATION_TYPE
//								- EVENTLOG_AUDIT_SUCCESS
//								- EVENTLOG_AUDIT_FAILURE
//-------------------------------------------------------------------
void odsAddToSystemLog( LPCTSTR strSourceName, LPCTSTR strMsg, WORD wType )
{    
    HANDLE  hEventSource;
 
    // Use event logging to log the error.
    //
    hEventSource = RegisterEventSource(NULL, strSourceName);
        
    if (hEventSource != NULL) 
	{
        ReportEvent(hEventSource,	// handle of event source
            wType,					// event type
            0,						// event category
            0,						// event ID
            NULL,					// current user's SID
            1,						// strings in lpszStrings
            0,						// no bytes of raw data
            &strMsg,				// array of error strings
            NULL);					// no raw data

        DeregisterEventSource(hEventSource);       
    }
}
