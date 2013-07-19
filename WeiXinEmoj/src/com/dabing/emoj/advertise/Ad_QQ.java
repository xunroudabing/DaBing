package com.dabing.emoj.advertise;

import com.tencent.exmobwin.MobWINManager;
import com.tencent.exmobwin.Type;

import android.content.Context;

public class Ad_QQ implements IAdvertise {

	public void init(Context context) {
		// TODO Auto-generated method stub
		MobWINManager.init(context, Type.MOBWIN_BANNER);
	}

	public void release(Context context) {
		// TODO Auto-generated method stub
		MobWINManager.destroy();
	}

}
