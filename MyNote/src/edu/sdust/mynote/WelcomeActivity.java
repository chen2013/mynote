package edu.sdust.mynote;


import edu.sdust.mynote.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler; 

public class WelcomeActivity extends Activity {    

    private final int SPLASH_DISPLAY_LENGHT = 3000; //—”≥Ÿ»˝√Î 

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
        		 Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class); 
                 WelcomeActivity.this.startActivity(mainIntent); 
                 WelcomeActivity.this.finish();
        	 }
         } 
        }, SPLASH_DISPLAY_LENGHT); 
    } 
}

