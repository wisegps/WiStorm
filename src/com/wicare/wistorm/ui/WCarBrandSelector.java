package com.wicare.wistorm.ui;

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
import com.wicare.wistorm.ui.WCarSelectorSideBar.OnTouchingLetterChangedListener; 

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	static final String carBrand_url  = "http://api.bibibaba.cn/base/car_brand";
	
	static final String carSeries_url = "http://api.bibibaba.cn/base/car_series?pid=";
	
	private static final int GET_CAR_BRAND_DATA  = 1;
	private static final int GET_CAR_Series_DATA = 2;
	
	/** 品牌 **/
	private List<CarBrandData> brandDatas = new ArrayList<CarBrandData>(); // 车辆品牌集合
	/** 车型 **/
	private List<String[]> carSeries = new ArrayList<String[]>();
	/** 车款 **/
	private List<String[]> carTypes = new ArrayList<String[]>();
	
	private TextView letterIndex = null; // 字母索引选中提示框
	private WCarSelectorSideBar sideBar;//侧旁的字母列表
	private WPingYinUtil characterParser; // 将汉字转成拼音
	private ListView carBrandListView;//汽车品牌列表
	private WInputField etSearchCarKey;//搜索框
	private WCarBrandAdapter mCarBrandAdapter;//汽车品牌列表适配器
	
	private ListView carModelListView; //汽车型号列表
	
	boolean isNeedType = true;
	boolean isNeedModel = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ws_brand_selector);
		initView();
	}
	
	
	/**
	 * 初始化UI
	 */
	private void initView(){
		ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(this);
		
		carModelListView = (ListView) findViewById(R.id.lv_car_model);
		
		
		etSearchCarKey = (WInputField) findViewById(R.id.et_car_key);
		etSearchCarKey.addTextChangedListener(this);
		carBrandListView = (ListView) findViewById(R.id.lv_car_brand);
		carBrandListView.setOnItemClickListener(onBrankClickListener);
		characterParser = new WPingYinUtil();
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
			
			if (isNeedModel) {
				// 点击品牌列表 选择车型
				getSeriesData(carBrankId);
			} else {
			
				//返回
				
			}
			
		}
	};
	
	
	
	
	

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.iv_back){
			WCarBrandSelector.this.finish();
		}
		
	}

	
	
	
	
	
	
	
	/**
     * Handler 处理消息
     */
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
			Log.e(TAG, brankList.size() + "");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
	}
	
    
    /**
     * @param result 车型json格式数据
     */
    private void jsonSeries(String result) {
		carSeries.clear();
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(result);
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
			carBrandListView.setVisibility(View.GONE);
//			ArrayAdapter<String> adapterModel = new ArrayAdapter<String>(context, resource);
			
			carModelListView.setVisibility(View.VISIBLE);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}
    
    /**
     * @param carBrandId 	汽车id
     */
    private void getSeriesData(String carBrandId){
		String url = carSeries_url + carBrankId;
		Log.e("获取车型", url);
		//开启线程获取服务器数据
		new HttpThread.getDataThread(mHandler, url, GET_CAR_Series_DATA).start();
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
	private List<CarBrandData> filledData(List<CarBrandData> brankList) {
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
     
}
