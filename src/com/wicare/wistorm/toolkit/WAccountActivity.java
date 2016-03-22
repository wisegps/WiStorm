package com.wicare.wistorm.toolkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.wicare.wistorm.toolkit.WUploadUtil.OnUploadProcessListener;
import com.wicare.wistorm.ui.WBottomPopupWindow;
import com.wicare.wistorm.ui.WBottomPopupWindow.OnItemClickListener;
import com.wicare.wistorm.ui.pickerview.TimePopupWindow;
import com.wicare.wistorm.ui.pickerview.TimePopupWindow.OnTimeSelectListener;
import com.wicare.wistorm.ui.pickerview.TimePopupWindow.Type;
//import com.wicare.wistorm.ui.WBottomPopupWindow.OnItemClickListener;
//import com.wicare.wistorm.ui.WDateSelector;
//import com.wicare.wistorm.ui.WDateSelector.OnDateChangedListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Wu
 * 
 * 帐号信息以及修改
 */
public class WAccountActivity extends Activity implements OnUploadProcessListener {
	
	static final String TAG = "WAccountActivity";
	
	static final String accountUrl = WConfig.BaseUrl 
			+ "customer/" 
			+ WConfig.CustomerId + "?"
			+ "auth_code=" 
			+ WConfig.AuthCode;
	
	static final int UPDATA_ACCOUNT  = 1;
	static final int GET_ACCOUNT_MSG = 2;
	private TextView tv_phone, tv_name, tv_email, tv_sex, tv_birth;
	private ImageView iv_pic;//头像
	private RequestQueue mQueue;
	private String birth;//生日
	private TimePopupWindow pwDate;//日期选择器
	
	
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
		
		pwDate = new TimePopupWindow(WAccountActivity.this, Type.YEAR_MONTH_DAY);
		pwDate.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				// TODO Auto-generated method stub
				String url = WConfig.BaseUrl 
						+ "customer/" 
						+ WConfig.CustomerId 
						+ "/field?" 
						+ "auth_code=" + WConfig.AuthCode;
				String Date = getDate(date);
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("field_name", "birth"));
				params.add(new BasicNameValuePair("field_type", "Date"));
				params.add(new BasicNameValuePair("field_value", Date));
				new HttpThread.putDataThread(mHandler, url, params, UPDATA_ACCOUNT).start();
				tv_birth.setText(Date);
			}
		});
		
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
				Intent intent2 = new Intent(WAccountActivity.this, WNameChangeActivity.class);
				intent2.putExtra("name", name);
				startActivityForResult(intent2, 1);
		       
			}
			if(v.getId() == R.id.tv_sex){//修改性别
				AlertDialog.Builder builder = new AlertDialog.Builder(WAccountActivity.this);
                builder.setTitle(getResources().getString(R.string.sex_title));
                builder.setSingleChoiceItems(sexItems,0,new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                    	
                    	String url = WConfig.BaseUrl 
                    			+ "customer/" + WConfig.CustomerId  
                    			+ "/field?" 
                    			+ "auth_code=" + WConfig.AuthCode;
                    	
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
				startActivity(new Intent(WAccountActivity.this, WUpdatePwdActivity.class));
			}
			if(v.getId() == R.id.tv_phone){//修改手机号
				Intent intent = new Intent(WAccountActivity.this, WChangeEmailPhoneActivity.class);
				intent.putExtra("mark", 3);
				intent.putExtra("phone", tv_phone.getText().toString().trim());
				startActivityForResult(intent, 1);
			}
			if(v.getId() == R.id.tv_email){//修改邮箱
				Intent intent1 = new Intent(WAccountActivity.this, WChangeEmailPhoneActivity.class);
				intent1.putExtra("mark", 4);
				intent1.putExtra("email", tv_email.getText().toString().trim());
				startActivityForResult(intent1, 1);
			}
			if(v.getId() == R.id.iv_pic){//更改头像
				picPop();
			}
			if(v.getId() == R.id.tv_birth){//设置生日日期
				pwDate.showAtLocation(v, Gravity.BOTTOM, 0, 0,new Date());
//				WDateSelector dateSelector = new WDateSelector(WAccountActivity.this);
//				dateSelector.setDate();
//				dateSelector.setOnDateChangedListener(new OnDateChangedListener() {
//					
//					@Override
//					public void onDateChanged(String year, String month, String day) {
//						String url = WConfig.BaseUrl 
//								+ "customer/" 
//								+ WConfig.CustomerId 
//								+ "/field?" 
//								+ "auth_code=" + WConfig.AuthCode;
//						String Date = year + "-" + month + "-" + day;
//						List<NameValuePair> params = new ArrayList<NameValuePair>();
//						params.add(new BasicNameValuePair("field_name", "birth"));
//						params.add(new BasicNameValuePair("field_type", "Date"));
//						params.add(new BasicNameValuePair("field_value", Date));
//						new HttpThread.putDataThread(mHandler, url, params, UPDATA_ACCOUNT).start();
//						tv_birth.setText(Date);
//					}
//				});
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
            	updaAccount();//提交完重新更新ui
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
			String password = jsonObject.getString("password");
			
			SharedPreferences preferences=getSharedPreferences(WConfig.USER_DATA,Context.MODE_PRIVATE);
			Editor editor=preferences.edit();
			editor.putString(WConfig.PASSWD, password);
			editor.commit();
			
			
			Log.e(TAG, password);
			Log.e(TAG+"11111", SystemTools.getM5DEndo("123456"));
			
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
			Uri uri = data.getData();//选取图片的路径
			Log.e("uri", uri.toString());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				UpdateBitmap(bitmap);
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
			UpdateBitmap(bitmap);
		}else if(resultCode ==1){
			tv_name.setText(data.getStringExtra("name"));
		}
		
		
		
		
	}

	String fileName; //图片路径名字

	/**
	 * @param bitmap 
	 */
	private void UpdateBitmap(Bitmap bitmap) {
		File filePath = new File(WConfig.userIconPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		bitmap = WPicCompresses.scaleImage(bitmap, 200);
		bitmap = WPicCompresses.getSquareBitmap(bitmap);
		FileOutputStream b = null;
		fileName = WConfig.userIconPath + WConfig.CustomerId + ".png";
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		iv_pic.setImageBitmap(bitmap);

		String url = WConfig.BaseUrl 
				+ "upload_image?" 
				+ "auth_code="  + WConfig.AuthCode;
		
		WUploadUtil.getInstance().setOnUploadProcessListener(WAccountActivity.this);
		WUploadUtil.getInstance().uploadFile(fileName, "image", url, new HashMap<String, String>());
	}


	/**
	 * @param str
	 */
	private void jsonUpdatePic(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			if (jsonObject.getString("status_code").equals("0")) {
				String ImageUrl = jsonObject.getString("image_file_url");
				
				String url = WConfig.BaseUrl 
						+ "customer/" + WConfig.CustomerId 
						+ "/field?" 
						+ "auth_code=" + WConfig.AuthCode;
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("field_name", "logo"));
				params.add(new BasicNameValuePair("field_type", "String"));
				params.add(new BasicNameValuePair("field_value", ImageUrl));
				
				new HttpThread.putDataThread(mHandler, url, params, UPDATA_ACCOUNT).start();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUploadDone(int responseCode, String message) {
		// TODO Auto-generated method stub
		if (responseCode == 1) {
			jsonUpdatePic(message);
		} else if (responseCode == 2) {
			Toast.makeText(WAccountActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
		} else if (responseCode == 3) {
			Toast.makeText(WAccountActivity.this, "服务器接受失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onUploadProcess(int uploadSize) {
	}


	@Override
	public void initUpload(int fileSize) {
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}	

