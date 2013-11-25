package com.dabing.emoj.advertise;

import com.dabing.emoj.utils.AppConfig;

import android.content.Context;
import android.util.Log;

public class AdManager {
	public enum AdType {
		QQ,
		UMENG,
		WAPS,
		YOUMI
	}
	public enum AdShowType{
		QQ_BANNER("QQ_BANNER"),
		WAPS_BANNER("WAPS_BANNER"),
		WAPS_MINI("WAPS_MINI"),
		WAPS_CUSTOM("WAPS_CUSTOM"),
		YOUMI_BANNER("YOUMI_BANNER");
		
		private String mDescription;		 
		private AdShowType(String description){
			mDescription = description;
		}
		
		public String getDescription(){
			return mDescription;
		}
		
		public static AdShowType getAdShowType(String description){
			for(AdShowType adShowType : AdShowType.values()){
				if(adShowType.getDescription().equals(description)){
					return adShowType;
				}
			}
			return WAPS_BANNER;
		}
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
		boolean b = AppConfig.getAdvertise_onV2(mContext);
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
			IAdvertise waps = getAdClass(AdType.WAPS);
			waps.init(mContext);
			IAdvertise youmi = getAdClass(AdType.YOUMI);
			youmi.init(mContext);
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
			IAdvertise youmi = getAdClass(AdType.YOUMI);
			youmi.release(mContext);
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
		}else if (adType == AdType.YOUMI) {
			return new Ad_YOUMI();
		}
		return new Ad_QQ();
	}
}
