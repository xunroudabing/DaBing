package com.dabing.emoj.demo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.activity.EmojViewActivity;
import com.dabing.emoj.jni.JniTest;
import com.dabing.emoj.jni.JniUtils;
import com.dabing.emoj.qqconnect.QQConnect;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.TokenStore;
import com.dabing.emoj.widget.QuickActionGrid;
import com.dabing.emoj.widget.QuickActionList;
import com.dabing.emoj.wxapi.WeiXinHelper;
import com.tencent.mm.sdk.channel.MMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;

public class democall extends BaseActivity implements OnClickListener {
	
	Tencent mTencent;
	QuickActionGrid mlist;
	IWXAPI api;
	OAuth oAuth;
	Button btn1, btn2, btn3, btn4, btn5, btn6, btn7,btn8;
	static final String TAG = democall.class.getSimpleName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		btn5 = (Button) findViewById(R.id.btn5);
		btn6 = (Button) findViewById(R.id.btn6);
		btn7 = (Button) findViewById(R.id.btn7);
		btn8 = (Button) findViewById(R.id.btn8);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		oAuth = TokenStore.fetch(getApplicationContext());
		api = WXAPIFactory.createWXAPI(democall.this, AppConstant.WEIXIN_APPID);
		boolean b = api.isWXAppInstalled();
		prepare();
		QQConnect.createInstance(getApplicationContext()).Init();
		mTencent = QQConnect.createInstance(getApplicationContext()).getTencent();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.demo_call;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn1:
			getHtId();
			break;
		case R.id.btn2:
			break;
		case R.id.btn3:
			sendemoj();
			break;
		case R.id.btn4:
			openWX();
			break;
		case R.id.btn5:
			mlist.show(v);
			break;
		case R.id.btn6:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + getPackageName()));
			startActivity(intent);
			break;
		case R.id.btn7:
			//getChannel();
			getImages();
			break;
		case R.id.btn8:		
			//shareQQ();
			//invite();
			getThumb("/storage/sdcard0/DCIM/Camera/");
			break;
		default:
			break;
		}
	}

	private void getHtId() {
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				DaBingRequest request = new DaBingRequest(
						OAuthConstants.OAUTH_VERSION_2_A);
				try {
					String response = request.getHt_ids(oAuth, "wxemoj1");
					Log.d(TAG, "response:" + response);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
			}
		}).start();
	}

	private void sendemoj() {
		WeiXinHelper helper = new WeiXinHelper(democall.this, api);
		String path = AppConfig.getEmoj() + "db.gif";
		Log.d(TAG, "path:" + path);
		helper.sendEmoj(path);
		// helper.sendIMGPath(path);
		finish();
	}

	private void openWX() {
		// api.openWXApp();
		MMessage.send(democall.this, "com.tencent.mm.permission.MM_MESSAGE",
				"com.tencent.mm.sdk.channel.Intent.ACTION_MESSAGE",
				"hello dabing", getIntent().getExtras());
	}

	private void prepare() {
		mlist = new QuickActionGrid(getApplicationContext());
		try {
			String json = AppConfig.getCategory(getApplicationContext());
			JSONArray array = new JSONArray(json);
			mlist.setData(array);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

	private void testJni() {
		int i = 0;
		// i |= 8;
		String path = AppConfig.getThumb() + "test.gif";
		String str = JniTest.ScanPictures(path, i);
		Log.d(TAG, "jni:" + str);
	}

	private void getChannel() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DaBingRequest request = new DaBingRequest(
						OAuthConstants.OAUTH_VERSION_2_A);
				try {
					String id = "903";
					String responseString = request.getChannelList(oAuth, "0",
							"0", "0", "", id, "2", "1", "4");
					Log.d(TAG, "微频道:" + responseString);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
			}
		}).start();

	}

	private void getImages() {
		Uri uri = Media.getContentUri("external");
		String order = MediaColumns.DATE_MODIFIED + " desc";
		String[] colums = { MediaColumns._ID, MediaColumns.TITLE,
				MediaColumns.DISPLAY_NAME, MediaColumns.DATE_MODIFIED,
				String.format("REPLACE(%s,%s,'')", MediaColumns.DATA,MediaColumns.DISPLAY_NAME) };
		Cursor cursor = getContentResolver().query(uri, colums, null, null,
				order);

		if(cursor == null){
			return;
		}
		cursor.moveToFirst();
		do {
			long id = cursor.getLong(cursor
					.getColumnIndexOrThrow(MediaColumns._ID));
			String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.TITLE));
			String display = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));
			String date = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATE_MODIFIED));
			String data = cursor.getString(4);
			Log.d(TAG, String.format("id:%d title:%s display:%s date:%s data:%s", id,title,display,date,data));
		} while (cursor.moveToNext());
	}
	
	private void QQlogin(){
		mTencent.login(democall.this, AppConfig.QQ_SCOPE, new IUiListener() {
			
			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onError:"+arg0.toString());
			}
			
			@Override
			public void onComplete(JSONObject arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onComplete:"+arg0.toString());
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Log.d(TAG, "onCancel()");
			}
		});
	}
	private void invite(){
		 Bundle params = new Bundle();
         params.putString(Constants.PARAM_APP_ICON,
                 "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
         params.putString(Constants.PARAM_APP_DESC,
                 "AndroidSdk_1_3: invite description!");
         params.putString(Constants.PARAM_APP_CUSTOM,
                 "AndroidSdk_1_3: invite message!");
         mTencent.invite(democall.this, params, new IUiListener() {
			
			@Override
			public void onError(UiError uierror) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(JSONObject jsonobject) {
				// TODO Auto-generated method stub
				Log.d(TAG, "invite:"+jsonobject);
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void shareQQ(){
		Intent intent=new Intent(Intent.ACTION_SEND);  
		intent.setType("image/*");
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo info : list){
			ActivityInfo aInfo = info.activityInfo;
			String packName = aInfo.packageName;
			Log.d(TAG, "packname:"+packName);
		}
//		Bundle params = new Bundle();
//		//params.putString(Constants.PARAM_TITLE,"分享的标题");
//		params.putString(Constants.PARAM_IMAGE_URL	,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//		params.putString(Constants.PARAM_TARGET_URL,
//				"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//		//params.putString(Constants.PARAM_SUMMARY,"一些描述");
//		params.putString(Constants.PARAM_APP_SOURCE,"微信表情包100399626");
//		
//		mTencent.shareToQQ(democall.this, params, new IUiListener() {
//			
//			@Override
//			public void onError(UiError uierror) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onComplete(JSONObject jsonobject) {
//				// TODO Auto-generated method stub
//				Log.d(TAG, "onComplete:"+jsonobject.toString());
//			}
//			
//			@Override
//			public void onCancel() {
//				// TODO Auto-generated method stub
//				
//			}
//		});

	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mTencent.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 *
	 * @param path /storage/sdcard0/DCIM/Camera/
	 */
	protected void getThumb(String path){
		Uri uri = Media.getContentUri("external");
		String order = MediaColumns.DATE_MODIFIED + " desc";
		String[] colums = { MediaColumns._ID, MediaColumns.DATA,
				MediaColumns.DATE_MODIFIED };
		String where = MediaColumns.DATA + " like ?";
		String[] whereArgs = { path + "%"};
		Cursor cursor = getContentResolver().query(uri, colums, where, whereArgs,
				order);
		cursor.moveToFirst();
		if(cursor != null){
			cursor.moveToFirst();
			do {
				long id = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaColumns._ID));
				String filepath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
				Log.d(TAG, " id:"+id + " filepath:"+filepath);
			} while (cursor.moveToNext());
		}
	}
}
