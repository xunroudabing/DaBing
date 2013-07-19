package com.dabing.emoj.activity;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConstant;
import com.tencent.appwallsdk.QQAppWallSDK;
import com.tencent.appwallsdk.view.QQAppWallView;

import android.app.Activity;
import android.os.Bundle;
/**
 * 应用墙
 * @author DaBing
 *
 */
public class AppWallActivity extends Activity {
	static final String TAG = AppWallActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		QQAppWallSDK.init(AppConstant.APP_WALL_ID, this);
		setContentView(R.layout.app_wall);
		QQAppWallView wallView = (QQAppWallView) findViewById(R.id.qq_app_wall_view);		
		wallView.openAppWall();
	}
	@Override
	protected void onDestroy() {
		//释放占用的内存资源
		QQAppWallSDK.destory();
		super.onDestroy();
	}
}
