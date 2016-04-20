package com.wicare.wistorm.api;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.Msg;

/**
 * VehicleApi
 * @author c
 * @date 2016-1-19
 */
public class WVehicleApi extends WiStormAPI {
	public String Method_Vehicle_Update = "wicare.vehicle.update";//修改车辆
	public String Method_Vehicle_Search = "wicare.vehicles.search";//搜索车辆信息
	public String Method_Vehicle_List = "wicare.vehicles.list";//删除车辆

	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	private Handler uiHandler;// UI线程
	private Handler workHandler;// 工作线程，非UI线程
	private BaseVolley volley;

	public WVehicleApi(Handler uiHandler) {
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
			case Msg.M_Vehicle_Update:
				parseUpdate(msg);
				break;
			case Msg.M_Vehicle_List:
				parseList(msg);
				break;
			case Msg.M_Vehicle_Search:
				parseSearch(msg);
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
	private void parseUpdate(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("vehicle", msg.obj.toString());
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
	private void parseSearch(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("vehicle", msg.obj.toString());
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
	private void parseList(Message msg) {
		try {
			JSONObject json = new JSONObject(msg.obj.toString());
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("vehicle", msg.obj.toString());
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	


	/***************************************************** 请求业务 ********************************/

	/**
	 * 修改车辆信息
	 * @param params
	 */
	public void update(HashMap<String, String> params) {
		String url = super.getUrl(Method_Vehicle_Update, "", params);
		volley.request(url, Msg.M_Vehicle_Update);
	}
	
	
	/**
	 * 搜索车辆信息
	 * @param params
	 */
	public void search(HashMap<String, String> params,String fields) {
		String url = super.getUrl(Method_Vehicle_Search, fields, params);
		volley.request(url, Msg.M_Vehicle_Search);
	}
	
	/**
	 * 车辆信息列表
	 * @param params
	 */
	public void list(HashMap<String, String> params,String fields) {
		String url = super.getUrl(Method_Vehicle_List, fields, params);
		volley.request(url, Msg.M_Vehicle_List);
	}


}
