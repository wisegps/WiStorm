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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WSearchBarActivity extends Activity implements TextWatcher, OnItemClickListener, OnClickListener{
	
	
	private static final String TAG = "WSearchBarActivity";
	/**
	 * 搜索列表
	 */
	private ListView lvSearch;
	/**
	 * 自定义的搜索框
	 */
	private WClearEditText etSearch = null;
	/**
	 * 返回
	 */
	private ImageView ivBack = null;
	
	Handler mHandler = new Handler();
	/**
	 * listview 适配器
	 */
	private listSearchBarAdapter searchBarAdapter;
	/**
	 * 筛选出来的数据
	 */	
	ArrayList<String> newListData = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ws_activity_search_bar);
		ivBack   = (ImageView) findViewById(R.id.iv_back);
		ivBack .setOnClickListener(this);
		lvSearch = (ListView) findViewById(R.id.lv_search);
		etSearch = (WClearEditText) findViewById(R.id.et_search);
		initWSearchBar();
	}	
	
	/**
	 * 初始化WSearchBar
	 */
	private void initWSearchBar(){
		Log.i(TAG, "initWSearchBar()...");
		// 设监听
		etSearch.addTextChangedListener(this);
		lvSearch.setOnItemClickListener(this);
		//加载listview	
		searchBarAdapter = new listSearchBarAdapter(this);
		searchBarAdapter.setData(newListData);
		lvSearch.setAdapter(searchBarAdapter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		WSearchBarActivity.this.finish();
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
	 * 这些把预先设置的数据存到arraylist
	 * @return arraylist
	 */
	private ArrayList<String> getData() {
		String[] SourceDateList = this.getResources().getStringArray(R.array.English);
		ArrayList<String> arraylist = new ArrayList<String>();   
		//根据需求添加一些数据,     
		for (int i = 0; i <SourceDateList.length; i++) {            
			arraylist.add(SourceDateList[i]);   
		}  
		return arraylist;   
	}  
	
	
	/**
	 * 刷新listview搜索结果
	 */
	Runnable searchChanged = new Runnable() {	
 		@Override
 		public void run() {
 			String strKey = etSearch.getText().toString();
			getmNewDates(strKey);//获取更新数据
 			Log.i(TAG, ""+"notifyDataSetChanged");	
 			searchBarAdapter.setData(newListData);
 			if(newListData.isEmpty()){
 				Toast.makeText(WSearchBarActivity.this, R.string.search_nothing, Toast.LENGTH_SHORT).show();
 				etSearch.setShakeAnimation();//没有搜索到内容时抖动窗口
 			}
 			searchBarAdapter.notifyDataSetChanged();//更新	
 		}
 	};
 	
 	
 	/**
 	 * 	设置搜索页面的返回图标
 	 * @param ivBackId
 	 */
 	public void setIvBack(int ivBackId){
 		ImageView ivBack   = (ImageView) findViewById(R.id.iv_back);
 		ivBack.setImageResource(ivBackId);
 	}
 	
 	/**
 	 * 	设置搜索框背景
 	 * @param editTextBackgroundId
 	 */
 	public void setEditTextBackground(int editTextBackgroundId){
 		etSearch.setBackgroundResource(editTextBackgroundId);
 	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "onItemClick()...");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		Log.i(TAG, "beforeTextChanged()...");	
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.i(TAG, "onTextChanged()...");
		if(s.length() == 0){		
			newListData.clear();
			searchBarAdapter.setData(newListData);
			searchBarAdapter.notifyDataSetChanged();//更新
		}else {
			newListData.clear();
			mHandler.post(searchChanged);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		Log.i(TAG, "afterTextChanged()...");	
	}
	
	
	/**
	 * 搜索列表的适配器
 	 */
 	public class listSearchBarAdapter extends BaseAdapter{
 		
		private ArrayList<String> searchListData;
		private Context mContext;

		public listSearchBarAdapter(Context context){
			this.mContext = context;
			Log.i(TAG, "listSearchBarAdapter()...init....");
		}
		
		@Override
		public int getCount() {
			return searchListData.size();
		}
		
		@Override
		public Object getItem(int position) {
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
			    view = LayoutInflater.from(mContext).inflate(R.layout.ws_itemlistw_search_bar, null);  
			    viewHolder = new ViewHolder();  
		    	viewHolder.img_icon = (ImageView) view.findViewById(R.id.iv_serach_bar_icon);  
			    viewHolder.img_up   = (ImageView) view.findViewById(R.id.iv_serach_bar_up) ;
			    viewHolder.tv_info  = (TextView)  view.findViewById(R.id.tv_search_bar_info); 
			    view.setTag(viewHolder);//将viewholder存储在view中  
			}else{  
			    view = convertView;  
			    viewHolder = (ViewHolder)view.getTag();//重新获取viewholder       
			}  
			viewHolder.img_icon.setImageResource(R.drawable.ws_common_icon_searchbox_magnifier_2);  
		    viewHolder.img_up  .setImageResource(R.drawable.ws_icon_search_up_retrieval);
		    viewHolder.tv_info .setText(searchListData.get(position));   		    
			return view;  
		}
		
		
		/**
		 * @param searchListData
		 */
		public void setData(ArrayList<String> searchListData) {
			this.searchListData = searchListData;
			Log.i(TAG, "listSearchBarAdapter()...setData()....");
		}	
 	}
 	
 	/**
 	 * ui for adapter
 	 */
 	class ViewHolder { 
 	    ImageView img_icon; 
 	    ImageView img_up;
 	    TextView  tv_info;  
 	}
}
