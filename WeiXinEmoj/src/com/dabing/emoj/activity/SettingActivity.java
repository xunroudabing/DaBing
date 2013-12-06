package com.dabing.emoj.activity;

import greendroid.image.ImageProcessor;
import greendroid.image.ScaleImageProcessor;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.advertise.AdManager;
import com.dabing.emoj.advertise.AdManager.AdType;
import com.dabing.emoj.advertise.WAPS_AppWallActivity;
import com.dabing.emoj.db.PushEmojDatabaseHelper;
import com.dabing.emoj.push.ReceivedEmojListActivity;
import com.dabing.emoj.qqconnect.BaseApiListener;
import com.dabing.emoj.qqconnect.QQConnect;
import com.dabing.emoj.service.LoginSuccessBroadcast;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.RegularEmojManager;
import com.dabing.emoj.utils.TokenStore;
import com.dabing.emoj.widget.EmojImageView;
import com.dabing.emoj.widget.PromptDialog;
import com.tencent.mm.sdk.uikit.MMImageButton;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;
import com.umeng.fb.UMFeedbackService;

/**
 * 设置
 * 
 * @author DaBing
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	MyHandler mHandler = new MyHandler();
	String action = "send";
	Button btnClear;
	EmojImageView headView;
	RelativeLayout downloadView, attentionView, removeadView, pushemojView;
	ImageView attensionIcon, removeadIcon, pushemojIcon;
	LinearLayout loginView, userinfoView, versionView, yijianView, aboutView,
			appwallView, helpView;
	LinearLayout pingjiaView, problemView;
	static final int REQUEST_LOGIN = 1;
	static final String TAG = SettingActivity.class.getSimpleName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle("设置");
		loginView = (LinearLayout) findViewById(R.id.setting_userLogin);
		userinfoView = (LinearLayout) findViewById(R.id.setting_userInfo);
		versionView = (LinearLayout) findViewById(R.id.setting_version);
		yijianView = (LinearLayout) findViewById(R.id.setting_yijian);
		aboutView = (LinearLayout) findViewById(R.id.setting_about);
		appwallView = (LinearLayout) findViewById(R.id.setting_appwall);
		helpView = (LinearLayout) findViewById(R.id.setting_help);
		pingjiaView = (LinearLayout) findViewById(R.id.setting_pingjia);
		problemView = (LinearLayout) findViewById(R.id.setting_problem);
		btnClear = (Button) findViewById(R.id.setting_btnclear);
		headView = (EmojImageView) findViewById(R.id.setting_userinfo_head);
		downloadView = (RelativeLayout) findViewById(R.id.setting_download);
		attentionView = (RelativeLayout) findViewById(R.id.setting_attention);
		removeadView = (RelativeLayout) findViewById(R.id.setting_removead);
		pushemojView = (RelativeLayout) findViewById(R.id.setting_pushemoj);
		attensionIcon = (ImageView) findViewById(R.id.setting_attention_icon);
		removeadIcon = (ImageView) findViewById(R.id.setting_removead_icon);
		pushemojIcon = (ImageView) findViewById(R.id.setting_pushemoj_icon);
		loginView.setOnClickListener(this);
		userinfoView.setOnClickListener(this);
		versionView.setOnClickListener(this);
		yijianView.setOnClickListener(this);
		aboutView.setOnClickListener(this);
		appwallView.setOnClickListener(this);
		helpView.setOnClickListener(this);
		btnClear.setOnClickListener(this);
		downloadView.setOnClickListener(this);
		pingjiaView.setOnClickListener(this);
		problemView.setOnClickListener(this);
		attentionView.setOnClickListener(this);
		removeadView.setOnClickListener(this);
		pushemojView.setOnClickListener(this);
		Initialize();
		SetupAction();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isNew();
		Initialize();
	};

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.setting;
	}

	private void isNew() {
		// if (AppConfig.getIsNew(getApplicationContext(), "unread_download")) {
		// downloadIcon.setVisibility(View.VISIBLE);
		// }else {
		// downloadIcon.setVisibility(View.INVISIBLE);
		// }
		// if(AppConfig.getIsNew(getApplicationContext(), "unread_public")){
		// attensionIcon.setVisibility(View.VISIBLE);
		// }else {
		// attensionIcon.setVisibility(View.INVISIBLE);
		// }
		if (AppConfig.getIsNew(getApplicationContext(), "unread_removead")) {
			removeadIcon.setVisibility(View.VISIBLE);
		} else {
			removeadIcon.setVisibility(View.INVISIBLE);
		}

		if (isPushEmojNew()) {
			pushemojIcon.setVisibility(View.VISIBLE);
		} else {
			pushemojIcon.setVisibility(View.INVISIBLE);
		}
	}

	// 是否有未读的推送表情
	protected boolean isPushEmojNew() {
		try {
			PushEmojDatabaseHelper helper = new PushEmojDatabaseHelper(
					getApplicationContext());
			boolean ret = helper.isNew();
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return false;
	}

	public void Initialize() {
		// OAuth oAuth = TokenStore.fetchPrivate(getApplicationContext());
		Tencent mTencent = QQConnect.createInstance(getApplicationContext())
				.getTencent();
		boolean valid = mTencent.isSessionValid();
		if (!valid) {
			// 未登录
			loginView.setVisibility(View.VISIBLE);
			userinfoView.setVisibility(View.GONE);
		} else {
			loginView.setVisibility(View.GONE);
			userinfoView.setVisibility(View.VISIBLE);
			BindUserInfo();
		}

		// 根据积分隐藏开关显示去除广告
		if (AppConfig.getBonusHide(getApplicationContext())) {
			removeadView.setVisibility(View.GONE);
		} else {
			removeadView.setVisibility(View.VISIBLE);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 登录
		case R.id.setting_userLogin:
			// Intent intent1 = new Intent(getApplicationContext(),
			// UserLoginActivity.class);
			// startActivityForResult(intent1, REQUEST_LOGIN);
			QQLogin();
			break;
		// 意见
		case R.id.setting_yijian:
			UMFeedbackService.setGoBackButtonVisible();
			UMFeedbackService.openUmengFeedbackSDK(SettingActivity.this);
			break;
		// 版本升级
		case R.id.setting_version:
			Intent intent2 = new Intent(getApplicationContext(),
					SettingVersionActivity.class);
			startActivity(intent2);
			break;
		// 关于
		case R.id.setting_about:
			Intent intent3 = new Intent(getApplicationContext(),
					SettingAboutActivity.class);
			startActivity(intent3);
			break;
		// 个人信息
		case R.id.setting_userInfo:
			Intent intent4 = new Intent(getApplicationContext(),
					SettingUserInfoActivity.class);
			startActivity(intent4);
			break;
		// 使用说明
		case R.id.setting_help:
			PromptDialog dialog = new PromptDialog(SettingActivity.this);
			dialog.show();
			break;
		// 清除历史记录
		case R.id.setting_btnclear:
			RegularEmojManager manager = new RegularEmojManager(
					getApplicationContext());
			manager.clear();
			Toast.makeText(SettingActivity.this, "清除历史记录成功", Toast.LENGTH_SHORT)
					.show();
			break;
		// 应用墙
		case R.id.setting_appwall:
			AdManager ad = new AdManager(SettingActivity.this);
			AdType adType = ad.getAdType();
			switch (adType) {
			case QQ:
				Intent intent5_q = new Intent(getApplicationContext(),
						AppWallActivity.class);
				startActivity(intent5_q);
				break;
			case WAPS:
				Intent intent5 = new Intent(getApplicationContext(),
						WAPS_AppWallActivity.class);
				startActivity(intent5);
				break;
			default:
				Intent intent5_d = new Intent(getApplicationContext(),
						AppWallActivity.class);
				startActivity(intent5_d);
				break;
			}

			break;
		// 打包下载
		case R.id.setting_download:
			AppConfig.setIsNew(getApplicationContext(), "unread_download");
			Intent intent6 = new Intent(getApplicationContext(),
					DownloadGridViewActivity.class);
			startActivity(intent6);
			break;
		// 常见问题
		case R.id.setting_problem:
			Intent intent7 = new Intent(getApplicationContext(),
					SettingProblemActivity.class);
			startActivity(intent7);
			break;
		// 评价
		case R.id.setting_pingjia:
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id="
						+ getPackageName()));
				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			break;
		// 关注微信账号
		case R.id.setting_attention:
			try {
				AppConfig.setIsNew(getApplicationContext(), "unread_public");
				Intent localIntent = new Intent("android.intent.action.VIEW",
						Uri.parse(AppConfig
								.getPublicAccount(getApplicationContext())));
				localIntent.setClassName("com.tencent.mm",
						"com.tencent.mm.ui.qrcode.GetQRCodeInfoUI");
				startActivity(localIntent);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			break;
		// 清除广告
		case R.id.setting_removead:
			try {
				AppConfig.setIsNew(getApplicationContext(), "unread_removead");
				Intent intent_removead = new Intent(getApplicationContext(),
						BonusGainActivity.class);
				intent_removead.putExtra(AppConstant.INTENT_TITLE,
						getString(R.string.title_setting_removead));
				startActivity(intent_removead);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			break;
		// 表情更新
		case R.id.setting_pushemoj:
			Intent intent8 = new Intent(getApplicationContext(),
					ReceivedEmojListActivity.class);
			startActivity(intent8);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// QQConnect.createInstance(getApplicationContext()).getTencent().onActivityResult(requestCode,
		// resultCode, data);
		// if(requestCode == REQUEST_LOGIN){
		// if(resultCode == RESULT_OK){
		// //登录成功
		// loginView.setVisibility(View.GONE);
		// userinfoView.setVisibility(View.VISIBLE);
		// BindUserInfo();
		// Toast.makeText(SettingActivity.this, "登录成功",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
	}

	public void SetupAction() {
		Intent data = getIntent();
		if (data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null) {
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);
		}
		Log.d(TAG, "action:" + action);
		if (action.equals("get")) {
			MMImageButton button = setTitleBtn4("微信", new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			button.setBackgroundDrawable(getDrawable(R.drawable.mm_title_btn_back));

		} else if (action.equals("pick")) {
			MMImageButton button = setTitleBtn4("返回", new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			button.setBackgroundDrawable(getDrawable(R.drawable.mm_title_btn_back));
		} else {

		}
	}

	public void BindUserInfo() {
		try {
			String json = AppConfig.getUserInfo(getApplicationContext());
			if (json == null) {
				// GDUtils.getExecutor(getApplicationContext()).execute(new
				// UserInfoTask());
				getUserInfo();
			} else {
				// Log.d(TAG, "111");
				mHandler.sendMessage(Message.obtain(mHandler, 1, json));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	// 废弃
	class UserInfoTask implements Runnable {
		OAuth oAuth;

		public UserInfoTask() {
			oAuth = TokenStore.fetchPrivate(getApplicationContext());
		}

		public void run() {
			// TODO Auto-generated method stub
			UserAPI api = new UserAPI(OAuthConstants.OAUTH_VERSION_2_A);
			try {
				String response = api.info(oAuth, "json");
				Log.d(TAG, "response:" + response);
				mHandler.sendMessage(Message.obtain(mHandler, 1, response));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			} finally {
				api.shutdownConnection();
			}
		}

	}

	class MyHandler extends Handler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			/**
			 * get_user_info:{"is_yellow_year_vip":"0","ret":0,"figureurl_qq_1":
			 * "http:\/\/q.qlogo.cn\/qqapp\/100399626\/09D80E49029BAC4F35902730E1DADDAC\/40","figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/100399626\/09D80E49029BAC4F35902730E1DADDAC\/100","nickname":"㊣熏肉大饼","yellow_vip_level":"0","is_lost":0,"msg":"","figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/100399626\/09D80E49029BAC4F35902730E1DADDAC\/50","vip":"0","level":"0","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/100399626\/09D80E49029BAC4F35902730E1DADDAC\/100","is_yellow_vip":"0","gender":"男","figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/100399626\/09D80E49029BAC4F35902730E1DADDAC\/30"
			 * }
			 */
			case 1:
				String json = msg.obj.toString();
				try {
					// JSONObject object = new JSONObject(json);
					// JSONObject data = object.getJSONObject("data");
					// String head = data.getString("head");
					// String nick = data.getString("nick");
					// String s = data.getString("sex");
					// String sex = "";
					// if(s.equals("1")){
					// sex = "男";
					// }else if (s.equals("2")) {
					// sex = "女";
					// }else {
					// sex = "未填写";
					// }
					// String location = data.getString("location");

					JSONObject object = new JSONObject(json);
					String url = object.getString("figureurl_qq_1");
					Drawable d = getDrawable(R.drawable.wb_head_default50x50);
					int width = d.getIntrinsicWidth();
					int height = d.getIntrinsicHeight();
					ImageProcessor processor = new ScaleImageProcessor(width,
							height, ScaleType.CENTER_CROP);
					headView.setImageProcessor(processor);
					headView.setUrl(url);
					// 缓存
					AppConfig.setUserInfo(getApplicationContext(), json);
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

	// *******QQ互联登陆*******
	protected void QQLogin() {
		Tencent mTencent = QQConnect.createInstance(getApplicationContext())
				.getTencent();
		if (mTencent != null) {
			mTencent.login(SettingActivity.this, "all", new IUiListener() {

				@Override
				public void onError(UiError uierror) {
					// TODO Auto-generated method stub
					Log.d(TAG, "onError:" + uierror.toString());

				}

				/**
				 * {"ret":0,"pay_token":"72E353919499F0A20997F85DCCDF1A7E","pf":
				 * "desktop_m_qq-10000144-android-2002-"
				 * ,"sendinstall":"0","expires_in"
				 * :"7776000","openid":"09D80E49029BAC4F35902730E1DADDAC"
				 * ,"pfkey":"D04C759A1FB3A1E3DBEA19F4294AD5D7","msg":"sucess",
				 * "access_token"
				 * :"4F4419D1B382887B05CAF3D2AEDE91F7","installwording":""}
				 */
				@Override
				public void onComplete(JSONObject jsonobject) {
					// TODO Auto-generated method stub
					Log.d(TAG, "onComplete:" + jsonobject.toString());
					try {
						int ret = jsonobject.getInt("ret");
						Log.d(TAG, "ret:" + ret);
						if (ret == 0) {
							String openId = jsonobject.getString("openid");
							String access_token = jsonobject
									.getString("access_token");
							long expires_in = jsonobject.getLong("expires_in");
							long expires = System.currentTimeMillis()
									+ expires_in * 1000;
							Date date = new Date(expires);
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Log.d(TAG, "valid date:" + format.format(date));

							AppConfig.setQQ_OpenId(getApplicationContext(),
									openId);
							AppConfig.setQQ_AccessToken(
									getApplicationContext(), access_token);
							AppConfig.setQQ_ExpiresIn(getApplicationContext(),
									expires);

							// //登录成功
							QQConnect.createInstance(getApplicationContext())
									.Init();
							loginView.setVisibility(View.GONE);
							userinfoView.setVisibility(View.VISIBLE);
							BindUserInfo();
							Toast.makeText(SettingActivity.this, "登录成功",
									Toast.LENGTH_SHORT).show();

							// 发送登陆成功的广播
							Intent intent = new Intent(getApplicationContext(),
									LoginSuccessBroadcast.class);
							sendBroadcast(intent);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.e(TAG, e.toString());
					}
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					Log.d(TAG, "onCancel");
				}
			});
		}
	}

	protected void getUserInfo() {
		Tencent mTencent = QQConnect.createInstance(getApplicationContext())
				.getTencent();
		// mTencent.requestAsync(Constants.GRAPH_USER_INFO, null,
		// Constants.HTTP_GET, apiListener, null);
		mTencent.requestAsync(Constants.GRAPH_SIMPLE_USER_INFO, null,
				Constants.HTTP_GET, apiListener, null);
	}

	protected BaseApiListener apiListener = new BaseApiListener(
			"get_user_info", true) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.dabing.emoj.qqconnect.BaseApiListener#doComplete(org.json.JSONObject
		 * , java.lang.Object)
		 */
		@Override
		protected void doComplete(JSONObject response, Object state) {
			// TODO Auto-generated method stub
			super.doComplete(response, state);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.dabing.emoj.qqconnect.BaseApiListener#onComplete(org.json.JSONObject
		 * , java.lang.Object)
		 */
		@Override
		public void onComplete(JSONObject jsonobject, Object obj) {
			// TODO Auto-generated method stub
			super.onComplete(jsonobject, obj);
			Log.d(TAG, "get_user_info:" + jsonobject);
			try {
				int ret = jsonobject.getInt("ret");
				if (ret == 0) {
					mHandler.sendMessage(Message.obtain(mHandler, 1,
							jsonobject.toString()));
					AppConfig.setUserInfo(getApplicationContext(),
							jsonobject.toString());
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.dabing.emoj.qqconnect.BaseApiListener#onNetworkUnavailableException
		 * (com.tencent.open.NetworkUnavailableException, java.lang.Object)
		 */
		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException networkunavailableexception,
				Object obj) {
			// TODO Auto-generated method stub
			super.onNetworkUnavailableException(networkunavailableexception,
					obj);
		}

	};
}
