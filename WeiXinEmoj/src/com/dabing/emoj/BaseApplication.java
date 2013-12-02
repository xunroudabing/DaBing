package com.dabing.emoj;

import greendroid.app.GDApplication;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import com.dabing.emoj.activity.BonusGainActivity;
import com.dabing.emoj.activity.MainTab2Activity;
import com.dabing.emoj.db.PushEmojDatabaseHelper;
import com.dabing.emoj.exception.DefaultCrashHandler;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.umeng.common.net.p;
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

//			s1 主标题
//			s2 副标题
//			c 类型 1-表情 2-文本
//*****二级字段 obj		
//			i id 表情id			
//			n name 表情名称	
//			p 0-免费 1-会员 2-铜板
//			m 铜板数
//			t thumb 封面图片
//			d 描述
//			j json
			save(content);
			if(!hasNotified){
				//sendNotification(content);
			}else {
				//
			}
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
	//发送通知
	protected void sendNotification(String content){
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, "接收到了一条推送消息", System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent pIntent = new Intent(getApplicationContext(), BonusGainActivity.class);
		//intent.putExtra("data", data);
		//pIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(), 0, pIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(getApplicationContext(), "标题", content, pendingIntent);
		mNotificationManager.notify(0, notification);
	}
	/**
	 * 保存推送消息
	 * @param content
	 */
	protected void save(String content){
		try {
			JSONObject object = new JSONObject(content);
			//消息类型 1-表情 2-文本
			int c = object.getInt("c");
			if(c == 1){
				JSONObject json = object.getJSONObject("obj");
				String emojId = json.getString("i");
				String name = json.getString("n");
				int type = json.getInt("p");
				String thumb = json.getString("t");
				String des = json.getString("d");
				String data = json.getString("json");
				
				ContentValues cv = new ContentValues();
				cv.put(PushEmojDatabaseHelper.FIELD_EMOJID, emojId);
				cv.put(PushEmojDatabaseHelper.FIELD_NAME, name);
				cv.put(PushEmojDatabaseHelper.FIELD_TYPE, type);
				cv.put(PushEmojDatabaseHelper.FIELD_THUMB, thumb);
				cv.put(PushEmojDatabaseHelper.FIELD_DES, des);
				cv.put(PushEmojDatabaseHelper.FIELD_EMOJ, data);
				cv.put(PushEmojDatabaseHelper.FIELD_TIME, System.currentTimeMillis());
				
				PushEmojDatabaseHelper helper = new PushEmojDatabaseHelper(getApplicationContext());
				helper.insert(cv);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
}
