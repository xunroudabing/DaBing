package com.dabing.emoj.activity;

import java.util.Random;

import com.dabing.emoj.R;
import com.dabing.emoj.service.StartUpBroadcast;
import com.umeng.analytics.MobclickAgent;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	ImageView imgView;
	AnimationDrawable anim;
	static final int[] ANIM_RESOURCE_ARRAY = {R.drawable.welcome_anim_1,R.drawable.welcome_anim_2,R.drawable.welcome_anim_3};
	static final long delay = 5000;
	static final String TAG = WelcomeActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , 
                WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		setContentView(R.layout.welcome);
		imgView = (ImageView) findViewById(R.id.welcome_img_anim);
		Random random = new Random();
		int index = random.nextInt(ANIM_RESOURCE_ARRAY.length);
		imgView.setBackgroundResource(ANIM_RESOURCE_ARRAY[index]);
		anim = (AnimationDrawable) imgView.getBackground();
		sendBroadcast();
		MobclickAgent.updateOnlineConfig(this);
		new Handler().postDelayed(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), MainTab2Activity.class);
				startActivity(intent);
				finish();
			}
		},delay);
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		anim.start();
	}
	
	private void sendBroadcast(){
		Intent intent = new Intent(getApplicationContext(), StartUpBroadcast.class);
		sendBroadcast(intent);
	}
}
