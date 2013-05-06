package edu.sdust.mynote;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.database.DatabaseHelper;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.function.DealWithDate;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.receiver.AlarmReceiver;
import edu.sdust.mynote.service.HttpPostRequest;
public class AddNewEventActivity extends Activity {


	public static String dbCreate = "create table if not exists memo (item_id text primary key,item_content string,create_time long,starrted integer,due_date long,completed integer,repeat_type integer)";
	DatabaseHelper databaseHelper = new DatabaseHelper(AddNewEventActivity.this,"gtask",1,dbCreate,"memo");
	
	private Button save,reset;
	private EditText title,content;
	private CheckBox checkBox;
	private Button setBtn,setDataBtn;
	private AlarmManager alarmManager=null; 
	private HttpPostRequest request=new HttpPostRequest();
	private DealWithDate dealWithDate = new DealWithDate();
	private Lists list = new Lists(MyApplication.getInstance());
    
    final int DIALOG_TIME = 0;    //设置对话框id  
    final int DIALOG_DATA = 1;	//dialog/id
   
    private boolean isSetData = false;
    private boolean isSetTime = false;
    private Calendar cal;
    private String starred="false";
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.add_new_event_layout);
       alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);//取得系统闹钟服务
       findView();
       initListen();
       init();
       
       
       Button add_event_back=(Button)findViewById(R.id.add_event_back_btn);
       add_event_back.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
    	   
       });
       
       Log.v("ate", ""+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+"/"+Calendar.getInstance().get(Calendar.MINUTE)+"/"+Calendar.getInstance().get(Calendar.YEAR)+"/"+Calendar.getInstance().get(Calendar.MONTH));
    }
    public void initListen()
    {
    	save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				databaseHelper.open();
				Memo memo = new Memo();
				
				EditText event_content = (EditText)findViewById(R.id.event_content);
				memo.setItem_content(event_content.getText().toString());
				memo.setCreate_time(cal.getTimeInMillis());
				memo.setDue_date(cal.getTimeInMillis());
				memo.setCompleted(0);
				if(((RadioButton)findViewById(R.id.radio0)).isChecked())
				{
					memo.setStarrted(0);
					starred="false";
				}else if(((RadioButton)findViewById(R.id.radio1)).isChecked())
				{
					memo.setStarrted(1);
					starred="true";
				}else
				{
					memo.setStarrted(2);
					starred="false";
				}
				memo.setRepeat_type(0);
				
				//获得当前处在那个list中
				SharedPreferences prefer=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
		        int position = prefer.getInt("lastPosition", 0);
		        
		        Log.v("xianzai de position", "position"+position);
		         
		         
		        list.open();
		        Cursor cursor=list.getItem(position+1);
		        String curList = cursor.getString(0);
		        cursor.close();
		        list.close();
				

//				request.addNewEvent(memo.getItem_content(), curList);
//				
//				String event_id=prefer.getString("newEvent", "0");
//				
//				memo.setItem_id(event_id);
//				
//				databaseHelper.insertData(memo);
//				databaseHelper.close();
//
//				Intent intent = new Intent(MyApplication.getInstance(), AlarmReceiver.class);    //创建Intent对象  
//				intent.putExtra("memo", new String[]{memo.getItem_content(),String.valueOf(memo.getStarrted()),String.valueOf(memo.getDue_date())});
//				Log.v("to broadcast", memo.getItem_content()+","+String.valueOf(memo.getStarrted())+","+String.valueOf(memo.getDue_date()));
//				intent.putExtra("event_id", event_id);
//                PendingIntent pi = PendingIntent.getBroadcast(MyApplication.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);    //创建PendingIntent 
                
		        if(checkBox.isChecked() == false) 
            	{
            		if (request.addNewEvent(memo.getItem_content(), curList,dealWithDate.dateToStr(cal.getTime()),dealWithDate.timeToStr(cal.getTime()),starred)!=0){
            			Toast.makeText(MyApplication.getInstance(), "添加失败~", Toast.LENGTH_LONG).show();
            			databaseHelper.close();
            			finish();
            		}
    				String event_id=prefer.getString("newEvent", "0");
    				
    				memo.setItem_id(event_id);
    				
    				databaseHelper.insertData(memo);
    				databaseHelper.close();

//            		Date date = new Date();
//                	request.modifyDate(event_id, dealWithDate.dateToStr(date));
//                	request.modifyTime(event_id,"10:00:00");
                		
            	}
		        else if(!isSetData && !isSetTime)
	            	{	
		        		cal.add(Calendar.DAY_OF_MONTH,1);
	                	if (request.addNewEvent(memo.getItem_content(), curList,dealWithDate.dateToStr(cal.getTime()),"10:00:00",starred)!=0){
	            			Toast.makeText(MyApplication.getInstance(), "添加失败~", Toast.LENGTH_LONG).show();
	            			databaseHelper.close();
	            			finish();
	            		}
	    				String event_id=prefer.getString("newEvent", "0");
	    				
	    				memo.setItem_id(event_id);
	    				
	    				databaseHelper.insertData(memo);
	    				databaseHelper.close();
	    				
//	                	Date date = new Date();
//	                	request.modifyDate(event_id, dealWithDate.dateToStr(date));
//	                	request.modifyTime(event_id,"10:00:00");
	                	
		        }
            	else 
            	{
            		
//            		request.modifyDate(event_id, dealWithDate.dateToStr(cal.getTime()));
//            		request.modifyTime(event_id, dealWithDate.timeToStr(cal.getTime()));
//            		request.addNewEvent(memo.getItem_content(), curList);
    				
            		Date date = cal.getTime();
            		
            		if (request.addNewEvent(memo.getItem_content(), curList,dealWithDate.dateToStr(date),dealWithDate.timeToStr(date),starred)!=0){
            			Toast.makeText(MyApplication.getInstance(), "添加失败~", Toast.LENGTH_LONG).show();
            			databaseHelper.close();
            			AddNewEventActivity.this.finish();
            		}
    				String event_id=prefer.getString("newEvent", "0");
    				
    				Log.v("dateformat", dealWithDate.dateToStr(date)+"  "+dealWithDate.timeToStr(date));
    				
    				memo.setItem_id(event_id);
    				
    				databaseHelper.insertData(memo);
    				databaseHelper.close();

    				Intent intent = new Intent(MyApplication.getInstance(), AlarmReceiver.class);    //创建Intent对象  
    				intent.putExtra("memo", new String[]{memo.getItem_content(),String.valueOf(memo.getStarrted()),String.valueOf(memo.getDue_date())});
    				Log.v("to broadcast", memo.getItem_content()+","+String.valueOf(memo.getStarrted())+","+String.valueOf(memo.getDue_date()));
    				intent.putExtra("event_id", event_id);
                    PendingIntent pi = PendingIntent.getBroadcast(MyApplication.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);    //创建PendingIntent 
//            		
            		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            		Log.v("tag","send");
            	}
                 
                isSetData = isSetTime = false;
				Log.v("tag", ""+System.currentTimeMillis());
				Log.v("tag", ""+cal.getTimeInMillis());
				finish();
			}
		});
    	reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				databaseHelper.open();
				databaseHelper.deleteAll("memo");
				databaseHelper.deleteAll("memo01");
				databaseHelper.close();
			}
		});
    	checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
					setBtn.setEnabled(isChecked);
					setDataBtn.setEnabled(isChecked);
			}
		});
    	setBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_TIME);
			}
		});
    	setDataBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_DATA);
			}
		});
    }
    private void init()
    {
    	cal = Calendar.getInstance();
    	cal.setTimeInMillis(System.currentTimeMillis());
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
                            
                           
                            if(!isSetData)
                            {
                            	cal.setTimeInMillis(System.currentTimeMillis());
                            }
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
        case DIALOG_DATA:
        	dialog = new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
				
					isSetData = true;
					if(!isSetTime)
					{
						cal.setTimeInMillis(System.currentTimeMillis());
						cal.add(Calendar.HOUR_OF_DAY, 2);
					}
					cal.set(Calendar.YEAR, arg1);
					cal.set(Calendar.MONTH, arg2);
					cal.set(Calendar.DAY_OF_MONTH, arg3);
					
				}
			}, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        	break;
        }  
        return dialog;  
    }  
    private void findView()
    {
    	save = (Button)findViewById(R.id.save);
    	reset = (Button)findViewById(R.id.reset);
    	title = (EditText)findViewById(R.id.title);
    	content = (EditText)findViewById(R.id.content);
    	checkBox = (CheckBox)findViewById(R.id.open);
    	setBtn = (Button)findViewById(R.id.setBtn);
    	setDataBtn = (Button)findViewById(R.id.setDataBtn);
    }


}
