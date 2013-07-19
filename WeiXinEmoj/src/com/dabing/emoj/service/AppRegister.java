package com.dabing.emoj.service;


import com.dabing.emoj.utils.AppConstant;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final IWXAPI api = WXAPIFactory.createWXAPI(context, AppConstant.WEIXIN_APPID);

		api.registerApp(AppConstant.WEIXIN_APPID);
	}

}
