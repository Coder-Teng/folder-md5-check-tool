
// CDMS_Files_CheckDlg.cpp : 实现文件
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


// 用于应用程序“关于”菜单项的 CAboutDlg 对话框

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// 对话框数据
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

// 实现
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


// CCDMS_Files_CheckDlg 对话框




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


// CCDMS_Files_CheckDlg 消息处理程序

BOOL CCDMS_Files_CheckDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// 将“关于...”菜单项添加到系统菜单中。

	// IDM_ABOUTBOX 必须在系统命令范围内。
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

	// 设置此对话框的图标。当应用程序主窗口不是对话框时，框架将自动
	//  执行此操作
	SetIcon(m_hIcon, TRUE);			// 设置大图标
	SetIcon(m_hIcon, FALSE);		// 设置小图标

	// TODO: 在此添加额外的初始化代码

	FolderPathEditBox.SetWindowText(L"请输入要检查的文件夹路径，例如：d:\\runtime\\");
	md5FilePathEditBox.SetWindowText(L"请输入保存MD5信息的文件，例如d:\\md5.txt");

	return TRUE;  // 除非将焦点设置到控件，否则返回 TRUE
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

// 如果向对话框添加最小化按钮，则需要下面的代码
//  来绘制该图标。对于使用文档/视图模型的 MFC 应用程序，
//  这将由框架自动完成。

void CCDMS_Files_CheckDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // 用于绘制的设备上下文

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 使图标在工作区矩形中居中
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 绘制图标
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

//当用户拖动最小化窗口时系统调用此函数取得光标
//显示。
HCURSOR CCDMS_Files_CheckDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

// 检查文件夹下文件正确性
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
	csOutLog =  L"开始检查......";
	CheckResultEditBox.SetWindowText(csOutLog);
	
	csOutLog += L"\r\n获取标准文件Md5信息......";
	CheckResultEditBox.SetWindowText(csOutLog);
	FileTool fileTool;
	fileTool.ReadFilesMd5FromFile(filesMd5Map1,strMd5FilePath);
	if (filesMd5Map1.empty())
	{
		csOutLog.Append(L"\r\n获取标准文件Md5信息失败！");
		CheckResultEditBox.SetWindowText(csOutLog);
		return;
	}
	
	csOutLog.Append(L"\r\n扫描目标文件夹目录......");
	CheckResultEditBox.SetWindowText(csOutLog);

	FolderPathEditBox.GetWindowText(csCheckFolderPath);
	strCheckFolderPath = CT2A(csCheckFolderPath.GetBuffer());
	
	fileTool.SetAppPath(strCheckFolderPath);
	fileTool.DfsFolder(strCheckFolderPath,filesMd5Map2,wsErrorLog);	
	
	if (!wsErrorLog.empty())
	{
		csOutLog.Append(L"\r\n扫描目标文件夹目录失败！");
		CheckResultEditBox.SetWindowText(csOutLog);
		return;
	}
	else
	{
		csOutLog.Append(L"\r\n检查目标文件夹目录下文件！");
		CheckResultEditBox.SetWindowText(csOutLog);
		fileTool.CompareFilesMd5(filesMd5Map1,filesMd5Map2,vLessFileList,vChangedFileList,vMoreFileList);
		
		csOutLog2.Append(L"\r\n检查结束！");
		
		csLessNum.Format(_T("%d"), vLessFileList.size());
		csOutLog2  += L"\r\n缺少文件：" + csLessNum;
		for (unsigned int i = 0; i < vLessFileList.size();i++ )
		{	
			csOutLog2 += L"\r\n";
			csOutLog2 += fileTool.toCString(vLessFileList[i]);
		}

		csChangedNum.Format(_T("%d"), vChangedFileList.size());
		csOutLog2  += L"\r\n篡改文件：" + csChangedNum;
		for (unsigned int i = 0; i < vChangedFileList.size();i++ )
		{
			csOutLog2 += L"\r\n";
			csOutLog2 += fileTool.toCString(vChangedFileList[i]);
		}

		csMoreNum.Format(_T("%d"), vMoreFileList.size());
		csOutLog2  += L"\r\n多余文件：" + csMoreNum;
		for ( unsigned int i = 0; i < vMoreFileList.size();i++ )
		{
			csOutLog2 += L"\r\n";
			csOutLog2 += fileTool.toCString(vMoreFileList[i]);
		}
		
		CheckResultEditBox.SetWindowText(csOutLog +csOutLog2);
	}
}

// 扫描文件夹下文件并计算记录md5
void CCDMS_Files_CheckDlg::OnBnClickedButton2()
{
	CString csCheckFolderPath,csFileMd5Path, csOutLog; 
	std::string strCheckFolderPath,strFileMd5Path;
	std::wstring wsErrorLog;
	std::map<std::string,MyFileInfo> filesMd5Map;
	
	FolderPathEditBox.GetWindowText(csCheckFolderPath);
	strCheckFolderPath = CT2A(csCheckFolderPath.GetBuffer());
	
	CheckResultEditBox.SetWindowText(L"");
	csOutLog = csOutLog + L"正在扫描......";
	CheckResultEditBox.SetWindowText(csOutLog);
	
	FileTool fileTool;
	fileTool.SetAppPath(strCheckFolderPath);
	fileTool.DfsFolder(strCheckFolderPath,filesMd5Map,wsErrorLog);	
	if (!wsErrorLog.empty())
	{
		csOutLog.Append(L"\r\n");
		csOutLog.Append(wsErrorLog.c_str());
		csOutLog.Append(L"\r\n扫描失败！");
		CheckResultEditBox.SetWindowText(csOutLog);
		return;
	}
	else
	{
		md5FilePathEditBox.GetWindowText(csFileMd5Path);
		strFileMd5Path = CT2A(csFileMd5Path.GetBuffer());
		fileTool.WriteFilesMd52File(filesMd5Map,strFileMd5Path);
		csOutLog += L"\r\n扫描完成！";
		CheckResultEditBox.SetWindowText(csOutLog);
	}
}
