package com.dabing.emoj.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;

public class CustomGridLayout extends GridLayout {
	View mFirstView;
	CursorAdapter mAdapter;
	static final String TAG = CustomGridLayout.class.getSimpleName();
	public CustomGridLayout(Context context){
		this(context, null);
	}
	public CustomGridLayout(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public CustomGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	private DataSetObserver mObserver = new DataSetObserver() {
		public void onChanged() {
			Log.d(TAG, "onChanged");
			setAdapter(mAdapter);
		};
	};
	public void setFirstView(View view){
		mFirstView = view;
	}
	public View getFirstView(){
		return mFirstView;
	}
	public void setAdapter(CursorAdapter adapter){
		if(adapter != null){
			if(mAdapter != null){
				mAdapter.unregisterDataSetObserver(mObserver);
			}
			mAdapter = adapter;
			mAdapter.registerDataSetObserver(mObserver);
		}
		
		removeAllViews();
		addViewsFromAdapter();
	}
	
	private void addViewsFromAdapter(){
		int count = mAdapter.getCount();
		Log.d(TAG, "count:"+count);
		for(int i = 0;i<mAdapter.getCount();i++){
			View view = mAdapter.getView(i, null, this);
			addView(view);
		}
		if(mFirstView != null){
			addView(mFirstView, 0);
		}
	}
	
	
}
