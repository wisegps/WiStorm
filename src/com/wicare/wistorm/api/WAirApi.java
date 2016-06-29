package com.wicare.wistorm.api;

import java.util.HashMap;

import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;

public class WAirApi extends WiStormAPI{

	public String Method_Set_Command = "wicare.command.create";
	
	public static final String  AIR_SPEED  = "16453";
	public static final String  AIR_SWITCH = "16451";
	public static final String  AIR_MODEL  = "16452";
	private BaseVolley volley;
	
	public WAirApi(){
		init();
	}

	/**
	 * 初始化BaseVolley()
	 */
	public void init(){
		volley = new BaseVolley();
	}
	

	/**
	 * @param token     token
	 * @param device_id 设备ID
	 * @param command   控制净化器速度的指令
	 * @param onSuccess 提交数据成功的回调
	 * @param onError   提交数据失败的回调
	 */
	public void setAirSpeed(String token,String device_id,String command,OnSuccess onSuccess,OnFailure onFailure){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", token);
		params.put("device_id", device_id);
		params.put("params", command);
		params.put("cmd_type", AIR_SPEED);
		String url = super.getUrl(Method_Set_Command, "", params);
		volley.request(url, onSuccess,onFailure);
	}
	

	/**
	 * @param token     token
	 * @param device_id 设备ID
	 * @param command   控制净化器模式的指令
	 * @param onSuccess 提交数据成功的回调
	 * @param onError   提交数据失败的回调
	 */
	public void setAirModel(String token,String device_id,String command,OnSuccess onSuccess,OnFailure onFailure){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", token);
		params.put("device_id", device_id);
		params.put("params", command);
		params.put("cmd_type", AIR_MODEL);
		String url = super.getUrl(Method_Set_Command, "", params);
		volley.request(url, onSuccess,onFailure);
	}
	
	/**
	 * @param token     token
	 * @param device_id 设备ID
	 * @param command   控制净化器开关的指令
	 * @param onSuccess 提交数据成功的回调
	 * @param onError   提交数据失败的回调
	 */
	public void setAirSwitch(String token,String device_id,String command,OnSuccess onSuccess,OnFailure onFailure){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", token);
		params.put("device_id", device_id);
		params.put("params", command);
		params.put("cmd_type", AIR_SWITCH);
		String url = super.getUrl(Method_Set_Command, "", params);
		volley.request(url, onSuccess,onFailure);
	}

}
