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

		}
		//���β˵���ť��Ϊʵ���Զ���˵���ť
		if(keyCode==KeyEvent.KEYCODE_MENU)
			return true;
		
		return false;
		
	}
	/**�����Ի��������button����¼�*/
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			switch (which)
			{
			case AlertDialog.BUTTON_POSITIVE:// "ȷ��"��ť�˳�����
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};	

}
