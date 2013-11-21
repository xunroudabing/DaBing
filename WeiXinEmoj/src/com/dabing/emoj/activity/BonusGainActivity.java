package com.dabing.emoj.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dabing.emoj.R;
import com.dabing.emoj.bonus.IBonusChangeListener;
import com.dabing.emoj.bonus.IBouns;
import com.dabing.emoj.bonus.WAPS_Bonus;
import com.dabing.emoj.utils.MediaUtils;
import com.tencent.mm.sdk.uikit.MMBaseActivity;
/**
 * 获取积分
 * @author DaBing
 *
 */
public class BonusGainActivity extends MMBaseActivity implements OnClickListener,IBonusChangeListener{


	IBouns mBouns;
	Button btnGet,btnRemove;
	TextView wealthTextView;
	static final String TAG = BonusGainActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		wealthTextView = (TextView) findViewById(R.id.bonus_gain_coin_wealth);
		btnGet = (Button) findViewById(R.id.bonus_gain_coin_btnGetCoin);
		btnRemove = (Button) findViewById(R.id.bonus_gain_coin_btnRemoveAd);
		btnGet.setOnClickListener(this);
		btnRemove.setOnClickListener(this);
		mBouns = new WAPS_Bonus(BonusGainActivity.this);
		mBouns.setBonusChangeListener(this);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.bonus_gain_coin;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bonus_gain_coin_btnGetCoin:
			GainCoin();
			break;
		case R.id.bonus_gain_coin_btnRemoveAd:
			RemoveAd();
			break;
		default:
			break;
		}
	}	
	
	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Init();
	}
	@Override
	public void onChange(String t, final int value) {
		// TODO Auto-generated method stub
		if(t.equals("get")){
			if(value != 0){
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MediaUtils.getInstance(getApplicationContext()).playSound(R.raw.coin);
						showToast(value);
						int b = mBouns.get();
						wealthTextView.setText(String.valueOf(b));
					}
				});
				
			}
		}else if (t.equals("spend")) {
			if(value != 0){
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MediaUtils.getInstance(getApplicationContext()).playSound(R.raw.coin);
						showToast(value);
					}
				});
			}
		}
	}

	@Override
	public void onError(String TAG, String ex) {
		// TODO Auto-generated method stub
		
	}
	
	protected void showToast(int value){
		View view = LayoutInflater.from(BonusGainActivity.this).inflate(R.layout.bonus_alert_toast, null);
		TextView txt = (TextView) view.findViewById(R.id.bonus_alert_toast_txt);
		String s = value > 0 ? String.format("+%d", value):String.valueOf(value);
		txt.setText(s);
		Toast toast = new Toast(BonusGainActivity.this);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}
	
	protected void Init(){
		mBouns.reflesh();
		int b = mBouns.get();
		wealthTextView.setText(String.valueOf(b));
	}
	//获取铜板
	protected void GainCoin(){
		mBouns.showOffers();
	}
	//去除广告
	protected void RemoveAd(){
		
	}

}
