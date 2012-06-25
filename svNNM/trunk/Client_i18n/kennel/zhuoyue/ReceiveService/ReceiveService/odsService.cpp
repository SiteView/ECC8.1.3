//////////////////////////////////////////////////////////////////////////
//
// odsService.cpp
//
//		�й� NT ������Ƶ�ͨ�ú���ʵ��
//
// by Wang Yong Gang, 1999-10-27
// �� �ž� 1998/05/17 �Լ� VC Sample RpcSvc ���������
//
//////////////////////////////////////////////////////////////////////////

#include "StdAfx.h"
#include "odsService.h"

//-----------------------------------------------------------------------
// odsServiceMessageBox				- �����е����ĶԻ���
//-----------------------------------------------------------------------
void odsServiceMessageBox( LPCTSTR lpszText )
{
	OSVERSIONINFO	osVersionInfo;
	UINT			uType;

	uType = MB_OK | MB_ICONSTOP;

	osVersionInfo.dwOSVersionInfoSize = sizeof(OSVERSIONINFO);
	if (!GetVersionEx(&osVersionInfo))	
		return;

	if (osVersionInfo.dwMajorVersion < 4)
		uType |= MB_SERVICE_NOTIFICATION_NT3X;
	else
		uType |= MB_SERVICE_NOTIFICATION;

	MessageBox( NULL, lpszText, _T("SERVICE"), uType );
}

//-----------------------------------------------------------------------
// odsReportStatusToSCMgr				- ���Լ���״̬֪ͨ���������
//
// ������
//		hServiceStatus					- ����״̬���
//		dwCurrentState					- Ҫ���õ�״̬
//		DWORD dwErrorCode				- �����룬0 ��ʾ�޴���
//		dwWaitHint						- �������ȴ����
//		pstError						- ���մ�����Ϣָ�루����Ϊ NULL ��
// ���أ�
//		bSuccess						- �ɹ����
//-----------------------------------------------------------------------
BOOL odsReportStatusToSCMgr(SERVICE_STATUS_HANDLE hServiceStatus,
							DWORD dwCurrentState,
							DWORD dwErrorCode,
							DWORD dwWaitHint,
							STODSERROR* pstError )
{
    static DWORD dwCheckPoint = 0;    
	SERVICE_STATUS	ssStatus;

	memset( &ssStatus, 0, sizeof(ssStatus) );
 
	// 
	// * ע�⣺��������������û�� SERVICE_ACCEPT_SHUTDOWN�������������ʱ��
	// * ������ NT ���������Ŀ�����Ϣ������ʱ������ Cleanup �Ĺ������� ORACLE 
	// * ������û���ͷš������ ORACLE ����˵��������̱�������������κ󣬻�
	// * �ľ� ORACLE �ķ���˽��̣�ʹ ORACLE ̱����
	//
    if ( dwCurrentState == SERVICE_RUNNING )  
        ssStatus.dwControlsAccepted = SERVICE_ACCEPT_STOP | SERVICE_ACCEPT_SHUTDOWN;
    else
        ssStatus.dwControlsAccepted = 0;

	ssStatus.dwServiceType = SERVICE_WIN32_OWN_PROCESS;
    ssStatus.dwCurrentState = dwCurrentState;

	if (dwErrorCode != 0)
		ssStatus.dwWin32ExitCode = ERROR_SERVICE_SPECIFIC_ERROR;
	else
		ssStatus.dwWin32ExitCode = NO_ERROR;
	ssStatus.dwServiceSpecificExitCode = dwErrorCode;
    ssStatus.dwWaitHint = dwWaitHint;

    if ( ( dwCurrentState == SERVICE_RUNNING ) ||
         ( dwCurrentState == SERVICE_STOPPED ) ||
		 ( dwCurrentState == SERVICE_PAUSED ) )
        dwCheckPoint = 0;
    else
        dwCheckPoint++;
	ssStatus.dwCheckPoint = dwCheckPoint;

    // Report the status of the service to the service control manager.
    //
    if (!SetServiceStatus( hServiceStatus, &ssStatus)) 
	{
		odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_REPORTSTATUS );
        odsMakeError( pstError, ODS_ERR_REPORTSTATUS, NULL );
		return FALSE;
    }    
    else
		return TRUE;
}


//-----------------------------------------------------------------------
// odsInstallService				- ��װ����
// 
// ������
//		strComputerName				- ���������NULL��ʾ���ؼ������
//		strServiceName				- ��������
//		strServicePath				- ����·��
//		strDisplayName				- �������ʾ����
//		dwStartType					- �����������ʽ
//									- SERVICE_AUTO_START �� SERVICE_DEMAND_START
//		strDependencies				- �����������ϵ������Ϊ NULL ��
//		pstError					- ���մ�����Ϣָ�루����Ϊ NULL ��
// ���أ�
//		bSuccess					- �ɹ����
//-----------------------------------------------------------------------
BOOL odsInstallService( LPCTSTR strComputerName, 
					   LPCTSTR strServiceName, 
					   LPCTSTR strServicePath, 
					   LPCTSTR strDisplayName, 
					   DWORD dwStartType,
					   LPCTSTR strDependencies,
					   STODSERROR* pstError )
{
	BOOL		bRet = TRUE;
    SC_HANDLE   schService;
    SC_HANDLE   schSCManager;

    schSCManager = OpenSCManager(
                        strComputerName,        // machine (NULL == local)
                        NULL,                   // database (NULL == default)
                        SC_MANAGER_ALL_ACCESS   // access required
                        );
    if ( schSCManager )
    {
        schService = CreateService(
            schSCManager,               // SCManager database
            strServiceName,		        // name of service
            strDisplayName,				// name to display
            SERVICE_ALL_ACCESS,         // desired access
            SERVICE_WIN32_OWN_PROCESS,  // service type
            dwStartType,				// start type
            SERVICE_ERROR_NORMAL,       // error control type
            strServicePath,             // service's binary
            NULL,                       // no load ordering group
            NULL,                       // no tag identifier
            strDependencies,			// dependencies
            NULL,                       // LocalSystem account
            NULL);                      // no password

        if ( schService )
        {            
            CloseServiceHandle(schService);
        }
        else
        {
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_INSTSERVICE );
            odsMakeError( pstError, ODS_ERR_INSTSERVICE, NULL );
			bRet = FALSE;
        }

        CloseServiceHandle(schSCManager);
    }
    else
	{
		odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSCM );
		odsMakeError( pstError, ODS_ERR_OPENSCM, NULL );
		bRet = FALSE;
	}
	return bRet;
}


//----------------------------------------------------------------
// odsRemoveService					- ɾ��ĳ������ϵķ���
//
// ������
//		strComputerName				- ���������Ϊ NULL ʱ��ʾ���ؼ����
//		strServiceName				- ������
//		dwWaitTime					- �ȴ�ʱ��
//		pstError					- ������Ϣ�ṹָ�룬����Ϊ NULL
// ���أ�
//		bSuccess					- �ɹ����
//----------------------------------------------------------------
BOOL odsRemoveService( LPCTSTR strComputerName, LPCTSTR strServiceName, DWORD dwWaitTime, STODSERROR* pstError )
{
	BOOL			bRet = TRUE;
	BOOL			bStopped = FALSE;
    SC_HANDLE		schService;
    SC_HANDLE		schSCManager;
	SERVICE_STATUS	ssStatus;
	DWORD			dwSleepLeft = dwWaitTime;
	DWORD			dwSleep = min( 1000, dwSleepLeft );

    schSCManager = OpenSCManager(
                        strComputerName,        // machine (NULL == local)
                        NULL,                   // database (NULL == default)
                        SC_MANAGER_ALL_ACCESS   // access required
                        );
    if ( schSCManager )
    {
        schService = OpenService( schSCManager, strServiceName, SERVICE_ALL_ACCESS );

        if (schService)
        {
            // try to stop the service
            if ( ControlService( schService, SERVICE_CONTROL_STOP, &ssStatus ) )
            {
                do
				{
					Sleep( dwSleep );
					dwSleepLeft -= dwSleep;
					dwSleep = min( 1000, dwSleepLeft );

					QueryServiceStatus( schService, &ssStatus );
                
                    if ( ssStatus.dwCurrentState == SERVICE_STOPPED )
					{
						bStopped = TRUE;
						break;
					}
                }while( dwSleepLeft > 0 );
            }
            // now remove the service
            if( !DeleteService(schService) )
			{
				odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_DELSERVICE );
				odsMakeError( pstError, ODS_ERR_DELSERVICE, NULL );
				bRet = FALSE;
			}

            CloseServiceHandle(schService);
        }
        else
		{
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSERVICE );
            odsMakeError( pstError, ODS_ERR_OPENSERVICE, NULL );
			bRet = FALSE;
		}

        CloseServiceHandle(schSCManager);
    }
    else
	{
		odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSCM );
		odsMakeError( pstError, ODS_ERR_OPENSCM, NULL );
		bRet = FALSE;
	}

	return bRet;
}

//----------------------------------------------------------------
// odsGetServiceStatus			- ȡ��ָ�������ϵķ�������״̬
//
// ����:
//		lpszComputerName		- ����������ڵļ�������ƣ����Ϊ NULL
//								- ��ָ���ػ���
//		lpszServiceName			- �����������
//		lpdwServiceStatus		- ���ط�������״̬
//		pstError				- ������Ϣ�ṹָ�룬����Ϊ NULL//
// ����ֵ:
//		bSuccess					- �ɹ����
//----------------------------------------------------------------
BOOL odsGetServiceStatus( LPCTSTR strComputerName, LPCTSTR strServiceName, 
						  LPDWORD lpdwServiceStatus, STODSERROR* pstError )
{
	BOOL			bRet = TRUE;
	SC_HANDLE		schSCManager;			//������ƹ������ľ��
	SC_HANDLE		schService;				//�������ľ��
	SERVICE_STATUS	ssServiceStatus;		//����״̬�ṹ
	
	schSCManager = OpenSCManager(
                        strComputerName,				// machine (NULL == local)
                        NULL,							// database (NULL == default)
                        GENERIC_READ				    // access required
                        );
    if ( schSCManager )
    {
        schService = OpenService( schSCManager, strServiceName, GENERIC_READ );

        if (schService)
        {
            if ( QueryServiceStatus(schService, &ssServiceStatus) )
			{
				if (lpdwServiceStatus != NULL)
					(*lpdwServiceStatus) = ssServiceStatus.dwCurrentState;
			}
			else
			{
				odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_QUERYSERVICE );
				odsMakeError( pstError, ODS_ERR_QUERYSERVICE, NULL );
				bRet = FALSE;
			}
            CloseServiceHandle(schService);
        }
        else
		{
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSERVICE );
            odsMakeError( pstError, ODS_ERR_OPENSERVICE, NULL );
			bRet = FALSE;
		}

        CloseServiceHandle(schSCManager);
    }
    else
	{
		odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSCM );
		odsMakeError( pstError, ODS_ERR_OPENSCM, NULL );
		bRet = FALSE;
	}
	
	return bRet;
}

//----------------------------------------------------------------
// odsStartService					- ��������
//
// ����:
//		lpszComputerName			- ����������ڵļ�������ƣ����Ϊ NULL
//									- ��ָ���ؼ����
//		lpszServiceName				- Ҫ�������������
//		dwWaitTime					- �ȴ�����������ʱ�䣬��(����)Ϊ��λ
//		pstError					- ������Ϣ�ṹָ�룬����Ϊ NULL
//
// ����:
//		bSuccess					- �ɹ����
//----------------------------------------------------------------
BOOL odsStartService(LPCTSTR strComputerName, LPCTSTR strServiceName, 
				   DWORD dwWaitTime, STODSERROR* pstError)
{
	BOOL			bRet = TRUE;
	BOOL			bStarted = FALSE;
    SC_HANDLE		schService;
    SC_HANDLE		schSCManager;
	SERVICE_STATUS	ssStatus;
	DWORD			dwSleepLeft = dwWaitTime;
	DWORD			dwSleep = min( 1000, dwSleepLeft );

    schSCManager = OpenSCManager(
                        strComputerName,        // machine (NULL == local)
                        NULL,                   // database (NULL == default)
                        SC_MANAGER_ALL_ACCESS   // access required
                        );
    if ( schSCManager )
    {
        schService = OpenService( schSCManager, strServiceName, SERVICE_ALL_ACCESS );

        if (schService)
        {
            // try to stop the service
            if (  StartService( schService, 0, NULL ) )
            {
                do
				{
					Sleep( dwSleep );
					dwSleepLeft -= dwSleep;
					dwSleep = min( 1000, dwSleepLeft );

					QueryServiceStatus( schService, &ssStatus );
                
                    if ( ssStatus.dwCurrentState == SERVICE_RUNNING )
					{
						bStarted = TRUE;
						break;
					}
                }while( dwSleepLeft > 0 );
            }
			if ( !bStarted )
			{
				odsMakeError( pstError, ODS_ERR_STARTSERVICE, ODS_MSG_STARTSERVICE );
				bRet = FALSE;
			} 
            CloseServiceHandle(schService);
        }
        else
		{
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSERVICE );
            odsMakeError( pstError, ODS_ERR_OPENSERVICE, NULL );
			bRet = FALSE;
		}

        CloseServiceHandle(schSCManager);
    }
    else
	{
		odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSCM );
		odsMakeError( pstError, ODS_ERR_OPENSCM, NULL );
		bRet = FALSE;
	}

	return bRet;
}

//----------------------------------------------------------------
// odsStopService					- ֹͣ����
//
// ����:
//		lpszComputerName			- ����������ڵļ�������ƣ����Ϊ NULL
//									- ��ָ���ؼ����
//		lpszServiceName				- Ҫ�������������
//		dwWaitTime					- �ȴ�����������ʱ�䣬��(����)Ϊ��λ
//		pstError					- ������Ϣ�ṹָ�룬����Ϊ NULL
//
// ����:
//		bSuccess					- �ɹ����
//----------------------------------------------------------------
BOOL odsStopService( LPCTSTR strComputerName, LPCTSTR strServiceName, 
					DWORD dwWaitTime, STODSERROR* pstError )
{
	BOOL			bRet = TRUE;
	BOOL			bStopped = FALSE;
    SC_HANDLE		schService;
    SC_HANDLE		schSCManager;
	SERVICE_STATUS	ssStatus;
	DWORD			dwSleepLeft = dwWaitTime;
	DWORD			dwSleep = min( 1000, dwSleepLeft );

    schSCManager = OpenSCManager(
                        strComputerName,        // machine (NULL == local)
                        NULL,                   // database (NULL == default)
                        SC_MANAGER_ALL_ACCESS   // access required
                        );
    if ( schSCManager )
    {
        schService = OpenService( schSCManager, strServiceName, SERVICE_ALL_ACCESS );

        if (schService)
        {
            // try to stop the service
            if ( ControlService( schService, SERVICE_CONTROL_STOP, &ssStatus ) )
            {
                do
				{
					Sleep( dwSleep );
					dwSleepLeft -= dwSleep;
					dwSleep = min( 1000, dwSleepLeft );

					QueryServiceStatus( schService, &ssStatus );
                
                    if ( ssStatus.dwCurrentState == SERVICE_STOPPED )
					{
						bStopped = TRUE;
						break;
					}
                }while( dwSleepLeft > 0 );                
            }
			if ( !bStopped )
			{
				odsMakeError( pstError, ODS_ERR_STOPSERVICE, ODS_MSG_STOPSERVICE );
				bRet = FALSE;
			} 
            CloseServiceHandle(schService);
        }
        else
		{
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSERVICE );
            odsMakeError( pstError, ODS_ERR_OPENSERVICE, NULL );
			bRet = FALSE;
		}

        CloseServiceHandle(schSCManager);
    }
    else
	{
		odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_OPENSCM );
		odsMakeError( pstError, ODS_ERR_OPENSCM, NULL );
		bRet = FALSE;
	}

	return bRet;
}