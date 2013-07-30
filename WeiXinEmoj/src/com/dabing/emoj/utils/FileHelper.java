package com.dabing.emoj.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import android.util.Log;

public class FileHelper {
	public interface OnScanFileListener {
		/**
		 * 获得目录下有图片的file
		 * @param file
		 */
		void get(File file);
		/**
		 * true-不扫描该file false-扫描该file 
		 * @param file
		 * @return
		 */
		boolean interception(File file);
	}
	boolean cancel = false;
	public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
	static final String[] IMAGE_PREFIX = {"gif","png","jpg","jpeg"};
	static final String TAG = FileHelper.class.getSimpleName();
	public FileHelper(){}
	/**
	 * 停止扫描
	 * @param b true-停止 false-开始
	 */
	public synchronized void setCancel(boolean b){
		cancel = b;
	}
	/**
	 * 
	 * @param path
	 * @param listener
	 */
	public void getFileContainsImage(String path,OnScanFileListener listener){
		try {
			if(cancel){
				return;
			}
			File file = new File(path);
			if(!file.isDirectory()){
				return;
			}
			//列出所有文件夹
			File[] files = file.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					return pathname.isDirectory();
				}
			});
			for(File subFile : files){
				if(listener != null){
					if(listener.interception(subFile)){
						continue;
					}
				}
				//搜索到目录下有图的文件夹
				if(isContainsImage(subFile)){
					if(listener != null){
						listener.get(subFile);
					}
				}
				getFileContainsImage(subFile.getAbsolutePath(), listener);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	/**
	 * 某文件下是否有图片
	 * @param file
	 * @return
	 */
	public boolean isContainsImage(File file){
		try {
			File[] files = file.listFiles();
			for(File subFile : files){
				if(subFile.getName().matches(IMAGE_PATTERN)){
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	/**
	 * 在一个文件夹下搜索有文件名里包含filename的文件
	 * @param path 
	 * @param filename
	 * @return
	 */
	public String find(String path,String filename){
		try {
			File file = new File(path);
			if(!file.exists()){
				return null;
			}
			String[] result = file.list(new MyFileNameFilter(filename));
			if(result != null){
				if(result.length > 0){
					return result[0];
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}
	  public static byte[] getBytesFromFile(File f) {
	        if (f == null) {
	            return null;
	        }
	        try {
	            FileInputStream stream = new FileInputStream(f);
	            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
	            byte[] b = new byte[1000];
	            for (int n;(n = stream.read(b)) != -1;) {

				out.write(b, 0, n);
			}
	            stream.close();
	            out.close();
	            return out.toByteArray();
	        } catch (IOException e) {
	        	Log.e(TAG, e.toString());
	        }
	        return null;
	    }
	class MyFileNameFilter implements FilenameFilter{
		String mKey;
		public MyFileNameFilter(String key){
			mKey = key;
		}
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			if(filename.indexOf(mKey) != -1){
				return true;
			}
			return false;
		}
		
	}
	//把图片过滤出来
	public class FileContainsImageFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return filename.matches(IMAGE_PATTERN);
		}
		
	}
}
