package com.dabing.emoj.utils;

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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dabing.emoj.widget.DownloadProgressDialog;

public class PackageDownloader {
	Context mContext;
	DownloadPackageTask mTask;
	DownloadProgressDialog mDialog;
	DownloadPackageHandler mHandler;
	static final String TAG = PackageDownloader.class.getSimpleName();
	public PackageDownloader(Context context){
		mContext = context;
		mDialog = new DownloadProgressDialog(mContext);
		mHandler = new DownloadPackageHandler();
		mDialog.setButtonClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mTask != null){
					mTask.stop();
					mDialog.dismiss();
				}
			}
		});
	}
	public void download(String menuid){
		mTask = new DownloadPackageTask(menuid);
		mDialog.show();
		GDUtils.getExecutor(mContext).execute(mTask);
	}
	class DownloadPackageTask implements Runnable{
		String mID;
		JSONArray mArray;
		boolean isRun = true;
		public DownloadPackageTask(String id){
			mID = id;			
			try {
				String json = AppConfig.getEmoj(mContext, id);
				Log.d(TAG, "json:"+json);
				//数组
				if(json.startsWith("[")){
					Log.d(TAG, "数组");
					mArray = new JSONArray(json);
				}else {
					JSONObject object = new JSONObject(json);
					mArray = object.getJSONArray("data");
				}
				

			} 
			catch (JSONException e1) {
				// TODO: handle exception
				Log.e(TAG, e1.toString());
			}
			catch (Exception e) {
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
				AppConfig.setIsDownload(mContext, id);
				if(mDialog != null){
					try {
						mDialog.setTitle("下载完成");
						mDialog.dismiss();
						Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
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
