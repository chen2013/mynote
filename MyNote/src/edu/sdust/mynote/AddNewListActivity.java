package edu.sdust.mynote;

import java.util.Date;
import edu.sdust.mynote.database.ListCount;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.function.DealWithDate;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddNewListActivity extends Activity {

	
	public int listCnt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_list_layout);
		
		Button commitBtn=(Button)this.findViewById(R.id.add_list_commit_btn);
		commitBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 //获取文本内容
				 EditText list_name=(EditText)findViewById(R.id.add_new_list_name);
				 
				 
				 //获取原本列表个数，加一后存储
				 SharedPreferences preferences_r= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
				 listCnt=preferences_r.getInt("listCount", 3);
				 
				 Log.v("listCnt_add",""+listCnt);
				 
				 listCnt=listCnt+1;
				 SharedPreferences preferences_w= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_WRITEABLE);
                 Editor editor = preferences_w.edit();
                 editor.putInt("listCount", listCnt);
                 editor.commit();
				 
				
				 //对数据库进行初始化
	       		 
	       		 DealWithDate dealWithDate =new DealWithDate();
	       		 
	       		 Lists listsDB=new Lists(AddNewListActivity.this);
	       		 listsDB.open();
	       		 Log.v("add_list_label1","open_success");
	       		 
	       		 //向数据库中插入数据
	       		 long id;
	       		 id = listsDB.insertItem(list_name.getText().toString(), dealWithDate.dateToStrLong(new Date()),0, "");     		 
					 
	       		 listsDB.close();
	       		 
	       		 finish();
	       		 
			}
		});
		
		
		Button backBtn=(Button)findViewById(R.id.add_list_back_btn);
		backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AddNewListActivity.this.finish();
			}
		});
		
	}

}
