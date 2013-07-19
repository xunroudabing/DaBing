package com.dabing.emoj.utils;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * 常用表情管理
 * {"name":"","p":"","time":""}
 * @author DaBing
 *
 */
public class RegularEmojManager {
	Context mContext;
	static final int MAX=20;
	static final String TAG = RegularEmojManager.class.getSimpleName();
	public RegularEmojManager(Context context){
		mContext = context;
	}
	public void clear(){
		AppConfig.setCommonUse(mContext, null);
	}
	/**
	 * 获取最近使用的表情
	 * @return
	 */
	public JSONArray get(){
		JSONArray array = null;
		String json = AppConfig.getCommonUse(mContext);
		if(json == null){
			return null;
		}
		try {
			array = new JSONArray(json);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return sort(array);
	}
	/**
	 * 添加到常用表情
	 * @param filename
	 * @param parms
	 * @throws JSONException
	 */
	public void add(String filename,String parms) throws JSONException{
		JSONObject item = new JSONObject();
		item.put("name", filename);
		item.put("p", parms);
		item.put("time", System.currentTimeMillis());
		JSONArray array;
		String json = AppConfig.getCommonUse(mContext);
		if(json == null){
			array = new JSONArray();
		}else {
			array = new JSONArray(json);
		}
		put(array, item);
		sort(array);
		if(array.length() > MAX){
			array = getArray(array, MAX);
		}
		Log.d(TAG, "array:"+array.toString());
		AppConfig.setCommonUse(mContext, array.toString());
	}
	private void put(JSONArray array,JSONObject object){
		int j = -1;
		for(int i=0;i<array.length();i++){
			try {
				JSONObject item = array.getJSONObject(i);
				if(item.getString("name").equals(object.getString("name"))){
					j = i;
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		if(j != -1){
			try {
				array.put(j, object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}	
		}else {
			array.put(object);
		}
	}
	private JSONArray getArray(JSONArray array,int length) throws JSONException{
		JSONArray temp = null;
		if(length >= array.length()){
			return array;
		}else {
			temp = new JSONArray();
			for(int i =0;i<array.length();i++){
				temp.put(array.getJSONObject(i));
				if(i >= length - 1){
					return temp;
				}
			}
		}
		return null;
		
	}
	public JSONArray sort(JSONArray array){
		if(array == null){
			return null;
		}
		for(int i=0;i<array.length();i++){
			for(int j=i+1;j<array.length();j++){
				try {
					JSONObject item_i = array.getJSONObject(i);
					JSONObject item_j = array.getJSONObject(j);
					long t_i = item_i.getLong("time");
					long t_j = item_j.getLong("time");
					Date d_i = new Date(t_i);
					Date d_j = new Date(t_j);
					if(d_j.after(d_i)){
						array.put(i, item_j);
						array.put(j, item_i);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e(TAG, e.toString());
				}
				
			}
		}
		return array;
	}
	
	
}
