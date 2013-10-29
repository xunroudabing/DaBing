package com.dabing.emoj.widget;

import org.w3c.dom.Text;

import com.dabing.emoj.R;

import android.content.Context;
import android.util.AttributeSet;
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
	int mWidth;
	String mChannelID;
	TextView mTextView;
	ImageView img1,img2,img3;
	static final String TAG = ChannelListItem.class.getSimpleName();
	public ChannelListItem(Context context){
		this(context, null);
	}
	public ChannelListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.channel_add_category_list_item, this, true);
		mTextView = (TextView) findViewById(R.id.channel_add_category_list_item_title);
	}
	public void setTitle(CharSequence title){
		mTextView.setText(title);
	}
	public void setChannelID(String id){
		mChannelID = id;
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
		int width = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		super.onMeasure(width, heightMeasureSpec);
	}
}
