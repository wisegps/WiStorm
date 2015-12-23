package com.wicare.wistorm.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
//import android.webkit.WebView.FindListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.Msg;
import com.wicare.wistorm.toolkit.WEncrypt;

/**
 * UserApi
 * 
 * @author c
 * @date 2015-10-12
 */
public class WUserApi extends WiStormAPI {
	public String Method_Access_Token = "wicare.user.access_token";
	public OnLoginListener onLoginListener;
	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	private Handler workHandler;// 工作线程，非UI线程
	public RequestQueue mQueue;
	private BaseVolley volley;

	public WUserApi() {
		super();
		this.workHandler = super.initWorkHandler(handleCallBack);
		volley = new BaseVolley(workHandler);
	}

	public WUserApi(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 工作子线程回调函数
	 */
	private Callback handleCallBack = new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Msg.M_Usr_Token:
				Log.i("WUserApi", msg.obj.toString());
				break;
			}

			return false;
		}
	};

	// 获取令牌
	// 参数：account: 手机号码或者邮箱地址
	// passsword: md5(登陆密码)
	public void requestAccessToken(String account, String password) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		String url = super.getUrl(Method_Access_Token, "", params);
		volley.request(url, Msg.M_Usr_Token);
	}

	/**
	 * 
	 * login 设置登录参数
	 * 
	 * @param userName
	 * @param password
	 */
	public void login(String userName, String password) {

		String appSecret = "21fb644e20c93b72773bf0f8d0905052";
		String appKey = "9410bc1cbfa8f44ee5f8a331ba8dd3fc";
		String method = "wicare.user.login";
		String timestamp = super.getCurrentTime();
		timestamp.replace(" ", "20%");
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
		// hashParam.put("fields", fields);
		// 登陆接口所需参数
		hashParam.put("account", userName);
		// 登陆接口所需参数
		hashParam.put("password", password);
		// 把参数排序并进行拼接
		String s = super.raw(hashParam);
		Log.i("WUserApi", s);
		
		String sign = WEncrypt.MD5(appSecret + s + appSecret).toUpperCase();
		String path = super.basePath + method + "&timestamp=" + timestamp
				+ "&format=" + format + "&app_key=" + appKey + "&v=" + v
				+ "&sign_method=" + signMethod + "&account=" + userName
				+ "&password=" + password + "&sign=" + sign;
		
		path = Uri.encode(path);
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
		public void loginSucess(String response);

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
