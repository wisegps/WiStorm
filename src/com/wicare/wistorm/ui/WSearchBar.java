package com.wicare.wistorm.ui;

import java.util.ArrayList;

import com.wisegps.wistorm.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 实现搜索功能
 * @author Wu
 * @date 2015-10-15
 * 
 */
public class WSearchBar implements TextWatcher, OnItemClickListener {
	
	private static final String TAG = "WSearchBar";
	
	private Context mContext;
	/**
	 * 搜索列表
	 * 
	 */
	private ListView lvSearch;
	/**
	 * 自定义的搜索框
	 * 
	 */
	private WClearEditText etSearch = null;
	
	Handler mHandler = new Handler();
	/**
	 * listview 适配器
	 * 
	 */
	private listSearchBarAdapter searchBarAdapter;
	/**
	 * 筛选出来的数据
	 * 
	 */	
	ArrayList<String> newListData = new ArrayList<String>();
	
	
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
 				Toast.makeText(mContext, R.string.search_nothing, Toast.LENGTH_SHORT).show();
 				etSearch.setShakeAnimation();//没有搜索到内容时抖动窗口
 			}
 			searchBarAdapter.notifyDataSetChanged();//更新	
 		}
 	};
	
	
	public WSearchBar (Context context,ListView listView,WClearEditText editView){
		this.mContext = context;
		this.lvSearch = listView;
		this.etSearch = editView;
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
		searchBarAdapter = new listSearchBarAdapter(mContext);
		searchBarAdapter.setData(newListData);
		lvSearch.setAdapter(searchBarAdapter);
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
		String[] SourceDateList = mContext.getResources().getStringArray(R.array.English);
		ArrayList<String> arraylist = new ArrayList<String>();   
		//根据需求添加一些数据,     
		for (int i = 0; i <SourceDateList.length; i++) {            
			arraylist.add(SourceDateList[i]);   
		}  
		return arraylist;   
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
 	 *
 	 */
 	class ViewHolder { 
 	    ImageView img_icon; 
 	    ImageView img_up;
 	    TextView  tv_info;  
 	}

}
