////////////////////////////////////////////////////////////////////////
//
// odsFileSystem.h
//
//		ODS �й��ļ�ϵͳ�����Ķ����ļ�
//
// by Wang Yong Gang, 1999-10-16
//
////////////////////////////////////////////////////////////////////////


#if !defined(_ODS_20_FILESYSTEM_H_)
#define _ODS_20_FILESYSTEM_H_

#include "odsError.h"

////////////////////////////////////////////////////////////////////////
// ���Ͷ���

//���� odsBrowseDirectory �Ļص���������
typedef BOOL (*FILEBROWSEFUNC) (LPCTSTR lpszFileName, STODSERROR* pstError );

//Ŀ¼���Խṹ����
typedef struct tagSTDIRPROPERTY
{
	 DWORD		dwDirNum;			//������Ŀ¼�ĸ���
	 DWORD		dwFileNum;			//�����ļ��ĸ���
	 __int64	i64Size;			//Ŀ¼�Ĵ�С�����ֽ�Ϊ��λ
}STDIRPROPERTY;


////////////////////////////////////////////////////////////////////////
// ����˵��

//------------------------------------------------------------
// ɾ��·�������ķ�б��(��ʽ1)
//------------------------------------------------------------
void odsRemoveLastSlash( LPTSTR lpszDirectory );

//------------------------------------------------------------
// ɾ��·�������ķ�б��(��ʽ2)
//------------------------------------------------------------
void odsRemoveLastSlash( CString& strDirectory );

//------------------------------------------------------------
// �ж���·�������ļ����ϳɵ�ȫ���ļ��������Ƿ�
// ������ϵͳ�������󳤶�
//------------------------------------------------------------
BOOL odsIsFullNameOverflow( LPCTSTR lpszDirectoryName, LPCTSTR lpszFileName );

//------------------------------------------------------------
// ���ļ�ȫ·�����з����������·�����ļ���
//------------------------------------------------------------ 
void odsGetFileNameFromPath( LPCTSTR lpszFileFullPath, LPTSTR lpszFileName );

//------------------------------------------------------------
// �ж�����Ŀ¼�Ƿ���ͬһ����������
//------------------------------------------------------------
BOOL odsIsDirInSameDriver( LPCTSTR lpszDirectory1, LPCTSTR lpszDirectory2 );

//------------------------------------------------------------
// ȡ��ָ��·���ĸ�Ŀ¼
//------------------------------------------------------------
void odsGetRootDir( LPCTSTR lpszDirectoryName, LPTSTR lpszRootDirectory );

//------------------------------------------------------------
// �ж�ָ�����ļ��Ƿ����
//------------------------------------------------------------
BOOL odsIsFileExist( LPCTSTR lpszFileName );

//------------------------------------------------------------
// �ж�ָ����Ŀ¼�Ƿ����
//------------------------------------------------------------
BOOL odsIsDirExist( LPCTSTR lpszDirectoryName );

//------------------------------------------------------------
// ȡָ���ļ��Ĵ�С( ���� 0xFFFFFFFF ��ʾʧ�� )��
// lpFileSizeHigh ����Ϊ NULL
//------------------------------------------------------------
DWORD odsGetFileSize( LPCTSTR lpszFileName, LPDWORD lpFileSizeHigh );

//------------------------------------------------------------
// �ﵽĿ¼���ڵ�Ӳ��ʣ��ռ�Ĵ�С��ʧ���򷵻ظ���
//------------------------------------------------------------
__int64 odsGetDiskFreeSpace( LPCTSTR strDir );

//------------------------------------------------------------
// ��Ŀ¼�е����ݴ�ԭʼĿ¼������Ŀ��Ŀ¼���ҿɰ�����Ŀ¼
//------------------------------------------------------------
BOOL odsCopyDirectory
	( 
		LPCTSTR		lpszSourceDirectory,			// ԭʼĿ¼
		LPCTSTR		lpszDestinationDirectory,		// Ŀ��Ŀ¼
		BOOL		bIncludeSubDir,					// �Ƿ������Ŀ¼
		BOOL		bOverwriteExist,				// �Ƿ񸲸����е�Ŀ¼���ļ�
		STODSERROR* pstError						// ���ش�����Ϣ
	);

//------------------------------------------------------------
// ɾ��ָ����Ŀ¼
//	bIncludeSubDir = TRUE,  ɾ��ָ����Ŀ¼����������Ŀ¼�����������ļ���
//	bIncludeSubDir = FALSE, ��ɾ��ָ��Ŀ¼�е��ļ�����ɾ����Ŀ¼����Ŀ¼
//							�ڵ��ļ���Ҳ��ɾ��ָ��Ŀ¼����
//------------------------------------------------------------
BOOL odsRemoveDirectory( LPCTSTR lpszDirectoryName, BOOL bIncludeSubDir, STODSERROR* pstError );

//------------------------------------------------------------
// �ƶ�Ŀ¼ - bDirect ָ����ֱ���ƶ������ȸ�����ɾ��
//------------------------------------------------------------
BOOL odsMoveDirectory
		( 
			LPCTSTR lpszSourceDirectory, 
			LPCTSTR lpszDestinationDirectory, 
			BOOL bDirect,
			STODSERROR* pstError 
		);

//------------------------------------------------------------ 
// ����Ŀ¼�������ļ�������
//	bIncludeSubDir			�Ƿ������Ŀ¼�ڵ��ļ�
//------------------------------------------------------------ 
BOOL odsSetFileAttributesInDir( LPCTSTR lpszDirectoryName, DWORD dwFileAttributes, BOOL bIncludeSubDir, STODSERROR* pstError );

//------------------------------------------------------------ 
// ȡ��ָ��Ŀ¼������
//		������Ŀ¼��������Ŀ¼�ĸ������ļ��ĸ������Լ�Ŀ¼���ֽ�Ϊ��λ�Ĵ�С
//------------------------------------------------------------ 
BOOL odsGetDirProperty( LPCTSTR lpszDirectoryName, BOOL bIncludeSubDir, 
					 STDIRPROPERTY* lpDirectoryProperty, STODSERROR* pstError );

//------------------------------------------------------------
// ���Ŀ¼������Ŀ¼��ָ�����͵��ļ�
// ����˵��:
// 	lpszDirectoryName	Ҫ�����Ŀ¼����
//	dwFileType			Ҫ����ļ������ͣ���Ϊ����ֵ�����ǵ����
//							FILE_ATTRIBUTE_ARCHIVE 
//							FILE_ATTRIBUTE_COMPRESSED 
//							FILE_ATTRIBUTE_DIRECTORY 
//							FILE_ATTRIBUTE_HIDDEN 
//							FILE_ATTRIBUTE_NORMAL 
//							FILE_ATTRIBUTE_OFFLINE 
//							FILE_ATTRIBUTE_READONLY 
//							FILE_ATTRIBUTE_SYSTEM 
//							FILE_ATTRIBUTE_TEMPORARY 
//	bIncludeSubDir		�Ƿ������Ŀ¼���ļ��ı�־
//							TRUE	������Ŀ¼�е��ļ�
//							FALSE	��������Ŀ¼�е��ļ�
//	FileBrowseFunc		�ص�������ָ�룬����������У���������Ὣ�ҵ�����������
//						�ļ��������˺������˻ص�����������Ӧ�Ĳ�����������Ϊ NULL
//	pstError			������Ϣ�ṹָ�룬����Ϊ NULL
//------------------------------------------------------------ 
BOOL odsBrowseDirectory
		(
			LPCTSTR lpszDirectoryName, 
			DWORD dwFileType, 
			BOOL bIncludeSubDir, 
			FILEBROWSEFUNC FileBrowseFunc, 
			STODSERROR* pstError
		);


#endif // _ODS_20_FILESYSTEM_H_