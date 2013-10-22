package com.dabing.emoj.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.content.ContentBody;
import org.json.JSONArray;
import org.json.JSONException;

import com.dabing.emoj.R;
import com.tencent.exmobwin.a.b;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.Html.TagHandler;
import android.util.Log;

public class AppConfig {
	static final String TAG = AppConfig.class.getSimpleName();
	public static final boolean DEBUG = false;
	//qq互联
	public static final String QQ_APPID = "100399626";
	//qq互联 权限
	public static final String QQ_SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,upload_photo,"
            + "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";
	//移动QQ包名
	public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
	//正式 http://t.qq.com/xunroudabing 测试 http://www.appchina.com/soft_detail_472820_0_10.html
	public static final String redirectUri="http://android.myapp.com/android/appdetail.jsp?appid=653908";
	//正式801278477 测试 801202538 测试
	public static final String clientId="801278477";
	//正式0e35e00fef0dacc616105cb20461ead1 测试 cc33fef861dad95cf7296643144b95cd
	public static final String clientSecret="0e35e00fef0dacc616105cb20461ead1";
	public static final String FOLDERNAME ="WeiXinEmoj";
	public static final String LOG_STRING = "Log";
	public static final String TEMP_STRING = "temp";
	public static final String EMOJ_STRING = "emoj";
	public static final String THUMB_STRING = "thumb";
	public static final String CACHE_STRING = "cache";
	public static final String ALBUM_STRING = "album";
	static final String FILENAME = "com.dabing.emoj";
	public static OAuth oAuth;
	/**
	 * 返回OAuth实体
	 * @return
	 */
	public static OAuth getOAuth(){
		if(oAuth == null){
			oAuth = new OAuthV2(clientId, clientSecret, redirectUri);
		}
		return oAuth;
	}
	
	/**
	 * 获取程序根目录
	 * @return
	 */
	public static String getRoot(){
		//没有sd卡
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			
			return Environment.getDataDirectory() + "/data/" + FILENAME + File.separator + "files" + File.separator;
		}		
		return Environment.getExternalStorageDirectory() + File.separator + FOLDERNAME + File.separator;
	}
	/**
	 * 获取log目录
	 * @return
	 */
	public static String getLog(){
		return getRoot() + LOG_STRING + File.separator;
	}
	/**
	 * 获取缓存文件夹
	 * @return
	 */
	public static String getTemp(){
		return getRoot() + TEMP_STRING + File.separator;
	}
	/**
	 * 获取表情文件夹
	 * @return
	 */
	public static String getEmoj(){
		return getRoot() + EMOJ_STRING + File.separator;
	}
	/**
	 * 获取缩略图文件夹
	 * @return
	 */
	public static String getThumb(){
		return getRoot() + THUMB_STRING + File.separator;
	}
	/**
	 * 获取cache文件夹
	 * @return
	 */
	public static String getCache(){
		return getRoot() + CACHE_STRING + File.separator;
	}
	/**
	 * 获取相册文件夹
	 * @return
	 */
	public static String getAblum(){
		return getRoot() + ALBUM_STRING + File.separator;
	}
	
	//***QQ互联相关****
	public static String getQQ_OpenId(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String ret = preferences.getString("QQ_OpenId", null);
		return ret;
	}
	public static void setQQ_OpenId(Context context,String openId){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("QQ_OpenId", openId);
		editor.commit();
	}
	public static String getQQ_AccessToken(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String ret = preferences.getString("QQ_AccessToken", null);
		return ret;
	}
	public static void setQQ_AccessToken(Context context,String accessToken){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("QQ_AccessToken", accessToken);
		editor.commit();
	}
	public static long getQQ_ExpiresIn(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		long ret = preferences.getLong("QQ_ExpiresIn", 0);
		return ret;
	}
	public static void setQQ_ExpiresIn(Context context,long expiresIn){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong("QQ_ExpiresIn", expiresIn);
		editor.commit();
	}
	/**
	 * 表情分类索引
	 * @param context
	 * @return
	 */
	public static String getEmojIndex(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String defValue = AppConstant.EMOJ_INDEX;
		String data = preferences.getString("EmojIndex", defValue);
		return data;
	}
	/**
	 * 设置表情分类索引
	 * @param context
	 * @param data
	 */
	public static void setEmojIndex(Context context,String data){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("EmojIndex", data);
		editor.commit();
	}
	/**
	 * 获取缓存表情数据
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getEmoj(Context context,String key){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String defaultEmoj = Emoj.getEmoj(context,key);
		String result = preferences.getString(key, defaultEmoj);
		return result;
	}
	/**
	 * 缓存数据
	 * @param context
	 * @param key
	 * @param json
	 */
	public static void setEmoj(Context context,String key,String json){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, json);
		editor.commit();
	}
	/**
	 * 常用列表
	 * @param context
	 * @return
	 */
	public static String getCommonUse(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("commonuse", null);
		return string;
	}
	public static void setCommonUse(Context context,String json){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("commonuse", json);
		editor.commit();
	}
	public static int getBackgroundResId(String background){
		int resid = R.drawable.textview_blue_selector;
		if(background.equals("blue")){
			resid = R.drawable.textview_blue_selector;			
		}else if (background.equals("red")) {
			resid = R.drawable.textview_red_selector;
		}else if (background.equals("orange")) {
			resid = R.drawable.textview_orange_selector;
		}else if (background.equals("pink")) {
			resid = R.drawable.textview_pink_selector;
		}else if (background.equals("brown")) {
			resid = R.drawable.textview_brown_selector;
		}else if (background.equals("lime")) {
			resid = R.drawable.textview_lime_selector;
		}else if (background.equals("teal")) {
			resid = R.drawable.textview_teal_selector;
		}else if (background.equals("purple")) {
			resid = R.drawable.textview_purple_selector;
		}else if (background.equals("magenta")) {
			resid = R.drawable.textview_magenta_selector;
		}
		return resid;
	}
	public static int getColor(String color){
		int resid = R.color.black;
		if(color.equals("blue")){
			resid = R.color.blue;			
		}else if (color.equals("red")) {
			resid = R.color.red;
		}else if (color.equals("orange")) {
			resid = R.color.orange;
		}else if (color.equals("pink")) {
			resid = R.color.pink;
		}else if (color.equals("brown")) {
			resid = R.color.brown;
		}else if (color.equals("lime")) {
			resid = R.color.lime;
		}else if (color.equals("teal")) {
			resid = R.color.teal;
		}else if (color.equals("purple")) {
			resid = R.color.purple;
		}else if (color.equals("magenta")) {
			resid = R.color.magenta;
		}
		return resid;
	}
	/**
	 * 广告开关
	 * @param context
	 * @return
	 */
	public static boolean getAdvertise_on(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean("advertise", AppConstant.AD_ENABLE);
		return b;
	}
	public static void setAdvertise_on(Context context,boolean b){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("advertise", b);
		editor.commit();
	}
	
	/**
	 * 新广告开关
	 * @param context
	 * @return
	 */
	public static boolean getAdvertise_onV2(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean("advertiseV2", AppConstant.AD_ENABLE);
		return b;
	}
	public static void setAdvertise_onV2(Context context,boolean b){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("advertiseV2", b);
		editor.commit();
	}
	/**
	 * 获取是否移除广告
	 * @param context
	 * @return
	 */
	public static boolean getAdvertiseRemove(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean("advertiseRemove", false);
		return b;
	}
	public static void setAdvertiseRemove(Context context,boolean b){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("advertiseRemove", b);
		editor.commit();
	}
	/**
	 * 获取广告TAG配置
	 * @param context
	 * @param tag ad_index1 ad_index2 ad_index3 ad_index4
	 * @return QQ_BANNER WAPS_BANNER WAPS_MINI WAPS_CUSTOM YOUMI_BANNER
	 */
	public static String getAdvertiseTAG(Context context,String tag){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String ret = preferences.getString("advertise_"+tag, AppConstant.AD_DEFAULT_ADSHOWTYPE);
		return ret;
	}
	
	public static void setAdvertiseTAG(Context context,String tag,String value){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("advertise_"+tag, value);
		editor.commit();
	}
	/**
	 * 关于本软件
	 * @param context
	 * @return
	 */
	public static String getNotice(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String def = context.getResources().getString(R.string.notice);
		String string = preferences.getString("notice", def);
		return string;
	}
	public static void setNotic(Context context,String notice){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("notice", notice);
		editor.commit();
	}
	/**
	 * 分享的微博内容
	 * @param context
	 * @return
	 */
	public static String getWeibo(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String def = context.getResources().getString(R.string.add_weibo_content);
		String string = preferences.getString("add_weibo_content", def);
		return string;
	}
	public static void setWeibo(Context context,String str){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("add_weibo_content", str);
		editor.commit();
	}
	/**
	 * 分享的微博图片url
	 * @param context
	 * @return
	 */
	public static String getSharePicUrl(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("share_pic_url", AppConstant.SHARE_IMG_URL);
		return string;
	}
	public static void setSharePicUrl(Context context,String picUrl){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("share_pic_url", picUrl);
		editor.commit();
	}
	/**
	 * umeng广告开关
	 * @param context
	 * @return
	 */
	public static boolean getUmengAdEnable(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean("umeng_ad_switch", false);
		return b;
	}
	public static void setUmengAdEnable(Context context,boolean b){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("umeng_ad_switch", b);
		editor.commit();
	}
	/**
	 * 获取广告类型 QQ WAPS UMENG
	 * @param context
	 * @return
	 */
	public static String getAdType(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("AdType", AppConstant.AD_TYPE);
		return string;
	}
	public static void setAdType(Context context,String type){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("AdType", type);
		editor.commit();
	}
	/**
	 * 获取万普广告形式 
	 * @param context
	 * @return BANNER MINI CUSTOM
	 */
	public static String getWAPS_AD_Type(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("WAPS_AD_Type", AppConstant.WAPS_AD_TYPE);
		return string;
	}
	public static void setWAPS_AD_Type(Context context,String adtype){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("WAPS_AD_Type", adtype);
		editor.commit();
	}
	/**
	 * 万普广告条位置1
	 * @param context
	 * @return BANNER MINI CUSTOM
	 */
	public static String getWAPS_INDEX1_AD_TYPE(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("WAPS_INDEX1_AD_TYPE", AppConstant.WAPS_INDEX1_AD_TYPE);
		return string;
	}
	public static void setWAPS_INDEX1_AD_TYPE(Context context,String adtype){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("WAPS_INDEX1_AD_TYPE", adtype);
		editor.commit();
	}
	
	/**
	 * 万普广告条位置2
	 * @param context
	 * @return BANNER MINI CUSTOM
	 */
	public static String getWAPS_INDEX2_AD_TYPE(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("WAPS_INDEX2_AD_TYPE", AppConstant.WAPS_INDEX2_AD_TYPE);
		return string;
	}
	public static void setWAPS_INDEX2_AD_TYPE(Context context,String adtype){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("WAPS_INDEX2_AD_TYPE", adtype);
		editor.commit();
	}
	
	/**
	 * 万普广告条位置3
	 * @param context
	 * @return BANNER MINI CUSTOM
	 */
	public static String getWAPS_INDEX3_AD_TYPE(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("WAPS_INDEX3_AD_TYPE", AppConstant.WAPS_INDEX3_AD_TYPE);
		return string;
	}
	public static void setWAPS_INDEX3_AD_TYPE(Context context,String adtype){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("WAPS_INDEX3_AD_TYPE", adtype);
		editor.commit();
	}
	/**
	 * vip列表
	 * @param context
	 * @return
	 */
	public static String getVip(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String def = context.getResources().getString(R.string.vip);
		String string = preferences.getString("vip", def);
		return string;
	}
	public static void setVip(Context context,String vip){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("vip", vip);
		editor.commit();
	}
	public static boolean getListenSwitch(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean("listen_switch", false);
		return b;
	}
	public static void setListenSwitch(Context context,boolean b){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("listen_switch", b);
		editor.commit();
	}
	public static String getListen(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("listen", null);
		return string;
	}
	public static void setListen(Context context,String string){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("listen", string);
		editor.commit();
	}
	public static String getUnListen(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("unlisten", null);
		return string;
	}
	public static void setUnListen(Context context,String string){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("unlisten", string);
		editor.commit();
	}
	/**
	 * 扩展菜单
	 * @param context
	 * @return
	 */
	public static String getMenuExtend(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("menu_extend", null);
		return string;
	}
	public static void setMenuExtend(Context context,String menu){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("menu_extend", menu);
		editor.commit();
	}
	/**
	 * 用户登录是否已过期
	 * @param context
	 * @return
	 */
	public static boolean getUserEnable(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean("userenable", false);
		return b;
	}
	public static void setUserEnable(Context context,boolean b){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("userenable", b);
		editor.commit();
		
	}
	/**
	 * 用户个人信息
	 * @param context
	 * @return
	 */
	public static String getUserInfo(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("userinfo", null);
		return string;
	}
	public static void setUserInfo(Context context,String userinfo){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("userinfo", userinfo);
		editor.commit();
	}
	/**
	 * 热门表情
	 * @param context
	 * @return
	 */
	public static String getHotEmoj(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String string = preferences.getString("hotemoj", AppConstant.EMOJ_HOT);
		return string;
	}
	public static void setHotEmoj(Context context,String json){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("hotemoj", json);
		editor.commit();
	}
	/**
	 * 是否第一次使用
	 * @param context
	 * @return
	 */
	public static boolean getFirstLogin(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean("first", true);
		return b;
	}
	public static void setFirstLogin(Context context,boolean b){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("first", b);
		editor.commit();
		
	}
	/**
	 * 趣图分类
	 * @param context
	 * @return
	 */
	public static String getCategory(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String str = preferences.getString("category1", AppConstant.CATEGORY_1);
		return str;
	}
	public static void setCategory(Context context,String data){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("category1", data);
		editor.commit();
	}
	/**
	 * 获取微信公共账号
	 * @param context
	 * @return
	 */
	public static String getPublicAccount(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String str = preferences.getString("publicAccount", AppConstant.WEIXIN_PUBLIC_ACCOUNT);
		return str;
	}
	/**
	 * 设置微信公共账号
	 * @param context
	 * @param account
	 */
	public static void setPublicAccount(Context context,String account){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("publicAccount", account);
		editor.commit();
	}
	public static JSONArray getNewEmojArray(Context context){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String json = preferences.getString("newemoj", AppConstant.EMOJ_NEW);
		if(json == null || json.equals("")){
			return null;
		}
		//Log.d(TAG, "json:"+json);
		JSONArray newArray = new JSONArray();
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			JSONArray array = new JSONArray(json);
			for(int i =0;i<array.length();i++){
				String id = array.getString(i);
				String key = String.format("version%d_emoj%s", versionCode,id);
				if(AppConfig.getIsNew(context, key)){
					newArray.put(id);
				}
			}
			return newArray;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	/**
	 * 将某新表情设为已读
	 * @param context
	 * @param id 表情id
	 */
	public static void setNewEmojArray(Context context,String id){
//		JSONArray array = getNewEmojArray(context);
//		if(array == null || array.length() <= 0){
//			return;
//		}
//		JSONArray newArray = new JSONArray();
//		try {
//			for(int i=0;i<array.length();i++){
//				String item = array.getString(i);
//				if(!id.equals(item)){
//					newArray.put(item);
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = preferences.edit();
//		editor.putString("newemoj", newArray.toString());
//		editor.commit();
		
		try {
			int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			String key = String.format("version%d_emoj%s", versionCode,id);
			Log.d(TAG, "setNewEmojArray key:"+key);
			AppConfig.setIsNew(context, key);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	//是否是新表情
	public static boolean isNewEmoj(Context context,String id){
		JSONArray array = getNewEmojArray(context);
		if(array == null){
			return false;
		}
		else if (array.length() <= 0) {
			return false;
		}
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			Log.e(TAG, e1.toString());
		}
		for(int i =0;i<array.length();i++){
			try {
				if(id.equals(array.getString(i))){
					String key = String.format("version%d_emoj%s", versionCode,id);
					return AppConfig.getIsNew(context, key);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		return false;

	}
	
	//点击计数 000-最近使用 099-热门表情
	public static int getHeaderClickCount(Context context,String id){
		if(id.equals("000")){
			return 10000;
		}else if (id.equals("099")) {
			return 9999;
		}
		String key = "header" + id;
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		int count = preferences.getInt(key, 0);
		return count;
	}
	
	public static void setHeaderClickCount(Context context,String id){
		if(id.equals("000")){
			return;
		}else if (id.equals("099")) {
			return;
		}
		int count = getHeaderClickCount(context, id);
		//最大就是9998
		if(count >= 9998){
			return;
		}
		count++;
		String key = "header" + id;
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, count);
		editor.commit();
	}
	//是否已下载
	public static boolean getIsDownload(Context context,String id){
		String key = "download" + id;
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean(key, false);
		return b;
	}
	public static void setIsDownload(Context context,String id){
		String key = "download" + id;
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, true);
		editor.commit();
	}
	/**
	 * 是否是新的
	 * @param context
	 * @param key unread_download 打包更新 unread_emotion 心情模式 unread_public 微信公众账号
	 * @return
	 */
	public static boolean getIsNew(Context context,String key){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		boolean b = preferences.getBoolean(key, true);
		return b;
	}
	public static void setIsNew(Context context,String key){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, false);
		editor.commit();
	}
	/**
	 * 
	 * @param context
	 * @param key
	 * @param filename
	 */
	public static void addEmotion(Context context,String key,String filename){
		try {
			String json = getEmoj(context, key);
			JSONArray array = null;
			if(json != null && !json.equals("")){
				array = new JSONArray(json);
			}else {
				array = new JSONArray();
			}
			if(!existEmotion(array, filename)){
				Log.d(TAG, "key:"+key+" filename:"+filename);
				array.put(filename);
				setEmoj(context, key, array.toString());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private static boolean existEmotion(JSONArray array,String filename){
		boolean exist = false;
		for(int i=0;i<array.length();i++){
			String item;
			try {
				item = array.getString(i);
				if(item.equals(filename)){
					exist = true;
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}			
		}
		return exist;
	}
	/**
	 * 获取上传的id集合
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getUploadIds(Context context,String id){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		String key = "upload_"+id;
		String result = preferences.getString(key, null);
		return result;
	}
	/**
	 * 设置上传的id集合
	 * @param context
	 * @param id
	 * @param data
	 */
	public static void setUploadIds(Context context,String id,String data){
		SharedPreferences preferences = context.getSharedPreferences(FILENAME, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		String key = "upload_"+id;
		editor.putString(key, data);
		editor.commit();
	}
}
