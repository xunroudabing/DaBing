package com.dabing.emoj.qqconnect;

import com.dabing.emoj.utils.AppConfig;
import com.tencent.tauth.Tencent;

import android.content.Context;
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
			mTencent.setAccessToken(access_token, String.valueOf(expire_in));
			mTencent.setOpenId(openId);
		}
		Log.d(TAG, "QQConnect.isSessionValid:"+mTencent.isSessionValid());
	}
	
	public Tencent getTencent(){
		return mTencent;
	}
}
