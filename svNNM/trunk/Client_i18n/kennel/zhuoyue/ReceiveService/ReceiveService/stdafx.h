// stdafx.h : ��׼ϵͳ�����ļ��İ����ļ���
// ���Ǿ���ʹ�õ��������ĵ�
// �ض�����Ŀ�İ����ļ�
//

#pragma once

#ifndef _WIN32_WINNT		// ����ʹ���ض��� Windows XP ����߰汾�Ĺ��ܡ�
#define _WIN32_WINNT 0x0501	// ����ֵ����Ϊ��Ӧ��ֵ���������� Windows �������汾��
#endif						

#include <stdio.h>
#include <tchar.h>


// stdafx.h : include file for standard system include files,
//  or project specific include files that are used frequently, but
//      are changed infrequently
//

#if !defined(AFX_STDAFX_H__40095FE4_5F70_11D4_B3A2_0080C88FC26A__INCLUDED_)
#define AFX_STDAFX_H__40095FE4_5F70_11D4_B3A2_0080C88FC26A__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define VC_EXTRALEAN		// Exclude rarely-used stuff from Windows headers


// Ϊ�ڷ�����ʹ�öԻ�����Ҫ�Ķ���
//#define _WIN32_WINNT			0X4000

#include <afx.h>
#include <afxwin.h>         // MFC core and standard components
#include <afxext.h>         // MFC extensions
#include <afxdtctl.h>		// MFC support for Internet Explorer 4 Common Controls
#ifndef _AFX_NO_AFXCMN_SUPPORT
#include <afxcmn.h>			// MFC support for Windows Common Controls
#endif // _AFX_NO_AFXCMN_SUPPORT


// TODO: reference additional headers your program requires here

#include <winsvc.h>
#include <afxdb.h>


//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_STDAFX_H__40095FE4_5F70_11D4_B3A2_0080C88FC26A__INCLUDED_)

// TODO: �ڴ˴����ó�����Ҫ������ͷ�ļ�
