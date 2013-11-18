package com.dabing.emoj.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;
/**
 * 常见问题
 * @author DaBing
 *
 */
public class SettingProblemActivity extends BaseActivity {
	SettingProblemAdapter mAdapter;
	ListView mListView;
	static final String TAG = SettingProblemActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setMMTitle("常见问题");
		setBackBtn(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mListView = (ListView) findViewById(R.id.setting_problem_listview);
		BindListView();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.setting_problem_v2;
	}
	
	protected void BindListView(){
		try {
			JSONArray array = new JSONArray(AppConfig.getProblem(getApplicationContext()));
			mAdapter = new SettingProblemAdapter(array);
			mListView.setAdapter(mAdapter);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
	}
	class SettingProblemAdapter extends BaseAdapter{
		JSONArray mArray;
		public SettingProblemAdapter(JSONArray array){
			mArray = array;
		}
		
		protected View makeView(int arg0, View arg1, ViewGroup arg2) throws JSONException{
			View root = null;
			if(arg1 == null){
				root = LayoutInflater.from(getApplicationContext()).inflate(R.layout.settring_problem_item, arg2, false);						
			}else {
				root = arg1;
			}
			TextView t1 = (TextView) root.findViewById(R.id.setting_problem_t1);
			TextView t2 = (TextView) root.findViewById(R.id.setting_problem_t2);
			JSONObject obj = mArray.getJSONObject(arg0);
			String problem = String.format("%d.%s", arg0+1,obj.getString("p"));
			String answer = String.format("答:%s", obj.getString("a"));
			t1.setText(problem);
			t2.setText(answer);
			return root;
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
