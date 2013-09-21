package com.dabing.emoj.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MatrixBitmap extends View {

	static final String TAG = MatrixBitmap.class.getSimpleName();
	public MatrixBitmap(Context context){
		this(context, null);
	}
	public MatrixBitmap(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public MatrixBitmap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
	}
}
