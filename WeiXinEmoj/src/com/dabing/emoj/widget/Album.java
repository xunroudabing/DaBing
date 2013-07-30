package com.dabing.emoj.widget;

import greendroid.util.GDUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ExecutorService;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.utils.Util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Album extends LinearLayout {
	FileInfo mFileInfo;
	ImageView imageView;
	TextView albumNameTextView;
	int Album_Width = 80;
	static ExecutorService mService;
	static final String TAG = Album.class.getSimpleName();
	public Album(Context context){
		this(context, null);
	}
	public Album(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.user_define_album, this, true);
		imageView = (ImageView) findViewById(R.id.user_define_img);
		albumNameTextView = (TextView) findViewById(R.id.user_define_txt);
		if(mService == null){
			mService = GDUtils.getExecutor(context);
		}
	}
	
	public void setFile(FileInfo fileInfo){
		if(fileInfo == null){
			return;
		}
		mFileInfo = fileInfo;
		albumNameTextView.setText(String.format("%s(%d)", fileInfo.fileName,fileInfo.Count));
	}
	public void setWidth(int width){
		Album_Width = width;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = MeasureSpec.makeMeasureSpec(Album_Width, MeasureSpec.EXACTLY);
		super.onMeasure(width, heightMeasureSpec);
	}
	static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
	//获取相册下的图片数,并更新至相册名称
	class GetFileTask implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String path = mFileInfo.filePath;
				if (mFileInfo.IsDir) {
					int lCount = 0;
					File lFile = new File(path);
					File[] files = lFile.listFiles(new FilenameFilter() {

						@Override
						public boolean accept(File dir, String filename) {
							// TODO Auto-generated method stub
							return filename.matches(IMAGE_PATTERN);
						}
					});

					// null means we cannot access this dir
					if (files == null) {
						return;
					}

					for (File child : files) {
						if ((!child.isHidden())
								&& Util.isNormalFile(child.getAbsolutePath())) {
							lCount++;
						}
					}
					mFileInfo.Count = lCount;
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}

	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
}
