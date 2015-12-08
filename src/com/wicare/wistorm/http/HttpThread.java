package com.wicare.wistorm.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author Wu
 */
public class HttpThread {
	
	static final String TAG = "HttpThread";
	private static final int CONNECT_TIMEOUT = 8000;
	/**
	 * Get获取数据
	 * @author honesty
	 */
	public static class getDataThread extends Thread{
		Handler handler;
		String url;
		int what;
		/**
		 * Get获取数据
		 * @param handler
		 * @param url
		 * @param what
		 */
		public getDataThread(Handler handler,String url,int what){
			this.handler = handler;
			this.url = url;
			this.what =what;
		}

		@Override
		public void run() {
			super.run();
			try {
				Log.e(TAG, "=====getDataThread=====" + url);
				HttpClient httpClient = new DefaultHttpClient();
				// 请求超时
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
                // 读取超时
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, CONNECT_TIMEOUT);
				HttpGet httpGet = new HttpGet(url.toString());
				HttpResponse httpResponse = httpClient.execute(httpGet);
				Log.e(TAG, "=====getDataThread=====" + httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = httpResponse.getEntity();
					String response = EntityUtils.toString(entity, "utf-8");
					Log.e(TAG, "====getDataThread======>" + response);
					Message message = new Message();
					message.what = what;
					message.obj = response;
					handler.sendMessage(message);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
				Message message = new Message();
				message.what = what;
				message.obj = "Exception";
				handler.sendMessage(message);
			}
		}
	}
	
	
	
	
	
	/**
	 * @author Wu
	 * post 提交数据
	 */
	public static class postDataThread extends Thread{
		Handler handler;
		String url;
		int what;
		List<NameValuePair> params;
		/**
		 * @param handler handler 
		 * @param url url
		 * @param parms 提交的参数
		 * @param what msg.what
		 */
		public postDataThread(Handler handler,String url,List<NameValuePair> params,int what){
			this.handler = handler;
			this.url = url;
			this.what = what;
			this.params = params;
		}
		@Override
		public void run() {
			super.run();
			HttpPost httpPost = new HttpPost(url);
			try {
				 httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				 HttpClient client = new DefaultHttpClient();
				 client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
				 client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
				 HttpResponse httpResponse = client.execute(httpPost);
				 Log.e(TAG, "=====postDataThread=====" + httpResponse.getStatusLine().getStatusCode());
				 if(httpResponse.getStatusLine().getStatusCode() == 200){
					 String strResult = EntityUtils.toString(httpResponse.getEntity());
					 Message message = new Message();
					 message.what = what;
					 message.obj = strResult;
					 Log.e(TAG, "=====postDataThread=====>" + strResult);
					 handler.sendMessage(message);	
				 }else{					 
					 Message message = new Message();
					 message.what = what;
					 message.obj = "";
					 handler.sendMessage(message);
				 }
			} catch (Exception e) {
				Message message = new Message();
				message.what = what;
				message.obj = "";
				handler.sendMessage(message);			
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author Wu
	 * 
	 * put提交数据
	 */
	public static class putDataThread extends Thread{
		Handler handler;
		String url;
		int what;
		List<NameValuePair> parms;
		/**
		 * @param handler handler 
		 * @param url url
		 * @param parms 提交的参数
		 * @param what msg.what
		 */
		public putDataThread(Handler handler,String url,List<NameValuePair> parms,int what){
			this.handler = handler;
			this.url = url;
			this.what =what;
			this.parms = parms;
		}
		
		@Override
		public void run() {
			super.run();
			try {
				BasicHttpParams httpParams = new BasicHttpParams();  
			    HttpConnectionParams.setConnectionTimeout(httpParams, 10000);  
			    HttpConnectionParams.setSoTimeout(httpParams, 10000); 
				HttpClient client = new DefaultHttpClient(httpParams);
		        HttpPut httpPut = new HttpPut(url);	
		        if(parms != null){
		        	httpPut.setEntity(new UrlEncodedFormEntity(parms,HTTP.UTF_8));
		        }
		        HttpResponse response = client.execute(httpPut); 
		        Log.e(TAG, "=====putDataThread=====" + response.getStatusLine().getStatusCode());
		        if (response.getStatusLine().getStatusCode()  == 200){
		        	HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					StringBuilder sb = new StringBuilder();
					String line = "";
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					Message message = new Message();
					message.what = what;
					message.obj = sb.toString();
					
					Log.e(TAG, "======putDataThread====" + sb.toString());
					
					handler.sendMessage(message);
		        }else{
		        	Message message = new Message();
					message.what = what;
					message.obj = "";
					handler.sendMessage(message);
		        }
			} catch (Exception e) {
				e.printStackTrace();	
				Message message = new Message();
				message.what = what;
				message.obj = "";
				handler.sendMessage(message);
			}
		}
	}
	
}