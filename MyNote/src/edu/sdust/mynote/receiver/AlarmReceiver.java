package edu.sdust.mynote.receiver;

import edu.sdust.mynote.AddNewEventActivity;
import edu.sdust.mynote.FinishActivity;
import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.database.DatabaseHelper;
import edu.sdust.mynote.pull.R;
import edu.sdust.mynote.service.HttpPostRequest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{

	 private static final String tag = "Notification Receiver"; 
	 HttpPostRequest request =new HttpPostRequest();
	@Override
	public void onReceive(Context context,Intent intent)
	{
		
			DatabaseHelper databaseHelper = new DatabaseHelper(context, "gtask", 1, AddNewEventActivity.dbCreate, "memo");
			databaseHelper.open();
			//这里要在memo表中删除 
			String event_id=intent.getStringExtra("event_id");
			databaseHelper.deleteById(event_id);///z这里的event-id有错误
			Log.v("tag",intent.getLongExtra("id", 0)+"hh");
			databaseHelper.close();
			
			databaseHelper = new DatabaseHelper(context, "gtask", 1, FinishActivity.dbCreate, "memo01");
            databaseHelper.open();
            Memo memoObj = new Memo();
	        String[] memo = intent.getStringArrayExtra("memo"); 
	        String content = memo[0];
	        memoObj.setItem_content(content);
	        memoObj.setStarrted(Integer.valueOf(memo[1]));
	        memoObj.setCompleted(1);
	        
	        request.modifyCompleted(event_id);//事物完成，修改完成标识字段
	        
	        memoObj.setDue_date(Long.valueOf(memo[2]));
	        Log.v("here is broadcast", memo[0]+","+String.valueOf(memo[1])+","+String.valueOf(memo[2]));
            databaseHelper.insertData(memoObj);
            databaseHelper.close();
	        this.sendNotification(context, "要完成的事务",content); 
	}
	 private void sendNotification(Context ctx, String title,String content) 
	    { 
	        //Get the notification manager 
	        String ns = Context.NOTIFICATION_SERVICE; 
	        NotificationManager nm =  (NotificationManager)ctx.getSystemService(ns); 
	         
	        //Create Notification Object 
	        int icon = R.drawable.event; 
	        CharSequence tickerText = "you have a"+title+"task--->"+"\n"+content; 
	        long when = System.currentTimeMillis(); 
	         
	        Notification notification = new Notification(icon, tickerText, when); 
	       
	        
	 
	        //Set ContentView using setLatestEvenInfo 
	        Intent intent = new Intent(Intent.ACTION_VIEW); 
	        intent.setData(Uri.parse("http://www.google.com")); 
	        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
	        notification.setLatestEventInfo(ctx, content, content, pi); 
	               
	        //Send notification 
	        //The first argument is a unique id for this notification. 
	        //This id allows you to cancel the notification later  
	        nm.notify(1, notification); 
	    } 
}
