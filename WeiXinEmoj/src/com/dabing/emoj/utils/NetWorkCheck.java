package com.dabing.emoj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkCheck {
	/**
	 * 检测网络连接状态
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context){
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean connect = false;
		if(manager != null){
			NetworkInfo info = manager.getActiveNetworkInfo();
			if(info == null){
				connect = false;
			}else {
				connect = info.isConnected();
			}
		}
		return connect;
	}
}
