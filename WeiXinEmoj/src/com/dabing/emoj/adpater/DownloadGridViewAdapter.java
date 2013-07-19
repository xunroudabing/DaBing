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
import android.view.WindowManager;
import android.widget.BaseAdapter;

public class DownloadGridViewAdapter extends BaseAdapter {
	IEmojItemClickListener listener;
	Context mContext;
	JSONArray mData;
	int colums = 4;
	int space = 15;
	int mWdith = 0;
	static final String TAG = DownloadGridViewAdapter.class.getSimpleName();
	public DownloadGridViewAdapter(Context context,JSONArray data){
		mContext = context;
		mData = data;
		final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screen_width = windowManager.getDefaultDisplay().getWidth();
		int width = (screen_width - space*(colums + 1)) / colums;
		mWdith = width;
	}
	public void setIEmojItemClickListener(IEmojItemClickListener l){
		listener = l;
	}
	private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.download_gridview_item, parent, false);
		}else {
			root = convertView;
		}
		CrystalButton button = (CrystalButton) root.findViewById(R.id.download_gridview_item_crystalButton);
		button.setWidth(mWdith);
		final JSONObject obj = mData.getJSONObject(position);
		String id = obj.getString("id");
		String name = obj.getString("t");
		String resId = "emoj"+id.toString();
		Drawable drawable = getIcon(resId);
		button.setImageDrawable(drawable);
		button.setID(id);
		button.setJson(obj);
		button.setIEmojItemClickListener(listener);
		if(AppConfig.getIsDownload(mContext, id))
		{
			button.setBanner(R.drawable.hasdownload);
		}else {
			button.setBanner(null);
		}
		return root;
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
	public JSONObject getJsonObject(int position) throws JSONException{
		return mData.getJSONObject(position);
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
