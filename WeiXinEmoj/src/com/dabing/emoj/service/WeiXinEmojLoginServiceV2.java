package com.dabing.emoj.service;

import greendroid.util.GDUtils;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.dabing.emoj.R;
import com.dabing.emoj.qqconnect.BaseApiListener;
import com.dabing.emoj.qqconnect.QQConnect;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.tencent.tauth.Constants;
import com.tencent.tauth.Tencent;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
/**
 * 用户登录成功后触发
 * @author Administrator
 *
 */
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
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		queue.add(new AddIdolTask(AppConstant.WEIBO_IDOL));
		queue.add(new ShareTask(AppConfig.getWeibo(getApplicationContext()), R.drawable.emoj042));
		BlockQueueTask task = new BlockQueueTask(queue);
		GDUtils.getExecutor(getApplicationContext()).execute(task);
		return START_STICKY;
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
	//收听触发的回调
	private BaseApiListener addIdolApiListener = new BaseApiListener("add_idol", false){
		public void onComplete(org.json.JSONObject jsonobject, Object obj) {
			Log.d(TAG, "jsonobject:"+jsonobject+" obj:"+obj);
		};
	};
	
	// 同步队列
	class BlockQueueTask implements Runnable {
		BlockingQueue<Runnable> mQueue;

		public BlockQueueTask(BlockingQueue<Runnable> queue) {
			mQueue = queue;
		}

		public void run() {
			// TODO Auto-generated method stub
			try {
				while (!mQueue.isEmpty()) {
					mQueue.take().run();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			} finally {
				stopSelf();
			}
		}

	}
	/**
	 * 收听
	 * @author Administrator
	 *
	 */
	class AddIdolTask implements Runnable{
		String mName;
		/**
		 * 要收听的用户的账户名列表。多个账户名之间用“,”隔开，例如：abc,bcde,cde。最多30个。
		 * @param name
		 */
		public AddIdolTask(String name){
			mName = name;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Bundle bundle = new Bundle();
				bundle.putString("format", "json");
				bundle.putString("name", mName);
				mTencent.requestAsync("relation/add_idol", bundle, Constants.HTTP_POST, addIdolApiListener, null);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	/*
	 *分享 
	 */
	class ShareTask implements Runnable{
		String mContent;
		int mResId;
		public ShareTask(String content,int resId){
			mContent = content;
			mResId = resId;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Bundle bundle = new Bundle();
				bundle.putString("content", mContent);
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mResId);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 40, os);
				byte[] bytes = os.toByteArray();
				bundle.putByteArray("pic", bytes);
				mTencent.requestAsync(Constants.GRAPH_ADD_PIC_T, bundle, Constants.HTTP_POST, new BaseApiListener("add_pic_t", false), null);
				bitmap.recycle();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	
	
}
