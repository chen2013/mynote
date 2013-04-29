package edu.sdust.mynote;

import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShareListActivity extends Activity {

	
	HttpPostRequest request = new HttpPostRequest();
	Lists list = new Lists(MyApplication.getInstance());
	
	String curList=null;
	int flag=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_list_layout);
		
		Button share_back_btn = (Button)findViewById(R.id.share_list_back_btn);
		share_back_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button share_commit_btn = (Button)findViewById(R.id.share_list_commit_btn);
		share_commit_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences preference = MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
				int position = preference.getInt("position", 0);
						list.open();
		         Cursor cursor=list.getItem(position+1);
		         cursor.moveToFirst();
		         curList = cursor.getString(0);
		         cursor.close();
		         
		         list.close();
		         
		         EditText toWhom = (EditText)findViewById(R.id.share_to_whom);
		         
		         String format = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		         
		         
		         if (toWhom.getText().toString().matches(format)){
		        	 flag=request.shareList(curList, toWhom.getText().toString());
			         switch(flag){
			         
			         	case 0:
			         		Toast.makeText(MyApplication.getInstance(), "分享成功",Toast.LENGTH_LONG).show();
			         		finish();
			         	default :
			         		Toast.makeText(MyApplication.getInstance(), "您输入的好友名称有误，请核实",Toast.LENGTH_LONG).show();
			         		toWhom.setText("");
			         		toWhom.getSelectionStart();
			         }
		         }
		         else{
		        	 Toast.makeText(MyApplication.getInstance(), "好友用户名必须是邮箱格式",Toast.LENGTH_LONG).show();
		         	 toWhom.setText("");
		         	 toWhom.getSelectionStart();
		         }
   
			}
			
		});
	}

}
