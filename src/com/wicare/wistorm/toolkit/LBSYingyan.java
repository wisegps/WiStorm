package com.wicare.wistorm.toolkit;

import android.content.Context;
import android.util.Log;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;

/**
 * LBSYingyan 鹰眼服务
 * 
 * @author c
 * @date 2015-10-27
 */
public class LBSYingyan {

	private Context context;
	private LBSTraceClient client;
	private Trace trace;

	// 鹰眼服务ID
	private long serviceId = 102838; // 开发者配置

	public LBSYingyan(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 启动鹰眼服务
	 * 
	 * @param entityName
	 *            entity标识
	 */
	public void start(String entityName) {
		// 实例化轨迹服务客户端
		client = new LBSTraceClient(context);

		// 轨迹服务类型（0 : 不上传位置数据，也不接收报警信息； 1 : 不上传位置数据，但接收报警信息；2 : 上传位置数据，且接收报警信息）
		int traceType = 2;
		// 实例化轨迹服务
		trace = new Trace(context, serviceId, entityName, traceType);
		// 实例化开启轨迹服务回调接口
		OnStartTraceListener startTraceListener = new OnStartTraceListener() {
			// 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
			@Override
			public void onTraceCallback(int arg0, String arg1) {
				Log.i("LBSYingyan", "开启轨迹服务回调接口");
			}

			// 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
			@Override
			public void onTracePushCallback(byte arg0, String arg1) {

				Log.i("LBSYingyan", "轨迹服务推送接口");
			}
		};

		// 位置采集周期
		int gatherInterval = 10;
		// 打包周期
		int packInterval = 60;
		// 设置位置采集和打包周期
		client.setInterval(gatherInterval, packInterval);

		// 开启轨迹服务
		client.startTrace(trace, startTraceListener);

	}

	/**
	 * 停止鹰眼服务
	 */
	public void stop() {
		// 实例化停止轨迹服务回调接口
		OnStopTraceListener stopTraceListener = new OnStopTraceListener() {
			// 轨迹服务停止成功
			@Override
			public void onStopTraceSuccess() {
				Log.i("LBSYingyan", "轨迹服务停止成功");
			}

			// 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
			@Override
			public void onStopTraceFailed(int arg0, String arg1) {
				Log.i("LBSYingyan", "轨迹服务停止失败");
			}
		};

		// 停止轨迹服务
		client.stopTrace(trace, stopTraceListener);
	}

	/**
	 * 轨迹实时查询
	 */
	public void query(String entityNames) {
		// 鹰眼服务ID
		// entity标识列表（多个entityName，以英文逗号"," 分割）
		// String entityNames = "mycar1,mycar2,mycar3";
		// 检索条件（格式为 : "key1=value1,key2=value2,....."）
		String columnKey = "car_team=1";
		// 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
		int returnType = 0;
		// 活跃时间，UNIX时间戳（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
		int activeTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
		// 分页大小
		int pageSize = 1000;
		// 分页索引
		int pageIndex = 1;
		// Entity监听器
		OnEntityListener entityListener = new OnEntityListener() {
			// 查询失败回调接口
			@Override
			public void onRequestFailedCallback(String arg0) {

				Log.i("LBSYingyan", "entity请求失败回调接口消息 : " + arg0);
			}

			// 查询entity回调接口，返回查询结果列表
			@Override
			public void onQueryEntityListCallback(String arg0) {
				Log.i("LBSYingyan", "entity回调接口消息 : " + arg0);
			}
		};

		// 查询实时轨迹
		client.queryEntityList(serviceId, entityNames, columnKey, returnType,
				activeTime, pageSize, pageIndex, entityListener);
	}

	/**
	 * 
	 * queryHistory 历史轨迹查询
	 * 
	 * @param entityName
	 *            entity标识
	 * @param pageIndex
	 *            分页索引
	 */

	public void queryHistory(String entityName, int pageIndex) {
		// 鹰眼服务ID
		// entity标识
		// String entityName = "mycar1";
		// 是否返回精简的结果（0 : 将只返回经纬度，1 : 将返回经纬度及其他属性信息）
		int simpleReturn = 0;
		// 开始时间（Unix时间戳）
		int startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
		// 结束时间（Unix时间戳）
		int endTime = (int) (System.currentTimeMillis() / 1000);
		// 分页大小
		int pageSize = 1000;
		// 分页索引
		// int pageIndex = 1;
		// 轨迹查询监听器
		OnTrackListener trackListener = new OnTrackListener() {
			// 请求失败回调接口
			@Override
			public void onRequestFailedCallback(String arg0) {
				Log.i("LBSYingyan", "track请求失败回调接口消息 : " + arg0);
			}

			// 查询历史轨迹回调接口
			@Override
			public void onQueryHistoryTrackCallback(String arg0) {
				Log.i("LBSYingyan", "查询历史轨迹回调接口消息 : " + arg0);
			}

		};

		// 查询历史轨迹
		client.queryHistoryTrack(serviceId, entityName, simpleReturn,
				startTime, endTime, pageSize, pageIndex, trackListener);
	}
}
