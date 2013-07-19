package com.dabing.emoj.advertise;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import cn.waps.AdInfo;
import cn.waps.AppConnect;
import cn.waps.MiniAdView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.adpater.WapsAppWallListViewAdapter;
import com.dabing.emoj.utils.AppConstant;
import com.tencent.mm.algorithm.MD5;

public class WAPS_AppWallActivity extends BaseActivity implements OnItemClickListener{
	LinearLayout miniAdView;
	WapsAppWallListViewAdapter adapter;
	ListView listView;
	static final String TAG = WAPS_AppWallActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle("猜你喜欢");
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setTitleBtn1("更多", new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppConnect.getInstance(WAPS_AppWallActivity.this).showOffers(WAPS_AppWallActivity.this);
			}
		});
		miniAdView = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
		listView = (ListView) findViewById(R.id.waps_appwall_listview);
		listView.setOnItemClickListener(this);
		InitMiniAd();
		BindAppWall();
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.waps_appwall;
	}
	public void InitMiniAd(){
		//设置迷你广告背景颜色
		AppConnect.getInstance(this).setAdBackColor(getResources().getColor(R.color.app_panel_bg)); 
		//设置迷你广告广告语颜色
		AppConnect.getInstance(this).setAdForeColor(Color.WHITE); 

		new MiniAdView(this, miniAdView).DisplayAd(10);
	}
	public void BindAppWall(){
		try {
			List<AdInfo> adInfoList = AppConnect.getInstance(this).getAdInfoList();
			if(adInfoList != null && adInfoList.size() > 0){
				adapter = new WapsAppWallListViewAdapter(WAPS_AppWallActivity.this, adInfoList);
				listView.setAdapter(adapter);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			AdInfo info = adapter.getAdInfo(position);
			//AppConnect.getInstance(this).clickAd(info.getAdId());
			if(info == null){
				return;
			}
			WAPS_ADInfo adinfo = WAPS_ADInfo.Convert(info);
			Log.d(TAG, "adinfo:"+adinfo.toString());
			Intent intent = new Intent(getContext(), WAPS_AppDetailActivity.class);
			intent.putExtra(AppConstant.INTENT_ADINFO, adinfo);
			intent.putExtra(AppConstant.INTENT_TITLE, adinfo.adName);
			getContext().startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
}
