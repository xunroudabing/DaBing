package com.dabing.emoj.widget;

import com.dabing.emoj.R;
import com.dabing.emoj.utils.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 下载进度提示dialog
 * @author DaBing
 *
 */
public class DownloadProgressDialog extends Dialog {
	TextView titleView;
	ProgressBar progressBar;
	TextView rateView;
	Button btn;
	public DownloadProgressDialog(Context context) {
		super(context, R.style.mmalertdialog);
		// TODO Auto-generated constructor stub
		setContentView(R.layout.confirm_dialog_item9);
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = Utils.getScreenWidth(context);	
		lp.width = (int)(0.9 * width);
		
		titleView = (TextView) findViewById(R.id.confirm_progress_title);
		progressBar = (ProgressBar) findViewById(R.id.confirm_progressbar);
		rateView = (TextView) findViewById(R.id.confirm_progress_rate);
		btn = (Button) findViewById(R.id.confirm_dialog_btn1);
	}
	
	public void setTitle(CharSequence title){
		titleView.setText(title);
	}
	public void setRate(int rate){
		progressBar.setProgress(rate);
		String msg = String.format("%d%%", rate);
		rateView.setText(msg);
	}
	public void setButtonClickListener(android.view.View.OnClickListener l){
		btn.setOnClickListener(l);
	}
}
