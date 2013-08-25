package com.dabing.emoj.adpater;

import java.io.File;

import org.json.JSONArray;

import com.dabing.emoj.R;
import com.dabing.emoj.widget.CacheWrapperImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
/**
 * 相册内图片详细gridview
 * @author DaBing
 *
 */
public class AlbumDetailAdapter extends BaseAdapter {
	Context mContext;
	File[] mFiles;
	int mSpacing = 2;
	int mWidth;
	static final String TAG = AlbumDetailAdapter.class.getSimpleName();
	public AlbumDetailAdapter(Context context,File[] files,int columNum){
		mContext = context;
		mFiles = files;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = manager.getDefaultDisplay().getWidth();
		//Log.d(TAG, "screenWidth:"+screenWidth);
		//图片宽度
		mWidth = (int) ((screenWidth - (columNum + 1)*mSpacing) / columNum);
		Log.d(TAG, "mWidth:"+mWidth);
	}
	
	private View makeView(int position, View convertView, ViewGroup parent){
		View root = null;
		if(convertView == null){
			root = LayoutInflater.from(mContext).inflate(R.layout.cache_item, parent, false);
		}else {
			root = convertView;
		}
		CacheWrapperImageView imageView = (CacheWrapperImageView) root;
		imageView.setWidth(mWidth);
		File file = mFiles[position];
		Log.d(TAG, "path:"+file.getPath());
		imageView.setPath(file.getPath());
		return root;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFiles.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
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
