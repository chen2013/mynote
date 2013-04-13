package edu.sdust.mynote;


import java.util.Date;

import edu.sdust.mynote.R;
import edu.sdust.mynote.database.ListCount;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.function.DealWithDate;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler; 

public class WelcomeActivity extends Activity {    

    private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒 

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
        		 
        		 
        		 //对数据库进行初始化
        		 
        		 DealWithDate dealWithDate =new DealWithDate();
        		 
        		 Lists listsDB=new Lists(WelcomeActivity.this);
        		 listsDB.open();
        		 
        		 long id;
        		 id = listsDB.insertItem("before", dealWithDate.dateToStrLong(new Date()),0, "");
        		 id = listsDB.insertItem("today", dealWithDate.dateToStrLong(new Date()),0, "");
        		 id = listsDB.insertItem("after", dealWithDate.dateToStrLong(new Date()),0, "");
 				 
        		 listsDB.close();
        		 
                 SharedPreferences preferences= MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_WRITEABLE);
                 Editor editor = preferences.edit();
                 editor.putInt("listCount", 3);
                 editor.commit();
                 
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

