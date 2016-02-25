package com.wicare.wistorm.toolkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wicare.wistorm.R;
import com.wicare.wistorm.http.HttpThread;
import com.wicare.wistorm.model.CarBrandData;
import com.wicare.wistorm.ui.WCarBrandAdapter;
import com.wicare.wistorm.ui.WCarSelectorSideBar;
import com.wicare.wistorm.ui.WInputField;
import com.wicare.wistorm.ui.WLoading;
import com.wicare.wistorm.ui.WPingYinUtil;
import com.wicare.wistorm.ui.WCarSelectorSideBar.OnTouchingLetterChangedListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @author Wu
 * 汽车品牌型号款式选择
 */
public class WCarBrandSelector extends Activity implements OnClickListener, TextWatcher {
	
	static final String TAG = "WBrandSelector";
	/** 图片路径存储地址 **/
	public static String BasePath = Environment.getExternalStorageDirectory().getPath() + "/wistorm/";
	/** 车品牌logo **/
	public static String VehicleLogoPath = BasePath + "vehicleLogo/";
	/** 图片地址 **/
	public static String ImageUrl = "http://img.wisegps.cn/logo/";
	static final int CAR_SELECTOR_RESULT_CODE = 1;
	static final String carBrand_url  = "http://api.bibibaba.cn/base/car_brand";
	static final String carSeries_url = "http://api.bibibaba.cn/base/car_series?pid=";
	static final String cartypes_url  = "http://api.bibibaba.cn/base/car_type?pid=";
	private static final int GET_CAR_BRAND_DATA  = 1;
	private static final int GET_CAR_Series_DATA = 2;
	private static final int GET_CAR_Types_DATA  = 3;
	private static final int GET_Logo_IMAGE      = 4;
	/** 品牌 **/
	private List<CarBrandData> brandDatas = new ArrayList<CarBrandData>(); // 车辆品牌集合
	/** 车型 **/
	private List<String[]> carSeries = new ArrayList<String[]>();
	/** 车款 **/
	private List<String[]> carTypes = new ArrayList<String[]>();
	private TextView tv_title = null;
	private TextView letterIndex = null;// 字母索引选中提示框
	private WCarSelectorSideBar sideBar;//侧旁的字母列表
	private WPingYinUtil characterParser; // 将汉字转成拼音
	private ListView carBrandListView;//汽车品牌列表
	private WInputField etSearchCarKey;//搜索框
	private WCarBrandAdapter mCarBrandAdapter;//汽车品牌列表适配器
	private WLoading mWLoading = null;
	private ListView carModelListView; //汽车型号列表
	private ListView carTypelListView; //汽车款式列表
	boolean currentSeriesState = false;//当前页面是选择车型？
	boolean currentTypesState  = false;//当前页面是选择车款？
	private boolean imageDownload = true;
	private Imagehread imageThread = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ws_brand_selector);
		initView();
//		startProgressDialog();//加载弹框
		
		Log.e(TAG, "---------"+VehicleLogoPath);
	}
	
	
	/**
	 * 初始化UI
	 */
	private void initView(){
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(this);
		
		carModelListView = (ListView) findViewById(R.id.lv_car_model);
		carModelListView.setOnItemClickListener(onSeriesClickListener);
		
		carTypelListView = (ListView) findViewById(R.id.lv_car_type);
		carTypelListView.setOnItemClickListener(onTypesClickListener);
		
		etSearchCarKey = (WInputField) findViewById(R.id.et_car_key);
		etSearchCarKey.addTextChangedListener(this);
		carBrandListView = (ListView) findViewById(R.id.lv_car_brand);
		carBrandListView.setOnItemClickListener(onBrankClickListener);
		characterParser = new WPingYinUtil();
		tv_title = (TextView) findViewById(R.id.tv_car_select_title);
		letterIndex = (TextView) findViewById(R.id.tv_car_alpha_overlay_bg);
		sideBar = (WCarSelectorSideBar) findViewById(R.id.sb_letter);
		sideBar.setTextView(letterIndex);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// TODO Auto-generated method stub
				try {
					int position = mCarBrandAdapter.getPositionForSection(s
							.charAt(0));
					if (position != -1) {
						carBrandListView.setSelection(position);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, carBrand_url, GET_CAR_BRAND_DATA).start();
	}
	
	
	String carBrankId;
	String carBrank;
	String logoUrl = "";
	
	/** 汽车品牌点击事件 **/
	OnItemClickListener onBrankClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {	
			carBrank   = brandDatas.get(position).getBrand();
			carBrankId = brandDatas.get(position).getId();
			logoUrl    = brandDatas.get(position).getLogoUrl();
			Log.e(TAG, carBrank + "--" + carBrankId + "--" + logoUrl);
			getSeriesData(carBrankId);
//			startProgressDialog();//弹框
		}
	};
	
	String carSeriesId;
	String carSerie;
	/** 汽车型号点击事件 **/
	OnItemClickListener onSeriesClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {	
			carSeriesId = carSeries.get(position)[0];
			carSerie    = carSeries.get(position)[1];
			Log.e(TAG, carSerie + "--" + carSeriesId);
			getTypeData(carSeriesId);
//			startProgressDialog();//弹框
		}
	};
	
	
	/** 汽车款式点击事件 **/
	OnItemClickListener onTypesClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {	
			//把数据返回
			Intent intent = new Intent();
			intent.putExtra("brank", carBrank);
			intent.putExtra("brankId", carBrankId);
			intent.putExtra("series", carSerie);
			intent.putExtra("seriesId", carSeriesId);
			intent.putExtra("typeId", carTypes.get(position)[0]);
			intent.putExtra("type", carTypes.get(position)[1]);
			intent.putExtra("logoUrl", logoUrl);
            WCarBrandSelector.this.setResult(CAR_SELECTOR_RESULT_CODE,intent);
            WCarBrandSelector.this.finish();
		}
	};
	

	/**
	 *  返回按钮设置
	 */
	@Override
	public void onClick(View v) {
		if(currentSeriesState){
			currentSeriesState = false;
			carBrandListView.setVisibility(View.VISIBLE);
			sideBar.setVisibility(View.VISIBLE);
			etSearchCarKey.setVisibility(View.VISIBLE);
			carModelListView.setVisibility(View.GONE);
			tv_title.setText(getResources().getString(R.string.car_brand_title));
		}else if(currentTypesState){
			currentSeriesState = true;
			currentTypesState  = false;
			carTypelListView.setVisibility(View.GONE);
			carModelListView.setVisibility(View.VISIBLE);
			tv_title.setText(getResources().getString(R.string.car_series_title));
		}else{
			WCarBrandSelector.this.finish();
		}
	}

	
	/**
     * Handler 处理消息
     */
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler() {
    	
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            
            case GET_CAR_BRAND_DATA:
                if(msg.obj.toString().indexOf("Exception") != -1){//有异常
                    Toast.makeText(WCarBrandSelector.this, "请求网络失败", Toast.LENGTH_SHORT).show(); 
                    return;
                }else{
                	jsonCarBrands(msg.obj.toString());//json 解析
                }
                break;
                
            case GET_CAR_Series_DATA:
            	if(msg.obj.toString().indexOf("Exception") != -1){//有异常
                    Toast.makeText(WCarBrandSelector.this, "请求网络失败", Toast.LENGTH_SHORT).show(); 
                    return;
                }else{
                	jsonSeries(msg.obj.toString());
                }
            	
            	break;
            	
            case GET_CAR_Types_DATA:
            	if(msg.obj.toString().indexOf("Exception") != -1){//有异常
                    Toast.makeText(WCarBrandSelector.this, "请求网络失败", Toast.LENGTH_SHORT).show(); 
                    return;
                }else{
                	jsonTypes(msg.obj.toString());
                }
            	break;
            	
            case GET_Logo_IMAGE:
            	mCarBrandAdapter.notifyDataSetChanged();
            	Log.e(TAG,"mCarBrandAdapter.notifyDataSetChanged();-------");
            	break;
            }
        }
    };	
	
	
	
    
    /**
     * @param jsonBrandData 返回的json格式数据解析
     */
    @SuppressWarnings("unchecked")
	private void jsonCarBrands(String jsonBrandData) {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(jsonBrandData);
			List<CarBrandData> brankList = null;
			int arrayLength = jsonArray.length();
			brankList = new ArrayList<CarBrandData>();
			for (int i = 0; i < arrayLength; i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				CarBrandData carBrand = new CarBrandData();
				carBrand.setBrand(jsonObj.getString("name"));
				carBrand.setId(jsonObj.getString("id"));
				if (jsonObj.opt("url_icon") != null) {
					carBrand.setLogoUrl(jsonObj.getString("url_icon"));
				} else {
					carBrand.setLogoUrl("");
				}
				brankList.add(carBrand);
			}
			brandDatas = filledData(brankList);
			Collections.sort(brandDatas, comparator);
			mCarBrandAdapter = new WCarBrandAdapter(this);
			mCarBrandAdapter.setData(brankList);
			carBrandListView.setAdapter(mCarBrandAdapter);
			// 刷新品牌logo
			imageThread = new Imagehread();
			imageThread.start();
//			stopProgressDialog();//关闭弹框
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
	}
	
    
    /**
     * @param result 车型json格式数据
     */
    private void jsonSeries(String jsonSeriesData) {
		carSeries.clear();
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(jsonSeriesData);
			int jsonLength = jsonArray.length();
			for (int i = 0; i < jsonLength; i++) {
				String[] series = new String[2];
				try {
					series[0] = jsonArray.getJSONObject(i).getString("id");
					series[1] = jsonArray.getJSONObject(i).getString("show_name");
					carSeries.add(series);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// 隐藏车牌列表 显示车型列表
			currentSeriesState = true;
			carBrandListView.setVisibility(View.GONE);
			sideBar.setVisibility(View.GONE);
			etSearchCarKey.setVisibility(View.GONE);
			tv_title.setText(getResources().getString(R.string.car_series_title));
			carSeriesAndTypeAdapter adapter = new carSeriesAndTypeAdapter(this, carSeries);
			carModelListView.setAdapter(adapter);
			carModelListView.setVisibility(View.VISIBLE);
//			stopProgressDialog();//关闭弹框
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}
    
    /**
     * @param carSeriesId 车型ID
     */
    private void jsonTypes(String carSeriesResult){
    	JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(carSeriesResult);
			int jsonTypeLength = jsonArray.length();
			for (int i = 0; i < jsonTypeLength; i++) {
				String[] typeStr = new String[2];
				try {
					typeStr[0] = jsonArray.getJSONObject(i).getString("id");
					typeStr[1] = jsonArray.getJSONObject(i) .getString("go_name")
							+ "  "
							+ jsonArray.getJSONObject(i).getString("name");
					carTypes.add(typeStr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			currentSeriesState = false;
			currentTypesState = true;
			carModelListView.setVisibility(View.GONE);
			tv_title.setText(getResources().getString(R.string.car_type_title));
			carSeriesAndTypeAdapter adapter = new carSeriesAndTypeAdapter(this, carTypes);
			carTypelListView.setAdapter(adapter);
			carTypelListView.setVisibility(View.VISIBLE);
//			stopProgressDialog();//关闭弹框
			if (jsonTypeLength == 0) {
				Intent intent = new Intent();
				intent.putExtra("brank", carBrank);
				intent.putExtra("brankId", carBrankId);
				intent.putExtra("series", carSerie);
				intent.putExtra("seriesId", carSeriesId);
				intent.putExtra("typeId", "");
				intent.putExtra("type", "");
				intent.putExtra("logo", logoUrl);
				WCarBrandSelector.this.setResult(CAR_SELECTOR_RESULT_CODE, intent);
				WCarBrandSelector.this.finish();
			}
			
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
    }
    
    /**
     * 获取车品牌
     * @param carBrandId 	汽车id
     */
    private void getSeriesData(String carBrandId){
		String url = carSeries_url + carBrankId;
		Log.e("获取车型", url);
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, url, GET_CAR_Series_DATA).start();
    }
    
    /**
	 * 获取车款
	 * @param carBrankId
	 */
	private void getTypeData(String carSeriesId) {
		String url = cartypes_url + carSeriesId;
		Log.e("获取车款", url);
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, url, GET_CAR_Types_DATA).start();
	}
    
	/**
	 * a-z排序
	 */
	@SuppressWarnings("rawtypes")
	Comparator comparator = new Comparator<CarBrandData>() {
		@Override
		public int compare(CarBrandData lhs, CarBrandData rhs) {
			String a = lhs.getLetter().substring(0, 1);
			String b = rhs.getLetter().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};
	
	
	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	@SuppressLint("DefaultLocale") private List<CarBrandData> filledData(List<CarBrandData> brankList) {
		for (int i = 0; i < brankList.size(); i++) {
			// 汉字转换成拼音
			@SuppressWarnings("static-access")
			String pinyin = characterParser.getPingYin(brankList.get(i)
					.getBrand());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				brankList.get(i).setLetter(sortString.toUpperCase());
			} else {
				brankList.get(i).setLetter("#");
			}
		}
		return brankList;
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	private void filterData(String filterStr) {
		try {
			List<CarBrandData> filterDateList = new ArrayList<CarBrandData>();
			// 编辑框的内容为空的时候
			if (TextUtils.isEmpty(filterStr)) {
				filterDateList = brandDatas;
			} else {
				// 匹配某些类型的品牌
				filterDateList.clear();
				for (CarBrandData sortModel : brandDatas) {
					String name = sortModel.getBrand();
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getPingYin(name).startsWith(
									filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}
			}
			// 根据a-z进行排序
			Collections.sort(filterDateList, comparator);
			mCarBrandAdapter.setData(filterDateList);
			carBrandListView.setAdapter(mCarBrandAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 开始显示加载
	 */
/*	private void startProgressDialog() {
		if (mWLoading == null) {
			mWLoading = WLoading.createDialog(this,WLoading.SMALL_TYPE);
			mWLoading.setMessage("数据加载中...");
		}
		mWLoading.show();
	}*/

	/**
	 * 关闭加载提示
	 */
/*	private void stopProgressDialog() {
		if (mWLoading != null) {
			mWLoading.dismiss();
			mWLoading = null;
		}
	}*/
	
	
	
	
	public void logoImageIsExist(final String imagePath, final String name,
			final String logoUrl) {
		// 去掉最后面的斜杠
		File filePath = new File(imagePath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		File imageFile = new File(imagePath + name + ".png");
		if (!imageFile.exists()) {
			Bitmap bitmap = getBitmapFromURL(ImageUrl + logoUrl);
			if (bitmap != null) {
				createImage(imagePath + name + ".png", bitmap);
			}
		}
	}

	/**
	 * @author Wu
	 * 开线程获取图片
	 */
	class Imagehread extends Thread {
		public void run() {
			for (int i = 0; i < brandDatas.size(); i++) {
				if (!"".equals(brandDatas.get(i).getLogoUrl())
						&& brandDatas.get(i).getLogoUrl() != null) {
					if (imageDownload) {
						logoImageIsExist(VehicleLogoPath, brandDatas.get(i).getId(), brandDatas.get(i).getLogoUrl());
					} else {
						continue;
					}
				}
			}
			super.run();
		}

		public void reStart() {
			run();
		}
	}

	// 向SD卡中添加图片
	public void createImage(String fileName, Bitmap bitmap) {
		FileOutputStream b = null;
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
			Message msg = new Message();
			msg.what = GET_Logo_IMAGE;
			mHandler.sendMessage(msg);
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
	}
	
	/**
	 * 从服务器读取图片
	 * 
	 * @param Path
	 * @return Bitmap
	 */
	public Bitmap getBitmapFromURL(String Path) {
		Log.e(TAG,"getBitmapFromURL(String Path);-------");
		try {
			URL url = new URL(Path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	protected void onPause() {
		imageDownload = false;
		super.onPause();
	}
    
	/**
	 * 搜索框输入监听
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
		filterData(s.toString());
	}
	
	@Override
	public void afterTextChanged(Editable s) {
	}
	
	
	/**
	 * @author Wu
	 * 车型和车款listview 适配器
	 */
	public class carSeriesAndTypeAdapter extends BaseAdapter{
		
		private Context mContext;
		private List<String[]> carSeriesList;
		
		public carSeriesAndTypeAdapter(Context context,List<String[]> seriesList){
			this.mContext = context;
			this.carSeriesList = seriesList;
		}

		@Override
		public int getCount() {
			return carSeriesList.size();
		}

		@Override
		public Object getItem(int position) {
			return carSeriesList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			viewHolder holder;
			if(convertView == null){
				view = LayoutInflater.from(mContext).inflate(R.layout.ws_car_series_and_type_item, null);
				holder = new viewHolder();
				holder.tv_car = (TextView) view.findViewById(R.id.tv_car_series_type);
				view.setTag(holder);
			}else {
				view = convertView;
				holder = (viewHolder)view.getTag();
			}
			
			holder.tv_car.setText(carSeriesList.get(position)[1]);
			return view;
		}

		class viewHolder{
			TextView tv_car;
		}
		
	}
     
}
