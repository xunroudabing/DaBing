package com.dabing.emoj.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FileType;
import com.dabing.emoj.utils.FileTypeJudge;
import com.dabing.emoj.utils.FlushedInputStream;
import com.dabing.emoj.utils.QStr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import greendroid.image.ImageCache;
import greendroid.util.GDUtils;
import greendroid.widget.AsyncImageView;
import greendroid.widget.EmojAsyncImageView;
import greendroid.widget.AsyncImageView.OnImageViewLoadListener;
/**
 * 带缓存功能的ImageView
 * @author DaBing
 *
 */
public class CacheImageView extends EmojAsyncImageView implements OnImageViewLoadListener {

	CacheHandler mHandler = new CacheHandler();
	static ExecutorService mService;
	static ImageCache mCache;
	OnImageViewLoadListener listener;
	Animation mAnimation;
	int mWidth;
	int mHeight;
	String mURL;
	boolean isLoading = false;
	static BitmapFactory.Options sDefaultOptions;
	static final String TAG = CacheImageView.class.getSimpleName();
	public CacheImageView(Context context){
		this(context, null);
	}
	public CacheImageView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public CacheImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		if(mService == null){
			mService = GDUtils.getExecutor(context);
					
		}
		if(mCache == null){
			mCache = GDUtils.getImageCache(context);	
			//mCache.setThumb(AppConfig.getThumb());
		}
		if (sDefaultOptions == null) {
			sDefaultOptions = new BitmapFactory.Options();
			sDefaultOptions.inDither = true;
			sDefaultOptions.inScaled = true;
			sDefaultOptions.inDensity = DisplayMetrics.DENSITY_DEFAULT;
			sDefaultOptions.inTargetDensity = getResources()
					.getDisplayMetrics().densityDpi;
		}
		mAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
		setOnImageViewLoadListener(this);
	}
	/**
	 * 从url获取图片
	 * @param url
	 */
	public void setImage(String url){  
		mURL = url;
		reloadImage();
	}
	/**
	 * 从file获取图片
	 * @param path
	 */
	public void setPath(String path,int width){
		setImageDrawable(null);
		Bitmap cache = GDUtils.getImageCache(getContext()).get(path);
		if(cache != null){
			mHandler.sendMessage(Message.obtain(mHandler, 1,
					cache));
			return;
		}
		mService.execute(new ThumbnailTask(path, width));
	}
	public void reloadImage(){
		setImageDrawable(null);
		mService.execute(new GetImageTask(mURL));
	}
	public void setLoadingListener(OnImageViewLoadListener l){
		listener = l;
	}
	public void onLoadingEnded(AsyncImageView imageView, Bitmap image) {
		// TODO Auto-generated method stub
		this.startAnimation(mAnimation);
		if(listener != null){
			listener.onLoadingEnded(imageView, image);
		}
		//mService.execute(new MakeTumbTask(image,mURL));
	}
	public void onLoadingFailed(AsyncImageView imageView, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}
	public void onLoadingStarted(AsyncImageView imageView) {
		// TODO Auto-generated method stub
		
	}
	private Bitmap makeThumb(String sourcePath,String filename){
		String path = AppConfig.getThumb() + filename;	
		try {
			File sFile = new File(sourcePath);
			InputStream stream = new FileInputStream(sFile);
			Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(stream));
			stream.close();
			if(bitmap == null){
				return null;
			}
			//生成缩略图
			File file = new File(path);
			FileOutputStream os = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, os);
			os.close();
			bitmap.recycle();
			InputStream is = new FileInputStream(file);
			Bitmap compressBitmap = BitmapFactory.decodeStream(is);
			is.close();
			return compressBitmap;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}
	/**
	 * 废弃
	 * @author DaBing
	 *
	 */
	class MakeTumbTask implements Runnable{
		private String _filename;
		private Bitmap mBitmap;
		private String _url;
		public MakeTumbTask(Bitmap bitmap,String url){
			_url = url;
			_filename = QStr.getPicNameFromURL(_url);
			mBitmap = bitmap;
		}
		public void run() {
			// TODO Auto-generated method stub
			if(mBitmap == null){
				return;
			}
			try {
				String thumb_path = AppConfig.getThumb() + _filename;
				File file = new File(thumb_path);
				OutputStream os = new FileOutputStream(file);
				mBitmap.compress(CompressFormat.PNG, 100, os);
				os.close();
				Log.d(TAG, "下载后生成缩略图");
				
            	//预加载
//            	FileType mFileType = FileTypeJudge.getTypeFromURL(_url);
//            	String mFilePath = AppConfig.getEmoj() + _filename + "." + mFileType.toString();
//            	//Log.d(TAG, "cached:"+mFilePath);
//                InputStream inputStream = new URL(_url).openStream();
//                File downloadFile = new File(mFilePath);	                    
//                OutputStream outputStream = new FileOutputStream(downloadFile);
//                byte buffer[] = new byte[1024*4];
//                int len = 0;
//                while((len = inputStream.read(buffer)) != -1){
//                	outputStream.write(buffer, 0, len);
//                }
//                inputStream.close();
//                outputStream.close();
                Log.d(TAG, "下载表情文件完毕");
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	class GetImageTask implements Runnable{
		private String _filename;
		private String _url;
		public GetImageTask(String url){
			_url = url;
			_filename = QStr.getPicNameFromURL(_url);
		}
		public void run() {
			// TODO Auto-generated method stub
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			Bitmap cacheBitmap = mCache.get(_url);
			if(cacheBitmap != null && !cacheBitmap.isRecycled()){
				Log.d(TAG, "取缓存:"+_url);
				mHandler.sendMessage(Message.obtain(mHandler, 1, cacheBitmap));
				return;
			}
			
			try {
				//缩略图
				String thumb_path = AppConfig.getThumb() + _filename;
				File thumb_file = new File(thumb_path);
				//存在缩略图
				if(thumb_file.exists() && thumb_file.length() > 0){
					InputStream is = new FileInputStream(thumb_file);
					Bitmap bitmap = BitmapFactory.decodeStream(is, null, mOptions);
					is.close();
					mCache.put(_url, bitmap);
					Log.d(TAG, "直接取缩略图:"+_filename);
					mHandler.sendMessage(Message.obtain(mHandler, 1, bitmap));
					return;
				}else {
					String filename = new FileHelper().find(AppConfig.getEmoj(), _filename);
					//存在原图
					if(filename != null){
						String source_path = AppConfig.getEmoj() + filename;
						File source_file = new File(source_path);
						if(source_file.exists() && source_file.length()>0){
						   //生成缩略图
						   Bitmap bitmap = makeThumb(source_path, _filename);
						   if(bitmap != null){
							   Log.d(TAG, "生成缩略图:"+_filename);
							   mCache.put(_url, bitmap);
							   mHandler.sendMessage(Message.obtain(mHandler, 1, bitmap));
							   return;
						   }
						}
					}
				}
				mHandler.sendMessage(Message.obtain(mHandler, 2, _url));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	}
	/**
	 * 获取缩略图
	 * @author DaBing
	 *
	 */
	public  class ThumbnailTask implements Runnable {
		String mPath;
		int mThumbWidth;
		public ThumbnailTask(String filepath,int width) {
			mPath = filepath;
			mThumbWidth = width;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				FileInputStream is = new FileInputStream(new File(mPath));
				Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
				is.close();
				//Bitmap bitmap = BitmapFactory.decodeFile(mPath, sDefaultOptions);
//				Bitmap bitmap = com.dabing.emoj.wxapi.Util.extractThumbNail(
//						mPath, mThumbWidth, mThumbWidth, true);
				//Bitmap bitmap = BitmapFactory.decodeFile(mPath);
				if (bitmap != null) {
					Bitmap resizeBitmap = com.dabing.emoj.wxapi.Util
							.resizeBitmap(bitmap, mThumbWidth, mThumbWidth);
					GDUtils.getImageCache(getContext()).put(mPath, resizeBitmap);
					mHandler.sendMessage(Message.obtain(mHandler, 1,
							resizeBitmap));
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}

	}
	class CacheHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO 1-成功 2-无缓存
			switch (msg.what) {
			case 1:
				Bitmap bitmap = (Bitmap) msg.obj;
				setImageBitmap(bitmap);
				startAnimation(mAnimation);
				break;
			case 2:
				String url = msg.obj.toString();
				setUrl(url);
				break;
			default:
				break;
			}
		}
		
	}
	

}
