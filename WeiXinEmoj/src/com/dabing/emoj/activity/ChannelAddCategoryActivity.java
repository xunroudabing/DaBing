package com.dabing.emoj.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.bonus.IBouns;
import com.dabing.emoj.bonus.WAPS_Bonus;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.ChannelListItem;
import com.tencent.mm.sdk.uikit.MMBaseActivity;

public class ChannelAddCategoryActivity extends MMBaseActivity {
	IBouns mBouns;
	ChannelCategoryAdpater mAdpater;
	GridView mGridView;
	RadioGroup header;
	static final String TAG = ChannelAddCategoryActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		header = (RadioGroup) findViewById(R.id.channel_add_category_head);
		mGridView = (GridView) findViewById(R.id.channel_add_category_gridview);
		mBouns = new WAPS_Bonus(ChannelAddCategoryActivity.this);
		BindHeader();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.channel_add_category;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mBouns.reflesh();
	}
	protected void BindHeader(){
		try {
			String json = AppConfig.getChannelCategory(getApplicationContext());
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String name = obj.getString("n");
				int id = obj.getInt("id");
				int childsize = 0;
				JSONArray info = obj.getJSONArray("info");
				if(info != null){
					childsize = info.length();
				}
				String title = String.format("%s(%d)", name,childsize);
				Log.d(TAG, "childsize:"+childsize);
				String btnid = String.format("channel_header_radio_%d", i+1);
				int resId = getResources().getIdentifier(btnid, "id", getPackageName());
				RadioButton rd = (RadioButton) LayoutInflater.from(getApplicationContext()).inflate(R.layout.channel_category_item,header,false);
				rd.setId(resId);
				rd.setText(title);
				rd.setTag(id);
				rd.setOnCheckedChangeListener(onHeaderChangeListener);
				header.addView(rd);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
	protected JSONArray getJsonArray(int id){
		String json = AppConfig.getChannelCategory(getApplicationContext());
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				int _id = obj.getInt("id");
				if(_id == id){
					JSONArray info = obj.getJSONArray("info");
					return info;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		return null;
		
	}
	protected void BindGridView(int id){
		JSONArray array = getJsonArray(id);
		mAdpater = new ChannelCategoryAdpater(array);
		mGridView.setAdapter(mAdpater);
	}
	
	// 点击header时触发
	private OnCheckedChangeListener onHeaderChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				int id = (Integer) buttonView.getTag();
				Log.d(TAG, "onCheckedChanged:" + id);
				BindGridView(id);
			}
		}
	};
	
	//
	class ChannelCategoryAdpater extends BaseAdapter{
		JSONArray mArray;
		int mWidth = 80;
		int COLUM_NUM = 2;
		
		static final int COLUM_PADDING = 10;
		public ChannelCategoryAdpater(JSONArray array){
			mArray = array;
			calculateAlbumWidth();
		}
		private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
			ChannelListItem root = null;
			if(convertView == null){
				root = new ChannelListItem(ChannelAddCategoryActivity.this);
			}else {
				root = (ChannelListItem) convertView;
				root.clear();
			}
			JSONObject obj = mArray.getJSONObject(position);
			String name = obj.getString("n");
			int id = obj.getInt("id");
			int bonus = 0;
			if(obj.has("b")){
				bonus = obj.getInt("b");
			}
			root.setWidth(mWidth);
			root.setTitle(name);
			root.setBonus(bonus);
			root.setChannelID(String.valueOf(id));
			return root;
		}
		// 计算相册宽度
		private void calculateAlbumWidth() {
			WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			//int screenWidth = windowManager.getDefaultDisplay().getWidth();
			windowManager.getDefaultDisplay().getMetrics(dm);
			int screenWidth = (int)(dm.widthPixels / dm.density);
			mWidth = (screenWidth - (COLUM_NUM - 1) * COLUM_PADDING)
					/ COLUM_NUM;
			
			Log.d(TAG, "width:" + mWidth + " screenWidth:"+screenWidth + " density:"+dm.density);

		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.length();
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
				Log.e(TAG, e.toString());
			}
			return null;
		}
		
	}
}
