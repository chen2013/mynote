package edu.sdust.mynote.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Items {
	public static final String KEY_ROWID = "item_id";
	public static final String KEY_CONTENT = "item_content";
	public static final String KEY_TIME = "item_created_time";
	public static final String KEY_STARRED = "item_starred";
	public static final String KEY_COMPLETED = "item_completed";
	public static final String KEY_DUE_DATE="item_due_date";
	public static final String KEY_DUE_TIME="item_due_time";
	public static final String KEY_REPEAT_TYPE="item_repeat_type";
	private static final String TAG = "Items";
	private static final String DATABASE_NAME = "mynote";
	private static final String DATABASE_TABLE = "items";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE =
	"create table items (item_id integer primary key autoincrement, "
	+ "item_content text, item_created_timeStringstamp, "
	+ "item_starred integer,item_completed integer,"
	+ "item_due_date date,item_due_timeString,item_repeat_type integer );";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	
	
	public Items(Context ctx)
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
	
	//---�����ݿ�---
	public Items open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	//---�ر����ݿ�---	
	public void close()
	{
		DBHelper.close();
	}
	
	
	//---�����ݿ��в���һ������---
	public long insertItem(String content,String created_time,int starred,
			int completed,String due_date,String due_time,int repeat_type)
	{	
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_TIME, created_time);
		initialValues.put(KEY_STARRED, starred);
		initialValues.put(KEY_COMPLETED, completed);
		initialValues.put(KEY_DUE_DATE, due_date);
		initialValues.put(KEY_DUE_TIME, due_time);
		initialValues.put(KEY_REPEAT_TYPE, repeat_type);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	//---ɾ��һ��ָ������---
	
	public boolean deleteItem(int rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ROWID +
		"=" + rowId, null) > 0;
	}
	
	
	//---�������б���---
	
	public Cursor getAllItems()
	{
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_CONTENT,KEY_TIME,KEY_STARRED,KEY_COMPLETED,KEY_DUE_DATE,KEY_DUE_TIME,KEY_REPEAT_TYPE},
		null,null,null,null,null);
	}
	
	
	//---����һ��ָ������---
	
	public Cursor getItem(int rowId) throws SQLException
	{
		Cursor mCursor =
		db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_CONTENT,KEY_TIME,KEY_STARRED,KEY_COMPLETED,KEY_DUE_DATE,KEY_DUE_TIME,KEY_REPEAT_TYPE},
		KEY_ROWID + "=" + rowId,
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	//---����һ������---
	public boolean updateItem(int id,String content,String created_time,int starred,
			int completed,String due_date,String due_time,int repeat_type)
	{
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_CONTENT, content);
		updateValues.put(KEY_TIME, created_time);
		updateValues.put(KEY_STARRED, starred);
		updateValues.put(KEY_COMPLETED, completed);
		updateValues.put(KEY_DUE_DATE, due_date);
		updateValues.put(KEY_DUE_TIME, due_time);
		updateValues.put(KEY_REPEAT_TYPE, repeat_type);
		return db.update(DATABASE_TABLE, updateValues,
		KEY_ROWID + "=" + id, null) > 0;
	}

}
