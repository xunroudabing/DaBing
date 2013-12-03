package com.dabing.emoj.activity;


import org.json.JSONArray;

import com.dabing.emoj.R;
import com.dabing.emoj.service.StartUpBroadcast;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.NetWorkCheck;
import com.dabing.emoj.widget.PromptDialog;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainTabActivity extends TabActivity implements OnCheckedChangeListener {
	String action ="send";
	int offset = 0;// 动画图片偏移量
	int currIndex = 0;// 当前页卡编号
	int bmpW;// 动画图片宽度
	ImageView cursor;
	RadioButton r1,r2,r3;
	TextView newEmojView;
	TextView newSettingView;
	TabHost mTabHost;
	Intent intent1,intent2,intent3;
	static final String TAG = MainTabActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);
		mTabHost = getTabHost();
		r1 = (RadioButton) findViewById(R.id.main_tab_home);
		r2 = (RadioButton) findViewById(R.id.main_tab_favorit);
		r3 = (RadioButton) findViewById(R.id.main_tab_settings);
		newEmojView = (TextView) findViewById(R.id.main_tab_unread_tv);
		newSettingView = (TextView) findViewById(R.id.main_tab_new_settings);
		r1.setOnCheckedChangeListener(this);
		r2.setOnCheckedChangeListener(this);
		r3.setOnCheckedChangeListener(this);
		SetupAction();
		InitilizeTabhost();
		InitCursor();
		InitAdvertise();
		InitPrompt();
		RegisterBroadcast();
		UMFeedbackService.enableNewReplyNotification(MainTabActivity.this, NotificationType.AlertDialog);
		UmengUpdateAgent.update(MainTabActivity.this);
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
			PromptDialog dialog = new PromptDialog(MainTabActivity.this);			
			dialog.show();
			AppConfig.setFirstLogin(getApplicationContext(), false);
		}
	}
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.main_tab_home:
				MoveCursor(0);
				r2.setChecked(false);
				r3.setChecked(false);
				currIndex = 0;
				mTabHost.setCurrentTabByTag("tab1");
				break;
			case R.id.main_tab_favorit:
				MoveCursor(1);
				r1.setChecked(false);
				r3.setChecked(false);
				currIndex = 1;
				mTabHost.setCurrentTabByTag("tab2");
				break;
			case R.id.main_tab_settings:
				MoveCursor(2);
				r1.setChecked(false);
				r2.setChecked(false);
				currIndex = 2;
				mTabHost.setCurrentTabByTag("tab3");
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
		}	
		if(action.equals("pick")){
			intent1.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
			intent2.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
			intent3.putExtra(AppConstant.INTENT_EMOJ_ACTION, action);
		}
		mTabHost.addTab(buildTabSpec("tab1","", intent1));
		mTabHost.addTab(buildTabSpec("tab2","",intent2));
		mTabHost.addTab(buildTabSpec("tab3","",intent3));
	}
    private TabHost.TabSpec buildTabSpec(String tag,String tabTitile,final Intent content) {
        return this.mTabHost.newTabSpec(tag).setIndicator(tabTitile).setContent(content);
    }
    
    private void InitCursor(){
    	cursor = (ImageView) findViewById(R.id.main_cursor);
    	bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
    	DisplayMetrics dm = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(dm);
    	int screenW = dm.widthPixels;// 获取分辨率宽度
    	offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
    	Matrix matrix = new Matrix();
    	matrix.postTranslate(offset, 0);
    	cursor.setImageMatrix(matrix);// 设置动画初始位置
    }
    private void MoveCursor(int index){
    	int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
    	Animation animation=null;
    	if(index == 0){
    		if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
			}
    	}
    	else if (index == 1) {
    		if (currIndex == 0) {
				animation = new TranslateAnimation(offset, one, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
			}
		}
    	else if (index == 2) {
    		if (currIndex == 0) {
				animation = new TranslateAnimation(offset, two, 0, 0);
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
			}
		}
    	animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		cursor.startAnimation(animation);
    }
    //初始化广告
    private void InitAdvertise(){
    	if(AppConfig.getAdvertise_on(getApplicationContext())){
    		try {
    			Log.d(TAG, "MobWINManager.init...");	
    			//MobWINManager.init(MainTabActivity.this, Type.MOBWIN_BANNER);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}    		
    	}
    }
    private void DestroyAdvertise(){
    	try {
			//MobWINManager.destroy();
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
    }
    private void CheckMediaMounted(){
    	if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sdcard_eject_toast, null);
    		TextView textView = (TextView) layout.findViewById(R.id.sdcard_toast_text);
    		textView.setText(R.string.sdcard_mounted);
    		Toast toast = new Toast(MainTabActivity.this);
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
    		Toast toast = new Toast(MainTabActivity.this);
    		toast.setDuration(Toast.LENGTH_LONG);
    		toast.setView(layout);
    		toast.show();
		}
    }
    //注册广播监听
    private void RegisterBroadcast(){
//    	IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
//        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
//        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
//        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
//        intentFilter.addDataScheme("file");  
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
			if (AppConfig.getIsNew(getApplicationContext(), "unread_download")) {
				newSettingView.setVisibility(View.VISIBLE);
			}else {
				newSettingView.setVisibility(View.INVISIBLE);
			}
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
    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
    		if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
    			DialogFactory.createTwoButtonDialog(MainTabActivity.this, "确定退出微信表情包?", "确定", "取消", new DialogInterface.OnClickListener() {
    				
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
}
