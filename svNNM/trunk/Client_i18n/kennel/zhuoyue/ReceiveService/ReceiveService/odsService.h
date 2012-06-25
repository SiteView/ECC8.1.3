//////////////////////////////////////////////////////////////////////////
//
// odsService.h 
//
//		�й� NT ������Ƶ�ͨ�ú�������
//
// by Wang Yong Gang, 1999-10-27
// �� �ž� 1998/05/17 �Լ� VC Sample RpcSvc ���������
//
//////////////////////////////////////////////////////////////////////////

#if !defined(_ODS_20_SERVICE_H_)
#define _ODS_20_SERVICE_H_

#include "odsError.h"

//-----------------------------------------------------------------------
// odsServiceMessageBox				- �����е����ĶԻ���
//-----------------------------------------------------------------------
void odsServiceMessageBox( LPCTSTR lpszText );

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
						  LPDWORD lpdwServiceStatus, STODSERROR* pstError );

//-----------------------------------------------------------------------
// odsReportStatusToSCMgr				- ���Լ���״̬֪ͨ�������̨
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
							STODSERROR* pstError );

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
					   STODSERROR* pstError );

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
BOOL odsRemoveService( LPCTSTR strComputerName, LPCTSTR strServiceName, DWORD dwWaitTime, STODSERROR* pstError );

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
				   DWORD dwWaitTime, STODSERROR* pstError);

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
					DWORD dwWaitTime, STODSERROR* pstError );

#endif // _ODS_20_SERVICE_H_