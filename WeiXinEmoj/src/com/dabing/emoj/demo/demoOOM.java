package com.dabing.emoj.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.ant.liao.GifView;
import com.dabing.emoj.BaseActivity;
import com.dabing.emoj.R;
import com.dabing.emoj.utils.AppConfig;

public class demoOOM extends BaseActivity {
	GifView gifView;
	static final String TAG = demoOOM.class.getSimpleName();
	/* (non-Javadoc)
	 * @see com.dabing.emoj.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gifView = (GifView) findViewById(R.id.gifview);
		Bind();
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.demo_oom;
	}
	
	private void Bind(){
		String path = Environment.getExternalStorageDirectory() + File.separator + "oom.GIF";
		File file = new File(path);
		try {
			InputStream is = new FileInputStream(file);
			gifView.setCahceImage(AppConfig.getCache(), "oom");
		
			gifView.setGifImage(is);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		gifView.destroy();
	}

}
