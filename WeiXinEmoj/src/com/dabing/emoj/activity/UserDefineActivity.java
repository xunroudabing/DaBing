package com.dabing.emoj.activity;

import java.io.File;
import java.io.FilenameFilter;

import android.R.anim;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.GridLayout.Alignment;
import android.support.v7.widget.GridLayout.Spec;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.adpater.AlbumCusorAdapter;
import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.fragment.AlbumDetailFragment;
import com.dabing.emoj.fragment.AlbumFragment;
import com.dabing.emoj.fragment.UserDefineFragment.IEmojScanCallBack;
import com.dabing.emoj.service.EmojScanService;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.widget.AddImageButton;
import com.dabing.emoj.widget.Album;
import com.dabing.emoj.widget.AlbumImageView;
import com.dabing.emoj.widget.CustomGridLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;


/**
 * 自定义表情
 * @author DaBing
 *
 */
public class UserDefineActivity extends FragmentActivity implements IEmojScanCallBack {
	boolean Mode = false;
	ProgressBar mProgressBar;
	TextView rightBtn;
	CustomGridLayout gridLayout;
	PullToRefreshScrollView mScrollView;
	AlbumCusorAdapter adapter;
	int Album_Width = 80;
	String[] PULL_MSG_ARRAY;
	int PULL_MSG_ARRAY_INDEX = 0;
	static final long REFLESH_UI_DELAY = 500;//刷新UI的间隔时间
	static final int COLUM_NUM = 3;
	static final int COLUM_PADDING = 0;
	static final String TAG = UserDefineActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_define_layout);
		mProgressBar = (ProgressBar) findViewById(R.id.user_define_progressBar);
		rightBtn = (TextView) findViewById(R.id.sort_btn);
		rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getSupportFragmentManager();
				AlbumFragment fragment = (AlbumFragment) fm.findFragmentByTag(AlbumFragment.class.getSimpleName());
				fragment.scanfiles();
			}
		});
		Init();
		
	}

//	@Override
//	protected int getLayoutId() {
//		// TODO Auto-generated method stub
//		return R.layout.user_define_layout;
//	}
	
	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	protected void showProgressBar(){
		mProgressBar.setVisibility(View.VISIBLE);
	}
	protected void hideProgressBar(){
		mProgressBar.setVisibility(View.INVISIBLE);
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
	
	
	
	protected void setDelMode(){
		Mode = !Mode;
		for(int i = 0;i<gridLayout.getChildCount();i++){
			View view = gridLayout.getChildAt(i);
			if(view instanceof Album){
				((Album) view).setDelMode(Mode);
			}
		}
	}
	//下拉监听
	private OnPullEventListener<ScrollView> pullEventListener = new OnPullEventListener<ScrollView>() {

		@Override
		public void onPullEvent(PullToRefreshBase<ScrollView> refreshView,
				State state, Mode direction) {
			// TODO Auto-generated method stub
			if(state == State.RESET){
				Log.d(TAG, "reset");
				PULL_MSG_ARRAY_INDEX = PULL_MSG_ARRAY_INDEX < PULL_MSG_ARRAY.length - 1 ? PULL_MSG_ARRAY_INDEX + 1 : 0;
				refreshView.getLoadingLayoutProxy().setPullLabel(PULL_MSG_ARRAY[PULL_MSG_ARRAY_INDEX]);
				refreshView.getLoadingLayoutProxy().setReleaseLabel(PULL_MSG_ARRAY[PULL_MSG_ARRAY_INDEX]);
			}
			
		}
	};
	
	private void Init(){
		FragmentManager fm = getSupportFragmentManager();
		if(fm.findFragmentByTag(AlbumFragment.class.getSimpleName()) == null){
			FragmentTransaction trans = fm.beginTransaction();
			AlbumFragment fragment = AlbumFragment.getInstance();
			fragment.setCallBack(this);
			trans.replace(R.id.user_define_container, fragment, AlbumFragment.class.getSimpleName());
			trans.commit();
		}
		
	}
	//跳转至相册详细页
	private void startDetailFragment(FileInfo fileInfo){
		FragmentManager fm = getSupportFragmentManager();
		if(fm.findFragmentByTag(AlbumDetailFragment.class.getSimpleName()) == null){
			FragmentTransaction trans = fm.beginTransaction();
			AlbumDetailFragment fragment = AlbumDetailFragment.getInstance(fileInfo);
			trans.replace(R.id.user_define_container, fragment, AlbumDetailFragment.class.getSimpleName());
			trans.addToBackStack(null);
			trans.commit();
		}
	}
	class ImageFilenameFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return filename.matches(FileHelper.IMAGE_PATTERN);
		}
		
	}

	@Override
	public void onLoading() {
		// TODO Auto-generated method stub
		showProgressBar();
	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		hideProgressBar();
	}

	@Override
	public void onClick(String TAG, Object parms) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onClick");
		if(TAG.equals(AlbumFragment.class.getSimpleName())){
			FileInfo fileInfo = (FileInfo) parms;
			Log.d(TAG, "fileinfo:"+fileInfo.filePath + " type:"+fileInfo.dbType);
			startDetailFragment(fileInfo);
		}
	}

}
