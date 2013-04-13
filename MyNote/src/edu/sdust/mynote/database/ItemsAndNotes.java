package edu.sdust.mynote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemsAndNotes {
	public static final String KEY_ROWID = "note_id";
	public static final String KEY_ITEM_ID = "item_id";
	public static final String KEY_NOTE_ID = "note_id";
	private static final String TAG = "ItemsAndNotes";
	private static final String DATABASE_NAME = "mynote";
	private static final String DATABASE_TABLE = "itemsAndnotes";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE =
	"create table itemsAndnotes (id integer primary key autoincrement, "
	+ "item_id integer not null"
	+ "note_id integer);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	
	
	public ItemsAndNotes(Context ctx)
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
	public ItemsAndNotes open() throws SQLException
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
	public long insertItem(int item_id,int note_id)
	{	
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ITEM_ID, item_id);
		initialValues.put(KEY_NOTE_ID, note_id);
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
		return db.query(DATABASE_TABLE, new String[] {KEY_ROWID,KEY_ITEM_ID,KEY_NOTE_ID},
		null,null,null,null,null);
	}
	
	
	//---检索一个指定标题---
	
	public Cursor getItem(int item_id) throws SQLException
	{
		Cursor mCursor =
		db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_ITEM_ID,KEY_NOTE_ID},
		KEY_ITEM_ID + "=" + item_id,
		null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	//---更新一个标题---
	public boolean updateItem(int id,int item_id,int note_id)
	{
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_ITEM_ID, item_id);
		updateValues.put(KEY_NOTE_ID, note_id);
		return db.update(DATABASE_TABLE, updateValues,
		KEY_ROWID + "=" + id, null) > 0;
	}
}
