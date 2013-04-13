package edu.sdust.mynote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Notes {
	public static final String KEY_ROWID = "note_id";
	public static final String KEY_TITLE = "note_title";
	public static final String KEY_CONTENT = "note_content";
	public static final String KEY_TIME = "note_create_time";
	private static final String TAG = "Notes";
	private static final String DATABASE_NAME = "mynote";
	private static final String DATABASE_TABLE = "notes";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE =
	"create table notes (note_id integer primary key autoincrement, "
	+ "note_title text, note_content text, "
	+ "note_created_time text );";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	
	
	public Notes(Context ctx)
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
	public Notes open() throws SQLException
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
	public long insertItem(String title,String content,String created_time)
	{	
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_TIME, created_time);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	//---删除一个指定标题---
	
	public boolean deleteItem(int rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ROWID +
		"=" + rowId, null) > 0;
	}
	
	
	//---检索所有标题---
	
	public Cursor getAllItems()
	{
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_TITLE,KEY_CONTENT,KEY_TIME},
		null,null,null,null,null);
	}
	
	
	//---检索一个指定标题---
	
	public Cursor getItem(int rowId) throws SQLException
	{
		Cursor mCursor =
		db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_TITLE,KEY_CONTENT,KEY_TIME},
		KEY_ROWID + "=" + rowId,
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	//---更新一个标题---
	public boolean updateItem(int id,String title,String content,String created_time)
	{
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_TITLE, title);
		updateValues.put(KEY_CONTENT, content);
		updateValues.put(KEY_TIME, created_time);
		return db.update(DATABASE_TABLE, updateValues,
		KEY_ROWID + "=" + id, null) > 0;
	}
}
