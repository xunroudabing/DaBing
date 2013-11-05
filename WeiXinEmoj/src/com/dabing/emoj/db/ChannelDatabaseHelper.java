package com.dabing.emoj.db;

import com.dabing.emoj.exception.DBLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 用于保存编辑的微频道
 * @author Administrator
 *
 */
public class ChannelDatabaseHelper extends SQLiteOpenHelper {
	public static final String FIELD_ID = "_id";
	public static final String FIELD_CHANNLEID = "channelid";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_TYPE = "channeltype";
	public static final String FIELD_TIME = "time";
	boolean isFirstCreate = false;
	public static final String TABLE_NAME = "channel";
	static final int VERSION = 1;
	static final String TAG = ChannelDatabaseHelper.class.getSimpleName();
	public ChannelDatabaseHelper(Context context){
		this(context, TABLE_NAME, null, VERSION);
	}
	public ChannelDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		final String sql = "CREATE TABLE " + TABLE_NAME + "(" + FIELD_ID
				+ " integer primary key autoincrement," + FIELD_CHANNLEID
				+ " integer," + FIELD_NAME + " text," + FIELD_TYPE + " text,"
				+ FIELD_TIME + " integer);";
		db.execSQL(sql);
		isFirstCreate = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}
	
	public Cursor getCursor(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "ORDER BY " + FIELD_TIME + " DESC");
		return cursor;
	}
	
	public long insert(ContentValues cv){
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert(TABLE_NAME, null, cv);
		return id;
	}

}
