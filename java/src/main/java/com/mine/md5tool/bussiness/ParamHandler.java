package com.mine.md5tool.bussiness;

import com.mine.md5tool.bean.ParamsBean;
import com.mine.md5tool.bean.RetBean;
import com.mine.md5tool.define.AppDefine;
import com.mine.md5tool.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Teng
 * @create 2019-12-12
 */
public class ParamHandler {

    /**
     * 读取参数
     *
     * @param args 控制台输入参数 （console runmode standardMd5Path folderPath）
     *             从txt中读取参数（txt paramTxtPath)
     * @return
     */
    public static RetBean<ParamsBean> handle(String[] args) {
        if (args == null || args.length == 0) {
            return new RetBean<ParamsBean> ( -1 , null , "Please input parameters!" );
        }
        String paramSettingMode = args[0];
        if (paramSettingMode == null || "".equals ( paramSettingMode )) {
            return new RetBean<ParamsBean> ( -1 , null , "Please input non-null parameters!" );
        }
        if (AppDefine.PARAMS_FROM_TXT.equalsIgnoreCase ( paramSettingMode )) {
            return handleParamFromTxt ( args );
        } else if (AppDefine.PARAMS_FROM_CONSOLE.equalsIgnoreCase ( paramSettingMode )) {
            return handleParamFromConsole ( args );
        } else {
            return new RetBean<ParamsBean> ( -1 , null , "Please input the correct parameters!" );
        }
    }

    /**
     * 从文本中读取参数
     *
     * @param args
     * @return
     */
    private static RetBean<ParamsBean> handleParamFromTxt(String[] args) {
        if (args.length < 2) {
            return new RetBean<ParamsBean> ( -1 , null , "Error in reading parameters from txt!" );
        }
        String paramTxtPath = args[1].trim ( );
        if (FileUtil.curIsAbsPath ( paramTxtPath ) == false) {
            paramTxtPath = FileUtil.USERDIR + File.separator + paramTxtPath;
        }
        ParamsBean params = FileUtil.readParamFromTxt ( paramTxtPath );
        if (checkParam ( params ) == false) {
            return new RetBean<ParamsBean> ( -1 , null , "Error in checking parameters from txt!" );
        }
        return new RetBean<ParamsBean> ( 0 , params , "Finish reading parameters from txt." );
    }

    /**
     * 从控制台读取参数
     *
     * @param args
     * @return
     */
    private static RetBean<ParamsBean> handleParamFromConsole(String[] args) {
        if (args.length < 4) {
            return new RetBean<ParamsBean> ( -1 , null , "Error in reading parameters from console!" );
        }
        ParamsBean params = new ParamsBean ( args[1] , args[2] , args[3] );
        if (args.length > 4) {
            String[] pathArr = args[4].split ( AppDefine.SEPARATOR );
            if (pathArr != null && !"".equals ( pathArr )) {
                params.excludedPaths = new HashSet<String> ( );
                for (String path : pathArr) {
                    params.excludedPaths.add ( FileUtil.parsePath ( path ) );
                }
            }
        }
        if (checkParam ( params ) == false) {
            return new RetBean<ParamsBean> ( -1 , null , "Error in checking parameters from console!" );
        }
        return new RetBean<ParamsBean> ( 0 , params , "Finish reading parameters from console." );

    }

    private static boolean checkParam(ParamsBean params) {
        if (params.runMode == null || "".equals ( params.runMode )) {
            return false;
        }
        if (params.standardMd5Path == null || "".equals ( params.standardMd5Path )) {
            return false;
        }
        if (params.folderPath == null || "".equals ( params.folderPath )) {
            return false;
        }
        return true;
    }
}
