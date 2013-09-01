package com.dabing.emoj.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FitSizeImageView extends ImageView {
	int mWidth = 0;
	static final String TAG = FitSizeImageView.class.getSimpleName();
	public FitSizeImageView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public FitSizeImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public void setWidth(int width){
		mWidth = width;
	}
	/* (non-Javadoc)
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG, "widthMode:"+widthMode+" widthSize:"+widthSize+" heightMode:"+heightMode+" heightSize:"+heightSize);
		int w = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		int h = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		super.onMeasure(w, h);
	}

}
