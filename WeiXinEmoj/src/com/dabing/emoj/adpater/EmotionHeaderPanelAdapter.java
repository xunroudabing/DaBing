package com.dabing.emoj.adpater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.ColorButton;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

public class EmotionHeaderPanelAdapter extends BaseAdapter {
	IEmojItemClickListener listener;
	Context mContext;
	JSONArray mData;
	static final String TAG = EmotionHeaderPanelAdapter.class.getSimpleName();
	public EmotionHeaderPanelAdapter(Context c,JSONArray array){
		mContext = c;
		mData = array;
	}
	public void setItemClickListener(IEmojItemClickListener l){
		listener = l;
	}
	private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
		ColorButton button = (ColorButton) LayoutInflater.from(mContext).inflate(R.layout.header_fragment_emotionpanel_item, parent, false);
		final JSONObject object = mData.getJSONObject(position);
		String text = object.getString("t");
		String background = object.getString("b");
		String id = object.getString("id");
		int resId = AppConfig.getColor(background);
		button.setID(id);
		//button.setTextBackgroundResource(resId);
		button.setTextColor(resId);
		button.setText(text);		
		button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onItemClick(v, object);
				}
			}
		});
		return button;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.length();
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
		}
		return null;
	}

}
