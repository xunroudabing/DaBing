package com.dabing.emoj.adpater;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dabing.emoj.R;
import com.dabing.emoj.fragment.HeaderFragment.IEmojItemClickListener;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import greendroid.widget.PagedAdapter;

public class CrystalHeaderPageViewAdpater extends PagedAdapter {
	int mWidth = 0;
	int colums = 4;
	int space = 15;
	IEmojItemClickListener listener;
	List<JSONArray> mData;
	Context mContext;
	static final String TAG = CrystalHeaderPageViewAdpater.class.getSimpleName();
	public CrystalHeaderPageViewAdpater(Context c,List<JSONArray> array){
		mContext =c;
		mData = array;
		final WindowManager windowManager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		int screen_width = windowManager.getDefaultDisplay().getWidth();
		int screen_height = windowManager.getDefaultDisplay().getHeight();
		int width = (screen_width - space*(colums + 1)) / colums;
		int height = screen_height / 10;
		Log.d(TAG, "width:"+width + " height:"+height);
		mWidth = width;
		Log.d(TAG, "mWidth:"+mWidth);
	}
	public void setOnItemClickListener(IEmojItemClickListener l){
		listener = l;
	}
	private View makeView(int position, View convertView, ViewGroup parent){
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.header_fragment_gridview, parent, false);	
		}else {
			root = convertView;
		}
		GridView gridView = (GridView) root.findViewById(R.id.header_gridview);
		JSONArray array = mData.get(position);
		final CrystalHeaderGridViewAdapter adapter = new CrystalHeaderGridViewAdapter(mContext, array,mWidth);
		adapter.setIEmojItemClickListener(listener);
		gridView.setAdapter(adapter);
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				JSONObject obj = adapter.getData(position);
//				if(listener != null){
//					listener.onItemClick(view,obj);
//				}
//			}
//		});		
		return root;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			return makeView(position, convertView, parent);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "111"+e.toString());
			return convertView;
		}
	}
}
