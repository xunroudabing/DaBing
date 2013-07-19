package com.dabing.emoj.widget;

import com.dabing.emoj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ColorButton extends RelativeLayout {
	String mID = "";
	TextView mTextView;
	ImageView mSelected;
	static final String TAG = ColorButton.class.getSimpleName();
	public ColorButton(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public ColorButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.colorbutton, this, true);
		mTextView = (TextView) findViewById(R.id.color_button_txt);
		mSelected = (ImageView) findViewById(R.id.color_button_img);
	}
	public void setID(String id){
		mID = id;
	}
	public String getID(){
		return mID;
	}
	public void setText(CharSequence text){
		mTextView.setText(text);
	}
	public void setTextBackgroundResource(int resId){
		mTextView.setBackgroundResource(resId);
	}
	public void setTextColor(int color){
		mTextView.setTextColor(getResources().getColor(color));	
	}
	public void setSelected(boolean b){
//		if(b){
//			mSelected.setVisibility(View.VISIBLE);
//		}else {
//			mSelected.setVisibility(View.GONE);
//		}
		if(b){
			mTextView.setBackgroundResource(R.drawable.textview_background_graydark);
		}else {
			mTextView.setBackgroundResource(R.drawable.textview_gray_selector);
		}
	}
}
