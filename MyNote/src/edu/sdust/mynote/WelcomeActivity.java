package edu.sdust.mynote;


import java.util.Date;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.function.DealWithDate;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import edu.sdust.mynote.service.HttpRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler; 

public class WelcomeActivity extends Activity {    

    private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒 
    private HttpPostRequest request = new HttpPostRequest();

    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.welcome_layout); 
        
        new Handler().postDelayed(new Runnable(){ 

         @Override 
         public void run() { 
        	 SharedPreferences firstStart = getSharedPreferences("MyNote.ini", 0);
             Boolean user_first = firstStart.getBoolean("FIRST",true);        
        	 if (user_first){
        		 firstStart.edit().putBoolean("FIRST", false).commit();  
        		 

                 
        		 Intent guideIntent = new Intent(WelcomeActivity.this,GuideActivity.class); 
                 WelcomeActivity.this.startActivity(guideIntent); 
                 WelcomeActivity.this.finish(); 
        	 }
        	 else{
        		    SharedPreferences preferences = getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        	        String read_username = preferences.getString("username", "");
        	        String read_password = preferences.getString("password", "");
        	        if(read_username != "" && read_password != ""){
        	        	
        	        	if(HttpRequest.isNetworkAvailable(WelcomeActivity.this) == false){
        	        		new AlertDialog.Builder(WelcomeActivity.this)  
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
        	                        WelcomeActivity.this.finish();
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
        	        		request.sendPostForLogin(read_username, read_password);
            	        	request.getAllList();
            	        	Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class); 
                            WelcomeActivity.this.startActivity(mainIntent); 
                            WelcomeActivity.this.finish();
        	        	}	
        	        }
        	        else{
               		 Intent loginIntent = new Intent(WelcomeActivity.this,LoginActivity.class); 
                     WelcomeActivity.this.startActivity(loginIntent); 
                     WelcomeActivity.this.finish();
        	        }
        	 }
         } 
        }, SPLASH_DISPLAY_LENGHT); 
    } 
}

