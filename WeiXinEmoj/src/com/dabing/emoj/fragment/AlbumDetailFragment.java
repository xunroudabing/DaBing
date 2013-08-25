package com.dabing.emoj.fragment;

import greendroid.util.GDUtils;

import java.io.File;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dabing.emoj.R;
import com.dabing.emoj.adpater.AlbumDetailAdapter;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileInfo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;

public class AlbumDetailFragment extends UserDefineFragment {
	AlbumDetailAdapter adapter;
	IEmojScanCallBack mCallBack;
	FileInfo mFileInfo;
	PullToRefreshGridView gridView;
	static final int COLUM_NUM = 4;
	static final String TAG = AlbumDetailFragment.class.getSimpleName();
	
	public static AlbumDetailFragment getInstance(FileInfo fileInfo){
		return new AlbumDetailFragment(fileInfo);
	}
	
	public AlbumDetailFragment(FileInfo fileInfo){
		mFileInfo = fileInfo;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.user_define_fragment2, container, false);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		gridView = (PullToRefreshGridView) getView().findViewById(R.id.gridview2);
		gridView.getLoadingLayoutProxy().setLoadingDrawable(null);
		gridView.getLoadingLayoutProxy().setPullLabel(null);
		gridView.getLoadingLayoutProxy().setReleaseLabel(null);
		gridView.setOnPullEventListener(onPullEventListener);
		
		//gridView = (GridView) getView().findViewById(R.id.gridview);
		scanFiles();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	//*****函数*****
	/**
	 * 设置与activity的回调监听
	 * @param callBack
	 */
	public void setCallBack(IEmojScanCallBack callBack){
		mCallBack = callBack;
	}
	//异步获取相册下的所有图片文件
	protected void scanFiles(){
		if(mFileInfo == null){
			return;
		}
		String path = mFileInfo.filePath;
		if(mCallBack != null){
			mCallBack.onLoading();
		}
		GDUtils.getExecutor(getActivity()).execute(new GetImagesTask(path));
	}
	
	protected void bindGridView(File[] files){
		adapter = new AlbumDetailAdapter(getActivity().getApplicationContext(), files, COLUM_NUM);
		gridView.getRefreshableView().setAdapter(adapter);
	}
	/**
	 * 获取相册下的所有图片
	 * @author DaBing
	 *
	 */
	class GetImagesTask implements Runnable{
		String mPath;
		public GetImagesTask(String path){
			mPath = path;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				File file = new File(mPath);
				File[] list = file.listFiles(new FileHelper.FileContainsImageFilter());
				Log.d(TAG, "list.length:"+list.length);
				mHandler.sendMessage(Message.obtain(mHandler, 0, list));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				if(mCallBack != null){
					mCallBack.onEnd();
				}
			}
		}
		
	}
	
	private OnPullEventListener<GridView> onPullEventListener = new OnPullEventListener<GridView>() {

		@Override
		public void onPullEvent(PullToRefreshBase<GridView> refreshView,
				State state, Mode direction) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//获取到相册下的所有图片文件
			case 0:
				File[] files = (File[]) msg.obj;
				bindGridView(files);
				break;

			default:
				break;
			}
		};
	};
}
