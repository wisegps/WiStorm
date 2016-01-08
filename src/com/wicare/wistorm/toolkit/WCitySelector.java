package com.wicare.wistorm.toolkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wicare.wistorm.R;
import com.wicare.wistorm.ui.WCity;
import com.wicare.wistorm.ui.WLetterSideBar;
import com.wicare.wistorm.ui.WPingYinUtil;
import com.wicare.wistorm.ui.WLetterSideBar.OnTouchingLetterChangedListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Wu
 * 
 * 城市选择Activity
 */
public class WCitySelector extends Activity implements OnScrollListener, TextWatcher, OnItemClickListener{
	
	private BaseAdapter adapter;
	private ResultListAdapter resultListAdapter;
	private ListView currentList;
	private ListView resultList;
	private TextView overlay; // 对话框首字母textview
	private ImageView ivBack;
	private WLetterSideBar letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread;   // 显示首字母对话框
	private ArrayList<WCity> allCity_lists; // 所有城市列表
	private ArrayList<WCity> city_lists;// 城市列表
	private ArrayList<WCity> city_hot;  // 热门城市
	private ArrayList<String> city_history; //历史城市
	
	private ArrayList<WCity> city_result;//搜索结果城市
	private EditText searchEdit; //搜索输入框
	private TextView noResult; //搜索不到结构显示的文字信息
	
	private String currentCity; // 用于保存定位到的城市
	private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
	private boolean isNeedFresh;
	
	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_activity_switch);
		handler = new Handler();
		alphaIndexer = new HashMap<String, Integer>();
		
		initView();
		cityInit();
		hotCityInit();
		initAlphaOverlay();
		setAdapter(allCity_lists, city_hot, city_history);
		
		isNeedFresh = true;
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
	}
	
	/**
	 * 定位初始化参数
	 */
	private void InitLocation() {
		// 设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000); // 10分钟扫描1次
		// 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
		option.setAddrType("all");
		// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setProdName("通过GPS定位我当前的位置");
		// 禁用启用缓存定位数据
		option.disableCache(true);
		// 设置定位方式的优先级。
		// 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
		option.setPriority(LocationClientOption.GpsFirst);
		mLocationClient.setLocOption(option);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getWindowManager().removeView(overlay);//移除字母弹框窗口不然会窗口泄露
	}
	
	/**
	 *  初始化界面的UI
	 */
	private void initView(){
		currentList = (ListView) findViewById(R.id.list_view);
		currentList.setOnItemClickListener(this);
		currentList.setAdapter(adapter);
		currentList.setOnScrollListener(this);
		noResult = (TextView) findViewById(R.id.tv_noresult);
		letterListView = (WLetterSideBar) findViewById(R.id.MyLetterListView01);
		letterListView
		.setOnTouchingLetterChangedListener(new LetterListViewListener());
		allCity_lists = new ArrayList<WCity>();
		city_hot = new ArrayList<WCity>();
		city_history = new ArrayList<String>();
		city_result = new ArrayList<WCity>();
		searchEdit = (EditText) findViewById(R.id.sh);
		searchEdit.addTextChangedListener(this);
		overlayThread = new OverlayThread();
		resultList = (ListView) findViewById(R.id.search_result);
		resultListAdapter = new ResultListAdapter(this, city_result);
		resultList.setAdapter(resultListAdapter);
		resultList.setOnItemClickListener(this);
		
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WCitySelector.this.finish();
			}
		});
	}

	/**
	 * 数据初始化
	 */
	private void cityInit() {
		WCity wCity = new WCity("定位", "0"); // 当前定位城市
		allCity_lists.add(wCity);
		wCity = new WCity("最近", "1"); // 最近访问的城市
		allCity_lists.add(wCity);
		wCity = new WCity("热门", "2"); // 热门城市
		allCity_lists.add(wCity);
		wCity = new WCity("全部", "3"); // 全部城市
		allCity_lists.add(wCity);
		city_lists = getCityList();
		allCity_lists.addAll(city_lists);
	}

	/**
	 * 热门城市
	 */
	private void hotCityInit() {
		WCity wCity = new WCity("上海", "2");
		city_hot.add(wCity);
		wCity = new WCity("北京", "2");
		city_hot.add(wCity);
		wCity = new WCity("广州", "2");
		city_hot.add(wCity);
		wCity = new WCity("深圳", "2");
		city_hot.add(wCity);
		wCity = new WCity("武汉", "2");
		city_hot.add(wCity);
		wCity = new WCity("天津", "2");
		city_hot.add(wCity);
		wCity = new WCity("西安", "2");
		city_hot.add(wCity);
		wCity = new WCity("南京", "2");
		city_hot.add(wCity);
		wCity = new WCity("杭州", "2");
		city_hot.add(wCity);
		wCity = new WCity("苏州", "2");
		city_hot.add(wCity);
		wCity = new WCity("成都", "2");
		city_hot.add(wCity);
		wCity = new WCity("重庆", "2");
		city_hot.add(wCity);
	}


	/**
	 * 查询全国城市数据
	 * @return list 返回查询的数据
	 */
    @SuppressWarnings("unchecked")
	private ArrayList<WCity> getCityList() {
    	
    	String[] date = getResources().getStringArray(
                R.array.all_citys);
    	String[] pinyin = getResources().getStringArray(
                R.array.city_pinyin);
    	WCity wCity;
    	ArrayList<WCity> list = new ArrayList<WCity>();
        for (int i = 0; i < date.length; i++) {
        	wCity = new WCity(date[i],pinyin[i]);
        	list.add(wCity);
        }
        Collections.sort(list, comparator);
        return list;
    }
	
    
	/**
	 * 实现实位回调监听
	 */
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			Log.e("info", "city = " + arg0.getCity());
			if (!isNeedFresh) {
				return;
			}
			isNeedFresh = false;
			if (arg0.getCity() == null) {
				locateProcess = 3; // 定位失败
				currentList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				return;
			}
			currentCity = arg0.getCity().substring(0,
					arg0.getCity().length() - 1);
			locateProcess = 2; // 定位成功
			currentList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}
	

	/**
	 * @param keyword 返回搜索的数据
	 */
	@SuppressLint("DefaultLocale") @SuppressWarnings("unchecked")
	private void getResultCityList(String keyword) {
		String[] date = getResources().getStringArray(
                R.array.all_citys);
    	String[] pinyin = getResources().getStringArray(
                R.array.city_pinyin);
    	WCity wCity;
        for (int i = 0; i < date.length; i++) {
        	if(pinyin[i].contains(keyword.toLowerCase())
        			|| date[i].contains(keyword)){
        		wCity = new WCity(date[i],pinyin[i]);
        		city_result.add(wCity);
        	}
        }
		Collections.sort(city_result, comparator);
	}
	
	
	/**
	 * a-z排序
	 */
	@SuppressWarnings("rawtypes")
	Comparator comparator = new Comparator<WCity>() {
		@Override
		public int compare(WCity lhs, WCity rhs) {
			String a = lhs.getPinyin().substring(0, 1);
			String b = rhs.getPinyin().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};

	private void setAdapter(List<WCity> list, List<WCity> hotList,
			List<String> hisCity) {
		adapter = new ListAdapter(this, list, hotList, hisCity);
		currentList.setAdapter(adapter);
	}


	/**
	 * @author Wu
	 * 
	 * 搜索的城市结果适配器
	 */
	private class ResultListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private ArrayList<WCity> results = new ArrayList<WCity>();

		public ResultListAdapter(Context context, ArrayList<WCity> results) {
			inflater = LayoutInflater.from(context);
			this.results = results;
		}

		@Override
		public int getCount() {
			return results.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ws_all_city_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText(results.get(position).getName());
			return convertView;
		}

		class ViewHolder {
			TextView name;
		}
	}

	/**
	 * @author Wu
	 * 
	 * 全部城市适配器
	 */
	public class ListAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<WCity> list;
		private List<WCity> hotList;
		private List<String> hisCity;
		final int VIEW_TYPE = 5;

		public ListAdapter(Context context, List<WCity> list,
				List<WCity> hotList, List<String> hisCity) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
			this.hotList = hotList;
			this.hisCity = hisCity;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getPinyin());
				// 上一个汉语拼音首字母，如果不存在为" "
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.getPinyin()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinyin());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}

		@Override
		public int getItemViewType(int position) {
			return position < 4 ? position : 4;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ViewHolder holder;

		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TextView city;
			int viewType = getItemViewType(position);
			if (viewType == 0) { // 定位当前城市
				convertView = inflater.inflate(R.layout.ws_gps_city_list_item, null);
				TextView locateHint = (TextView) convertView
						.findViewById(R.id.locateHint);
				city = (TextView) convertView.findViewById(R.id.gps_city);
				
				city.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (locateProcess == 2) {
							Intent intent = new Intent();
					        intent.putExtra("cityName", city.getText().toString());
					        WCitySelector.this.setResult(RESULT_OK,intent);
					        WCitySelector.this.finish();
							Toast.makeText(getApplicationContext(),
									city.getText().toString(),
									Toast.LENGTH_SHORT).show();
						} else if (locateProcess == 3) {
							locateProcess = 1;
							currentList.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							mLocationClient.stop();
							isNeedFresh = true;
							InitLocation();
							currentCity = "";
							mLocationClient.start();
						}
					}
				});
				ProgressBar pbLocate = (ProgressBar) convertView
						.findViewById(R.id.pbLocate);
				if (locateProcess == 1) { // 正在定位
					locateHint.setText(getResources().getString(R.string.gpsing));
					city.setVisibility(View.GONE);
					pbLocate.setVisibility(View.VISIBLE);
				} else if (locateProcess == 2) { // 定位成功
					locateHint.setText(getResources().getString(R.string.gps_city));
					city.setVisibility(View.VISIBLE);
					city.setText(currentCity);
					mLocationClient.stop();
					pbLocate.setVisibility(View.GONE);
				} else if (locateProcess == 3) {
					locateHint.setText(getResources().getString(R.string.no_search_city));
					city.setVisibility(View.VISIBLE);
					city.setText(getResources().getString(R.string.select_city_again));
					pbLocate.setVisibility(View.GONE);
				}
			} else if (viewType == 1) { // 最近访问城市
				convertView = inflater.inflate(R.layout.ws_hot_city_list_item, null);
				convertView.setVisibility(View.GONE);
				GridView rencentCity = (GridView) convertView
						.findViewById(R.id.recent_city);
				rencentCity.setVisibility(View.GONE);
				rencentCity
						.setAdapter(new HitCityAdapter(context, this.hisCity));
				rencentCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//把数据返回
						Intent intent = new Intent();
			            intent.putExtra("cityName", city_history.get(position));
			            WCitySelector.this.setResult(RESULT_OK,intent);
			            WCitySelector.this.finish();
						Toast.makeText(getApplicationContext(),
								city_history.get(position), Toast.LENGTH_SHORT)
								.show();
					}
				});
				TextView recentHint = (TextView) convertView
						.findViewById(R.id.recentHint);
				recentHint.setVisibility(View.GONE);
				
			} else if (viewType == 2) {//热门城市
				convertView = inflater.inflate(R.layout.ws_hot_city_list_item, null);
				GridView hotCity = (GridView) convertView
						.findViewById(R.id.recent_city);
				hotCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Intent intent = new Intent();
			            intent.putExtra("cityName", city_hot.get(position).getName());
			            WCitySelector.this.setResult(RESULT_OK,intent);
			            WCitySelector.this.finish();
	
						Toast.makeText(getApplicationContext(),
								city_hot.get(position).getName(),
								Toast.LENGTH_SHORT).show();
					}
				});
				hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
				TextView hotHint = (TextView) convertView
						.findViewById(R.id.recentHint);
				hotHint.setText("热门城市");
			} else if (viewType == 3) {
				convertView = inflater.inflate(R.layout.ws_all_city_title, null);
			} else {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.ws_all_city_list_item, null);
					holder = new ViewHolder();
					holder.alpha = (TextView) convertView
							.findViewById(R.id.alpha);
					holder.name = (TextView) convertView
							.findViewById(R.id.name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (position >= 1) {
					holder.name.setText(list.get(position).getName());
					String currentStr = getAlpha(list.get(position).getPinyin());
					String previewStr = (position - 1) >= 0 ? getAlpha(list
							.get(position - 1).getPinyin()) : " ";
					if (!previewStr.equals(currentStr)) {
						holder.alpha.setVisibility(View.VISIBLE);
						holder.alpha.setText(currentStr);
					} else {
						holder.alpha.setVisibility(View.GONE);
					}
				}
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // 首字母标题
			TextView name; // 城市名字	
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	
	/**
	 * @author Wu
	 * 
	 * 热门城市适配器
	 */
	private class HotCityAdapter extends BaseAdapter {
		
		private Context context;
		private LayoutInflater inflater;
		private List<WCity> hotCitys;

		public HotCityAdapter(Context context, List<WCity> hotCitys) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.hotCitys = hotCitys;
		}

		@Override
		public int getCount() {
			return hotCitys.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.ws_city_item, null);
			TextView city = (TextView) convertView.findViewById(R.id.city);
			city.setText(hotCitys.get(position).getName());
			return convertView;
		}
	}

	class HitCityAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<String> hotCitys;

		public HitCityAdapter(Context context, List<String> hotCitys) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.hotCitys = hotCitys;
		}

		@Override
		public int getCount() {
			return hotCitys.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.ws_city_item, null);
			TextView city = (TextView) convertView.findViewById(R.id.city);
			city.setText(hotCitys.get(position));
			return convertView;
		}
	}

	private boolean mReady;

	/**
	 *  初始化汉语拼音首字母弹出提示框
	 *  
	 */	
	@SuppressLint("InflateParams") private void initAlphaOverlay() {
		mReady = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.ws_alpha_overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private boolean isScroll = false;

	/**
	 * @author Wu
	 * 字母适配器
	 */
	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			isScroll = false;
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				currentList.setSelection(position);
				overlay.setText(s);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1000);
			}
		}
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	// 获得汉语拼音首字母
	@SuppressLint("DefaultLocale") private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else if (str.equals("0")) {
			return "定位";
		} else if (str.equals("1")) {
			return "最近";
		} else if (str.equals("2")) {
			return "热门";
		} else if (str.equals("3")) {
			return "全部";
		} else {
			return "#";
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL
				|| scrollState == SCROLL_STATE_FLING) {
			isScroll = true;
		}
	}

	@SuppressLint("DefaultLocale") @Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (!isScroll) {
			return;
		}

		if (mReady) {
			String text;
			String name = allCity_lists.get(firstVisibleItem).getName();
			String pinyin = allCity_lists.get(firstVisibleItem).getPinyin();
			if (firstVisibleItem < 4) {
				text = name;
			} else {
				text = WPingYinUtil.converterToFirstSpell(pinyin)
						.substring(0, 1).toUpperCase();
			}
			overlay.setText(text);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			// 延迟一秒后执行，让overlay为不可见
			handler.postDelayed(overlayThread, 1000);
		}
	}

	
	/** 
	 * 搜索框变化监听：搜索框（编辑框）变化时候实现的逻辑代码
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		/**搜索到结果*/
		if (s.toString() == null || "".equals(s.toString())) {
			letterListView.setVisibility(View.VISIBLE);
			currentList.setVisibility(View.VISIBLE);
			resultList.setVisibility(View.GONE);
			noResult.setVisibility(View.GONE);
		} else {/**搜索不到结果*/
			city_result.clear();
			letterListView.setVisibility(View.GONE);
			currentList.setVisibility(View.GONE);
			getResultCityList(s.toString());
			if (city_result.size() <= 0) {
				noResult.setVisibility(View.VISIBLE);
				resultList.setVisibility(View.GONE);
			} else {
				noResult.setVisibility(View.GONE);
				resultList.setVisibility(View.VISIBLE);
				resultListAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	/** 
	 * 点击事件：点击城市后返回当前点击的城市名称
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub


		if(parent.getId() == R.id.list_view){
			if (position >= 4) {
				
				Intent intent = new Intent();
		        intent.putExtra("cityName", allCity_lists.get(position).getName());
		        WCitySelector.this.setResult(RESULT_OK,intent);
		        WCitySelector.this.finish();	
				Toast.makeText(getApplicationContext(),
						allCity_lists.get(position).getName(),
						Toast.LENGTH_SHORT).show();
			}
		}
				
		if(parent.getId() == R.id.search_result){
			Intent intent = new Intent();
	        intent.putExtra("cityName", city_result.get(position).getName());
	        WCitySelector.this.setResult(RESULT_OK,intent);
	        WCitySelector.this.finish();

			Toast.makeText(getApplicationContext(),
					city_result.get(position).getName(), Toast.LENGTH_SHORT)
					.show();
	
			}
		
	}
	
}
