package edu.sdust.mynote.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import edu.sdust.mynote.MyApplication;
import edu.sdust.mynote.bean.Memo;
import edu.sdust.mynote.bean.Note;
import edu.sdust.mynote.database.DatabaseHelper;
import edu.sdust.mynote.database.Lists;
import edu.sdust.mynote.database.NoteDB;
import edu.sdust.mynote.function.DealWithDate;
import edu.sdust.mynote.function.DealWithString;
import edu.sdust.mynote.receiver.AlarmReceiver;

public class HttpPostRequest {
	
	private Calendar cal = Calendar.getInstance();;
	private DealWithDate dealWithDate=new DealWithDate();
	public static String dbCreate = "create table if not exists memo (item_id text primary key,item_content string,create_time long,starrted integer,due_date long,completed integer,repeat_type integer)";
	public static String dbCreate_memo01 = "create table if not exists memo01 (item_id text primary key,item_content string,create_time long,starrted integer,due_date long,completed integer,repeat_type integer)";
	Lists lists =new Lists(MyApplication.getInstance());
	DatabaseHelper db_memo = new DatabaseHelper(MyApplication.getInstance(), "gtask", 1, dbCreate, "memo");
	DatabaseHelper db_memo01 = new DatabaseHelper(MyApplication.getInstance(), "gtask", 1, dbCreate_memo01, "memo01");
	private String dbCreate_note = "create table if not exists note (note_id text primary key,note_title text,note_create_time long,note_content text)";
	NoteDB noteDb = new NoteDB(MyApplication.getInstance(),"notes",1, dbCreate_note,"note");
	
	//��ȡ��½�ķ�������
	public String sendPostForLogin(String username,String password){
		 
		/*����HTTPost����*/
		 	    
				
		        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"User.php");        
		        HttpClient client = new DefaultHttpClient();
		        StringBuilder builder = new StringBuilder();        
		        boolean res = true;

		        /*
		         * NameValuePairʵ����������ķ�װ
		       */
		        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		        params.add(new BasicNameValuePair("action","login"));
		        params.add(new BasicNameValuePair("username", username)); 
		        params.add(new BasicNameValuePair("password", password));
		        try {
				    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		        try 
		        {
		            //HttpResponse httpResponse = client.execute(httpRequest);
		        	HttpResponse httpResponse = client.execute(httpPost);
		            int resInt = httpResponse.getStatusLine().getStatusCode();
		            if (resInt == 200) { 
		                /* 
		                 * ��������Ϊ200ʱ�������� 
		                 * �õ��������˷���json���ݣ��������� 
		                 * */  
		                BufferedReader bufferedReader = new BufferedReader( 
		                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
		                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
		                        .readLine()) {
		                    builder.append(s); 
		                } 	   
		                
		                DealWithString deal=new DealWithString();
		                String string=deal.strToJson(builder.toString());
		                Log.v("tohear����login",string);
		                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
		                res = jsonObject.getBoolean("error");
		                String token_c=jsonObject.getString("token");
		                
		                
		                SharedPreferences preference=MyApplication.getInstance().getSharedPreferences("store",Context.MODE_WORLD_WRITEABLE);
		                Editor editor = preference.edit();
		                editor.putString("token",token_c);
		                editor.commit();
		                
		                Toast.makeText(MyApplication.getInstance(), "��½�ɹ�", 1).show();
		                
		            	}
		            else
		            	Log.v("res","meiyou renhe xiangying");
		            }
		            catch (Exception e) {
				    	 Log.v("url response", "false");
				    	 e.printStackTrace();
		            }
			         if (res==false)
			            	return "0";
			         else
			            	return "1";

		}
	
	
	//��ȡע��ķ�������
	public int sendPostForRegister(String username,String password){
		 
		
		 	    
		        /*����HTTPost����*/
		        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"User.php");        
		        HttpClient client = new DefaultHttpClient();
		        StringBuilder builder = new StringBuilder();        
		        int res = 20;

		        /*
		         * NameValuePairʵ����������ķ�װ
		       */
		        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		        params.add(new BasicNameValuePair("action","signup"));
		        params.add(new BasicNameValuePair("username", username)); 
		        params.add(new BasicNameValuePair("password", password));
		        try {
				    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		        try 
		        {
		            //HttpResponse httpResponse = client.execute(httpRequest);
		        	HttpResponse httpResponse = client.execute(httpPost);
		            int resInt = httpResponse.getStatusLine().getStatusCode();
		            if (resInt == 200) { 
		                /* 
		                 * ��������Ϊ200ʱ�������� 
		                 * �õ��������˷���json���ݣ��������� 
		                 * */  
		                BufferedReader bufferedReader = new BufferedReader( 
		                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
		                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
		                        .readLine()) {
		                    builder.append(s); 
		                } 	   
		                
		                DealWithString deal=new DealWithString();
		                String string=deal.strToJson(builder.toString());
		                Log.v("tohear-register",string);
		                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
		                res = jsonObject.getInt("error_code");
		                
		                return res;
		            	}
		            else{
		            	Log.v("res","meiyou renhe xiangying");
		            }
		            }
		            catch (Exception e) {
				    	 Log.v("url response", "false");
				    	 e.printStackTrace();
		            }
		        return res;
		}
	//�½��б��б�,���ش������~~~
	public int addNewList(String list_name){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"List.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        Log.v("woxiangxin token meicuo",token_c);
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","new"));
        params.add(new BasicNameValuePair("list_name",list_name));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-addNewList",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                String list_id=jsonObject.getString("list_id");
                
                SharedPreferences preference=MyApplication.getInstance().getSharedPreferences("store",Context.MODE_WORLD_WRITEABLE);
                Editor editor = preference.edit();
                editor.putString("newList",list_id);
                editor.commit();
                
                
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	//�޸��б�����,���ش������~~~
	public int modifyListName(String list_name_new,String list_id){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"List.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","changename"));
        params.add(new BasicNameValuePair("list_name_new",list_name_new));
        params.add(new BasicNameValuePair("list_id",list_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-modifyListName",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	
	//ɾ��ĳ���б�,���ش������~~~
	public int deleteList(String list_id){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"List.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","del"));
        params.add(new BasicNameValuePair("list_id",list_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-deleteList",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	
	//��ȡȫ���б�,���ش������~~~
	public int getAllList(){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"List.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","get"));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-getAllList",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                
                
                JSONArray jsonArray = jsonObject.getJSONArray("lists");
                int iSize = jsonArray.length();
                Log.v("listCount",""+iSize);
                
       		    
       		    
       		    //�����е��б���Ϣ���뱾�����ݿ�
       		    lists.open();
       		    lists.deleteAll("lists");
       		    lists.close();
       		    
       		    
       		    lists.open();
       		    
                for(int i=0;i<iSize;i++){
                	JSONObject jsonObj = jsonArray.getJSONObject(i);
    	       		String list_id=jsonObj.getString("list_id");
    	       		
    	       		String list_name=jsonObj.getString("list_name");
    	       		
    	       		String list_created_time=jsonObj.getString("list_created_time");
    	       		
    	       		int event_total=jsonObj.getInt("event_total");
    	       		
    	       		String list_class;
    	       		if(jsonObj.getBoolean("shared")){
    	       			list_class="1";
    	       		}else
    	       			list_class="0";
    	       		
    	       		long id=lists.insertItem(list_id, list_name, list_created_time
    	         			, event_total, list_class);
                } 
                
                lists.close();
                
                SharedPreferences preferences=MyApplication.getInstance().getSharedPreferences("store",Context.MODE_WORLD_WRITEABLE);
                Editor editor=preferences.edit();
                editor.putInt("listCount", iSize);
                Log.v("listCount22222",""+iSize);
                
                editor.commit();

                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}

	//����ȫ���б�,���ش������~~~
	public int shareList(String list_id,String to_username){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"List.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","share"));
        params.add(new BasicNameValuePair("list_id",list_id));
        params.add(new BasicNameValuePair("to_username",to_username));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear--shareList",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}

	//�½�����,���ش������~~~
	public int addNewEvent(String event_content,String list_id,String event_date,String event_time,String starred){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        Log.v("token ����",token_c);
        Log.v("list_id �Ƿ��tokenһ��", list_id);
        
        int res = 20;

        Log.v("adfasdf", event_content+list_id+event_date+event_time+starred);
        
        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","new_android"));
        params.add(new BasicNameValuePair("list_id",list_id));
        params.add(new BasicNameValuePair("event_content",event_content));
        params.add(new BasicNameValuePair("token",token_c));
        params.add(new BasicNameValuePair("event_date",event_date));
        params.add(new BasicNameValuePair("event_time",event_time));
        params.add(new BasicNameValuePair("starred",starred));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-addNewEvent",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                
                
                SharedPreferences preference=MyApplication.getInstance().getSharedPreferences("store",Context.MODE_WORLD_WRITEABLE);
                Editor editor = preference.edit();
                editor.putString("newEvent",jsonObject.getString("event_id") );
                editor.commit();
                
                
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	//�޸���������,���ش������~~~
	public int modifyEventContent(String event_id,String event_content){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","updatecontent"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("event_content",event_content));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-modifyEvent",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	//ɾ��ĳ������,���ش������~~~
	public int deleteEvent(String list_id,String event_id){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","del"));
        params.add(new BasicNameValuePair("list_id",list_id));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-deleteEvent",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	
	//��ȡȫ������,���ش������~~~
	public int getAllEvent(String list_id){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;
        
        db_memo.open();
        db_memo.deleteAll("memo");
        db_memo.close();
        
        db_memo01.open();
        db_memo01.deleteAll("memo01");
        db_memo01.close();

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","get"));
        params.add(new BasicNameValuePair("list_id",list_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-getAllEvent",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                         
                Memo memo = new Memo();
                
                JSONArray jsonArray = jsonObject.getJSONArray("events");
                int iSize = jsonArray.length();
              
                db_memo.open();
                db_memo01.open();
                Log.v("iSize", ""+iSize);
                for(int i=0;i<iSize;++i){
                	
                	JSONObject jsonObj = jsonArray.getJSONObject(i);
                	memo.setItem_id(jsonObj.getString("event_id"));
                	memo.setItem_content(jsonObj.getString("event_content"));
                	
                	String create_time=jsonObj.getString("event_created_time");
                	cal.setTime(dealWithDate.strToDateLong(create_time));
                	memo.setCreate_time(cal.getTimeInMillis());
                	
                	memo.setStarrted(jsonObj.getBoolean("event_starred")?1:0);
                	Log.v("starrted OK",""+memo.getStarrted());
                	
                	memo.setCompleted(jsonObj.getBoolean("event_completed")?1:0);
                	Log.v("completed OK",""+memo.getCompleted());
                	
                	String due_date=jsonObj.getString("event_due_date")+" "+jsonObj.getString("event_due_time");
                	Log.v("due_time OK","due-date"+due_date);       	
                	if (due_date==" "){                	
	                	memo.setDue_date(Calendar.getInstance().getTimeInMillis());
                	}else{
                		cal.setTime(dealWithDate.strToDateLong(due_date));
	                	memo.setDue_date(cal.getTimeInMillis());
                	}
                		
                	memo.setRepeat_type(0);
                	
                	if (memo.getCompleted()==0){
                		
                		
                		Date date = new Date();

                		if (date.getTime()<memo.getDue_date()){
                    		db_memo.insertData(memo);
	                		AlarmManager alarmManager = (AlarmManager)MyApplication.getInstance().getSystemService(Context.ALARM_SERVICE);//ȡ��ϵͳ���ӷ���
	                		Intent intent = new Intent(MyApplication.getInstance(), AlarmReceiver.class);    //����Intent����  
		       				intent.putExtra("memo", new String[]{memo.getItem_content(),String.valueOf(memo.getStarrted()),String.valueOf(memo.getDue_date())});
		       				intent.putExtra("item_id", memo.getItem_id());
	                        PendingIntent pi = PendingIntent.getBroadcast(MyApplication.getInstance(), 0, intent, 0);    //����PendingIntent 
	                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                        }
                		else{
                			modifyCompleted(memo.getItem_id());
                			db_memo01.insertData(memo);
                		}
                	}
                	else if (memo.getCompleted()==1){
                		db_memo01.insertData(memo);
                	}               	
                }    
            	db_memo.close();
            	db_memo01.close();
                
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	
	//������������,���ش������~~~
	public int modifyDate(String event_id,String event_date){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","changedate"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("event_date",event_date));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-modifyDate",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}

	
	//��������ʱ��,���ش������~~~
	public int modifyTime(String event_id,String event_time){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","changetime"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("event_time",event_time));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-modifyTime",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	//���������Ƿ����,���ش������~~~
	public int modifyCompleted(String event_id){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","completed"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-modifyCompleted",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}

	//���������Ƿ����,���ش������~~~
	public int modifyStarrted(String event_id){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Event.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","starrted"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-modifyStarrted",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	//�½��ʼ�,���ش������~~~
	public int addNewNote(String event_id,String note_title,String note_content){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Note.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","new"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("note_title",note_title));
        params.add(new BasicNameValuePair("note_content",note_content));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-addNewNote",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	//ɾ���ʼ�,���ش������~~~
	public int deleteNote(String event_id,String note_id){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Note.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","del"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("note_id",note_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-deleteNote",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	//�޸ıʼ�,���ش������~~~
	public int modifyNote(String note_id,String note_title,String note_content){
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Note.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","update"));
        params.add(new BasicNameValuePair("note_id",note_id));
        params.add(new BasicNameValuePair("note_title",note_title));
        params.add(new BasicNameValuePair("note_content",note_content));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear--modifyNote",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	//��ȡ�ʼ�,���ش������~~~
	public int getNote(String event_id){
		
		/*����HTTPost����*/
        HttpPost httpPost = new HttpPost(SettingIP.GetIP()+"Note.php");        
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
        SharedPreferences token=MyApplication.getInstance().getSharedPreferences("store", Context.MODE_WORLD_READABLE);
        String token_c=token.getString("token", "");
        
        int res = 20;
        

        noteDb.open();
        noteDb.deleteAll("note");
        noteDb.close();

        /*
         * NameValuePairʵ����������ķ�װ
       */
        List<NameValuePair> params = new ArrayList<NameValuePair>(); 
        params.add(new BasicNameValuePair("action","gettitles"));
        params.add(new BasicNameValuePair("event_id",event_id));
        params.add(new BasicNameValuePair("token",token_c));
        
        try {
		    httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
        try 
        {
            //HttpResponse httpResponse = client.execute(httpRequest);
        	HttpResponse httpResponse = client.execute(httpPost);
            int resInt = httpResponse.getStatusLine().getStatusCode();
            if (resInt == 200) { 
                /* 
                 * ��������Ϊ200ʱ�������� 
                 * �õ��������˷���json���ݣ��������� 
                 * */  
                BufferedReader bufferedReader = new BufferedReader( 
                        new InputStreamReader(httpResponse.getEntity().getContent(),"UTF-8"));  
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    builder.append(s); 
                } 	   
                
                DealWithString deal=new DealWithString();
                String string=deal.strToJson(builder.toString());
                Log.v("tohear-getAllEvent",string);
                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
                res = jsonObject.getInt("error_code");
                         
                Note note = new Note();
                
                JSONArray jsonArray = jsonObject.getJSONArray("notes_title");
                int iSize = jsonArray.length();
              
                noteDb.open();
                Log.v("iSize", ""+iSize);
                for(int i=0;i<iSize;++i){
                	
                	JSONObject jsonObj = jsonArray.getJSONObject(i);
                	note.setNote_id(jsonObj.getString("note_id"));
                	note.setNote_content(jsonObj.getString("note_content"));
                	note.setNote_title(jsonObj.getString("note_title"));
                	
                	String create_time=jsonObj.getString("note_created_time");
                	
                	Log.v("huode le note", note.getNote_id()+note.getNote_title()+note.getNote_content()+create_time);
                	note.setNote_created_time(dealWithDate.strToDate(create_time).getTime());
                	
                	noteDb.insertData(note);
                }
	            noteDb.close();
                
                return res;
            	}
            else{
            	Log.v("res","meiyou renhe xiangying");
            }
            }
            catch (Exception e) {
		    	 Log.v("url response", "false");
		    	 e.printStackTrace();
            }
        return res;
	}
	
	
}
