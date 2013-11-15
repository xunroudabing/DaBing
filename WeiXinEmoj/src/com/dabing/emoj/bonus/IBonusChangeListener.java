package com.dabing.emoj.bonus;
/**
 * 积分变化监听
 * @author Administrator
 *
 */
public interface IBonusChangeListener {
	/**
	 * 积分变化
	 * @param TAG get-获取 spend-消费 award-奖励
	 * @param value 变化的积分 有正负值
	 */
	void onChange(String TAG,int value);
	/**
	 * 异常
	 * @param TAG
	 * @param ex
	 */
	void onError(String TAG,String ex);
}
