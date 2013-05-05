package edu.sdust.mynote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import edu.sdust.mynote.adapter.MyDragListView;
import edu.sdust.mynote.adapter.NoteListAdapter;
import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.bean.Note;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.database.NoteDB;
import edu.sdust.mynote.function.DealWithDate;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


public class NoteListActivity extends Activity {

	
	ListView listView=null;
	Intent intent;
	private String dbCreate = "create table if not exists note (note_id text primary key,note_title text,note_create_time long,note_content text)";
	private Builder builder;
	private NoteListAdapter adapter;
	String note_id="";
	String event_id="";
	String delete_note_id="";
	Long create_time_long;
	HttpPostRequest request = new HttpPostRequest();
	DealWithDate dealWithDate = new DealWithDate();
	NoteDB noteDb = new NoteDB(MyApplication.getInstance(), "notes", 1, dbCreate, "note");
    public Lists list =new Lists(MyApplication.getInstance());
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.note_list_layout);
		
		
		init();
	}
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
			
		init();
	}



	private void init()
    {
		intent = this.getIntent();
		event_id = intent.getStringExtra("event_id");
		builder = new AlertDialog.Builder(this);
		
    	listView = (ListView)this.findViewById(R.id.note_list);
    	List<String> titles = new ArrayList<String>();
    	List<Integer> drawables = new ArrayList<Integer>();
    	List<String> content = new ArrayList<String>();
    	List<Integer> idList = new ArrayList<Integer>();
    	Log.v("show", "init");
    	
    	noteDb.open();
    	
    	List<Note> list = noteDb.selectAll();
    	List<Integer> va = new ArrayList<Integer>();
    	//List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();

		Calendar cal = Calendar.getInstance();
		int i = 0;
    	for (Note note : list) {
			//Map<String,String> map = new HashMap<String, String>();
			//map.put("title", memo.getTitle());
			//map.put("content", memo.getContent());
			//resultList.add(map);
			//Log.v("show",memo.getTitle());
			titles.add(i+1+"---"+(note.getNote_title().toString().length()>10?note.getNote_title().toString().substring(0, 10):note.getNote_title().toString()));
			content.add(note.getNote_content().toString().length()>10?note.getNote_content().toString().substring(0, 10):note.getNote_content().toString());
			drawables.add(R.drawable.note);
			idList.add(i+1);
			va.add(i++);
		}
    	
    	Button backBtn = (Button)findViewById(R.id.note_list_back_btn);
    	backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NoteListActivity.this.finish();
			}
    		
    	});
    	
    	adapter = new NoteListAdapter(this, titles, content,drawables,list);
    	
       	listView.setAdapter(adapter);
    	


    	//SimpleAdapter adapter = new SimpleAdapter(this, resultList, R.layout.line, new String[]{"title","content"}, new int[]{R.id.title,R.id.content});
    	//listView.setAdapter(adapter);
    	
       	listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				AlertDialog builderCreate = null;
				
				Toast.makeText(MyApplication.getInstance(), "点击的事件", Toast.LENGTH_LONG).show();
				builder.setTitle("修改笔记");
				final View note_layout = (View)getLayoutInflater().inflate(R.layout.add_modify_note_layout, null);
				builder.setView(note_layout);
				
				
				final EditText title = (EditText)note_layout.findViewById(R.id.note_modify_title);
				final EditText content = (EditText)note_layout.findViewById(R.id.note_modify_content);
				
				TextView create_time_label = (TextView)note_layout.findViewById(R.id.note_date_label);
				TextView create_time = (TextView)note_layout.findViewById(R.id.note_modify_date);
				
				
				noteDb.open();
				List<Note> list = noteDb.selectByKey(position+1);
				for (Note note:list){
					note_id = note.getNote_id();
					title.setText(note.getNote_title());
					content.setText(note.getNote_content());
					create_time_label.setVisibility(0);
					create_time.setVisibility(0);
					create_time_long = note.getNote_created_time();
					Date date = new Date();
					date.setTime(note.getNote_created_time());
					create_time.setText(dealWithDate.dateToStr(date));
				}
				
				builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
						 Note note = new Note();
						 note.setNote_id(note_id);
						 note.setNote_title(title.getText().toString());
						 note.setNote_content(content.getText().toString());
						 note.setNote_created_time(create_time_long);
						 
						 if (request.modifyNote(note_id, note.getNote_title(), note.getNote_content())==0){
							 noteDb.updata(note);	
							 noteDb.close();
						 }
						 Toast.makeText(NoteListActivity.this, "保存成功", 3000).show();
						 init();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						noteDb.close();
					}
				});
				builderCreate =  builder.create();
				builderCreate.show();//从工厂中取得dialog对象
			}
       		
       	});
    	listView.setOnItemLongClickListener(new OnItemLongClickListener() {
	 	
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
	    	 	AlertDialog builderCreate = null;
	    	 	
				builder.setTitle("是否删除？");			
			
				builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						noteDb.open();
						List<Note> list = noteDb.selectByKey(position+1);
						for (Note note:list){
							delete_note_id = note.getNote_id();
						}
						if (request.deleteNote(event_id,delete_note_id)==0){
							noteDb.deleteById(delete_note_id);
							noteDb.close();
							init();
						}
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				builderCreate =  builder.create();
				builderCreate.show();//从工厂中取得dialog对象
	
	    	
				return true;
			}
		});
    }

	
}
