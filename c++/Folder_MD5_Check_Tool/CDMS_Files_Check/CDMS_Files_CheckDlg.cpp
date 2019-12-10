
// CDMS_Files_CheckDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "CDMS_Files_Check.h"
#include "CDMS_Files_CheckDlg.h"

#include <string>
#include <map>
#include "CFileTool.h"

#include "MyMd5.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// ����Ӧ�ó��򡰹��ڡ��˵���� CAboutDlg �Ի���

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// �Ի�������
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

// ʵ��
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


// CCDMS_Files_CheckDlg �Ի���




CCDMS_Files_CheckDlg::CCDMS_Files_CheckDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CCDMS_Files_CheckDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CCDMS_Files_CheckDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_EDIT1, FolderPathEditBox);
	DDX_Control(pDX, IDC_BUTTON1, StartCheckButton);
	DDX_Control(pDX, IDC_EDIT2, CheckResultEditBox);
	DDX_Control(pDX, IDC_EDIT3, md5FilePathEditBox);
}

BEGIN_MESSAGE_MAP(CCDMS_Files_CheckDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDC_BUTTON1, &CCDMS_Files_CheckDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CCDMS_Files_CheckDlg::OnBnClickedButton2)
END_MESSAGE_MAP()


// CCDMS_Files_CheckDlg ��Ϣ�������

BOOL CCDMS_Files_CheckDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// ��������...���˵�����ӵ�ϵͳ�˵��С�

	// IDM_ABOUTBOX ������ϵͳ���Χ�ڡ�
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		BOOL bNameValid;
		CString strAboutMenu;
		bNameValid = strAboutMenu.LoadString(IDS_ABOUTBOX);
		ASSERT(bNameValid);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// ���ô˶Ի����ͼ�ꡣ��Ӧ�ó��������ڲ��ǶԻ���ʱ����ܽ��Զ�
	//  ִ�д˲���
	SetIcon(m_hIcon, TRUE);			// ���ô�ͼ��
	SetIcon(m_hIcon, FALSE);		// ����Сͼ��

	// TODO: �ڴ���Ӷ���ĳ�ʼ������

	FolderPathEditBox.SetWindowText(L"������Ҫ�����ļ���·�������磺d:\\runtime\\");
	md5FilePathEditBox.SetWindowText(L"�����뱣��MD5��Ϣ���ļ�������d:\\md5.txt");

	return TRUE;  // ���ǽ��������õ��ؼ������򷵻� TRUE
}

void CCDMS_Files_CheckDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// �����Ի��������С����ť������Ҫ����Ĵ���
//  �����Ƹ�ͼ�ꡣ����ʹ���ĵ�/��ͼģ�͵� MFC Ӧ�ó���
//  �⽫�ɿ���Զ���ɡ�

void CCDMS_Files_CheckDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // ���ڻ��Ƶ��豸������

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// ʹͼ���ڹ����������о���
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// ����ͼ��
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

//���û��϶���С������ʱϵͳ���ô˺���ȡ�ù��
//��ʾ��
HCURSOR CCDMS_Files_CheckDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

// ����ļ������ļ���ȷ��
void CCDMS_Files_CheckDlg::OnBnClickedButton1()
{
	CString csMd5FilePath,csCheckFolderPath, csOutLog, csOutLog2; 
	std::string strMd5FilePath,strCheckFolderPath;
	std::wstring wsErrorLog, wsTmp;
	std::map<std::string,MyFileInfo> filesMd5Map1, filesMd5Map2;
	std::vector<std::string> vLessFileList,vChangedFileList,vMoreFileList;
	CString csLessNum, csChangedNum, csMoreNum;
	
	md5FilePathEditBox.GetWindowText(csMd5FilePath);
	strMd5FilePath = CT2A(csMd5FilePath.GetBuffer());

	csOutLog = L"";
	CheckResultEditBox.GetWindowText(csOutLog);
	csOutLog =  L"��ʼ���......";
	CheckResultEditBox.SetWindowText(csOutLog);
	
	csOutLog += L"\r\n��ȡ��׼�ļ�Md5��Ϣ......";
	CheckResultEditBox.SetWindowText(csOutLog);
	FileTool fileTool;
	fileTool.ReadFilesMd5FromFile(filesMd5Map1,strMd5FilePath);
	if (filesMd5Map1.empty())
	{
		csOutLog.Append(L"\r\n��ȡ��׼�ļ�Md5��Ϣʧ�ܣ�");
		CheckResultEditBox.SetWindowText(csOutLog);
		return;
	}
	
	csOutLog.Append(L"\r\nɨ��Ŀ���ļ���Ŀ¼......");
	CheckResultEditBox.SetWindowText(csOutLog);

	FolderPathEditBox.GetWindowText(csCheckFolderPath);
	strCheckFolderPath = CT2A(csCheckFolderPath.GetBuffer());
	
	fileTool.SetAppPath(strCheckFolderPath);
	fileTool.DfsFolder(strCheckFolderPath,filesMd5Map2,wsErrorLog);	
	
	if (!wsErrorLog.empty())
	{
		csOutLog.Append(L"\r\nɨ��Ŀ���ļ���Ŀ¼ʧ�ܣ�");
		CheckResultEditBox.SetWindowText(csOutLog);
		return;
	}
	else
	{
		csOutLog.Append(L"\r\n���Ŀ���ļ���Ŀ¼���ļ���");
		CheckResultEditBox.SetWindowText(csOutLog);
		fileTool.CompareFilesMd5(filesMd5Map1,filesMd5Map2,vLessFileList,vChangedFileList,vMoreFileList);
		
		csOutLog2.Append(L"\r\n��������");
		
		csLessNum.Format(_T("%d"), vLessFileList.size());
		csOutLog2  += L"\r\nȱ���ļ���" + csLessNum;
		for (unsigned int i = 0; i < vLessFileList.size();i++ )
		{	
			csOutLog2 += L"\r\n";
			csOutLog2 += fileTool.toCString(vLessFileList[i]);
		}

		csChangedNum.Format(_T("%d"), vChangedFileList.size());
		csOutLog2  += L"\r\n�۸��ļ���" + csChangedNum;
		for (unsigned int i = 0; i < vChangedFileList.size();i++ )
		{
			csOutLog2 += L"\r\n";
			csOutLog2 += fileTool.toCString(vChangedFileList[i]);
		}

		csMoreNum.Format(_T("%d"), vMoreFileList.size());
		csOutLog2  += L"\r\n�����ļ���" + csMoreNum;
		for ( unsigned int i = 0; i < vMoreFileList.size();i++ )
		{
			csOutLog2 += L"\r\n";
			csOutLog2 += fileTool.toCString(vMoreFileList[i]);
		}
		
		CheckResultEditBox.SetWindowText(csOutLog +csOutLog2);
	}
}

// ɨ���ļ������ļ��������¼md5
void CCDMS_Files_CheckDlg::OnBnClickedButton2()
{
	CString csCheckFolderPath,csFileMd5Path, csOutLog; 
	std::string strCheckFolderPath,strFileMd5Path;
	std::wstring wsErrorLog;
	std::map<std::string,MyFileInfo> filesMd5Map;
	
	FolderPathEditBox.GetWindowText(csCheckFolderPath);
	strCheckFolderPath = CT2A(csCheckFolderPath.GetBuffer());
	
	CheckResultEditBox.SetWindowText(L"");
	csOutLog = csOutLog + L"����ɨ��......";
	CheckResultEditBox.SetWindowText(csOutLog);
	
	FileTool fileTool;
	fileTool.SetAppPath(strCheckFolderPath);
	fileTool.DfsFolder(strCheckFolderPath,filesMd5Map,wsErrorLog);	
	if (!wsErrorLog.empty())
	{
		csOutLog.Append(L"\r\n");
		csOutLog.Append(wsErrorLog.c_str());
		csOutLog.Append(L"\r\nɨ��ʧ�ܣ�");
		CheckResultEditBox.SetWindowText(csOutLog);
		return;
	}
	else
	{
		md5FilePathEditBox.GetWindowText(csFileMd5Path);
		strFileMd5Path = CT2A(csFileMd5Path.GetBuffer());
		fileTool.WriteFilesMd52File(filesMd5Map,strFileMd5Path);
		csOutLog += L"\r\nɨ����ɣ�";
		CheckResultEditBox.SetWindowText(csOutLog);
	}
}
