package com.dabing.emoj.provider;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.TokenStore;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;

public class MyRequest extends BaseRequest{
	String mKey="";
	String mReqnum="40";
	OAuth oauth;
	public MyRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyRequest(Context context,String key){
		this(context);
		if(context != null){
			oauth = TokenStore.fetch(context);
		}
		mKey = key;
	}
	@Override
	public void setContext(Context context){
		super.setContext(context);
		oauth = TokenStore.fetch(context);
	}
	@Override
	public String execute(String pageflag, String tweetid, String time) {
		// TODO Auto-generated method stub
		DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);
		try {
			String response = request.getHt_timeline(oauth, mKey, mReqnum, pageflag, tweetid, time, "4");
			Log.d(TAG, "MyRequest execute");
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}
	public static final Parcelable.Creator<MyRequest> CREATOR = new Creator<MyRequest>() {

		public MyRequest createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			String pageflag = source.readString();
			String time = source.readString();
			String tweetid = source.readString();
			String key = source.readString();
			MyRequest request = new MyRequest(null, key);
			request.mPageflag = pageflag;
			request.mTime = time;
			request.mTweetid = tweetid;
			return request;
		}

		public MyRequest[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mPageflag);
		dest.writeString(mTime);
		dest.writeString(mTweetid);
		dest.writeString(mKey);
		
		
	}
	
}
