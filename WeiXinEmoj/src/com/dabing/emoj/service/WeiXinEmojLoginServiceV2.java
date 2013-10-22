package com.dabing.emoj.service;

import com.dabing.emoj.qqconnect.QQConnect;
import com.tencent.tauth.Tencent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WeiXinEmojLoginServiceV2 extends Service {
	Tencent mTencent;
	static final String TAG = WeiXinEmojLoginServiceV2.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mTencent = QQConnect.createInstance(getApplicationContext()).getTencent();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
