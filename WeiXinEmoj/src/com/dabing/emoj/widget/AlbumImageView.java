package com.dabing.emoj.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dabing.emoj.R;

public class AlbumImageView extends LinearLayout {
	

	int mWidth = 80;
	ImageView mImageView;
	static final String TAG = Album.class.getSimpleName();
	public AlbumImageView(Context context){
		this(context, null);
	}
	public AlbumImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AlbumImageView, 0, 0);
		int resId = a.getResourceId(R.styleable.AlbumImageView_default_src, -1);
		LayoutInflater.from(context).inflate(R.layout.user_define_album_image, this, true);
		mImageView = (ImageView) findViewById(R.id.user_define_album_image_imageview);
		if(resId != -1){
			mImageView.setImageResource(resId);
		}
		a.recycle();
		
	}
	
	public void setWidth(int width){
		mWidth = width;
	}
	
	public void setImageBitmap(Bitmap bm){
		mImageView.setImageBitmap(bm);
	}
	
	public void setImageDrawable(Drawable drawable){
		mImageView.setImageDrawable(drawable);
	}
	
	public void setImageResource(int resId){
		mImageView.setImageResource(resId);
	}
	/* (non-Javadoc)
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG, "widthMode:"+widthMode+" widthSize:"+widthSize+" heightMode:"+heightMode+" heightSize:"+heightSize);
		int w = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		int h = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		super.onMeasure(w, h);
	}
}
