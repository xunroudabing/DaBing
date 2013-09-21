package com.dabing.emoj.qqconnect;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;

public class BaseApiListener implements IRequestListener {
	public String mScope = "all";
	public Boolean mNeedReAuth = false;

	public BaseApiListener(String scope, boolean needReAuth) {
		mScope = scope;
		mNeedReAuth = needReAuth;
	}

	protected void doComplete(JSONObject response, Object state) {
		

	}

	@Override
	public void onComplete(JSONObject jsonobject, Object obj) {
		// TODO Auto-generated method stub
		doComplete(jsonobject, obj);
	}

	@Override
	public void onConnectTimeoutException(
			ConnectTimeoutException connecttimeoutexception, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHttpStatusException(HttpStatusException httpstatusexception,
			Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIOException(IOException ioexception, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onJSONException(JSONException jsonexception, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMalformedURLException(
			MalformedURLException malformedurlexception, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetworkUnavailableException(
			NetworkUnavailableException networkunavailableexception, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocketTimeoutException(
			SocketTimeoutException sockettimeoutexception, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnknowException(Exception exception, Object obj) {
		// TODO Auto-generated method stub

	}

}
