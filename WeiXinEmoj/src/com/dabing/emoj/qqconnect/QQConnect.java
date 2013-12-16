package com.dabing.emoj.qqconnect;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.utils.AppConfig;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
/**
 * QQ互联
 * @author DaBing
 *
 */
public class QQConnect {
	Tencent mTencent;
	static QQConnect instance;
	Context mContext;
	static final String TAG = QQConnect.class.getSimpleName();
	private QQConnect(Context context){
		mContext = context;
		
	}
	
	public static QQConnect createInstance(Context context){
		if(instance == null){
			instance = new QQConnect(context);
		}
		return instance;
	}
	
	public void Init(){
		Log.d(TAG, "QQConnect.Init()...");		
		mTencent = Tencent.createInstance(AppConfig.QQ_APPID, mContext);
		String access_token =  AppConfig.getQQ_AccessToken(mContext);
		String openId = AppConfig.getQQ_OpenId(mContext);
		long expire = AppConfig.getQQ_ExpiresIn(mContext);
		long expire_in = (expire - System.currentTimeMillis())/1000;
		if(access_token != null && openId != null && expire > 0){
			mTencent.setOpenId(openId);
			mTencent.setAccessToken(access_token, String.valueOf(expire_in));
		}
		Log.d(TAG, "QQConnect.isSessionValid:"+mTencent.isSessionValid());
	}
	/**
	 * 退出登录
	 */
	public void Logout(){
		if(mTencent != null){
			mTencent.logout(mContext);
		}
		AppConfig.setQQ_AccessToken(mContext, null);
		AppConfig.setQQ_ExpiresIn(mContext, 0);
		AppConfig.setQQ_OpenId(mContext, null);
		AppConfig.setUserInfo(mContext, null);
	}
	public Tencent getTencent(){
		return mTencent;
	}
	
	public void shareToQQ(Activity activity,String filepath){
		try {
			Bundle bundle = new Bundle();
			bundle.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, Tencent.SHARE_TO_QQ_TYPE_IMAGE);
			bundle.putString(Tencent.SHARE_TO_QQ_IMAGE_LOCAL_URL, filepath);
			mTencent.shareToQQ(activity, bundle, new IUiListener() {
				
				@Override
				public void onError(UiError arg0) {
					// TODO Auto-generated method stub
					Log.d(TAG, "shareToQQ onError:"+arg0.errorMessage);
				}
				
				@Override
				public void onComplete(JSONObject arg0) {
					// TODO Auto-generated method stub
					Log.d(TAG, "shareToQQ onComplete:"+arg0);
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					Log.d(TAG, "onCancel");
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	/**
	 * 分享到QQ空间
	 * @param title
	 * @param webUrl
	 * @param imgUrl
	 * @param comment
	 * @param summary
	 * @param type
	 * @return
	 */
	public void shareToQZone(String title,String webUrl,String imgUrl,String comment,String summary,String type){
		try {
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putString("url", webUrl);
			if(comment != null){
				bundle.putString("comment", comment);
			}
			if(summary != null){
				bundle.putString("summary", summary);
			}
			bundle.putString("images", imgUrl);
			bundle.putString("type", type);
			bundle.putString("site", "微信表情包");
			bundle.putString("fromurl", webUrl);
			//JSONObject json = mTencent.request(Constants.GRAPH_ADD_SHARE, bundle, Constants.HTTP_POST);
			mTencent.requestAsync(Constants.GRAPH_ADD_SHARE, bundle, Constants.HTTP_POST, new IRequestListener() {
				
				@Override
				public void onUnknowException(Exception exception, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onSocketTimeoutException(
						SocketTimeoutException sockettimeoutexception, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onNetworkUnavailableException(
						NetworkUnavailableException networkunavailableexception, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onMalformedURLException(
						MalformedURLException malformedurlexception, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onJSONException(JSONException jsonexception, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onIOException(IOException ioexception, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onHttpStatusException(HttpStatusException httpstatusexception,
						Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onConnectTimeoutException(
						ConnectTimeoutException connecttimeoutexception, Object obj) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(JSONObject jsonobject, Object obj) {
					// TODO Auto-generated method stub
					Log.d(TAG, "onComplete:"+jsonobject.toString());
				}
			}, null);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
}
