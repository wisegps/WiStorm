package com.wicare.wistorm.api;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.widget.Toast;

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
	public String Method_User_Login = "wicare.user.login";
	public String Method_User_SSO_Login = "wicare.user.sso_login";
	public String Method_User_Register = "wicare.user.register";

	public String Method_User_Psd_Reset = "wicare.user.password.reset";

	public String Method_User_Volid_Code = "wicare.user.valid_code";
	
	public String Wicare_User_Create = "wicare.user.create";

	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	private Handler uiHandler;// UI线程
	private Handler workHandler;// 工作线程，非UI线程
	private BaseVolley volley;

	public WUserApi(Handler uiHandler) {
		this.uiHandler = uiHandler;
		init();
	}

	/**
	 * 初始化数据
	 * 
	 * @param uiHandler
	 */
	public void init() {
		this.workHandler = super.initWorkHandler(handleCallBack);
		volley = new BaseVolley(workHandler);
	}

	/**
	 * 工作子线程回调函数,负责接收网络返回数据，并交给解析函数进一步处理
	 */
	private Callback handleCallBack = new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Msg.M_Usr_Token:
				parseToken(msg);
				break;
			case Msg.M_Usr_Login:
				parseLogin(msg);
				break;
			case Msg.M_Usr_Third_Login:
				parseThirdLogin(msg);
				break;
			case Msg.M_Usr_Register:
				parseRegister(msg);

				break;
			case Msg.M_Usr_Volid_Code:
				parseVolidCode(msg);
				break;
			case Msg.M_Usr_Psd_Reset:
				parseReset(msg);
				break;
			case Msg.M_Usr_Create:
				Log.i("WUserApi", "M_Usr_Create handleCallBack"+msg.obj.toString());
				parseCreate(msg);
				break;
			}
			return false;
		}
	};

	
	/*****************************************************解析网络返回的数据********************************/
	/**
	 * 解析返回的token数据
	 * 
	 * @param msg
	 */
	private void parseToken(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = new Bundle();
			bundle.putString("access_token", json.getString("access_token"));
			bundle.putString("valid_time", json.getString("valid_time"));
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析返回的登陆信息
	 * 
	 * @param msg
	 */
	public void parseLogin(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = new Bundle();
			int status = json.getInt("status_code");
			bundle.putInt("status_code", status);
			if (status == 0) {
				bundle.putString("cust_id", json.getString("cust_id"));
				bundle.putString("access_token", json.getString("access_token"));
				bundle.putString("valid_time", json.getString("valid_time"));
			}
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析返回的第三方登陆信息
	 * 
	 * @param msg
	 */
	public void parseThirdLogin(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = new Bundle();
			int status = json.getInt("status_code");
			bundle.putInt("status_code", status);
			if (status == 0) {
				bundle.putString("cust_id", json.getString("cust_id"));
				bundle.putString("access_token", json.getString("access_token"));
				bundle.putString("valid_time", json.getString("valid_time"));
			}
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析返回的注册结果
	 * 
	 * @param msg
	 */
	private void parseRegister(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = new Bundle();
			bundle.putString("cust_id", json.getString("cust_id"));
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void parseVolidCode(Message msg) {
		try {

			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = new Bundle();
			bundle.putBoolean("valid", json.getBoolean("valid"));
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析返回的密码重置结果
	 * 
	 * @param msg
	 */
	private void parseReset(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = new Bundle();
			bundle.putString("access_token", json.getString("access_token"));
			bundle.putString("valid_time", json.getString("valid_time"));
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析创建用户返回信息
	 * @param msg
	 */
	public void parseCreate(Message msg){
		
		if(msg.arg1==-1){
			Toast.makeText(context, "网络请求失败", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Log.i("WUserApi", "M_Usr_Create parseCreate"+msg.obj.toString());
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = new Bundle();
			bundle = msg.getData();
			bundle.putString("cust_id", json.getString("cust_id"));
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*****************************************************请求业务********************************/

	/**
	 * 获取令牌
	 * 
	 * @param account
	 *            手机号码或者邮箱地址
	 * @param password
	 *            登陆密码
	 */
	public void getToken(String account, String password) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		String url = super.getUrl(Method_Access_Token, "", params);
		volley.request(url, Msg.M_Usr_Token);
	}

	/**
	 * 登陆
	 * 
	 * @param account
	 *            手机号码或者邮箱地址
	 * @param password
	 *            登陆密码
	 */
	public void login(String account, String password) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		String url = super.getUrl(Method_User_Login, "", params);
		volley.request(url, Msg.M_Usr_Login);
	}

	/**
	 * 第三方登陆
	 * 
	 * @param loginId
	 *            第三方登陆返回的标识ID
	 */
	public void thridLogin(String loginId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("login_id", loginId);
		String url = super.getUrl(Method_User_SSO_Login, "", params);
		volley.request(url, Msg.M_Usr_Third_Login);
	}

	/**
	 * 注册
	 * 
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @param validType
	 *            验证类型
	 * @param validCode
	 *            验证码
	 */
	public void register(String account, String password, String validCode) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		// 1: 通过手机号 2:通过邮箱
		String validType = "1";
		// 判断手机或者邮箱
		if (account.contains("@")) {
			validType = "2";
			params.put("email", account);
		} else {
			validType = "1";
			params.put("mobile", account);
		}
		params.put("password", password);
		params.put("valid_type", validType);
		params.put("valid_code", validCode);

		String url = super.getUrl(Method_User_Register, "", params);
		volley.request(url, Msg.M_Usr_Register);
	}

	/**
	 * 验证校验码
	 * 
	 * @param account
	 *            账号
	 * @param validCode
	 *            验证码
	 */
	public void volidCode(String account, String validCode) {
		HashMap<String, String> params = new HashMap<String, String>();
		// 1: 通过手机号 2:通过邮箱
		String validType = "1";
		// 判断手机或者邮箱
		if (account.contains("@")) {
			validType = "2";
			params.put("email", account);
		} else {
			validType = "1";
			params.put("mobile", account);
		}
		params.put("valid_type", validType);
		params.put("valid_code", validCode);

		String url = super.getUrl(Method_User_Volid_Code, "", params);
		volley.request(url, Msg.M_Usr_Volid_Code);
	}

	/**
	 * 重置密码
	 * 
	 * @param account
	 *            账号
	 * @param password
	 *            密码
	 * @param validType
	 *            验证类型
	 * @param validCode
	 *            验证码
	 */
	public void reset(String account, String password, String validType,
			String validCode) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		params.put("valid_type", validType);
		params.put("valid_code", validCode);

		String url = super.getUrl(Method_User_Psd_Reset, "", params);
		volley.request(url, Msg.M_Usr_Psd_Reset);
	}

	/**
	 创建客户信息 params 的key包含下面：
	 
	mode: 创建模式 1:仅仅创建用户 2:同时创建用户,车辆,到店记录
	seller_id: 商户ID, 如果没有默认为0
	cust_type: 用户类型 1:车主 2:商户
	cust_name: 用户名称
	mobile: 手机
	obj_name: 车牌号
	frame_no: 车架号
	car_brand_id: 品牌ID
	car_brand: 品牌
	car_series_id: 车系ID
	car_series: 车系
	car_type_id: 车款ID
	car_type: 车款
	mileage: 行驶里程
	if_arrive: 是否到店, 1则需要传入到店类型和备注, 0则不需要
	business_type: 业务类型
	business_content: 业务内容
	 */
	public void create(HashMap<String, String> params) {
		
		Log.i("WUserApi", "M_Usr_Create create");
		String url = super.getUrl(Wicare_User_Create, "", params);
		
		Log.i("WUserApi", "M_Usr_Create create url:"+url);
		volley.request(url, Msg.M_Usr_Create);
	}

}
