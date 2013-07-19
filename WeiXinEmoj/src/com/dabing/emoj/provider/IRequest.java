package com.dabing.emoj.provider;

public interface IRequest {
	/**
	 * 第一页
	 * @param response
	 */
	void onBind(String response);
	/**
	 * 分页事件
	 * @param response
	 */
	void onRefresh(String response);
	/**
	 * 是否有下一页
	 * @param hasnext 1-有下一页 0-拉取完毕
	 */
	void onHasNext(String hasnext);
	void onException(Exception ex);
	/**
	 * 加载中
	 * @param pageflag 0-第一页 1-分页
	 */
	void onLoading(String pageflag);
	void onRequestEnd();
}
