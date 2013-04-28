package edu.sdust.mynote;

import edu.sdust.mynote.menu.AboutUsActivity;
import edu.sdust.mynote.menu.FeedbackActivity;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import edu.sdust.mynote.service.HttpRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	//数据定义部分
	
	private EditText username;
	private EditText password;
	private String r_username;
	private String r_password;
	private String flag;
	private boolean whether_save=true;
	boolean result;	
	boolean netcheck;
	private ProgressDialog proDialog;
	HttpPostRequest request = new HttpPostRequest();
	
	
	Handler checkHandler = new Handler() {
		public void handleMessage(Message msg) {
			result = msg.getData().getBoolean("ischeckError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (result == false) {
				Toast.makeText(LoginActivity.this, "验证失败，请检查您的用户名和密码！",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(LoginActivity.this, "绑定成功!",Toast.LENGTH_LONG).show();
				request.getAllList();
				Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
				startActivity(mainIntent);
				LoginActivity.this.finish();
			}
		}
	};
	Handler NetError = new Handler() {
		public void handleMessage(Message msg) {
			result = msg.getData().getBoolean("isNetError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (netcheck == false) {
				Toast.makeText(LoginActivity.this, "验证失败: 请检查您网络连接!",Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
	//代码正式开始部分
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
       
        
        //点击界面下方选项的按钮事件
	    ImageButton imageButton=(ImageButton)this.findViewById(R.id.login_option);
	    imageButton.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				openOptionsMenu();  
			}
		
	    });//end imageButton.setOnClickListener
	    
	    //注册新账号的点击事件
	    TextView regTextView=(TextView)this.findViewById(R.id.register_new_acc_id);
	    regTextView.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v){
	    		Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
	    		startActivity(regIntent);
	    	}
	    });//end regTextView.setOnClickListener
	    
	    
	    
	    
	    //获取是否已经存在用户和密码
	    username =(EditText)findViewById(R.id.login_edit_account);        
        password = (EditText)findViewById(R.id.login_edit_pwd);
        
        SharedPreferences preferences;
        preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String read_username = preferences.getString("username", "");
        String read_password = preferences.getString("password", "");
        if(read_username != ""){
        	username.setText(read_username);
        }
        if(read_password != ""){
        	password.setText(read_password);
        }
        
        
        //点击复选框的操作
        CheckBox checkbox=(CheckBox)this.findViewById(R.id.login_cb_savepwd);
        checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
         
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	        // TODO Auto-generated method stub
	        if(isChecked)
	        {
	        	Toast.makeText(LoginActivity.this, "被选中!",Toast.LENGTH_LONG).show();
	        	whether_save=true;
	        }
	        else
	        	Toast.makeText(LoginActivity.this, "取消选中!",Toast.LENGTH_LONG).show();
	        	whether_save=false;
	        }
        });//end setOnCheckedChangeListener

	    
        
        //点击登陆时的操作
        Button loginButton =(Button)this.findViewById(R.id.login_btn_login);
        loginButton.setOnClickListener(new OnClickListener(){

        	    @Override
        		public void onClick(View v) {
        			// TODO Auto-generated method stub
        			r_username =  username.getText().toString();
        			r_password =  password.getText().toString();
        			if(r_username.equals("") || r_password.equals("")){
        				Toast.makeText(LoginActivity.this, "您填写的数据不能为空!",Toast.LENGTH_LONG).show();
        			}else{
        	        	if(HttpRequest.isNetworkAvailable(LoginActivity.this) == false){
        	        		new AlertDialog.Builder(LoginActivity.this)  
        	                .setTitle("开启网络服务")  
        	                .setMessage("本软件需要使用网络资源，是否开启网络？")  
        	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
        	                      
        	                    @Override  
        	                    public void onClick(DialogInterface dialog, int which) {  
        	                        // Go to the activity of settings of wireless  
        	                        Intent intentToNetwork = new Intent("/");  
        	                        ComponentName componentName = new ComponentName(  
        	                                "com.android.settings",  
        	                                "com.android.settings.WirelessSettings"  
        	                        );  
        	                        intentToNetwork.setComponent(componentName);  
        	                        intentToNetwork.setAction("android.intent.action.VIEW");  
        	                        startActivity(intentToNetwork);  
       	                        dialog.cancel();  
        	                    }  
        	                })  
        	                .setNegativeButton("否", new DialogInterface.OnClickListener() {  
        	                      
        	                    @Override  
        	                    public void onClick(DialogInterface dialog, int which) {  
       	                        dialog.cancel();  
        	                    }  
        	                })  
        	                .show();  
        	        	}//end if
        	        	else{
        					proDialog = ProgressDialog.show(LoginActivity.this, "验证中..","验证中..请稍后....", true, true);     					
        					Thread loginThread = new Thread(new checkFailureHandler());
        					loginThread.start();
        	        	}//end else
        			}
        		}//onClick 函数结束
        });
    }//onCreate 函数结束
    
    
    
	
    
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
        setContentView(R.layout.login_layout);
       
        
        //点击界面下方选项的按钮事件
	    ImageButton imageButton=(ImageButton)this.findViewById(R.id.login_option);
	    imageButton.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				openOptionsMenu();  
			}
		
	    });//end imageButton.setOnClickListener
	    
	    //注册新账号的点击事件
	    TextView regTextView=(TextView)this.findViewById(R.id.register_new_acc_id);
	    regTextView.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v){
	    		Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
	    		startActivity(regIntent);
	    	}
	    });//end regTextView.setOnClickListener
	    
	    
	    
	    
	    //获取是否已经存在用户和密码
	    username =(EditText)findViewById(R.id.login_edit_account);        
        password = (EditText)findViewById(R.id.login_edit_pwd);
        
        SharedPreferences preferences;
        preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String read_username = preferences.getString("username", "");
        String read_password = preferences.getString("password", "");
        if(read_username != ""){
        	username.setText(read_username);
        }
        if(read_password != ""){
        	password.setText(read_password);
        }
        
        
        //点击复选框的操作
        CheckBox checkbox=(CheckBox)this.findViewById(R.id.login_cb_savepwd);
        checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
         
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	        // TODO Auto-generated method stub
	        if(isChecked)
	        {
	        	Toast.makeText(LoginActivity.this, "被选中!",Toast.LENGTH_LONG).show();
	        	whether_save=true;
	        }
	        else
	        	Toast.makeText(LoginActivity.this, "取消选中!",Toast.LENGTH_LONG).show();
	        	whether_save=false;
	        }
        });//end setOnCheckedChangeListener

	    
        
        //点击登陆时的操作
        Button loginButton =(Button)this.findViewById(R.id.login_btn_login);
        loginButton.setOnClickListener(new OnClickListener(){

        	    @Override
        		public void onClick(View v) {
        			// TODO Auto-generated method stub
        			r_username =  username.getText().toString();
        			r_password =  password.getText().toString();
        			if(r_username.equals("") || r_password.equals("")){
        				Toast.makeText(LoginActivity.this, "您填写的数据不能为空!",Toast.LENGTH_LONG).show();
        			}else{
        	        	if(HttpRequest.isNetworkAvailable(LoginActivity.this) == false){
        	        		new AlertDialog.Builder(LoginActivity.this)  
        	                .setTitle("开启网络服务")  
        	                .setMessage("本软件需要使用网络资源，是否开启网络？")  
        	                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
        	                      
        	                    @Override  
        	                    public void onClick(DialogInterface dialog, int which) {  
        	                        // Go to the activity of settings of wireless  
        	                        Intent intentToNetwork = new Intent("/");  
        	                        ComponentName componentName = new ComponentName(  
        	                                "com.android.settings",  
        	                                "com.android.settings.WirelessSettings"  
        	                        );  
        	                        intentToNetwork.setComponent(componentName);  
        	                        intentToNetwork.setAction("android.intent.action.VIEW");  
        	                        startActivity(intentToNetwork);  
       	                        dialog.cancel();  
        	                    }  
        	                })  
        	                .setNegativeButton("否", new DialogInterface.OnClickListener() {  
        	                      
        	                    @Override  
        	                    public void onClick(DialogInterface dialog, int which) {  
       	                        dialog.cancel();  
        	                    }  
        	                })  
        	                .show();  
        	        	}//end if
        	        	else{
        					proDialog = ProgressDialog.show(LoginActivity.this, "验证中..","验证中..请稍后....", true, true);     					
        					Thread loginThread = new Thread(new checkFailureHandler());
        					loginThread.start();
        	        	}//end else
        			}
        		}//onClick 函数结束
        });
	}//end onResume






	class checkFailureHandler implements Runnable {
		@Override
		public void run() {
			
			HttpPostRequest loginRequest=new HttpPostRequest();
			flag = loginRequest.sendPostForLogin(r_username,r_password);	//返回登陆是否成功标识
			if(flag.equals("0")){
				SharedPreferences preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE+ Context.MODE_WORLD_WRITEABLE);
				Editor editor = preferences.edit();
				editor.putString("username", r_username);
				if(true==whether_save)
					editor.putString("password", r_password);
				else
					editor.putString("password", "");
				editor.commit();
				result = true; 
			}else{
				result = false; 
			}
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("ischeckError", result);
			message.setData(bundle);
			checkHandler.sendMessage(message);
		}
	}//end checkFailureHandler



	public boolean checkNetwork(Context ctx){
		ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}//end checkNetwork
	
	
	
    
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
		//选择第一个菜单
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

		}//end if (keyCode == KeyEvent.KEYCODE_BACK )
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
				LoginActivity.this.finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};	//end OnClickListener

}//end Activity
