package com.mine.md5tool.bussiness;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import com.mine.md5tool.bean.RetBean;
import com.mine.md5tool.define.AppDefine;
import com.mine.md5tool.util.FileUtil;
import com.mine.md5tool.util.MD5Util;

public class Md5FolderTool {


    /**
     * 读取指定路径文件夹下的文件的md5，存放到文本中
     *
     * @param standardMd5Path
     * @param folderPath
     * @return 0 成功
     */
    public RetBean readAndRecordMd5(String standardMd5Path , String folderPath , Set<String> excludedPaths) {
        File rootFolder = new File ( folderPath );
        if (!rootFolder.exists ( )) {
            return new RetBean ( -1 , "Error, folder does not exist," + folderPath );
        }
        File standardMd5File = new File ( standardMd5Path );
        if (standardMd5File.exists ( ) && !standardMd5File.delete ( )) {
            return new RetBean ( -2 , "Error, file is occupied, " + standardMd5Path );
        }
        // 避免传入参数中\/的差异
        String rootFolderPath = rootFolder.getAbsolutePath ( );
        String rootFolderName = rootFolder.getName ( );
        List<File> fileList = new ArrayList<File> ( );
        FileUtil.traverseFolder ( rootFolderPath , rootFolderPath , excludedPaths , fileList );
        for (File file : fileList) {
            String absolutePath = file.getAbsolutePath ( );
            int pos = absolutePath.indexOf ( rootFolderPath );
            if (pos == -1) {
                continue;
            }
            String relativePath = rootFolderName + absolutePath.substring ( pos + rootFolderPath.length ( ) ).trim ( );
            String md5 = "directory";
            if (file.isDirectory ( ) == false) {
                md5 = MD5Util.calculateMD5OfFile ( file );
            }
            FileUtil.write2File ( standardMd5Path , relativePath + "=" + md5 + "\r\n" );
        }
        return new RetBean ( 0 , "Done, records in " + standardMd5File.getAbsolutePath ( ) );
    }

    /**
     * 根据xml文件中的md5 ，比对指定文件夹下的文件的md5
     *
     * @param standardMd5Path 标准MD5文件路径
     * @param folderPath      需要检查的文件夹路径
     * @return
     */
    public RetBean compareFolderMd5(String standardMd5Path , String folderPath , Set<String> excludedPaths) {
        File standardMd5File = new File ( standardMd5Path );
        if (!standardMd5File.exists ( )) {
            return new RetBean ( -1 , "Error, file does not exist," + standardMd5File.getAbsolutePath ( ) );
        }
        File rootFolder = new File ( folderPath );
        if (!rootFolder.exists ( )) {
            return new RetBean ( -2 , "Error, folder does not exist," + rootFolder.getAbsolutePath ( ) );
        }
        Map<String, String[]> md5Map = FileUtil.readMd5FromTxt ( standardMd5Path );
        if (md5Map == null) {
            return new RetBean ( -3 , "Error, cannot read information from file!" );
        }

        String rootFolderPath = rootFolder.getAbsolutePath ( );
        String rootFolderName = rootFolder.getName ( );
        List<File> fileList = new ArrayList<File> ( );
        FileUtil.traverseFolder ( rootFolderPath , rootFolderPath , excludedPaths , fileList );
        for (File file : fileList) {
            String absolutePath = file.getAbsolutePath ( );
            int pos = absolutePath.indexOf ( rootFolderPath );
            if (pos == -1) {
                continue;
            }
            String relativePath = rootFolderName + absolutePath.substring ( pos + rootFolderPath.length ( ) ).trim ( );
            String md5 = "directory";
            if (file.isDirectory ( ) == false) {
                md5 = MD5Util.calculateMD5OfFile ( file );
            }
            String[] md5Arr = md5Map.get ( relativePath );
            if (md5Arr == null) {
                md5Arr = new String[2];
                md5Map.put ( relativePath , md5Arr );
            }
            md5Arr[1] = md5;
        }

        String checkRetOfIncorrectFilesPath = System.getProperty ( "user.dir" ) + File.separatorChar
                + "checkResult_incorrect.csv";
        String checkRetOfAllFilesPath = System.getProperty ( "user.dir" ) + File.separatorChar
                + "checkResult_all.csv";
        File checkRetOfIncorrectFiles = new File ( checkRetOfIncorrectFilesPath );
        File checkRetOfAllFiles = new File ( checkRetOfAllFilesPath );
        if (checkRetOfIncorrectFiles.exists ( ) && !checkRetOfIncorrectFiles.delete ( )) {
            return new RetBean ( -4 , "Error, file is occupied, " + checkRetOfIncorrectFiles.getAbsolutePath ( ) );
        }
        if (checkRetOfAllFiles.exists ( ) && !checkRetOfAllFiles.delete ( )) {
            return new RetBean ( -4 , "Error, file is occupied, " + checkRetOfAllFiles.getAbsolutePath ( ) );
        }

        boolean checkRet = true;
        String csvHead = AppDefine.CSV_HEAD;
        FileUtil.write2File ( checkRetOfAllFilesPath , csvHead );
        FileUtil.write2File ( checkRetOfIncorrectFilesPath , csvHead );
        for (Entry<String, String[]> entry : md5Map.entrySet ( )) {
            String relativePath = entry.getKey ( );
            String md51 = entry.getValue ( )[0];
            String md52 = entry.getValue ( )[1];
            String checkRetStr = "ok";
            if (md51 == null || md52 == null || !md51.equalsIgnoreCase ( md52 )) {
                checkRet = false;
                checkRetStr = "error";
                FileUtil.write2File ( checkRetOfIncorrectFilesPath ,
                        relativePath + "," + md51 + "," + md52 + "," + checkRetStr + "\r\n" );
            }
            FileUtil.write2File ( checkRetOfAllFilesPath ,
                    relativePath + "," + md51 + "," + md52 + "," + checkRetStr + "\r\n" );
        }
        if (checkRet == true) {
            return new RetBean ( 0 , "Done, all files are correct, Details in " + checkRetOfAllFilesPath );
        } else {
            return new RetBean ( 1 , "Done, some files are incorrect, Details in "
                    + checkRetOfIncorrectFilesPath + "," + checkRetOfAllFilesPath );
        }
    }

}
