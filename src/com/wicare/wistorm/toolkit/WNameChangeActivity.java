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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Wu
 * 
 * 修改帐号的昵称Activity
 */
public class WNameChangeActivity extends Activity {
	
	private static final int UPDATA_NAME  = 1;
	private static final int GET_CUSTOMER = 2;
	private static final int NAME_EXIST   = 3;
	
	EditText et_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_name_change);
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(onClickListener);
		Button bt_sure = (Button) findViewById(R.id.bt_sure);
		bt_sure.setOnClickListener(onClickListener);
		String name = getIntent().getStringExtra("name");
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setText(name);
	}
	
	
	
	/**
	 * 点击事件
	 */
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.iv_back){
				finish();
			}
			if(v.getId() == R.id.bt_sure){
				updateName();
			}
		}
	};
	
	String name_1;

	private void updateName() {
		name_1 = et_name.getText().toString().trim();
		if (name_1.equals("")) {
			Toast.makeText(WNameChangeActivity.this, "昵称不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String url = WConfig.BaseUrl
				+ "exists?query_type=5&value=" + name_1;
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, url, NAME_EXIST).start();
	
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
	
			case NAME_EXIST:
				updateName(msg.obj.toString());
				break;
			case UPDATA_NAME:
				String url = WConfig.BaseUrl 
						+ "customer/" + WConfig.CustomerId
						+ "?auth_code=" + WConfig.AuthCode;
				new HttpThread.getDataThread(mHandler, url, GET_CUSTOMER).start();
				break;
			case GET_CUSTOMER:
				Log.e("ddddddd", msg.obj.toString());
				break;
			}
		}
	};
	
	private void updateName(String str) {
		try {
			JSONObject json = new JSONObject(str);
			if (!json.getBoolean("exist")) {
				Intent data = new Intent();
				data.putExtra("name", name_1);
				setResult(1, data);
				String url = WConfig.BaseUrl 
						+  "customer/" + WConfig.CustomerId
						+ "/field?" 
						+ "auth_code=" + WConfig.AuthCode;
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("field_name", "cust_name"));
				params.add(new BasicNameValuePair("field_type", "String"));
				params.add(new BasicNameValuePair("field_value", name_1));
				
				new HttpThread.putDataThread(mHandler, url, params, UPDATA_NAME).start();
				finish();
			} else {
				Toast.makeText(WNameChangeActivity.this, "昵称已存在", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
}
