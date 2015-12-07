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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Wu
 * 
 * 修改密码页面
 */
public class WUpdatePwdActivity extends Activity{
	private static final int update_pwd = 1;
	EditText et_pwd,et_new_pwd,et_new_pwd_again;
	String new_pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_updata_passwd);
		ImageView iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(onClickListener);
		ImageView iv_sure = (ImageView)findViewById(R.id.iv_sure);
		iv_sure.setOnClickListener(onClickListener);
		et_pwd = (EditText)findViewById(R.id.et_pwd);
		et_new_pwd = (EditText)findViewById(R.id.et_new_pwd);
		et_new_pwd_again = (EditText)findViewById(R.id.et_new_pwd_again);
	}
	
	
	/**
	 * 点击事件
	 */
	OnClickListener onClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.iv_back){
				finish();
			}
			if(v.getId() == R.id.iv_sure){
				updatePwd();
			}
		}		
	};
	
	
	/**
	 * handler 信息处理
	 */
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case update_pwd:
				jsonUpdatePwd(msg.obj.toString());
				break;
			}
		}		
	};
	
	/**
	 * 修改密码
	 */
	private void updatePwd(){
		String pwd = et_pwd.getText().toString().trim();
		new_pwd = et_new_pwd.getText().toString().trim();
		String new_pwd_again = et_new_pwd_again.getText().toString().trim();
		
		if(pwd.equals("") || new_pwd.equals("") || new_pwd_again.equals("")){
			Toast.makeText(WUpdatePwdActivity.this, 
					getResources().getString(R.string.finish_all_msg), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!new_pwd.equals(new_pwd_again)){
			Toast.makeText(WUpdatePwdActivity.this, 
					getResources().getString(R.string.pw_two_diff), Toast.LENGTH_SHORT).show();
			return;
		}
		
		SharedPreferences preferences = getSharedPreferences(WConfig.USER_DATA, Context.MODE_PRIVATE);
        String old_passwd = preferences.getString(WConfig.PASSWD, "");

        if(!old_passwd.equals(SystemTools.getM5DEndo(pwd))){
        	Toast.makeText(WUpdatePwdActivity.this, 
        			getResources().getString(R.string.pw_err_again), Toast.LENGTH_SHORT).show();
        	return ;
        }
        String url = WConfig.BaseUrl 
        		+ "customer/" + WConfig.CustomerId 
        		+ "/field?auth_code=" + WConfig.AuthCode;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("field_name", "password"));
        params.add(new BasicNameValuePair("field_type", "String"));
        params.add(new BasicNameValuePair("field_value", SystemTools.getM5DEndo(new_pwd)));
        
        new HttpThread.putDataThread(mHandler, url, params, update_pwd).start();
	}
	
	
	
	/**
	 * @param str 修改密码验证信息
	 */
	private void jsonUpdatePwd(String str){
		try {
			JSONObject jsonObject = new JSONObject(str);
			if(jsonObject.getString("status_code").equals("0")){
				SharedPreferences preferences = getSharedPreferences(WConfig.USER_DATA, Context.MODE_PRIVATE);
		        Editor editor = preferences.edit();
		        editor.putString(WConfig.PASSWD, SystemTools.getM5DEndo(new_pwd));
		        editor.commit();
		        Toast.makeText(WUpdatePwdActivity.this, 
		        		getResources().getString(R.string.pw_ud_succ), Toast.LENGTH_SHORT).show();
		        finish();
			}else{
				Toast.makeText(WUpdatePwdActivity.this, 
						getResources().getString(R.string.pw_ud_fail), Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
