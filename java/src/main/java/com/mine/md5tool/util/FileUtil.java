package com.mine.md5tool.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileUtil {

	public static void write2File(String filePath, String content) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(filePath), true));
			bw.write(content);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static StringBuffer readFromFile(String filePath) {
		StringBuffer ret = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(filePath)));
			String line = null;
			while ((line = br.readLine()) != null) {
				ret.append(line);
				ret.append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	public static void readMd5FromConfig(String standardMd5Path, Map<String, String[]> md5Map) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(standardMd5Path)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains("=")) {
					String[] arr = line.split("=");
					String[] md5Arr = md5Map.get(arr[0]);
					if (md5Arr == null) {
						md5Arr = new String[2];
						md5Map.put(arr[0], md5Arr);
					}
					md5Arr[0] = arr[1];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static int readParamFromConfig(String paramConfigPath, Map<String, String> paramMap) {
		int ret = -1;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(paramConfigPath)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains("=")) {
					String[] arr = line.split("=");
					paramMap.put(arr[0].trim(), arr[1].trim());
				}
			}
			ret = 0;
		} catch (IOException e) {
			e.printStackTrace ();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

}
