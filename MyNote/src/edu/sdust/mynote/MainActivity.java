package edu.sdust.mynote;

import edu.sdust.mynote.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView loginBtn=(TextView)this.findViewById(R.id.loginlabel);
        
        SharedPreferences preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String read_username = preferences.getString("username", "");
        String read_password = preferences.getString("password", "");
        if(read_username != "" && read_password != ""){
        	loginBtn.setText(read_username);
        }
        else{
        	loginBtn.setText("用户登陆");
        }
        
        loginBtn.setOnClickListener(new OnClickListener(){
        	
        	@Override
        	public void onClick(View v){
        		Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        		startActivity(loginIntent);
        	}
        });
             
    }//end onCreate
    
    
    

    //返回这个Activity时自动执行
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();      
        
		setContentView(R.layout.main);
        TextView loginBtn=(TextView)this.findViewById(R.id.loginlabel);
        
        SharedPreferences preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String read_username = preferences.getString("username", "");
        String read_password = preferences.getString("password", "");
        if(read_username != "" && read_password != ""){
        	loginBtn.setText(read_username);
        }
        else{
        	loginBtn.setText("用户登陆");
        }
        
        loginBtn.setOnClickListener(new OnClickListener(){
        	
        	@Override
        	public void onClick(View v){
        		Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        		startActivityForResult(loginIntent,0);
        	}
        });
	}



    //离开这个Activity时自动执行
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}   

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.activity_main, menu);
        return true;
    }//end onCreateOptionsMenu
    
    
    //监听程序里边的返回按键是否点击，随后要修改成后台运行，以完成提醒的的功能
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}
		//屏蔽菜单按钮，为实现自定义菜单按钮
		if(keyCode==KeyEvent.KEYCODE_MENU)
			return true;
		
		return false;
		
	}//end onKeyDown
    
    
	/**监听对话框里面的button点击事件*/
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				System.exit(0);
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};	

    
}//end Activity