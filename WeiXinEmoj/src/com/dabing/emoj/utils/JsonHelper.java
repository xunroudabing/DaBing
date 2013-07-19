package com.dabing.emoj.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JsonHelper {
	static final String TAG = JsonHelper.class.getSimpleName();
	public JsonHelper(){
		
	}
	/**
	 * 过滤重复的图片
	 * @param array
	 */
	public JSONArray Filter(JSONArray array){
		JSONArray newArray = new JSONArray();
		List<String> list = new ArrayList<String>();
		for(int i=0;i<array.length();i++){
			try {
				JSONObject item = array.getJSONObject(i);
				if(!item.isNull("image")){
					String json = item.getString("image");
					String image = null;
					if(json.startsWith("{")){
						if(item.getJSONObject("image").has("info")){
							JSONArray info = item.getJSONObject("image").getJSONArray("info");
							JSONObject o = info.getJSONObject(0);
							image = o.getJSONArray("url").getString(0);
						}
					}else {
						image = item.getJSONArray("image").getString(0);
					}
					if(image != null){
						if(!list.contains(image)){
							list.add(image);
							newArray.put(item);
						}
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
		//Log.d(TAG, "before filter:"+array.length() + " after filter:"+newArray.length());
		return newArray;
	}
}
