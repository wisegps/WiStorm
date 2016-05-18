package com.wicare.wistorm.api;

import java.util.HashMap;

import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;

/**
 * BaseApi 基础API
 * 
 * @author wu
 * @date 2015-10-12
 */
public class WBaseApi extends WiStormAPI {

	public String Method_Base_Brands = "wicare.base.car_brands.list";//获取品牌列表(非活跃数据1d)
	public String Method_Base_Series = "wicare.base.car_series.list";//获取车系列表(非活跃数据1d)
	public String Method_Base_types = "wicare.base.car_types.list";//获取车款列表
	
	private BaseVolley volley;

	public WBaseApi(){
		init();
	}
	
	/**
	 * 初始化 (在WbaseApi实例化时候会初始化)
	 */
	public void init(){
		volley = new BaseVolley();
	}
	
	/**
	 * @param params    参数
	 *				    params.put("access_token", app.access_token);
	 *					params.put("id", ">0");//>0获取所有的车辆品牌
	 *					params.put("page", "t_spell");
	 *					params.put("sorts", "t_spell");//按照首字母排列
	 *					params.put("limit", "-1");//-1 没有限制
	 * @param fields    返回信息字段     fields = "pid,name,url_icon,t_spell";
	 * @param onSuccess 连接成功回调
	 * @param onFailure 连接失败回调
	 */
	public void getBrands(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Method_Base_Brands, fields, params);
		volley.request(url, onSuccess, onFailure);
	}
	
	/**
	 * @param params    参数
					    params.put("access_token", app.access_token);
						params.put("pid", pid);//车系列的id (车系列id就是车品牌返回数据中的 id)
						params.put("page", "go_name");
						params.put("sorts", "go_name");//按照首字母排列
						params.put("limit", "-1");//-1 没有限制
	 * @param fields    返回字段 "pid,name,show_name,go_id,go_name"
	 * @param onSuccess 连接成功回调
	 * @param onFailure 连接失败回调
	 */
	public void getSeries(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Method_Base_Series, fields, params);
		volley.request(url, onSuccess, onFailure);
	}
	
	/**
	 * @param params    参数
					    params.put("access_token", app.access_token);
						params.put("pid", pid);//车款的id (车款id就是车系列返回数据中的 id)
						params.put("page", "go_name");
						params.put("sorts", "name");//按照首字母排列
						params.put("limit", "-1");//-1 没有限制
	 * @param fields    返回字段 "pid,name,show_name,go_id,go_name"
	 * @param onSuccess 连接成功回调
	 * @param onFailure 连接失败回调
	 */
	public void getTypes(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Method_Base_types, fields, params);
		volley.request(url, onSuccess, onFailure);
	}
		
}	

 