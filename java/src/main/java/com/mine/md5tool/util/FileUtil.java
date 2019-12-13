package com.mine.md5tool.util;

import com.mine.md5tool.bean.ParamsBean;
import com.mine.md5tool.define.AppDefine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileUtil {

    /**
     * 当前路径
     */
    public static final String USERDIR = System.getProperty ( "user.dir" );

    /**
     * 遍历文件夹
     *
     * @param rootPath      [In]根文件夹路径
     * @param curPath       [In]当前路径
     * @param excludedPaths [In]排除的路劲
     * @param fileList      [Out]遍历到的所有文件以及文件夹
     */
    public static void traverseFolder(String rootPath , String curPath , Set<String> excludedPaths ,
                                      List<File> fileList) {
        File f = new File ( curPath );
        if (f.exists ( ) == false) {
            return;
        }
        boolean excluded = CheckForExclusion ( rootPath , f.getAbsolutePath ( ) , excludedPaths );
        if (excluded == true) {
            return;
        }
        if (f.exists ( ) && f.isDirectory ( )) {
            fileList.add ( f );
            for (File file : f.listFiles ( )) {
                traverseFolder ( rootPath , file.getAbsolutePath ( ) , excludedPaths , fileList );
            }
        }
        if (f.exists ( ) && f.isFile ( )) {
            fileList.add ( f );
        }
    }

    private static boolean CheckForExclusion(String rootPath , String subPath , Set<String> excludedPaths) {
        if (excludedPaths == null || excludedPaths.isEmpty ( )) {
            return false;
        }
        // 将输入的排除路径按完整路径处理
        for (String excludedPath : excludedPaths) {
            if (curIsAbsPath ( excludedPath ) == false) {
                excludedPath = rootPath + File.separator + excludedPath;
            }
            if (subPath.equals ( excludedPath )) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查path路径是否为完整路径
     *
     * @param path
     * @return
     */
    public static boolean curIsAbsPath(String path) {
        boolean curIsAbsPath = false;
        if (OsUtil.CURISWINDOWS == true) {
            char c1 = path.charAt ( 0 );
            char c2 = path.charAt ( 1 );
            if ((c1 <= 'z' && c1 >= 'a' || c1 <= 'Z' && c1 >= 'A') && c2 == ':') {
                curIsAbsPath = true;
            }
        }
        if (OsUtil.CURISLINUX == true) {
            char c1 = path.charAt ( 0 );
            if (c1 == '/') {
                curIsAbsPath = true;
            }
        }
        return curIsAbsPath;
    }

    public static void write2File(String filePath , String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter ( new FileWriter ( new File ( filePath ) , true ) );
            bw.write ( content );
            bw.flush ( );
        } catch ( IOException e ) {
            e.printStackTrace ( );
        } finally {
            if (bw != null) {
                try {
                    bw.close ( );
                } catch ( IOException e ) {
                    e.printStackTrace ( );
                }
            }
        }
    }

    public static Map<String, String[]> readMd5FromTxt(String standardMd5Path) {
        Map<String, String[]> md5Map = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader ( new FileReader ( new File ( standardMd5Path ) ) );
            md5Map = new HashMap<String, String[]> ( );
            String line = null;
            while ((line = br.readLine ( )) != null) {
                if (line.contains ( "=" )) {
                    String[] arr = line.split ( "=" );
                    String[] md5Arr = md5Map.get ( arr[0] );
                    if (md5Arr == null) {
                        md5Arr = new String[2];
                        md5Map.put ( arr[0] , md5Arr );
                    }
                    md5Arr[0] = arr[1];
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace ( );
        } finally {
            if (br != null) {
                try {
                    br.close ( );
                } catch ( IOException e ) {
                    e.printStackTrace ( );
                }
            }
        }
        return md5Map;
    }

    public static ParamsBean readParamFromTxt(String paramsTxtPath) {
        ParamsBean params = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader ( new FileReader ( new File ( paramsTxtPath ) ) );
            String line = null;
            params = new ParamsBean ( );
            while ((line = br.readLine ( )) != null) {
                int pos = line.indexOf ( "=" );
                if (pos == -1) {
                    continue;
                }
                String key = line.substring ( 0 , pos );
                String value = line.substring ( pos + 1 , line.length ( ) );
                if (AppDefine.PARAM_TAG_RUNMODE.equalsIgnoreCase ( key )) {
                    params.runMode = value;
                }
                if (AppDefine.PARAM_TAG_STANDARDMD5PATH.equalsIgnoreCase ( key )) {
                    params.standardMd5Path = value;
                }
                if (AppDefine.PARAM_TAG_FOLDERPATH.equalsIgnoreCase ( key )) {
                    params.folderPath = value;
                }
                if (AppDefine.PARAM_TAG_EXCLUDEDPATHS.equalsIgnoreCase ( key )) {
                    if (value != null || !"".equals ( value )) {
                        String[] pathArr = value.split ( AppDefine.SEPARATOR );
                        if (pathArr != null && !"".equals ( pathArr )) {
                            params.excludedPaths = new HashSet<String> ( );
                            for (String path : pathArr) {
                                params.excludedPaths.add ( FileUtil.parsePath ( path ) );
                            }
                        }
                    }
                }
            }
        } catch (
                IOException e ) {
            e.printStackTrace ( );
        } finally {
            if (br != null) {
                try {
                    br.close ( );
                } catch ( IOException e ) {
                    e.printStackTrace ( );
                }
            }
        }
        return params;
    }

    /**
     * 将文件路径中的'/''\'替换为和操作系统一致
     *
     * @param path
     * @return
     */
    public static String parsePath(String path) {
        String newPath = path;
        if (path == null || "".equals ( path )) {
            return null;
        }
        if (OsUtil.CURISWINDOWS == true) {
            path.trim ( ).replaceAll ( "/" , "\\\\" );
        }
        return newPath;
    }
}
