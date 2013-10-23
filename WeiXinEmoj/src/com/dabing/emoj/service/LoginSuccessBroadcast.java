package com.dabing.emoj.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LoginSuccessBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		Intent service = new Intent(context, WeiXinEmojLoginService.class);
//		context.startService(service);
		Intent service = new Intent(context, WeiXinEmojLoginServiceV2.class);
		context.startService(service);
	}

}
