package edu.sdust.mynote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class Lists
{
	public static final String KEY_ROWID = "list_id";
	public static final String KEY_NAME = "list_name";
	public static final String KEY_TIME = "list_created_time";
	public static final String KEY_TOTAL = "list_total";
	public static final String KEY_CLASS = "list_class";
	private static final String TAG = "Lists";
	private static final String DATABASE_NAME = "mynote";
	private static final String DATABASE_TABLE = "lists";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE =
	"create table lists (list_id integer primary key autoincrement, "
	+ "list_name text not null, list_created_time text, "
	+ "list_total integer,list_class text);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	
	
	public Lists(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
		int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion
			+ " to "
			+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}
	
	//---打开数据库---
	public Lists open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	//---关闭数据库---	
	public void close()
	{
		DBHelper.close();
	}
	
	
	//---向数据库中插入一个标题---
	public long insertItem(String list_name, String list_created_time, int list_total,String list_class)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, list_name);
		initialValues.put(KEY_TIME, list_created_time);
		initialValues.put(KEY_TOTAL, list_total);
		initialValues.put(KEY_CLASS, list_class);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	//---删除一个指定标题---
	
	public boolean deleteItem(int rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ROWID +
		"=" + rowId, null) > 0;
	}
	
	
	//---检索所有标题---
	
	public Cursor getAllLists()
	{
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_NAME,KEY_TIME,KEY_TOTAL,KEY_CLASS},
		null,null,null,null,null);
	}
	
	
	//---检索一个指定标题---
	
	public Cursor getItem(int rowId) throws SQLException
	{
		Cursor mCursor =
		db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_NAME,KEY_TIME,KEY_TOTAL,KEY_CLASS},
		KEY_ROWID + "=" + rowId,
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	//---更新一个标题---
	public boolean updateItem(int rowId, String list_name, String list_created_time, int list_total,String list_class)
	{
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_NAME, list_name);
		updateValues.put(KEY_TIME, list_created_time);
		updateValues.put(KEY_TOTAL, list_total);
		updateValues.put(KEY_CLASS, list_class);
		return db.update(DATABASE_TABLE, updateValues,
		KEY_ROWID + "=" + rowId, null) > 0;
	}
}

