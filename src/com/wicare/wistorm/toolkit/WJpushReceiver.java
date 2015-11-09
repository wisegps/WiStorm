package com.wicare.wistorm.toolkit;

import org.json.JSONObject;

import com.wicare.wistorm.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class WJpushReceiver extends BroadcastReceiver {
	private static final String TAG = "WJpushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

			/**
			 * 需要自己修改该方法内容
			 */
			// process(context, intent);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "用户点击打开了通知");

			// 打开自定义的Activity

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	/**
	 * process 仅供参考 在实际项目中返回的通知，不在通知栏显示，需要构造一个在通知栏显示
	 * 
	 * @param bundle
	 */
	public void process(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		// 接收到推送下来的通知
		int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

		// 接收到推送下来的通知的ID: " + notifactionId
		String result = intent.getExtras()
				.getString(JPushInterface.EXTRA_EXTRA);

		String msg = "";
		String msg_name = "通知";
		try {
			JSONObject jsonObject = new JSONObject(result);
			// 接收到推送下来的通知的消息内容
			msg = jsonObject.getString("msg");

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "[MyReceiver] 非聊天界面");

		/*
		 * 在通知栏显示通知
		 */
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		notification.tickerText = msg_name;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;

		/*
		 * 点击该通知后要跳转的Activity 这里为空，需要定义Intent
		 */
		Intent notificationIntent = null;

		PendingIntent contentItent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, msg_name, msg, contentItent);
		nm.notify(19172449, notification);

	}
}
