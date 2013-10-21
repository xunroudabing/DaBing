package com.dabing.emoj.advertise;

import com.dabing.emoj.utils.AppConstant;

import com.dabing.ads.*;
import android.content.Context;
import android.util.Log;

public class Ad_WAPS implements IAdvertise {
	static final String TAG = Ad_WAPS.class.getSimpleName();
	public void init(Context context) {
		// TODO Auto-generated method stub
		try {
			//AppConnect.getInstance(context);	
			AppConnect.getInstance(AppConstant.WAPS_ID, AppConstant.WAPS_PID, context);
			AppConnect.getInstance(context).setAdViewClassName("com.dabing.emoj.advertise.WAPS_ADView");
			// 禁用错误报告
			AppConnect.getInstance(context).setCrashReport(false);
			
			//初始化广告数据
			AppConnect.getInstance(context).initAdInfo();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}

	public void release(Context context) {
		// TODO Auto-generated method stub
		AppConnect.getInstance(context).close();	
	}

}
