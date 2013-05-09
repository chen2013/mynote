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
				//Toast.makeText(MyApplication.getInstance(), "ԭʼ���벻��Ϊ��", Toast.LENGTH_LONG).show();
				
				if (pwdOld.getText().toString().equals(""))
					Toast.makeText(MyApplication.getInstance(), "ԭʼ���벻��Ϊ��", Toast.LENGTH_LONG).show();
				else if(pwdNew.getText().toString().equals(""))
					Toast.makeText(MyApplication.getInstance(), "�����벻��Ϊ��", Toast.LENGTH_LONG).show();
				else if(pwdNewAgain.getText().toString().equals(""))
					Toast.makeText(MyApplication.getInstance(), "�ٴ����벻��Ϊ��", Toast.LENGTH_LONG).show();
				else if (!(pwdNew.getText().toString().equals(pwdNewAgain.getText().toString())))
					Toast.makeText(MyApplication.getInstance(), "�������벻һ��", Toast.LENGTH_LONG).show();
				else{
					
					result = request.modifyPwd(pwdOld.getText().toString(), pwdNewAgain.getText().toString());
					
					if(result==0){
						
						Toast.makeText(MyApplication.getInstance(), "�����޸ĳɹ�~~", Toast.LENGTH_LONG).show();
						SharedPreferences pref = MyApplication.getInstance().getSharedPreferences("store",Context.MODE_WORLD_WRITEABLE);
						Editor editor = pref.edit();
						editor.putString("password", pwdNewAgain.getText().toString());
						editor.commit();
						
						finish();
					}
					else if(result == 6)
						Toast.makeText(MyApplication.getInstance(), "ԭ���벻��ȷ~~", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(MyApplication.getInstance(), "����δ֪����~~", Toast.LENGTH_LONG).show();
				}
			}
			
		});
	}

}
