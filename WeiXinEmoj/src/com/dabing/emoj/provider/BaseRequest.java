package com.dabing.emoj.provider;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dabing.emoj.exception.Check;

import greendroid.util.GDUtils;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public abstract class BaseRequest implements Parcelable{
	/**
	 * id的字段名称
	 */
	protected String FieldName_ID = "id";
	/**
	 * 时间戳的字段名称
	 */
	protected String FieldName_TimeStamp = "timestamp";
	protected String mPageflag="0";
	protected String reqnum="30";
	protected String mTweetid="";
	protected String mTime="";
	protected Context mContext;
	private IRequest listener;
	private RequestHandler mHandler;
	static final String TAG = BaseRequest.class.getSimpleName();
	public BaseRequest(Context context){
		mContext = context;
		mHandler = new RequestHandler();
	}
	public void setContext(Context context){
		mContext = context;
	}
	public void setOnRequestListener(IRequest l){
		listener = l;
	}
	public abstract String execute(String pageflag,String tweetid,String time);
	/**
	 * 请求数据
	 */
	public void beginRequest(){
		mHandler.sendMessage(Message.obtain(mHandler, 4, mPageflag));
		GDUtils.getExecutor(mContext).execute(new executeTask());
	}
	public void reset(){
		mPageflag = "0";
		mTweetid = "";
		mTime="";
	}
	class executeTask implements Runnable{
		public void run() {
			// TODO Auto-generated method stub
			try {
				String response = execute(mPageflag,mTweetid,mTime);
				Log.d(TAG, "response:"+response);
				JSONObject object = new JSONObject(response);
				//成功返回值
				if(Check.check_ret(object)){
					JSONObject data = object.getJSONObject("data");
					String hasnext = data.getString("hasnext");
					mHandler.sendMessage(Message.obtain(mHandler, 0, hasnext));
					//第一页
					if(mPageflag.equals("0")){
						mPageflag = "1";	
						mHandler.sendMessage(Message.obtain(mHandler, 1, response));
					}else if (mPageflag.equals("1")) {
						mHandler.sendMessage(Message.obtain(mHandler, 2, response));
					}
					JSONArray array = data.getJSONArray("info");
					if(array.length() > 0){
						JSONObject lastItem = array.getJSONObject(array.length()-1);
						mTweetid = lastItem.getString(FieldName_ID);
						mTime = lastItem.getString(FieldName_TimeStamp);
					}
				
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
				mHandler.sendMessage(Message.obtain(mHandler, 3, e));
			}finally{
				mHandler.sendEmptyMessage(5);
			}

		}		
	}
	class RequestHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO 0-hasnext后面还有没有数据 1-onbind初次绑定 2-onrefresh分页 3-exception 4-loading 5-requestend请求结束
			switch (msg.what) {
			case 0:
				if(listener != null){
					listener.onHasNext(msg.obj.toString());
				}
				break;
			case 1:
				if(listener != null){
					listener.onBind(msg.obj.toString());
				}
				break;
			case 2:
				if(listener != null){
					listener.onRefresh(msg.obj.toString());
				}
				break;
			case 3:
				Exception e = (Exception) msg.obj;
				if(listener != null){
					listener.onException(e);
				}
				break;
			case 4:
				if(listener != null){
					listener.onLoading(msg.obj.toString());
				}
				break;
			case 5:
				if(listener != null){
					listener.onRequestEnd();
				}
			default:
				break;
			}
		}
		
	}
}
