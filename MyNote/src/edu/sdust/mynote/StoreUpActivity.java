package edu.sdust.mynote;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.mynote.adapter.DragListAdapter;
import edu.sdust.mynote.adapter.MyDragListView;
import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.database.DatabaseHelper;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StoreUpActivity extends Activity {
	private String curList=null;
	private String dbCreate = "create table if not exists memo (item_id integer primary key,item_content string,create_time long,starrted integer,due_date long,completed integer,repeat_type integer)";
	private Builder builder;
	private MyDragListView listView;
	private DragListAdapter adapter;
	HttpPostRequest request = new HttpPostRequest();
	DatabaseHelper databaseHelper = new DatabaseHelper(MyApplication.getInstance(), "gtask", 1, dbCreate, "memo");
    public Lists list =new Lists(MyApplication.getInstance());
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.storeup);
       
       
       dataBinding();
       init();
    }
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	dataBinding();
    	init();
    }
    
    
    private void dataBinding() {
		// TODO Auto-generated method stub
         builder = new AlertDialog.Builder(this);
         
         SharedPreferences prefer=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
         int position = prefer.getInt("position", 0);
         
         
         list.open();
         Cursor cursor=list.getItem(position+1);
         cursor.moveToFirst();
         curList = cursor.getString(0);
         cursor.close();
         
         
         request.getAllEvent(curList);
         list.close();
	}
	private void init()
    {
    		
    	listView = (MyDragListView)this.findViewById(R.id.storeUpList);
    	List<String> titles = new ArrayList<String>();
    	List<Integer> drawables = new ArrayList<Integer>();
    	List<Integer> idList = new ArrayList<Integer>();
    	Log.v("show", "init");
    	
    	databaseHelper.open();
    	List<Memo> list = databaseHelper.selectAll();
    	listView = (MyDragListView)findViewById(R.id.storeUpList);
    	List<Integer> va = new ArrayList<Integer>();
    	//List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();

		Calendar cal = Calendar.getInstance();
		int i = 0;
    	for (Memo memo : list) {
			//Map<String,String> map = new HashMap<String, String>();
			//map.put("title", memo.getTitle());
			//map.put("content", memo.getContent());
			//resultList.add(map);
			//Log.v("show",memo.getTitle());
    		cal.setTimeInMillis(memo.getDue_date());
			titles.add(i+1+"---"+(memo.getItem_content().toString().length()>10?memo.getItem_content().toString().substring(0, 10):memo.getItem_content().toString()+"..."+"\n"+"结束时间："+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"..."+cal.get(Calendar.HOUR_OF_DAY)+"："+cal.get(Calendar.MINUTE)));
			Log.v("tag", cal.getTimeInMillis()+"value");
			drawables.add(R.drawable.ic_launcher);
			idList.add(i+1);
			va.add(i++);
		}
    	
    	adapter = new DragListAdapter(this, titles, drawables,list);
    	
       	listView.setAdapter(adapter);
    	
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			 AlertDialog builderCreate = null;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				//Log.v("log", ""+arg2+"."+((DragListAdapter.ViewData<String, Integer>)arg1.getTag()).value);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("修改备忘");
				final View reset = (View)getLayoutInflater().inflate(R.layout.reset, null);
				builder.setView(reset);
				Memo memo = (Memo)arg1.getTag();
				final EditText title = (EditText)reset.findViewById(R.id.title);
				final EditText con = (EditText)reset.findViewById(R.id.content);
				final Button btn = (Button)reset.findViewById(R.id.del);
				if(memo.getStarrted() == 0)
				{
					((RadioButton)reset.findViewById(R.id.radio0)).setChecked(true);
				}else if(memo.getStarrted() == 1)
				{
					((RadioButton)reset.findViewById(R.id.radio1)).setChecked(true);
				}else 
				{
					((RadioButton)reset.findViewById(R.id.radio2)).setChecked(true);
				}
				
				Log.v("tag", ""+memo.getStarrted());
				final String id = memo.getItem_id();
				con.setText(memo.getItem_content());
				 databaseHelper.open();
				 con.setOnEditorActionListener(new  TextView.OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
						// TODO Auto-generated method stub
						return false;
					}
				});
				btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						
						List<Memo> list=databaseHelper.selectByKey(arg2+1);
						for (Memo memo :list){
							Log.v("从这里"+arg2+"查询到的item_id",memo.getItem_id()+"kongde" );
							String item_id=memo.getItem_id();
							databaseHelper.deleteById(item_id);
							request.deleteEvent(curList, item_id);
						}
						init();
						Toast.makeText(StoreUpActivity.this, "删除", 3000).show();
						StoreUpActivity.this.closeOptionsMenu();
						builderCreate.dismiss();
					}
				});
				builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
						 Memo memo = new Memo();
						 memo.setItem_content(con.getText().toString());
						 memo.setItem_id(id);
						 if(((RadioButton)reset.findViewById(R.id.radio0)).isChecked())
						 {
							 memo.setStarrted(0);
						 } else if(((RadioButton)reset.findViewById(R.id.radio1)).isChecked())
						 {
							 memo.setStarrted(1);
						 } else
						 {
							 memo.setStarrted(2);
						 }
						
						 databaseHelper.updata(memo);
						 
						 List<Memo> list = databaseHelper.selectByKey(arg2+1);
						 for (Memo memo1:list)
							 request.modifyEventContent(memo1.getItem_id(), memo.getItem_content());
						 
						 init();
						 Toast.makeText(StoreUpActivity.this, "保存成功", 3000).show();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						databaseHelper.close();
					}
				});
				builderCreate =  builder.create();builderCreate.show();//从工厂中取得dialog对象
			}
    		
		});
    	listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.v("debug", "longclick");
				listView.isLongClick = true;
				listView.startDragSet();
				return false;
			}
		});

    	//SimpleAdapter adapter = new SimpleAdapter(this, resultList, R.layout.line, new String[]{"title","content"}, new int[]{R.id.title,R.id.content});
    	//listView.setAdapter(adapter);
    	databaseHelper.close();

    }

 
}
