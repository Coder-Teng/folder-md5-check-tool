package com.mine.md5tool.util;

import java.io.File;
import java.util.List;

public class FolderTraverseUtil {

	public static void getAllFile(String filePath, List<File> fileList) {
		File f = new File(filePath);

		if (f.exists() && f.isDirectory()) {
			for (File file : f.listFiles()) {
				getAllFile(file.getAbsolutePath(), fileList);
			}
		}
		if (f.exists() && f.isFile()) {
			fileList.add(f);
		}
	}
}
