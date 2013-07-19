package com.dabing.emoj.adpater;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.widget.CrystalButton;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CrystalHeaderGridViewAdapter extends BaseAdapter {
	IEmojItemClickListener listener;
	int mWidth = 0;
	Context mContext;
	JSONArray mData;
	static final String TAG = CrystalHeaderGridViewAdapter.class.getSimpleName();
	public CrystalHeaderGridViewAdapter(Context c,JSONArray array,int width){
		mContext =c;
		mData = array;
		mWidth = width;
	}
	public void setIEmojItemClickListener(IEmojItemClickListener l){
		listener = l;
	}
	private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.header_fragment_gridview_crystal_item, parent, false);			
		}else {
			root = convertView;
		}
		CrystalButton crystalButton = (CrystalButton) root.findViewById(R.id.header_fragment_gridview_item_cr1);
		final JSONObject obj = mData.getJSONObject(position);
		String id = obj.getString("id");
		String name = obj.getString("t");
		String background = obj.getString("b");
		crystalButton.setWidth(mWidth);
		String resId = "emoj"+id.toString();
		Drawable drawable = getIcon(resId);
		crystalButton.setImageDrawable(drawable);
		crystalButton.setID(id);
		crystalButton.setJson(obj);
		if(AppConfig.isNewEmoj(mContext, id)){
			crystalButton.setIcon(R.drawable.alert_new);
		}else {
			crystalButton.setIconVisibility(View.GONE);
		}
		crystalButton.setIEmojItemClickListener(listener);
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
