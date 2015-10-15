package com.wicare.wistorm.ui;

import com.wisegps.wistorm.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 *  WDialog
 * 自定义Dialog（参数传入Dialog样式文件，Dialog布局文件） 
 * @author Wu
 * @version 1.0
 */

public class WDialog extends AlertDialog{
	
	final Context mContext;
	private int layoutRes;

	public WDialog(Context context) {
		super(context);
		this.mContext = context;
		this.layoutRes = R.layout.wise_alert_dialog;
	}
	
	/**
	 * @param context
	 * @param resLayout  自定义布局
	 */
	public WDialog (Context context,int resLayout){
		super(context,resLayout);
		this.mContext = context;
		this.layoutRes = resLayout;
	}
	
	/**
	 * @param context
	 * @param theme     自定义主题
	 * @param resLayout 自定义布局
	 */
	public WDialog (Context context, int theme,int resLayout){
		super(context,theme);
		this.mContext = context;
		this.layoutRes = resLayout;
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(layoutRes);	
		
	}
	
	
	public void setWDialogTitle(String title){
		setTitle(title);
	}
	
	public void setWDialogIcon(int iconRes){
		setIcon(iconRes);
	}

} 
