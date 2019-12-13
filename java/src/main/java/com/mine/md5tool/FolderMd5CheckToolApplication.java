package com.mine.md5tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mine.md5tool.bean.ParamsBean;
import com.mine.md5tool.bean.RetBean;
import com.mine.md5tool.bussiness.Md5FolderTool;
import com.mine.md5tool.bussiness.ParamHandler;
import com.mine.md5tool.define.AppDefine;
import com.mine.md5tool.util.FileUtil;

public class FolderMd5CheckToolApplication {
    public static void main(String[] args) {
        RetBean<ParamsBean> paramHandleRet = ParamHandler.handle ( args );
        if (paramHandleRet.num != 0) {
            System.out.println ( paramHandleRet.tips );
            return;
        }
        System.out.println ( paramHandleRet.obj );
        RetBean ret = null;
        String runMode = paramHandleRet.obj.runMode;
        String standardMd5Path = paramHandleRet.obj.standardMd5Path;
        String folderPath = paramHandleRet.obj.folderPath;
        Set<String> excludedPaths = paramHandleRet.obj.excludedPaths;
        Md5FolderTool tool = new Md5FolderTool ( );
        if (AppDefine.RUNMODE_READANDRECORDMD5_NUM.equalsIgnoreCase ( runMode ) ||
                AppDefine.RUNMODE_READANDRECORDMD5_STR.equalsIgnoreCase ( runMode )) { // 读取文件夹下文件、计算md5并保存
            ret = tool.readAndRecordMd5 ( standardMd5Path , folderPath , excludedPaths );
        } else if (AppDefine.RUNMODE_COMPAREFOLDERMD5_NUM.equalsIgnoreCase ( runMode ) ||
                AppDefine.RUNMODE_COMPAREFOLDERMD5_STR.equalsIgnoreCase ( runMode )) { // 用给定的 md5 记录文件,比较文件夹下文件的md5
            ret = tool.compareFolderMd5 ( standardMd5Path , folderPath , excludedPaths );
        } else {
            ret = new RetBean ( -3 , "Incorrect RunMode: " + runMode );
        }
        System.out.println ( ret.tips );
    }
}
