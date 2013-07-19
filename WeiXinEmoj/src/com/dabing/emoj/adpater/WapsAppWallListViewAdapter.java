package com.dabing.emoj.adpater;

import java.util.List;

import com.dabing.emoj.R;
import com.dabing.emoj.widget.WrapperImageView;

import cn.waps.AdInfo;
import cn.waps.AppConnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 万普应用墙
 * @author DaBing
 *
 */
public class WapsAppWallListViewAdapter extends BaseAdapter {
	Context mContext;
	List<AdInfo> mData;
	static final String TAG = WapsAppWallListViewAdapter.class.getSimpleName();
	public WapsAppWallListViewAdapter(Context context,List<AdInfo> data){
		mContext = context;
		mData = data;
	}
	private View makeView(int position, View convertView, ViewGroup parent){
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.waps_appwall_listitem, parent, false);
		}else {
			root = convertView;
		}
		TextView appnameView = (TextView) root.findViewById(R.id.waps_appwall_listitem_appname);
		TextView detailView = (TextView) root.findViewById(R.id.waps_appwall_listitem_appdetail);
		ImageView iconView = (ImageView) root.findViewById(R.id.waps_appwall_listitem_icon);
		LinearLayout downloadView = (LinearLayout) root.findViewById(R.id.waps_appwall_download);
		final AdInfo info = mData.get(position);
		String name = info.getAdName();
		String detail = info.getAdText();
		Bitmap icon = info.getAdIcon();
		
		appnameView.setText(name);
		detailView.setText(detail);
		iconView.setImageBitmap(icon);
		downloadView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppConnect.getInstance(mContext).downloadAd(info.getAdId());
			}
		});
		return root;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public AdInfo getAdInfo(int position){
		return mData.get(position);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			return makeView(position, convertView, parent);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, e.toString());
		}
		return null;
	}

}
