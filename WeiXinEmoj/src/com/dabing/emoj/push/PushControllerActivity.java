package com.dabing.emoj.push;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dabing.emoj.R;
import com.tencent.mm.sdk.uikit.MMBaseActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

public class PushControllerActivity extends MMBaseActivity {
	Sender mSender;
	Button btnSend;
	EditText topicText, aliasText, dataText;
	static final String TAG = PushControllerActivity.class.getSimpleName();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		topicText = (EditText) findViewById(R.id.push_edit_topic);
		aliasText = (EditText) findViewById(R.id.push_edit_alias);
		dataText = (EditText) findViewById(R.id.push_edit_json);
		btnSend = (Button) findViewById(R.id.push_btnsend);
		btnSend.setOnClickListener(btnSendListener);
		Init();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.push_controller_activity;
	}

	protected void Init() {
		mSender = new Sender("Ti0G3OHSRMWJwRecxvomt7KuSR8fEI9qe+XZ9Cz4JjQ=");
	}

	protected void sendMessageByAlias() {
		
		Constants.useOfficial();
		String alias = aliasText.getText().toString();
		String data = dataText.getText().toString();
		String title = "this is a title";
		String des = "这是一段描述";

		Message msg = new Message.Builder().title(title).description(des)
				.payload(data).restrictedPackageName("com.dabing.emoj").notifyType(1).build();
		
		try {
			mSender.sendToAlias(msg, alias, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}

	}
	
	protected void sendMessageByTopic(){
		Constants.useOfficial();
		String topic = topicText.getText().toString();
		String data = dataText.getText().toString();
		String title = "this is a title";
		String des = "这是一段描述";

		Message msg = new Message.Builder().title(title).description(des)
				.payload(data).restrictedPackageName("com.dabing.emoj").notifyType(1).build();
		
		try {
			Result result = mSender.broadcast(msg, topic, 3);
			com.xiaomi.push.sdk.ErrorCode code = result.getErrorCode();
			JSONObject object = result.getData();
			String reason = result.getReason();
			if(code != null){
				Log.d(TAG, "errorcode:"+code.getFullDescription());
			}
			if(object != null){
				Log.d(TAG, "data:"+object.toJSONString());
			}
			if(reason != null){
				Log.d(TAG, "reason:"+reason);
			}
			//Log.d(TAG, "result error:"+code.toString()+" data:"+object.toJSONString() + " reason:"+reason);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	private OnClickListener btnSendListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//sendMessageByAlias();
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					sendMessageByTopic();
				}
			});
			t.start();
		}
	};

}
