package com.dabing.emoj.widget;

import greendroid.util.GDUtils;

import java.util.concurrent.ExecutorService;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabing.emoj.R;
import com.dabing.emoj.fragment.UserDefineFragment.IEmojScanCallBack;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.FileInfo;

public class Album extends LinearLayout {
	Dialog confirmDialog;
	boolean isDelMode;//是否是删除模式
	AlbumClickListener mListener;
	FileInfo mFileInfo;
	AlbumImageView imageView;
	ImageButton btnDel;
	TextView albumNameTextView;
	TextView childSizeTextView;
	int mWidth = 80;
	int mThumbWidth = 75;
	static Animation zoomOut;
	static Animation zoomEnter;
	static BitmapFactory.Options sDefaultOptions;
	static ExecutorService mService;
	static final String TAG = Album.class.getSimpleName();
	/**
	 * 相册点击事件
	 * @author DaBing
	 *
	 */
	public interface AlbumClickListener {
		void click(FileInfo fileInfo);
		void del(Album view,FileInfo fileInfo);
	}
	public Album(Context context) {
		this(context, null);
	}

	public Album(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.user_define_album, this,
				true);
		imageView = (AlbumImageView) findViewById(R.id.user_define_albumImageview);
		albumNameTextView = (TextView) findViewById(R.id.user_define_txt);
		childSizeTextView = (TextView) findViewById(R.id.user_define_childsize);
		btnDel = (ImageButton) findViewById(R.id.user_define_btnDel);
		if (mService == null) {
			mService = GDUtils.getExecutor(context);
		}
		if (sDefaultOptions == null) {
			sDefaultOptions = new BitmapFactory.Options();
			sDefaultOptions.inDither = true;
			sDefaultOptions.inScaled = true;
			sDefaultOptions.inDensity = DisplayMetrics.DENSITY_DEFAULT;
			sDefaultOptions.inTargetDensity = getResources()
					.getDisplayMetrics().densityDpi;
		}
		if(zoomEnter == null){
			zoomEnter = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_enter);
		}
		if(zoomOut == null){
			zoomOut = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
		}
		setOnClickListener(listener);
		btnDel.setOnClickListener(delListener);
	}
	public void setAlbumClickListener(AlbumClickListener listener){
		mListener = listener;
	}
	public void setFile(FileInfo fileInfo) {
		if (fileInfo == null) {
			return;
		}
		mFileInfo = fileInfo;
		albumNameTextView.setText(String.format("%s", fileInfo.fileName));
		childSizeTextView.setText(String.valueOf(fileInfo.Count));
		//mService.submit(new ThumbnailTask(mFileInfo.dbThumb));
		if(mFileInfo.dbThumb != null && !mFileInfo.dbThumb.equals("")){
			long id = Long.parseLong(mFileInfo.dbThumb);
			mService.submit(new ThumbnailProvider(id));
		}
		
	}
	public FileInfo getFile(){
		return mFileInfo;
	}
	public void setWidth(int width) {
		mWidth = width;
		mThumbWidth = mWidth - 5;
		imageView.setWidth(mThumbWidth);
	}

	/**
	 * 设置为删除模式
	 * 
	 * @param b
	 */
	public void setDelMode(boolean b) {
		isDelMode = b;
		if (b) {
			btnDel.startAnimation(zoomEnter);
			btnDel.setVisibility(View.VISIBLE);
		} else {
			btnDel.startAnimation(zoomOut);
			btnDel.setVisibility(View.GONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	// @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		super.onMeasure(width, heightMeasureSpec);
	}
	//点击删除时触发
	private OnClickListener delListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(confirmDialog == null){
				String txt = getContext().getString(R.string.alert_confirm_del_file);
				txt = txt.replace("{file}", "\""+mFileInfo.dbName+"\"");
				confirmDialog = DialogFactory.createTwoButtonDialog(getContext(), txt, null, null, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(mListener != null){
							mListener.del(Album.this, mFileInfo);
						}
						dialog.dismiss();
					}
				}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}
			confirmDialog.show();
		}
	};
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//删除模式不可点击
			if(isDelMode){
				return;
			}
				
			if(mListener != null){
				mListener.click(mFileInfo);
			}
		}
	};
	
	class ThumbnailProvider implements Runnable{
		long _id;
		public ThumbnailProvider(long id){
			_id = id;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String key = "Album_" + String.valueOf(_id);
				Bitmap cache = GDUtils.getImageCache(getContext()).get(key);
				if(cache != null){
					Log.d(TAG, "取缓存中的缩略图");
					mHandler.sendMessage(Message.obtain(mHandler, 1,
							cache));
					return;
				}
				Bitmap bitmap = Thumbnails.getThumbnail(getContext().getContentResolver(), _id, Thumbnails.MINI_KIND, sDefaultOptions);
				if (bitmap != null) {
					Log.d(TAG, "生成缩率图");
					Bitmap resizeBitmap = com.dabing.emoj.wxapi.Util
							.resizeBitmap2(bitmap, mThumbWidth, mThumbWidth);
					GDUtils.getImageCache(getContext()).put(key, resizeBitmap);
					mHandler.sendMessage(Message.obtain(mHandler, 1,
							resizeBitmap));
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	public  class ThumbnailTask implements Runnable {
		String mPath;

		public ThumbnailTask(String filepath) {
			mPath = filepath;

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Bitmap bitmap = com.dabing.emoj.wxapi.Util.extractThumbNail(
						mPath, mThumbWidth, mThumbWidth, true);
				if (bitmap != null) {
					Bitmap resizeBitmap = com.dabing.emoj.wxapi.Util
							.resizeBitmap(bitmap, mThumbWidth, mThumbWidth);
					mHandler.sendMessage(Message.obtain(mHandler, 1,
							resizeBitmap));
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Bitmap bitmap = (Bitmap) msg.obj;
				imageView.setImageBitmap(bitmap);
				break;

			default:
				break;
			}
		};
	};
}
