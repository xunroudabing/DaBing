package com.dabing.emoj.utils;



import com.dabing.emoj.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DialogFactory {
	public static Dialog creatRequestDialog(final Context context, String tip){
		
		final Dialog dialog = new Dialog(context, R.style.dialog);	
		dialog.setContentView(R.layout.dialog_layout);	
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = Utils.getScreenWidth(context);	
		lp.width = (int)(0.6 * width);	
		lp.height = (int)(0.6 * width);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);
		if (tip == null || tip.length() == 0)
		{
			titleTxtv.setText(R.string.sending_request);	
		}else{
			titleTxtv.setText(tip);	
		}
		
		return dialog;
	}
	
	public static Dialog createSuccessfulDialog(final Context context,String txt,final OnClickListener l){
		final Dialog dialog = new Dialog(context, R.style.mmalertdialog);
		dialog.setContentView(R.layout.confirm_dialog_item5);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = Utils.getScreenWidth(context);	
		lp.width = (int)(0.9 * width);	
		TextView txtView = (TextView) dialog.findViewById(R.id.confirm_dialog_message_tv);
		Button btn = (Button) dialog.findViewById(R.id.confirm_dialog_btn1);
		if(txt != null){
			txtView.setText(txt);
		}
		btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(l != null){
					l.onClick(dialog, 1);
				}
			}
		});
		return dialog;
	}
	
	public static Dialog createFailDialog(final Context context,String txt,final OnClickListener l){
		final Dialog dialog = new Dialog(context, R.style.mmalertdialog);
		dialog.setContentView(R.layout.confirm_dialog_item7);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = Utils.getScreenWidth(context);	
		lp.width = (int)(0.98 * width);
		TextView txtView = (TextView) dialog.findViewById(R.id.confirm_dialog_message_tv);
		Button btn = (Button) dialog.findViewById(R.id.confirm_dialog_btn1);
		if(txt != null){
			txtView.setText(txt);
		}else {
			txtView.setText("请求失败,请重试...");
		}
		btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(l != null){
					l.onClick(dialog, 1);
				}
			}
		});
		return dialog;
	}
	
	public static Dialog createCommonDialog(final Context context,String txt,final OnClickListener l){
		return createCommonDialog(context, txt, null, l);
	}
	public static Dialog createCommonDialog(final Context context,String txt,String button,final OnClickListener l){
		final Dialog dialog = new Dialog(context, R.style.mmalertdialog);
		dialog.setContentView(R.layout.confirm_dialog_item8);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = Utils.getScreenWidth(context);	
		lp.width = (int)(0.9 * width);
		TextView txtView = (TextView) dialog.findViewById(R.id.confirm_dialog_message_tv);
		Button btn = (Button) dialog.findViewById(R.id.confirm_dialog_btn1);
		if(txt != null){
			txtView.setText(txt);
		}else {
			txtView.setText("加载中...");
		}
		if(button != null && !button.equals("")){
			btn.setText(button);
		}
		btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(l != null){
					l.onClick(dialog, 1);
				}
			}
		});
		return dialog;
	}
	
	public static Dialog createTwoButtonDialog(final Context context,String txt,String button1,String button2,final OnClickListener l1,final OnClickListener l2){
		final Dialog dialog = new Dialog(context, R.style.mmalertdialog);
		dialog.setContentView(R.layout.confirm_dialog_item2);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = Utils.getScreenWidth(context);	
		lp.width = (int)(0.9 * width);
		TextView txtView = (TextView) dialog.findViewById(R.id.confirm_dialog_message_tv);
		Button btn = (Button) dialog.findViewById(R.id.confirm_dialog_btn1);
		Button btn2 = (Button) dialog.findViewById(R.id.confirm_dialog_btn2);
		if(txt != null){
			txtView.setText(txt);
		}
		if(button1 != null && !button1.equals("")){
			btn.setText(button1);
		}
		if(button2 != null && !button2.equals("")){
			btn2.setText(button2);
		}
		btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(l1 != null){
					l1.onClick(dialog, 1);
				}
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(l2 != null){
					l2.onClick(dialog, 0);
				}
			}
		});
		return dialog;
	}
	
	public static Dialog createTextTransparentBack(Context context,String text,OnCancelListener listener){
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_transparent_layout, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final TextView txtView = (TextView) layout.findViewById(R.id.alert_dialog_transparent_txt);
		txtView.setText(text);
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (listener != null) {
			dlg.setOnCancelListener(listener);
		}
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}
}
