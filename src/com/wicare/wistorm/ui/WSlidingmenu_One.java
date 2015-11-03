package com.wicare.wistorm.ui;


import com.wicare.wistorm.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WSlidingmenu_One extends Activity{
	
	final String TAG = "WSlidingmenu_OneActivity";
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(com.wicare.wistorm.R.layout.ws_slidingmenu_menufragment);
		tv = (TextView) findViewById(R.id.tv_fragment_name);
		tv.setText(TAG);
	}

}
