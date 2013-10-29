package com.dabing.emoj.widget;

import org.w3c.dom.Text;

import com.dabing.emoj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * @author DaBing
 *
 */
public class ChannelListItem extends LinearLayout {
	int img1_width,img2_width;
	int mWidth;
	String mChannelID;
	TextView mTextView;
	FitSizeAsyncImageView img1,img2,img3;
	static final int COLUM_PADDING = 10;
	static final String TAG = ChannelListItem.class.getSimpleName();
	public ChannelListItem(Context context){
		this(context, null);
	}
	public ChannelListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.channel_add_category_list_item, this, true);
		mTextView = (TextView) findViewById(R.id.channel_add_category_list_item_title);
		img1 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img1);
		img2 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img2);
		img3 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img3);
	}
	public void setTitle(CharSequence title){
		mTextView.setText(title);
	}
	public void setChannelID(String id){
		mChannelID = id;
	}
	public void setWidth(int width){
		mWidth = width;
		img2_width = (mWidth - COLUM_PADDING * 2 - 5) / 3;
		img1_width = img2_width * 2 + 5;
		Log.d(TAG, "img1:"+img1_width + " img2:"+img2_width);
		img1.setWidth(img1_width);
		img2.setWidth(img2_width);
		img3.setWidth(img2_width);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	// @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//将dp换算为px
		int width_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWidth, getResources().getDisplayMetrics());
		Log.d(TAG, "px:"+width_px);
		int width = MeasureSpec.makeMeasureSpec(width_px, MeasureSpec.EXACTLY);
		super.onMeasure(width, heightMeasureSpec);
	}
}
