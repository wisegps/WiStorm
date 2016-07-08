package com.wicare.wistorm.toolkit;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.wicare.wistorm.R;
import com.wicare.wistorm.api.WUserApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wicare.wistorm.ui.WInputField;

/**
 * WLoginActivity 用户登录Activity 继承该类可以复用登录界面和登录功能
 * 
 * @author Wu
 * @date 2015-10-13
 */
public abstract class WLoginActivity extends Activity implements OnClickListener{

	private WInputField etAccount, etPassword;
	public Button btnLogin;//登录按钮
	private int logoId  = R.drawable.ic_launcher;//默认logo
	private int bgColor = R.color.blue_press;
	public WUserApi userApi;
	protected abstract void onClickRegister();//点击注册账号
	protected abstract void onClickUpdataPassword();//点击重置密码
	protected abstract void onLoginSuccess(String response);//登陆成功
	protected abstract void onLoginFail();//登陆失败
	protected abstract void setUpView();//初始化UI
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_login);
		init();
		setUpView();
	}

	/**
	 * 初始化
	 */
	public void init(){
		setBackgroundColor(bgColor);
		setLogo(logoId);
		etAccount = (WInputField) findViewById(R.id.et_account);
		etPassword = (WInputField) findViewById(R.id.et_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
		findViewById(R.id.tv_register).setOnClickListener(this);
		findViewById(R.id.tv_password).setOnClickListener(this);
		//初始化网络
		BaseVolley.init(WLoginActivity.this);
		 //用户api 
		userApi = new WUserApi(WLoginActivity.this);
	}
	
	
	@Override
	public void onClick(View view) {
		// 点击登录
		if (view.getId() == R.id.btn_login) {
			login();
		}else if(view.getId() == R.id.tv_register){
			onClickRegister();
		}else if(view.getId() == R.id.tv_password){
			onClickUpdataPassword();
		}
	}

	/**
	 * login 点击后发送登录请求
	 */
	private void login() {
		String userName = etAccount.getText().toString().trim();
		String password = etPassword.getText().toString().trim();
		if (userName.length() == 0 || password.length() == 0) {
			//loginFail(WUserApi.Fail_Null);
			Toast.makeText(WLoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
			etAccount.setShakeAnimation();
			etPassword.setShakeAnimation();
			return;
		}
		btnLogin.setEnabled(false);
		userApi.login(userName, password, new OnSuccess() {
			
			@Override
			protected void onSuccess(String response) {
				// TODO Auto-generated method stub
				parseLogin(response);
			}
		},new OnFailure() {
			
			@Override
			protected void onFailure(VolleyError error) {
				// TODO Auto-generated method stub
				btnLogin.setEnabled(true);
				onLoginFail();
			}
		});
	}

	
	/**
	 * @param strJson
	 */
	/**   
	 * @Title: parseLogin   
	 * @Description: TODO(解析)   
	 * @param: @param strJson      
	 * @return: void      
	 * @throws   
	 */  
	private void parseLogin(String strJson){
		try {
			JSONObject object = new JSONObject(strJson);	
			if("0".equals(object.getString("status_code"))){
//				String access_token = object.getString("access_token");
//				String cust_id      = object.getString("cust_id");
//				String cust_name    = object.getString("cust_name");
				btnLogin.setEnabled(true);
				onLoginSuccess(strJson);
			}else{
				btnLogin.setEnabled(true);
				onLoginFail();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 在继承WloginActivity 的 setUpView()方法中去重写setBackground()可以设置背景图片
	 * 
	 * @param bgId 背景图片id
	 *            
	 */
	public void setBackground(int bgId) {
		findViewById(R.id.ws_llyt_login).setBackgroundResource(bgId);
	}
	
	
	/** 
	 * 在继承WloginActivity 的 setUpView()方法中去重写setBackground()可以设置背景颜色
	 * 
	 * @param bgColor 登陆背景颜色
	 */
	public void setBackgroundColor(int bgColor){
		this.bgColor = bgColor;
		findViewById(R.id.ws_llyt_login).setBackgroundColor(bgColor);
	}
	
	

	/**
	 * 在继承WloginActivity 的 setUpView()方法中去重写setLogo()可以设置app的logo
	 * 
	 * @param logoId 背景图片id
	 */
	public void setLogo(int logoId) {
		this.logoId = logoId;
		ImageView ivLogo = (ImageView) findViewById(R.id.ws_iv_logo);
		ivLogo.setImageResource(logoId);

	}

}
