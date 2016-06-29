package com.wicare.wistorm.api;

import java.util.HashMap;

import android.content.Context;

import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;

/**
 * DeviceApi
 * @author c
 * @date 2015-10-12
 */
public class WDeviceApi extends WiStormAPI {
	
	public String Method_Device_List = "wicare.devices.list";//获取设备列表
	public String Method_Device_Obd_Datas = "wicare.device_obd_datas.list";//获取电压曲线及水温曲线
	public String Method_Device_Update = "wicare.device.update";//更新设备信息
	public String Method_Device_Get = "wicare.device.get";//获取单个设备信息

	public Context context;
	private BaseVolley volley;
	
	public WDeviceApi(){
		init();
	}
	
	public void init(){
		volley = new BaseVolley();
	}
	
	
	/** ----------------------------------------------------------------------------------------*/
	
	/**
	 * 获取设备列表信息
	 * 
	 * @param params
	 * 			参数字段
	 * @param fields
	 * 			返回信息字段
	 * @param onSuccess
	 * 			连接成功回调
	 * @param onFailure
	 * 			连接失败回调
	 */
	public void getDevicelist(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure) {
		String url = super.getUrl(Method_Device_List, fields, params);
		volley.request(url, onSuccess,onFailure);
	}
	
	/**
	 * 获取OBD信息
	 * 
	 * @param params
	 * 			参数字段
	 * @param fields
	 * 			返回信息字段
	 * @param onSuccess
	 * 			连接成功回调
	 * @param onFailure
	 * 			连接失败回调
	 */
	public void getObdDataList(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure) {
		String url = super.getUrl(Method_Device_Obd_Datas, fields, params);
		volley.request(url, onSuccess,onFailure);
	}
	
	/**
	 * 更新设备信息
	 * 
	 * @param params
	 * 			参数字段
	 * @param fields
	 * 			返回信息字段
	 * @param onSuccess
	 * 			连接成功回调
	 * @param onFailure
	 * 			连接失败回调
	 */
	public void update(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure) {
		String url = super.getUrl(Method_Device_Update, fields, params);
		volley.request(url, onSuccess,onFailure);
	}
	
	/**
	 * 获取单个设备的信息
	 * 
	 * @param params
	 * 			参数字段
	 * @param fields
	 * 			返回信息字段
	 * @param onSuccess
	 * 			连接成功回调
	 * @param onFailure
	 * 			连接失败回调
	 */
	public void get(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Method_Device_Get, fields, params);
		volley.request(url, onSuccess,onFailure);
	}
}
