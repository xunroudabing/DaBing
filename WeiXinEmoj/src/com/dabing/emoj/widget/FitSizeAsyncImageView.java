package com.dabing.emoj.widget;

import greendroid.widget.AsyncImageView;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
/**
 * 制定宽度高度的AsyncImageView
 * @author DaBing
 *
 */
public class FitSizeAsyncImageView extends AsyncImageView {
	int mWidth;
	static final String TAG = FitSizeAsyncImageView.class.getSimpleName();
	public FitSizeAsyncImageView(Context context,AttributeSet attrs){
		this(context, attrs, 0);
	}
	public FitSizeAsyncImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
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
}
