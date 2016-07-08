package com.wicare.wistorm.api;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wicare.wistorm.toolkit.WEncrypt;

/**
 * UserApi
 * 
 * @author wu
 * @date 2016-03-30
 */
public class WUserApi extends WiStormAPI {
	public String Method_Access_Token    = "wicare.user.access_token";
	public String Method_User_User_exist = "wicare.user.exists";
	public String Method_User_Login      = "wicare.user.login";
	public String Method_User_SSO_Login  = "wicare.user.sso_login";
	public String Method_User_Register   = "wicare.user.register";
	public String Method_User_Psd_Reset  = "wicare.user.password.reset";
	public String Method_User_Volid_Code = "wicare.user.valid_code";
	
	public String Wicare_User_Create     = "wicare.user.create";
	public String Wicare_User_Get        = "wicare.user.get";
	public String Wicare_User_Update     = "wicare.user.update";
	public String Wicare_User_List       = "wicare.users.list";
	public String Wicare_User_Bind       = "wicare.user.bind";

	public Context context;
	private BaseVolley volley;

	public WUserApi(Context context){
		super(context);
		init();
	}
	
	public void init(){
		volley = new BaseVolley();
	}
	
	/* ****与上面的用Handler.what 方式返回不同，这种方法直接用 volley 的 请求回调来返回信息*******************************************************************************************************/

	/**
	 * 获取 Token
	 * 
	 * @param account  帐号
	 * @param password 密码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void getToken(String account, String password,OnSuccess onSuccess,OnFailure onFailure) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		String url = super.getUrl(Method_Access_Token, "", params);
		volley.request(url,onSuccess,onFailure);
	}


	/**
	 * 登陆
	 * 
	 * @param account  帐号
	 * @param password 密码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void login(String account, String password,OnSuccess onSuccess,OnFailure onFailure) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		String url = super.getUrl(Method_User_Login, "", params);
		volley.request(url,onSuccess,onFailure);
	}
	
	/**
	 * 第三方登陆
	 * 
	 * @param loginId 第三方登陆返回的标识ID
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void thridLogin(String loginId,OnSuccess onSuccess,OnFailure onFailure) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("login_id",loginId);
		String url = super.getUrl(Method_User_SSO_Login, "", params);
		volley.request(url,onSuccess,onFailure);
	}
	

	/** 
	 * @param mobile    手机号
	 * @param cust_name 用户名
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void exists(String mobile,OnSuccess onSuccess,OnFailure onFailure){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile",mobile);
		String url = super.getUrl(Method_User_User_exist, "", params);
		volley.request(url,onSuccess,onFailure);
	}
	
	/**
	 * 注册
	 * 
	 * @param account   账号
	 * @param password  密码
	 * @param validCode 验证类型
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void register(String account, String password, String validCode,OnSuccess onSuccess,OnFailure onFailure) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		// 1: 通过手机号 2:通过邮箱
		String validType = "1";
		// 判断手机或者邮箱
		if (account.contains("@")) {
			validType = "2";
			params.put("email", account);
		} else {
			validType = "1";
			params.put("mobile", account);
		}
		params.put("password", password);
		params.put("valid_type", validType);
		params.put("valid_code", validCode);
		String url = super.getUrl(Method_User_Register, "", params);
		volley.request(url,onSuccess,onFailure);
	}

	/**
	 * 验证校验码
	 * 
	 * @param account
	 *            账号
	 * @param validCode
	 *            验证码
	 */
	/**
	 * @param account   账号
	 * @param validCode 验证码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void volidCode(String account, String validCode,OnSuccess onSuccess,OnFailure onFailure) {
		HashMap<String, String> params = new HashMap<String, String>();
		// 1: 通过手机号 2:通过邮箱
		String validType = "1";
		// 判断手机或者邮箱
		if (account.contains("@")) {
			validType = "2";
			params.put("email", account);
		} else {
			validType = "1";
			params.put("mobile", account);
		}
		params.put("valid_type", validType);
		params.put("valid_code", validCode);
		String url = super.getUrl(Method_User_Volid_Code, "", params);
		volley.request(url,onSuccess,onFailure);
	}

	/**
	 * 重置密码
	 * 
	 * @param account  账号
	 * @param password 密码
	 * @param validType 验证类型
	 * @param validCode 验证码
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void reset(String account, String password, String validType,
			String validCode,OnSuccess onSuccess,OnFailure onFailure) {
		password = WEncrypt.MD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("password", password);
		params.put("valid_type", validType);
		params.put("valid_code", validCode);

		String url = super.getUrl(Method_User_Psd_Reset, "", params);
		volley.request(url,onSuccess,onFailure);
	}


	/**
	 * 创建客户信息 params 的key包含下面：
	 * 
	 * @param params  mode: 创建模式 1:仅仅创建用户 2:同时创建用户,车辆,到店记录 seller_id: 商户ID, 如果没有默认为0 cust_type:
	 * 用户类型 1:车主 2:商户 cust_name: 用户名称 mobile: 手机 obj_name: 车牌号 frame_no: 车架号
	 * car_brand_id: 品牌ID car_brand: 品牌 car_series_id: 车系ID car_series: 车系
	 * car_type_id: 车款ID car_type: 车款 mileage: 行驶里程 if_arrive: 是否到店,
	 * 1则需要传入到店类型和备注, 0则不需要 business_type: 业务类型 business_content: 业务内容
	 * 
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void create(HashMap<String, String> params,OnSuccess onSuccess,OnFailure onFailure) {

		Log.i("WUserApi", "M_Usr_Create create");
		String url = super.getUrl(Wicare_User_Create, "", params);

		Log.i("WUserApi", "M_Usr_Create create url:" + url);
		volley.request(url,onSuccess,onFailure);
	}


	/**
	 * @param params
	 * 				access_token:"" cust_id:""
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void get(HashMap<String, String> params,OnSuccess onSuccess,OnFailure onFailure) {
		String fields = "cust_id,cust_name,cust_type,car_brand,car_series,parent_cust_id,logo,remark,create_time,update_time,photo,address,tel,mobile";
		String url = super.getUrl(Wicare_User_Get, fields, params);
		Log.i("WUserApi", "M_Usr_Create  get url:" + url);
		volley.request(url,onSuccess,onFailure);
	}

	
	/**
	 * @param params 本页最下面客户字段中除了cust_id, create_time,mobile,update_time之外的字段，
	 * 				需要修改的条件参数前加下划线如 “_cust_id” 修改客户id 下的参数，其他参数字段不变(如下就是修改车牌)
	 * 				params.put("_cust_id", custId); 
	 * 				params.put("access_token", token);
	 * 				params.put("obj_name", "粤update1");
	 * 
	 * 除了cust_id, create_time,mobile,update_time之外
	 * 
	 * @param onSuccess 连接成功回调
	 * @param onError   连接失败回调
	 */
	public void update(HashMap<String, String> params,OnSuccess onSuccess,OnFailure onFailure) {
		String url = super.getUrl(Wicare_User_Update, "", params);
		Log.i("WUserApi", "Wicare_User_Update  url:" + url);
		volley.request(url,onSuccess,onFailure);
	}
	
	
	/** 
	 * @param params    本页最下面客户字段中除了cust_id, create_time,mobile,update_time之外的字段
	 * @param onSuccess 连接成功回调
	 * @param onFailure 连接失败回调
	 */
	public void bind(HashMap<String, String> params,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Wicare_User_Bind, "", params);
		Log.i("WUserApi", "Wicare_User_Bind  url:" + url);
		volley.request(url,onSuccess,onFailure);
	}
	
	/**
	 * @param params 如下所示：
	 * 				 params.put("access_token",app.access_token);
	 *				 params.put("seller_id", 你的商户ID);//根据商户ID获取用户列表  
	 *	             params.put("sorts", "cust_name");//排序依据
	 *	             params.put("limit", "20");       //返回的数量
	 * @param fields 客户表的字段（请查看最下面的客户表字段）
	 * @param onSuccess 连接成功回调
	 * @param onFailure 连接成功回调
	 */
	public void getList(HashMap<String, String> params,String fields,OnSuccess onSuccess,OnFailure onFailure){
		String url = super.getUrl(Wicare_User_List, fields, params);
		Log.i("WUserApi", "Wicare_User_Cust_List  url:" + url);
		volley.request(url,onSuccess,onFailure);
	}
	
	/*
	 * 客户信息字段
	 * 
	cust_id: Number,
    seller_id: Number,         //商户ID, 如果为商户, 则为0, 如果为车主, 则为车主所绑定的商户, 如果为员工, 则为员工所属商户
    login_id: String,          //第三方登录返回的标识ID
    cust_name: String,         //用户昵称
    cust_type: Number,         //用户类型 0: 无车 1: 车主 2：服务商 3: 员工
    service_type: Number,      //服务商类型（0 销售，1 售后，2 保险，3 理赔，4 代办，5 维修，6 保养）
    car_brand: String,         //车辆品牌
    car_series: String,        //车型
    mobile: String,            //登陆手机
    email: String,             //邮箱地址
    password: String,          //登陆密码
    province: String,          //省份
    city: String,              //城市
    loc: {},                   //经纬度
    logo: String,              //车主头像
    photo: [],                 //店铺照片
    remark: String,            //用户简介
    sex: Number,               //性别
    birth: Date,               //生日
    contacts: String,          //联系人
    address: String,           //联系地址
    tel: String,               //联系电话
    id_card_type: String,      //驾照类型
    annual_inspect_date: Date, //驾照年审
    change_date: Date,         //换证日期
    balance: Number,           //账户余额，仅用于返还现金，暂时不支持充值
    privilege: String,         //操作权限, 格式:功能编码01,功能编码02,功能编码03,功能编码04
    create_time: Date,         //创建时间
    update_time: Date          //更新时间
*/
}
