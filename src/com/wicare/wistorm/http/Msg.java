/**
 * Msg.java
 * c
 * TODO
 * 2015-12-11
 */
package com.wicare.wistorm.http;

/**
 * Msg
 * 
 * @author c TODO 2015-12-11
 */
public class Msg {
	
	/**
	 * 基础模块
	 */
	public final static int M_Base_Brands = 2000;// 获取品牌列表(非活跃数据1d)
	public final static int M_Base_Series = 2001;// 获取车系列表(非活跃数据1d)
	public final static int M_Base_Types = 2002;// 获取车款列表
	

	/**
	 * 用户模块
	 */
	public final static int M_Usr_Register = 100;// 用户注册接口
	public final static int M_Usr_Psd_Reset = 1000;// 用户密码重置
	public final static int M_Usr_Volid_Code = 1001;// 验证短信验证码是否正确

	public final static int M_Usr_Login = 101;// 用户登陆接口
	public final static int M_Usr_Third_Login = 1011;// 第三方登陆接口
	public final static int M_Usr_Delete = 102;// 用户注销接口

	public final static int M_Usr_Create = 1030;// 创建用户接口
	public final static int M_Usr_Get = 1031;// 用户信息接口
	public final static int M_Usr_Update = 1032;// 修改用户信息接口
	public final static int M_Usr_Token = 104;// 获取Access Token
	public final static int M_Usr_Cust_List = 105;// 获取用户下属客户
	public final static int M_Usr_Vehicles = 106;// 获取用户下属车辆
	public final static int M_Usr_Vehicle = 107;// 搜索用户下属车辆
	public final static int M_Usr_Devices = 108;// 获取用户下属设备
	public final static int M_Usr_Devicesh = 109;// 搜索用户下属设备
	public final static int M_Usr_Customer = 110;// 搜索客户
	public final static int M_Usr_Notifications = 111;// 获取用户通知
	public final static int M_Usr_Poi_Create = 112;// 新增兴趣点或者电子围栏
	public final static int M_Usr_Poi_Update = 113;// 更新兴趣点或者电子围栏
	public final static int M_Usr_Pois_List = 114;// 获取用户下属所有兴趣点或电子围栏
	public final static int M_Usr_Pois_Search = 115;// 搜索用户下属所有兴趣点或电子围栏
	public final static int M_Usr_poi_Get = 116;// 获取兴趣单或者电子围栏信息

	/**
	 * 业务模块
	 */
	public final static int M_Biz_Create = 120;// 业务信息
	public final static int M_Biz_Update = 121;// 业务信息

	public final static int M_Biz_Update_Status = 122;// 业务信息
	public final static int M_Biz_List = 123;// 业务信息
	public final static int M_Biz_Total = 124;// 业务信息

	/**
	 * 车辆接口
	 */
	public final static int M_Vehicle_Create = 133;// 新增车辆
	public final static int M_Vehicle_Update = 134;// 修改车辆
	public final static int M_Vehicle_Get = 135;// 获取车辆信息
	public final static int M_Vehicle_Delete = 136;// 删除车辆
	public final static int M_Vehicle_List = 137;// 车辆列表
	public final static int M_Vehicle_Search = 138;// 搜索车辆

	/**
	 * 5.通讯接口 comm
	 */
	public final static int M_Comm_Sms_Send = 500;// 发送短信验证码
}
