package edu.sdust.mynote;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.mynote.adapter.DragListAdapter;
import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.database.DatabaseHelper;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FinishActivity extends Activity {

	public static String dbCreate = "create table if not exists memo01 (item_id text primary key,item_content string,create_time long,starrted integer,due_date long,completed integer,repeat_type integer)";
	public DatabaseHelper databaseHelper = new DatabaseHelper(MyApplication.getInstance(), "gtask", 1, dbCreate, "memo01");
	private Builder builder;
	private ListView listView;
	private DragListAdapter adapter;
	private String event_id="";
	HttpPostRequest request =new HttpPostRequest();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.finished_layout);
       builder = new AlertDialog.Builder(this);
       init();
    }
    @Override
    public void onResume()
    {
    	super.onResume();
    	init();
    }
    private void init()
    {
    	listView = (ListView)this.findViewById(R.id.finish);
    	List<String> titles = new ArrayList<String>();
    	List<Integer> drawables = new ArrayList<Integer>();
    	List<Integer> idList = new ArrayList<Integer>();
    	List<String> top = new ArrayList<String>();
    	//Log.v("show", "init");
    	
    	databaseHelper.open();
    	List<Memo> list = databaseHelper.selectAll();
    	List<Integer> va = new ArrayList<Integer>();
    	//List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
    	Calendar cal = Calendar.getInstance();
		int i = 0;
    	for (Memo memo : list) {
			//Map<String,String> map = new HashMap<String, String>();
			//map.put("title", memo.getTitle());
			//map.put("content", memo.getContent());
			//resultList.add(map);
			////Log.v("show",memo.getTitle());
    		cal.setTimeInMillis(memo.getDue_date());
			titles.add(i+1+"---"+(memo.getItem_content().toString().length()>10?memo.getItem_content().toString().substring(0, 10):memo.getItem_content().toString()+"..."+"\n"+"结束时间："+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"~~"+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)));
			drawables.add(R.drawable.event);
			top.add("已完成");
			idList.add(i+1);
			va.add(i++);
		}
    	
    	adapter = new DragListAdapter(this, titles,top, drawables,list);
    	
    	listView.setAdapter(adapter);
    	
    	//注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的实现长按是的菜单
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
		       public void onCreateContextMenu(ContextMenu menu, View v,
		              ContextMenuInfo menuInfo) {
		                  menu.add(0, 0, 0, "添加笔记");
		                  menu.add(0, 1, 0, "查看笔记");
		              }
		});
		
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			  AlertDialog builderCreate = null;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				////Log.v("log", ""+arg2+"."+((DragListAdapter.ViewData<String, Integer>)arg1.getTag()).value);
				builder.setTitle("备忘");
				final View reset = (View)getLayoutInflater().inflate(R.layout.event_reset_layout, null);
				builder.setView(reset);
				Memo memo = (Memo)arg1.getTag();
				final EditText con = (EditText)reset.findViewById(R.id.content);
				final Button btn = (Button)reset.findViewById(R.id.del);
				final Button setbtn1 = (Button)reset.findViewById(R.id.modifyTimeBtn);
				final Button setbtn2 = (Button)reset.findViewById(R.id.modifyDateBtn);
				setbtn1.setVisibility(View.INVISIBLE);
				setbtn2.setVisibility(View.INVISIBLE);
				btn.setVisibility(View.INVISIBLE);
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
				
				//Log.v("tag", ""+memo.getStarrted());
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
				builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {//设置dialog
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String item_id=null;
						List<Memo> list=databaseHelper.selectByKey(arg2+1);
						for (Memo memo :list){
							item_id=memo.getItem_id();
							databaseHelper.deleteById(item_id);
							request.deleteEvent("D9E12762AC824E4BDC02C00212CC37E1", item_id);
						}
	
						 init();
						 Toast.makeText(FinishActivity.this, "删除成功", 3000).show();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {//dialog
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						databaseHelper.close();
					}
				});
				builderCreate =  builder.create();builderCreate.show();
			}
    		
		});
    	listView.setAdapter(adapter);//Log.v("for", "-");
    	databaseHelper.close();
    	
    	
    	
    	Button to_finish_back = (Button)findViewById(R.id.to_finish_back_btn);
    	to_finish_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
    		
    	});
    }
 // 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {
		 
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
			                                .getMenuInfo();
			final int pos = (int) info.position;// 这里的info.id对应的就是数据库中rowId的值，position对应的第一个被点击

			
			//Log.v("chang an shijian ", "pos:"+pos+1);
			databaseHelper.open();
			List<Memo> list = databaseHelper.selectByKey(pos+1);
			for (Memo memo:list)
				event_id=memo.getItem_id();
        		        	               		        
		    switch (item.getItemId()) {
		                case 0:
		                        // 添加笔记
		                	AlertDialog builderCreate = null;
		                	
		                	View add_new_note = (View)getLayoutInflater().inflate(R.layout.add_modify_note_layout, null);
		                	
		                		    	 	
		                	TextView create_time_label = (TextView)add_new_note.findViewById(R.id.note_date_label);
		                	TextView create_time = (TextView)add_new_note.findViewById(R.id.note_modify_date);
		                	final EditText title = (EditText)add_new_note.findViewById(R.id.note_modify_title);
		                	final EditText content = (EditText)add_new_note.findViewById(R.id.note_modify_content);
		                					
		                		    		
		                	builder.setTitle("新增笔记");
		                	builder.setView(add_new_note);
		                					
		                					
		                	builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			                						
			                	@Override
			                	public void onClick(DialogInterface arg0, int arg1) {
			                		// TODO Auto-generated method stub
			                	
			                			if (request.addNewNote(event_id, title.getText().toString(), content.getText().toString())==0)
			                				Toast.makeText(MyApplication.getInstance(), "添加成功~", Toast.LENGTH_LONG).show();
			                			else
			                				Toast.makeText(MyApplication.getInstance(), "添加失败~", Toast.LENGTH_LONG).show();
			                		}
		                	});
		                	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		                						
		                		@Override
		                		public void onClick(DialogInterface dialog, int which) {
		                			// TODO Auto-generated method stub
		                							
		                		}
		                	});
		                	databaseHelper.close();
		                	builderCreate =  builder.create();
		                	builderCreate.show();//从工厂中取得dialog对象
		                	break;
		 
		                case 1:
		                        // 查看笔记
		                	 	if (request.getNote(event_id)==0){
		                	 		
		                	 		
			                		Intent intent = new Intent(FinishActivity.this,NoteListActivity.class);
			                		intent.putExtra("event_id", event_id);
			                		startActivity(intent);
		                	 	}
		                	 	break;
	        		        	
		 
		                default:
		                        break;
		                }
		    
		                return true;
		 
		        }


}
