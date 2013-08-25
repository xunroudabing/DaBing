package com.dabing.emoj;

import java.io.File;

import android.util.Log;

import com.dabing.emoj.exception.DefaultCrashHandler;
import com.dabing.emoj.utils.AppConfig;


import greendroid.app.GDApplication;

public class BaseApplication extends GDApplication {
	static final String TAG = BaseApplication.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			RegCrashHandler();
			InitFolder();
			clearTemp();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	//注册异常处理
	private void RegCrashHandler(){
		if(AppConfig.DEBUG){
			return;
		}
		DefaultCrashHandler mHandler = DefaultCrashHandler.getInstance();
		mHandler.Init(getApplicationContext());
	}
	//文件夹初始化
	private void InitFolder(){
		File root = new File(AppConfig.getRoot());
		if(!root.exists()){
			root.mkdirs();
		}
		File logFile = new File(AppConfig.getLog());
		if(!logFile.exists()){
			logFile.mkdirs();
		}
		File emojFile = new File(AppConfig.getEmoj());
		if(!emojFile.exists()){
			emojFile.mkdirs();
		}
		File tempFile = new File(AppConfig.getTemp());
		if(!tempFile.exists()){
			tempFile.mkdirs();
		}
		File thumbFile = new File(AppConfig.getThumb());
		if(!thumbFile.exists()){
			thumbFile.mkdirs();
		}
		File cacheFile = new File(AppConfig.getCache());
		if(!cacheFile.exists()){
			cacheFile.mkdirs();
		}
		File albumFile = new File(AppConfig.getAblum());
		if(!albumFile.exists()){
			albumFile.mkdirs();
		}
		//InitShare();
	}
	private void clearTemp(){
		try {
			String path = AppConfig.getTemp();
			File file = new File(path);
			if(!file.exists()){
				return;
			}
			String[] files = file.list();
			for(int i =0;i<files.length;i++){
				String filename = files[i];
				if(filename != null && !filename.equals("")){
					String filepath = path+filename;
					Log.d(TAG, "del:"+filepath);
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
}
