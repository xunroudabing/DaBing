package com.dabing.emoj.provider;

import com.dabing.emoj.utils.DaBingRequest;
import com.dabing.emoj.utils.TokenStore;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
/**
 * 微频道请求对象
 * @author DaBing
 *
 */
public class ChannelRequest extends BaseRequest {
	String mKey="";
	String mReqnum="40";
	OAuth oauth;
	public ChannelRequest(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		FieldName_ID = "id";
		FieldName_TimeStamp = "pubtime";
	}
	/**
	 * 
	 * @param context
	 * @param key 频道id
	 */
	public ChannelRequest(Context context,String key){
		this(context);
		if(context != null){
			oauth = TokenStore.fetch(context);
		}
		mKey = key;
		
	}
	/**
	 * 
	 * @param context
	 * @param key 频道id
	 * @param reqNum 请求几条数据
	 */
	public ChannelRequest(Context context,String key,String reqNum){
		this(context, key);
		mReqnum = reqNum;
	}
	@Override
	public void setContext(Context context) {
		super.setContext(context);
		oauth = TokenStore.fetch(context);
	};
	public static final Parcelable.Creator<ChannelRequest> CREATOR = new Parcelable.Creator<ChannelRequest>() {

		@Override
		public ChannelRequest createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			String pageflag = source.readString();
			String time = source.readString();
			String tweetid = source.readString();
			String key = source.readString();
			ChannelRequest request = new ChannelRequest(null, key);
			request.mPageflag = pageflag;
			request.mTime = time;
			request.mTweetid = tweetid;
			return request;
		}

		@Override
		public ChannelRequest[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mPageflag);
		dest.writeString(mTime);
		dest.writeString(mTweetid);
		dest.writeString(mKey);
	}

	@Override
	public String execute(String pageflag, String tweetid, String time) {
		// TODO Auto-generated method stub
		DaBingRequest request = new DaBingRequest(OAuthConstants.OAUTH_VERSION_2_A);
		try {
			//String response = request.getHt_timeline(oauth, mKey, mReqnum, pageflag, tweetid, time, "4");
			String response = request.getChannelList(oauth, pageflag, time, tweetid, "", mKey, mReqnum, "1", "4");
			Log.d(TAG, "ChannelRequest execute:"+pageflag+" " + tweetid + " " + time);
			return response;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}

}
