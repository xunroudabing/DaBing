package com.dabing.emoj.bonus;

import com.dabing.ads.AppConnect;
import com.dabing.ads.UpdatePointsNotifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
/**
 * 万普积分
 * @author DaBing
 *
 */
public class WAPS_Bonus implements IBouns {
	Context mContext;
	static final String FILENAME = "com.dabing.emoj";
	static final String TAG = WAPS_Bonus.class.getSimpleName();
	public WAPS_Bonus(Context context){
		mContext = context;
	}
	@Override
	public int get() {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public int spend(int bonus) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void award(int bonus) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void set(int bonus) {
		// TODO Auto-generated method stub
		setBonus(bonus);
	}
	
	protected void setBonus(int bonus){
		SharedPreferences preferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("waps_bonus", bonus);
		editor.commit();
	}
	
	public int getBonusCached(){
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
			}
			
			@Override
			public void getUpdatePoints(String s, int i) {
				// TODO Auto-generated method stub
				Log.d(TAG, "getUpdatePoints:"+i+" currency:"+s);
				setBonus(i);
			}
		});
	}

}
