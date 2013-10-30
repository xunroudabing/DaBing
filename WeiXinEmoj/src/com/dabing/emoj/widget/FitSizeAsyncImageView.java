package com.dabing.emoj.widget;

import com.dabing.emoj.R;

import greendroid.widget.AsyncImageView;
import greendroid.widget.AsyncImageView.OnImageViewLoadListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
/**
 * 制定宽度高度的AsyncImageView
 * @author DaBing
 *
 */
public class FitSizeAsyncImageView extends AsyncImageView implements OnImageViewLoadListener{
	int mWidth;
	Animation mAnimation;
	static final String TAG = FitSizeAsyncImageView.class.getSimpleName();
	public FitSizeAsyncImageView(Context context,AttributeSet attrs){
		this(context, attrs, 0);
	}
	public FitSizeAsyncImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mAnimation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
		setOnImageViewLoadListener(this);
	}
	
	public void setWidth(int width){
		mWidth = width;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	// @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWidth, getResources().getDisplayMetrics());
		int width = MeasureSpec.makeMeasureSpec(px, MeasureSpec.EXACTLY);
		super.onMeasure(width, width);
	}
	@Override
	public void onLoadingEnded(AsyncImageView arg0, Bitmap arg1) {
		// TODO Auto-generated method stub
		this.startAnimation(mAnimation);
	}
	@Override
	public void onLoadingFailed(AsyncImageView arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadingStarted(AsyncImageView arg0) {
		// TODO Auto-generated method stub
		
	}
}
