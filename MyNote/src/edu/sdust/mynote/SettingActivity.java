package edu.sdust.mynote;

import edu.sdust.mynote.pull.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		
		mainFunction();
	}
	
	
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}





	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mainFunction();
	}





	private void mainFunction() {
		// TODO Auto-generated method stub
		Button back = (Button)findViewById(R.id.setting_back_btn);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
		TextView setting_clear  = (TextView)findViewById(R.id.setting_clear);
		setting_clear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences preference = MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_WRITEABLE);
				Editor editor = preference.edit();
				editor.putString("username", "");
				editor.putString("password", "");
				editor.commit();
				
				Toast.makeText(MyApplication.getInstance(), "Çå³ý³É¹¦!!", Toast.LENGTH_LONG).show();
			}
			
		});
		
		TextView setting_feedback = (TextView)findViewById(R.id.setting_feedback);
		setting_feedback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toFeedback = new Intent(SettingActivity.this,FeedbackActivity.class);
				startActivity(toFeedback);
			}
			
		});
		
		TextView setting_about = (TextView)findViewById(R.id.setting_about);
		setting_about.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toAbout = new Intent(SettingActivity.this,AboutUsActivity.class);
				startActivity(toAbout);
			}	
		});
		
		
		TextView setting_logout = (TextView)findViewById(R.id.setting_logout);
		setting_logout.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences preference = MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_WRITEABLE);
				Editor editor = preference.edit();
				editor.putString("username", "");
				editor.putString("password", "");
				editor.commit();
				
				Intent toLogin = new Intent(SettingActivity.this,LoginActivity.class);
				startActivity(toLogin);
				finish();
			}

		});
		
		TextView modifyPwd = (TextView)findViewById(R.id.setting_modify);
		modifyPwd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toModifyPwd = new Intent(SettingActivity.this,ModifyPwdActivity.class);
				startActivity(toModifyPwd);
			}
			
		});
	}

}
