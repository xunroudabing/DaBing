package com.dabing.emoj.widget;

import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CrystalButton extends RelativeLayout implements OnClickListener {
	IEmojItemClickListener listener;
	JSONObject mJson;
	String ID = "";
	int mWidth = 80;
	RelativeLayout container;
	ImageView imageView,iconView,bannerView;
	public static String selected = "000";
	static final int PADDING = 10;
	static final String TAG = CrystalButton.class.getSimpleName();
	public CrystalButton(Context context){
		this(context, null);
	}
	public CrystalButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setBackgroundResource(R.drawable.crystal_rectangle_selector);
		setPadding(PADDING, PADDING, PADDING, PADDING);
		LayoutInflater.from(context).inflate(R.layout.crystal_button_background, this, true);
		imageView = (ImageView) findViewById(R.id.crystal_button_background_img);
		iconView = (ImageView) findViewById(R.id.crystal_button_background_icon);
		bannerView = (ImageView) findViewById(R.id.crystal_button_background_banner);
		container = (RelativeLayout) findViewById(R.id.crystal_button_background_container);
		setOnClickListener(this);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    //Log.d(TAG,"widthSize:"+widthSize+"widthMode:"+widthMode+ "onMeasure heightsize:"+heightSize+ " " + heightMode);
	    int widthSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		super.onMeasure(widthSpec, widthSpec);
	};
	public void setImageDrawable(Drawable d){
		imageView.setImageDrawable(d);
	}
	public void setBanner(int d){
		bannerView.setImageResource(d);
	}
	public void setBanner(Drawable d){
		bannerView.setImageDrawable(d);
	}
	public void setIcon(int d){
		iconView.setImageResource(d);
	}
	public void setIcon(Drawable d){
		iconView.setImageDrawable(d);
	}
	public void setIconVisibility(int visibility){
		iconView.setVisibility(visibility);
	}
	public void setWidth(int width){
		mWidth = width;
	}
	public void setSelected(boolean b){
		if(b){
			setBackgroundResource(R.drawable.crystal_rectangle_pressed);
		}else {
			setBackgroundResource(R.drawable.crystal_rectangle_selector);
		}
		setPadding(PADDING, PADDING, PADDING, PADDING);
	}

	public String getID(){
		return ID;
	}
	public void setID(String id){
		ID = id;
	}
	public void setJson(JSONObject item){
		mJson = item;
	}
	public void setIEmojItemClickListener(IEmojItemClickListener l){
		listener = l;
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(listener != null){
			listener.onItemClick(v, mJson);
		}
	}
	
	
}
