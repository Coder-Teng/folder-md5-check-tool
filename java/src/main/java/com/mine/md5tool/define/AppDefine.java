package com.mine.md5tool.define;

/**
 * @author Teng
 * @create 2019-12-12
 */
public class AppDefine {

    /**
     * 从txt中读取参数 标志
     */
    public static final String PARAMS_FROM_TXT = "txt";

    /**
     * 从控制台读取参数 标志
     */
    public static final String PARAMS_FROM_CONSOLE = "console";

    public static final String PARAM_TAG_RUNMODE = "RunMode";

    public static final String PARAM_TAG_STANDARDMD5PATH = "StandardMd5Path";

    public static final String PARAM_TAG_FOLDERPATH = "FolderPath";

    public static final String PARAM_TAG_EXCLUDEDPATHS = "ExcludedPaths";

    public static final String SEPARATOR = "\\|\\|";

    public static final String WINDOWS_PATH_START = "";

    public static final String LINUX_PATH_START = "/";
    /**
     * 读取文件夹下文件、计算md5并保存（完整指令）
     */
    public static final String RUNMODE_READANDRECORDMD5_STR = "ReadAndRecordMd5";

    /**
     * 用给定的xml md5 记录文件,比较文件夹下文件的md5（完整指令）
     */
    public static final String RUNMODE_COMPAREFOLDERMD5_STR = "CompareFolderMd5";

    /**
     * 读取文件夹下文件、计算md5并保存（简单指令）
     */
    public static final String RUNMODE_READANDRECORDMD5_NUM = "1";

    /**
     * 用给定的xml md5 记录文件,比较文件夹下文件的md5（简单指令）
     */
    public static final String RUNMODE_COMPAREFOLDERMD5_NUM = "2";

    /**
     * csv文件头：文件名,标准MD5,检查所得MD5,检查结果\r\n
     */
    public static final String CSV_HEAD = "fileName,standard-md5,check-md5,check-ret\r\n";
}
