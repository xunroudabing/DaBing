package com.dabing.emoj.utils;

import java.io.File;
import java.io.FilenameFilter;

import com.dabing.emoj.db.UserDefineDataBaseHelper.UserDefineCursor;

import android.R.integer;


public class FileInfo {
	public String fileName;

	public String filePath;

	public long fileSize;

	public boolean IsDir;

	public int Count;

	public long ModifiedDate;

	public boolean Selected;

	public boolean canRead;

	public boolean canWrite;

	public boolean isHidden;

	public long dbId; // id in the database, if is from database
	//****数据库中的扩展属性*****
	public String dbName;
	public long dbTime;
	public int dbState;
	public String dbType;
	public String dbThumb;
	public static FileInfo GetFileInfo(String filePath) {
        File lFile = new File(filePath);
        if (!lFile.exists())
            return null;

        FileInfo lFileInfo = new FileInfo();
        lFileInfo.canRead = lFile.canRead();
        lFileInfo.canWrite = lFile.canWrite();
        lFileInfo.isHidden = lFile.isHidden();
        lFileInfo.fileName = Util.getNameFromFilepath(filePath);
        lFileInfo.ModifiedDate = lFile.lastModified();
        lFileInfo.IsDir = lFile.isDirectory();
        lFileInfo.filePath = filePath;
        lFileInfo.fileSize = lFile.length();
        return lFileInfo;
    }

    public static FileInfo GetFileInfo(File f, FilenameFilter filter, boolean showHidden) {
        FileInfo lFileInfo = new FileInfo();
        String filePath = f.getPath();
        File lFile = new File(filePath);
        lFileInfo.canRead = lFile.canRead();
        lFileInfo.canWrite = lFile.canWrite();
        lFileInfo.isHidden = lFile.isHidden();
        lFileInfo.fileName = f.getName();
        lFileInfo.ModifiedDate = lFile.lastModified();
        lFileInfo.IsDir = lFile.isDirectory();
        lFileInfo.filePath = filePath;
        if (lFileInfo.IsDir) {
            int lCount = 0;
            File[] files = lFile.listFiles(filter);

            // null means we cannot access this dir
            if (files == null) {
                return null;
            }

            for (File child : files) {
                if ((!child.isHidden() || showHidden)
                        && Util.isNormalFile(child.getAbsolutePath())) {
                    lCount++;
                }
            }
            lFileInfo.Count = lCount;

        } else {

            lFileInfo.fileSize = lFile.length();

        }
        return lFileInfo;
    }

	static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
	public static FileInfo GetFileInfo(UserDefineCursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		String filePath = cursor.getPath();
		FileInfo fileInfo = FileInfo.GetFileInfo(filePath);
//		FileInfo fileInfo = FileInfo.GetFileInfo(new File(filePath),
//				new FilenameFilter() {
//
//					@Override
//					public boolean accept(File dir, String filename) {
//						// TODO Auto-generated method stub
//						return filename.matches(IMAGE_PATTERN);
//					}
//				}, false);
		
		fileInfo.dbId = cursor.getId();
		fileInfo.dbName = cursor.getName();
		fileInfo.dbState = cursor.getState();
		fileInfo.dbThumb = cursor.getThumb();
		fileInfo.dbTime = cursor.getTime();
		fileInfo.Count = cursor.getChildSize();
		return fileInfo;

	}
    
}
