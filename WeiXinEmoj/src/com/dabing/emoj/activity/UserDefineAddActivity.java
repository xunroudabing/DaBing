package com.dabing.emoj.activity;

import greendroid.util.GDUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.adpater.UserDefineAddFilesAdapter;
import com.dabing.emoj.adpater.UserDefineAddFilesAdapter.IAddFileCallBack;
import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.SimpleFile;
import com.dabing.emoj.widget.NormalProgressDialog;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * 选择文件夹
 * @author DaBing
 *
 */
public class UserDefineAddActivity extends BaseActivity implements IAddFileCallBack{
	
	String current;
	Stack<String> mStack = new Stack<String>();
	UserDefineDataBaseHelper mHelper;
	NormalProgressDialog progressDialog;
	UserDefineAddFilesAdapter mAdapter;
	PullToRefreshListView mListView;
	/**
	 * 获取文件集合
	 */
	static final int MSG_GET_FILES = 0;
	static final int MSG_LOADING = 1;
	static final int MSG_TASK_FINISH = 2;
	static final String TAG = UserDefineAddActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		progressDialog = new NormalProgressDialog(UserDefineAddActivity.this);
		mListView = (PullToRefreshListView) findViewById(R.id.user_define_add_file_listview);
		mListView.getLoadingLayoutProxy().setLoadingDrawable(null);
		mListView.getLoadingLayoutProxy().setPullLabel(null);
		mListView.getLoadingLayoutProxy().setReleaseLabel(null);
		showSDcardRoot();
		mHelper = new UserDefineDataBaseHelper(getApplicationContext());
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.user_define_add_file;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!back()){
			super.onBackPressed();
		}
	}
	//第一次进入展现SD卡根目录文件列表
	protected void showSDcardRoot(){
		String root = Environment.getExternalStorageDirectory() + File.separator;
		current = root;
		showFiles(root);
	}
	protected void showFiles(String path){
		progressDialog.show();	
		Log.d(TAG, "showFiles:"+path);
		GDUtils.getExecutor(getApplicationContext()).execute(new GetFilesTask(path));
	}
	
	protected boolean back(){
		try {
			if(!mStack.isEmpty()){
				current = mStack.pop();
				showFiles(current);
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return false;
		
	}
	protected void BindFiles(List<SimpleFile> list){
		mAdapter = new UserDefineAddFilesAdapter(UserDefineAddActivity.this, list,this);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnItemClickListener(listener);
	}
	
	
	class GetFilesTask implements Runnable{
		String mPath;
		public GetFilesTask(String root){
			mPath = root;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				File root = new File(mPath);
				File[] files = root.listFiles();
				List<File> lists = Arrays.asList(files);
				List<SimpleFile> collection = new ArrayList<SimpleFile>();
				if(files != null){
					Collections.sort(lists, cmpName);
					for(int i=0;i<lists.size();i++){
						File file = lists.get(i);
						if(file.isDirectory()&&!file.isHidden()){
							//图片文件集合
							File[] imageFiles = file.listFiles(new FilenameFilter() {								
								@Override
								public boolean accept(File dir, String filename) {
									// TODO Auto-generated method stub
									return filename.matches(FileHelper.IMAGE_PATTERN);
								}
							});
							SimpleFile simpleFile = new SimpleFile();
							simpleFile.name = file.getName();
							simpleFile.path = file.getPath();
							simpleFile.count = imageFiles == null ? 0:imageFiles.length;
							collection.add(simpleFile);
						}
					}
					mHandler.sendMessage(Message.obtain(mHandler, MSG_GET_FILES, collection));
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				mHandler.sendEmptyMessage(MSG_TASK_FINISH);
			}
		}
		
	}
	private Comparator<File> cmpName = new Comparator<File>() {

		@Override
		public int compare(File object1, File object2) {
			// TODO Auto-generated method stub
			return object1.getName().compareToIgnoreCase(object2.getName());
		}
	}; 
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_GET_FILES:
				List<SimpleFile> files = (List<SimpleFile>) msg.obj;
				BindFiles(files);
				break;
			case MSG_TASK_FINISH:
				try {
					progressDialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			default:
				break;
			}
		};
	};
	//点击跳转
	protected OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			try {
				Log.d(TAG, "pos:"+parent.getAdapter().getItem(position));
				SimpleFile file = mAdapter.getFile((Integer)parent.getAdapter().getItem(position));
				if(file != null){
					Log.d(TAG, "onItemClick:"+file.name+" "+file.path+" pos:"+position);
					mStack.push(current);
					current = file.path;
					showFiles(file.path);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
	//点击添加为自定义表情
	@Override
	public void addFile(SimpleFile file) {
		// TODO Auto-generated method stub
		long id = mHelper.getId(file.path);
		if(id == -1){
			id = mHelper.insert(file);
		}else {
			mHelper.enable(id);
		}
		Log.d(TAG, "addFile:"+id);
		Intent intent = new Intent();
		intent.putExtra(AppConstant.INTENT_USER_DEFINE_ADD_FILE_ID, id);
		setResult(RESULT_OK, intent);
		finish();
	}

}
