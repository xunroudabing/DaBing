package com.dabing.emoj.widget;

import com.dabing.emoj.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class PromptDialog extends Dialog implements android.view.View.OnClickListener {
	ImageView imageView;
	public PromptDialog(Context context) {
		// TODO Auto-generated constructor stub
		super(context,R.style.PromptDialog);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.prompt);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		imageView = (ImageView) findViewById(R.id.prompt_img);
		imageView.setOnClickListener(this);
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismiss();
	}
	
}
