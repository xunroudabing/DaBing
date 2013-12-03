package com.dabing.emoj.push;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.db.PushEmojDatabaseHelper;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.utils.QStr;
import com.dabing.emoj.widget.CacheWrapperImageView;
/**
 * 接收到的表情
 * @author Administrator
 *
 */
public class ReceivedEmojListActivity extends BaseActivity {

	MyEmojAdapter mAdapter;
	PushEmojDatabaseHelper mHelper;
	ListView mListView;
	ImageView noImageView;
	static final String TAG = ReceivedEmojListActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle(R.string.title_receive_emoj);
		noImageView = (ImageView) findViewById(R.id.push_emoj_list_nodata);
		mListView = (ListView) findViewById(R.id.push_emoj_list_listview);
		mHelper = new PushEmojDatabaseHelper(getApplicationContext());
		mListView.setOnItemClickListener(itemClickListener);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.push_emoj_list;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BindListView();
	}
	protected void BindListView(){
		Cursor cursor = null;
		try {
			cursor = mHelper.getCursor();
			if(cursor != null){
				if(cursor.getCount() > 0){
					JSONArray array = new JSONArray();
					cursor.moveToFirst();
					do {
						String emojid = cursor.getString(cursor.getColumnIndexOrThrow(PushEmojDatabaseHelper.FIELD_EMOJID));
						String name = cursor.getString(cursor.getColumnIndexOrThrow(PushEmojDatabaseHelper.FIELD_NAME));
						String thumb = cursor.getString(cursor.getColumnIndexOrThrow(PushEmojDatabaseHelper.FIELD_THUMB));
						String des = cursor.getString(cursor.getColumnIndexOrThrow(PushEmojDatabaseHelper.FIELD_DES));
						int read = cursor.getInt(cursor.getColumnIndexOrThrow(PushEmojDatabaseHelper.FIELD_READ));
						long millsec = cursor.getLong(cursor.getColumnIndexOrThrow(PushEmojDatabaseHelper.FIELD_TIME));
						
						JSONObject obj = new JSONObject();
						obj.put("id", emojid);
						obj.put("name", name);
						obj.put("thumb", thumb);
						obj.put("des", des);
						obj.put("read", read);
						obj.put("time", millsec);
						
						array.put(obj);
					} while (cursor.moveToNext());
					
					mAdapter = new MyEmojAdapter(array);
					mListView.setAdapter(mAdapter);
					
					noImageView.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
				}else {
					Log.d(TAG, "no data..");
					noImageView.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.GONE);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			try {
				String emojId = mAdapter.getEmojId(arg2);
				Log.d(TAG, "emojid:"+emojId);
				Intent intent = new Intent(getApplicationContext(), PushEmojActivity.class);
				intent.putExtra(AppConstant.INTENT_PUSH_EMOJID, emojId);
				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
	
	class MyEmojAdapter extends BaseAdapter{
		JSONArray mArray;
		public MyEmojAdapter(JSONArray array){
			mArray = array;
		}
		protected View makeView(int arg0, View arg1, ViewGroup arg2) throws JSONException{
			View root = null;
			if(arg1 == null){
				root = LayoutInflater.from(getApplicationContext()).inflate(R.layout.push_emoj_list_item, arg2, false);
			}else {
				root = arg1;
			}
			
			CacheWrapperImageView thumbView = (CacheWrapperImageView) root.findViewById(R.id.push_emoj_list_item_img);
			TextView nameView = (TextView) root.findViewById(R.id.push_emoj_list_item_txt1);
			TextView desView = (TextView) root.findViewById(R.id.push_emoj_list_item_txt2);
			TextView timeView = (TextView) root.findViewById(R.id.push_emoj_list_item_time);
			ImageView readView = (ImageView) root.findViewById(R.id.push_emoj_list_item_imgNew);
			JSONObject obj = mArray.getJSONObject(arg0);
			String id = obj.getString("id");
			String name = obj.getString("name");
			String thumb = obj.getString("thumb");
			String des = obj.getString("des");
			int read = obj.getInt("read");
			long millsec = obj.getLong("time");			
			String time = QStr.fixTime(String.valueOf(millsec/1000));
			
			thumbView.setWidth(100);
			thumbView.setImage(AppConstant.PIC_SERVER_URL + thumb + AppConstant.PIC_ITEM_FULL_PREFIX);
			nameView.setText(name);
			desView.setText(des);
			timeView.setText(time);
			if(read == 0){
				readView.setVisibility(View.VISIBLE);
			}else {
				readView.setVisibility(View.GONE);
			}
			return root;
		}
		
		public String getEmojId(int position){
			try {
				JSONObject object = mArray.getJSONObject(position);
				String id = object.getString("id");
				return id;
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			return null;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.length();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			try {
				return makeView(arg0, arg1, arg2);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			return null;
		}
		
	}

}
