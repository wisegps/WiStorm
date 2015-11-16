package com.wicare.wistorm.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
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
				Log.e(TAG, "==========" + url);
				HttpClient httpClient = new DefaultHttpClient();
				// 请求超时
				httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
                // 读取超时
				httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, CONNECT_TIMEOUT);
				HttpGet httpGet = new HttpGet(url.toString());
				HttpResponse httpResponse = httpClient.execute(httpGet);
				Log.e(TAG, "==========" + httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = httpResponse.getEntity();
					String response = EntityUtils.toString(entity, "utf-8");
					Log.e(TAG, "==========>" + response);
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
}