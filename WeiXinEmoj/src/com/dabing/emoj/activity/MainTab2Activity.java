package com.dabing.emoj.activity;

import org.json.JSONArray;

import com.dabing.emoj.R;
import com.dabing.emoj.advertise.AdManager;
import com.dabing.emoj.qqconnect.QQConnect;
import com.dabing.emoj.service.StartUpBroadcast;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.NetWorkCheck;
import com.dabing.emoj.widget.PromptDialog;
import com.tencent.qqconnect.dataprovider.CallbackManager;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * 新Bottombar样式
 * @author DaBing
 *
 */
public class MainTab2Activity extends TabActivity implements OnCheckedChangeListener {
	CallbackManager mCallbackManager;
	String action ="send";
	ImageView cursor;
	RadioButton r1,r2,r3,r4,r5;
	TextView newEmojView;
	TextView newSettingView;
	TextView newEmotionView;
	TabHost mTabHost;
	Intent intent1,intent2,intent3,intent4,intent5;
	static final String TAG = MainTab2Activity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab2);
		mTabHost = getTabHost();
		r1 = (RadioButton) findViewById(R.id.main_tab_home);
		r2 = (RadioButton) findViewById(R.id.main_tab_favorit);
		r3 = (RadioButton) findViewById(R.id.main_tab_settings);
		r4 = (RadioButton) findViewById(R.id.main_tab_emotion);
		r5 = (RadioButton) findViewById(R.id.main_tab_user_define);
				
		newEmojView = (TextView) findViewById(R.id.main_tab_unread_tv);
		newSettingView = (TextView) findViewById(R.id.main_tab_new_settings);
		newEmotionView = (TextView) findViewById(R.id.main_tab_new_emotion);
		r1.setOnCheckedChangeListener(this);
		r2.setOnCheckedChangeListener(this);
		r3.setOnCheckedChangeListener(this);
		r4.setOnCheckedChangeListener(this);
		r5.setOnCheckedChangeListener(this);
		SetupAction();
		InitilizeTabhost();
		InitAdvertise();
		InitPrompt();
		RegisterBroadcast();
		mCallbackManager = new CallbackManager(MainTab2Activity.this);
		QQConnect.createInstance(getApplicationContext()).Init();
		UMFeedbackService.enableNewReplyNotification(MainTab2Activity.this, NotificationType.AlertDialog);
		UmengUpdateAgent.update(MainTab2Activity.this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		CheckMediaMounted();
		CheckNetWorkState();
		showNewEmoj();
		showNewSetting();
	};
	@Override
	protected void onDestroy() {
		DestroyAdvertise();
		unRegisterBroadcast();
		super.onDestroy();
	};
	public void SetupAction(){
		Intent data = getIntent();
		if(data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION) != null){
			action = data.getStringExtra(AppConstant.INTENT_EMOJ_ACTION);			
		}

		if(action.equals("get")){
			Intent intent = new Intent(getApplicationContext(), StartUpBroadcast.class);
			sendBroadcast(intent);
		}
		Log.d(TAG, "action:"+action);

	}
	
	public void InitPrompt(){
		if(AppConfig.getFirstLogin(getApplicationContext())){
			PromptDialog dialog = new PromptDialog(MainTab2Activity.this);			
			dialog.show();
			AppConfig.setFirstLogin(getApplicationContext(), false);
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.main_tab_home:
				r2.setChecked(false);
				r3.setChecked(false);
				r4.setChecked(false);
				r5.setChecked(false);
				mTabHost.setCurrentTabByTag("tab1");
				break;
			case R.id.main_tab_favorit:

				r1.setChecked(false);
				r3.setChecked(false);
				r4.setChecked(false);
				r5.setChecked(false);
				mTabHost.setCurrentTabByTag("tab2");
				break;
			case R.id.main_tab_settings:

				r1.setChecked(false);
				r2.setChecked(false);
				r4.setChecked(false);
				r5.setChecked(false);
				mTabHost.setCurrentTabByTag("tab3");
				break;
			case R.id.main_tab_emotion:

				r1.setChecked(false);
				r2.setChecked(false);
				r3.setChecked(false);
				r5.setChecked(false);
				mTabHost.setCurrentTabByTag("tab4");
				break;
				
			case R.id.main_tab_user_define:
				r1.setChecked(false);
				r2.setChecked(false);
				r3.setChecked(false);
				r4.setChecked(false);
				mTabHost.setCurrentTabByTag("tab5");
				break;
			default:
				break;
			}
		}
	}
	
	protected void InitilizeTabhost(){
		intent1 = new Intent(getApplicationContext(),EmojContainerActivity.class);		
		intent2 = new Intent(getApplicationContext(),EmojSearchActivity.class);
		intent3=new Intent(getApplicationContext(),SettingActivity.class);
		intent4 = new Intent(getApplicationContext(), EmojEmotionActivity.class);
		intent5 = new Intent(getApplicationContext(), UserDefineActivity.class);
		Intent intent = getIntent();
		if(intent.getAction() != null){
			String intent_action = intent.getAction();
			Log.d(TAG, "intent_action:"+intent_action);
			if(intent_action.equals(Intent.ACTION_GET_CONTENT)){
				action = "pick";				
			}else if (intent_action.equals(Intent.ACTION_PICK)) {
				action = "pick";
			}
		}
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			intent1.putExtras(bundle);
			intent2.putExtras(bundle);
			intent3.putExtras(bundle);
			intent4.putExtras(bundle);
			intent5.putExtras(bundle);
		}	
		if(action.equals("pick")){
			intent1.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
			intent2.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
			intent3.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
			intent4.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
			intent5.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
		}
		mTabHost.addTab(buildTabSpec("tab1","", intent1));
		mTabHost.addTab(buildTabSpec("tab2","",intent2));
		mTabHost.addTab(buildTabSpec("tab3","",intent3));
		mTabHost.addTab(buildTabSpec("tab4","",intent4));
		mTabHost.addTab(buildTabSpec("tab5","",intent5));
	}
    private TabHost.TabSpec buildTabSpec(String tag,String tabTitile,final Intent content) {
        return this.mTabHost.newTabSpec(tag).setIndicator(tabTitile).setContent(content);
    }
    
    //初始化广告
    private void InitAdvertise(){
    	AdManager ad = new AdManager(MainTab2Activity.this);
    	ad.init();
    }
    private void DestroyAdvertise(){
    	AdManager ad = new AdManager(MainTab2Activity.this);
    	ad.release();
    }
    private void CheckMediaMounted(){
    	if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sdcard_eject_toast, null);
    		TextView textView = (TextView) layout.findViewById(R.id.sdcard_toast_text);
    		textView.setText(R.string.sdcard_mounted);
    		Toast toast = new Toast(MainTab2Activity.this);
    		toast.setDuration(Toast.LENGTH_LONG);
    		toast.setView(layout);
    		toast.show();
    	}
    }
    private void CheckNetWorkState(){
    	boolean connect = NetWorkCheck.isConnect(getApplicationContext());
		//网络不可用
		if(!connect){
			View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sdcard_eject_toast, null);
			ImageView iconView = (ImageView) layout.findViewById(R.id.sdcard_toast_img);
    		TextView textView = (TextView) layout.findViewById(R.id.sdcard_toast_text);
    		textView.setText(R.string.network_disconnect);
    		iconView.setImageResource(R.drawable.net_warn_icon);
    		Toast toast = new Toast(MainTab2Activity.this);
    		toast.setDuration(Toast.LENGTH_LONG);
    		toast.setView(layout);
    		toast.show();
		}
    }
    //注册广播监听
    private void RegisterBroadcast(){

    	IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");    	
        try {
        	registerReceiver(broadcastReceiver, intentFilter);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
        
    }
    //注销监听
    private void unRegisterBroadcast(){
    	try {
			unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
    }
    //显示新表情个数
    private void showNewEmoj(){
    	try {
			JSONArray emojArray= AppConfig.getNewEmojArray(getApplicationContext());
			if(emojArray != null && emojArray.length() > 0){
				int count = emojArray.length();
				newEmojView.setText(String.valueOf(count));
				newEmojView.setVisibility(View.VISIBLE);
			}else {
				newEmojView.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
    }
    //设置项是否有更新
    private void showNewSetting(){
    	try {
			if (AppConfig.getIsNew(getApplicationContext(), "unread_download") || AppConfig.getIsNew(getApplicationContext(), "unread_public")) {
				newSettingView.setVisibility(View.VISIBLE);
			}else {
				newSettingView.setVisibility(View.INVISIBLE);
			}
			
//			if(AppConfig.getIsNew(getApplicationContext(), "unread_emotion")){
//				newEmotionView.setVisibility(View.VISIBLE);
//			}else {
//				newEmotionView.setVisibility(View.INVISIBLE);
//			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "action:"+intent.getAction());
			if(intent.getAction().equals("android.intent.action.MEDIA_MOUNTED"))//SD卡已经成功挂载
            {                
                                                  
            }
			else if(intent.getAction().equals("android.intent.action.MEDIA_REMOVED")||intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED")||intent.getAction().equals("android.intent.action.ACTION_MEDIA_BAD_REMOVAL"))
            {	
				
            }
			else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
				CheckNetWorkState();
			}

		}
	};
	
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d(TAG, "dispatchKeyEvent");
    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && !mTabHost.getCurrentTabTag().equals("tab5")){
    		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
    			DialogFactory.createTwoButtonDialog(MainTab2Activity.this, "确定退出微信表情包?", "确定", "取消", new DialogInterface.OnClickListener() {
    				
    				public void onClick(DialogInterface dialog, int which) {
    					// TODO Auto-generated method stub
    					finish();
    					dialog.dismiss();
    				}
    			}, new DialogInterface.OnClickListener() {
    				
    				public void onClick(DialogInterface dialog, int which) {
    					// TODO Auto-generated method stub
    					dialog.dismiss();
    				}
    			}).show();
    			return true;
    		}
    	}
    	return super.dispatchKeyEvent(event);
    };
    
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
