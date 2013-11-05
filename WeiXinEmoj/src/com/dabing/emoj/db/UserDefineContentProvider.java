package com.dabing.emoj.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class UserDefineContentProvider extends ContentProvider {
	UserDefineDataBaseHelper mHelper;
	static final int URI_ALBUM = 1;// album
	static final int URI_ALBUM_ID = 2;// album/id

	static final String AUTHORITY = "com.dabing.emoj."
			+ UserDefineContentProvider.class.getSimpleName();
	static final UriMatcher MATCHER;
	static final String TAG = UserDefineContentProvider.class.getSimpleName();

	static {
		MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		MATCHER.addURI(AUTHORITY, "album", URI_ALBUM);
		MATCHER.addURI(AUTHORITY, "album/#", URI_ALBUM_ID);
	}

	public static Uri getUri() {
		Uri uri = Uri.parse("content://" + AUTHORITY + "/album");
		return uri;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mHelper = new UserDefineDataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Log.d(TAG, "query uri:"+uri.toString());
		Cursor cursor = null;
		int code = MATCHER.match(uri);
		switch (code) {
		case URI_ALBUM:
			try {
				final String query = "SELECT * FROM " + UserDefineDataBaseHelper.TABLE_NAME + " WHERE "
						+ UserDefineDataBaseHelper.FIELD_STATE + "=?" + " ORDER BY " + UserDefineDataBaseHelper.FIELD_ORDER + " DESC";
				SQLiteDatabase db = mHelper.getReadableDatabase();
				cursor = db.rawQuery(query, new String[]{"1"});
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(TAG, e.toString());
			}
			break;
		case URI_ALBUM_ID:

			try {
				long id = ContentUris.parseId(uri);
				cursor = mHelper.getCursor(id);
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		default:
			throw new IllegalArgumentException(
					"unknown UserDefineContentProvider Type");
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (MATCHER.match(uri)) {
		case URI_ALBUM:

			return "vnd.android.cursor.dir/vnd.com.dabing.emoj.user_define_table";
		case URI_ALBUM_ID:
			return "vnd.android.cursor.item/vnd.com.dabing.emoj.user_define_table";
		default:
			break;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
