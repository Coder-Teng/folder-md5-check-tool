#include "stdafx.h"
#include <iostream>
#include <fstream>
#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <io.h>
#include <algorithm>
#include <fstream>
#include <Windows.h>


#include "CFileTool.h"
#include "MyMd5.h"

// flag: 0 默认；1 相等；
void FileTool::CompareFilesMd5(std::map<std::string,MyFileInfo> &fileInfoMap1,std::map<std::string,MyFileInfo> &fileInfoMap2,
	 std::vector<std::string>& lessFileList,std::vector<std::string>& changedFileList,std::vector<std::string>& moreFileList)
{
	std::string strRelativeFilePath, strMd5;
	std::map<std::string,MyFileInfo>::iterator it1, tmpIt;
	
	it1 = fileInfoMap1.begin();
	while ( it1 != fileInfoMap1.end() )
	{
		strRelativeFilePath = it1->first;
		strMd5 = it1->second.strMd5;
		
		// 扫描目录下 有文件缺失
		tmpIt = fileInfoMap2.find(strRelativeFilePath);
		if ( fileInfoMap2.end() == tmpIt )
		{
			lessFileList.push_back(strRelativeFilePath);
		}
		// 扫描目录下 有文件被篡改
		else if( 0 != stricmp( tmpIt->second.strMd5.c_str(),strMd5.c_str() ))
		{
			changedFileList.push_back(strRelativeFilePath);
		}
		else
		{
			tmpIt->second.flag = 1;
		}
		it1 ++ ;
	}

	it1 = fileInfoMap2.begin();
	while ( it1 != fileInfoMap2.end() )
	{
		// 扫描目录下 有多出来的文件
		if ( 0 == it1->second.flag )
		{
			moreFileList.push_back(it1->first);
		}
		it1 ++;
	}
}




void FileTool::ReadFilesMd5FromFile(std::map<std::string,MyFileInfo> &filesMd5Map,std::string strFilesMd5Path)
{
	std::string strLine,strRelativeFilePath, strMd5;
	std::wstring wsTmpPath, wsExePath;
	std::ifstream in;
	std::vector<std::string> vTemp;

	in.open(strFilesMd5Path.c_str());
	while ( getline(in,strLine) )
	{
		if (strLine.empty())
		{
			continue;
		}
		vTemp.clear();
		SplitString(strLine.c_str(),vTemp,",");
		MyFileInfo myFileInfo;
		myFileInfo.strRelativeFilePath = vTemp[0];
		myFileInfo.strMd5 = vTemp[1];
		myFileInfo.flag = 0;
		filesMd5Map.insert(std::pair<std::string,MyFileInfo>(myFileInfo.strRelativeFilePath,myFileInfo));
	}
}

void FileTool::WriteFilesMd52File(std::map<std::string,MyFileInfo> filesMd5Map,std::string strFileMd5Path)
{
	std::wstring wsFileMd5Path;
	S2WS(strFileMd5Path,wsFileMd5Path);
	DeleteFile(wsFileMd5Path.c_str());
	std::fstream fout;
	fout.open(strFileMd5Path.c_str(),std::ios::app);

	if ( !filesMd5Map.empty() && fout )
	{
		std::map<std::string,MyFileInfo>::iterator it;
		it = filesMd5Map.begin();
		while( it != filesMd5Map.end() )
		{
			fout << it->first << "," << it->second.strMd5 << std::endl;
			it ++;
		}
	}
	fout.flush();
	fout.close();
}


void  FileTool::DfsFolder(std::string folderPath,std::map<std::string,MyFileInfo> &filesMd5Map,std::wstring &wsErrorLog)
{
	_finddata_t fileinfo;
	std::string strfind = folderPath + "\\*";
	long handle = _findfirst(strfind.c_str(), &fileinfo);

	if (handle == -1l)
	{
		wsErrorLog = L"输入文件夹路径有误";
		return;
	}
	do{
		//判断是否有子目录
		if (fileinfo.attrib & _A_SUBDIR)    
		{
			if( (strcmp(fileinfo.name,".") != 0 ) &&(strcmp(fileinfo.name,"..") != 0))   
			{
				std::string newpath = folderPath + "\\" + fileinfo.name;
				DfsFolder(newpath,filesMd5Map,wsErrorLog);
			}
		}
		else  
		{
			MyFileInfo  myfileInfo;
			std::string strAppPath = GetAppPath();
			if( !strAppPath.empty() )
			{
				std::string strFilePath = folderPath + "\\" + fileinfo.name;
				int position = 0;
				if( ( position = strFilePath.find(strAppPath.c_str(),0 ) ) != std::string::npos ) 
				 {
					myfileInfo.strRelativeFilePath = strFilePath.substr(position + strAppPath.size() + 1 ,strFilePath.size());
					if (!myfileInfo.strRelativeFilePath.empty())
					{
						char  szFilePath[250];
						memset(szFilePath,0,sizeof(szFilePath));
						strcpy(szFilePath,strFilePath.c_str());
						myfileInfo.strMd5 = MD5_file(szFilePath,32);
						std::transform(myfileInfo.strMd5.begin(),myfileInfo.strMd5.end(),myfileInfo.strMd5.begin(),::toupper);
						myfileInfo.flag = 0;
						
						filesMd5Map.insert(std::pair<std::string,MyFileInfo>(myfileInfo.strRelativeFilePath,myfileInfo));
					}
				 }				
			}
		}
	}while (_findnext(handle, &fileinfo) == 0);

	_findclose(handle);
}


void FileTool::S2WS(const std::string& s, std::wstring& ws)
{
	int len = ( s.length()  )+1;
	wchar_t* buf = (wchar_t*)malloc( ( len )* 2 );
	memset( buf, 0 , (len )  * 2 );
	len = len * 2;
	int ret = MultiByteToWideChar( CP_UTF8, 0, s.c_str(), -1, buf, len  );
	if( ret == 0 || ret > len )
	{
		int err = GetLastError();
	}
	ws =  buf ;
	free(buf);
}

void FileTool::WS2S(const std::wstring& ws, std::string& s)
{
	char* buf =(char*) malloc( ( ws.length() + 1 ) * 3 );
	memset( buf, 0 , ( ws.length() + 1 ) * 3 );
	WideCharToMultiByte( CP_UTF8, 0 , ws.c_str(), ws.length(), buf, ( ws.length() + 1 ) * 3 ,NULL, NULL );
	s = buf;
	free(buf);
}
void FileTool::SplitString(const std::string& s, std::vector<std::string>& v, const std::string& c)
{
	std::string::size_type pos1, pos2;
	pos2 = s.find(c);
	pos1 = 0;
	while(std::string::npos != pos2)
	{
		v.push_back(s.substr(pos1, pos2-pos1));

		pos1 = pos2 + c.size();
		pos2 = s.find(c, pos1);
	}
	if(pos1 != s.length())
		v.push_back(s.substr(pos1));
}

CString FileTool::toCString(std::string str)
{
#ifdef _UNICODE
	//如果是unicode工程
	USES_CONVERSION; CString s(str.c_str());
	CString ans(str.c_str());
	return ans;
#else
	//如果是多字节工程 
	//string 转 CString
	CString ans;
	ans.Format("%s", str.c_str());
	return ans; 
#endif // _UNICODE  
}