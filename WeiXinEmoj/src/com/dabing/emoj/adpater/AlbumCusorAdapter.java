package com.dabing.emoj.adpater;

import com.dabing.emoj.db.UserDefineDataBaseHelper;
import com.dabing.emoj.db.UserDefineDataBaseHelper.UserDefineCursor;
import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.widget.Album;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class AlbumCusorAdapter extends CursorAdapter {

	public AlbumCusorAdapter(Context context, Cursor c) {
		super(context, c,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		return new Album(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}
	
	private FileInfo getFileInfo(Cursor cursor){
		return FileInfo.GetFileInfo((UserDefineCursor)cursor);
	}

}
