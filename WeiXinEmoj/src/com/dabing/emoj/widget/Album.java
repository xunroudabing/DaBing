package com.dabing.emoj.widget;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.FileInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Album extends LinearLayout {
	
	ImageView imageView;
	TextView albumNameTextView;
	int Album_Width = 80;
	static final String TAG = Album.class.getSimpleName();
	public Album(Context context){
		this(context, null);
	}
	public Album(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.user_define_album, this, true);
		imageView = (ImageView) findViewById(R.id.user_define_img);
		albumNameTextView = (TextView) findViewById(R.id.user_define_txt);
	}
	
	public void setFile(FileInfo fileInfo){
		albumNameTextView.setText(String.format("%s(%d)", fileInfo.fileName,fileInfo.Count));
	}
	public void setWidth(int width){
		Album_Width = width;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = MeasureSpec.makeMeasureSpec(Album_Width, MeasureSpec.EXACTLY);
		super.onMeasure(width, heightMeasureSpec);
	}
}
