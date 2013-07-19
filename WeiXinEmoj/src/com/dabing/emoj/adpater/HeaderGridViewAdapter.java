package com.dabing.emoj.adpater;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HeaderGridViewAdapter extends BaseAdapter {
	boolean init = false;
	Context mContext;
	JSONArray mData;
	static final String TAG = HeaderGridViewAdapter.class.getSimpleName();
	public HeaderGridViewAdapter(Context c,JSONArray array){
		mContext =c;
		mData = array;
	}
	private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.header_fragment_gridview_item, parent, false);			
		}else {
			root = convertView;
		}
		TextView textView = (TextView) root.findViewById(R.id.header_txtEmoj);
		//ImageView iconView = (ImageView) root.findViewById(R.id.header_icon);
		ImageView cornerView = (ImageView) root.findViewById(R.id.header_corner);
		JSONObject obj = mData.getJSONObject(position);
		String id = obj.getString("id");
		String name = obj.getString("t");
		String background = obj.getString("b");
		//String type = obj.getString("c");
		//String parms = obj.getString("p");
		String icon = obj.getString("d");
		textView.setText(name);
		textView.setBackgroundDrawable(getBackground(background));
		//设置小标签
		if(icon != null && !icon.equals("")){
			Drawable drawable = getIcon(icon);
			if(drawable != null){
				textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				//iconView.setImageDrawable(drawable);
				//iconView.setVisibility(View.VISIBLE);
				
			}
		}else {
			textView.setCompoundDrawables(null, null, null, null);
			//iconView.setVisibility(View.GONE);
		}
		
		return root;
	}
	private Drawable getBackground(String background){
		Drawable d = mContext.getResources().getDrawable(AppConfig.getBackgroundResId(background));
		return d;
	}
	private Drawable getIcon(String res){
		Field field;
		try {
			field = R.drawable.class.getDeclaredField(res);
			int resourceId = Integer.parseInt(field.get(null).toString());
			Drawable d = mContext.getResources().getDrawable(resourceId);
			d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
			return d;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		return null;
	} 
	public void setInit(boolean b){
		init = b;
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
	public JSONObject getData(int position){
		try {
			return mData.getJSONObject(position);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		return null;
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
