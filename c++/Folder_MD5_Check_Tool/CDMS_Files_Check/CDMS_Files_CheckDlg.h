
// CDMS_Files_CheckDlg.h : ͷ�ļ�
//

#pragma once
#include "afxwin.h"


// CCDMS_Files_CheckDlg �Ի���
class CCDMS_Files_CheckDlg : public CDialog
{
// ����
public:
	CCDMS_Files_CheckDlg(CWnd* pParent = NULL);	// ��׼���캯��

// �Ի�������
	enum { IDD = IDD_CDMS_FILES_CHECK_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV ֧��


// ʵ��
protected:
	HICON m_hIcon;

	// ���ɵ���Ϣӳ�亯��
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedButton1();
	CEdit FolderPathEditBox;
	CButton StartCheckButton;
	CEdit CheckResultEditBox;
	afx_msg void OnBnClickedButton2();
	CEdit md5FilePathEditBox;
};
