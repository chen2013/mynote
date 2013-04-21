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

public class RegisterActivity extends Activity {
	
	
	int flag=20;
	String loginResult="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		
		Button regBackBtn=(Button)this.findViewById(R.id.register_back_btn);
		regBackBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		
		Button regCommitBtn=(Button)this.findViewById(R.id.register_commit_btn);
		regCommitBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText username=(EditText)findViewById(R.id.register_username);
				EditText userpwd=(EditText)findViewById(R.id.register_userpassword);
				EditText userpwd2=(EditText)findViewById(R.id.register_userpassword_again);
				
				String usernameText = username.getText().toString();
				String userpwdText = userpwd.getText().toString();
				String userpwd2Text = userpwd2.getText().toString();
				
				String format = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

				
				if(usernameText.matches(format))
				{
					if (userpwdText.equals(userpwd2Text)^true){
						Toast.makeText(RegisterActivity.this, "密码不一致!",Toast.LENGTH_LONG).show();
						userpwd.setText("");
						userpwd2.setText("");
					}
					else{
						HttpPostRequest loginRequest=new HttpPostRequest();
						flag = loginRequest.sendPostForRegister(usernameText,userpwdText);
						switch(flag){
							case 0:
								Toast.makeText(RegisterActivity.this, "注册成功!",Toast.LENGTH_LONG).show();
								storeAfterReg(usernameText,userpwdText);
								RegisterActivity.this.finish();
								break;
							case 3:
								Toast.makeText(RegisterActivity.this, "用户名已存在!",Toast.LENGTH_LONG).show();
								username.setText("");
								userpwd.setText("");
								userpwd2.setText("");
								break;
							default:
								Toast.makeText(RegisterActivity.this, "发生未知错误!",Toast.LENGTH_LONG).show();
								break;	
						}
					}
				}
				else
				{
					Toast.makeText(RegisterActivity.this, "用户名格式不正确，请使用邮箱!",Toast.LENGTH_LONG).show();
					username.setText("");
				}			
			}//end onClick()	
		});//end regCommitBtn.setOnClickListener
		
		
	}//end onCreate()


	protected void storeAfterReg(String username, String userpwd) {
		// TODO Auto-generated method stub
			SharedPreferences preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE+ Context.MODE_WORLD_WRITEABLE);
			Editor editor = preferences.edit();
			editor.putString("username", username);
			editor.putString("password", userpwd);
			editor.commit();
	}
}
