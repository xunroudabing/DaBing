package com.dabing.emoj.adpater;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;

import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.db.UserDefineDataBaseHelper.UserDefineCursor;
import com.dabing.emoj.fragment.UserDefineFragment.IEmojScanCallBack;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.widget.Album;
import com.dabing.emoj.widget.Album.AlbumClickListener;

public class AlbumCusorAdapter extends CursorAdapter {
	int mWidth = 80;
	AlbumClickListener mListener;
	Context mContext;
	int COLUM_NUM = 3;
	static final int COLUM_PADDING = 0;
	static final String TAG = AlbumCusorAdapter.class.getSimpleName();
	public AlbumCusorAdapter(Context context, Cursor c , int col_num ,AlbumClickListener listener) {
		super(context, c,false);
		// TODO Auto-generated constructor stub
		mContext = context;
		COLUM_NUM = col_num;
		mListener = listener;
		calculateAlbumWidth();
		
	}

	// 计算相册宽度
	private void calculateAlbumWidth() {
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		mWidth = (screenWidth - (COLUM_NUM + 1) * COLUM_PADDING)
				/ COLUM_NUM;
		Log.d(TAG, "width:" + mWidth);

	}
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		Album album = new Album(mContext);
		album.setWidth(mWidth);
		album.setAlbumClickListener(mListener);
		return album;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		Album album = (Album) view;
		album.setFile(getFileInfo(cursor));
	}
	
	private FileInfo getFileInfo(Cursor cursor){
		return FileInfo.GetFileInfo(cursor);
	}
	
	public void reflesh(){
		//changeCursor(new UserDefineDataBaseHelper(mContext).getCursor());
		if(getCursor() == null || getCursor().isClosed()){
			return;
		}
		getCursor().requery();
		notifyDataSetChanged();
	}
	
	public void close(){
		if(getCursor() != null){
			getCursor().close();
		}
	}

}
