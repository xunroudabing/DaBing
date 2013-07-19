package com.dabing.emoj.exception;

import org.json.JSONException;
import org.json.JSONObject;



public class Check {
	public static boolean check_ret(JSONObject object) throws JSONException, OauthFailException, AccessRateLimitException, ParmErrorException, ServerErrorException{
		String ret = object.getString("ret");
		if(!ret.equals("0")){
			if(ret.equals("3")){
				throw new OauthFailException();
			}else if (ret.equals("2")) {
				//频率受限
				throw new AccessRateLimitException();
			}else if (ret.equals("1")) {
				//参数错误
				throw new ParmErrorException();
			}else if (ret.equals("4")) {
				//服务器内部错误
				throw new ServerErrorException();
			}
			return false;
		}

	return true;
	
}
}
