package com.dabing.emoj.provider;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import greendroid.util.GDUtils;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UMengDataProvider {
	public interface IUMengReceiveListener{
		void Receive(String resposne);
		void Exception(Exception exception);
		void Loading();
		void Finish();
	}
	UMengHandler mHandler;
	IUMengReceiveListener listener;
	Context mContext;
	static final String TAG = UMengDataProvider.class.getSimpleName();
	public UMengDataProvider(Context context){
		mContext = context;
		mHandler = new UMengHandler();
	}
	public void setUMengReceiveListener(IUMengReceiveListener l){
		listener = l;
	}
	/**
	 * 获取数据
	 * @param array ["001","002"]
	 */
	public void execute(JSONArray array){
		mHandler.sendEmptyMessage(3);
		GDUtils.getExecutor(mContext).execute(new RequestTask(array));
	}
	class RequestTask implements Runnable{
		JSONArray mkey;
		public RequestTask(JSONArray array){
			mkey = array;
		}
		private String merge(List<String> list){
			JSONObject object = new JSONObject();
			JSONArray data = new JSONArray();
			String name ="";
			String p = "";
			for(String json : list){
				try {
					JSONObject item = new JSONObject(json);
					JSONArray item_data = item.getJSONArray("data");
					name = item.getString("name");
					p = item.getString("p");
					for(int i=0;i<item_data.length();i++){
						data.put(item_data.getString(i));
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG, e.toString());
				}
			}
			try {
				object.put("data", data);
				object.put("name", name);
				object.put("p", p);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
			return object.toString();
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				List<String> list = new LinkedList<String>();
				for(int i =0;i<mkey.length();i++){
					String key = mkey.getString(i);
					String response = MobclickAgent.getConfigParams(mContext, key);
					Log.d(TAG, "response:"+response);
					if(response != null && !response.equals("")){
						list.add(response);
					}
				}
				String json = merge(list);
				Log.d(TAG, "merged:"+json);
				mHandler.sendMessage(Message.obtain(mHandler, 1, json));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
				mHandler.sendMessage(Message.obtain(mHandler, 2, e));
			}finally{
				mHandler.sendEmptyMessage(4);
			}
		}
		
	}
	class UMengHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO 1-Receive 2-Exception 3-Loading 4-Finish
			switch (msg.what) {
			case 1:
				if(listener != null){
					listener.Receive(msg.obj.toString());
				}
				break;
			case 2:
				if(listener != null){
					Exception e = (Exception) msg.obj;
					listener.Exception(e);
				}
				break;
			case 3:
				if(listener != null){
					listener.Loading();
				}
				break;
			case 4:
				if(listener != null){
					listener.Finish();
				}
			default:
				break;
			}
		}
		
	}
}
