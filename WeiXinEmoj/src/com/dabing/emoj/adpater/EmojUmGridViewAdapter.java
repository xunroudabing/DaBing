package com.dabing.emoj.adpater;

import org.json.JSONArray;
import org.json.JSONException;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.CacheWrapperImageView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

public class EmojUmGridViewAdapter extends BaseAdapter {
	//ImageProcessor processor;
	Context mContext;
	JSONArray mData;
	int mSpacing = 2;
	int mWidth;
	static final String TAG = EmojGridViewAdapter.class.getSimpleName();
	public EmojUmGridViewAdapter(Context context,JSONArray array,int columNum){
		mContext = context;
		mData = array;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = manager.getDefaultDisplay().getWidth();
		//Log.d(TAG, "screenWidth:"+screenWidth);
		//图片宽度
		mWidth = (int) ((screenWidth - (columNum + 1)*mSpacing) / columNum);
		//Log.d(TAG, "mWidth:"+mWidth);
		//processor = new ScaleImageProcessor(mWidth, mWidth, ScaleType.CENTER_CROP);
		
	}
	private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.cache_item, parent, false);
		}else {
			root = convertView;
		}
		CacheWrapperImageView imageView = (CacheWrapperImageView) root;
		imageView.setWidth(mWidth);
		String pic = mData.getString(position);
		String url = AppConstant.PIC_SERVER_URL + pic + AppConstant.PIC_ITEM_FULL_PREFIX;
		//Log.d(TAG, "url:"+url);		
		imageView.setImage(url);
		return root;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.length();
	}
	public String getURL(int position){
		try {
			String pic = mData.getString(position);
			String url = AppConstant.PIC_SERVER_URL + pic + AppConstant.PIC_ITEM_FULL_PREFIX;
			return url;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public String getPicName(int position) throws JSONException{
		return mData.getString(position);
	}
	public String getData(){
		return mData.toString();
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
