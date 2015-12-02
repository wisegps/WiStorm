package com.wicare.wistorm.toolkit;

import com.wicare.wistorm.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class WNameChangeActivity extends Activity {
	EditText et_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ws_name_change);
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
//		iv_back.setOnClickListener(onClickListener);
		Button bt_sure = (Button) findViewById(R.id.bt_sure);
//		bt_sure.setOnClickListener(onClickListener);
		String name = getIntent().getStringExtra("name");
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setText(name);
	}
}
