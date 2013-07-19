package com.dabing.emoj.activity;

import greendroid.util.GDUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.adpater.DownloadGridViewAdapter;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.Emoj;
import com.dabing.emoj.utils.FileType;
import com.dabing.emoj.utils.FileTypeJudge;
import com.dabing.emoj.utils.FlushedInputStream;
import com.dabing.emoj.widget.CrystalButton;
import com.dabing.emoj.widget.DownloadProgressDialog;
/**
 * 打包下载
 * @author DaBing
 *
 */
public class DownloadGridViewActivity extends BaseActivity implements IEmojItemClickListener {
	DownloadProgressDialog mDialog;
	DownloadPackageHandler mHandler = new DownloadPackageHandler();
	DownloadGridViewAdapter adapter;
	DownloadPackageTask downloadTask;
	GridView gridView;
	static final String TAG = DownloadGridViewActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gridView = (GridView) findViewById(R.id.download_gridview);
		setMMTitle("表情打包下载");
		setBackBtn(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Bind();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.download_gridview;
	}
	public void Bind(){
		try {
			String json = AppConstant.EMOJ_INDEX;
			JSONArray jsonArray = new JSONArray(json);
			JSONArray mArray = getEmojArray(jsonArray);
			adapter = new DownloadGridViewAdapter(getApplicationContext(), mArray);
			adapter.setIEmojItemClickListener(this);
			gridView.setAdapter(adapter);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private JSONArray getEmojArray(JSONArray array){
		JSONArray data = new JSONArray();
		for(int i =0;i<array.length();i++){
			try {
				JSONObject item = array.getJSONObject(i);
				int c = item.getInt("c");
				if(c == 1){
					data.put(item);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		return data;
	}
	CrystalButton currentButton;
	public void onItemClick(View view, JSONObject item) {
		// TODO Auto-generated method stub
		Log.d(TAG, item.toString());
		try {
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				Toast.makeText(DownloadGridViewActivity.this, R.string.download_sdcard_mounted, Toast.LENGTH_SHORT).show();
				return;
			}
			if(view instanceof CrystalButton){
				currentButton = (CrystalButton) view;
			}
			final String menuID = item.getString("id");
			String name = item.getString("t");
			String text = String.format("打包下载\"%s\"表情?", name);
			Dialog dialog = DialogFactory.createTwoButtonDialog(DownloadGridViewActivity.this, text, "确定", "取消", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					downloadTask = new DownloadPackageTask(menuID);
					mDialog = new DownloadProgressDialog(DownloadGridViewActivity.this);
					mDialog.show();
					mDialog.setButtonClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(downloadTask != null){
								downloadTask.stop();
								mDialog.dismiss();
							}
						}
					});
					GDUtils.getExecutor(getApplicationContext()).execute(downloadTask);
				}
			}, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	
	class DownloadPackageTask implements Runnable{
		String mID;
		JSONArray mArray;
		boolean isRun = true;
		public DownloadPackageTask(String id){
			mID = id;
			String json = Emoj.getEmoj(getApplicationContext(),id);
			try {
				JSONObject object = new JSONObject(json);
				mArray = object.getJSONArray("data");
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		public void stop(){
			isRun = false;
		}
		public void run() {
			// TODO Auto-generated method stub
			for(int i = 0;i<mArray.length();i++){
				if(!isRun){
					return;
				}
				try {
					String filename = mArray.getString(i);
					download(filename);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
				}
				int rate = ((i+1)*100)/mArray.length();
				mHandler.sendMessage(Message.obtain(mHandler, 1, rate));
			}
			mHandler.sendMessage(Message.obtain(mHandler, 2, mID));
		}
		
		private void download(String filename){
			try {
				String downloadURL = AppConstant.PIC_SERVER_URL + filename + AppConstant.PIC_ITEM_FULL_PREFIX;
				InputStream is = new URL(downloadURL).openStream();
				FileType mFileType = FileTypeJudge.getType(is);
				String mFilePath = AppConfig.getEmoj()+filename+"."+mFileType.toString();
				Log.d(TAG, "mFilePath:"+mFilePath);
				File file = new File(mFilePath);
				//该表情已下载则生成缩略图
				if(file.exists() && file.length() > 0){
					Log.d(TAG, "该表情已存在,直接生成缩略图");
					makeThumb(mFilePath, filename);
					return;
				}
				URL imgUrl=new URL(downloadURL);
				URLConnection connection = imgUrl.openConnection();
				int size=connection.getContentLength();
				byte buffer[] = new byte[1024*4];
				int len=0;
				is = connection.getInputStream();
				OutputStream os=new FileOutputStream(file);
				while((len=is.read(buffer))!=-1){  
	                os.write(buffer, 0, len);  	               
	            }
				is.close();
				os.close();
				Log.d(TAG, "下载成功");
				//生成缩略图
				makeThumb(mFilePath, filename);
			} 			
			catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			
		}
		
		private void makeThumb(String sourcePath,String filename){
			String path = AppConfig.getThumb() + filename;	
			try {
				//生成缩略图
				File file = new File(path);
				//如果缩略图已存在则跳过
				if(file.exists() && file.length() > 0){
					Log.d(TAG, "缩略图已存在,跳过...");
					return;
				}
				File sFile = new File(sourcePath);
				InputStream stream = new FileInputStream(sFile);
				Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(stream));
				stream.close();
				if(bitmap == null){
					return;
				}

				FileOutputStream os = new FileOutputStream(file);
				bitmap.compress(CompressFormat.PNG, 100, os);
				os.close();
				bitmap.recycle();
				Log.d(TAG, "生成缩略图成功");
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	
	class DownloadPackageHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO 1-报告进度 2-下载完成 
			switch (msg.what) {
			case 1:
				int rate = (Integer) msg.obj;
				if(mDialog != null){
					try {
						mDialog.setRate(rate);
					} catch (Exception e) {
						// TODO: handle exception
						Log.e(TAG, e.toString());
					}
				
				}
				break;
			case 2:
				String id = msg.obj.toString();
				AppConfig.setIsDownload(getApplicationContext(), id);
				if(mDialog != null){
					try {
						mDialog.setTitle("下载完成");
						mDialog.dismiss();
						Toast.makeText(DownloadGridViewActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
						if(currentButton != null){
							currentButton.setBanner(R.drawable.hasdownload);
						}
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
}
