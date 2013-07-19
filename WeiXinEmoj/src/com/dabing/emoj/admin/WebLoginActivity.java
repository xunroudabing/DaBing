package com.dabing.emoj.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.TokenStore;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

public class WebLoginActivity extends BaseActivity {

	WebView webView;
	OAuthV2 oAuthV2;
	static final String TAG = WebLoginActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.tencent.mm.sdk.uikit.MMBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle("登录");
		webView = (WebView) findViewById(R.id.webview);
		oAuthV2 = (OAuthV2) AppConfig.getOAuth();
		String url = OAuthV2Client.generateImplicitGrantUrl(oAuthV2);
		Log.d(TAG, "----url:"+url);
		Intent data = getIntent();
		String className = "";
		if(data.getStringExtra("classname") != null){
			className = data.getStringExtra("classname");
		}
		 WebSettings webSettings = webView.getSettings();
	     webSettings.setJavaScriptEnabled(true);
	     webSettings.setSupportZoom(true);
	     webView.requestFocus();
	     webView.loadUrl(url);
	     final String mClassName = className;
	     WebViewClient client = new WebViewClient(){
	    	 @Override
	    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    		// TODO Auto-generated method stub
	    		 Log.d(TAG, "on onPageStarted...");
	    		 if (url.indexOf("access_token=") != -1) {
	                    int start=url.indexOf("access_token=");
	                    String responseData=url.substring(start);
	                    Log.d(TAG, "----responseData:"+responseData);
	                    OAuthV2Client.parseAccessTokenAndOpenId(responseData, oAuthV2);
	                    TokenStore.store(getApplicationContext(), responseData);

	                    view.destroyDrawingCache();
	                    view.destroy();
	                    finish();
	                }
	    		super.onPageStarted(view, url, favicon);
	    	}
	    	 @Override
	            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
	                if ((null != view.getUrl()) && (view.getUrl().startsWith("https://open.t.qq.com"))) {
	                    handler.proceed();// 接受证书
	                } else {
	                    handler.cancel(); // 默认的处理方式，WebView变成空白页
	                }
	                // handleMessage(Message msg); 其他处理
	            }
	     };
	     
	     webView.setWebViewClient(client);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.webview_login;
	}
}
