package edu.sdust.mynote.database;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.bean.Note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

 
public class NoteDB extends SQLiteOpenHelper {

	private String dbCreate = "";
	private String dbTable = "";
	private SQLiteDatabase sqlliteDatabase;
	

	

	public NoteDB(Context context, String dbName, int dbVersion, String dbCreate, String dbTable) {
		// 当调用getWritableDatabase()
				// 或 getReadableDatabase()方法时
				// 则创建一个数据库
		super(context, dbName, null, dbVersion);
		this.dbCreate =  dbCreate;//use to create database
		this.dbTable = dbTable;//the table name
	
	}

	/* 创建一个表 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 数据库没有表时创建一个
		db.execSQL(dbCreate);
		Log.v("database", "onCreate");
		
	}
	
	public void open() throws SQLException
	{
		sqlliteDatabase = getWritableDatabase();
		sqlliteDatabase.execSQL(dbCreate);
		Log.v("database","open");
	}
	
	public Long insertData(Note note)
	{
		//String sql = "insert into memo values('"+memo.getItem_id()+"','"+memo.getItem_content()+"','"+memo.getCreate_time()+"',"+memo.getStarrted()+",'"+memo.getDue_date()+"','"+memo.getCompleted()+"','"+memo.getRepeat_type()+"')";
		ContentValues contentValue = new ContentValues();
		contentValue.put("note_id",note.getNote_id());
		contentValue.put("note_content", note.getNote_content());
		contentValue.put("note_title",note.getNote_title());
		contentValue.put("note_create_time", note.getNote_created_time());

		return sqlliteDatabase.insert(dbTable,null,contentValue);
	}
	public List<Note> selectAll()
	{
		String sql  = "select * from "+dbTable;
		Cursor cursor = sqlliteDatabase.rawQuery(sql, null);
		if(null == cursor)return null;
		List<Note> list = new ArrayList<Note>();
		while(cursor.moveToNext())
		{
			Note note = new Note();
			note.setNote_id(cursor.getString(cursor.getColumnIndex("note_id")));
			note.setNote_content(cursor.getString(cursor.getColumnIndex("note_content")));
			note.setNote_created_time(cursor.getLong(cursor.getColumnIndex("note_create_time")));
			note.setNote_title(cursor.getString(cursor.getColumnIndex("note_title")));
			list.add(note);
		}
		cursor.close();
		return list;
	}
	public List<Note> selectByKey(int rowId)
	{
		String sql  = "select * from "+dbTable+" where rowId="+rowId;
		Cursor cursor = sqlliteDatabase.rawQuery(sql, null);
		if(null == cursor) return null;
		List<Note> list = new ArrayList<Note>();
		while(cursor.moveToNext())
		{
			Note note = new Note();
			note.setNote_id(cursor.getString(cursor.getColumnIndex("note_id")));
			note.setNote_content(cursor.getString(cursor.getColumnIndex("note_content")));
			note.setNote_created_time(cursor.getLong(cursor.getColumnIndex("note_create_time")));
			note.setNote_title(cursor.getString(cursor.getColumnIndex("note_title")));
			list.add(note);
		}
		cursor.close();
		return list;
	}
	public boolean updata(Note note)
	{
		String id = note.getNote_id();
		String con = note.getNote_content();
		Long date = note.getNote_created_time();
		String title = note.getNote_title();
		
		
		ContentValues args = new ContentValues();
		
		args.put("note_title", title);
		args.put("note_content", con);
		
		return sqlliteDatabase.update(dbTable,args,"note_id=?",new String[]{note.getNote_id()})>0;
	}
	public boolean deleteById(String note_id)
	{
		//delete方法第一参数：数据库表名，第二个参数表示条件语句,第三个参数为条件带?的替代值

		//返回值大于0表示删除成功

		return sqlliteDatabase.delete(dbTable,"note_id = ?",new String[]{note_id})>0;


	}
	public void dropTable(String tableName)
	{
		sqlliteDatabase.execSQL("drop table if exists "+tableName);
	}

	/* 升级数据库 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		sqlliteDatabase.close();
	}
	
	public void deleteAll(String tableName){
		// TODO Auto-generated method stub
		String sql="delete from "+tableName;
		sqlliteDatabase.execSQL(sql); 
	}

	}

    
