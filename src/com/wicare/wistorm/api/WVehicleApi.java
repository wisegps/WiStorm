package com.wicare.wistorm.api;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.Msg;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;

/**
 * VehicleApi
 * @author c
 * @date 2016-1-19
 */
public class WVehicleApi extends WiStormAPI {
	
	public String Method_Vehicle_Create = "wicare.vehicle.create";//创建车辆信息
	public String Method_Vehicle_Delete = "wicare.vehicle.delete";//删除车辆信息
	public String Method_Vehicle_Update = "wicare.vehicle.update";//修改车辆
	public String Method_Vehicle_List   = "wicare.vehicles.list";//获取车辆列表
	public String Method_Vehicle_Get    = "wicare.vehicle.get";//获取车辆信息

	public HashMap<String, String> hashParam = new HashMap<String, String>();

	private BaseVolley volley;

	public WVehicleApi(){
		init();
	}
	
	public void init(){
		volley = new BaseVolley();
	}

	/**   
	 * @Description:  创建车辆信息 
	 * @param: @param params 
	 * @param: @param onSuccess 连接成功回调 
	 * @param: @param onFailure 连接失败回调     
	 * @return: void            无      
	 */  
	public void create(HashMap<String, String>params,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Method_Vehicle_Create, "", params);
		volley.request(url, onSuccess,onFailure);
	}
	
	
	
	/**   
	 * @Description:  修改车辆信息 
	 * @param: @param params
	 * @param: @param onSuccess 连接成功回调 
	 * @param: @param onFailure 连接失败回调       
	 * @return: void            无   
	 */  
	public void update(HashMap<String, String> params,OnSuccess onSuccess,OnFailure onFailure) {
		String url = super.getUrl(Method_Vehicle_Update, "", params);
		volley.request(url, onSuccess,onFailure);
	}


	/**   
	 * @Description:  车辆信息列表  
	 * @param: @param params
	 * @param: @param fields    
	 * @param: @param onSuccess 连接成功回调 
	 * @param: @param onFailure 连接失败回调       
	 * @return: void            无    
	 */  
	public void list(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure) {
		String url = super.getUrl(Method_Vehicle_List, fields, params);
		volley.request(url, onSuccess,onFailure);
	}
	
	
	/**   
	 * @Description:  删除车辆   
	 * @param: @param params    obj_id：车辆id access_token:token
	 * @param: @param onSuccess 连接成功回调 
	 * @param: @param onFailure 连接失败回调     
	 * @return: void            无
	 */  
	public void delete(HashMap<String, String> params,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Method_Vehicle_Delete, "", params);
		volley.request(url, onSuccess,onFailure);
	}
	
	
	/**   
	 * @Description:  获取车辆信息   
	 * @param: @param params    obj_id：车辆id access_token:token
	 * @param: @param fields    返回字段
	 * @param: @param onSuccess 连接成功回调
	 * @param: @param onFailure 连接失败回调       
	 * @return: void            无   
	 */   
	public void get(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Method_Vehicle_Get, fields, params);
		volley.request(url, onSuccess,onFailure);
	}

}
