package com.mine.md5tool.util;

/**
 * @author Teng
 * @create 2019-12-13
 */
public class OsUtil {

    public static final boolean CURISWINDOWS;
    public static final boolean CURISLINUX;

    static {
        String osName = System.getProperty ( "os.name" );
        System.out.println ( "os=" + osName );
        if (osName.trim ( ).toLowerCase ( ).startsWith ( "win" )) {
            CURISWINDOWS = true;
        } else {
            CURISWINDOWS = false;
        }
        if (osName.trim ( ).toLowerCase ( ).startsWith ( "linux" )) {
            CURISLINUX = true;
        } else {
            CURISLINUX = false;
        }
    }

}
