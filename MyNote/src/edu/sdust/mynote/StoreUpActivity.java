package edu.sdust.mynote;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdust.mynote.adapter.DragListAdapter;
import edu.sdust.mynote.adapter.MyDragListView;
import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.bean.Note;
import edu.sdust.mynote.database.DatabaseHelper;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.function.DealWithDate;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class StoreUpActivity extends Activity {
	private String curList="";
	private String dbCreate = "create table if not exists memo (item_id String primary key,item_content string,create_time long,starrted integer,due_date long,completed integer,repeat_type integer)";
	private Builder builder;
	private MyDragListView listView;
	private DragListAdapter adapter;
	int starred=0;
	String event_id_longClick = "";
 	int getResult= 20;
	String event_id=""; 
	Date modifyDateTime = new Date();
	String modifyDateStr="";
	String modifyTimeStr="";
	
    final int DIALOG_TIME = 0;    //设置对话框id  
    final int DIALOG_DATE = 1;	//dialog/id
   
    private boolean isSetDate = false;
    private boolean isSetTime = false;
    private Calendar cal = Calendar.getInstance();
	
 	Intent intent = this.getIntent();
	HttpPostRequest request = new HttpPostRequest();
	DealWithDate dealWithDate = new DealWithDate();
	DatabaseHelper databaseHelper = new DatabaseHelper(MyApplication.getInstance(), "gtask", 1, dbCreate, "memo");
    public Lists list =new Lists(MyApplication.getInstance());
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.storeup_layout);
       
       builder = new AlertDialog.Builder(this);
       init();
    }
    @Override
    public void onResume()
    {
    	super.onResume();
    	builder = new AlertDialog.Builder(this);
    	init();
    }
    
	private void init()
    {
    		
    	listView = (MyDragListView)this.findViewById(R.id.storeUpList);
    	List<String> titles = new ArrayList<String>();
    	List<Integer> drawables = new ArrayList<Integer>();
    	List<Integer> idList = new ArrayList<Integer>();
    	List<String> top = new ArrayList<String>();
    	Log.v("show", "init");
    	
    	databaseHelper.open();
    	
    	List<Memo> lists = databaseHelper.selectAll();
    	listView = (MyDragListView)findViewById(R.id.storeUpList);
    	List<Integer> va = new ArrayList<Integer>();
    	//List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();

		int i = 0;
    	for (Memo memo : lists) {
			//Map<String,String> map = new HashMap<String, String>();
			//map.put("title", memo.getTitle());
			//map.put("content", memo.getContent());
			//resultList.add(map);
			//Log.v("show",memo.getTitle());
    		cal.setTimeInMillis(memo.getDue_date());
			titles.add(i+1+"---"+(memo.getItem_content().toString().length()>10?memo.getItem_content().toString().substring(0, 10):memo.getItem_content().toString()+"..."+"\n"+"结束时间："+cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"..."+cal.get(Calendar.HOUR_OF_DAY)+"："+cal.get(Calendar.MINUTE)));
			Log.v("tag", cal.getTimeInMillis()+"value");
			drawables.add(R.drawable.event);
			top.add("未完成");
			idList.add(i+1);
			va.add(i++);
		}
    	
    	adapter = new DragListAdapter(this, titles,top, drawables,lists);
    	
       	listView.setAdapter(adapter);
    	
    	listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


			AlertDialog builderCreate = null;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				//Log.v("log", ""+arg2+"."+((DragListAdapter.ViewData<String, Integer>)arg1.getTag()).value);
				builder.setTitle("修改备忘");
				final View reset = (View)getLayoutInflater().inflate(R.layout.event_reset_layout, null);
				builder.setView(reset);
				Memo memo = (Memo)arg1.getTag();
				final EditText con = (EditText)reset.findViewById(R.id.content);
				final Button btn = (Button)reset.findViewById(R.id.del);
				
				
				starred=memo.getStarrted();
				if(starred == 0)
				{
					((RadioButton)reset.findViewById(R.id.radio0)).setChecked(true);
				}else if(starred == 1)
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
						
						SharedPreferences preference = MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
						int position = preference.getInt("lastPosition", 0);
								list.open();
				         Cursor cursor=list.getItem(position+1);
				         cursor.moveToFirst();
				         curList = cursor.getString(0);
				         cursor.close();
				         
				         list.close();
						List<Memo> list=databaseHelper.selectByKey(arg2+1);
						for (Memo memo :list){
							Log.v("从这里"+arg2+"查询到的item_id",memo.getItem_id()+"kongde" );
							String item_id=memo.getItem_id();
							databaseHelper.deleteById(item_id);
							int result = request.deleteEvent(curList, item_id);
							if (result == 0)
								Toast.makeText(StoreUpActivity.this, "删除成功", Toast.LENGTH_LONG).show();
							else if(result == 11)
								Toast.makeText(StoreUpActivity.this, "下层有数据", Toast.LENGTH_LONG).show();
							else 
								Toast.makeText(StoreUpActivity.this, "未知原因，删除失败", Toast.LENGTH_LONG).show();
						}
						init();
						
						StoreUpActivity.this.closeOptionsMenu();
						builderCreate.dismiss();
					}
				});
				builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
										
						 
						 List<Memo> list = databaseHelper.selectByKey(arg2+1);
						 for (Memo memo1:list){
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
							 request.modifyEventContent(memo1.getItem_id(), memo.getItem_content());
//							 request.modifyStarrted(memo1.getItem_id());
							 if (starred==1 && ((RadioButton)reset.findViewById(R.id.radio0)).isChecked())
								 request.modifyStarrted(memo1.getItem_id());
							 else if (starred==0 && ((RadioButton)reset.findViewById(R.id.radio1)).isChecked())
								 request.modifyStarrted(memo1.getItem_id());
							 if (isSetDate){
								 if (request.modifyDate(memo1.getItem_id(), dealWithDate.dateToStr(cal.getTime()))==0){
									 modifyDateTime.setTime(memo1.getDue_date());
									 
									 modifyTimeStr=dealWithDate.timeToStr(modifyDateTime);
									 
									 Log.v("cur dateTime","cur dateTime"+modifyTimeStr);
									 modifyDateStr =dealWithDate.dateToStr(cal.getTime());
									 memo.setDue_date(dealWithDate.strToDateLong(modifyDateStr+" "+modifyTimeStr).getTime());
									 databaseHelper.updata(memo);
								 }
							 }
							 if (isSetTime){
								 if (request.modifyTime(memo1.getItem_id(), dealWithDate.timeToStr(cal.getTime()))==0){
									 
									 modifyDateTime.setTime(memo1.getDue_date());
									 
									 modifyDateStr=dealWithDate.dateToStr(modifyDateTime);
									 
									 modifyTimeStr =dealWithDate.timeToStr(cal.getTime());
									 memo.setDue_date(dealWithDate.strToDateLong(modifyDateStr+" "+modifyTimeStr).getTime());
									 databaseHelper.updata(memo);
								 }
							 }
						 } 
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
				
				Button modify_time = (Button)reset.findViewById(R.id.modifyTimeBtn);
				modify_time.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDialog(DIALOG_TIME);
					}
				});
				
				Button modify_date = (Button)reset.findViewById(R.id.modifyDateBtn);
				modify_date.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDialog(DIALOG_DATE);
					}
				});
			}
    		
		});
    	


    	//注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的实现长按是的菜单
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
		       public void onCreateContextMenu(ContextMenu menu, View v,
		              ContextMenuInfo menuInfo) {
		                  menu.add(0, 0, 0, "添加笔记");
		                  menu.add(0, 1, 0, "查看笔记");
		              }
		});
		
//    	listView.setOnItemLongClickListener(new OnItemLongClickListener() {
//    	 	
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				
//	    	 	AlertDialog builderCreate = null;
//
//				View add_new_note = (View)getLayoutInflater().inflate(R.layout.add_modify_note_layout, null);
//
//	    	 	
//	    	 	TextView create_time_label = (TextView)add_new_note.findViewById(R.id.note_date_label);
//				TextView create_time = (TextView)add_new_note.findViewById(R.id.note_date);
//				final EditText title = (EditText)add_new_note.findViewById(R.id.note_title);
//				final EditText content = (EditText)add_new_note.findViewById(R.id.note_content);
//				
//				final SharedPreferences prefer = MyApplication.getInstance().getSharedPreferences("store",Context.MODE_WORLD_READABLE);
//	    		final String note_id = prefer.getString("note_id", "");
//	    		
//        		builder.setIcon(R.drawable.ic_launcher);
//				builder.setTitle("笔记");
//				builder.setView(add_new_note);
//				
//				databaseHelper.open();
//				List<Memo> list = databaseHelper.selectByKey(position+1);
//				
//				for (Memo memo1:list){
//					 event_id_longClick = memo1.getItem_id();
//					 getResult=request.getNote(memo1.getItem_id());
//					 if (!note_id.equals("")){
//						 
//						 create_time_label.setVisibility(0);
//
//						 create_time.setVisibility(0);
//
//						 create_time.setText(prefer.getString("note_create_time", ""));
//						 
//
//						 title.setText(prefer.getString("note_title", ""));
//						 
//						 content.setText(prefer.getString("note_content", ""));
//					 } 
//				 }
//				
//				builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						// TODO Auto-generated method stub
//						if (!note_id.equals("")){
//							if (request.modifyNote(prefer.getString("note_id", ""), title.getText().toString(), content.getText().toString())==0){
//								Toast.makeText(MyApplication.getInstance(), "修改成功~", Toast.LENGTH_LONG).show();
//							}
//							else
//								Toast.makeText(MyApplication.getInstance(), "修改失败~", Toast.LENGTH_LONG).show();
//						}
//						else{
//							if (request.addNewNote(event_id_longClick, title.getText().toString(), content.getText().toString())==0)
//								Toast.makeText(MyApplication.getInstance(), "添加成功~", Toast.LENGTH_LONG).show();
//							else
//								Toast.makeText(MyApplication.getInstance(), "添加失败~", Toast.LENGTH_LONG).show();
//						}
//					}
//				});
//				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//				databaseHelper.close();
//				builderCreate =  builder.create();
//				builderCreate.show();//从工厂中取得dialog对象
//
//        	
//				return true;
//			}
//		});

    	//SimpleAdapter adapter = new SimpleAdapter(this, resultList, R.layout.line, new String[]{"title","content"}, new int[]{R.id.title,R.id.content});
    	//listView.setAdapter(adapter);
    	
		
		
    	
    	databaseHelper.close();

    }

	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {
		 
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
			                                .getMenuInfo();
			final int pos = (int) info.position;// 这里的info.id对应的就是数据库中rowId的值，position对应的第一个被点击

			
			Log.v("chang an shijian ", "pos:"+pos+1);
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
		                	 		
		                	 		
			                		Intent intent = new Intent(StoreUpActivity.this,NoteListActivity.class);
			                		intent.putExtra("event_id", event_id);
			                		startActivity(intent);
		                	 	}
		                	 	break;
	        		        	
		 
		                default:
		                        break;
		                }
		    
		                return true;
		 
		        }
	
	
	
	
	
	
	@Override
    protected Dialog onCreateDialog(int id) {  
        Dialog dialog=null;  
        switch (id) {  
        case DIALOG_TIME:  
            dialog=new TimePickerDialog(  
                    this,   
                    new TimePickerDialog.OnTimeSetListener(){  
                        public void onTimeSet(TimePicker timePicker, int hourOfDay,int minute) {  
                            
                           
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            cal.set(Calendar.MINUTE, minute);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
          
                            isSetTime = true;
                          // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);       
                            //设置闹钟，当前时间就唤醒  
                           
                        }  
                    },   
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),   
                    Calendar.getInstance().get(Calendar.MINUTE),  
                    false);  
 
            break;  
        case DIALOG_DATE:
        	dialog = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
				
					isSetDate = true;
					cal.set(Calendar.YEAR, arg1);
					cal.set(Calendar.MONTH, arg2);
					cal.set(Calendar.DAY_OF_MONTH, arg3);
					
				}
			}, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        	break;
        }  
        return dialog;  
    }


 
}
