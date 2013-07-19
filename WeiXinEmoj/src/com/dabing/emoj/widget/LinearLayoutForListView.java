package com.dabing.emoj.widget;


import com.dabing.emoj.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class LinearLayoutForListView extends LinearLayout {
	static final String TAG = LinearLayoutForListView.class.getSimpleName();
	public LinearLayoutForListView(Context context){
		this(context, null);
	}
    public LinearLayoutForListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//LayoutInflater.from(context).inflate(R.layout.linearlayout_forlistview, this, true);
	}

	private BaseAdapter adapter;
    private OnClickListener onClickListener = null;
    private int divider = R.layout.divider;
    public void SetDivider(int resid){
    	divider = resid;
    }
    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, this);
            //Log.d(TAG, "---------:"+i);
            if(this.onClickListener != null){
            	v.setOnClickListener(this.onClickListener);
            }
            addView(v);
            if(i != count - 1){
                View divider=LayoutInflater.from(getContext()).inflate(this.divider, this,false);
            	addView(divider);
            }
        }
        
        Log.v("countTAG", "" + count);
    }
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
    	@Override
    	public void onChanged() {
    		setAdapter(adapter);
    	};
	};	

    /**
     * 获取Adapter
     * 
     * @return adapter
     */
    public BaseAdapter getAdpater() {
        return adapter;
    }

    /**
     * 设置数据
     * 
     * @param adpater
     */
    public void setAdapter(BaseAdapter ad) {
    	if(null != ad){
    		if(this.adapter != null){
    			this.adapter.unregisterDataSetObserver(mDataSetObserver);
    		}
    	}
    	removeAllViews();
        this.adapter = ad;
        if(null != ad){
        	this.adapter.registerDataSetObserver(mDataSetObserver);
        }
        bindLinearLayout();
        requestLayout();
        invalidate();
    }
    public void addFootView(View view){
    	View divider=LayoutInflater.from(getContext()).inflate(this.divider, this,false);
     	addView(divider);
     	addView(view);
    }
    /**
     * 获取点击事件
     * 
     * @return
     */
    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    /**
     * 设置点击事件
     * 
     * @param onClickListener
     */
    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
