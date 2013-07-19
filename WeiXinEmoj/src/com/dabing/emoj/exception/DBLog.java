package com.dabing.emoj.exception;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dabing.emoj.utils.AppConfig;

import android.util.Log;


public class DBLog {
	private static final String LOG_STRING = "log";
	public static void d(String Tag,String message){
		long l = System.currentTimeMillis();
		Date date = new Date(l);
		SimpleDateFormat format = new SimpleDateFormat("_yyyy_MM_dd");
		
		String timeprefix = format.format(date);
		String filename = LOG_STRING+timeprefix+".txt";
		String path = AppConfig.getLog()+filename;
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(Tag, e.toString());
			}
		}
		
		try {

			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "gb2312"));
			bufferedWriter.write(String.format("\n\r--------Tag:{%s} %s----------", Tag,date.toLocaleString()));
			bufferedWriter.write("\n\r\n\r"+message);
			bufferedWriter.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.toString());
		}
	}
	
	public static void d(String FileName,String Tag,String message){
		long l = System.currentTimeMillis();
		Date date = new Date(l);
		SimpleDateFormat format = new SimpleDateFormat("_yyyy_MM_dd");
		
		//String timeprefix = format.format(date);
		String filename = FileName+".txt";
		String path = AppConfig.getLog()+filename;
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(Tag, e.toString());
			}
		}
		
		try {			
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true), "gb2312"));
			bufferedWriter.write(String.format("\n\r--------Tag:{%s} %s----------", Tag,date.toLocaleString()));
			bufferedWriter.write("\n\r\n\r"+message);
			bufferedWriter.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.toString());
		}
	}
	
}

