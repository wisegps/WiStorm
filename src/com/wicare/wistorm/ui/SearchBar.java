package com.wicare.wistorm.ui;

import java.util.ArrayList;

import com.wisegps.wistorm.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * SearchBar
 * @author Wu
 * @date 2015-10-12
 */
public class SearchBar extends Activity implements TextWatcher, OnItemClickListener{
	
	private static final String TAG = "SearchBar";
	
	private ListView lvSearch;//搜索列表
	private EditText etSearch = null;//搜索框
	private ImageView ivClearText = null;//搜索框清除按键
	private listSearchBarAdapter searchBarAdapter;//listview 适配器
	Handler mhandler = new Handler();
	ArrayList<String> newListData = new ArrayList<String>();//筛选出来的数据
	 	
    Runnable searchChanged = new Runnable() {	
 		@Override
 		public void run() {
 			String strKey = etSearch.getText().toString();
			newListData.clear();
			getmNewDates(strKey);//获取更新数据
 			Log.i(TAG, ""+"notifyDataSetChanged");	
 			searchBarAdapter.setData(newListData);
 			searchBarAdapter.notifyDataSetChanged();//更新	
 			if(newListData.isEmpty()){
 				Toast.makeText(SearchBar.this, "没有搜索到内容", Toast.LENGTH_SHORT).show();
 			}
 		}
 	};
	
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
		Log.i(TAG, "init().......");
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
	
		searchBarAdapter = new listSearchBarAdapter(this);
		searchBarAdapter.setData(newListData);
		lvSearch.setAdapter(searchBarAdapter);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		Log.i(TAG, "beforeTextChanged().......");
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.i(TAG, "onTextChanged().......");
		if(s.length() == 0){
			ivClearText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
			newListData.clear();
			searchBarAdapter.setData(newListData);
			searchBarAdapter.notifyDataSetChanged();//更新
		}else {
			ivClearText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
			mhandler.post(searchChanged);
		}
	}

	/** *(non-Javadoc)
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 *  搜索框EditText 变化后 回调
	 */
	@Override
	public void afterTextChanged(Editable s) {
		Log.i(TAG, "afterTextChanged().......");	
	}

	/**
	 * 获得根据搜索框的数据data来从元数据筛选，筛选出来的数据放入mDataSubs里
	 * @param mDataSubs
	 * @param data
	 */
	private void getmNewDates(String strKey){
		for(int i = 0; i < getData().size(); i++){
			if(getData().get(i).contains(strKey)){
				newListData.add(getData().get(i));
				Log.i(TAG ,"getmNewDates" + getData().get(i));
			}    	
		}		
	}
    
	/**
	 * 设置叉叉的点击事件，即清空 EditText 功能
	 */
    private void setImgClearTextOnClick(){
    	ivClearText = (ImageView) findViewById(R.id.iv_search_clear);
    	ivClearText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i(TAG, "setImgClearTextOnClick().......");
				etSearch.setText("");
			}
		});
    }
 	
	/**
	 * @return arraylist
	 * 把数组的字符存到arraylist
	 */
	private ArrayList<String> getData() {
		String[] SourceDateList = getResources().getStringArray(R.array.English);
		ArrayList<String> arraylist = new ArrayList<String>();   
		//根据需求添加一些数据,     
		for (int i = 0; i <SourceDateList.length; i++) {            
		arraylist.add(SourceDateList[i]);   
		}  
		return arraylist;   
	}  
 	
 	/**
 	 * @author Wu
 	 * 搜索列表适配器
 	 */
 	public class listSearchBarAdapter extends BaseAdapter{
 		
		private ArrayList<String> historyListData;
		private ArrayList<String> searchListData;
		private Context mContext;
		private boolean isHistory; 

		public listSearchBarAdapter(Context context){
			this.mContext = context;
			Log.i(TAG, "listSearchBarAdapter()...init....");
		}
		
		@Override
		public int getCount() {
			if(isHistory)
				return historyListData.size();
			else 
				return searchListData.size();
		}
		
		@Override
		public Object getItem(int position) {
			if(isHistory)
				return historyListData.get(position);
			else 
				return searchListData.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@SuppressLint("InflateParams") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view;  
			ViewHolder viewHolder;  
			if (convertView == null) {  
			    view = LayoutInflater.from(mContext).inflate(R.layout.wise_itemlistw_search_bar, null);  
			    viewHolder = new ViewHolder();  
			    if(isHistory){
			    	
			    }else{
			    	viewHolder.img_icon = (ImageView) view.findViewById(R.id.iv_serach_bar_icon);  
				    viewHolder.img_up   = (ImageView) view.findViewById(R.id.iv_serach_bar_up) ;
				    viewHolder.tv_info  = (TextView)  view.findViewById(R.id.tv_search_bar_info); 
				    viewHolder.tv_clear = (TextView)  view.findViewById(R.id.tv_clear_history);
				    viewHolder.tv_clear.setVisibility(View.GONE);
				    view.setTag(viewHolder);//将viewholder存储在view中  
			    }  
			}else{  
			    view = convertView;  
			    viewHolder = (ViewHolder)view.getTag();//重新获取viewholder       
			}  
			if(isHistory){
				
			}else{
				viewHolder.img_icon.setImageResource(R.drawable.wise_common_icon_searchbox_magnifier_2);  
			    viewHolder.img_up  .setImageResource(R.drawable.wise_icon_search_up_retrieval);
			    viewHolder.tv_info .setText(searchListData.get(position)); 
			}   		    
			return view;  
		}
		
		/**
		 * @param historyListData
		 */
		public void setHistoryData(ArrayList<String> historyListData) {
			this.isHistory = true;
			this.historyListData = historyListData;
		}
		
		/**
		 * @param searchListData
		 */
		public void setData(ArrayList<String> searchListData) {
			this.isHistory = false;
			this.searchListData = searchListData;
		}	
 	}
 	
 	/**
 	 * ui for  adapter
 	 *
 	 */
 	class ViewHolder {  
 	    ImageView img_icon; 
 	    ImageView img_up;
 	    TextView  tv_info;  
 	    TextView  tv_clear;
 	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(SearchBar.this, newListData.get(position), Toast.LENGTH_SHORT).show();
	}  		
}
