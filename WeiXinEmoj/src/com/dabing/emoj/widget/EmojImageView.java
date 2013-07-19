package com.dabing.emoj.widget;


import com.dabing.emoj.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import greendroid.image.ChainImageProcessor;
import greendroid.image.ImageProcessor;
import greendroid.image.ScaleImageProcessor;
import greendroid.widget.AsyncImageView;
import greendroid.widget.AsyncImageView.OnImageViewLoadListener;

public class EmojImageView extends AsyncImageView implements OnImageViewLoadListener {
	OnImageViewLoadListener listener;
	Animation mAnimation;
	int mWidth;
	int mHeight;
	static final String TAG = EmojImageView.class.getSimpleName();
	public EmojImageView(Context context){
		this(context, null);
	}
	public EmojImageView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public EmojImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
		setOnImageViewLoadListener(this);
	}
	/* (non-Javadoc)
	 * @see android.widget.ImageView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG, "widthMode:"+widthMode+" widthSize:"+widthSize+" heightMode:"+heightMode+" heightSize:"+heightSize);
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
	}

	public void onLoadingFailed(AsyncImageView imageView, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	public void onLoadingStarted(AsyncImageView imageView) {
		// TODO Auto-generated method stub
		
	}

}
