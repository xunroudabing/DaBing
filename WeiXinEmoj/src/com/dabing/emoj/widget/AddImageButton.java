package com.dabing.emoj.widget;

import com.dabing.emoj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

public class AddImageButton extends LinearLayout {
	int mWidth = 80;
	FitSizeImageView mImageView;
	static final String TAG = AddImageButton.class.getSimpleName();
	public AddImageButton(Context context){
		this(context, null);
	}
	public AddImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.user_define_album_add, this, true);
		mImageView = (FitSizeImageView) findViewById(R.id.user_define_album_add_btnAdd);
	}
	public void setWidth(int width){
		mWidth = width;
		mImageView.setWidth(width-50);
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

}
