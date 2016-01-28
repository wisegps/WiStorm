package com.wicare.wistorm.api;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;

import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.Msg;

/**
 * DeviceApi
 * @author c
 * @date 2015-10-12
 */
public class WDeviceApi extends WiStormAPI {
	public String Method_Device_List = "wicare.devices.list";//获取设备列表
	public String Method_Device_Obd_Datas = "wicare.device_obd_datas.list";//获取电压曲线及水温曲线
	public String Method_Device_Update = "wicare.device.update";//更新设备信息
	
	

	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	private Handler uiHandler;// UI线程
	private Handler workHandler;// 工作线程，非UI线程
	private BaseVolley volley;

	public WDeviceApi(Handler uiHandler) {
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
			case Msg.M_Device_List:
				parseList(msg);
				break;
			case Msg.M_Device_Obd_Datas:
				parseOBD(msg);
				break;
			case Msg.M_Device_Update:
				parseUpdate(msg);
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
	private void parseList(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("device", msg.obj.toString());
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
	private void parseOBD(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("device", msg.obj.toString());
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
			bundle.putString("device", msg.obj.toString());
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设备信息列表
	 * @param params
	 */
	public void list(HashMap<String, String> params,String fields) {
		String url = super.getUrl(Method_Device_List, fields, params);
		volley.request(url, Msg.M_Device_List);
	}
	
	/**
	 * 获取电压曲线及水温曲线
	 * @param params
	 */
	public void getObdDatas(HashMap<String, String> params,String fields) {
		String url = super.getUrl(Method_Device_Obd_Datas, fields, params);
		volley.request(url, Msg.M_Device_Obd_Datas);
	}
	
	/**
	 * obd信息
	 * @param params
	 */
	public void update(HashMap<String, String> params,String fields) {
		String url = super.getUrl(Method_Device_Update, fields, params);
		volley.request(url, Msg.M_Device_Update);
	}

}
