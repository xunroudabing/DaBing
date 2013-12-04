package com.dabing.emoj.widget;

import greendroid.image.ImageProcessor;
import greendroid.image.ScaleImageProcessor;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabing.emoj.R;
import com.dabing.emoj.bonus.IBonusChangeListener;
import com.dabing.emoj.bonus.IBouns;
import com.dabing.emoj.bonus.WAPS_Bonus;
import com.dabing.emoj.db.ChannelDatabaseHelper;
import com.dabing.emoj.provider.ChannelRequest;
import com.dabing.emoj.provider.IRequest;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.Utils;
import com.dabing.emoj.widget.ChannelCheckBox.AfterCheckedChangeListener;
import com.dabing.emoj.widget.ChannelCheckBox.BeforeCheckedChangeListener;
import com.umeng.analytics.MobclickAgent;
/**
 * 
 * @author DaBing
 *
 */
public class ChannelListItem extends LinearLayout implements IRequest {
	IBonusChangeListener mBonusChangeListener;
	String mTitle;
	ChannelRequest mRequest;
	int img1_width = 105;//单位 px
	int img2_width = 50;//单位  px
	int mWidth;
	int mBonus = 0;
	String mChannelID;
	TextView mTextView;
	TextView mBonusTextView;
	FitSizeAsyncImageView img1,img2,img3;
	ChannelCheckBox mCheckBox;
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
		mBonusTextView = (TextView) findViewById(R.id.channel_add_category_list_item_bonus);
		img1 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img1);
		img2 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img2);
		img3 = (FitSizeAsyncImageView) findViewById(R.id.channel_add_category_list_item_img3);
		mCheckBox = (ChannelCheckBox) findViewById(R.id.channel_add_category_list_item_chkbtn);
		mCheckBox.setInterception(changeListener);
		mCheckBox.setOnCheckedChangeListener(afterCheckedChangeListener);
	}
	/**
	 * 设置监听事件
	 * @param listener
	 */
	public void setBonusChangeListener(IBonusChangeListener listener){
		mBonusChangeListener = listener;
	}
	/**
	 * 设置选中状态
	 * @param checked
	 */
	public void setChecked(boolean checked){
		mCheckBox.setChecked(checked);
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
	 * 设置积分
	 * @param bonus
	 */
	public void setBonus(int bonus){
		mBonus = bonus;
		//积分开关为开
		if (AppConfig.getBonusEnable(getContext())) {
			// 免费
			if (mBonus <= 0) {
				mBonusTextView.setText("");
				mBonusTextView.setVisibility(View.GONE);
			}
			// 收费
			else {
				//不显示具体数额 特此注释
				//mBonusTextView.setText(String.format("%d", mBonus));
				mBonusTextView.setVisibility(View.VISIBLE);
			}
		}else {
			mBonusTextView.setText("");
			mBonusTextView.setVisibility(View.GONE);
		}
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
	
	protected void addChannel(){
		try {
			ChannelDatabaseHelper helper = new ChannelDatabaseHelper(getContext());
			ContentValues cv = new ContentValues();
			cv.put(ChannelDatabaseHelper.FIELD_CHANNLEID, mChannelID);
			cv.put(ChannelDatabaseHelper.FIELD_NAME, mTitle);
			cv.put(ChannelDatabaseHelper.FIELD_TYPE, "common");
			cv.put(ChannelDatabaseHelper.FIELD_TIME, System.currentTimeMillis());			
			helper.insert(cv);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}		
		
	}
	
	protected void removeChannel(){
		try {
			ChannelDatabaseHelper helper = new ChannelDatabaseHelper(getContext());
			helper.remove(Long.parseLong(mChannelID));
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	private BeforeCheckedChangeListener changeListener = new BeforeCheckedChangeListener() {		
		@Override
		public boolean interception(final ChannelCheckBox view, boolean checked) {
			// TODO Auto-generated method stub
			Log.d(TAG, "interception checked:"+checked);
			if(checked){
				if(!AppConfig.getBonusEnable(getContext())){
					return false;
				}
				//收费的
				if(mBonus > 0){
					//是否已购买
					boolean isBuyed = AppConfig.getChannelBuyed(getContext(), Integer.parseInt(mChannelID));
					//已购买
					if(isBuyed){
						//写入订阅的频道库
					}
					//未购买
					else {
						final IBouns helper = new WAPS_Bonus(getContext());
						helper.setBonusChangeListener(mBonusChangeListener);
						//获取拥有的积分
						int wealth = helper.get();
						//积分足够提示是否购买
						if(wealth >= mBonus){
							Log.d(TAG, "have enough bonus,ready to buy?");
							String tmp1 = getResources().getString(R.string.alert_confirm_channel_add);
							String txt1 = tmp1.replace("{bonus}", String.valueOf(mBonus));
							Dialog dialog1 = DialogFactory.createTwoButtonDialog(getContext(), txt1, null, null, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									Log.d(TAG, "ok");
									helper.spend(mBonus);
									view.setChecked(true, false,true);
									//设为已购买
									AppConfig.setChannelBuyed(getContext(), Integer.parseInt(mChannelID));
									UmengEvent("action031", mTitle);
									dialog.dismiss();
								}
							}, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									Log.d(TAG, "cancel");
									dialog.dismiss();
								}
							});
							dialog1.show();
							return true;
						}
						//积分不足提示获取积分
						else {
							Log.d(TAG, "not enough bonus");
							String tmp2 = getResources().getString(R.string.alert_confirm_channel_getbonus);
							String txt2 = tmp2.replace("{bonus}", String.valueOf(mBonus));
							Dialog dialog2 = DialogFactory.createTwoButtonDialog(getContext(), txt2, "获取铜板", null, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									Log.d(TAG, "ok");
									helper.showOffers();
									dialog.dismiss();
								}
							}, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									Log.d(TAG, "cancel");
									dialog.dismiss();
								}
							});
							dialog2.show();
							return true;
						}
					}
				}else {
					//无积分
					UmengEvent("action032", mTitle);
				}
			}else {
				//取消选中
			}
			return false;
		}
		
	};

	private AfterCheckedChangeListener afterCheckedChangeListener = new AfterCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(ChannelCheckBox view, boolean checked) {
			// TODO Auto-generated method stub
			Log.d(TAG, mChannelID + " " + "onCheckedChanged");
			ChannelDatabaseHelper helper = new ChannelDatabaseHelper(getContext());
			if(checked){
				Log.d(TAG, "addChannel:"+mChannelID);
				addChannel();
			}else {
				Log.d(TAG, "removeChannel:"+mChannelID);
				removeChannel();
			}
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
	
	protected void showToast(int value){
		View view = LayoutInflater.from(getContext()).inflate(R.layout.bonus_alert_toast, null);
		TextView txt = (TextView) view.findViewById(R.id.bonus_alert_toast_txt);
		String s = value > 0 ? String.format("+%d", value):String.valueOf(value);
		txt.setText(s);
		Toast toast = new Toast(getContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
	
	protected void UmengEvent(String eventid,String parms){
		Map<String, String> map = new HashMap<String, String>();
		map.put("parms", parms);
		//map.put("filename", filename);
		MobclickAgent.onEvent(getContext(), eventid, map);
	}
}
