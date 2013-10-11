package com.dabing.emoj.service;

import greendroid.util.GDUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.db.UserDefineDataBaseHelper.UserDefineCursor;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileHelper.OnScanFileListener;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.utils.Util;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Media;
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
	/**
	 * 操作开始
	 */
	public static final int CLIENT_ACTION_START = 7;
	/**
	 * 操作完成
	 */
	public static final int CLIENT_ACTION_END = 8;
	/**
	 * UI层刷新
	 */
	public static final int CLINET_REFLESH_UI = 9;
	static final String TAG = EmojScanService.class.getSimpleName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "EmojScanService.start....");
		GDUtils.getExecutor(getApplicationContext()).execute(new AlbumCheckTask());
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
			//测试用
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

			case CLIENT_ACTION_START:
				for (int i = 0; i < mClients.size(); i++) {
					Messenger client = mClients.get(i);
					try {
						client.send(Message.obtain(null, CLIENT_ACTION_START));
					} catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}
				}
				break;
			case CLIENT_ACTION_END:
				for (int i = 0; i < mClients.size(); i++) {
					Messenger client = mClients.get(i);
					try {
						client.send(Message.obtain(null, CLIENT_ACTION_END));
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
		blockingQueue.add(new ImageScanTask());
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
				Log.d(TAG, "扫描任务开始...");
				mHandler.sendEmptyMessage(CLIENT_ACTION_START);
				while (!mQueue.isEmpty()) {
					mQueue.take().run();
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			} finally {
				Log.d(TAG, "扫描任务结束...");
				mHandler.sendEmptyMessage(CLIENT_ACTION_END);
			}
		}

	}

	class Data {
		public int count;
		public String thumb;
		public long id;
	}

	// 从根目录扫描(废弃)
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
									long id = dbHelper.insert(fileInfo);
									mHandler.sendMessage(Message.obtain(
											mHandler, CLIENT_SCAN_GET_FILE,
											fileInfo));
								}

							}
						});
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			} finally {

			}
		}

	}

	// 用provider方式取图片文件夹 速度更快
	class ImageScanTask implements Runnable {
		private Map<Integer, String> getThumbnails(Map<String, Data> list){
			Map<Integer, String> coll = new HashMap<Integer, String>();
			StringBuilder builder = new StringBuilder();
			builder.append("(");
			for (String path : list.keySet()) {
				Data data = list.get(path);
				builder.append(data.id);
				builder.append(",");
			}
			String string = builder.substring(0, builder.length() - 1);
			string += ")";
			Log.d(TAG, "where:"+string);
			String whereClause = Thumbnails.IMAGE_ID + " IN " + string;
			String[] colums = {Thumbnails.THUMB_DATA,Thumbnails.IMAGE_ID};
			Cursor cursor = null;
			try {
				cursor = getContentResolver().query(Thumbnails.getContentUri("external"), colums, whereClause, null, null);
				if(cursor != null){
					cursor.moveToFirst();
					do {
						String thumb = cursor.getString(cursor.getColumnIndexOrThrow(Thumbnails.THUMB_DATA));
						int image_id = cursor.getInt(cursor.getColumnIndexOrThrow(Thumbnails.IMAGE_ID));
						coll.put(image_id, thumb);
						Log.d(TAG, "image_id:"+image_id + " thumb:"+thumb);
					} while (cursor.moveToNext());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				if(cursor != null){
					cursor.close();
				}
			}
			return coll;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.d(TAG, "begin scan");
			Cursor cursor = null;
			Map<String, Data> fileList = new HashMap<String, Data>();
			try {
				Uri uri = Media.getContentUri("external");
				String order = MediaColumns.DATE_MODIFIED + " desc";
				String[] colums = {
						MediaColumns._ID,
						MediaColumns.TITLE,
						MediaColumns.DISPLAY_NAME,
						MediaColumns.DATE_MODIFIED,
						String.format("REPLACE(%s,%s,'')", MediaColumns.DATA,
								MediaColumns.DISPLAY_NAME), MediaColumns.DATA };
				cursor = getContentResolver().query(uri, colums, null, null,
						order);
				if (cursor == null) {
					return;
				}
				if (!cursor.moveToFirst()) {
					return;
				}
				do {
					long id = cursor.getLong(cursor
							.getColumnIndexOrThrow(MediaColumns._ID));
//					String title = cursor.getString(cursor
//							.getColumnIndexOrThrow(MediaColumns.TITLE));
//					String display = cursor.getString(cursor
//							.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));
//					String date = cursor.getString(cursor
//							.getColumnIndexOrThrow(MediaColumns.DATE_MODIFIED));
					String filepath = cursor.getString(4);
//					String thumbpath = cursor.getString(cursor
//							.getColumnIndexOrThrow(MediaColumns.DATA));
//					//Log.d(TAG, String.format(
//							"id:%d title:%s display:%s date:%s data:%s", id,
//							title, display, date, filepath));
					if (fileList.containsKey(filepath)) {
						Data data = fileList.get(filepath);
						data.count++;
						fileList.put(filepath, data);
					} else {
						Data data = new Data();
						data.count = 1;
						data.thumb = String.valueOf(id);
						data.id = id;
						fileList.put(filepath, data);
					}
				} while (cursor.moveToNext());
				
				for (String path : fileList.keySet()) {
					Log.d(TAG, "path:" + path);
					File file = new File(path);
					if (!file.exists() || !file.isDirectory()) {
						continue;
					}
					Data data = fileList.get(path);
					int id = dbHelper.getId(path);
					// 插入数据
					if (id == -1) {
						FileInfo fileInfo = new FileInfo();
						fileInfo.filePath = path;
						fileInfo.fileName = Util.getNameFromFilepath(path);
						fileInfo.Count = data.count;
						fileInfo.dbThumb = data.thumb;
						dbHelper.insert(fileInfo);
						mHandler.sendMessage(Message.obtain(mHandler,
								CLIENT_SCAN_GET_FILE, fileInfo));
					}
					// 更新数据
					else {
						FileInfo fileInfo = new FileInfo();
						fileInfo.Count = data.count;
						fileInfo.dbThumb = data.thumb;
						dbHelper.update(id, fileInfo);
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			} finally {
				if(cursor != null){
					cursor.close();
				}
			}
		}

	}

	// 更新相册信息
	class AlbumCheckTask implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			UserDefineCursor cusor = dbHelper.getCursor();
			try {
				if (cusor != null && cusor.moveToFirst()) {
					do {
						int id = cusor.getId();
						String path = cusor.getPath();
						Log.d(TAG, "check:" + path);
						// 该文件不存在
						if (!exists(path)) {
							dbHelper.delete(id);
						} else {
							// 更新子文件数
							int count = getChildCount(path);
							long thumb = getThumbId(path); 
							ContentValues cv = new ContentValues();
							cv.put(UserDefineDataBaseHelper.FIELD_CHILDSIZE, count);
							cv.put(UserDefineDataBaseHelper.FIELD_THUMB, String.valueOf(thumb));
							dbHelper.update(cv, id);
						}
					} while (cusor.moveToNext());
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
		
		/**
		 * 获取文件夹下的第一幅照片的id
		 * @param path
		 * @return
		 */
		protected long getThumbId(String path){
			Uri uri = Media.getContentUri("external");
			String order = MediaColumns.DATE_MODIFIED + " desc";
			String[] colums = { MediaColumns._ID, MediaColumns.DATA,
					MediaColumns.DATE_MODIFIED };
			String where = MediaColumns.DATA + " like ?";
			String[] whereArgs = { path + "%" };
			Cursor cursor = null;
			long ret = -1;
			try {
				cursor = getContentResolver().query(uri, colums, where, whereArgs,
						order);
				cursor.moveToFirst();
				if(cursor != null){
					cursor.moveToFirst();
					if(cursor.getCount() > 0){
						long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaColumns._ID));
						ret = id;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				if(cursor != null){
					cursor.close();
				}
			}
			return ret;
		}

	}
}
