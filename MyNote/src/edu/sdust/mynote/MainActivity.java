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
        	loginBtn.setText("�û���½");
        }
        
        loginBtn.setOnClickListener(new OnClickListener(){
        	
        	@Override
        	public void onClick(View v){
        		Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        		startActivity(loginIntent);
        	}
        });
             
    }//end onCreate
    
    
    

    //�������Activityʱ�Զ�ִ��
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
        	loginBtn.setText("�û���½");
        }
        
        loginBtn.setOnClickListener(new OnClickListener(){
        	
        	@Override
        	public void onClick(View v){
        		Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        		startActivityForResult(loginIntent,0);
        	}
        });
	}



    //�뿪���Activityʱ�Զ�ִ��
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
    
    
    //����������ߵķ��ذ����Ƿ��������Ҫ�޸ĳɺ�̨���У���������ѵĵĹ���
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK )
		{
			// �����˳��Ի���
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// ���öԻ������
			isExit.setTitle("ϵͳ��ʾ");
			// ���öԻ�����Ϣ
			isExit.setMessage("ȷ��Ҫ�˳���");
			// ���ѡ��ť��ע�����
			isExit.setButton("ȷ��", listener);
			isExit.setButton2("ȡ��", listener);
			// ��ʾ�Ի���
			isExit.show();

		}
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
				System.exit(0);
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			default:
				break;
			}
		}
	};	

    
}//end Activity