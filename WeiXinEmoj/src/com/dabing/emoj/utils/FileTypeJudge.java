package com.dabing.emoj.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

/**  
 * 文件类型判断类  
 */  
public final class FileTypeJudge {   
    static final String TAG = FileTypeJudge.class.getSimpleName();
    /**  
     * Constructor  
     */  
    private FileTypeJudge() {}   
       
    /**  
     * 将文件头转换成16进制字符串  
     *   
     * @param 原生byte  
     * @return 16进制字符串  
     */  
    private static String bytesToHexString(byte[] src){   
           
        StringBuilder stringBuilder = new StringBuilder();      
        if (src == null || src.length <= 0) {      
            return null;      
        }      
        for (int i = 0; i < src.length; i++) {      
            int v = src[i] & 0xFF;      
            String hv = Integer.toHexString(v);      
            if (hv.length() < 2) {      
                stringBuilder.append(0);      
            }      
            stringBuilder.append(hv);      
        }      
        return stringBuilder.toString();      
    }   
    private static String getURLContent(String url) throws Exception{
    	URLConnection connection = null;
    	InputStream is = null;
    	try {
			URL mUrl=new URL(url);
			connection = mUrl.openConnection();
			is = connection.getInputStream();
			return getFileContent(is);
		} 
    	catch (EOFException e) {
			// TODO: handle exception
    		Log.e(TAG, e.toString());
    		return getURLContent(url);
		}
    	catch (IOException e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			throw e;
		}
    	catch (Exception e) {
			// TODO: handle exception
    		Log.e(TAG, e.toString());
    		throw e;
		}
		
    }
      private static String getFileContent(InputStream is) throws IOException{
    	  byte[] b = new byte[28];            
          InputStream inputStream = null;   
          try {
			inputStream = is;			
			inputStream.read(b, 0, 28);  
		} catch (IOException e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			throw e;
		}finally{
			 if (inputStream != null) {   
	                try {   
	                    inputStream.close();   
	                } catch (IOException e) {   
	                	Log.e(TAG, e.toString());
	                    throw e;   
	                }   
	            }   
		}
		 return bytesToHexString(b);   
      }
    /**  
     * 得到文件头  
     *   
     * @param filePath 文件路径  
     * @return 文件头  
     * @throws IOException  
     */  
    private static String getFileContent(String filePath) throws IOException {   
           
        byte[] b = new byte[28];   
           
        InputStream inputStream = null;   
           
        try {   
            inputStream = new FileInputStream(filePath);   
            inputStream.read(b, 0, 28);   
        } catch (IOException e) {   
            e.printStackTrace();   
            throw e;   
        } finally {   
            if (inputStream != null) {   
                try {   
                    inputStream.close();   
                } catch (IOException e) {   
                    e.printStackTrace();   
                    throw e;   
                }   
            }   
        }   
        return bytesToHexString(b);   
    }   
    public static FileType getTypeFromURL(String url) throws Exception{
    	 String fileHead = getURLContent(url);
         
         if (fileHead == null || fileHead.length() == 0) {   
             return null;   
         }   
            
         fileHead = fileHead.toUpperCase();   
            
         FileType[] fileTypes = FileType.values();   
            
         for (FileType type : fileTypes) {   
             if (fileHead.startsWith(type.getValue())) {   
                 return type;   
             }   
         }   
            
         return null;   
    }
    public static FileType getType(InputStream is) throws IOException{
        
        String fileHead = getFileContent(is);   
           
        if (fileHead == null || fileHead.length() == 0) {   
            return null;   
        }   
           
        fileHead = fileHead.toUpperCase();   
           
        FileType[] fileTypes = FileType.values();   
           
        for (FileType type : fileTypes) {   
            if (fileHead.startsWith(type.getValue())) {   
                return type;   
            }   
        }   
           
        return null;   
    }   
    /**  
     * 判断文件类型  
     *   
     * @param filePath 文件路径  
     * @return 文件类型  
     */  
    public static FileType getType(String filePath) throws IOException {   
           
        String fileHead = getFileContent(filePath);   
           
        if (fileHead == null || fileHead.length() == 0) {   
            return null;   
        }   
           
        fileHead = fileHead.toUpperCase();   
           
        FileType[] fileTypes = FileType.values();   
           
        for (FileType type : fileTypes) {   
            if (fileHead.startsWith(type.getValue())) {   
                return type;   
            }   
        }   
           
        return null;   
    }   
}  

