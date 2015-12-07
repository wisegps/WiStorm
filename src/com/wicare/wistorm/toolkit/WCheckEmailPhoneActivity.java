package com.wicare.wistorm.toolkit;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.wicare.wistorm.R;
import com.wicare.wistorm.http.HttpThread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Wu
 * 
 * 修改手机号和邮箱验证页面
 */
public class WCheckEmailPhoneActivity extends Activity {
	static final String TAG = "WCheckEmailPhoneActivity";
	private static final int get_captcha = 1;
	private static final int update_account = 2;
	private static final int reset_pwd = 3;
	EditText et_captcha, et_pwd;
	boolean isPhone = true;
	String account = "";
	String valid_code = "";
	/**
	 * 3修改手机,4修改邮箱
	 */
	int mark = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_check_email_phone);
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(onClickListener);
		et_captcha = (EditText) findViewById(R.id.et_captcha);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		Button bt_Submit = (Button) findViewById(R.id.bt_Submit);
		bt_Submit.setOnClickListener(onClickListener);
		TextView tv_account = (TextView) findViewById(R.id.tv_account);
		Intent intent = getIntent();
		isPhone = intent.getBooleanExtra("isPhone", true);
		account = intent.getStringExtra("account");
		mark = intent.getIntExtra("mark", 0);
		tv_account.setText(account);
		GetCaptcha();
		if (mark == 3 || mark == 4) {
			bt_Submit.setText("修改");
		}
	}

	/**
	 * 点击事件
	 */
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.bt_Submit){
				if (mark == 3 || mark == 4) {
					SubmitPhone();
				}
			}
			if(v.getId() == R.id.iv_back){
				finish();
			}
		}
	};
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case get_captcha:
				jsonCaptcha(msg.obj.toString());
				break;
			case update_account:
				jsonPhone(msg.obj.toString());
				break;
			case reset_pwd:
				jsonReset(msg.obj.toString());
				break;
			}
		}
	};

	/**
	 * 请求验证信息
	 */
	private void GetCaptcha() {
		String url;
		if (isPhone) {
			url = WConfig.BaseUrl + "valid_code?mobile=" + account + "&type=1";
		} else {
			url = WConfig.BaseUrl + "valid_code/email?email=" + account
					+ "&type=1";
		}
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, url, get_captcha).start();
		
	}

	/**
	 * @param str 获取验证消息
	 */
	private void jsonCaptcha(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			valid_code = jsonObject.getString("valid_code");
			System.out.println("valid_code = " + valid_code);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改的时候验证
	 */
	private void SubmitPhone() {
		String captcha = et_captcha.getText().toString().trim();
		String pwd = et_pwd.getText().toString().trim();
		
		SharedPreferences preferences = getSharedPreferences(WConfig.USER_DATA, Context.MODE_PRIVATE);
		String old_passwd = preferences.getString(WConfig.PASSWD, "");
		
		if (!old_passwd.equals(SystemTools.getM5DEndo(pwd))) {
			Toast.makeText(WCheckEmailPhoneActivity.this, "密码错误，请重新输入",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!captcha.equals(valid_code)) {
			Toast.makeText(WCheckEmailPhoneActivity.this, "验证码错误，请重新输入",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (mark == 3) {
			String url = WConfig.BaseUrl + "customer/" + WConfig.CustomerId
					+ "/field?auth_code=" + WConfig.AuthCode;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("field_name", "mobile"));
			params.add(new BasicNameValuePair("field_type", "String"));
			params.add(new BasicNameValuePair("field_value", account));
			
			new HttpThread.putDataThread(mHandler, url, params, get_captcha).start();

		} else {
			String url = WConfig.BaseUrl + "customer/" + WConfig.CustomerId
					+ "/field?auth_code=" + WConfig.AuthCode;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("field_name", "email"));
			params.add(new BasicNameValuePair("field_type", "String"));
			params.add(new BasicNameValuePair("field_value", account));
			new HttpThread.putDataThread(mHandler, url, params, update_account).start();
		}
	}

	/**
	 * @param str 修改的结果
	 */
	private void jsonReset(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			if (jsonObject.getString("status_code").equals("0")) {
				setResult(2);
				finish();
			} else {
				Toast.makeText(WCheckEmailPhoneActivity.this, "修改失败", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void jsonPhone(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			if (jsonObject.getString("status_code").equals("0")) {
/*
				SharedPreferences preferences = getSharedPreferences(
						Constant.sharedPreferencesName, Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString(Constant.sp_account, account);
				editor.commit();*/

				Intent intent = new Intent();
				intent.putExtra("isPhone", isPhone);
				intent.putExtra("account", account);
				setResult(2, intent);
				finish();
			} else {
				Toast.makeText(WCheckEmailPhoneActivity.this, "修改失败", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}