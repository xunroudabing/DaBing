package com.dabing.emoj.advertise;

import net.youmi.android.banner.AdSize;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.openapi.v1.AppConnect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dabing.emoj.R;
import com.dabing.emoj.advertise.AdManager.AdShowType;
import com.dabing.emoj.utils.AppConfig;
/**
 * 自定义混合广告控件
 * @author DaBing
 *
 */
public class MixAdView extends LinearLayout {
	LinearLayout waps_banner,waps_mini,youmi_banner;
	WAPS_CustomAd waps_custom;
	String mTAG;
	static final String TAG = MixAdView.class.getSimpleName();
	public MixAdView(Context context){
		this(context, null);
	}
	public MixAdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.mix_adview, this, true);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MixAdView, 0, 0);
		mTAG = a.getString(R.styleable.MixAdView_tag);
		a.recycle();
		InitView();
	}
	/**
	 * 设置标签名
	 * @param tag ad_index1-我的表情 ad_index2-心情模式 ad_index3-自定义 ad_index4-微频道
	 */
	public void setAdTag(String tag){
		mTAG = tag;
	}
	protected void InitView(){
		waps_banner = (LinearLayout) findViewById(R.id.waps_banner);
		waps_custom = (WAPS_CustomAd) findViewById(R.id.waps_custom);
		waps_mini = (LinearLayout) findViewById(R.id.waps_mini);
		youmi_banner = (LinearLayout) findViewById(R.id.youmi_banner);		
	}
	/**
	 * 广告初始化，展现广告必须调用此方法
	 */
	public void InitAD(){
		// 广告开关
		boolean ad_switch = AppConfig.getAdvertise_onV2(getContext());
		if (ad_switch) {
			// 是否移除广告
			boolean IsRemove = AppConfig.getAdvertiseRemove(getContext());
			if (IsRemove) {
				return;
			}
			String adShowType = AppConfig.getAdvertiseTAG(getContext(), mTAG);
			Log.d(TAG, "mTag:" + mTAG + " " + adShowType);
			AdShowType showType = AdShowType.getAdShowType(adShowType);
			switch (showType) {
			case QQ_BANNER:
				//tencent_banner.setVisibility(View.VISIBLE);
				break;
			case WAPS_BANNER:
				waps_banner.setVisibility(View.VISIBLE);
				//new AdView(getContext(), waps_banner).DisplayAd();
				AppConnect.getInstance(getContext()).showBannerAd(getContext(), waps_banner);
				break;
			case WAPS_CUSTOM:
				waps_custom.setVisibility(View.VISIBLE);
				waps_custom.start();
				break;
			case WAPS_MINI:
				waps_mini.setVisibility(View.VISIBLE);
				AppConnect.getInstance(getContext()).setAdBackColor(
						getResources().getColor(R.color.app_panel_bg));
				// 设置迷你广告广告语颜色
				AppConnect.getInstance(getContext())
						.setAdForeColor(Color.WHITE);
				//new MiniAdView(getContext(), waps_mini).DisplayAd(10);
				AppConnect.getInstance(getContext()).showMiniAd(getContext(), waps_mini, 10);
				break;
			case YOUMI_BANNER:
				youmi_banner.setVisibility(View.VISIBLE);
				net.youmi.android.banner.AdView adView = new net.youmi.android.banner.AdView(
						getContext(), AdSize.FIT_SCREEN);
				youmi_banner.addView(adView);
				break;
			default:
				break;
			}
		}
	}
}
