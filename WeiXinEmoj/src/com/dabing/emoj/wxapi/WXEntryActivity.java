package com.dabing.emoj.wxapi;


import com.dabing.emoj.activity.MainTab1Activity;
import com.dabing.emoj.activity.MainTab2Activity;
import com.dabing.emoj.activity.MainTabActivity;
import com.dabing.emoj.demo.democall;
import com.dabing.emoj.utils.AppConstant;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;
	static final String TAG = WXEntryActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		//setContentView(R.layout.about);
		api = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APPID, false);
		api.handleIntent(getIntent(), this);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);		
		Log.d(TAG, "onNewIntent");
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onReq.getType():"+arg0.getType());
		switch (arg0.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			GetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		default:
			break;
		}
	}
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResp.errCode:"+arg0.errCode+arg0.errStr);
		switch (arg0.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			break;
		case BaseResp.ErrCode.ERR_UNSUPPORT:
			break;
		default:
			break;
		}
		finish();
	}
	private void GetMsg(){
		Intent intent = new Intent(getApplicationContext(), MainTab2Activity.class);
		intent.putExtra(AppConstant.INTENT_EMOJ_ACTION, "get");
		intent.putExtras(getIntent().getExtras());
//		Bundle bundle = getIntent().getExtras();
//		Set<String> keyset = bundle.keySet();
//		for(String key:keyset){
//			Object obj = bundle.get(key);
//			Log.d(TAG, "key:"+key+" obj:"+obj);
//		}
		startActivity(intent);
		finish();
	}
}
