package com.dabing.emoj.activity;

import greendroid.image.ImageProcessor;
import greendroid.image.ScaleImageProcessor;
import greendroid.util.GDUtils;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.qqconnect.BaseApiListener;
import com.dabing.emoj.qqconnect.QQConnect;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.TokenStore;
import com.dabing.emoj.widget.EmojImageView;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.Tencent;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;
/**
 * 个人信息
 * @author DaBing
 *
 */
public class SettingUserInfoActivity extends BaseActivity {
	Tencent mTencent;
	TextView nickView,sexView,locationView;
	EmojImageView headView;
	Button btnlogout;
	MyHandler mHandler = new MyHandler();
	static final int REQUEST_LOGIN = 1;
	static final String TAG = SettingUserInfoActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		nickView = (TextView) findViewById(R.id.setting_userinfo_nick);
		sexView = (TextView) findViewById(R.id.setting_userinfo_sex);
		locationView = (TextView) findViewById(R.id.setting_userinfo_location);
		headView = (EmojImageView) findViewById(R.id.setting_userinfo_head);
		btnlogout = (Button) findViewById(R.id.setting_userinfo_logout);
		btnlogout.setOnClickListener(logoutListener);
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mTencent = QQConnect.createInstance(getApplicationContext()).getTencent();
		Bind();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.setting_userinfo;
	}
	private void Bind(){
		try {
			String json = AppConfig.getUserInfo(getApplicationContext());
			if(json == null){
				getUserInfoV2();
			}else {
				//Log.d(TAG, "111");
				mHandler.sendMessage(Message.obtain(mHandler, 1, json));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	//退出登录
	private OnClickListener logoutListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			Intent intent1 = new Intent(getApplicationContext(), UserLoginActivity.class);
//			startActivityForResult(intent1, REQUEST_LOGIN);
			QQConnect.createInstance(getApplicationContext()).Logout();
			finish();
		}
	};
	private void getUserInfo(){
		GDUtils.getExecutor(getApplicationContext()).execute(new UserInfoTask());
	}
	//废弃
	class UserInfoTask implements Runnable{
		OAuth oAuth;
		public UserInfoTask(){
			oAuth = TokenStore.fetchPrivate(getApplicationContext());
		}
		public void run() {
			// TODO Auto-generated method stub
			UserAPI api = new UserAPI(OAuthConstants.OAUTH_VERSION_2_A);
			try {
				String response = api.info(oAuth, "json");
				Log.d(TAG, "response:"+response);
				mHandler.sendMessage(Message.obtain(mHandler, 1, response));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}finally{
				api.shutdownConnection();
			}
		}
		
	}
	
	class MyHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				String json = msg.obj.toString();
				try {
//					JSONObject object = new JSONObject(json);
//					JSONObject data = object.getJSONObject("data");
//					String head = data.getString("head");
//					String nick = data.getString("nick");
//					String s = data.getString("sex");
//					String sex = "";
//					if(s.equals("1")){
//						sex = "男";
//					}else if (s.equals("2")) {
//						sex = "女";
//					}else {
//						sex = "未填写";
//					}
//					String location = data.getString("location");
					JSONObject object = new JSONObject(json);
					String nick = object.getString("nickname");
					String sex = object.getString("gender");
					String url = object.getString("figureurl_qq_1");
					nickView.setText(nick);
					sexView.setText(sex);
					//locationView.setText(location);
					Drawable d = getDrawable(R.drawable.wb_head_default50x50);
					int width = d.getIntrinsicWidth();
					int height = d.getIntrinsicHeight();
					ImageProcessor processor = new ScaleImageProcessor(width, height, ScaleType.CENTER_CROP);
					headView.setImageProcessor(processor);
					headView.setUrl(url);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == REQUEST_LOGIN){
			if(resultCode == RESULT_OK){
				getUserInfo();
			}
		}
	}
	
	protected void getUserInfoV2(){
		Tencent mTencent = QQConnect.createInstance(getApplicationContext()).getTencent();
		mTencent.requestAsync(Constants.GRAPH_USER_INFO, null, Constants.HTTP_GET, apiListener, null);
	}
	
	protected BaseApiListener apiListener = new BaseApiListener("get_user_info", true){

		/* (non-Javadoc)
		 * @see com.dabing.emoj.qqconnect.BaseApiListener#doComplete(org.json.JSONObject, java.lang.Object)
		 */
		@Override
		protected void doComplete(JSONObject response, Object state) {
			// TODO Auto-generated method stub
			super.doComplete(response, state);
		}

		/* (non-Javadoc)
		 * @see com.dabing.emoj.qqconnect.BaseApiListener#onComplete(org.json.JSONObject, java.lang.Object)
		 */
		@Override
		public void onComplete(JSONObject jsonobject, Object obj) {
			// TODO Auto-generated method stub
			super.onComplete(jsonobject, obj);
			Log.d(TAG, "get_user_info:"+jsonobject);
			try {
				int ret = jsonobject.getInt("ret");
				if(ret == 0){
					mHandler.sendMessage(Message.obtain(mHandler, 1, jsonobject.toString()));
					AppConfig.setUserInfo(getApplicationContext(), jsonobject.toString());
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}

		/* (non-Javadoc)
		 * @see com.dabing.emoj.qqconnect.BaseApiListener#onNetworkUnavailableException(com.tencent.open.NetworkUnavailableException, java.lang.Object)
		 */
		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException networkunavailableexception,
				Object obj) {
			// TODO Auto-generated method stub
			super.onNetworkUnavailableException(networkunavailableexception, obj);
		}
		
	};

}
