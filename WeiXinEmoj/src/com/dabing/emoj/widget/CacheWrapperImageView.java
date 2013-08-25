package com.dabing.emoj.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import greendroid.image.ImageProcessor;
import greendroid.util.GDUtils;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.FlushedInputStream;
import com.dabing.emoj.utils.PatchInputStream;
import com.dabing.emoj.utils.QStr;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CacheWrapperImageView extends LinearLayout {
	int mWidth;
	ImageProcessor mProcessor;
	CacheImageView imageView;
	static final String TAG = CacheWrapperImageView.class.getSimpleName();
	public CacheWrapperImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.cache_imageview, this, true);
		imageView = (CacheImageView) findViewById(R.id.cache_imageview_img);	
		imageView.setCachedPath(AppConfig.getEmoj());
		imageView.setThumbPath(AppConfig.getThumb());
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG, "widthMode:"+widthMode+" widthSize:"+widthSize+" heightMode:"+heightMode+" heightSize:"+heightSize);
		int widthSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		int heightSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		super.onMeasure(widthSpec, heightSpec);
	};
	
	public void setWidth(int width){
		mWidth = width;
	}
	public void setImage(String url){
		imageView.setImage(url);
	}
	public void setPath(String path){
		imageView.setPath(path, mWidth);
	}
	public void setImageProcessor(ImageProcessor processor){
		mProcessor = processor;
		imageView.setImageProcessor(mProcessor);
	}
	
	public void setCachedPath(String path){
		imageView.setCachedPath(path);
	}

}
