package edu.sdust.mynote;

import java.util.Date;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.function.DealWithDate;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
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
	HttpPostRequest request=new HttpPostRequest();
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
				 
				 request.addNewList(list_name.getText().toString());

				 //获取原本列表个数，加一后存储
				 SharedPreferences preferences_r= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
				 listCnt=preferences_r.getInt("listCount", 0);
			 
				 listCnt=listCnt+1;
				 SharedPreferences preferences_w= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_WRITEABLE);
                 Editor editor = preferences_w.edit();
                 editor.putInt("listCount", listCnt);
                 editor.commit();
				 
				
				 //对数据库进行初始化
	       		 
	       		 DealWithDate dealWithDate =new DealWithDate();
	       		 
	       		 Lists listsDB=new Lists(MyApplication.getInstance());
	       		 listsDB.open();
	       		 //Log.v("add_list_label1","open_success");
	       		 
	       		 
	       		 //得到添加后从网络段得到的新列表的Id并添加到数据库中
				 SharedPreferences newList= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
				 String  list_id=newList.getString("newList", "");

	       		 listsDB.insertItem(list_id,list_name.getText().toString(), dealWithDate.dateToStrLong(new Date()),0, "0");     		 
					 
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
