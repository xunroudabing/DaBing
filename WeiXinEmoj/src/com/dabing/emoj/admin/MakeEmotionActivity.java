package com.dabing.emoj.admin;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.exception.DBLog;
import com.dabing.emoj.utils.AppConfig;
import com.dabing.emoj.utils.AppConstant;

public class MakeEmotionActivity extends BaseActivity {
	Button btn;
	static final String TAG = MakeEmotionActivity.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		btn = (Button) findViewById(R.id.admin_make_emotion_btn1);
		btn.setOnClickListener(onClickListener);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.admin_make_emotion;
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				String json = AppConstant.EMOJ_EMOTION_INDEX;
				JSONArray array = new JSONArray(json);
				for(int i=0;i<array.length();i++){
					JSONObject object = array.getJSONObject(i);
					String t = object.getString("t");
					String id = object.getString("id");
					String message = AppConfig.getEmoj(getApplicationContext(), id);					
					if(message != null && !message.equals("")){
						String msg = String.format("static final String %s = \"%s\"", id,message.replace('"', '\"'));
						DBLog.d("emotion", t + "  " + id, msg );
					}
					
				}
				Toast.makeText(MakeEmotionActivity.this, "生成成功", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
}
