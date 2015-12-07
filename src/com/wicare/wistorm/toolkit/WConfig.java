package com.wicare.wistorm.toolkit;

import android.os.Environment;

/**
 * @author Wu
 * wistorm 的一些配置如：文件路径，url等
 */
public class WConfig {

	public static String BaseUrl = "http://api.bibibaba.cn/";
	public static String CustomerId = "178";
	public static String AuthCode = "127a154df2d7850c4232542b4faa2c3d";
	
	/** 图片路径存储地址 **/
	public static String BasePath = Environment.getExternalStorageDirectory().getPath() + "/wistorm/";
	/** 存放用户头像 **/
	public static String userIconPath = BasePath + "userIcon/";
	/** 车品牌logo **/
	public static String VehicleLogoPath = BasePath + "vehicleLogo/";
	public static String USER_DATA = "userData";
	public static String PASSWD    = "password";
}
