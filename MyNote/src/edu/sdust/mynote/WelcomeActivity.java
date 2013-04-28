package edu.sdust.mynote;


import java.util.Date;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.function.DealWithDate;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler; 

public class WelcomeActivity extends Activity {    

    private final int SPLASH_DISPLAY_LENGHT = 3000; //—”≥Ÿ»˝√Î 
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
        	        	request.sendPostForLogin(read_username, read_password);
        	        	request.getAllList();
        	        	Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class); 
                        WelcomeActivity.this.startActivity(mainIntent); 
                        WelcomeActivity.this.finish();
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

