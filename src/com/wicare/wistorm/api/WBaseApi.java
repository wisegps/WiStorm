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
 * BaseApi
 * 
 * @author c
 * @date 2015-10-12
 */
public class WBaseApi extends WiStormAPI {

	public String Method_Base_Brands = "wicare.base.car_brands.list";//获取品牌列表(非活跃数据1d)
	public String Method_Base_Series = "wicare.base.car_series.list";//获取车系列表(非活跃数据1d)
	public String Method_Base_types = "wicare.base.car_types.list";//获取车款列表
	public HashMap<String, String> hashParam = new HashMap<String, String>();

	public Context context;
	private Handler uiHandler;// UI线程
	private Handler workHandler;// 工作线程，非UI线程
	private BaseVolley volley;

	public WBaseApi(Handler uiHandler) {
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
			case Msg.M_Base_Brands:
				parseBrands(msg);
				break;
			case Msg.M_Base_Series:
				parseSeries(msg);
				break;
			case Msg.M_Base_Types:
				parseTypes(msg);
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
	private void parseBrands(Message msg) {
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("brands", msg.obj.toString());
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
	}
	
	/**
	 * 解析返回的数据
	 * 
	 * @param msg
	 */
	private void parseSeries(Message msg) {
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("series", msg.obj.toString());
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
	}
	
	/**
	 * 解析返回的数据
	 * 
	 * @param msg
	 */
	private void parseTypes(Message msg) {
			Message uimsg = uiHandler.obtainMessage();
			uimsg.what = msg.what;
			Bundle bundle = msg.getData();
			bundle.putString("types", msg.obj.toString());
			uimsg.setData(bundle);
			uiHandler.sendMessage(uimsg);
	}
	


	/***************************************************** 请求业务 ********************************/

	/**
	 * 获取品牌列表
	 * @param params
	 */
	public void getBrands(HashMap<String, String> params) {
		String url = super.getUrl(Method_Base_Brands, "", params);
		volley.request(url, Msg.M_Base_Brands);
	}

	/**
	 * 获取车系列表
	 * @param params
	 */
	public void getSeries(HashMap<String, String> params) {
		String url = super.getUrl(Method_Base_Series, "", params);
		volley.request(url, Msg.M_Base_Series);
	}

	/**
	 * 获取车款列表
	 * @param params
	 */
	public void getTypes(HashMap<String, String> params) {
		String url = super.getUrl(Method_Base_types, "", params);
		volley.request(url, Msg.M_Base_Types);
	}
}	

 