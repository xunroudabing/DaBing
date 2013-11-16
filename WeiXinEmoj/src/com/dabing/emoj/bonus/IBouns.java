package com.dabing.emoj.bonus;

public interface IBouns {
	/**
	 * 跳转至积分墙
	 */
	void showOffers();
	/**
	 * 设置积分
	 * @param bonus
	 */
	void set(int bonus);
	/**
	 * 获取积分
	 * @return
	 */
	int get();
	/**
	 * 消费积分
	 * @param bonus
	 * @return
	 */
	int spend(int bonus);
	/**
	 * 奖励积分
	 * @param bonus
	 */
	void award(int bonus);
	/**
	 * 刷新 从服务器端获取积分
	 */
	void reflesh();
	/**
	 * 积分变化的监听事件
	 * @param listener
	 */
	void setBonusChangeListener(IBonusChangeListener listener);
}
