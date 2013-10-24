package com.dabing.emoj.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.tencent.mm.sdk.uikit.MMBaseActivity;

public class ChannelAddCategoryActivity extends MMBaseActivity {
	String json = "[{\"id\":1,\"n\":\"测试1\",\"p\":\"\"},{\"id\":2,\"n\":\"测试1\",\"p\":\"\"},{\"id\":3,\"n\":\"测试1\",\"p\":\"\"},{\"id\":4,\"n\":\"测试1\",\"p\":\"\"},{\"id\":5,\"n\":\"测试1\",\"p\":\"\"},{\"id\":6,\"n\":\"测试1\",\"p\":\"\"},{\"id\":7,\"n\":\"测试1\",\"p\":\"\"},{\"id\":8,\"n\":\"测试1\",\"p\":\"\"},{\"id\":9,\"n\":\"测试1\",\"p\":\"\"},{\"id\":10,\"n\":\"测试1\",\"p\":\"\"}]";
	RadioGroup header;
	static final String TAG = ChannelAddCategoryActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		header = (RadioGroup) findViewById(R.id.channel_add_category_head);
		BindHeader();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.channel_add_category;
	}
	
	protected void BindHeader(){
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String name = obj.getString("n");
				String id = String.format("channel_header_radio_%d", i+1);
				int resId = getResources().getIdentifier(id, "id", getPackageName());
				RadioButton rd = (RadioButton) LayoutInflater.from(getApplicationContext()).inflate(R.layout.channel_add_category_header_item,header,false);
				rd.setId(resId);
				rd.setText(name);
				
				header.addView(rd);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		
	}
}
