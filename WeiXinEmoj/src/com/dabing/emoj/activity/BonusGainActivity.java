package com.dabing.emoj.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.bonus.IBonusChangeListener;
import com.dabing.emoj.bonus.IBouns;
import com.dabing.emoj.bonus.WAPS_Bonus;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.DialogFactory;
import com.dabing.emoj.utils.MediaUtils;
import com.tencent.mm.sdk.uikit.MMBaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 获取积分
 * 
 * @author DaBing
 * 
 */
public class BonusGainActivity extends BaseActivity implements
		OnClickListener, IBonusChangeListener {

	IBouns mBouns;
	Button btnGet, btnRemove;
	TextView wealthTextView;
	static final int BONUS_REMOVE_AD = 50;// 移除广告所需积分
	static final String TAG = BonusGainActivity.class.getSimpleName();

	/*
	 * (non-Javadoc)
	 * 
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
		ResetButtonState();
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

	/*
	 * (non-Javadoc)
	 * 
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
		if (t.equals("get")) {
			if (value != 0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						MediaUtils.getInstance(getApplicationContext())
								.playSound(R.raw.coin);
						showToast(value);
						int b = mBouns.get();
						wealthTextView.setText(String.valueOf(b));
					}
				});

			}
		} else if (t.equals("spend")) {
			if (value != 0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						MediaUtils.getInstance(getApplicationContext())
								.playSound(R.raw.coin);
						showToast(value);
					}
				});
			}
		}
		// 更新UI 最新余额
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				UpdateUI();
			}
		});
	}

	@Override
	public void onError(String TAG, String ex) {
		// TODO Auto-generated method stub

	}

	protected void showToast(int value) {
		View view = LayoutInflater.from(BonusGainActivity.this).inflate(
				R.layout.bonus_alert_toast, null);
		TextView txt = (TextView) view.findViewById(R.id.bonus_alert_toast_txt);
		String s = value > 0 ? String.format("+%d", value) : String
				.valueOf(value);
		txt.setText(s);
		Toast toast = new Toast(BonusGainActivity.this);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		toast.show();
	}

	protected void Init() {
		mBouns.reflesh();
	}

	// 更新UI
	protected void UpdateUI() {
		int b = mBouns.get();
		wealthTextView.setText(String.valueOf(b));
		
	}
	//重置按钮状态
	protected void ResetButtonState(){
		// 广告已去除
		if (AppConfig.getAdvertiseRemove(getApplicationContext())) {
			btnRemove.setEnabled(false);
			btnRemove.setText(R.string.btn_remove_ad_finish);
		}
	}
	// 获取铜板
	protected void GainCoin() {
		mBouns.showOffers();
	}
	
	protected void UmengEvent(String eventid){
		MobclickAgent.onEvent(BonusGainActivity.this, eventid);
	}
	// 去除广告
	protected void RemoveAd() {
		try {
			int wealth = mBouns.get();
			// 积分足够，提示购买
			if (wealth >= BONUS_REMOVE_AD) {
				Log.d(TAG, "have enough bonus,ready to buy?");
				String temp = getString(R.string.alert_confirm_removead);
				String msg = temp.replace("{bonus}",
						String.valueOf(BONUS_REMOVE_AD));
				Dialog mDialog = DialogFactory.createTwoButtonDialog(
						BonusGainActivity.this, msg, null, null,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								// TODO 确定
								mBouns.spend(BONUS_REMOVE_AD);
								AppConfig.setAdvertiseRemove(
										getApplicationContext(), true);
								UmengEvent("action033");
								dialog.dismiss();
								runOnUiThread(new Runnable() {
									public void run() {
										ResetButtonState();
										Toast.makeText(
												BonusGainActivity.this,
												R.string.alert_removead_success,
												Toast.LENGTH_SHORT).show();
									}
								});
							}
						}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								// TODO 取消
								dialog.dismiss();
							}
						});
				mDialog.show();
			}
			// 积分不足，提示获取铜板
			else {
				Dialog mDialog = DialogFactory.createFailDialog(BonusGainActivity.this, getString(R.string.alert_bonus_notenough), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				mDialog.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}

}
