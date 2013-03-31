package edu.sdust.mynote;
import edu.sdust.mynote.menu.AboutUsActivity;
import edu.sdust.mynote.menu.FeedbackActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class LoginActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
       
        
    ImageButton imageButton=(ImageButton)this.findViewById(R.id.login_option);
    imageButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			openOptionsMenu();  
		}
	
    });
    
    Button loginButton=(Button)this.findViewById(R.id.login_btn_login);
    loginButton.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
			startActivity(mainIntent);
			LoginActivity.this.finish();
		}
    	
    });
    
    }
    
    //对菜单进行重写
    @Override  
	public boolean onCreateOptionsMenu(Menu menu) {
		  menu.add(Menu.NONE, Menu.FIRST + 1, 1, "意见反馈");
		 // setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，留意一下,以      
		 // android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的        
		  menu.add(Menu.NONE, Menu.FIRST + 2, 2, "关于我们"); 
		return true;  
		        }   
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{        
		case Menu.FIRST + 1: 			
			Intent feedback =new Intent(LoginActivity.this,FeedbackActivity.class);
			startActivity(feedback); 
			break;
		case Menu.FIRST + 2: 
			Intent aboutUs =new Intent(LoginActivity.this,AboutUsActivity.class);
			startActivity(aboutUs); 
			break;     
		} 
		return false;  
		}
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示！！");
			// 设置对话框消息
			isExit.setMessage("确定要退出登陆吗？");
			// 添加选择按钮并注册监听
			isExit.setButton("当然", listener);
			isExit.setButton2("才不", listener);
			// 显示对话框
			isExit.show();

		}
		//屏蔽菜单按钮，为实现自定义菜单按钮
		if(keyCode==KeyEvent.KEYCODE_MENU)
			return true;
		
		return false;
		
	}
	/**监听对话框里面的button点击事件*/
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};	

}
