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

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.Msg;
import com.wicare.wistorm.toolkit.WEncrypt;

/**
 * UserApi
 * 
 * @author wu
 * @date 2016-03-30
 */
public class WUserApi extends WiStormAPI {
	public String Method_Access_Token = "wicare.user.access_token";
	public String Method_User_Login = "wicare.user.login";
	public String Method_User_SSO_Login = "wicare.user.sso_login";
	public String Method_User_Register = "wicare.user.register";
	public String Method_User_Psd_Reset = "wicare.user.password.reset";
	public String Method_User_Volid_Code = "wicare.user.valid_code";
	public String Wicare_User_Create = "wicare.user.create";
	public String Wicare_User_Get = "wicare.user.get";
	public String Wicare_User_Update = "wicare.user.update";
	public String Wicare_User_Cust_List = "wicare.user.customers.list";

	public HashMap<String, String> hashParam = new HashMap<String, String>();
	public Context context;
	private Handler uiHandler;// UI线程
	private Handler workHandler;// 工作线程，非UI线程
	private BaseVolley volley;

	
	public WUserApi(){
		init_1();
	}
	
	public void init_1(){
		volley = new BaseVolley();
	}
	
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
				Log.i("WUserApi",
						"M_Usr_Token handleCallBack" + msg.obj.toString());
				parseToken(msg);
				break;
			case Msg.M_Usr_Login:
				parseLogin(msg);
				break;
			}
			return false;
		}
	};

	/***************************************************** 解析网络返回的数据 ********************************/
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


	/***************************************************** 请求业务 ********************************/

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

	/* ****与上面的用Handler.what 方式返回不同，这种方法直接用 volley 的 请求回调来返回信息*******************************************************************************************************/

	/**
	 * 获取 Token
	 * 
	 * @param account  帐号
	 * @param password 密码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void getToken(String account, String password,Listener<String> onSuccess,ErrorListener onError) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		String url = super.getUrl(Method_Access_Token, "", params);
		volley.request(url,onSuccess,onError);
	}


	/**
	 * 登陆
	 * 
	 * @param account  帐号
	 * @param password 密码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void login(String account, String password,Listener<String> onSuccess,ErrorListener onError) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		String url = super.getUrl(Method_User_Login, "", params);
		volley.request(url,onSuccess,onError);
	}
	
	/**
	 * 第三方登陆
	 * 
	 * @param loginId 第三方登陆返回的标识ID
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void thridLogin(String loginId,Listener<String> onSuccess,ErrorListener onError) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("login_id", loginId);
		String url = super.getUrl(Method_User_SSO_Login, "", params);
		volley.request(url,onSuccess,onError);
	}
	

	/**
	 * 注册
	 * 
	 * @param account   账号
	 * @param password  密码
	 * @param validCode 验证类型
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void register(String account, String password, String validCode,Listener<String> onSuccess,ErrorListener onError) {
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
		volley.request(url,onSuccess,onError);
	}

	/**
	 * 验证校验码
	 * 
	 * @param account
	 *            账号
	 * @param validCode
	 *            验证码
	 */
	/**
	 * @param account   账号
	 * @param validCode 验证码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void volidCode(String account, String validCode,Listener<String> onSuccess,ErrorListener onError) {
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
		volley.request(url,onSuccess,onError);
	}

	/**
	 * 重置密码
	 * 
	 * @param account  账号
	 * @param password 密码
	 * @param validType 验证类型
	 * @param validCode 验证码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void reset(String account, String password, String validType,
			String validCode,Listener<String> onSuccess,ErrorListener onError) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		params.put("valid_type", validType);
		params.put("valid_code", validCode);

		String url = super.getUrl(Method_User_Psd_Reset, "", params);
		volley.request(url,onSuccess,onError);
	}


	/**
	 * 创建客户信息 params 的key包含下面：
	 * 
	 * @param params  mode: 创建模式 1:仅仅创建用户 2:同时创建用户,车辆,到店记录 seller_id: 商户ID, 如果没有默认为0 cust_type:
	 * 用户类型 1:车主 2:商户 cust_name: 用户名称 mobile: 手机 obj_name: 车牌号 frame_no: 车架号
	 * car_brand_id: 品牌ID car_brand: 品牌 car_series_id: 车系ID car_series: 车系
	 * car_type_id: 车款ID car_type: 车款 mileage: 行驶里程 if_arrive: 是否到店,
	 * 1则需要传入到店类型和备注, 0则不需要 business_type: 业务类型 business_content: 业务内容
	 * 
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void create(HashMap<String, String> params,Listener<String> onSuccess,ErrorListener onError) {

		Log.i("WUserApi", "M_Usr_Create create");
		String url = super.getUrl(Wicare_User_Create, "", params);

		Log.i("WUserApi", "M_Usr_Create create url:" + url);
		volley.request(url,onSuccess,onError);
	}


	/**
	 * @param params
	 * 				access_token:"" cust_id:""
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void get(HashMap<String, String> params,Listener<String> onSuccess,ErrorListener onError) {
		String fields = "cust_id,cust_name,cust_type,car_brand,car_series,parent_cust_id,logo,remark,create_time,update_time,photo,address,tel,mobile";
		String url = super.getUrl(Wicare_User_Get, fields, params);
		Log.i("WUserApi", "M_Usr_Create  get url:" + url);
		volley.request(url,onSuccess,onError);
	}

	
	/**
	 * @param params 需要修改的参数前加下划线，_obj_name-更新车牌号
	 * params.put("cust_id", custId); params.put("access_token", "");
	 * params.put("_obj_name", "粤update1");
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void update(HashMap<String, String> params,Listener<String> onSuccess,ErrorListener onError) {
		String url = super.getUrl(Wicare_User_Update, "", params);
		Log.i("WUserApi", "Wicare_User_Update  url:" + url);
		volley.request(url,onSuccess,onError);
	}
	

}
