package com.dabing.emoj.advertise;

import com.dabing.emoj.utils.AppConfig;

import android.content.Context;
import android.util.Log;

public class AdManager {
	public enum AdType {
		QQ,
		UMENG,
		WAPS
	}
	static final String TAG = AdManager.class.getSimpleName();
	Context mContext;
	public AdManager(Context context){
		mContext = context;
	}
	/**
	 * 获取广告类型
	 * @return
	 */
	public AdType getAdType(){
		String config = AppConfig.getAdType(mContext);
		if(config.toUpperCase().equals("QQ")){
			return AdType.QQ;
		}else if (config.toUpperCase().equals("UMENG")) {
			return AdType.UMENG;
		}else if (config.toUpperCase().equals("WAPS")) {
			return AdType.WAPS;
		}
		return AdType.QQ;
	}
	/**
	 * 广告是否开启
	 * @return
	 */
	public boolean getEnable(){
		boolean b = AppConfig.getAdvertise_on(mContext);
		return b;
	}
	/**
	 * 广告初始化,应该在应用中第一个Activity类中(启动的第一个类)执行
	 */
	public void init(){
//		if(!getEnable()){
//			return;
//		}
		try {
			IAdvertise ad = getAdClass(getAdType());
			ad.init(mContext);
			Log.d(TAG, "AdManager.init");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
	public void release(){
//		if(!getEnable()){
//			return;
//		}
		try {
			IAdvertise ad = getAdClass(getAdType());
			ad.release(mContext);
			Log.d(TAG, "AdManager.release");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
	//广告工厂
	private IAdvertise getAdClass(AdType adType){
		if(adType == AdType.QQ){
			return new Ad_QQ();
		}else if (adType == AdType.UMENG) {
			return new Ad_UMENG();
		}else if (adType == AdType.WAPS) {
			return new Ad_WAPS();
		}
		return new Ad_QQ();
	}
}
