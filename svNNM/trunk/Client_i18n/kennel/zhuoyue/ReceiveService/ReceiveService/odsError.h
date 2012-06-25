////////////////////////////////////////////////////////////////////////
//
// odsError.h
//
//		ODS �йش�����Ķ����ļ�
//
// by Wang Yong Gang, 1999-10-15
//
////////////////////////////////////////////////////////////////////////


#if !defined(_ODS_20_ERROR_H_)
#define _ODS_20_ERROR_H_


#define LEN_ERR_MSG			200

////////////////////////////////////////////////////////////////////////
// �ṹ����

// ������Ϣ�ṹ
typedef struct tabSTODSERROR
{
	DWORD nCode;									// �������
	TCHAR strMsg[LEN_ERR_MSG + 1];					// ��������
}STODSERROR;

////////////////////////////////////////////////////////////////////////
// �궨��

// ������Ϣ�ʹ������
#include "odsErrorCode.h"

// �����궨��

#define FORMAT_WITH_SYSTEM					1
#define FORMAT_WITH_STRING					2
#define FORMAT_WITH_SYSTEM_AND_STRING		( FORMAT_WITH_SYSTEM | FORMAT_WITH_STRING )

////////////////////////////////////////////////////////////////////////
// ��������

//-------------------------------------------------------------
//	��ȱʡ������ȡ��ϵͳ�������Ӧ�Ĵ�����Ϣ( ��ʽ 1 )
//-------------------------------------------------------------
void odsGetSysErrMsg( DWORD dwErrCode, CString& strMsg );

//-------------------------------------------------------------
//	��ȱʡ������ȡ��ϵͳ�������Ӧ�Ĵ�����Ϣ( ��ʽ 1 )
//-------------------------------------------------------------
void odsGetSysErrMsg( DWORD dwErrCode, LPTSTR strBuf, int lenMsg );

//-------------------------------------------------------------
// �ڴ�����Ϣ�����������ʹ�����Ϣ��strMsgΪNULLʱֻ���������
//-------------------------------------------------------------
void odsMakeError( STODSERROR* pstError, DWORD nCode, LPCTSTR strMsg );

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
void odsFormatError( STODSERROR* pstError, UINT uFlag, LPCTSTR lpszFormat, ...);

#endif // _ODS_20_ERROR_H_