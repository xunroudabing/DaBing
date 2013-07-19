package com.dabing.emoj.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;

public class SettingAboutActivity extends BaseActivity {
	TextView txtView;
	Button btn;
	static final String TAG = SettingAboutActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		txtView=(TextView) findViewById(R.id.setting_about_notice);
		btn = (Button) findViewById(R.id.setting_about_btnscore);
		btn.setOnClickListener(clickListener);
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Bind();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.setting_about;
	}
	//打分
	private OnClickListener clickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + getPackageName()));
				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}

		}
	};
	private void Bind(){
		txtView.setText(AppConfig.getNotice(getApplicationContext()));
	}
}
