package com.dabing.emoj.activity;

import java.io.File;
import java.io.FilenameFilter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.GridLayout.Alignment;
import android.support.v7.widget.GridLayout.Spec;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.adpater.AlbumCusorAdapter;
import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.service.EmojScanService;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.widget.Album;
import com.dabing.emoj.widget.CustomGridLayout;

/**
 * 自定义表情
 * @author DaBing
 *
 */
public class UserDefineActivity extends BaseActivity {
	
	final MyHandler mHandler = new MyHandler();
	Messenger client = new Messenger(mHandler);
	Messenger mService;	
	boolean mBound = false;
	private int numButtons = 1;
	CustomGridLayout gridLayout;
	AlbumCusorAdapter adapter;
	int Album_Width = 80;
	static final int COLUM_NUM = 4;
	static final int COLUM_PADDING = 0;
	static final String TAG = UserDefineActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		calculateAlbumWidth();
		BindService();
		gridLayout = (CustomGridLayout) findViewById(R.id.gridContainer);
		gridLayout.setColumnCount(COLUM_NUM);

		Button addButton = (Button) findViewById(R.id.addNewButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				scanfiles();
			}
		});
		
		bindGridLayout();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.user_define_layout;
	}
	
	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		UnBindService();
	}
	//计算相册宽度
	private void calculateAlbumWidth(){
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		Album_Width = (screenWidth - (COLUM_NUM + 1)*COLUM_PADDING)/COLUM_NUM;
		Log.d(TAG, "width:"+Album_Width);
		
	}
	//插入相册
	protected void addAlbum(FileInfo fileInfo){
		Album album = new Album(getApplicationContext());
		album.setWidth(Album_Width);
		album.setFile(fileInfo);
		
		gridLayout.addView(album);
	}
	
	protected void bindGridLayout(){
		if(adapter == null){
			adapter = new AlbumCusorAdapter(getApplicationContext(), new UserDefineDataBaseHelper(getApplicationContext()).getCursor());
		}
		gridLayout.setAdapter(adapter);
	}
	
	protected void refleshGridLayout(){
		if(adapter != null){
			adapter.reflesh();
		}
	}
	//*******bind service 相关*********
	private void BindService(){
		Intent intent = new Intent(getApplicationContext(), EmojScanService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBound = true;
	}
	private void UnBindService(){
		unbindService(mConnection);
		mBound = false;
	}
	//注册
	protected void register(){
		if(mService != null){
			try {
				Message message = Message.obtain(null, EmojScanService.CLIENT_REGISTER);
				message.replyTo = client;
				mService.send(message);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			
		}
	}
	protected void unregister(){
		try {
			Message message = Message.obtain(null, EmojScanService.CLIENT_UNREGISTER);
			message.replyTo = client;
			mService.send(message);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	//开始扫描
	protected void scanfiles(){
		try {
			Message message = Message.obtain(null, EmojScanService.CLIENT_SCAN);
			message.replyTo = client;
			mService.send(message);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private final ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.d(TAG, "disconnect");
			mService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			try {
				mService = new Messenger(service);
				register();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
	
	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case EmojScanService.CLIENT_SCAN_GET_FILE:
				File file = (File) msg.obj;
				Log.d(TAG, "get:"+file.toString());
//				FileInfo fileInfo = FileInfo.GetFileInfo(file, new ImageFilenameFilter(), false);
//				addAlbum(fileInfo);
				refleshGridLayout();
				break;

			default:
				break;
			}
		}
	}
	
	class ImageFilenameFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return filename.matches(FileHelper.IMAGE_PATTERN);
		}
		
	}
}
