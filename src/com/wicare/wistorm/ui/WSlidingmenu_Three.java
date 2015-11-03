package com.wicare.wistorm.ui;

import com.wicare.wistorm.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WSlidingmenu_Three extends Activity{
	
	final String TAG = "WSlidingmenu_ThreeActivity";
	
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ws_slidingmenu_menufragment);
		tv = (TextView) findViewById(R.id.tv_fragment_name);
		tv.setText(TAG);
	}

}
