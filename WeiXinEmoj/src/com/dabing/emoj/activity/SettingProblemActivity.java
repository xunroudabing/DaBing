package com.dabing.emoj.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
/**
 * 常见问题
 * @author DaBing
 *
 */
public class SettingProblemActivity extends BaseActivity {
	
	static final String TAG = SettingProblemActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle("常见问题");
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.setting_problem;
	}

}
