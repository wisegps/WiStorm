package com.wicare.wistorm.toolkit;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.wicare.wistorm.R;
import com.wicare.wistorm.http.HttpThread;
import com.wicare.wistorm.ui.WBottomPopupWindow;
import com.wicare.wistorm.ui.WBottomPopupWindow.OnItemClickListener;
import com.wicare.wistorm.ui.WDateSelector;
import com.wicare.wistorm.ui.WDateSelector.OnDateChangedListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountActivity extends Activity {
	
	static final String TAG = "AccountActivity";
	
	String accountUrl = "http://api.bibibaba.cn/customer/178?auth_code=127a154df2d7850c4232542b4faa2c3d";
	private static final int UPDATA_ACCOUNT  = 1;
	
	private static final int GET_ACCOUNT_MSG = 2;
	
	
	private TextView tv_phone, tv_name, tv_email, tv_sex, tv_birth;
	private ImageView iv_pic;
	private RequestQueue mQueue;
	private String birth;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_account_activity);
		mQueue = Volley.newRequestQueue(this);
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(onClickListener);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_pic.setOnClickListener(onClickListener);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone.setOnClickListener(onClickListener);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_name.setOnClickListener(onClickListener);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_email.setOnClickListener(onClickListener);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_sex.setOnClickListener(onClickListener);
		tv_birth = (TextView) findViewById(R.id.tv_birth);
		tv_birth.setOnClickListener(onClickListener);
		TextView tv_update_pwd = (TextView) findViewById(R.id.tv_update_pwd);
		tv_update_pwd.setOnClickListener(onClickListener);
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, accountUrl, GET_ACCOUNT_MSG).start();	
	}
	
	String sexItems[] = {"男","女"};
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.iv_back){
				finish();
			}
			if(v.getId() == R.id.tv_name){
	
				String name = tv_name.getText().toString().trim();
				Intent intent2 = new Intent(AccountActivity.this, WNameChangeActivity.class);
				intent2.putExtra("name", name);
				startActivityForResult(intent2, 1);
		       
			}
			if(v.getId() == R.id.tv_sex){
				AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle(getResources().getString(R.string.sex_title));
                builder.setSingleChoiceItems(sexItems,0,new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                    	String url = "http://api.bibibaba.cn/customer/178?/field?auth_code=127a154df2d7850c4232542b4faa2c3d";
                    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        				params.add(new BasicNameValuePair("field_name", "sex"));
        				params.add(new BasicNameValuePair("field_type", "Number"));
        				params.add(new BasicNameValuePair("field_value", String.valueOf(which)));
                    	tv_sex.setText(sexItems[which]);
						new HttpThread.putDataThread(mHandler, url, params, UPDATA_ACCOUNT).start();
                    	dialog.dismiss();  
                    }  
                });  
                builder.setNegativeButton(getResources().getString(R.string.cancel),
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
                builder.show();
			}
			if(v.getId() == R.id.tv_update_pwd){
				
			}
			if(v.getId() == R.id.tv_phone){
				
			}
			if(v.getId() == R.id.tv_email){
				
			}
			if(v.getId() == R.id.iv_pic){//更改头像
				picPop();
			}
			if(v.getId() == R.id.tv_birth){//设置生日日期
				WDateSelector dateSelector = new WDateSelector(AccountActivity.this);
				dateSelector.setDate();
				dateSelector.setOnDateChangedListener(new OnDateChangedListener() {
					
					@Override
					public void onDateChanged(String year, String month, String day) {
					
						String url = "http://api.bibibaba.cn/customer/178/field?auth_code=127a154df2d7850c4232542b4faa2c3d";
						String Date = year + "-" + month + "-" + day;
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("field_name", "birth"));
						params.add(new BasicNameValuePair("field_type", "Date"));
						params.add(new BasicNameValuePair("field_value", Date));
						new HttpThread.putDataThread(mHandler, url, params, UPDATA_ACCOUNT).start();
						tv_birth.setText(Date);
					}
				});
			}
		}
	};

	
	/**
     * Handler 处理消息
     */
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler() {
    	
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {  
            case GET_ACCOUNT_MSG:
                jsonCustomer(msg.obj.toString());
                break;
            case UPDATA_ACCOUNT:
            	updaAccount();
                break;    
                
            }
        }
	};
	
	
	/**
	 * 修改信息过后更新ui界面
	 */
	private void updaAccount(){
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, accountUrl, GET_ACCOUNT_MSG).start();
	}
	
	
	/**
	 * @param accountJson
	 */
	private void jsonCustomer(String accountJson) {
		try {
			JSONObject jsonObject = new JSONObject(accountJson);
			tv_phone.setText(jsonObject.getString("mobile"));
			tv_name.setText(jsonObject.getString("cust_name"));
			tv_email.setText(jsonObject.getString("email"));
			if (jsonObject.getString("sex").equals("0")) {
				tv_sex.setText("男");
			} else {
				tv_sex.setText("女");
			}
			birth = jsonObject.getString("birth").substring(0, 10);
			tv_birth.setText(birth);
			String logo = jsonObject.getString("logo");
			if (logo == null || logo.equals("")) {

			} else {
				mQueue.add(new ImageRequest(logo, new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						iv_pic.setImageBitmap(response);
					}
				}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				}));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 修改头像 并更新
	 */
	private void picPop() {
		List<String> items = new ArrayList<String>();
		items.add("拍照");
		items.add("从手机相册中选取");
		items.add("取消");
		final WBottomPopupWindow popView = new WBottomPopupWindow(this);
		popView.initView(findViewById(R.id.ll_account));//这个布局为参考
		popView.setData(items);
		popView.SetOnItemClickListener(new OnItemClickListener() {
			@Override
			public void OnItemClick(int index) {
				switch (index) {
				case 0:
					Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent1, 1);
					popView.dismiss();
					break;
				case 1:
					Intent intent = new Intent();
					/* 开启Pictures画面Type设定为image */
					intent.setType("image/*");
					/* 使用Intent.ACTION_GET_CONTENT这个Action */
					intent.setAction(Intent.ACTION_GET_CONTENT);
					/* 取得相片后返回本画面 */
					startActivityForResult(intent, 9);
					popView.dismiss();
					break;
				case 2:
					popView.dismiss();
					break;
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//				UpdateBitmap(bitmap);
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
			return;
		} else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.v("TestFile", "SD card is not avaiable/writeable right now.");
				return;
			}
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//			UpdateBitmap(bitmap);
		}
	}

	
}	

