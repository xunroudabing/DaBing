package com.dabing.emoj.fragment;

import android.support.v4.app.Fragment;

public class UserDefineFragment extends Fragment {
	public interface IEmojScanCallBack{
		/**
		 * 加载中
		 */
		void onLoading();
		/**
		 * 任务结束
		 */
		void onEnd();
		/**
		 * 点击事件
		 * @param TAG 标签名
		 * @param parms 参数
		 */
		void onClick(String TAG,Object parms);
		/**
		 * 初始化完成
		 * @param TAG
		 */
		void onInit(String TAG,Object parms);
	}
}
