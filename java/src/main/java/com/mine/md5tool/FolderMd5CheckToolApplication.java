package com.mine.md5tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.mine.md5tool.bussiness.Md5FolderTool;
import com.mine.md5tool.util.FileUtil;

public class FolderMd5CheckToolApplication {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println ( "Null input, check the script！" );
            return;
        }
        String paramsFilePath = args[0].trim ( );
        Map<String, String> paramMap = new HashMap<String, String> ( );
        int ret = FileUtil.readParamFromConfig ( paramsFilePath , paramMap );
        if (ret != 0) {
            System.out.println ( "Incorrect input, check the script！" );
            return;
        }
        String command = paramMap.get ( "command" ).trim ( );
        String standardMd5Path = paramMap.get ( "standardMd5Path" ).trim ( );
        String folerPath = paramMap.get ( "folerPath" ).trim ( );
        boolean checkRet = checkParam ( command , standardMd5Path , folerPath );
        if (checkRet == false) {
            System.out.println ( "Incorrect Input: " + command + "," + standardMd5Path + "," + folerPath );
            return;
        }
        System.out.println ( "command=" + command );
        System.out.println ( "standardMd5Path=" + standardMd5Path );
        System.out.println ( "folerPath=" + folerPath );

        Md5FolderTool tool = new Md5FolderTool ( );
        String log;
        if (command.equals ( "1" ) || "readAndRecordMd5".equalsIgnoreCase ( command )) { // 读取文件夹下文件、计算md5并保存
            log = tool.readAndRecordMd5 ( standardMd5Path , folerPath );
        } else if (command.equals ( "2" ) || "compareFolderMd5".equalsIgnoreCase ( command )) { // 用给定的xml md5 记录文件,比较文件夹下文件的md5
            log = tool.compareFolderMd5 ( standardMd5Path , folerPath );
        } else {
            log = "Incorrect Command: " + command;
        }
        System.out.println ( log );
    }

    private static boolean checkParam(String command , String xmlPath , String folerPath) {
        if (command == null || "".equals ( command )) {
            return false;
        }
        if (xmlPath == null || "".equals ( xmlPath )) {
            return false;
        }
        if (folerPath == null || "".equals ( folerPath )) {
            return false;
        }
        return true;
    }
}
