package com.dabing.emoj.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dabing.emoj.R;
/**
 * ChannelCheckBox
 * @author Administrator
 *
 */
public class ChannelCheckBox extends LinearLayout {
	//***接口***
	public interface AfterCheckedChangeListener{
		void onCheckedChanged(ChannelCheckBox view,boolean checked);
	}
	public interface BeforeCheckedChangeListener{
		/**
		 * 
		 * @param view
		 * @param checked
		 * @return true-吃掉该事件 false-放行
		 */
		boolean interception(ChannelCheckBox view,boolean checked);
	}
	BeforeCheckedChangeListener mBeforeCheckedChangeListener;
	AfterCheckedChangeListener mCheckedChangeListener;
	boolean mChecked = false;
	int mCheckedResId = -1;
	int mResId = -1;
	ImageView mView;
	static final String TAG = ChannelCheckBox.class.getSimpleName();
	public ChannelCheckBox(Context context){
		this(context, null);
	}
	public ChannelCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.channel_checkbox, this, true);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChannelCheckBox, 0, 0);
		mResId = a.getResourceId(R.styleable.ChannelCheckBox_src, -1);
		mCheckedResId = a.getResourceId(R.styleable.ChannelCheckBox_checked_src, -1);
		a.recycle();
		InitView();
	}
	public void setChecked(boolean checked){
		setChecked(checked, false,false);
	}
	/**
	 * 
	 * @param checked
	 * @param interception 是否触发interception事件
	 * @param onchange 是否触发onchange事件
	 */
	public void setChecked(boolean checked,boolean interception,boolean onchange){
		if (mChecked == checked) {
			return;
		}
		if (interception) {
			boolean skip = false;
			if (mBeforeCheckedChangeListener != null) {
				skip = mBeforeCheckedChangeListener.interception(this, checked);
			}
			if (skip) {
				return;
			}
		}
		mChecked = checked;
		updateUI();
		if (onchange) {
			if (mCheckedChangeListener != null) {
				mCheckedChangeListener.onCheckedChanged(this, mChecked);
			}
		}
	}
	public boolean isChecked(){
		return mChecked;
	}
	public void toggle(){
		setChecked(!mChecked,true,true);
	}
	public void setOnCheckedChangeListener(AfterCheckedChangeListener listener){
		mCheckedChangeListener = listener;
	}
	public void setInterception(BeforeCheckedChangeListener listener){
		mBeforeCheckedChangeListener = listener;
	}
	protected void InitView(){
		mView = (ImageView) findViewById(R.id.channel_checkbox_img);
		if(mResId != -1){
			mView.setImageResource(mResId);
		}
		mView.setOnClickListener(onClickListener);
	}
	protected void updateUI(){
		if(mChecked){
			if(mCheckedResId != -1){
				mView.setImageResource(mCheckedResId);
			}
		}else {
			if(mResId != -1){
				mView.setImageResource(mResId);
			}
		}
	}
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			toggle();
		}
	};
}
