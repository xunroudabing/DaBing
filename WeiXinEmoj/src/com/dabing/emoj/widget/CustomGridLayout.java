package com.dabing.emoj.widget;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;

public class CustomGridLayout extends GridLayout {
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

}
