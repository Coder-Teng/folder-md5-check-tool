package com.mine.md5tool.bussiness;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mine.md5tool.util.FileUtil;
import com.mine.md5tool.util.FolderTraverseUtil;
import com.mine.md5tool.util.MD5Util;

public class Md5FolderTool {

	/**
	 * 读取指定路径文件夹下的文件的md5，存放到xml中
	 * 
	 * @param folderPath
	 * @param xmlPath
	 */
	public String readAndRecordMd5(String standardMd5Path, String folderPath) {
		File f = new File(folderPath);
		if (!f.exists()) {
			return "error, folder does not exist," + folderPath;
		}
		File standardMd5File = new File(standardMd5Path);
		if (standardMd5File.exists()) {
			standardMd5File.delete();
		}
		String folerName = f.getName(); // 为了在文件中保存相对文件夹的路径
		List<File> fileList = new ArrayList<File>();
		FolderTraverseUtil.getAllFile(folderPath, fileList);
		for (File file : fileList) {
			String absolutePath = file.getAbsolutePath();
			String fileName = absolutePath.substring(absolutePath.indexOf(folerName), absolutePath.length()).trim();
			String md5 = MD5Util.getMD5(file);
			FileUtil.write2File(standardMd5Path, fileName + "=" + md5 + "\r\n");
		}
		return "done, records in " + standardMd5Path;
	}

	/**
	 * 根据xml文件中的md5 ，比对指定文件夹下的文件的md5
	 * 
	 * @param xmlPath
	 * @param folerPath
	 */
	public String compareFolderMd5(String standardMd5Path, String folderPath) {
		File standardMd5File = new File(standardMd5Path);
		if (!standardMd5File.exists()) {
			return "error, standard-md5.txt does not exist," + standardMd5Path;
		}
		File f = new File(folderPath);
		if (!f.exists()) {
			return "error, folder does not exist," + folderPath;
		}
		Map<String, String[]> md5Map = new HashMap<String, String[]>();
		FileUtil.readMd5FromConfig(standardMd5Path, md5Map);

		String folerName = f.getName();
		List<File> fileList = new ArrayList<File>();
		FolderTraverseUtil.getAllFile(folderPath, fileList);
		for (File file : fileList) {
			String absolutePath = file.getAbsolutePath();
			String fileName = absolutePath.substring(absolutePath.indexOf(folerName), absolutePath.length()).trim();
			String md5 = MD5Util.getMD5(file);
			String[] md5Arr = md5Map.get(fileName);
			if (md5Arr == null) {
				md5Arr = new String[2];
				md5Map.put(fileName, md5Arr);
			}
			md5Arr[1] = md5;
		}

		String checkRetOfAllFilesPath = System.getProperty("user.dir") + File.separatorChar + "checkResult_all.csv";
		String checkRetOfIncorrectFilesPath = System.getProperty("user.dir") + File.separatorChar
				+ "checkResult_incorrect.csv";
		File checkRetOfIncorrectFiles = new File(checkRetOfAllFilesPath);
		File checkRetOfAllFiles = new File(checkRetOfAllFilesPath);
		if (checkRetOfIncorrectFiles.exists()) {
			checkRetOfIncorrectFiles.delete();
		}
		if (checkRetOfAllFiles.exists()) {
			checkRetOfAllFiles.delete();
		}
//		String csvHead = "文件名,标准MD5,检查所得MD5,检查结果\r\n";
		boolean checkRet = true;
		String csvHead = "fileName,standard-md5,check-md5,check-ret\r\n";
		FileUtil.write2File(checkRetOfAllFilesPath, csvHead);
		FileUtil.write2File(checkRetOfIncorrectFilesPath, csvHead);
		for (Entry<String, String[]> entry : md5Map.entrySet()) {
			String fileName = entry.getKey();
			String md51 = entry.getValue()[0];
			String md52 = entry.getValue()[1];
			String checkRetStr = "ok";
			if (md51 == null || md52 == null || !md51.equals(md52)) {
				checkRet = false;
				checkRetStr = "error";
				FileUtil.write2File(checkRetOfIncorrectFilesPath,
						fileName + "," + md51 + "," + md52 + "," + checkRetStr + "\r\n");
			}
			FileUtil.write2File(checkRetOfAllFilesPath,
					fileName + "," + md51 + "," + md52 + "," + checkRetStr + "\r\n");
		}
		if (checkRet == true) {
			return "done, The files are correct";
		} else {
			return "done, Some files are incorrect, Details in " + checkRetOfIncorrectFilesPath + ","
					+ checkRetOfAllFilesPath;
		}
	}

}
