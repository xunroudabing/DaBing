package com.dabing.emoj.activity;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dabing.emoj.R;
import com.dabing.emoj.adpater.AlbumCusorAdapter;
import com.dabing.emoj.fragment.AlbumDetailFragment;
import com.dabing.emoj.fragment.AlbumFragment;
import com.dabing.emoj.fragment.UserDefineFragment.IEmojScanCallBack;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.widget.Album;
import com.dabing.emoj.widget.CustomGridLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tencent.mm.sdk.uikit.MMImageButton;


/**
 * 自定义表情
 * @author DaBing
 *
 */
public class UserDefineActivity extends FragmentActivity implements IEmojScanCallBack {
	String currentTAG;
	String action = "send";
	boolean Mode = false;
	ProgressBar mProgressBar;
	TextView rightBtn,leftBtn,mTitle;
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
		mTitle = (TextView) findViewById(R.id.umeng_fb_conversation_title);
		mProgressBar = (ProgressBar) findViewById(R.id.user_define_progressBar);
		leftBtn = (TextView) findViewById(R.id.umeng_fb_goback_btn);
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
			fragment.setCallBack(this);
			trans.replace(R.id.user_define_container, fragment, AlbumDetailFragment.class.getSimpleName());
			trans.addToBackStack(null);
			trans.commit();
		}
	}
	
	public void SetupAction(){
		Intent data = getIntent();
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);			
		}
		Log.d(TAG, "action:"+action);
		if(action.equals("get")){			
			leftBtn.setText(R.string.btn_userdefine_weixin);
			leftBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			leftBtn.setVisibility(View.VISIBLE);
		}
		else if (action.equals("pick")) {			
			leftBtn.setText(R.string.btn_userdefine_back);
			leftBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			leftBtn.setVisibility(View.VISIBLE);
		}
		else {
			leftBtn.setVisibility(View.INVISIBLE);
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
		//点击相册跳转至图片列表
		if(TAG.equals(AlbumFragment.class.getSimpleName())){
			FileInfo fileInfo = (FileInfo) parms;
			Log.d(TAG, "fileinfo:"+fileInfo.filePath + " type:"+fileInfo.dbType);
			startDetailFragment(fileInfo);						
		}
		
	}

	@Override
	public void onInit(String TAG,Object obj) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onInit:"+TAG);
		currentTAG = TAG;
		if(TAG.equals(AlbumFragment.class.getSimpleName())){
			mTitle.setText(R.string.title_custom);
			SetupAction();
			rightBtn.setVisibility(View.VISIBLE);
			rightBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					FragmentManager fm = getSupportFragmentManager();
					Fragment fragment = fm.findFragmentByTag(AlbumFragment.class.getSimpleName());
					if(fragment instanceof AlbumFragment){
						((AlbumFragment) fragment).edit();
					}
				}
			});
		}
		else if (TAG.equals(AlbumDetailFragment.class.getSimpleName())) {
			rightBtn.setVisibility(View.GONE);
			leftBtn.setText(R.string.btn_userdefine_up);
			leftBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					FragmentManager fm = getSupportFragmentManager();
					fm.popBackStack(null, 0);
				}
			});
			leftBtn.setVisibility(View.VISIBLE);
			if(obj != null){
				FileInfo fileInfo = (FileInfo) obj;
				mTitle.setText(fileInfo.dbName);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Log.d(TAG, "onBackPressed");
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "dispatchKeyEvent");
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
    		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
    			if(currentTAG.equals(AlbumFragment.class.getSimpleName())){
    				FragmentManager fm = getSupportFragmentManager();
    				Fragment fragment = fm.findFragmentByTag(currentTAG);
    				if(fragment != null){
    					if(((AlbumFragment)fragment).getDelMode()){
    						((AlbumFragment)fragment).cancelDelMode();
    						return true;
    					}else {
    						DialogFactory.createTwoButtonDialog(UserDefineActivity.this, "确定退出微信表情包?", "确定", "取消", new DialogInterface.OnClickListener() {
    		    				
    		    				public void onClick(DialogInterface dialog, int which) {
    		    					// TODO Auto-generated method stub
    		    					getParent().finish();
    		    					dialog.dismiss();
    		    				}
    		    			}, new DialogInterface.OnClickListener() {
    		    				
    		    				public void onClick(DialogInterface dialog, int which) {
    		    					// TODO Auto-generated method stub
    		    					dialog.dismiss();
    		    				}
    		    			}).show();
    						return true;
						}
    				}
    			}
    		}
		}
		return super.dispatchKeyEvent(event);
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		Log.d(TAG, "onActivityResult");
		
	}

}
