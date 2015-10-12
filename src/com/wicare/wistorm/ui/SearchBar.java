package com.wicare.wistorm.ui;

import com.wisegps.wistorm.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * SearchBar
 * @author c
 * @date 2015-10-9
 */
public class SearchBar extends Activity implements TextWatcher, OnItemClickListener{
	
	private static final String TAG = "SearchBar";
	
	private ListView lvSearch;//搜索列表
	private EditText etSearch = null;//搜索框
	private ImageView ivClearText = null;//搜索框清除按键
	private String[] SourceDateList;//字符数据组
	Handler mhandler = new Handler();
	ArrayAdapter<String> adapter;  
	private String[] newDateList;//字符数据组
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wise_activity_search_bar);
		init();
	}
	
	public void init(){
		/*
		 * 再初始化界面控件
		 */
//		newDateList = getResources().getStringArray(R.array.cityDate);
		SourceDateList = getResources().getStringArray(R.array.cityDate);
		lvSearch = (ListView) findViewById(R.id.lv_search);
		etSearch = (EditText) findViewById(R.id.et_search);
		
		// 设监听
		etSearch.addTextChangedListener(this);
		lvSearch.setOnItemClickListener(this);
		setImgClearTextOnClick();//搜索框 清除 图标监听
		
		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchBar.this.finish();//关闭当前活动
			}
		});
		
		
		setListViewAdapter();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		Log.i(TAG, "beforeTextChanged().......");
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.i(TAG, "onTextChanged().......");
	}

	@Override
	public void afterTextChanged(Editable s) {
		Log.i(TAG, "afterTextChanged().......");
		if(s.length() == 0){
			ivClearText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
		}else {
			ivClearText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
		}
//		mhandler.post(searchChanged);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		Toast.makeText(SearchBar.this,SourceDateList[position] , Toast.LENGTH_SHORT).show();
		
	}

	 /**
     * 设置ListView的Adapter
     */
    private void setListViewAdapter()
    {    
    	
//    	getmNewDates(SourceDateList, "a");//获取更新数据
    	
    	lvSearch = (ListView) findViewById(R.id.lv_search); 
        adapter = new ArrayAdapter<String>(SearchBar.this, android.R.layout.simple_list_item_1,newDateList);     	    
        lvSearch.setAdapter(adapter);
    }
    
//	/**
//	 * 获得根据搜索框的数据data来从元数据筛选，筛选出来的数据放入mDataSubs里
//	 * @param mDataSubs
//	 * @param data
//	 */
//	
//	private void getmNewDates(String [] mDataBase, String strKey)
//	{
//		for(int i = 0; i < mDataBase.length; i++){
//			if(mDataBase[i].contains(strKey)){
//				newDateList[i] = mDataBase[i];	
//				newDateList.
//			}
//		}
//	}
    
	/**
	 * 设置叉叉的点击事件，即清空 EditText 功能
	 */
    private void setImgClearTextOnClick(){
    	ivClearText = (ImageView) findViewById(R.id.iv_search_clear);
    	ivClearText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				etSearch.setText("");
			}
		});
    }
    
//    Runnable searchChanged = new Runnable() {
//		
// 		@Override
// 		public void run() {
// 			// TODO Auto-generated method stub
// 			String strKey = etSearch.getText().toString();
// 			getmNewDates(SourceDateList, strKey);//获取更新数据
// 			adapter.notifyDataSetChanged();//更新	
// 		}
// 	};
}
