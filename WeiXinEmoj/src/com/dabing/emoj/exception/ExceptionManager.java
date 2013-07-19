package com.dabing.emoj.exception;

import android.content.Context;
import android.widget.Toast;

public class ExceptionManager {
	public static void handle(Context context,Exception e){
		String text = "";
		if(e instanceof AccessRateLimitException){
			text = "当前访问频率过高,请稍后再试...";
		}else if (e instanceof OauthFailException) {
			text = "认证授权已过期...";
		}else if (e instanceof ParmErrorException) {
			text = "参数错误";
		}else if (e instanceof ServerErrorException) {
			text = "服务器内部错误,请稍后再试...";
		}
		if(!text.equals("")){
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}
	}
}
