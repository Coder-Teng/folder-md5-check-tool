
// CDMS_Files_Check.h : PROJECT_NAME Ӧ�ó������ͷ�ļ�
//

#pragma once

#ifndef __AFXWIN_H__
	#error "�ڰ������ļ�֮ǰ������stdafx.h�������� PCH �ļ�"
#endif

#include "resource.h"		// ������


// CCDMS_Files_CheckApp:
// �йش����ʵ�֣������ CDMS_Files_Check.cpp
//

class CCDMS_Files_CheckApp : public CWinAppEx
{
public:
	CCDMS_Files_CheckApp();

// ��д
	public:
	virtual BOOL InitInstance();

// ʵ��

	DECLARE_MESSAGE_MAP()
};

extern CCDMS_Files_CheckApp theApp;