package com.wicare.wistorm.toolkit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wicare.wistorm.R;
import com.wicare.wistorm.api.WUserApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.Msg;
import com.wicare.wistorm.ui.WInputField;

/**
 * WLoginActivity 用户登录Activity 继承该类可以复用登录界面和登录功能
 * 
 * @author c
 * @date 2015-10-13
 */
public class WLoginActivity extends Activity implements OnClickListener
		{

	private WInputField etAccount, etPassword;
	public Button btnLogin;//登录按钮
	private int bgId = R.drawable.ws_login_bg; //默认背景
	private int logoId = R.drawable.ws_logo;//默认logo
	public WUserApi userApi;
	public Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_login);
		setBackground(bgId);
		setLogo(logoId);
		etAccount = (WInputField) findViewById(R.id.et_account);
		etPassword = (WInputField) findViewById(R.id.et_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
		
		
	}

	/**
	 * 初始化
	 */
	public void init(){
		//初始化网络
		BaseVolley.init(this);
		// 网络请求回调
		handler = new Handler(callback);
		 //用户api 
		userApi = new WUserApi(handler);
		
	}
	/**
	 * 实现控件点击接口
	 */
	@Override
	public void onClick(View view) {
		// 点击登录
		if (view.getId() == R.id.btn_login) {
			login();
		}
	}

	/**
	 * 
	 * login 点击后发送登录请求
	 */
	private void login() {
		String userName = etAccount.getText().toString().trim();
		String password = etPassword.getText().toString().trim();

		if (userName.length() == 0 || password.length() == 0) {
			//loginFail(WUserApi.Fail_Null);

			etAccount.setShakeAnimation();
			etPassword.setShakeAnimation();
			return;
		}

		btnLogin.setEnabled(false);
		
		// 设置登录监听接口
		// 发送请求
		userApi.login(userName, password);
	}

	
	/**
	 * 网络回调
	 */
	Handler.Callback callback = new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case Msg.M_Usr_Token:
				Bundle bundle = msg.getData();
				String access_token = bundle.getString("access_token");
				String valid_time = bundle.getString("valid_time");
				Log.i("LoginTest", "返回access_token值" + access_token);
				Log.i("LoginTest", "返回valid_time值" + valid_time);
				break;
			case Msg.M_Usr_Login:
				bundle = msg.getData();
				String cust_id = bundle.getString("cust_id");
				access_token = bundle.getString("access_token");
				valid_time = bundle.getString("valid_time");
				Log.i("LoginTest", "返回cust_id值" + cust_id);
				Log.i("LoginTest", "返回access_token值" + access_token);
				Log.i("LoginTest", "返回valid_time值" + valid_time);
				break;
			}
			
			return false;
		}

	};
	
	/**
	 * 实现登录监听接口，登录成功回调函数
	 * 
	
	@Override
	public void loginSucess(String response) {
		Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
		Log.i("WLoginActivity", response);
		btnLogin.setEnabled(true);
	}
 */
	/**
	 * 实现登录监听接口，登录失败回调函数
	 * 
	 
	@Override
	public void loginFail(int statusCode) {
		// 根据错误码，显示不同错误提示信息
		int errorId = R.string.ws_login_id_wrong;

		if (statusCode == WUserApi.Fail_Bound) {
			errorId = R.string.ws_accout_bind_phone;
		} else if (statusCode == WUserApi.Fail_Account
				|| statusCode == WUserApi.Fail_Password) {
			errorId = R.string.ws_login_id_wrong;
		} else if (statusCode == WUserApi.Fail_Net) {
			errorId = R.string.ws_net_timeout;
		} else if (statusCode == WUserApi.Fail_Null) {
			errorId = R.string.ws_login_null;
		}
		Toast.makeText(this, errorId, Toast.LENGTH_LONG).show();
		etAccount.setShakeAnimation();
		etPassword.setShakeAnimation();
		btnLogin.setEnabled(true);
	}
*/
	/**
	 * setBackground 子类重写覆盖此方法可以设置背景图片
	 * 
	 * @param bgId
	 *            图片ID
	 */
	public void setBackground(int bgId) {
		this.bgId = bgId;
		findViewById(R.id.ws_llyt_login).setBackgroundResource(bgId);

	}

	/**
	 * setLogo 子类重写覆盖此方法可以设置logo图片
	 * 
	 * @param logoId
	 *            图片id
	 */
	public void setLogo(int logoId) {
		this.logoId = logoId;
		ImageView ivLogo = (ImageView) findViewById(R.id.ws_iv_logo);
		ivLogo.setImageResource(logoId);

	}

}
