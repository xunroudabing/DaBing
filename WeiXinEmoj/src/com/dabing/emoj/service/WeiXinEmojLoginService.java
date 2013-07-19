package com.dabing.emoj.service;

import greendroid.util.GDUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.MyOauth;
import com.dabing.emoj.utils.TokenStore;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WeiXinEmojLoginService extends Service {
	static final String TAG = WeiXinEmojLoginService.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.d(TAG, "LoginService启动...");
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		queue.add(new ListenTask());
		queue.add(new ShareTask());
		BlockQueueTask task = new BlockQueueTask(queue);
		GDUtils.getExecutor(getApplicationContext()).execute(task);
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//同步队列
	class BlockQueueTask implements Runnable{
		BlockingQueue<Runnable> mQueue;
		public BlockQueueTask(BlockingQueue<Runnable> queue){
			mQueue = queue;
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(!mQueue.isEmpty()){
					mQueue.take().run();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				stopSelf();
			}
		}
		
	}
	
	/**
	 * 收听任务
	 * @author DaBing
	 *
	 */
	class ListenTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			OAuth oAuth = TokenStore.fetchPrivate(getApplicationContext());
			DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);
			try {
				String response = request.add(oAuth, "xunroudabing");
				//Log.d(TAG, "listentask-response:"+response);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				request.shutdownConnection();
			}
		}
		
	}
	//分享
	class ShareTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			String oauthString = TokenStore.getPrivate(getApplicationContext());
			if(oauthString == null){
				return;
			}
			MyOauth oauth = new MyOauth(oauthString);
//			if(oauth.name.equals("xunroudabing") || oauth.name.equals("xunroudabing_")){
//				return;
//			}
			OAuth oAuth = TokenStore.fetchPrivate(getApplicationContext());
			//TAPI api = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
			DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);
			try {
				String content = AppConfig.getWeibo(getApplicationContext());
				String picpath = AppConfig.getSharePicUrl(getApplicationContext());
				//String response = api.addPic(oAuth, "json", content, oAuth.getClientIP(), picpath);
				String response = request.addPicUrl(oAuth, content, oAuth.getClientIP(), picpath);
				Log.d(TAG, "sharetask-response:"+response);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				request.shutdownConnection();
			}
		}
		
	}
}
