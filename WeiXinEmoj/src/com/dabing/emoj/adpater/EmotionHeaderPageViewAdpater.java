package com.dabing.emoj.adpater;

import greendroid.widget.PagedAdapter;

import java.util.List;

import org.json.JSONArray;

import com.dabing.emoj.R;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;
import com.dabing.emoj.widget.EmotionPanel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class EmotionHeaderPageViewAdpater extends PagedAdapter {
	IEmojItemClickListener listener;
	Context mContext;
	List<JSONArray> mData;
	static final String TAG = EmotionHeaderPageViewAdpater.class.getSimpleName();
	public EmotionHeaderPageViewAdpater(Context c,List<JSONArray> array){
		mContext = c;
		mData = array;
	}
	public void setItemClickListener(IEmojItemClickListener l){
		listener = l;
	}
	private View makeView(int position, View convertView, ViewGroup parent){
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.header_fragment_emotionpanel, parent, false);
		}else {
			root = convertView;
		}
		EmotionPanel panel = (EmotionPanel) root.findViewById(R.id.header_fragment_panel);
		JSONArray array = mData.get(position);
		EmotionHeaderPanelAdapter adapter = new EmotionHeaderPanelAdapter(mContext, array);
		if(listener != null){
			adapter.setItemClickListener(listener);
		}
		panel.setAdapter(adapter);
		return root;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			return makeView(position, convertView, parent);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
			return convertView;
		}
	}

}
