package com.dabing.emoj.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import greendroid.image.ImageProcessor;
import greendroid.util.FlushedInputStream;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.FileHelper;
import com.dabing.emoj.utils.QStr;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class WrapperImageView extends LinearLayout {
	boolean mPadding = true;
	int mWidth;
	int mHeight;
	ImageProcessor mProcessor;
	EmojImageView imageView;
	static BitmapFactory.Options sDefaultOptions;
	static final String TAG = WrapperImageView.class.getSimpleName();
	public WrapperImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WrapperImageView, 0, 0);
		mPadding = a.getBoolean(R.styleable.WrapperImageView_padding, true);
		if(mPadding){
			LayoutInflater.from(context).inflate(R.layout.emoj_item, this, true);
		}else {
			LayoutInflater.from(context).inflate(R.layout.emoj_item_nopadding, this, true);
		}
		a.recycle();
		imageView = (EmojImageView) findViewById(R.id.emoj_item_img);		
        if (sDefaultOptions == null) {
        	sDefaultOptions = new BitmapFactory.Options();
        	sDefaultOptions.inDither = true;
        	sDefaultOptions.inScaled = true;
        	sDefaultOptions.inDensity = DisplayMetrics.DENSITY_HIGH;
        	sDefaultOptions.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        }
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG, "widthMode:"+widthMode+" widthSize:"+widthSize+" heightMode:"+heightMode+" heightSize:"+heightSize);
		int widthSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		int heightSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
		super.onMeasure(widthSpec, heightSpec);
	};
	public void setWidth(int width){
		mWidth = width;
	}
	public void setHeight(int height){
		mHeight = height;
	}
	public void setUrl(String url){
//		String name = QStr.getPicNameFromURL(url);
//		String filename = new FileHelper().find(AppConfig.getEmoj(), name);
//		if(filename != null){
//			String path = AppConfig.getEmoj() + filename;
//			//Log.d(TAG, "path:"+path);
//			try {
//				File file = new File(path);
//				InputStream is = new FileInputStream(file);
//				Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
//				is.close();
//				imageView.setImageBitmap(bitmap);
//			} catch (Exception e) {
//				// TODO: handle exception
//				Log.e(TAG, e.toString());
//			}
//			
//			return;
//		}
		imageView.setUrl(url);
	}
	public void setImageProcessor(ImageProcessor processor){
		mProcessor = processor;
		imageView.setImageProcessor(mProcessor);
	}
}
