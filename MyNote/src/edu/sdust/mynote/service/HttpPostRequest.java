package edu.sdust.mynote.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;
import edu.sdust.mynote.function.DealWithString;

public class HttpPostRequest {
	
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
		                Log.v("tohear",string);
		                JSONObject jsonObject = new JSONObject(string);//��Ҫȥ��ǰ��һ������
		                res = jsonObject.getBoolean("error");
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
		                Log.v("tohear",string);
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
	

}
