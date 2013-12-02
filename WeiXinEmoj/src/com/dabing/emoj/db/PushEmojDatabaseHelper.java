package com.dabing.emoj.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 推送表情数据库
 * 
 * @author Administrator
 * 
 */
public class PushEmojDatabaseHelper extends SQLiteOpenHelper {
	public static final String FIELD_ID = "_id";
	public static final String FIELD_EMOJID = "emojid";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_THUMB = "thumb";
	public static final String FIELD_EMOJ = "emoj";
	public static final String FIELD_DES = "des";
	public static final String FIELD_TYPE = "type";// 0-免费 1-会员 2-铜板
	public static final String FIELD_MONEY = "money";// 铜板
	public static final String FIELD_STATE = "state"; // 0-未添加 1-已添加
	public static final String FIELD_READ = "read";// 0-未读 1-已读
	public static final String FIELD_TIME = "time";

	public static final String TABLE_NAME = "pushemoj";
	static final int VERSION = 1;
	static final String TAG = PushEmojDatabaseHelper.class.getSimpleName();

	public PushEmojDatabaseHelper(Context context) {
		this(context, TABLE_NAME, null, VERSION);
	}

	public PushEmojDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		final String sql = "CREATE TABLE " + TABLE_NAME + "(" + FIELD_ID
				+ " integer primary key autoincrement," + FIELD_EMOJID
				+ " text," + FIELD_NAME + " text," + FIELD_THUMB + " text,"
				+ FIELD_EMOJ + " text," + FIELD_DES + " text," + FIELD_STATE
				+ " integer default 0," + FIELD_READ + " integer default 0,"
				+ FIELD_TYPE + " integer default 0," + FIELD_MONEY
				+ " integer default 0," + FIELD_TIME + " integer);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = " DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}

	public Cursor getCursor() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
				FIELD_TIME + " DESC");
		return cursor;
	}
	
	public Cursor getCursor(String emojId){
		SQLiteDatabase db = getReadableDatabase();
		String whereClasue = FIELD_EMOJID + "=?";
		Cursor cursor = db.query(TABLE_NAME, null, whereClasue, new String[]{emojId}, null, null, null);
		return cursor;
	}
	public long insert(ContentValues cv) {
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert(TABLE_NAME, null, cv);
		return id;
	}
	
	public void remove(String emojId){
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = FIELD_EMOJID + "=?";
		db.delete(TABLE_NAME, whereClause, new String[]{emojId});
	}

}
