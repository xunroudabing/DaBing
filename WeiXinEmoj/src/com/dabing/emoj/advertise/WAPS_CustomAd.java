package com.dabing.emoj.advertise;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.openapi.v1.AdInfo;
import android.openapi.v1.AppConnect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConstant;
/**
 * 万普自定义广告条
 * @author DaBing
 *
 */
public class WAPS_CustomAd extends LinearLayout implements OnClickListener {
	WapsHandler mHandler = new WapsHandler();
	boolean pause = false;
	AdInfo currentAdInfo;
	Timer mTimer;
	ImageView imageView;
	TextView nameView,detailView;
	Animation[] animations;
	Random random;
	static final int[] ANIMATION_ARRAY = {R.anim.overshoot_left_in,R.anim.overshoot_right_in,R.anim.overshoot_up_in,R.anim.overshoot_down_in};
	static final long DELAY = 0;
	static final long PERIOD = 10000;
	static final String TAG = WAPS_CustomAd.class.getSimpleName();
	public WAPS_CustomAd(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.waps_custom_ad, this, true);
		nameView = (TextView) findViewById(R.id.waps_custom_ad_appname);
		detailView = (TextView) findViewById(R.id.waps_custom_ad_appdetail);
		imageView = (ImageView) findViewById(R.id.waps_custom_ad_icon);
		InitAnimation();
		Init();
	}
	private void InitAnimation(){
		random = new Random();
		animations = new Animation[ANIMATION_ARRAY.length];
		for(int i=0;i<ANIMATION_ARRAY.length;i++){
			animations[i] = AnimationUtils.loadAnimation(getContext(), ANIMATION_ARRAY[i]);
		}
	}
	private void Init(){
		findViewById(R.id.waps_custom_ad_layout).setOnClickListener(this);
	}
	private void BindAd(AdInfo info){
		currentAdInfo = info;
		String name = info.getAdName();
		String detail = info.getAdText();
		
		nameView.setText(name);
		detailView.setText(detail);
		imageView.setImageBitmap(info.getAdIcon());
		int seed = random.nextInt(animations.length);
		Log.d(TAG, "seed:"+seed);
		nameView.startAnimation(animations[seed]);
		detailView.startAnimation(animations[seed]);
	}
	public void start(){
		try {
			if(mTimer == null){
				mTimer = new Timer("ADTimer");
			}
			mTimer.schedule(new WAPS_AD_TASK(), DELAY, PERIOD);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}		
		
	}
	/**
	 * 暂停
	 * @param b true-暂停 false-取消暂停
	 */
	public void setPause(boolean b){
		pause = b;
	}
	
	public void stop(){
		try {
			if(mTimer != null){
				mTimer.cancel();
				Log.d(TAG, "AD stop");
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	class WAPS_AD_TASK extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				if(pause){
					return;
				}
				Log.d(TAG, "WAPS_AD_TASK getAdInfo");
				mHandler.sendEmptyMessage(0);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		
	}
	class WapsHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				try {
					AdInfo info = AppConnect.getInstance(getContext()).getAdInfo();
					if(info != null){
						BindAd(info);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}				
				break;

			default:
				break;
			}
		}
		
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
		if(currentAdInfo == null){
			return;
		}
		WAPS_ADInfo adinfo = WAPS_ADInfo.Convert(currentAdInfo);
		Log.d(TAG, "adinfo:"+adinfo.toString());
		Intent intent = new Intent(getContext(), WAPS_AppDetailActivity.class);
		intent.putExtra(AppConstant.INTENT_WAPS_AD_DETAIL_HASMORE, true);
		intent.putExtra(AppConstant.INTENT_ADINFO, adinfo);
		intent.putExtra(AppConstant.INTENT_TITLE, adinfo.adName);
		getContext().startActivity(intent);
	}
}
