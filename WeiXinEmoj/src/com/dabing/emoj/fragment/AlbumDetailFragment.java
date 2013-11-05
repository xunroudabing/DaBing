package com.dabing.emoj.fragment;

import greendroid.util.GDUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dabing.emoj.R;
import com.dabing.emoj.activity.UserDefineEmojViewActivity;
import com.dabing.emoj.adpater.AlbumDetailAdapter;
import com.dabing.emoj.utils.AppConstant;
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
		gridView.getLoadingLayoutProxy().setPullLabel("");
		gridView.getLoadingLayoutProxy().setReleaseLabel("");
		gridView.setOnPullEventListener(onPullEventListener);
		if(mCallBack != null){
			mCallBack.onInit(TAG,mFileInfo);
		}
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "requestCode:"+requestCode+" resultCode:"+resultCode);
		if(requestCode == AppConstant.REQUEST_COMMON_EMOJ){
			if(resultCode == Activity.RESULT_OK){
				getActivity().getParent().setResult(Activity.RESULT_OK, data);
				getActivity().getParent().finish();
			}
		}
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
		adapter = new AlbumDetailAdapter(getActivity(), files, COLUM_NUM);		
		gridView.getRefreshableView().setAdapter(adapter);
		gridView.getRefreshableView().setOnItemClickListener(itemClickListener);
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
				List<File> files = Arrays.asList(list);
				Collections.sort(files, dateComparator);
				files.toArray(list);
				mHandler.sendMessage(Message.obtain(mHandler, 0, list));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				mHandler.sendEmptyMessage(10);
			}
		}
		
	}
	//修改日期排序
	private Comparator<File> dateComparator = new Comparator<File>() {

		@Override
		public int compare(File object1, File object2) {
			// TODO Auto-generated method stub
			return longToCompareInt(object2.lastModified() - object1.lastModified());
		}
	};
	
    private int longToCompareInt(long result) {
        return result > 0 ? 1 : (result < 0 ? -1 : 0);
    }
	private OnPullEventListener<GridView> onPullEventListener = new OnPullEventListener<GridView>() {

		@Override
		public void onPullEvent(PullToRefreshBase<GridView> refreshView,
				State state, Mode direction) {
			// TODO Auto-generated method stub
			
		}
	};
	//图片点击事件
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			try {
				File file = adapter.getFile(position);
				File[] mFiles = adapter.getData();
				String[] paths = new String[mFiles.length];
				for (int i = 0; i < mFiles.length; i++) {
					paths[i] = mFiles[i].getPath();
				}
				long dbId = mFileInfo.dbId;
				Intent intent = new Intent(getActivity(), UserDefineEmojViewActivity.class);
				intent.putExtra(AppConstant.INTENT_PIC_NAME, file.getPath());
				intent.putExtra(AppConstant.INTENT_PIC_ARRAY, paths);
				intent.putExtra(AppConstant.INTENT_PIC_PARMS, "自定义表情");
				intent.putExtra(AppConstant.INTENT_TITLE, getString(R.string.title_custom));
				//自定义表情相册在数据库中的id
				intent.putExtra(AppConstant.INTENT_USER_DEFINE_DBID, dbId);
				intent.putExtras(getActivity().getIntent());
				startActivityForResult(intent, AppConstant.REQUEST_COMMON_EMOJ);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			
			 
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
			//异步任务结束	
			case 10:
				if(mCallBack != null){
					mCallBack.onEnd();
				}
			default:
				break;
			}
		};
	};
}
