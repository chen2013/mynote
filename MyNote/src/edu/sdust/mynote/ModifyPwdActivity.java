package edu.sdust.mynote;

import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyPwdActivity extends Activity {

	int result =20;
	EditText pwdOld;
	EditText pwdNew;
	EditText pwdNewAgain;
	Button back;
	Button commit;
	HttpPostRequest request=new HttpPostRequest();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modify_pwd_layout);
		
		
		pwdOld = (EditText)findViewById(R.id.modify_pwd_old);
		
		pwdNew = (EditText)findViewById(R.id.modify_pwd_new);
		
		pwdNewAgain = (EditText)findViewById(R.id.modify_pwd_new_again);
			
		back = (Button)findViewById(R.id.modify_pwd_back_btn);
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
	
		commit = (Button)findViewById(R.id.modify_pwd_commit_btn);
		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(MyApplication.getInstance(), "原始密码不能为空", Toast.LENGTH_LONG).show();
				
				if (pwdOld.getText().toString().equals(""))
					Toast.makeText(MyApplication.getInstance(), "原始密码不能为空", Toast.LENGTH_LONG).show();
				else if(pwdNew.getText().toString().equals(""))
					Toast.makeText(MyApplication.getInstance(), "新密码不能为空", Toast.LENGTH_LONG).show();
				else if(pwdNewAgain.getText().toString().equals(""))
					Toast.makeText(MyApplication.getInstance(), "再次输入不能为空", Toast.LENGTH_LONG).show();
				else if (!(pwdNew.getText().toString().equals(pwdNewAgain.getText().toString())))
					Toast.makeText(MyApplication.getInstance(), "两次输入不一致", Toast.LENGTH_LONG).show();
				else{
					
					result = request.modifyPwd(pwdOld.getText().toString(), pwdNewAgain.getText().toString());
					
					if(result==0){
						
						Toast.makeText(MyApplication.getInstance(), "密码修改成功~~", Toast.LENGTH_LONG).show();
						SharedPreferences pref = MyApplication.getInstance().getSharedPreferences("store",Context.MODE_WORLD_WRITEABLE);
						Editor editor = pref.edit();
						editor.putString("password", pwdNewAgain.getText().toString());
						editor.commit();
						
						finish();
					}
					else if(result == 6)
						Toast.makeText(MyApplication.getInstance(), "原密码不正确~~", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(MyApplication.getInstance(), "发生未知错误~~", Toast.LENGTH_LONG).show();
				}
			}
			
		});
	}

}
