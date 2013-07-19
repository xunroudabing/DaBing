package com.dabing.emoj.widget;

import java.util.LinkedList;
import java.util.List;

import javax.sql.RowSet;

import android.content.Context;
import android.database.DataSetObserver;
import android.inputmethodservice.Keyboard.Row;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.BaseAdapter;

public class EmotionPanel extends ViewGroup {
	BaseAdapter mAdapter;
	static final int mSpacing = 5;
	static final String TAG = EmotionPanel.class.getSimpleName();
	public EmotionPanel(Context context,AttributeSet attrs){
		this(context, attrs, 0);
	}
	public EmotionPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG,"widthSize:"+widthSize+"widthMode:"+widthMode+ "onMeasure heightsize:"+heightSize+ " " + heightMode);
	    measureChildren(widthMeasureSpec, heightMeasureSpec);
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    setMeasuredDimension(widthSize, heightSize);
	};
	@Override
	protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
		int width_mode = MeasureSpec.getMode(widthMeasureSpec);
		int width_size = MeasureSpec.getSize(widthMeasureSpec);
		//Log.d(TAG, "measureChildren width_mode:"+width_mode + " width_size:"+width_size);
		super.measureChildren(widthMeasureSpec, heightMeasureSpec);
	};
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		//Log.d(TAG, "onLayout width:"+width+ " height:"+height);
		int row = 0;
		int left = mSpacing;
		int top = mSpacing;
		Integer[] row_array = calculateRows(); 
		for(int i=0;i<getChildCount();i++){
			View child = getChildAt(i);
			int child_width = child.getMeasuredWidth();
			int child_height = child.getMeasuredHeight();
			//Log.d(TAG, "child:"+child_width + "x" + child_height);
			//换行
			if(left + child_width + mSpacing > width){
				left = mSpacing;
				top += child_height + mSpacing;
				row++;				
			}
			child.layout(left + row_array[row], top, left + row_array[row] + child_width, top + child_height);
			left += child_width + mSpacing;			
		}
	}
	private Integer[] calculateRows(){
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int left = mSpacing;
		int current_width = mSpacing;
		List<Integer> Rows = new LinkedList<Integer>();
		for(int i=0;i<getChildCount();i++){
			View child = getChildAt(i);
			int child_width = child.getMeasuredWidth();
			if(left + child_width + mSpacing > width){
				//换行
				int space = width - current_width;
				Rows.add(space/2);
				left = mSpacing;
				current_width = mSpacing;
			}
			current_width += child_width + mSpacing;
			left += child_width + mSpacing;
			if(i == getChildCount() -1){
				//最后一项
				int space = width - current_width;
				Rows.add(space/2);
			}
			
		}
		Integer[] result = new Integer[Rows.size()];
		return (Integer[])Rows.toArray(result);
	}

	private DataSetObserver mDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			setAdapter(mAdapter);
		};
	};
	/**
	 * 设置数据
	 * @param ad
	 */
    public void setAdapter(BaseAdapter ad) {
    	if(null != ad){
    		if(this.mAdapter != null){
    			this.mAdapter.unregisterDataSetObserver(mDataSetObserver);
    		}
    	}
    	removeAllViews();
        this.mAdapter = ad;
        if(null != ad){
        	this.mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        Bind();
        requestLayout();
        invalidate();
    }
    
    private void Bind(){
    	if(mAdapter == null){
    		return;
    	}
    	int count = mAdapter.getCount();
    	for(int i = 0; i < count;i++){
    		View view = mAdapter.getView(i, null, this);
    		if(view != null){
    			addView(view);
    		}
    	}
    }

}
