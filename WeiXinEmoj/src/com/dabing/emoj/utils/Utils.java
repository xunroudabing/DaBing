package com.dabing.emoj.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Utils {
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
	public static float getScreenDensity(Context context) {
    	try {
    		DisplayMetrics dm = new DisplayMetrics();
	    	WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	    	manager.getDefaultDisplay().getMetrics(dm);
	    	return dm.density;
    	} catch(Exception ex) {
    	
    	}
    	return 1.0f;
    }
	public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
             || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
	public static int getLength(CharSequence sequence){
		double count=0;
		for(int i=0;i<sequence.length();i++){
			char c=sequence.charAt(i);
			if(isChinese(c)){
				count++;
			}else {
				count = count+0.5;
			}
		}
		return (int) Math.round(count);
	}
	
	public static String getRealPathFromURI(Activity activity,Uri contentUri) {     
		String[] proj = { MediaStore.Images.Media.DATA };       		
		Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);   
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);   
		cursor.moveToFirst();       
		return cursor.getString(column_index);   
	} 
	
}
