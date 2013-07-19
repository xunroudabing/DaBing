package com.dabing.emoj.service;

import greendroid.util.GDUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileHelper.OnScanFileListener;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
/**
 * 图片扫描服务
 * @author DaBing
 *
 */
public class EmojScanService extends Service {
	
	UserDefineDataBaseHelper dbHelper;
	final FileHelper fileHelper = new FileHelper();
	boolean cancel = false;
	boolean isRun = false;
	final String[] ARRAY_IGNOR = {"tencent","cache","template"};//此文件名不扫描
	final EmojScanHandler mHandler = new EmojScanHandler();
	final Messenger mMessenger = new Messenger(mHandler);
	List<Messenger> mClients = new ArrayList<Messenger>();
	/**
	 * 注册
	 */
	public static final int CLIENT_REGISTER = 0;
	/**
	 * 解除注册
	 */
	public static final int CLIENT_UNREGISTER = 1;
	/**
	 * 发消息
	 */
	public static final int CLIENT_SEND_MSG = 2;
	
	public static final int CLIENT_START = 3;
	/**
	 * 扫描
	 */
	public static final int CLIENT_SCAN = 4;
	/**
	 * 扫描到文件 参数file
	 */
	public static final int CLIENT_SCAN_GET_FILE = 5;
	static final String TAG = EmojScanService.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}



	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dbHelper = new UserDefineDataBaseHelper(getApplicationContext());
	}

	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mMessenger.getBinder();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Unbind");
		return super.onUnbind(intent);
	}
	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
	class EmojScanHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CLIENT_REGISTER:
				mClients.add(msg.replyTo);
				break;
			case CLIENT_UNREGISTER:
				mClients.remove(msg.replyTo);
				break;
			case CLIENT_SEND_MSG:
				
				break;
			case CLIENT_START:
				start();
				break;
			case CLIENT_SCAN:
				scan();
				break;
			case CLIENT_SCAN_GET_FILE:
				for(int i=0;i<mClients.size();i++){
					Messenger messenger = mClients.get(i);
					try {
						messenger.send(Message.obtain(null, CLIENT_SCAN_GET_FILE, msg.obj));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, e.toString());
					}
				}
				break;
			default:
				break;
			}
		}
		
	}
	private void start(){
		for(int i = 0; i < 10;i++){
			try {
				mMessenger.send(Message.obtain(mHandler, CLIENT_SEND_MSG, i));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
	}
	
	public void scan(){
		fileHelper.setCancel(false);
		BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();
		blockingQueue.add(new FileRootScanTask());
		BlockQueueTask blockTask = new BlockQueueTask(blockingQueue);
		GDUtils.getExecutor(getApplicationContext()).execute(blockTask);
	}
	public void stopScan(){
		fileHelper.setCancel(true);
	}
	class BlockQueueTask implements Runnable{
		BlockingQueue<Runnable> mQueue;
		public BlockQueueTask(BlockingQueue<Runnable> queue){
			mQueue = queue;
		}
		@Override
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
				
			}
		}
		
	}
	
	class FileScanTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	//从根目录扫描
	class FileRootScanTask implements Runnable{
		String rootPath;
		public FileRootScanTask(){
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			Log.d(TAG, "root:"+rootPath);
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				fileHelper.getFileContainsImage(rootPath, new OnScanFileListener() {
					
					@Override
					public boolean interception(File file) {
						// TODO Auto-generated method stub
						String name = file.getName().toLowerCase();
						//文件名前带.忽略
						if(name.startsWith(".")){
							return true;
						}
						for (String item : ARRAY_IGNOR) {
							if(name.contains(item)){
								return true;
							}
						}
						return false;
					}
					
					@Override
					public void get(File file) {
						// TODO Auto-generated method stub
						Log.d(TAG, "get:"+file);
						dbHelper.insert(file);
						mHandler.sendMessage(Message.obtain(mHandler, CLIENT_SCAN_GET_FILE, file));
					}
				});
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	

}
