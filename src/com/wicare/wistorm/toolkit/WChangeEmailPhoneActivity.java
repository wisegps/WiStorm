package com.wicare.wistorm.toolkit;

import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

import com.wicare.wistorm.R;
import com.wicare.wistorm.http.HttpThread;

import android.app.Activity;
import android.content.Intent;
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
 * 修改手机号和邮箱页面
 * 
 * @author Wu
 * 
 */
public class WChangeEmailPhoneActivity extends Activity {
	
	static final String TAG = "WChangeEmailPhoneActivity";
	private static final int exists = 1;
	TextView tv_title;
	EditText et_account;
	boolean isPhone = true;
	String account;
	/**
	 * 3修改手机,4修改邮箱
	 */
	int mark = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_change_email_phone);
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(onClickListener);
		tv_title = (TextView) findViewById(R.id.tv_title);
		Button bt_register = (Button) findViewById(R.id.bt_register);
		bt_register.setOnClickListener(onClickListener);
		et_account = (EditText) findViewById(R.id.et_account);
		Intent intent = getIntent();
		mark = intent.getIntExtra("mark", 0);
		if (mark == 3) {
			tv_title.setText("修改手机");
			bt_register.setText("下一步");
			et_account.setText(intent.getStringExtra("phone"));
			et_account.setHint("请输入要修改的手机号码");
		} else if (mark == 4) {
			tv_title.setText("修改邮箱");
			bt_register.setText("下一步");
			et_account.setText(intent.getStringExtra("email"));
			et_account.setHint("请输入要修改的邮箱");
		}
	}

	/**
	 * 点击事件
	 */
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.bt_register){
				if (mark == 3) {
					ChangePhone();
				} else if (mark == 4) {
					ChangeEmail();
				}
			}
			if(v.getId() == R.id.iv_back){
				finish();
			}
		}
	};

	/**
	 * Handler 处理
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case exists:
				jsonExists(msg.obj.toString());
				break;
			}
		}
	};

	/**
	 * 修改手机号
	 */
	private void ChangePhone() {
		account = et_account.getText().toString().trim();
		String url = WConfig.BaseUrl + "exists?query_type=6&value=" + account;
		if (account.equals("")) {
			Toast.makeText(WChangeEmailPhoneActivity.this, "请填写手机号码", Toast.LENGTH_SHORT)
					.show();
		} else if (account.length() == 11 && isNumeric(account)) {
			isPhone = true;
			//开启线程获取服务器数据
			new HttpThread.getDataThread(mHandler, url, exists).start();	
		} else {
			Toast.makeText(WChangeEmailPhoneActivity.this, "您手机号码格式不正确",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 修改邮箱
	 */
	private void ChangeEmail() {
		account = et_account.getText().toString().trim();
		String url = WConfig.BaseUrl + "exists?query_type=6&value=" + account;
		if (account.equals("")) {
			Toast.makeText(WChangeEmailPhoneActivity.this, "请填写邮箱", Toast.LENGTH_SHORT)
					.show();
		} else if (isEmail(account)) {
			isPhone = false;
			//开启线程获取服务器数据
			new HttpThread.getDataThread(mHandler, url, exists).start();	
		} else {
			Toast.makeText(WChangeEmailPhoneActivity.this, "您输入的邮箱格式不正确",
					Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * @param result 返回验证的信息是否存在
	 */
	private void jsonExists(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			boolean isExist = jsonObject.getBoolean("exist");
			if (isExist) {// true ,账号已存在
				if(mark == 3) {
					Toast.makeText(WChangeEmailPhoneActivity.this, "该手机号码已注册，请重新输入",
							Toast.LENGTH_SHORT).show();
				} else if (mark == 4) {
					Toast.makeText(WChangeEmailPhoneActivity.this, "该邮箱已注册，请重新输入",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				if (mark == 3 || mark == 4) {
					Intent intent = new Intent(WChangeEmailPhoneActivity.this,
							WCheckEmailPhoneActivity.class);
					intent.putExtra("account", account);
					intent.putExtra("isPhone", isPhone);
					intent.putExtra("mark", mark);
					startActivityForResult(intent, 1);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		return pattern.matcher(str).matches();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 2:
			setResult(2, data);
			finish();
			break;
		}
	}

}