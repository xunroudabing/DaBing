package com.dabing.emoj.utils;

import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.oauthv3.OAuthV3;
import com.tencent.weibo.oauthv3.OAuthV3Client;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenStore {
	public static String fileName = "token_store";
	static final String PUBLIC_ACCESS_QUERYSTRING="PUBLIC_ACESS_QUERYSTRING";
	static final String PRIVATE_ACCESS_QUERYSTRING="PERSONAL_ACCESS_QUERYSTRING";
	static final String TAG = TokenStore.class.getSimpleName();
	public static void store(Context context, String responseData) {
		SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);		
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PUBLIC_ACCESS_QUERYSTRING, responseData);		
		editor.commit();		
	}
	public static String get(Context context){
		SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		String responseData = settings.getString(PUBLIC_ACCESS_QUERYSTRING, AppConstant.OAUTH_STRING);
		return responseData; 
	}
	public static String getPrivate(Context context){
		SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		String responseData = settings.getString(PRIVATE_ACCESS_QUERYSTRING, null);
		return responseData; 
	}
	/**
	 * 存储用户的response
	 * @param context
	 * @param responseData
	 */
	public static void storePrivate(Context context,String responseData){
		SharedPreferences pre = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putString(PRIVATE_ACCESS_QUERYSTRING, responseData);
		editor.commit();
	}
	/**
	 * 存储公共的response
	 * @param context
	 * @param responseData
	 */
	public static void storePublic(Context context,String responseData){
		SharedPreferences pre = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putString(PUBLIC_ACCESS_QUERYSTRING, responseData);
		editor.commit();
	}
	public static OAuthV2 fetch(Context context) {
		SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		String responseData = settings.getString(PUBLIC_ACCESS_QUERYSTRING, AppConstant.OAUTH_STRING);
		if(responseData == null){
			return null;
		}
		//Log.d(TAG, "fetch OAuth:"+responseData);
		OAuthV2 oAuthV2 = (OAuthV2) AppConfig.getOAuth();
		boolean b = OAuthV2Client.parseAccessTokenAndOpenId(responseData, oAuthV2);
		if(!b){
			return null;
		}
		return oAuthV2;
	}
	public static OAuthV2 fetchPrivate(Context context){
		SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		String responseData = settings.getString(PRIVATE_ACCESS_QUERYSTRING, null);
		if(responseData == null){
			return null;
		}
		//Log.d(TAG, "fetch PrivateOAuth:"+responseData);
		OAuthV2 oAuthV2 = (OAuthV2) AppConfig.getOAuth();
		boolean b = OAuthV2Client.parseAccessTokenAndOpenId(responseData, oAuthV2);
		if(!b){
			return null;
		}
		return oAuthV2;
	}

	public static void clear(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = settings.edit();
		
		editor.clear();  
        editor.commit(); 
	}	
}
