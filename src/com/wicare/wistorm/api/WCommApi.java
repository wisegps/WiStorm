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

import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.Msg;
/**
 * CommApi
 * @author c
 * @date 2015-10-12
 */
public class WCommApi extends WiStormAPI {
	public String Method_Comm_Sms_Send = "wicare.comm.sms.send";
	
	
	// type: 发送短信类型   1: 普通校验码信息 2: 忘记密码校验信息
	public final static int Tpye_Nomal = 1,Type_Forget_Password = 2;
	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	private Handler uiHandler;// UI线程
	private Handler workHandler;// 工作线程，非UI线程
	private BaseVolley volley;

	public WCommApi(Handler uiHandler) {
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
			case Msg.M_Comm_Sms_Send:
				break;
			}
			return false;
		}
	};

	/**
	 * 发送短信验证码
	 * @param mobile 手机号
	 * @param type type: 发送短信类型   1: 普通校验码信息 2: 忘记密码校验信息
	 */
	public void sendSMS(String mobile, int type) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("type", type+"");
		String url = super.getUrl(Method_Comm_Sms_Send, "", params);
		volley.request(url, Msg.M_Comm_Sms_Send);
	}
	
	/**
	 * 发送短信验证码
	 * @param mobile 手机号
	 * @param type type: 发送短信类型   1: 普通校验码信息 2: 忘记密码校验信息
	 */
	public void volidCode(String mobile, int type) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("type", type+"");
		String url = super.getUrl(Method_Comm_Sms_Send, "", params);
		volley.request(url, Msg.M_Comm_Sms_Send);
	}


}
