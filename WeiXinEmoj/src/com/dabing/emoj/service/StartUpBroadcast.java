package com.dabing.emoj.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 应用启动通知
 * @author DaBing
 *
 */
public class StartUpBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent service = new Intent(context, WeiXinEmojService.class);
		context.startService(service);
	}

}
