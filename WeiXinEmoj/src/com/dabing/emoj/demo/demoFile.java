package com.dabing.emoj.demo;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.service.EmojScanService;

public class demoFile extends BaseActivity {
	Messenger mService;
	Button btn1,btn2,btn3;
	TextView t1,t2;
	static final String TAG = demoFile.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		btn1 = (Button) findViewById(R.id.demo_file_btn1);
		btn2 = (Button) findViewById(R.id.demo_file_btn2);
		btn3 = (Button) findViewById(R.id.demo_file_btn3);
		t1 = (TextView) findViewById(R.id.demo_file_t1);
		t2 = (TextView) findViewById(R.id.demo_file_t2);
		
		btn1.setOnClickListener(listener);
		btn2.setOnClickListener(listener);
		btn3.setOnClickListener(listener);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.demo_file_scan;
	}
	private void maketext1(CharSequence txt){
		t1.setText(txt);
	}
	private void maketext2(CharSequence txt){
		t2.setText(t2.getText().toString() + "\r\n" + txt);
	}
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.demo_file_btn1:
				BindService();
				break;
			case R.id.demo_file_btn2:
				scan();
				break;
			case R.id.demo_file_btn3:
				UnBindService();
				break;
			default:
				break;
			}
			BindService();
		}
	};
	private void BindService(){
		Intent intent = new Intent(getApplicationContext(), EmojScanService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	private void UnBindService(){
		if(mService != null){
			try {
				Message msg = Message.obtain(null, EmojScanService.CLIENT_UNREGISTER);
				msg.replyTo = clientMessenger;
				mService.send(msg);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		unbindService(mConnection);
	}
	private void start(){
		if(mService != null){
			
			try {
				Message msg = Message.obtain(null, EmojScanService.CLIENT_START);
				msg.replyTo = clientMessenger;
				mService.send(msg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
	}
	private void scan(){
		if(mService != null){
			try {
				Message msg = Message.obtain(null, EmojScanService.CLIENT_SCAN);
				msg.replyTo = clientMessenger;
				mService.send(msg);
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	}
	final Messenger clientMessenger = new Messenger(new mHandler());
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onServiceDisconnected");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			try {
				Log.d(TAG, "onServiceConnected");
				mService = new Messenger(service);
				Log.d(TAG, "attached");
				Message msg = Message.obtain(null, EmojScanService.CLIENT_REGISTER);
				msg.replyTo = clientMessenger;
				
				mService.send(msg);
				
				//mService.send(Message.obtain(null, EmojScanService.CLIENT_START));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
		}
	};
	class mHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case EmojScanService.CLIENT_SCAN_GET_FILE:
				File file = (File) msg.obj;
				Log.d(TAG, "rec_msg:"+file);
				maketext2(file.toString());
				break;

			default:
				break;
			}
		}
	}
}
