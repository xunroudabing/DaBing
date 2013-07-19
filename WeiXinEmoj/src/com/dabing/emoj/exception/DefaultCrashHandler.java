package com.dabing.emoj.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.DialogFactory;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

public class DefaultCrashHandler implements UncaughtExceptionHandler {
	private Context mContext;
	private UncaughtExceptionHandler mDefaultHandler;
	private static DefaultCrashHandler instance;
	public static final String TAG = DefaultCrashHandler.class.getSimpleName();
	private DefaultCrashHandler(){};
	/**
	 * 单例模式,获取实例
	 * @return
	 */
	public static DefaultCrashHandler getInstance(){
		if(instance == null){
			instance = new DefaultCrashHandler();
		}
		return instance;
	}
	/**
	 * 初始化,将handler注册
	 * @param context
	 */
	public void Init(Context context){
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		 if (!handleException(ex) && mDefaultHandler != null) {  
	            //如果用户没有处理则让系统默认的异常处理器来处理  
	            mDefaultHandler.uncaughtException(thread, ex);  
	        } else {  
	           
	        }  
	}
	private void killProcess(){
//		 try {  
//             Thread.sleep(3000);  
//         } catch (InterruptedException e) {  
//             Log.e(TAG, e.toString());  
//         }  
         android.os.Process.killProcess(android.os.Process.myPid());  
         System.exit(10);  
	}
	private boolean handleException(Throwable ex){
		if(ex == null){
			return true;
		}
		String str = mContext.getString(R.string.ex_default);
		//内存溢出
		if(ex instanceof OutOfMemoryError){
			str = mContext.getString(R.string.ex_oom);
			Log.e(TAG, "OutOfMemoryError:"+ex.getLocalizedMessage());
		}
		else if (ex instanceof RuntimeException) {
			str = mContext.getString(R.string.ex_runtime);
			Log.e(TAG, "RuntimeException:"+ex.getLocalizedMessage());
		}
		final String msg = str;
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				Dialog dialog = DialogFactory.createFailDialog(mContext, msg, listener);
				//dialog.setCancelable(false);
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
				dialog.show();
				Looper.loop();
			}
		}).start();
		
		return true;
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			killProcess();
		}
	};
}
