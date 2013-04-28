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
	
	//���ݶ��岿��
	
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
				Toast.makeText(LoginActivity.this, "��֤ʧ�ܣ����������û��������룡",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(LoginActivity.this, "�󶨳ɹ�!",Toast.LENGTH_LONG).show();
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
				Toast.makeText(LoginActivity.this, "��֤ʧ��: ��������������!",Toast.LENGTH_LONG).show();
			}
		}
	};
	
	
	//������ʽ��ʼ����
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
       
        
        //��������·�ѡ��İ�ť�¼�
	    ImageButton imageButton=(ImageButton)this.findViewById(R.id.login_option);
	    imageButton.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				openOptionsMenu();  
			}
		
	    });//end imageButton.setOnClickListener
	    
	    //ע�����˺ŵĵ���¼�
	    TextView regTextView=(TextView)this.findViewById(R.id.register_new_acc_id);
	    regTextView.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v){
	    		Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
	    		startActivity(regIntent);
	    	}
	    });//end regTextView.setOnClickListener
	    
	    
	    
	    
	    //��ȡ�Ƿ��Ѿ������û�������
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
        
        
        //�����ѡ��Ĳ���
        CheckBox checkbox=(CheckBox)this.findViewById(R.id.login_cb_savepwd);
        checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
         
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	        // TODO Auto-generated method stub
	        if(isChecked)
	        {
	        	Toast.makeText(LoginActivity.this, "��ѡ��!",Toast.LENGTH_LONG).show();
	        	whether_save=true;
	        }
	        else
	        	Toast.makeText(LoginActivity.this, "ȡ��ѡ��!",Toast.LENGTH_LONG).show();
	        	whether_save=false;
	        }
        });//end setOnCheckedChangeListener

	    
        
        //�����½ʱ�Ĳ���
        Button loginButton =(Button)this.findViewById(R.id.login_btn_login);
        loginButton.setOnClickListener(new OnClickListener(){

        	    @Override
        		public void onClick(View v) {
        			// TODO Auto-generated method stub
        			r_username =  username.getText().toString();
        			r_password =  password.getText().toString();
        			if(r_username.equals("") || r_password.equals("")){
        				Toast.makeText(LoginActivity.this, "����д�����ݲ���Ϊ��!",Toast.LENGTH_LONG).show();
        			}else{
        	        	if(HttpRequest.isNetworkAvailable(LoginActivity.this) == false){
        	        		new AlertDialog.Builder(LoginActivity.this)  
        	                .setTitle("�����������")  
        	                .setMessage("�������Ҫʹ��������Դ���Ƿ������磿")  
        	                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
        	                      
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
        	                .setNegativeButton("��", new DialogInterface.OnClickListener() {  
        	                      
        	                    @Override  
        	                    public void onClick(DialogInterface dialog, int which) {  
       	                        dialog.cancel();  
        	                    }  
        	                })  
        	                .show();  
        	        	}//end if
        	        	else{
        					proDialog = ProgressDialog.show(LoginActivity.this, "��֤��..","��֤��..���Ժ�....", true, true);     					
        					Thread loginThread = new Thread(new checkFailureHandler());
        					loginThread.start();
        	        	}//end else
        			}
        		}//onClick ��������
        });
    }//onCreate ��������
    
    
    
	
    
    
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
       
        
        //��������·�ѡ��İ�ť�¼�
	    ImageButton imageButton=(ImageButton)this.findViewById(R.id.login_option);
	    imageButton.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				openOptionsMenu();  
			}
		
	    });//end imageButton.setOnClickListener
	    
	    //ע�����˺ŵĵ���¼�
	    TextView regTextView=(TextView)this.findViewById(R.id.register_new_acc_id);
	    regTextView.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v){
	    		Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
	    		startActivity(regIntent);
	    	}
	    });//end regTextView.setOnClickListener
	    
	    
	    
	    
	    //��ȡ�Ƿ��Ѿ������û�������
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
        
        
        //�����ѡ��Ĳ���
        CheckBox checkbox=(CheckBox)this.findViewById(R.id.login_cb_savepwd);
        checkbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
         
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
	        // TODO Auto-generated method stub
	        if(isChecked)
	        {
	        	Toast.makeText(LoginActivity.this, "��ѡ��!",Toast.LENGTH_LONG).show();
	        	whether_save=true;
	        }
	        else
	        	Toast.makeText(LoginActivity.this, "ȡ��ѡ��!",Toast.LENGTH_LONG).show();
	        	whether_save=false;
	        }
        });//end setOnCheckedChangeListener

	    
        
        //�����½ʱ�Ĳ���
        Button loginButton =(Button)this.findViewById(R.id.login_btn_login);
        loginButton.setOnClickListener(new OnClickListener(){

        	    @Override
        		public void onClick(View v) {
        			// TODO Auto-generated method stub
        			r_username =  username.getText().toString();
        			r_password =  password.getText().toString();
        			if(r_username.equals("") || r_password.equals("")){
        				Toast.makeText(LoginActivity.this, "����д�����ݲ���Ϊ��!",Toast.LENGTH_LONG).show();
        			}else{
        	        	if(HttpRequest.isNetworkAvailable(LoginActivity.this) == false){
        	        		new AlertDialog.Builder(LoginActivity.this)  
        	                .setTitle("�����������")  
        	                .setMessage("�������Ҫʹ��������Դ���Ƿ������磿")  
        	                .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
        	                      
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
        	                .setNegativeButton("��", new DialogInterface.OnClickListener() {  
        	                      
        	                    @Override  
        	                    public void onClick(DialogInterface dialog, int which) {  
       	                        dialog.cancel();  
        	                    }  
        	                })  
        	                .show();  
        	        	}//end if
        	        	else{
        					proDialog = ProgressDialog.show(LoginActivity.this, "��֤��..","��֤��..���Ժ�....", true, true);     					
        					Thread loginThread = new Thread(new checkFailureHandler());
        					loginThread.start();
        	        	}//end else
        			}
        		}//onClick ��������
        });
	}//end onResume






	class checkFailureHandler implements Runnable {
		@Override
		public void run() {
			
			HttpPostRequest loginRequest=new HttpPostRequest();
			flag = loginRequest.sendPostForLogin(r_username,r_password);	//���ص�½�Ƿ�ɹ���ʶ
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
	
	
	
    
    //�Բ˵�������д
    @Override  
	public boolean onCreateOptionsMenu(Menu menu) {
		  menu.add(Menu.NONE, Menu.FIRST + 1, 1, "�������");
		 // setIcon()����Ϊ�˵�����ͼ�꣬����ʹ�õ���ϵͳ�Դ���ͼ�꣬����һ��,��      
		 // android.R��ͷ����Դ��ϵͳ�ṩ�ģ������Լ��ṩ����Դ����R��ͷ��        
		  menu.add(Menu.NONE, Menu.FIRST + 2, 2, "��������"); 
		return true;  
		        }   
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{        
		//ѡ���һ���˵�
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
			// �����˳��Ի���
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// ���öԻ������
			isExit.setTitle("ϵͳ��ʾ����");
			// ���öԻ�����Ϣ
			isExit.setMessage("ȷ��Ҫ�˳���½��");
			// ���ѡ��ť��ע�����
			isExit.setButton("��Ȼ", listener);
			isExit.setButton2("�Ų�", listener);
			// ��ʾ�Ի���
			isExit.show();

		}//end if (keyCode == KeyEvent.KEYCODE_BACK )
		//���β˵���ť��Ϊʵ���Զ���˵���ť
		if(keyCode==KeyEvent.KEYCODE_MENU)
			return true;
		
		return false;
		
	}//end onKeyDown
    
    
	/**�����Ի��������button����¼�*/
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				LoginActivity.this.finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};	//end OnClickListener

}//end Activity
