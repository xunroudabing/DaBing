package com.dabing.emoj.widget;


import com.dabing.emoj.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class NormalProgressDialog extends Dialog {
	TextView messageTextView;
	public NormalProgressDialog(Context context) {
		super(context,R.style.MyDialog);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.progress_dialog);
		messageTextView = (TextView) findViewById(R.id.progress_dialog_message);
	}
	/**
	 * 设置提示 默认为：加载中...
	 * @param msg
	 */
	public void setMessage(CharSequence msg){
		messageTextView.setText(msg);
	}
}
