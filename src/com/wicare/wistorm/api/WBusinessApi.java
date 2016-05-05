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
	private Handler uiHandler;// UI线程
	private Handler workHandler;// 工作线程，非UI线程
	private BaseVolley volley;

	public WBusinessApi(Handler uiHandler) {
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
			case Msg.M_Biz_Create:
				Log.i("WUserApi",
						"M_Usr_Token handleCallBack" + msg.obj.toString());
				parseCreate(msg);
				break;
			case Msg.M_Biz_Update:
				Log.i("WUserApi",
						"M_Biz_Update handleCallBack" + msg.obj.toString());
				parseUpdate(msg);
				break;
			case Msg.M_Biz_List:
				Log.i("WUserApi",
						"Method_Biz_List handleCallBack" + msg.obj.toString());
				parseGet(msg);
				break;
			case Msg.M_Biz_Total:
				Log.i("WUserApi",
						"M_Biz_Total handleCallBack" + msg.obj.toString());
				parseGetTotal(msg);
				break;
			}
			return false;
		}
	};

	/***************************************************** 解析网络返回的数据 ********************************/
	/**
	 * 解析返回的数据
	 * 
	 * @param msg
	 */
	private void parseCreate(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			String status_code = json.getString("status_code");
			String business_id = json.getString("business_id");
			bundle.putString("status_code", status_code);
			bundle.putString("business_id", business_id);
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析返回的数据
	 * 
	 * @param msg
	 */
	private void parseUpdate(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			String status_code = json.getString("status_code");
			bundle.putString("status_code", status_code);
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 解析返回的数据
	 * 
	 * @param msg
	 */
	private void parseGet(Message msg) {
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("list", msg.obj.toString());
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
	}
	
	
	/**
	 * 解析返回的数据
	 * 
	 * @param msg
	 */
	private void parseGetTotal(Message msg) {
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("total", msg.obj.toString());
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
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
