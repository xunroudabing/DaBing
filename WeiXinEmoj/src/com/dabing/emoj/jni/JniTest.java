package com.dabing.emoj.jni;

public class JniTest extends JniUtils {
	public static String ScanPictures(String path,int i){
		return fsScanFolders(path, i);
	}
}
