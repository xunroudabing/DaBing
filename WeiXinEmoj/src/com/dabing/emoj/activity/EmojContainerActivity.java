package com.dabing.emoj.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dabing.emoj.R;
import com.dabing.emoj.fragment.BaseEmojFragment;
import com.dabing.emoj.fragment.CommonEmojFragment;
import com.dabing.emoj.fragment.CrystalHeaderFragment;
import com.dabing.emoj.fragment.HotEmojFragment;
import com.dabing.emoj.fragment.RegularEmojFragment;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;
import com.dabing.emoj.utils.AppConstant;
import com.umeng.analytics.MobclickAgent;
/**
 * 第一页
 * @author DaBing
 *
 */
public class EmojContainerActivity extends FragmentActivity implements IEmojItemClickListener {
	String action = "send";
	TextView btnBack;
	BaseEmojFragment currentFragment;
	CrystalHeaderFragment headerFragment;
	static final String TAG = EmojContainerActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emoj_index1);
		btnBack = (TextView) findViewById(R.id.umeng_fb_goback_btn);
		btnBack.setOnClickListener(backListener);
		headerFragment = (CrystalHeaderFragment) findViewById(R.id.emoj_index1_header);
		headerFragment.setOnItemClickListener(this);
		headerFragment.show();
		SetupAction();
		InitFragment();
		MobclickAgent.onError(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(EmojContainerActivity.this);
	};
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(EmojContainerActivity.this);
		
	};
	public void SetupAction(){
		Intent data = getIntent();
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);	
		}
		Log.d(TAG, "action:"+action);
		if(action.equals("get")){
			btnBack.setVisibility(View.VISIBLE);
		}
		else if (action.equals("pick")) {
			btnBack.setText("返回");
			btnBack.setVisibility(View.VISIBLE);			
		}
		else {
			btnBack.setVisibility(View.GONE);
		}
	}
	private OnClickListener backListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	public void onItemClick(View view,JSONObject item) {
		// TODO Auto-generated method stub
		if(item != null){
			Log.d(TAG, "item:"+item.toString());
			try {
				String id = item.getString("id");
				setFragment(id, item);
				UmengEvent(id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
			
		}
	}
	private void InitFragment(){
		try {
			JSONObject item = new JSONObject(AppConstant.MENU_REGULAR);
			setFragment("000", item);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
	//替换fragment
	private void setFragment(String id,JSONObject item){
		try {
			FragmentManager fm = getSupportFragmentManager();
			if(fm.findFragmentByTag(id) == null){
				FragmentTransaction trans = fm.beginTransaction();
				BaseEmojFragment fragment = createFragment(item);
				currentFragment = fragment;
				trans.replace(R.id.emoj_index1_container, fragment,id);
				trans.commit();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	private BaseEmojFragment createFragment(JSONObject item) throws JSONException{
		int type = item.getInt("c");
		BaseEmojFragment fragment = null;
		//0-常用 1-普通表情分类 2-热门表情
		switch (type) {
		case 0:
			fragment = RegularEmojFragment.getInstance(item);
			break;
		case 1:
			fragment = CommonEmojFragment.getInstance(item);
			break;
		case 2:
			fragment = HotEmojFragment.getInstance(item);
			break;
		default:
			fragment = CommonEmojFragment.getInstance(item);
			break;
		}
		return fragment;
	}
	
	private void UmengEvent(String eventid){
		MobclickAgent.onEvent(EmojContainerActivity.this, eventid);
	}
}
