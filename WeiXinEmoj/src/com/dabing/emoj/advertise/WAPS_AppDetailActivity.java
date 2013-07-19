package com.dabing.emoj.advertise;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.waps.AppConnect;
import cn.waps.MiniAdView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.WrapperImageView;

public class WAPS_AppDetailActivity extends BaseActivity implements OnClickListener {
	Button btnInstall,btnDownload;
	WAPS_ADInfo adInfo;
	TextView appnameView,versionView,filesizeView,providerView,appTextView,appDesView;
	ImageView iconView;
	WrapperImageView img1,img2;
	LinearLayout miniAdview;
	boolean hasMore = false;
	static final String TAG = WAPS_AppDetailActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appnameView = (TextView) findViewById(R.id.waps_app_detail_txtAppname);
		versionView = (TextView) findViewById(R.id.waps_app_detail_txtVersion);
		filesizeView = (TextView) findViewById(R.id.waps_app_detail_txtFilesize);
		providerView = (TextView) findViewById(R.id.waps_app_detail_txtProvider);
		appTextView = (TextView) findViewById(R.id.waps_app_detail_txtAppText);
		appDesView = (TextView) findViewById(R.id.waps_app_detail_txtAppDes);
		iconView = (ImageView) findViewById(R.id.waps_app_detail_icon);
		img1 = (WrapperImageView) findViewById(R.id.waps_app_detail_img1);
		img2 = (WrapperImageView) findViewById(R.id.waps_app_detail_img2);
		miniAdview = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
		btnInstall = (Button) findViewById(R.id.waps_app_detail_btnInstall);
		btnDownload = (Button) findViewById(R.id.waps_app_detail_btnDownload);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		btnInstall.setOnClickListener(this);
		btnDownload.setOnClickListener(this);
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		InitButton();
		img1.setWidth(240);
		img1.setHeight(400);
		img2.setWidth(240);
		img2.setHeight(400);
		initAD();
		BindDetail();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.waps_app_detail;
	}
	private void InitButton(){
		hasMore = getIntent().getBooleanExtra(AppConstant.INTENT_WAPS_AD_DETAIL_HASMORE, false);
		if(hasMore){
			setTitleBtn1("更多推荐", new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), WAPS_AppWallActivity.class);
					startActivity(intent);
				}
			});
		}
	}
	private void initAD(){
		//设置迷你广告背景颜色
		AppConnect.getInstance(this).setAdBackColor(getResources().getColor(R.color.app_panel_bg)); 
		//设置迷你广告广告语颜色
		AppConnect.getInstance(this).setAdForeColor(Color.WHITE); 

		new MiniAdView(this, miniAdview).DisplayAd(10);
	}
	public void BindDetail(){
		if(getIntent().getParcelableExtra(AppConstant.INTENT_ADINFO) != null){
			adInfo = getIntent().getParcelableExtra(AppConstant.INTENT_ADINFO);
			Log.d(TAG, "adinfo:"+adInfo.toString());
			appnameView.setText(adInfo.adName);
			versionView.setText("版本:"+adInfo.version);
			filesizeView.setText("大小:"+adInfo.filesize+"M");
			providerView.setText(adInfo.provider+"官方出品");
			appTextView.setText(adInfo.adText);
			appDesView.setText(adInfo.description);
			iconView.setImageBitmap(adInfo.adIcon);
			if(adInfo.imageUrls != null && adInfo.imageUrls.length > 1){
				img1.setUrl(adInfo.imageUrls[0]);
				img2.setUrl(adInfo.imageUrls[1]);
			}
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
		AppConnect.getInstance(this).downloadAd(adInfo.adId);
	}
}
