package edu.sdust.mynote.database;

import java.util.ArrayList;
import java.util.List;

import edu.sdust.mynote.bean.Memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

 
public class DatabaseHelper extends SQLiteOpenHelper {

	private String dbCreate = "";
	private String dbTable = "";
	private SQLiteDatabase sqlliteDatabase;
	

	

	public DatabaseHelper(Context context, String dbName, int dbVersion, String dbCreate, String dbTable) {
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
	public Long insertData(Memo memo)
	{
		//String sql = "insert into memo values('"+memo.getItem_id()+"','"+memo.getItem_content()+"','"+memo.getCreate_time()+"',"+memo.getStarrted()+",'"+memo.getDue_date()+"','"+memo.getCompleted()+"','"+memo.getRepeat_type()+"')";
		ContentValues contentValue = new ContentValues();
		contentValue.put("item_id",memo.getItem_id());
		contentValue.put("item_content", memo.getItem_content());
		contentValue.put("create_time",memo.getCreate_time());
		contentValue.put("starrted", memo.getStarrted());
		contentValue.put("due_date", memo.getDue_date());
		contentValue.put("completed", memo.getCompleted());
		contentValue.put("repeat_type", memo.getRepeat_type());
		return sqlliteDatabase.insert(dbTable,null,contentValue);
	}
	public List<Memo> selectAll()
	{
		String sql  = "select * from "+dbTable;
		Cursor cursor = sqlliteDatabase.rawQuery(sql, null);
		if(null == cursor)return null;
		List<Memo> list = new ArrayList<Memo>();
		while(cursor.moveToNext())
		{
			Memo memo = new Memo();
			memo.setItem_id(cursor.getString(cursor.getColumnIndex("item_id")));
			memo.setItem_content(cursor.getString(cursor.getColumnIndex("item_content")));
			memo.setCreate_time(cursor.getLong(cursor.getColumnIndex("create_time")));
			memo.setStarrted(cursor.getInt(cursor.getColumnIndex("starrted")));
			memo.setDue_date(cursor.getLong(cursor.getColumnIndex("due_date")));
			memo.setCompleted(cursor.getInt(cursor.getColumnIndex("completed")));
			memo.setRepeat_type(cursor.getInt(cursor.getColumnIndex("repeat_type")));
			list.add(memo);
		}
		cursor.close();
		return list;
	}
	public List<Memo> selectByKey(int rowId)
	{
		String sql  = "select * from "+dbTable+" where rowId="+rowId;
		Cursor cursor = sqlliteDatabase.rawQuery(sql, null);
		if(null == cursor) return null;
		List<Memo> list = new ArrayList<Memo>();
		while(cursor.moveToNext())
		{
			Memo memo = new Memo();
			memo.setItem_id(cursor.getString(cursor.getColumnIndex("item_id")));
			memo.setItem_content(cursor.getString(cursor.getColumnIndex("item_content")));
			memo.setCreate_time(cursor.getLong(cursor.getColumnIndex("create_time")));
			memo.setStarrted(cursor.getInt(cursor.getColumnIndex("starrted")));
			memo.setDue_date(cursor.getLong(cursor.getColumnIndex("due_date")));
			memo.setCompleted(cursor.getInt(cursor.getColumnIndex("completed")));
			memo.setRepeat_type(cursor.getInt(cursor.getColumnIndex("repeat_type")));
			list.add(memo);
		}
		cursor.close();
		return list;
	}
	public boolean updata(Memo memo)
	{
		String id = memo.getItem_id();
		String con = memo.getItem_content();
		Long date = memo.getDue_date();
		
		
		ContentValues args = new ContentValues();
		
		args.put("item_content", con);
		args.put("starrted", memo.getStarrted());
		args.put("due_date", date);
		
		return sqlliteDatabase.update(dbTable,args,"item_id=?",new String[]{memo.getItem_id()})>0;
	}
	public boolean deleteById(String item_id)
	{
		//delete方法第一参数：数据库表名，第二个参数表示条件语句,第三个参数为条件带?的替代值

		//返回值大于0表示删除成功

		return sqlliteDatabase.delete(dbTable,"item_id = ?",new String[]{item_id})>0;


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

    