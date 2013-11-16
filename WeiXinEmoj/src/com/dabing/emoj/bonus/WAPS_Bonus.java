package com.dabing.emoj.bonus;

import com.dabing.ads.AppConnect;
import com.dabing.ads.UpdatePointsNotifier;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
/**
 * 万普积分
 * @author DaBing
 *
 */
public class WAPS_Bonus implements IBouns {
	IBonusChangeListener mListener;
	Context mContext;
	int spend_retry = 0;
	int get_retry = 0;
	static final int RETRY = 3;//重试次数
	static final String FILENAME = "com.dabing.emoj";
	static final String TAG = WAPS_Bonus.class.getSimpleName();
	public WAPS_Bonus(Context context){
		mContext = context;
	}
	@Override
	public int get() {
		// TODO Auto-generated method stub		
		return getBonus();
	}

	@Override
	public int spend(final int bonus) {
		// TODO Auto-generated method stub
		AppConnect.getInstance(mContext).spendPoints(bonus, new UpdatePointsNotifier() {
			
			@Override
			public void getUpdatePointsFailed(String arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "spend getUpdatePointsFailed:"+arg0);
				if(mListener != null){
					mListener.onError("spend", arg0);
				}
				//重试
				if(spend_retry++<RETRY){
					Log.d(TAG, "spend retry " + spend_retry);
					spend(bonus);
				}
			}
			
			@Override
			public void getUpdatePoints(String arg0, int arg1) {
				// TODO Auto-generated method stub
				Log.d(TAG, "spend getUpdatePoints:"+arg0 + " " + arg1);
				if(mListener != null){
					mListener.onChange("spend", -bonus);
				}
				setBonus(arg1);
			}
		});
		return 0;
	}

	@Override
	public void award(int bonus) {
		// TODO Auto-generated method stub
		AppConnect.getInstance(mContext).awardPoints(bonus, new UpdatePointsNotifier() {
			
			@Override
			public void getUpdatePointsFailed(String arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "spend getUpdatePointsFailed:"+arg0);
				if(mListener != null){
					mListener.onError("award", arg0);
				}
			}
			
			@Override
			public void getUpdatePoints(String arg0, int arg1) {
				// TODO Auto-generated method stub
				if(mListener != null){
					mListener.onChange("award", arg1);
				}
				setBonus(arg1);
			}
		});
		
	}
	@Override
	public void set(int bonus) {
		// TODO Auto-generated method stub
		setBonus(bonus);
	}
	/**
	 * 设置本地积分
	 * @param bonus
	 */
	protected void setBonus(int bonus){
		SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("waps_bonus", bonus);
		editor.commit();
	}
	/**
	 * 获取积分,来自本地缓存
	 * @return
	 */
	public int getBonus(){
		SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		int ret = preferences.getInt("waps_bonus", 0);
		return ret;
	}
	
	@Override
	public void reflesh() {
		// TODO Auto-generated method stub
		AppConnect.getInstance(mContext).getPoints(new UpdatePointsNotifier() {
			
			@Override
			public void getUpdatePointsFailed(String s) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Failed:"+s);
				if(mListener != null){
					mListener.onError("get", s);
				}
				//重试
				if(get_retry++<RETRY){
					reflesh();
				}
			}
			
			@Override
			public void getUpdatePoints(String s, int i) {
				// TODO Auto-generated method stub
				Log.d(TAG, "getUpdatePoints:"+i+" currency:"+s);
				if(mListener != null){
					//计算变化的积分
					int bonus_changed = i - getBonus();
					mListener.onChange("get", bonus_changed);
				}
				setBonus(i);
			}
		});
	}
	@Override
	public void setBonusChangeListener(IBonusChangeListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
	}
	@Override
	public void showOffers() {
		// TODO Auto-generated method stub
		AppConnect.getInstance(mContext).showAppOffers(mContext);
	}

}
