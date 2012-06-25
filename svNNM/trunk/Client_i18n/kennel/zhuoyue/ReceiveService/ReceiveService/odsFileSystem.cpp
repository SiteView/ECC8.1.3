//////////////////////////////////////////////////////////////
//
// odsFileSystem.cpp
//
//		���ļ�ϵͳ��صĺ���
//
// by Wang Yong Gang, 1999-10-16
// �� �ž� 1998/05/20 �汾���������
//
//////////////////////////////////////////////////////////////

#include "StdAfx.h"
#include "odsFileSystem.h"

//------------------------------------------------------------
// ɾ��·�������ķ�б��(��ʽ1)
//------------------------------------------------------------
void odsRemoveLastSlash( LPTSTR lpszDirectory )
{
	int	i = lstrlen(lpszDirectory);
	if (i == 0)
		return;
 
	// ������һ���ַ���(\)����ɾ����	
	if ( lpszDirectory[i - 1] == _T('\\') )
		lpszDirectory[i - 1] = _T('\0');	
}

//------------------------------------------------------------
// ɾ��·�������ķ�б��(��ʽ2)
//------------------------------------------------------------
void odsRemoveLastSlash( CString& strDirectory )
{
	TCHAR* p = strDirectory.GetBuffer(0);
	odsRemoveLastSlash( p );
	strDirectory.ReleaseBuffer();
}

//------------------------------------------------------------
// �ж���·�������ļ����ϳɵ�ȫ���ļ��������Ƿ񳬹���ϵͳ�������󳤶�
//------------------------------------------------------------
BOOL odsIsFullNameOverflow( LPCTSTR lpszDirectoryName, LPCTSTR lpszFileName )
{	
	return ( lstrlen(lpszDirectoryName) + lstrlen(lpszFileName) + 1
				> _MAX_PATH ) ? TRUE : FALSE;
}

//------------------------------------------------------------
// ���ļ�ȫ·�����з����������·�����ļ���
//------------------------------------------------------------ 
void odsGetFileNameFromPath( LPCTSTR lpszFileFullPath, LPTSTR lpszFileName )
{
	TCHAR szFileName[_MAX_PATH];
	TCHAR szExtName[_MAX_PATH];

	//���ļ�ȫ·���з�����ļ�������չ��
	_tsplitpath( lpszFileFullPath, NULL, NULL, szFileName, szExtName );
	
	//���ļ�������չ���ϳ��ļ���
	wsprintf( lpszFileName, _T("%s%s"), szFileName, szExtName );	
}

//------------------------------------------------------------
// �ж�����Ŀ¼�Ƿ���ͬһ����������
//------------------------------------------------------------
BOOL odsIsDirInSameDriver( LPCTSTR lpszDirectory1, LPCTSTR lpszDirectory2 )
{
	TCHAR	szDriver1[_MAX_DRIVE + 1];
	TCHAR	szDriver2[_MAX_DRIVE + 1];

	// ��·����ȡ����������
	_tsplitpath( lpszDirectory1, szDriver1, NULL, NULL, NULL );
	_tsplitpath( lpszDirectory2, szDriver2, NULL, NULL, NULL );

	//�Ƚ����������Ƿ����
	if ( _tcsicmp(szDriver1, szDriver2) == 0 )
		return TRUE;
	else
		return FALSE;
}

//------------------------------------------------------------
// ȡ��ָ��·���ĸ�Ŀ¼
//------------------------------------------------------------
void odsGetRootDir( LPCTSTR lpszDirectoryName, LPTSTR lpszRootDirectory )
{
	TCHAR	szDrive[_MAX_DRIVE];
	szDrive[0] = _T('\0');

	//�ֽ�Ŀ¼
	_tsplitpath( lpszDirectoryName, szDrive, NULL, NULL, NULL );

	//���ɸ�Ŀ¼
	wsprintf(lpszRootDirectory, _T("%s\\"), szDrive);
}

//------------------------------------------------------------
// �ж�ָ�����ļ��Ƿ����
//------------------------------------------------------------
BOOL odsIsFileExist( LPCTSTR lpszFileName )
{
	WIN32_FIND_DATA	FindData;
	HANDLE			hFindData;

	//ִ�в��Ҳ���
	hFindData = FindFirstFile
			 (
			  lpszFileName,		//Ҫ���ҵ��ļ�����
			  &FindData			//���صĲ��ҽ��
			 );

	//���û���ҵ�
	if (hFindData == INVALID_HANDLE_VALUE)
		return FALSE;

	//�رղ��Ҿ��
	FindClose(hFindData);

	//�ж��ҵ����ļ������Ƿ����ļ�
	if (FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
		return FALSE;

	return TRUE;
}

//------------------------------------------------------------
// �ж�ָ����Ŀ¼�Ƿ����
//------------------------------------------------------------
BOOL odsIsDirExist( LPCTSTR lpszDirectoryName )
{
	WIN32_FIND_DATA	FindData;
	HANDLE				hFindData;
	TCHAR				szRootDirectory[_MAX_DRIVE + 2];

	//�ж��Ƿ��Ǹ�Ŀ¼
	odsGetRootDir( lpszDirectoryName, szRootDirectory );
	if (_tcsicmp(lpszDirectoryName, szRootDirectory) == 0)
		return TRUE; 

	//ִ�в��Ҳ���
	hFindData = FindFirstFile
			 (
			  lpszDirectoryName,	//Ҫ���ҵ�Ŀ¼����
			  &FindData				//���صĲ��ҽ��
			 );

	//���û���ҵ�
	if (hFindData == INVALID_HANDLE_VALUE)
		return FALSE;

	//�رղ��Ҿ��
	FindClose(hFindData);

	//�ж��ҵ����ļ��Ƿ���Ŀ¼
	if (FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
		return TRUE;
	else
		return FALSE;
}


//------------------------------------------------------------
// ȡָ���ļ��Ĵ�С( ���� 0xFFFFFFFF ��ʾʧ�� )��
//		lpFileSizeHigh ����Ϊ NULL
//------------------------------------------------------------
DWORD odsGetFileSize( LPCTSTR lpszFileName, LPDWORD lpFileSizeHigh )
{
	WIN32_FIND_DATA	FindData;
	HANDLE			hFindData;

	//ִ�в��Ҳ���
	hFindData = FindFirstFile
			 (
			  lpszFileName,		//Ҫ���ҵ��ļ�����
			  &FindData			//���صĲ��ҽ��
			 );

	//���û���ҵ�
	if ( hFindData == INVALID_HANDLE_VALUE )
	{
		if (lpFileSizeHigh != NULL)
			(*lpFileSizeHigh) = 0xFFFFFFFF;
		return 0xFFFFFFFF;
	}

	//�رղ��Ҿ��
	FindClose(hFindData);

	// ����ҵ�����Ŀ¼��Ҳ����ʧ��
	if ( FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY )
	{
		if (lpFileSizeHigh != NULL)
			(*lpFileSizeHigh) = 0xFFFFFFFF;
		return 0xFFFFFFFF;
	}

	if (lpFileSizeHigh != NULL)
		(*lpFileSizeHigh) = FindData.nFileSizeHigh;
	return FindData.nFileSizeLow;
}

//------------------------------------------------------------
// �ﵽĿ¼���ڵ�Ӳ��ʣ��ռ�Ĵ�С��ʧ���򷵻ظ���
//------------------------------------------------------------
__int64 odsGetDiskFreeSpace( LPCTSTR strDir )
{
	ULARGE_INTEGER ul1, ul2;
	if (!GetDiskFreeSpaceEx( strDir, &ul1, &ul2, NULL ))
		return (__int64)( -1 );
	else
		return (__int64)( ul1.QuadPart );
}

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
	)
{
	LPTSTR				lpszExistFileName;
	LPTSTR				lpszNewFileName;
	LPTSTR				lpszPath;
	TCHAR				lpszBuffer[ 3 * _MAX_PATH ];

	lpszPath = lpszBuffer;
	lpszExistFileName = lpszBuffer + _MAX_PATH;
	lpszNewFileName = lpszBuffer + _MAX_PATH * 2;

	BOOL				bReturnCode = TRUE;
	WIN32_FIND_DATA		FindData;
	HANDLE				hFindData;

	//�ж�ԭʼ·���Ƿ����
	if (!odsIsDirExist(lpszSourceDirectory))
	{
		odsMakeError( pstError, ODS_ERR_NODIR, ODS_MSG_NODIR );
		return FALSE;
	}
 
	//���ɲ��ҵ��ļ���
	wsprintf( lpszPath, _T("%s\\*.*"), lpszSourceDirectory );

	//�ж�Ŀ��Ŀ¼�Ƿ���ڣ�����������򴴽���
	if (!odsIsDirExist(lpszDestinationDirectory))
	{
		if (!CreateDirectory(lpszDestinationDirectory, NULL))
		{
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_CREATEDIR );
			odsMakeError( pstError, ODS_ERR_CREATEDIR, NULL );
			return FALSE;
		}
	}
	else if ( !bOverwriteExist )
	{
		odsMakeError( pstError, ODS_ERR_OVERWRITE, ODS_MSG_OVERWRITE );
		return FALSE;
	}

	//ִ�в���
	hFindData = FindFirstFile(lpszPath, &FindData);
	if (hFindData == INVALID_HANDLE_VALUE)
	{
		odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_BROWSEDIR );
		odsMakeError( pstError, ODS_ERR_BROWSEDIR, NULL );		
		return FALSE;
	}

	bReturnCode = TRUE;

	do
	{
		//�ж��Ƿ��Ǳ���Ŀ¼
		if (lstrcmp(FindData.cFileName, _T(".")) == 0)
			continue;

		//�ж��Ƿ����ϼ�Ŀ¼
		if (lstrcmp(FindData.cFileName, _T("..")) == 0)
			continue;

		//�ж�Ҫ���ɵ��ļ����ĳ���
		if( odsIsFullNameOverflow(lpszDestinationDirectory, FindData.cFileName) )
		{
			odsMakeError( pstError, ODS_ERR_NAMEOVERFLOW, ODS_MSG_NAMEOVERFLOW );
			bReturnCode = FALSE;
			break;
		}

		//���ɲ鵽���ļ���
		wsprintf(lpszExistFileName, _T("%s\\%s"),lpszSourceDirectory, FindData.cFileName);
		//����Ҫ�������ļ���
		wsprintf(lpszNewFileName, _T("%s\\%s"), lpszDestinationDirectory, FindData.cFileName);

		//���ݲ鵽�ļ������ͣ�����ͬ�Ĳ���
		if (FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
		{
			if ( bIncludeSubDir )
				bReturnCode = odsCopyDirectory
						  (
						   lpszExistFileName, 
						   lpszNewFileName, 
						   bIncludeSubDir, 
						   bOverwriteExist,
						   pstError
						  );
		}
		else
		{
			if (! CopyFile( lpszExistFileName, lpszNewFileName, !bOverwriteExist ) )
			{
				odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_COPYDIR );
				odsMakeError( pstError, ODS_ERR_COPYDIR, NULL );
				bReturnCode = FALSE;
			}

			// Ϊ��ֹ��ֻ�������ϸ��Ƶ�Ӳ�̺��ļ�����ֻ�����Զ��޷��޸�
			// ��Ҫ�ڴ˴��жϲ�ȥ���ļ���ֻ������
			DWORD dwAttr = GetFileAttributes( lpszNewFileName );
			if ( dwAttr & FILE_ATTRIBUTE_READONLY )
				SetFileAttributes( lpszNewFileName, dwAttr & (~FILE_ATTRIBUTE_READONLY) );
		}

		if (!bReturnCode)
			break;
	}while ( FindNextFile(hFindData, &FindData) );

	// �رղ��Ҿ��
	FindClose(hFindData);
 	
	return(bReturnCode);
}


//------------------------------------------------------------
// ɾ��ָ����Ŀ¼
//	bIncludeSubDir = TRUE,  ɾ��ָ����Ŀ¼����������Ŀ¼�����������ļ���
//	bIncludeSubDir = FALSE, ��ɾ��ָ��Ŀ¼�е��ļ�����ɾ����Ŀ¼����Ŀ¼
//							�ڵ��ļ���Ҳ��ɾ��ָ��Ŀ¼����
//------------------------------------------------------------
BOOL odsRemoveDirectory( LPCTSTR lpszDirectoryName, BOOL bIncludeSubDir, STODSERROR* pstError )
{
	BOOL				bReturnCode = TRUE;	
	WIN32_FIND_DATA		FindData;
	HANDLE				hFindData;	
	TCHAR				szPath[_MAX_PATH];

	//�ж�Ŀ¼�Ƿ����
	if( !odsIsDirExist(lpszDirectoryName) )
	{
		odsMakeError( pstError, ODS_ERR_NODIR, ODS_MSG_NODIR );
		return(FALSE);
	}

	//���ɲ��ҵ��ļ���
	wsprintf(szPath, _T("%s\\*.*"), lpszDirectoryName);
	
	//���в��Ҳ���
	hFindData = FindFirstFile
				 (
				  szPath,
				  &FindData
				 );

	//����ҵ��ļ�
	if(hFindData != INVALID_HANDLE_VALUE)
	{
		do
		{
			//�ж��Ƿ��Ǳ���Ŀ¼
			if (lstrcmp(FindData.cFileName, _T(".")) == 0)
				continue;

			//�ж��Ƿ����ϼ�Ŀ¼
			if (lstrcmp(FindData.cFileName, _T("..")) == 0)
				continue;

			//����Ҫɾ�����ļ���
			wsprintf(szPath, _T("%s\\%s"), lpszDirectoryName, FindData.cFileName);

			//�ж��ļ�������
			if (FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
			{				
				if ( bIncludeSubDir )
					bReturnCode = odsRemoveDirectory(szPath, TRUE, pstError);
			}
			else
			{
				if (! DeleteFile( szPath ) )
				{
					odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_REMOVEDIR );
					odsMakeError( pstError, ODS_ERR_REMOVEDIR, NULL );
					bReturnCode = FALSE;
				}				
			}

			if ( !bReturnCode )			
				break;			
		}while (FindNextFile(hFindData, &FindData));

		//�رղ��Ҿ��
		FindClose(hFindData);		
	}	// end of if (hFindData != INVALID_HANDLE_VALUE)

	if (bReturnCode )
	{
		//ɾ��ָ��Ŀ¼����
		if ( bIncludeSubDir )
			bReturnCode = RemoveDirectory(lpszDirectoryName);
		if (!bReturnCode)
		{
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_REMOVEDIR );
			odsMakeError( pstError, ODS_ERR_REMOVEDIR, NULL );
		}
	}
	
	return(bReturnCode);
}

//------------------------------------------------------------ 
// ����Ŀ¼�������ļ�������
//	bIncludeSubDir			�Ƿ������Ŀ¼�ڵ��ļ�
//------------------------------------------------------------ 
BOOL odsSetFileAttributesInDir( LPCTSTR lpszDirectoryName, DWORD dwFileAttributes, BOOL bIncludeSubDir, STODSERROR* pstError )
{
	BOOL				bReturnCode = TRUE;	
	WIN32_FIND_DATA		FindData;
	HANDLE				hFindData;	
	TCHAR				szPath[_MAX_PATH];

	//�ж�Ŀ¼�Ƿ����
	if( !odsIsDirExist(lpszDirectoryName) )
	{
		odsMakeError( pstError, ODS_ERR_NODIR, ODS_MSG_NODIR );
		return(FALSE);
	}

	//���ɲ��ҵ��ļ���
	wsprintf(szPath, _T("%s\\*.*"), lpszDirectoryName);
	
	//���в��Ҳ���
	hFindData = FindFirstFile
				 (
				  szPath,
				  &FindData
				 );

	//����ҵ��ļ�
	if(hFindData != INVALID_HANDLE_VALUE)
	{
		do
		{
			//�ж��Ƿ��Ǳ���Ŀ¼
			if (lstrcmp(FindData.cFileName, _T(".")) == 0)
				continue;

			//�ж��Ƿ����ϼ�Ŀ¼
			if (lstrcmp(FindData.cFileName, _T("..")) == 0)
				continue;

			//����Ҫ�������Ե��ļ���
			wsprintf(szPath, _T("%s\\%s"), lpszDirectoryName, FindData.cFileName);

			//�ж��ļ�������
			if (FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
			{				
				if ( bIncludeSubDir )
					bReturnCode = odsSetFileAttributesInDir(szPath, dwFileAttributes, TRUE, pstError);
			}
			else
			{
				if (!SetFileAttributes( szPath, dwFileAttributes ))
				{
					odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_SETATTR );
					odsMakeError( pstError, ODS_ERR_SETATTR, NULL );
					bReturnCode = FALSE;
				}
			}

			if ( !bReturnCode )
				break;			
		}while (FindNextFile(hFindData, &FindData));

		//�رղ��Ҿ��
		FindClose(hFindData);		
	}	// end of if (hFindData != INVALID_HANDLE_VALUE)

	return(bReturnCode);
}

//------------------------------------------------------------
// �ƶ�Ŀ¼ - bDirect ָ����ֱ���ƶ������ȸ�����ɾ��
//------------------------------------------------------------
BOOL odsMoveDirectory
		( 
			LPCTSTR lpszSourceDirectory, 
			LPCTSTR lpszDestinationDirectory, 
			BOOL bDirect,
			STODSERROR* pstError 
		)
{
	BOOL	bReturnCode;

	//�ж�Ŀ¼�Ƿ����
	if( !odsIsDirExist(lpszSourceDirectory) )
	{
		odsMakeError( pstError, ODS_ERR_NODIR, ODS_MSG_NODIR );
		return(FALSE);
	}

	if ( bDirect && odsIsDirInSameDriver( lpszSourceDirectory, lpszDestinationDirectory) )
	{
		//ִ���ƶ�����
		bReturnCode = MoveFile
			   (
			    lpszSourceDirectory,
				lpszDestinationDirectory
			   );
		if ( !bReturnCode )
		{
			odsFormatError( pstError, FORMAT_WITH_SYSTEM_AND_STRING, ODS_MSG_MOVEDIR );
			odsMakeError( pstError, ODS_ERR_MOVEDIR, NULL );
		}
	}
	else
	{
		//ִ�п�������
		bReturnCode = odsCopyDirectory
			   (
				(LPSTR)lpszSourceDirectory,
				(LPSTR)lpszDestinationDirectory,
				TRUE,									// include sub dir
				TRUE,									// overwrite
				pstError
			   );

		if ( bReturnCode )
		{
			//ִ��ɾ������
			bReturnCode = odsRemoveDirectory
			   (
			    (LPSTR)lpszSourceDirectory, 
				TRUE,
				pstError
			   );
		}
	}

	return(bReturnCode);
}


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
		)
{
	BOOL				bReturnCode = TRUE;
	WIN32_FIND_DATA		FindData;
	HANDLE				hFindData;	
	TCHAR				szPath[_MAX_PATH];

	pstError->nCode = 0; pstError->strMsg[0] = _T('\0');

	//�ж�Ŀ¼�Ƿ����
	if ( !odsIsDirExist(lpszDirectoryName) )
	{
		odsMakeError( pstError, ODS_ERR_NODIR, ODS_MSG_NODIR );
		return(FALSE);
	}

	//���ɲ��ҵ��ļ���
	wsprintf(szPath, _T("%s\\*.*"), lpszDirectoryName);

	//���в��Ҳ���
	hFindData = FindFirstFile
			 (
			  szPath,
			  &FindData
			 );

	//���û���ҵ�
	if (hFindData == INVALID_HANDLE_VALUE)
		return(TRUE);

	bReturnCode = TRUE;
	do
	{
		//�ж��Ƿ��Ǳ���Ŀ¼
		if (lstrcmp(FindData.cFileName, _T(".")) == 0)
			continue;

		//�ж��Ƿ����ϼ�Ŀ¼
		if(lstrcmp(FindData.cFileName, _T("..")) == 0)
			continue;

		//�����ҵ����ļ���
		wsprintf(szPath, _T("%s\\%s"), lpszDirectoryName, FindData.cFileName);

		//�������Ŀ¼
		if (FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
		{
			if (dwFileType & FILE_ATTRIBUTE_DIRECTORY)
				bReturnCode = FileBrowseFunc(szPath, pstError);

			if (bReturnCode  && bIncludeSubDir )
				bReturnCode = odsBrowseDirectory(szPath, dwFileType, bIncludeSubDir, 
												 FileBrowseFunc, pstError );
		}
		else
		{
			if (FindData.dwFileAttributes & dwFileType)
				bReturnCode = FileBrowseFunc(szPath, pstError);
		}

		if(!bReturnCode)
			break;
	}while (FindNextFile(hFindData, &FindData) );

	//�رղ��Ҿ��
	FindClose(hFindData);

	return(bReturnCode);
}


//------------------------------------------------------------ 
// ȡ��ָ��Ŀ¼������
//		������Ŀ¼��������Ŀ¼�ĸ������ļ��ĸ������Լ�Ŀ¼���ֽ�Ϊ��λ�Ĵ�С
//------------------------------------------------------------ 
BOOL odsGetDirProperty( LPCTSTR lpszDirectoryName, BOOL bIncludeSubDir, 
					 STDIRPROPERTY* lpDirectoryProperty, STODSERROR* pstError )
{
	BOOL				bReturnCode = TRUE;
	WIN32_FIND_DATA		FindData;
	HANDLE				hFindData;
	TCHAR				szPath[_MAX_PATH];
	STDIRPROPERTY		DirectoryProperty;

	lpDirectoryProperty->dwDirNum = 0;
	lpDirectoryProperty->dwFileNum = 0;
	lpDirectoryProperty->i64Size = 0;

	//�ж�Ŀ¼�Ƿ����
	if( !odsIsDirExist(lpszDirectoryName) )
	{
		odsMakeError( pstError, ODS_ERR_NODIR, ODS_MSG_NODIR );
		return(FALSE);
	}

	//���ɲ��ҵ��ļ���
	wsprintf(szPath, _T("%s\\*.*"), lpszDirectoryName);

	//���в��Ҳ���
	hFindData = FindFirstFile
			 (
			  szPath,
			  &FindData
			 );

	//���û���ҵ�
	if (hFindData == INVALID_HANDLE_VALUE)
		return(TRUE);

	bReturnCode = TRUE;

	do
	{
		//�ж��Ƿ��Ǳ���Ŀ¼
		if (lstrcmp(FindData.cFileName, _T(".")) == 0)
			continue;

		//�ж��Ƿ����ϼ�Ŀ¼
		if (lstrcmp(FindData.cFileName, _T("..")) == 0)
			continue;

		//�����ҵ����ļ���
		wsprintf(szPath, _T("%s\\%s"), lpszDirectoryName, FindData.cFileName);

		//�ж��ļ�������
		if (FindData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
		{
			lpDirectoryProperty->dwDirNum ++;
			if (bIncludeSubDir )
			{
				bReturnCode = odsGetDirProperty( szPath, TRUE, &DirectoryProperty, pstError );
				if( bReturnCode )
				{
					lpDirectoryProperty->dwDirNum += DirectoryProperty.dwDirNum;
					lpDirectoryProperty->dwFileNum += DirectoryProperty.dwFileNum;
					lpDirectoryProperty->i64Size += DirectoryProperty.i64Size;
				}
			}
		}
		else
		{
			lpDirectoryProperty->dwFileNum ++;
			lpDirectoryProperty->i64Size += 
				((__int64)FindData.nFileSizeHigh * (__int64)MAXDWORD) + (__int64)FindData.nFileSizeLow;
		}

		if (!bReturnCode)
			break;

	}while(FindNextFile(hFindData, &FindData) );

	//�رղ��Ҿ��
	FindClose(hFindData);

	return(bReturnCode);
} 

