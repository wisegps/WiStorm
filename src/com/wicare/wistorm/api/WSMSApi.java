package com.wicare.wistorm.api;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * WSMSApi获取手机验证码api
 * 
 * @author c
 * @date 2015-11-12
 */
public class WSMSApi extends WiStormAPI {

	public SMSListener smsListener;
	public HashMap<String, String> hashParam = new HashMap<String, String>();
	public Context context;
	public RequestQueue mQueue;

	public WSMSApi(Context context, SMSListener smsListener) {
		super();
		this.context = context;
		this.smsListener = smsListener;
	}

	public void getVerifyCode(String mobile) {
		String url = "http://api.bibibaba.cn/valid_code?mobile=" + mobile
				+ "&type=1";
		request(url);
	}

	/**
	 * request 发送请求
	 * 
	 * @param url
	 */
	public void request(String url) {
		mQueue = Volley.newRequestQueue(context);
		Listener<String> listener = new Response.Listener<String>() {
			public void onResponse(String response) {

				try {
					JSONObject obj = new JSONObject(response);
					int statusCode = obj.getInt("status_code");
					if (statusCode == WSMSApi.Response_Sucess) {
						int code = obj.getInt("valid_code");
						smsListener.onCodeResponse(code + "");
					} else {
						smsListener.onCodeResponse("");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};

		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				smsListener.onCodeResponse("");
			}
		};

		Request request = new StringRequest(url, listener, errorListener);
		request.setShouldCache(false);
		mQueue.add(request);
		mQueue.start();
	}

	/**
	 * 
	 * SMSListener 短信验证码返回监听
	 * 
	 * @author c
	 * @date 2015-11-12
	 */
	public interface SMSListener {
		/**
		 * 
		 * onCodeResponse
		 * 
		 * @param response
		 *            返回了验证码“6543”
		 */
		public void onCodeResponse(String response);

	}

	public final static int Response_Sucess = 0;
}
