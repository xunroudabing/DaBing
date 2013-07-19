package com.dabing.emoj;

import android.content.Intent;
import android.os.Bundle;

import com.dabing.emoj.utils.AppConstant;
import com.tencent.mm.sdk.uikit.MMBaseActivity;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends MMBaseActivity {


	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	};
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	};
	protected void init(){
		Intent intent = getIntent();
		if(intent.getStringExtra(AppConstant.INTENT_TITLE) != null){
			String title = intent.getStringExtra(AppConstant.INTENT_TITLE);
			setMMTitle(title);
		}
	}
}
