package com.dabing.emoj.fragment;

import com.dabing.emoj.R;
import com.dabing.emoj.adpater.AlbumCusorAdapter;
import com.dabing.emoj.db.UserDefineContentProvider;
import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.service.EmojScanService;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.widget.Album.AlbumClickListener;
import com.dabing.emoj.widget.AddImageButton;
import com.dabing.emoj.widget.AlbumImageView;
import com.dabing.emoj.widget.CustomGridLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

public class AlbumFragment extends UserDefineFragment implements LoaderCallbacks<Cursor>{
	int mWidth = 80;
	IEmojScanCallBack mCallBack;
	Messenger client;
	Messenger mService;
	boolean mBound = false;
	String[] PULL_MSG_ARRAY;
	int PULL_MSG_ARRAY_INDEX = 0;
	CustomGridLayout gridLayout;
	PullToRefreshScrollView mScrollView;
	AlbumCusorAdapter adapter;
	boolean IsScaned = false;//是否扫描过
	static final int COLUM_NUM = 3;
	static final int COLUM_PADDING = 0;
	static final String TAG = AlbumFragment.class.getSimpleName();	
	static AlbumFragment instance;
	public static AlbumFragment getInstance(){
		if(instance == null){
			instance = new AlbumFragment();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		mScrollView = (PullToRefreshScrollView) getView().findViewById(
				R.id.pull_refresh_scrollview);
		gridLayout = (CustomGridLayout) getView().findViewById(
				R.id.gridContainer);
		gridLayout.setColumnCount(COLUM_NUM);

		PULL_MSG_ARRAY = getResources().getStringArray(R.array.pull_msg_array);
		PULL_MSG_ARRAY_INDEX = 0;
		mScrollView.getLoadingLayoutProxy().setLoadingDrawable(null);
		mScrollView.getLoadingLayoutProxy().setPullLabel(
				PULL_MSG_ARRAY[PULL_MSG_ARRAY_INDEX]);
		mScrollView.getLoadingLayoutProxy().setReleaseLabel(
				PULL_MSG_ARRAY[PULL_MSG_ARRAY_INDEX]);
		mScrollView.setOnPullEventListener(pullEventListener);
		
		bindGridLayout();
		getLoaderManager().initLoader(0, null, this);
		//通知父activity初始化完成
		if(mCallBack != null){
			mCallBack.onInit(TAG);
		}
		//扫描图片
		if(!IsScaned){
			IsScaned = true;
			scanFilesDelayed(500);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		client = new Messenger(mHandler);
		BindService();
		calculateAlbumWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.user_define_fragment1, container,
				false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
		getLoaderManager().destroyLoader(0);
		UnBindService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
		
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	// *********函数**************
	
	protected void refleshGridLayout() {
		if (adapter != null) {
			adapter.reflesh();
		}
	}
	
	protected void bindGridLayout(){
		AddImageButton addImageButton = new AddImageButton(getActivity());
		addImageButton.setWidth(mWidth);
		gridLayout.setFirstView(addImageButton);
		if(adapter == null){
			adapter = new AlbumCusorAdapter(getActivity().getApplicationContext(), null,COLUM_NUM,albumClickListener);
		}
		gridLayout.setAdapter(adapter);		
		
		
		
	}
	
	// 计算相册宽度
	private void calculateAlbumWidth() {
		WindowManager windowManager = (WindowManager) getActivity()
				.getApplication().getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		mWidth = (screenWidth - (COLUM_NUM + 1) * COLUM_PADDING) / COLUM_NUM;
		//Log.d(TAG, "width:" + mWidth);

	}
		
	/**
	 * 设置与activity的回调监听
	 * @param callBack
	 */
	public void setCallBack(IEmojScanCallBack callBack){
		mCallBack = callBack;
	}
	
	public void scanFilesDelayed(long delay){
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				scanfiles();
			}
		}, delay);
	}
	// *********成员*********
	private AlbumClickListener albumClickListener = new AlbumClickListener() {
		
		@Override
		public void click(FileInfo fileInfo) {
			// TODO Auto-generated method stub
			if(mCallBack != null){
				mCallBack.onClick(TAG, fileInfo);
			}
		}
	};
	private OnPullEventListener<ScrollView> pullEventListener = new OnPullEventListener<ScrollView>() {

		@Override
		public void onPullEvent(PullToRefreshBase<ScrollView> refreshView,
				State state, Mode direction) {
			// TODO Auto-generated method stub
			if (state == State.RESET) {
				Log.d(TAG, "reset");
				PULL_MSG_ARRAY_INDEX = PULL_MSG_ARRAY_INDEX < PULL_MSG_ARRAY.length - 1 ? PULL_MSG_ARRAY_INDEX + 1
						: 0;
				refreshView.getLoadingLayoutProxy().setPullLabel(
						PULL_MSG_ARRAY[PULL_MSG_ARRAY_INDEX]);
				refreshView.getLoadingLayoutProxy().setReleaseLabel(
						PULL_MSG_ARRAY[PULL_MSG_ARRAY_INDEX]);
			}

		}
	};

	private  Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case EmojScanService.CLIENT_SCAN_GET_FILE:
				FileInfo file = (FileInfo) msg.obj;
				Log.d(TAG, "get:" + file.toString());

				break;
			case EmojScanService.CLIENT_ALBUM_CHECK:

				break;
			case EmojScanService.CLIENT_ACTION_END:
				if(mCallBack != null){
					mCallBack.onEnd();
				}
				refleshGridLayout();
				break;
			case EmojScanService.CLINET_REFLESH_UI:
				Log.d(TAG, "reflesh UI");
				break;
			case EmojScanService.CLIENT_ACTION_START:
				if(mCallBack != null){
					mCallBack.onLoading();
				}
				break;
			default:
				break;
			}
		}
	};

	// *******bind service 相关*********
	private void BindService() {
		Intent intent = new Intent(getActivity().getApplicationContext(),
				EmojScanService.class);
		getActivity().getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBound = true;
	}

	private void UnBindService() {
		try {
			getActivity().getApplicationContext().unbindService(mConnection);
			mBound = false;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}

	// 注册
	protected void register() {
		if (mService != null) {
			try {
				Message message = Message.obtain(null,
						EmojScanService.CLIENT_REGISTER);
				message.replyTo = client;
				mService.send(message);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}

		}
	}

	protected void unregister() {
		try {
			Message message = Message.obtain(null,
					EmojScanService.CLIENT_UNREGISTER);
			message.replyTo = client;
			mService.send(message);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	// 开始扫描
	public void scanfiles() {
		try {
			Log.d(TAG, "222");
			Message message = Message.obtain(null, EmojScanService.CLIENT_SCAN);
			message.replyTo = client;
			mService.send(message);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "11:"+e.toString());
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
			Log.d(TAG, "onServiceConnected");
			try {
				mService = new Messenger(service);
				register();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
	
	//*****CusorLoader事件*******
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateLoader");
		CursorLoader loader = new CursorLoader(getActivity(), UserDefineContentProvider.getUri(), null, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLoadFinished");
		adapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLoaderReset");
		
	}
}
