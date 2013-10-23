package com.dabing.emoj.utils;

import java.io.File;
import java.io.FilenameFilter;

import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.db.UserDefineDataBaseHelper.UserDefineCursor;

import android.R.integer;
import android.database.Cursor;
import android.text.Html.TagHandler;
import android.util.Log;


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
	public static FileInfo GetFileInfo(Cursor cursor) {
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		String filePath = cursor.getString(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_PATH));
		FileInfo fileInfo = FileInfo.GetFileInfo(filePath);

		
//		fileInfo.dbId = cursor.getId();
//		fileInfo.dbName = cursor.getName();
//		fileInfo.dbState = cursor.getState();
//		fileInfo.dbThumb = cursor.getThumb();
//		fileInfo.dbTime = cursor.getTime();		
//		fileInfo.Count = cursor.getChildSize();
		//文件夹不存在，数据直接从数据库取
		if(fileInfo == null){
			fileInfo = new FileInfo();
			fileInfo.filePath = filePath;
			fileInfo.fileName = Util.getNameFromFilepath(filePath);
		}
		fileInfo.dbId = cursor.getInt(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_ID));
		fileInfo.dbName = cursor.getString(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_NAME));
		fileInfo.dbState = cursor.getInt(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_STATE));
		fileInfo.dbThumb = cursor.getString(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_THUMB));
		fileInfo.dbTime = cursor.getLong(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_TIME));	
		fileInfo.Count = cursor.getInt(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_CHILDSIZE));
		fileInfo.dbType = cursor.getString(cursor.getColumnIndexOrThrow(UserDefineDataBaseHelper.FIELD_TYPE));
		return fileInfo;

	}
    
}
