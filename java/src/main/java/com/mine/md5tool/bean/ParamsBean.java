package com.mine.md5tool.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Teng
 * @create 2019-12-12
 */
public class ParamsBean {
    public String runMode;
    public String standardMd5Path;
    public String folderPath;
    public Set<String> excludedPaths;

    public ParamsBean() {
    }

    public ParamsBean(String runMode , String standardMd5Path , String folderPath) {
        this.runMode = runMode;
        this.standardMd5Path = standardMd5Path;
        this.folderPath = folderPath;
    }

    @Override
    public String toString() {
        return "RunMode=" + runMode + "\r\n" +
                "StandardMd5Path=" + standardMd5Path + "\r\n" +
                "FolderPath=" + folderPath + "\r\n" +
                "ExcludedPaths=" + excludedPaths;
    }
}
