package com.dabing.emoj.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.sax.Element;
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
public class BonusGainActivity extends BaseActivity implements OnClickListener,
		IBonusChangeListener {

	IBouns mBouns;
	Button btnGet, btnRemove, btnVip;
	TextView wealthTextView;
	static final String ACTION_VIEW_TYPE = "ACTION_VIEW_TYPE";//操作类型
	static final int ACTION_REMOVE_AD = 1;//去除广告
	static final int ACTION_TOVIP = 2;//升级会员
	static final int BONUS_REMOVE_AD = 50;// 移除广告所需积分
	static final int BONUS_TO_VIP = 80;// 升级会员所需积分
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
		btnVip = (Button) findViewById(R.id.bonus_gain_coin_btnVip);
		btnGet.setOnClickListener(this);
		btnRemove.setOnClickListener(this);
		btnVip.setOnClickListener(this);
		mBouns = new WAPS_Bonus(BonusGainActivity.this);
		mBouns.setBonusChangeListener(this);
		InitVisableState();
		InitViews();
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
		case R.id.bonus_gain_coin_btnVip:
			ToVip();
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

	// 重置按钮状态
	protected void ResetButtonState() {

	}

	// 获取铜板
	protected void GainCoin() {
		mBouns.showOffers();
	}

	protected void UmengEvent(String eventid) {
		MobclickAgent.onEvent(BonusGainActivity.this, eventid);
	}
	//显示按钮可见状态
	protected void InitVisableState(){
		if(getIntent().getIntExtra(ACTION_VIEW_TYPE, -1) != -1){
			int action = getIntent().getIntExtra(ACTION_VIEW_TYPE, -1);
			switch (action) {
			case ACTION_TOVIP:
				btnVip.setVisibility(View.VISIBLE);
				btnRemove.setVisibility(View.GONE);
				break;
			case ACTION_REMOVE_AD:
				btnRemove.setVisibility(View.VISIBLE);
				btnVip.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	}
	// 初始化
	protected void InitViews() {
		String str_tovip = "";
		// 非会员
		if (!AppConfig.getIsVip(getApplicationContext())) {
			str_tovip = getString(R.string.btn_vip).replace("{bonus}",
					String.valueOf(BONUS_TO_VIP));
		} else {
			str_tovip = getString(R.string.btn_vip_finish);
			btnVip.setEnabled(false);
		}
		btnVip.setText(str_tovip);
		
		String str_removead = "";
		// 广告已去除
		if (AppConfig.getAdvertiseRemove(getApplicationContext())) {
			str_removead = getString(R.string.btn_remove_ad_finish);
			btnRemove.setEnabled(false);
		}else {
			str_removead = getString(R.string.btn_remove_ad).replace("{bonus}", String.valueOf(BONUS_REMOVE_AD));
		}
		btnRemove.setText(str_removead);
	}

	// 升级会员
	protected void ToVip() {
		try {
			int wealth = mBouns.get();
			// 积分足够
			if (wealth >= BONUS_TO_VIP) {
				String temp = getString(R.string.alert_confirm_tovip);
				String msg = temp.replace("{bonus}",
						String.valueOf(BONUS_TO_VIP));
				Dialog mDialog = DialogFactory.createTwoButtonDialog(
						BonusGainActivity.this, msg, null, null,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								// TODO 确定
								mBouns.spend(BONUS_TO_VIP);
								AppConfig.setIsVip(getApplicationContext(),
										true);
								UmengEvent("action034");
								dialog.dismiss();
								runOnUiThread(new Runnable() {
									public void run() {
										InitViews();
										Toast.makeText(BonusGainActivity.this,
												R.string.alert_tovip_success,
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
			} else {
				Dialog mDialog = DialogFactory.createFailDialog(
						BonusGainActivity.this,
						getString(R.string.alert_bonus_notenough),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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
										InitViews();
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
				Dialog mDialog = DialogFactory.createFailDialog(
						BonusGainActivity.this,
						getString(R.string.alert_bonus_notenough),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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
