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
 * WBusinessApi 业务模块
 * 
 * @author c
 * @date 2015-1-14
 */
public class WBusinessApi extends WiStormAPI {
	public String Method_Biz_Create = "wicare.business.create";//创建业务信息
	public String Method_Biz_Upate = "wicare.business.update";//更新业务
	public String Method_Biz_Upate_Status = "wicare.business.update";//更新业务离店状态
	public String Method_Biz_List = "wicare.business.list";//获取业务列表
	public String Method_Biz_Total = "wicare.business.total";//获取业务统计
	

	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	private BaseVolley volley;

	
	public WBusinessApi(Context context) {
		super(context);
		init();
	}
	
	public void init(){
		volley = new BaseVolley();
	}

	/***************************************************** 请求业务 ********************************/

	/**
	 * 创建业务
	 * @param params
	 */
	public void create(HashMap<String, String> params) {
		String url = super.getUrl(Method_Biz_Create, "", params);
		volley.request(url, Msg.M_Biz_Create);
	}
	

	/**
	 * 更新业务，到店离店
	 * @param params
	 */
	public void update(HashMap<String, String> params) {
		String url = super.getUrl(Method_Biz_Upate, "", params);
		volley.request(url, Msg.M_Biz_Update);
	}

	
	/**
	 * 获取到店离店列表
	 * @param params
	 */
	
	public void get(HashMap<String, String> params,String fields) {
		String url = super.getUrl(Method_Biz_List, fields, params);
		volley.request(url, Msg.M_Biz_List);
	}
	
	/**
	 * 获取业务统计
	 * @param params
	 */
	
	public void getTotal(HashMap<String, String> params,String fields) {
		String url = super.getUrl(Method_Biz_Total, fields, params);
		volley.request(url, Msg.M_Biz_Total);
	}



}
