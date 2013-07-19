package com.dabing.emoj.fragment;

import org.json.JSONObject;

import android.support.v4.app.Fragment;

public class BaseEmojFragment extends Fragment {
	protected JSONObject mObject;
	public BaseEmojFragment(){
		this(null);
	}
	public BaseEmojFragment(JSONObject obj){
		mObject = obj;
	}
}
