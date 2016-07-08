package com.wicare.wistorm.api;

import java.util.HashMap;
import android.content.Context;

import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
/**
 * 发送短信验证码
 * 
 * @author WU
 * @date 2015-10-12
 */
public class WCommApi extends WiStormAPI {
	
	public String Method_Comm_Sms_Send   = "wicare.comm.sms.send";
	/* type: 发送短信类型   1: 普通校验码信息 2: 忘记密码校验信息*/
	public final static int Tpye_Nomal = 1,Type_Forget_Password = 2;
	public Context context;
	private BaseVolley volley;

	public WCommApi(Context context){
		super(context);
		init();
	}
	
	/**
	 * 初始化BaseVolley
	 */
	public void init(){
		volley = new BaseVolley();
	}

	/**
	 * 发送短信验证码
	 * @param account 手机号
	 * @param type type: 发送短信类型   1: 普通校验码信息 2: 忘记密码校验信息
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void sendSMS(String mobile, int type,OnSuccess onSuccess, OnFailure onFailure){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("type", type+"");
		String url = super.getUrl(Method_Comm_Sms_Send, "", params);
		volley.request(url, onSuccess,onFailure);
	}

}
