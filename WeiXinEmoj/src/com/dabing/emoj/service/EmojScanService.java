package com.dabing.emoj.service;

import greendroid.util.GDUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.db.UserDefineDataBaseHelper.UserDefineCursor;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileHelper.OnScanFileListener;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.utils.Util;

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
 * 
 * @author DaBing
 * 
 */
public class EmojScanService extends Service {

	UserDefineDataBaseHelper dbHelper;
	final FileHelper fileHelper = new FileHelper();
	boolean cancel = false;
	boolean isRun = false;
	final String[] ARRAY_IGNOR = { "tencent", "cache", "template" };// 此文件名不扫描
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
	/**
	 * 数据库中的文件记录扫描更新完成
	 */
	public static final int CLIENT_ALBUM_CHECK = 6;
	static final String TAG = EmojScanService.class.getSimpleName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Unbind");
		return super.onUnbind(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	class EmojScanHandler extends Handler {

		/*
		 * (non-Javadoc)
		 * 
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
				for (int i = 0; i < mClients.size(); i++) {
					Messenger messenger = mClients.get(i);
					try {
						messenger.send(Message.obtain(null,
								CLIENT_SCAN_GET_FILE, msg.obj));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, e.toString());
					}
				}
				break;
			case CLIENT_ALBUM_CHECK:
				for (int i = 0; i < mClients.size(); i++) {
					Messenger client = mClients.get(i);
					try {
						client.send(Message.obtain(null, CLIENT_ALBUM_CHECK));
					} catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}
				}
				break;
			default:
				break;
			}
		}

	}

	private void start() {
		for (int i = 0; i < 10; i++) {
			try {
				mMessenger.send(Message.obtain(mHandler, CLIENT_SEND_MSG, i));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
	}

	public void scan() {
		fileHelper.setCancel(false);
		BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();
		blockingQueue.add(new AlbumCheckTask());
		blockingQueue.add(new FileRootScanTask());
		BlockQueueTask blockTask = new BlockQueueTask(blockingQueue);
		GDUtils.getExecutor(getApplicationContext()).execute(blockTask);
	}

	public void stopScan() {
		fileHelper.setCancel(true);
	}

	/**
	 * 同步队列
	 * 
	 * @author DaBing
	 * 
	 */
	class BlockQueueTask implements Runnable {
		BlockingQueue<Runnable> mQueue;

		public BlockQueueTask(BlockingQueue<Runnable> queue) {
			mQueue = queue;
		}

		@Override
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

			}
		}

	}

	// 从根目录扫描
	class FileRootScanTask implements Runnable {
		String rootPath;

		public FileRootScanTask() {
			rootPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			Log.d(TAG, "root:" + rootPath);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				fileHelper.getFileContainsImage(rootPath,
						new OnScanFileListener() {

							@Override
							public boolean interception(File file) {
								// TODO Auto-generated method stub
								String name = file.getName().toLowerCase();
								// 文件名前带.忽略
								if (name.startsWith(".")) {
									return true;
								}
								for (String item : ARRAY_IGNOR) {
									if (name.contains(item)) {
										return true;
									}
								}
								return false;
							}

							@Override
							public void get(File file) {
								// TODO Auto-generated method stub
								Log.d(TAG, "get:" + file);
								// dbHelper.insert(file);
								FileInfo fileInfo = FileInfo.GetFileInfo(file,
										new FilenameFilter() {

											@Override
											public boolean accept(File dir,
													String filename) {
												// TODO Auto-generated method
												// stub
												return filename
														.matches(FileHelper.IMAGE_PATTERN);
											}
										}, false);
								if (!dbHelper.exist(fileInfo)) {
									dbHelper.insert(fileInfo);
									mHandler.sendMessage(Message.obtain(mHandler,
											CLIENT_SCAN_GET_FILE, file));
								}
								
							}
						});
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}

	}

	class AlbumCheckTask implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			UserDefineCursor cusor = dbHelper.getCursor();
			try {
				while (cusor.moveToNext()) {
					int id = cusor.getId();
					String path = cusor.getPath();
					Log.d(TAG, "check:" + path);
					// 该文件不存在
					if (!exists(path)) {
						dbHelper.remove(id);
					} else {
						// 更新子文件数
						int count = getChildCount(path);
						dbHelper.updateCount(count, id);
					}
				}
				mHandler.sendEmptyMessage(CLIENT_ALBUM_CHECK);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			} finally {
				cusor.close();
			}
		}

		// 该文件是否存在
		private boolean exists(String path) {
			File file = new File(path);
			return file.exists();
		}

		// 获取子文件数
		private int getChildCount(String path) {
			int count = 0;
			File file = new File(path);
			if (file.isDirectory()) {
				File[] files = file.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String filename) {
						// TODO Auto-generated method stub
						return filename.matches(FileHelper.IMAGE_PATTERN);
					}
				});
				for (File childFile : files) {
					if (!childFile.isHidden()
							&& Util.isNormalFile(childFile.getAbsolutePath())) {
						count++;
					}
				}
			}
			return count;
		}

	}
}
