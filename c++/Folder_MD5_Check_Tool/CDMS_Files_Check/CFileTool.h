#include<vector>

struct MyFileInfo
{
	std::string strRelativeFilePath;
	std::string strMd5;
	int flag ;
};

class FileTool
{
private:
	std::string m_strAppPath;
	


public:
	

	void DfsFolder(std::string folderpath,std::map<std::string,MyFileInfo> &fileInfoMap,std::wstring &wsErrorLog);
	
	void WriteFilesMd52File(std::map<std::string,MyFileInfo> filesMd5Map,std::string strFileMd5Path);
	
	void FileTool::ReadFilesMd5FromFile(std::map<std::string,MyFileInfo> &filesMd5Map,std::string strFilesMd5Path);

	void S2WS(const std::string& s, std::wstring& ws);

	void WS2S(const std::wstring& ws, std::string& s);

	void SplitString(const std::string& s,std::vector<std::string>& v, const std::string& c);

	void CompareFilesMd5(std::map<std::string,MyFileInfo> &fileInfoMap1,std::map<std::string,MyFileInfo> &fileInfoMap2,
			std::vector<std::string>& changedFileList,std::vector<std::string>& lessFileList,std::vector<std::string>& moreFileList);

	CString toCString(std::string str);

	std::string GetAppPath()
	{
		return m_strAppPath;
	}
	void SetAppPath(std::string strAppPath)
	{
		m_strAppPath = strAppPath;
	}
};
