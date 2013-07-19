package com.dabing.emoj.demo;

import java.lang.reflect.Field;

import com.dabing.emoj.R;
import com.dabing.emoj.widget.CrystalButton;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

public class demoshow extends Activity {
	CrystalButton btn1,btn2,btn3,btn4;
	static final String TAG = demoshow.class.getSimpleName();
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_show);
		btn1 = (CrystalButton) findViewById(R.id.cr1);
		btn2 = (CrystalButton) findViewById(R.id.cr2);
		btn3 = (CrystalButton) findViewById(R.id.cr3);
		btn4 = (CrystalButton) findViewById(R.id.cr4);
		
		init();
	}
	
	public void init(){
		Drawable d1 = getIcon("emoj001");
		Drawable d2 = getIcon("emoj002");
		Drawable d3 = getIcon("emoj003");
		btn1.setImageDrawable(d1);
		btn2.setImageDrawable(d2);
		btn3.setImageDrawable(d3);
		btn4.setImageDrawable(d3);
		
		btn2.setWidth(60);
		btn3.setWidth(100);
		btn4.setWidth(120);
	}
	
	private Drawable getIcon(String res){
		Field field;
		try {
			field = R.drawable.class.getDeclaredField(res);
			int resourceId = Integer.parseInt(field.get(null).toString());
			Drawable d = getResources().getDrawable(resourceId);
			d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
			return d;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		return null;
	} 
}
