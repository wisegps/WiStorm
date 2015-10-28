package com.wicare.wistorm.toolkit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wicare.wistorm.R;

/**
 * WMap 百度地图activity 继承该类可以使直接使用定位，图标显示，划线等功能
 * 
 * @author c
 * @date 2015-10-9
 */
public class WMap extends Activity implements BDLocationListener,
		OnGetGeoCoderResultListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public LocationClient mLocationClient;// 定位服务客户端

	public GeoCoder geoCoder;
	private LBSYingyan yingyan;// 鹰眼服务

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.ws_map);
		// 地图页面
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 鹰眼服务
		yingyan = new LBSYingyan(this);
		initLocation();

	}

	/*---------------------------------------定位篇--------------------------------------------------*/

	/**
	 * 高精度定位 initLocation
	 */
	public void initLocation() {

		// 配置参数
		mLocationClient = new LocationClient(this); // 声明LocationClient类
		mLocationClient.registerLocationListener(this); // 注册监听函数，onReceiveLocation()
		LocationClientOption option = new LocationClientOption();

		LocationClientOption.LocationMode mode = LocationClientOption.LocationMode.Hight_Accuracy;
		option.setLocationMode(mode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);

		// 开启定位
		mLocationClient.start();

		// 关闭定位
		// mLocationClient.stop();
	}

	/**
	 * 定位信息返回
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {

		StringBuffer sb = new StringBuffer(256);
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());

		Log.i("WMap", "定位" + sb.toString());
		setMyLocation(location);

	}

	/**
	 * 
	 * setMyLocation 打开定位图层，设置定位图标
	 * 
	 * @param location
	 */
	public void setMyLocation(BDLocation location) {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 构造定位数据
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(location.getDirection() + 45)
				.latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		// 设置定位数据
		mBaiduMap.setMyLocationData(locData);
		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.ws_map_follow);
		MyLocationConfiguration config = new MyLocationConfiguration(
				LocationMode.NORMAL, true, mCurrentMarker);
		mBaiduMap.setMyLocationConfigeration(config);
		// 当不需要定位图层时关闭定位图层
		// mMapView.getMap().setMyLocationEnabled(false);

		// 设置地图中心坐标
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		animateMapCenter(latLng);
		reverseGeoCode(latLng);
		//getGeoCode("深圳", "西丽366大街50号");
	}

	/**
	 * 设置当前地图中心坐标，显示到视野中间
	 * 
	 * @param latLng
	 *            需要显示的坐标中心
	 */
	public void animateMapCenter(LatLng latLng) {

		MapStatus.Builder builer = new MapStatus.Builder();
		// 定义地图状态
		MapStatus mMapStatus = builer.target(latLng).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.animateMapStatus(mMapStatusUpdate);
	}

	/*-----------------------------------地理位置编码篇--------------------------------------------------*/
	/**
	 * initGeoCoder 初始化地理编码类
	 */
	private void initGeoCoder() {
		if (geoCoder != null) {
			return;
		}
		geoCoder = GeoCoder.newInstance();
		geoCoder.setOnGetGeoCodeResultListener(this);
	}

	/**
	 * reverseGeoCode 反向地理编码服务实现了将地球表面的地址坐标转换为标准地址的过程
	 *  
	 * 
	 * @param latLng
	 *            坐标
	 */
	public void reverseGeoCode(LatLng latLng) {
		initGeoCoder();
		ReverseGeoCodeOption option = new ReverseGeoCodeOption()
				.location(latLng);
		geoCoder.reverseGeoCode(option);
	}

	/**
	 * 
	 * getGeoCode 地理位置转成坐标
	 * 
	 * @param city
	 *            所在城市
	 * @param address
	 *            地址
	 */
	public void getGeoCode(String city, String address) {
		initGeoCoder();
		GeoCodeOption option = new GeoCodeOption().city(city).address(address);
		geoCoder.geocode(option);
	}

	/**
	 * 地址转坐标结果返回
	 */
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		LatLng latLng = result.getLocation();
		String address = result.getAddress();
		Log.i("WMap", "地址转坐标: " + address);
		Log.i("WMap", "地址转坐标: " + latLng.latitude + " " + latLng.longitude);
		
	}

	/**
	 * 
	 * 
	 * 坐标转地址编码结果返回
	 */
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		LatLng latLng = result.getLocation();
		String address = result.getAddress();
		Log.i("WMap", "坐标转地址: " + address);
		Log.i("WMap", "坐标转地址: " + latLng.latitude + " " + latLng.longitude);
	}

	/*-----------------------------------生命周期篇--------------------------------------------------*/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		// 开启定位
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

}
