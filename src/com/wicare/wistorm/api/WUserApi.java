package com.wicare.wistorm.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.util.Xml.Encoding;
import android.webkit.WebView.FindListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wicare.wistorm.toolkit.WEncrypt;

/**
 * UserApi
 * 
 * @author c
 * @date 2015-10-12
 */
public class WUserApi extends WiStormAPI {

	public OnLoginListener onLoginListener;
	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	public RequestQueue mQueue;

	public WUserApi() {
		super();
	}

	public WUserApi(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 
	 * login 设置登录参数
	 * 
	 * @param userName
	 * @param password
	 */
	public void login(String userName, String password) {

		String appSecret = "7de743e26f6a9b71a14b8ac3";
		String appKey = "51ccb6294c18a0979cc10240";
		String method = "wicare.user.login";

		String timestamp = super.getCurrentTime();

		String format = "json";
		String v = "1.0";
		String signMethod = "md5";
		String fields = "cust_id,cust_name";
		password = WEncrypt.MD5(password);
		// 方法名称
		hashParam.put("method", method);
		// 时间戳yyyy-mm-dd hh:nn:ss
		hashParam.put("timestamp", timestamp);
		// 返回数据格式
		hashParam.put("format", format);
		// app key
		hashParam.put("app_key", appKey);
		// 接口版本
		hashParam.put("v", v);
		// 签名方式
		hashParam.put("sign_method", signMethod);
		// 需要返回的字段
		hashParam.put("fields", fields);
		// 登陆接口所需参数
		hashParam.put("account", userName);
		// 登陆接口所需参数
		hashParam.put("password", password);
		// 把参数排序并进行拼接
		String s = super.raw(hashParam);
		String sign = WEncrypt.MD5(appSecret + s + appSecret);

		try {
			timestamp = URLEncoder.encode(timestamp, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String path = super.basePath + method + "&timestamp=" + timestamp
				+ "&format=" + format + "&app_key=" + appKey + "&v=" + v
				+ "&sign_method=" + signMethod + "&fields=" + fields
				+ "&account=" + userName + "&password=" + password + "&sign="
				+ sign;
		Log.i("WUserApi", s);
		Log.i("WUserApi", path);

		request(path);

	}

	/**
	 * 
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
					if (statusCode == WUserApi.Login_Sucess) {
						onLoginListener.loginSucess(response);
					} else {
						onLoginListener.loginFail(statusCode);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};

		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onLoginListener.loginFail(WUserApi.Fail_Net);
			}
		};

		Request request = new StringRequest(url, listener, errorListener);
		request.setShouldCache(false);
		mQueue.add(request);
		mQueue.start();
	}

	/**
	 * 
	 * setOnLoginListener 设置登录监听器
	 * 
	 * @param onLoginListener
	 */
	public void setOnLoginListener(OnLoginListener onLoginListener) {
		this.onLoginListener = onLoginListener;
	}

	/**
	 * 
	 * OnLoginListener 定义登录请求监听接口,由登录界面实现
	 * 
	 * @author c
	 * @date 2015-10-13
	 */
	public interface OnLoginListener {
		/**
		 * 
		 * loginSucess 登录成功
		 */
		public  void loginSucess(String response);

		/**
		 * 
		 * loginFail 登录失败
		 * 
		 * @param statusCode
		 *            失败状态码 1，用户名，2密码
		 */
		public void loginFail(int statusCode);

	}
	
	public final static int Login_Sucess = 0;
	public final static int Fail_Account = 1;
	public final static int Fail_Password = 2;
	public final static int Fail_Net = 3;
	public final static int Fail_Null = 4;
	public final static int Fail_Bound = 5;
	
}
