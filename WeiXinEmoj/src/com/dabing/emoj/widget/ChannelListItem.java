package com.dabing.emoj.widget;

import java.util.HashMap;
import java.util.Map;

import greendroid.image.ImageProcessor;
import greendroid.image.ScaleImageProcessor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.dabing.ads.ar;
import com.dabing.emoj.R;
import com.dabing.emoj.provider.ChannelRequest;
import com.dabing.emoj.provider.IRequest;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.Utils;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * @author DaBing
 *
 */
public class ChannelListItem extends LinearLayout implements IRequest {
	String mTitle;
	ChannelRequest mRequest;
	int img1_width = 105;//单位 px
	int img2_width = 50;//单位  px
	int mWidth;
	String mChannelID;
	TextView mTextView;
	FitSizeAsyncImageView img1,img2,img3;
	CheckBox mCheckBox;
	static Map<String, String[]> CHANNEL_CACHE = new HashMap<String, String[]>();
	static final int THUMB_SIZE = 3;//请求图片数量
	static final int COLUM_PADDING = 10;
	static final String TAG = ChannelListItem.class.getSimpleName();
	public ChannelListItem(Context context){
		this(context, null);
	}
	public ChannelListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.channel_add_category_list_item, this, true);
		mTextView = (TextView) findViewById(R.id.channel_add_category_list_item_title);
		img1 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img1);
		img2 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img2);
		img3 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img3);
		mCheckBox = (CheckBox) findViewById(R.id.channel_add_category_chkbox);
		mCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
	}
	/**
	 * 设置频道名称
	 * @param title
	 */
	public void setTitle(CharSequence title){
		mTitle = title.toString();
		mTextView.setText(title);
	}
	/**
	 * 设置频道id
	 * @param id
	 */
	public void setChannelID(String id){
		mChannelID = id;
		String[] thumbs = CHANNEL_CACHE.get(mChannelID);
		if(thumbs != null && thumbs.length >= THUMB_SIZE){
			Log.d(TAG, mTitle + mChannelID + "get thumb from cache");
			ShowThumb(thumbs);
		}else {
			Log.d(TAG,mTitle + mChannelID + "get thumb new");
			mRequest = new ChannelRequest(getContext(), mChannelID, String.valueOf(THUMB_SIZE));
			mRequest.setOnRequestListener(this);
			mRequest.beginRequest();
		}
		
	}
	/**
	 * 设置频道收听状态
	 * @param checked
	 */
	public void setChecked(boolean checked){
		mCheckBox.setChecked(checked);
	}
	/**
	 * 设置控件宽度 
	 * @param width 单位dp
	 */
	public void setWidth(int width){
		mWidth = width;
		int img2_width_dp = (mWidth - COLUM_PADDING * 2 - 5) / 3;
		int img1_width_dp = img2_width_dp * 2 + 5;
		//Log.d(TAG, "img1:"+img1_width_dp+"dp" + " img2:"+img2_width_dp+"dp");
		img1.setWidth(img1_width_dp);
		img2.setWidth(img2_width_dp);
		img3.setWidth(img2_width_dp);
		//将dp转为px
		float density = Utils.getScreenDensity(getContext());
		img1_width = (int) (img1_width_dp * density);
		img2_width = (int) (img2_width_dp * density);
		//Log.d(TAG, "img1:"+img1_width+"px" + " img2:"+img2_width+"px");
	}
	
	public void clear(){
//		mChannelID = null;
//		img1.setImageDrawable(null);
//		img2.setImageDrawable(null);
//		img3.setImageDrawable(null);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	// @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//将dp换算为px
		int width_px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWidth, getResources().getDisplayMetrics());
		//Log.d(TAG, "px:"+width_px);
		int width = MeasureSpec.makeMeasureSpec(width_px, MeasureSpec.EXACTLY);
		super.onMeasure(width, heightMeasureSpec);
	}
	//缓存
	protected void CacheThumb(String[] array){
		CHANNEL_CACHE.put(mChannelID, array);
	}
	//显示封面图片
	protected void ShowThumb(String[] array){
		int h1 = img1.getMeasuredHeight();
		int w1 = img1.getMeasuredWidth();
		
		int h2 = img2.getMeasuredHeight();
		int w2 = img2.getMeasuredWidth();
//		Log.d(TAG, "img1:"+w1+"x"+h1);
//		Log.d(TAG, "img2:"+w2+"x"+h2);
		ImageProcessor processor1 = new ScaleImageProcessor(img1_width, img1_width, ScaleType.CENTER_CROP);
		ImageProcessor processor2 = new ScaleImageProcessor(img2_width, img2_width, ScaleType.CENTER_CROP);
		
		img1.setImageProcessor(processor1);
		img2.setImageProcessor(processor2);
		img3.setImageProcessor(processor2);
		
		img1.setUrl(array[0]);
		img2.setUrl(array[1]);
		img3.setUrl(array[2]);
//		Log.d(TAG, mTitle + " " + mChannelID);
//		Log.d(TAG, array[0]);
//		Log.d(TAG, array[1]);
//		Log.d(TAG, array[2]);

	}
	//点击checkbox时触发
	protected OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			Log.d(TAG, "isChecked:"+isChecked);
		}
	};
	
	//*****ChannelRequest事件*********
	@Override
	public void onBind(String response) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "ChannelId:"+mChannelID+" "+response);
		try {
			JSONObject object = new JSONObject(response);
			JSONArray array = object.getJSONObject("data").getJSONArray("info");
			//构造封面图片数组并缓存
			Log.d(TAG, mChannelID + " length:" + array.length());
			if(array.length() >= THUMB_SIZE){
				String[] thumbs = new String[THUMB_SIZE];
				for (int i = 0; i < THUMB_SIZE; i++) {
					String url;
					if(i == 0){
						url = getURL(array.getJSONObject(i)) + AppConstant.PIC_ITEM_PREFIX;
					}else {
						url = getURL(array.getJSONObject(i)) + AppConstant.PIC_ITEM_SMALL_PREFIX;
					}
					thumbs[i] = url;
				}
				//显示封面图片
				ShowThumb(thumbs);
				//缓存
				CacheThumb(thumbs);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	@Override
	public void onRefresh(String response) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onHasNext(String hasnext) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onException(Exception ex) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onException:"+ex);
	}
	@Override
	public void onLoading(String pageflag) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onRequestEnd() {
		// TODO Auto-generated method stub
		
	}
	//返回图片url
	protected String getURL(JSONObject object){
		try {
			if(object.getJSONObject("image").has("info")){
				JSONObject item = object.getJSONObject("image").getJSONArray("info").getJSONObject(0);
				String url = item.getJSONArray("url").getString(0);
				return url;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}
}
