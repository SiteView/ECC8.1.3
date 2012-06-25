//////////////////////////////////////////////////////////////////////////
//
// odsLog.h 
//
//		�йس�����־��ʹ�����ݿ���־��NTϵͳ��־���ַ�ʽ���Ĳ���
//
// by Wang Yong Gang, 1999-10-27
// �� �ž� 1998/05/31 ���������
//
//////////////////////////////////////////////////////////////////////////

#if !defined(_ODS_20_LOG_H_)
#define _ODS_20_LOG_H_

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
void odsAddToSystemLog( LPCTSTR strSourceName, LPCTSTR strMsg, WORD wType );

#endif // _ODS_20_LOG_H_