package com.wicare.wistorm.toolkit;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.wicare.wistorm.R;

/**
 * WMapBase 百度地图activity 继承该类可以使直接使用定位，图标显示，划线等功能
 * 
 * @author c
 * @date 2015-10-29
 */
public class WMap extends Activity implements BDLocationListener,
		OnGetGeoCoderResultListener, OnGetPoiSearchResultListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public LocationClient mLocationClient;// 定位服务客户端
	public GeoCoder geoCoder;
	public PoiSearch mPoiSearch;
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

		// 测试设置地图中心坐标
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());

		/*---------------测试内容----------------*/
		// 测试滚到地图中心
		animateMapCenter(latLng);
		// 测试地理反位置编码
		reverseGeoCode(latLng);
		// 测试地理位置编码
		getGeoCode("深圳", "西丽崇文花园");
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

	/*-----------------------------------覆盖物标注篇--------------------------------------------------*/
	/**
	 * addOverlay 在地图指定的位置上添加标注信息
	 * 
	 * @param point
	 *            坐标点
	 * @return 标注
	 */
	public Marker addOverlay(LatLng point) {
		// 默认Marker图标id
		int drawableId = R.drawable.ws_park;
		return addOverlay(point, drawableId, null);
	}

	/**
	 * 
	 * addOverlay在地图指定的位置上添加标注信息
	 * 
	 * @param point
	 *            坐标点
	 * @param drawableId
	 *            图片id
	 * @param bundle
	 *            附加信息
	 * @return 标注
	 */

	public Marker addOverlay(LatLng point, int drawableId, Bundle bundle) {
		// 构建Marker图标
		BitmapDescriptor bmDesc = BitmapDescriptorFactory
				.fromResource(drawableId);

		return addOverlay(point, bmDesc, bundle);
	}

	/**
	 * 
	 * addOverlay在地图指定的位置上添加标注信息
	 * 
	 * @param point坐标点
	 * @param markerBitmap
	 * @param bundle
	 *            附加信息
	 * @return 标注
	 */

	public Marker addOverlay(LatLng point, Bitmap markerBitmap, Bundle bundle) {
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromBitmap(markerBitmap);
		return addOverlay(point, bitmap, bundle);
	}

	/**
	 * 
	 * addOverlay在地图指定的位置上添加标注信息
	 * 
	 * @param point坐标点
	 * @param bmDesc
	 * @param bundle
	 *            附加信息
	 * @return 标注
	 */

	private Marker addOverlay(LatLng point, BitmapDescriptor bmDesc,
			Bundle bundle) {
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.extraInfo(bundle).icon(bmDesc).anchor(0.5f, 0.5f);
		// 在地图上添加Marker，并显示
		Marker marker = (Marker) mBaiduMap.addOverlay(option);
		return marker;
	}

	/**
	 * 
	 * addLine 在地图上画线
	 * 
	 * @param startPoint
	 *            起点坐标
	 * @param endPoint
	 *            终点坐标
	 * @return
	 */
	public Overlay addLineOverlay(LatLng startPoint, LatLng endPoint) {

		// 测试
		// startPoint = new LatLng(23,113);
		// endPoint = new LatLng(25,116);

		if (startPoint == null) {
			return null;
		}
		if (endPoint == null) {
			return null;
		}

		// 如果划线超出屏幕，就移动到屏幕中间区域显示
		Point screenPoint = mBaiduMap.getProjection()
				.toScreenLocation(endPoint);
		if (screenPoint.x < 0 || screenPoint.x > mMapView.getWidth()
				|| screenPoint.y < 0 || screenPoint.y > mMapView.getHeight()) {
			animateMapCenter(endPoint);
		}

		// 根据 起点， 终点划线
		List<LatLng> points = new ArrayList<LatLng>();
		points.add(startPoint);// 点元素
		points.add(endPoint);// 点元素
		OverlayOptions polyline = new PolylineOptions().color(Color.GREEN)
				.points(points);
		Overlay lineOverlay = mBaiduMap.addOverlay(polyline);
		return lineOverlay;

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
		addOverlay(latLng);
		// addLineOverlay(null, null);
		poiSearch("深圳", "美食");
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

	/*-----------------------------------POI检索篇--------------------------------------------------*/

	/**
	 * 
	 * initPoiSearch 初始化poi
	 */
	private void initPoiSearch() {
		if (mPoiSearch != null) {
			return;
		}
		// 创建POI检索实例
		mPoiSearch = PoiSearch.newInstance();
		// 设置POI检索监听者；
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		// mPoiSearch.destroy();
	}

	/**
	 * 
	 * poiSearch 城市兴趣点检索
	 * 
	 * @param city
	 * @param keyword
	 */
	public void poiSearch(String city, String keyword) {
		initPoiSearch();
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city)
				.keyword(keyword).pageNum(10));

	}

	/**
	 * 
	 * poiSearchNearBy周边兴趣点检索
	 * 
	 * @param location
	 *            位置
	 * @param radius
	 *            半径
	 * @param keyword
	 *            关键字
	 */
	public void poiSearchNearBy(LatLng location, int radius, String keyword) {
		initPoiSearch();
		mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(keyword)
				.location(location).radius(radius));
	}

	/**
	 * 获取Place详情页检索结果
	 */
	@Override
	public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
		Log.i("WMap", "获取Place详情页检索结果: " + poiDetailResult.getAddress());

	}

	/**
	 * 获取poi检索结果
	 */
	@Override
	public void onGetPoiResult(PoiResult poiResult) {
		Log.i("WMap", "获取poi检索结果: " + poiResult.getAllPoi().get(0).name);
	}

	/*-----------------------------------生命周期篇--------------------------------------------------*/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		if (mPoiSearch != null) {
			mPoiSearch.destroy();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

}
