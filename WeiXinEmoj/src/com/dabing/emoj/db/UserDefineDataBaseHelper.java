package com.dabing.emoj.db;

import java.io.File;

import com.dabing.emoj.utils.FileInfo;
import com.dabing.emoj.utils.SimpleFile;
import com.dabing.emoj.utils.Util;
import com.dabing.emoj.widget.CustomGridLayout;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

/**
 * 用户自定义表情数据库类
 * 
 * @author DaBing
 * 
 */
public class UserDefineDataBaseHelper extends SQLiteOpenHelper {
	// 字段
	public static final String FIELD_ID = "_id";
	public static final String FIELD_NAME = "name";// 文件名
	public static final String FIELD_PATH = "path";// 文件路径
	public static final String FIELD_TYPE = "type"; // 类型 common
	public static final String FIELD_THUMB = "thumb";// 缩略图地址
	public static final String FIELD_TIME = "time";// 时间戳
	public static final String FIELD_STATE = "state";// 状态位 1-默认 0-删除
	public static final String FIELD_CHILDSIZE = "childsize";// 图片文件数
	boolean isFirstCreate = false;
	static final String TABLE_NAME = "user_define_table";
	static final int VERSION = 2;
	static final String TAG = UserDefineDataBaseHelper.class.getSimpleName();

	public UserDefineDataBaseHelper(Context context) {
		this(context, TABLE_NAME, null, VERSION);
	}

	public UserDefineDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + "(" + FIELD_ID
				+ "	integer primary key autoincrement," + FIELD_NAME + " text,"
				+ FIELD_PATH + " text," + FIELD_TYPE + " text," + FIELD_THUMB
				+ " text," + FIELD_TIME + " integer," + FIELD_CHILDSIZE
				+ " integer," + FIELD_STATE + " integer default 1);";
		Log.d(TAG, "onCreate:" + sql);
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

	public UserDefineCursor getCursor() {
		final String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
				+ FIELD_STATE + "=?";
		SQLiteDatabase db = getReadableDatabase();
		UserDefineCursor cursor = (UserDefineCursor) db.rawQueryWithFactory(
				new UserDefineCursor.Factory(), query, new String[] { "1" },
				null);
		return cursor;
	}

	public UserDefineCursor getCursor(long id) {
		final String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
				+ FIELD_ID + "=?";
		SQLiteDatabase db = getReadableDatabase();
		UserDefineCursor cursor = (UserDefineCursor) db.rawQueryWithFactory(
				new UserDefineCursor.Factory(), query,
				new String[] { String.valueOf(id) }, null);
		return cursor;
	}

	public FileInfo getFileInfo(int id) {
		final String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
				+ FIELD_ID + "=?";
		SQLiteDatabase db = getReadableDatabase();
		UserDefineCursor cursor = (UserDefineCursor) db.rawQueryWithFactory(
				new UserDefineCursor.Factory(), query,
				new String[] { String.valueOf(id) }, null);
		cursor.moveToFirst();
		FileInfo fileInfo = FileInfo.GetFileInfo(cursor.getPath());
		fileInfo.dbId = cursor.getId();
		fileInfo.dbName = cursor.getName();
		fileInfo.dbState = cursor.getState();
		fileInfo.dbThumb = cursor.getThumb();
		fileInfo.dbTime = cursor.getTime();
		fileInfo.dbType = cursor.getType();
		cursor.close();
		return fileInfo;
	}

	public FileInfo getFileInfo(String path) {
		final String query = "SELECT * FROM " + TABLE_NAME + " WHERE "
				+ FIELD_PATH + "=?";
		SQLiteDatabase db = getReadableDatabase();
		UserDefineCursor cursor = (UserDefineCursor) db.rawQueryWithFactory(
				new UserDefineCursor.Factory(), query, new String[] { path },
				null);
		cursor.moveToFirst();
		FileInfo fileInfo = null;
		if (cursor.getCount() > 0) {
			fileInfo = FileInfo.GetFileInfo(path);
			fileInfo.dbId = cursor.getId();
			fileInfo.dbName = cursor.getName();
			fileInfo.dbState = cursor.getState();
			fileInfo.dbThumb = cursor.getThumb();
			fileInfo.dbTime = cursor.getTime();
			fileInfo.dbType = cursor.getType();
		}

		cursor.close();
		return fileInfo;
	}

	/**
	 * 移除数据 将状态为改为0
	 * 
	 * @param id
	 */
	public void remove(int id) {
		ContentValues cv = new ContentValues();
		cv.put(FIELD_STATE, 0);
		SQLiteDatabase db = getWritableDatabase();
		db.update(TABLE_NAME, cv, FIELD_ID + "=?",
				new String[] { String.valueOf(id) });
		Log.d(TAG, "remove id:" + id);
	}
	/**
	 * 恢复数据 将状态改为1
	 * @param id
	 */
	public void enable(long id){
		ContentValues cv = new ContentValues();
		cv.put(FIELD_STATE, 1);
		SQLiteDatabase db = getWritableDatabase();
		db.update(TABLE_NAME, cv, FIELD_ID + "=?",
				new String[] { String.valueOf(id) });
	}
	/**
	 * 物理删除
	 * 
	 * @param id
	 */
	public void delete(int id) {
		String whereClause = FIELD_ID + " =?";
		String[] whereValue = { String.valueOf(id) };
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, whereClause, whereValue);
	}

	/**
	 * 更新子文件数
	 * 
	 * @param count
	 * @param id
	 */
	public void updateCount(int count, int id) {
		ContentValues values = new ContentValues();
		values.put(FIELD_CHILDSIZE, count);
		SQLiteDatabase db = getWritableDatabase();
		db.update(TABLE_NAME, values, FIELD_ID + "=?",
				new String[] { String.valueOf(id) });
		// Log.d(TAG, "update count:" + count + " id:" + id);
	}

	public void update(int id, FileInfo fileInfo) {
		ContentValues cv = new ContentValues();
		if (fileInfo.fileName != null && !fileInfo.fileName.equals("")) {
			cv.put(FIELD_NAME, fileInfo.fileName);
		}
		if (fileInfo.filePath != null && !fileInfo.filePath.equals("")) {
			cv.put(FIELD_PATH, fileInfo.filePath);
		}
		if (fileInfo.dbType != null && !fileInfo.dbType.equals("")) {
			cv.put(FIELD_TYPE, fileInfo.dbType);
		}
		if (fileInfo.dbThumb != null && !fileInfo.dbThumb.equals("")) {
			cv.put(FIELD_THUMB, fileInfo.dbThumb);
		}
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = FIELD_ID + " =?";
		String[] whereArgs = { String.valueOf(id) };
		db.update(TABLE_NAME, cv, whereClause, whereArgs);
		// Log.d(TAG, "update:"+id);
	}

	public long insert(FileInfo fileInfo) {
		ContentValues cv = new ContentValues();
		cv.put(FIELD_NAME, fileInfo.fileName);
		cv.put(FIELD_PATH, fileInfo.filePath);
		cv.put(FIELD_TIME, System.currentTimeMillis());
		// cv.put(FIELD_STATE, 1);
		cv.put(FIELD_TYPE, "common");
		cv.put(FIELD_CHILDSIZE, fileInfo.Count);
		cv.put(FIELD_THUMB, fileInfo.dbThumb);
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert(TABLE_NAME, null, cv);
		Log.d(TAG, "insert id:" + id);
		return id;
	}

	public void insert(File file) {
		ContentValues cv = new ContentValues();
		cv.put(FIELD_NAME, file.getName());
		cv.put(FIELD_PATH, file.getAbsolutePath());
		cv.put(FIELD_TIME, System.currentTimeMillis());
		// cv.put(FIELD_STATE, 1);
		cv.put(FIELD_TYPE, "common");
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert(TABLE_NAME, null, cv);
		Log.d(TAG, "insert id:" + id);
	}

	public long insert(SimpleFile file) {
		ContentValues cv = new ContentValues();
		cv.put(FIELD_NAME, file.name);
		cv.put(FIELD_PATH, file.path);
		cv.put(FIELD_TIME, System.currentTimeMillis());
		cv.put(FIELD_TYPE, "common");
		cv.put(FIELD_CHILDSIZE, file.count);
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert(TABLE_NAME, null, cv);
		Log.d(TAG, "insert id:" + id);
		return id;

	}

	public boolean exist(FileInfo fileInfo) {
		return exist(fileInfo.filePath);
	}

	public boolean exist(String path) {
		String filepath = Util.makeStandardPath(path);
		final String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE "
				+ FIELD_PATH + "=?" + " AND " + FIELD_STATE + "=?";
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] { "COUNT(*)" },
				FIELD_PATH + "=?" + " AND " + FIELD_STATE + "=?", new String[] {
						filepath, "1" }, null, null, null);
		if (cursor == null) {
			return false;
		}
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		Log.d(TAG, path + " exist:" + count);
		return count > 0;
	}

	/**
	 * 获取id
	 * 
	 * @param path
	 * @return -1 该path在数据库不存在
	 */
	public int getId(String path) {
		int id = -1;
		String filepath = Util.makeStandardPath(path);
		final String[] columns = { FIELD_ID };
		String whereClause = FIELD_PATH + " =?";
		String[] whereArgs = { filepath };
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs,
				null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			id = cursor.getInt(cursor.getColumnIndexOrThrow(FIELD_ID));
		}
		cursor.close();
		return id;
	}

	public static class UserDefineCursor extends SQLiteCursor {

		private UserDefineCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
				String editTable, SQLiteQuery query) {
			super(db, driver, editTable, query);
			// TODO Auto-generated constructor stub
		}

		private static class Factory implements CursorFactory {

			@Override
			public Cursor newCursor(SQLiteDatabase db,
					SQLiteCursorDriver masterQuery, String editTable,
					SQLiteQuery query) {
				// TODO Auto-generated method stub
				return new UserDefineCursor(db, masterQuery, editTable, query);
			}

		}

		public int getId() {
			return getInt(getColumnIndexOrThrow(FIELD_ID));
		}

		public String getName() {
			return getString(getColumnIndexOrThrow(FIELD_NAME));
		}

		public String getPath() {
			return getString(getColumnIndexOrThrow(FIELD_PATH));
		}

		public String getType() {
			return getString(getColumnIndexOrThrow(FIELD_TYPE));
		}

		public String getThumb() {
			return getString(getColumnIndexOrThrow(FIELD_THUMB));
		}

		public long getTime() {
			return getLong(getColumnIndexOrThrow(FIELD_TIME));
		}

		public int getState() {
			return getInt(getColumnIndexOrThrow(FIELD_STATE));
		}

		public int getChildSize() {
			return getInt(getColumnIndexOrThrow(FIELD_CHILDSIZE));
		}

	}

}
