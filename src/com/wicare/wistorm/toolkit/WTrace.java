package com.wicare.wistorm.toolkit;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.baidu.mapapi.model.LatLng;

/**
 * WTrack轨迹播放服务
 * 
 * @author c
 * @date 2015-10-28
 */
public class WTrace extends WMap {
	private LatLng startPoint = null; // 轨迹起点
	private List<LatLng> entityList = new ArrayList<LatLng>();// 轨迹点集合
	private HandlerThread trackThread; // 轨迹播放线程
	private Handler trackHandler; // 轨迹播放handler
	private int index = 0;// //当前播放轨迹点在集合中的序列
	private int IntervalTime = 1000;// 轨迹逐步回放间隔时间,控制轨迹播放速率
	public PlayStatus status = PlayStatus.Default;

	/**
	 * initTrace 初始化轨迹服务，开启轨迹线程
	 * 
	 * @param trackListener轨迹播放回调接口
	 */
	public void initTrace() {
		// 控制轨迹绘制的线程，非UI线程
		trackThread = new HandlerThread("WTrace");
		trackThread.start();
		Looper looper = trackThread.getLooper();
		trackHandler = new Handler(looper);
		track();
	}

	/**
	 * 
	 * track 画轨迹
	 * 
	 * @param endPoint
	 *            下一个坐标点
	 */
	private void track() {

		/*
		 * 每间隔IntervalTime执行一次轨迹播放
		 */
		trackHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				track();
			}
		}, IntervalTime);

		// 暂停播放
		if (status != PlayStatus.Play) {
			return;
		}

		int size = entityList.size();

		// 轨迹点为空了
		if (size <= 0) {
			return;
		}
		// 越界了，不存在该位置数据
		if (index >= size) {
			return;
		}

		final LatLng latLng = entityList.get(index++);

		// 在UI主线程画轨迹线
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				track(latLng);
				// 轨迹播放接口
				onTrackListener(index);
			}
		});

	}

	/**
	 * 
	 * track 画轨迹
	 * 
	 * @param endPoint
	 *            下一个坐标点
	 */
	private void track(LatLng endPoint) {
		if (startPoint == null) {
			startPoint = endPoint;
		}
		// 调用父类的划线方法
		addLineOverlay(startPoint, endPoint);
		startPoint = endPoint;
	}

	/**
	 * 
	 * startTrack 添加轨迹点集合,并且开始播放
	 * 
	 * @param entityList
	 */
	public void startTrack(List<LatLng> entityList) {
		this.entityList.clear();
		index = 0;
		this.entityList = entityList;
		status = PlayStatus.Play;
	}

	/**
	 * 暂停轨迹播放 pauseTrack
	 */
	public void pauseTrack() {
		status = PlayStatus.Pause;
	}

	/**
	 * 播放轨迹 startTrack
	 */
	public void startTrack() {
		status = PlayStatus.Play;
	}

	/**
	 * 停止轨迹播放 stopTrack
	 */
	public void stopTrack() {
		status = PlayStatus.Stop;
	}

	/**
	 * 
	 * onTrackListener 轨迹播放监听接口
	 * 
	 * @author c
	 * @date 2015-10-30
	 */
	public void onTrackListener(int index) {

	}

	/**
	 * 播放状态
	 * 
	 * @author c
	 * 
	 */
	public enum PlayStatus {
		Default, FirstLoad, // 第一次加载
		Play, // 播放
		Pause, // 暂停
		Finish, // 播放完成
		Stop, // 停止
		RePlay// 重新播放
	}

}
