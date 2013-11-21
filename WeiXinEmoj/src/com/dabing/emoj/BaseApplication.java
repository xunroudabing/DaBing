package com.dabing.emoj;

import greendroid.app.GDApplication;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.dabing.emoj.exception.DefaultCrashHandler;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushClient.MiPushClientCallback;

public class BaseApplication extends GDApplication {
	static final String TAG = BaseApplication.class.getSimpleName();
	// 小米推送服务调试 开发完成后关闭该日志
	static {
		Constants.useOfficial();
		LoggerInterface newLogger = new LoggerInterface() {

			@Override
			public void setTag(String tag) {
				// ignore
			}

			@Override
			public void log(String content, Throwable t) {
				Log.d(PUSHTAG, content, t);
			}

			@Override
			public void log(String content) {
				Log.d(PUSHTAG, content);
			}
		};
		Logger.setLogger(newLogger);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			RegCrashHandler();
			initPushService();
			InitFolder();
			clearTemp();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	// 注册异常处理
	private void RegCrashHandler() {
		if (AppConfig.DEBUG) {
			return;
		}
		DefaultCrashHandler mHandler = DefaultCrashHandler.getInstance();
		mHandler.Init(getApplicationContext());
	}

	// 文件夹初始化
	private void InitFolder() {
		File root = new File(AppConfig.getRoot());
		if (!root.exists()) {
			root.mkdirs();
		}
		File logFile = new File(AppConfig.getLog());
		if (!logFile.exists()) {
			logFile.mkdirs();
		}
		File emojFile = new File(AppConfig.getEmoj());
		if (!emojFile.exists()) {
			emojFile.mkdirs();
		}
		File tempFile = new File(AppConfig.getTemp());
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		File thumbFile = new File(AppConfig.getThumb());
		if (!thumbFile.exists()) {
			thumbFile.mkdirs();
		}
		File cacheFile = new File(AppConfig.getCache());
		if (!cacheFile.exists()) {
			cacheFile.mkdirs();
		}
		File albumFile = new File(AppConfig.getAblum());
		if (!albumFile.exists()) {
			albumFile.mkdirs();
		}
		// InitShare();
	}

	private void clearTemp() {
		try {
			String path = AppConfig.getTemp();
			File file = new File(path);
			if (!file.exists()) {
				return;
			}
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				String filename = files[i];
				if (filename != null && !filename.equals("")) {
					String filepath = path + filename;
					Log.d(TAG, "del:" + filepath);
					try {
						File tempFile = new File(filepath);
						tempFile.delete();
					} catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	// ****小米推送服务相关*****
	public boolean initPushService() {
		boolean shouldInit = true;
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningServiceInfo> serviceInfos = am.getRunningServices(1000);
		for (RunningServiceInfo info : serviceInfos) {
			if (info.pid == Process.myPid()
					&& (getPackageName() + ":pushservice").equals(info.process)) {
				shouldInit = false;
			}
		}
		if (shouldInit) {
			CallbackImpl callback = new CallbackImpl();
			MiPushClient.initialize(this, AppConstant.MI_APP_ID,
					AppConstant.MI_APP_KEY, callback);
			MiPushClient.subscribe(this, "emoj", null);
			return true;
		}
		return false;
	}

	private static final String PUSHTAG = "XIAOMI";

	public class CallbackImpl extends MiPushClientCallback {

		@Override
		public void onUnsubscribeResult(long resultCode, String reason,
				String topic) {
			Log.d(PUSHTAG, "onUnsubscribeResult is called.");
		}

		@Override
		public void onSubscribeResult(long resultCode, String reason,
				String topic) {
			Log.d(PUSHTAG, "onSubscribeResult is called.");
		}

		@Override
		public void onReceiveMessage(String content, String alias,
				String topic, boolean hasNotified) {
			Log.d(PUSHTAG, "onReceiveMessage is called. " + content + ", "
					+ alias + ", " + topic + ", " + hasNotified);
		}

		@Override
		public void onInitializeResult(long resultCode, String reason,
				final String regID) {
			Log.d(PUSHTAG, "onInitializeResult is called. " + regID);			
		}

		@Override
		public void onCommandResult(String command, long resultCode,
				String reason, List<String> params) {
			Log.d(PUSHTAG, "onCommandResult is called. " + command + ": "
					+ params);
		}
	}
}
