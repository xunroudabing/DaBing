package com.dabing.emoj.advertise;

import com.dabing.emoj.utils.AppConstant;

import net.youmi.android.AdManager;
import android.content.Context;

public class Ad_YOUMI implements IAdvertise {

	@Override
	public void init(Context context) {
		// TODO Auto-generated method stub
		AdManager.getInstance(context).init(AppConstant.YOUMI_ID, AppConstant.YOUMI_KEY, false);
	}

	@Override
	public void release(Context context) {
		// TODO Auto-generated method stub
		
	}

}
