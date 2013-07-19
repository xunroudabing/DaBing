package com.dabing.emoj.activity;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class SettingVersionActivity extends BaseActivity {
	TextView versionView;
	LinearLayout updateView;
	static final String TAG = SettingVersionActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle("版本升级");
		versionView = (TextView) findViewById(R.id.setting_version_txtVer);
		updateView = (LinearLayout) findViewById(R.id.setting_version_update);
		updateView.setOnClickListener(clickListener);
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
		return R.layout.setting_version;
	}
	
	private void Bind(){
		try {
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			versionView.setText(versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	
	UmengUpdateListener updateListener = new UmengUpdateListener() {
		public void onUpdateReturned(int updateStatus,
				UpdateResponse updateInfo) {
			switch (updateStatus) {
			case 0: // has update
				Log.i("--->", "callback result");
				UmengUpdateAgent.showUpdateDialog(SettingVersionActivity.this, updateInfo);
				break;
			case 1: // has no update
				Toast.makeText(SettingVersionActivity.this, "已经是最新版本", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2: // none wifi
				Toast.makeText(SettingVersionActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3: // time out
				Toast.makeText(SettingVersionActivity.this, "网络超时", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};
	
	private OnClickListener clickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			com.umeng.common.Log.LOG = true;
			UmengUpdateAgent.setUpdateOnlyWifi(false); // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(updateListener);
			
			UmengUpdateAgent.setOnDownloadListener(new UmengDownloadListener(){

				public void OnDownloadEnd(int result) {
					Log.i(TAG, "download result : " + result);
				}
				
			});
			
			UmengUpdateAgent.update(SettingVersionActivity.this);
		}
	};

}
