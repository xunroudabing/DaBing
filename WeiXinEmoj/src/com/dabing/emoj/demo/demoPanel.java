package com.dabing.emoj.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;
import com.dabing.emoj.widget.ColorButton;
import com.dabing.emoj.widget.EmotionPanel;

public class demoPanel extends BaseActivity {
	EmotionPanel panel;
	static final String TAG = demoPanel.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		panel = (EmotionPanel) findViewById(R.id.demo_panel1);
		Bind();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.demo_panel;
	}
	private void Bind(){
		try {
			String json = AppConstant.EMOJ_EMOTION_INDEX;
			JSONArray array = new JSONArray(json);
			EmotionAdpater adpater = new EmotionAdpater(array);
			panel.setAdapter(adpater);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	class EmotionAdpater extends BaseAdapter{
		JSONArray mData;
		public EmotionAdpater(JSONArray array){
			mData = array;
		}
		private View makeView(int position, View convertView, ViewGroup parent) throws JSONException{
			ColorButton root = (ColorButton) LayoutInflater.from(getApplicationContext()).inflate(R.layout.header_fragment_emotionpanel_item, parent, false);
			
			JSONObject object = mData.getJSONObject(position);
			String text = object.getString("t");
			root.setText(text);
			root.setTextBackgroundResource(R.drawable.textview_orange_selector);
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
