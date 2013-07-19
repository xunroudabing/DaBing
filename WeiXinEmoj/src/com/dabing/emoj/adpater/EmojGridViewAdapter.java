package com.dabing.emoj.adpater;

import greendroid.image.ImageProcessor;
import greendroid.image.ScaleImageProcessor;
import greendroid.widget.AsyncImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.EmojImageView;
import com.dabing.emoj.widget.WrapperImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
/**
 * 趣图adapter
 * @author DaBing
 *
 */
public class EmojGridViewAdapter extends BaseAdapter {
	ImageProcessor processor;
	Context mContext;
	JSONArray mData;
	int mSpacing = 2;
	int mWidth;
	static final String TAG = EmojGridViewAdapter.class.getSimpleName();
	public EmojGridViewAdapter(Context context,JSONArray array,int columNum){
		mContext = context;
		mData = array;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = manager.getDefaultDisplay().getWidth();
		Log.d(TAG, "screenWidth:"+screenWidth);
		//图片宽度
		mWidth = (int) ((screenWidth - (columNum + 1)*mSpacing) / columNum);
		Log.d(TAG, "mWidth:"+mWidth);
		processor = new ScaleImageProcessor(mWidth, mWidth, ScaleType.CENTER_CROP);
	}
	private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.wrapper_item_nopadding, parent, false);
		}else {
			root = convertView;
		}
		WrapperImageView imageView = (WrapperImageView) root;
		imageView.setWidth(mWidth);
		imageView.setHeight(mWidth);
		JSONObject object = mData.getJSONObject(position);
		//Log.d(TAG, object.toString());
		String url = "";
		if(!object.isNull("image") && !object.get("image").equals("null")){
			String json = object.getString("image");
			//Log.d(TAG, "json:"+json);
			//微频道
//			{
//                "info": [{
//                    "pic_XDPI": [96],
//                    "pic_YDPI": [96],
//                    "pic_height": [141],
//                    "pic_size": [1472044],
//                    "pic_width": [240],
//                    "url": ["http:\/\/app.qpic.cn\/mblogpic\/2cbcc4ca5dc8377ff244"]
//                }]
//            }
			if(json.startsWith("{")){
				if(object.getJSONObject("image").has("info")){
					JSONArray info = object.getJSONObject("image").getJSONArray("info");
					JSONObject item = info.getJSONObject(0);
					url = item.getJSONArray("url").getString(0);
					//Log.d(TAG, "info.url:"+url);
				}
			}else {
				//一般时间线
				url = object.getJSONArray("image").getString(0);
			}
			
		}	
		if(!url.equals("")){
			//Log.d(TAG, "url:"+url);
			imageView.setImageProcessor(processor);
			imageView.setUrl(url+AppConstant.PIC_ITEM_SMALL_PREFIX);
		}
		return root;
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
	public void refresh(JSONArray array){
		for(int i=0;i<array.length();i++){			
			try {
				mData.put(array.getJSONObject(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		notifyDataSetChanged();
	}
	public JSONObject getData(int position) throws JSONException{
		return mData.getJSONObject(position);
	}
	public JSONArray getArray(){
		return mData;
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
