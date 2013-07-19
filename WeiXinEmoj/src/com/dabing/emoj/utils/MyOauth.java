package com.dabing.emoj.utils;

import android.util.Log;

import com.tencent.weibo.utils.QStrOperate;

public class MyOauth {
	public String access_token = "";
	public long expires_in = 0;
	public String openid="";
	public String openkey="";
	public String refresh_token="";
	public String name="";
	public String nick="";
	public String msg="";
	static final String TAG = MyOauth.class.getSimpleName();
	public MyOauth(String accessToken,long expiresIn,String refreshToken){
		access_token = accessToken;
		expires_in = expiresIn;
		refresh_token = refreshToken;
	}
	/**
	 * 通过字符串来实例化
	 * @param response access_token=c498110ee804f82662b6a80d00e7f199&expires_in=604800&openid=1F897B956FF99B6865FDF04639FE19CF&openkey=7F3F4EC466C63713151A4E1E7F0D1289&refresh_token=54ae548a984a45bbc4249c82f12b930a&name=xunroudabing_&nick=%E7%86%8F%E8%82%89%E5%A4%A7%E9%A5%BC
	 */
	public MyOauth(String response){
		if(!QStrOperate.hasValue(response)){
			throw new UnsupportedOperationException("response can not be null");
		}
		 String[] tokenArray = response.split("&");	        
	     Log.i(TAG, "parseToken response=>> tokenArray.length = "+tokenArray.length);

	     if (tokenArray.length < 2) {
	        	throw new UnsupportedOperationException("tokenArray.length must more than 2");
	      }
	     String strAccessToken = tokenArray[0];
	     String strExpiresIn = tokenArray[1];

	     String[] accessToken = strAccessToken.split("=");
	     String[] experesIn = strExpiresIn.split("=");
	     if(accessToken.length < 2 || experesIn.length < 2){
	    	 throw new UnsupportedOperationException("response参数不合法");
	     }
	     access_token = accessToken[1];
	     expires_in = Long.parseLong(experesIn[1]);
	     if(tokenArray.length == 7){
		     String strOpenId = tokenArray[2];
		     String strOpenKey = tokenArray[3];
		     String strRefleshToken = tokenArray[4];
		     String strName = tokenArray[5];
		     String strNick = tokenArray[6];
		     
		     String[] OPENID = strOpenId.split("=");
		     String[] OPENKEY = strOpenKey.split("=");
		     String[] refleshToken = strRefleshToken.split("=");
		     String[] NAME = strName.split("=");
		     String[] NICK = strNick.split("=");
		     
		     openid = OPENID[1];
		     openkey = OPENKEY[1];
		     refresh_token = refleshToken[1];
		     name = NAME[1];
		     nick = NICK[1];
	     }else if (tokenArray.length == 8) {
	    	 String strOpenId = tokenArray[2];
		     String strOpenKey = tokenArray[3];
		     String strRefleshToken = tokenArray[4];
		     String strName = tokenArray[6];
		     String strNick = tokenArray[7];
		     
		     String[] OPENID = strOpenId.split("=");
		     String[] OPENKEY = strOpenKey.split("=");
		     String[] refleshToken = strRefleshToken.split("=");
		     String[] NAME = strName.split("=");
		     String[] NICK = strNick.split("=");
		     
		     openid = OPENID[1];
		     openkey = OPENKEY[1];
		     refresh_token = refleshToken[1];
		     name = NAME[1];
		     nick = NICK[1];
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("access_token=%s&expires_in=%d&openid=%s&openkey=%s&refresh_token=%s&name=%s&nick=%s", access_token,expires_in,openid,openkey,refresh_token,name,nick);
	}
	/**
	 * 刷新Token
	 * @param response access_token=04db230bd2032d089252284444d0aff0&expires_in=604800&refresh_token=f0e1ba22c5c460701f6f32f35a6eeab9&name=xunroudabing_&nick=熏肉大饼
	 */
	public void RefreshToken(String response){
		Log.d(TAG, "RefreshToken :"+response);
		String[] tokenArray = response.split("&");	  
		if(tokenArray.length < 3){
			throw new UnsupportedOperationException("tokenArray.length must more than 2");
		}
		String strAccessToken = tokenArray[0];
		String strRefreshToken = tokenArray[2];
		
		String[] accessToken = strAccessToken.split("=");
		String[] refreshToken = strRefreshToken.split("=");
		
		this.access_token = accessToken[1];
		this.refresh_token = refreshToken[1];
		Log.d(TAG, "access_token:"+access_token + " refresh_token:"+refresh_token);
	}
	
}
