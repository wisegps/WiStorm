package com.wicare.wistorm.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wicare.wistorm.R;

/**
 * @author Wu
 * 
 */
public class WLisView extends Activity{
	
	final String TAG = WLisView.this.toString();
	
    private String[] mListTitle={"姓名: ","性别: ","年龄: ","居住地: ","邮箱: "};
    private String[] mListStr={"Wistorm","男","100","shenzhen","1000@live.cn"}; 
	private int i=0; 
	
	private PullToRefreshListView  mPullToRefreshListView;  
    List<Map<String,Object>> mData=new ArrayList<Map<String,Object>>(); 
    private listPullToRefreshAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ws_pull_refresh);
		
		initIndicator();
		mData=getmData();//模拟数据
		
		listAdapter = new listPullToRefreshAdapter(this);
		listAdapter.setData(mData);
		mPullToRefreshListView.setAdapter(listAdapter);    
	}
	
	
	/**
	 * 默认为 下拉刷新
	 * 设置刷新的模式：Mode.BOTH(上下拉)  
	 * Mode.PULL_FROM_END(上拉)  
	 * Mode.PULL_FROM_START(下拉)
	 * 刷新模式为上下拉   
	 */
	public void setModelBoth(){
		mPullToRefreshListView.setMode(Mode.BOTH);
	}
	
	/**
	 * 默认为 下拉刷新
	 * 设置刷新的模式：Mode.BOTH(上下拉)  
	 * Mode.PULL_FROM_END(上拉)  
	 * Mode.PULL_FROM_START(下拉)
	 * 刷新模式为上拉
	 */
	public void setModelFromEnd(){
		mPullToRefreshListView.setMode(Mode.BOTH);
	}
	
	/**
	 * 默认为 下拉刷新
	 * 设置刷新的模式：Mode.BOTH(上下拉)  
	 * Mode.PULL_FROM_END(上拉)  
	 * Mode.PULL_FROM_START(下拉)
	 * 刷新模式为下拉
	 */
	public void setModelFromStart(){
		mPullToRefreshListView.setMode(Mode.BOTH);
	}
    /**
     * 初始化列表格式、 刷新监听
     */
    private void initIndicator(){ 
    	mPullToRefreshListView= (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		//刷新监听事件  
		mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>(){  
            @SuppressLint("SimpleDateFormat") @Override  
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {  
                // 模拟加载任务  
                new GetDataTask().execute();  
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                String label=simpleDateFormat.format(System.currentTimeMillis());  
                // 显示最后更新的时间  
                refreshView.getLoadingLayoutProxy()  
                        .setLastUpdatedLabel(label);  
            }  
        });  
        ILoadingLayout startLabels = mPullToRefreshListView.getLoadingLayoutProxy(true, false);  
        startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示  
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时  
        startLabels.setReleaseLabel("松开以刷新");// 下来达到一定距离时，显示的提示  
  
        ILoadingLayout endLabels = mPullToRefreshListView.getLoadingLayoutProxy(false, true);  
        endLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示  
        endLabels.setRefreshingLabel("正在刷新...");// 刷新时  
        endLabels.setReleaseLabel("松开以刷新");// 下来达到一定距离时，显示的提示  
    }  
    
    
    
    /**
     * @return  返回listview的显示数据
     */
    public List<Map<String,Object>> getmData(){  
        for(int i=0;i<mListTitle.length;i++){  
            Map<String,Object> map=new HashMap<String,Object>();  
            map.put("title",mListTitle[i]);  
            map.put("info",mListStr[i]);  
            mData.add(map);  
        }  
        return mData;  
    }  
	
    
    
	/**
	 * 异步加载
	 */
	private class GetDataTask extends AsyncTask<Void, Void, Map<String,Object>> {  
        @Override  
        protected Map<String, Object> doInBackground(Void... params) {  
            Map<String,Object> map=new HashMap<String,Object>();  
            //如果这个地方不使用线程休息的话，刷新就不会显示在那个PullToRefreshListView的UpdatedLabel上面  
			try {  
			    Thread.sleep(2500);  
			} catch (InterruptedException e) {  
			    e.printStackTrace();  
			}  
			i++;
			map.put("title","title"+(i)+":--->");  
			map.put("info", "info" + (i));  
//			//加入数据，这里是可以的  
//		    mData.add(map);  
		    
		    return map;  
		}  
		@Override  
		protected void onPostExecute(Map<String, Object> stringObjectMap) {  
		    //super.onPostExecute(stringObjectMap);  
			mData.add(stringObjectMap);  
			listAdapter.notifyDataSetChanged();//更新数据  
			// Call onRefreshComplete when the list has been refreshed. 如果没有下面的函数那么刷新将不会停  
			mPullToRefreshListView.onRefreshComplete();  
		}  
	}  
 

	/**
	 * 上下拉列表的适配器
 	 */
 	public class listPullToRefreshAdapter extends BaseAdapter{
 		
		private List<Map<String,Object>> listData;
		private Context mContext;

		public listPullToRefreshAdapter(Context context){
			this.mContext = context;
			Log.i(TAG, "listPullToRefreshAdapter()...init....");
		}
		
		@Override
		public int getCount() {
			return listData.size();
		}
		
		@Override
		public Object getItem(int position) {
			return listData.get(position);
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
			    view = LayoutInflater.from(mContext).inflate(R.layout.ws_pull_refresh_items, null);  
			    viewHolder = new ViewHolder();  
			    viewHolder.tv_title  = (TextView)  view.findViewById(R.id.tv_item_title);
			    viewHolder.tv_info  = (TextView)  view.findViewById(R.id.tv_item_info); 
			    view.setTag(viewHolder);//将viewholder存储在view中  
			}else{  
			    view = convertView;  
			    viewHolder = (ViewHolder)view.getTag();//重新获取viewholder       
			}  
			viewHolder.tv_title .setText(listData.get(position).get("title").toString());
		    viewHolder.tv_info .setText(listData.get(position).get("info").toString());   		    
			return view;  
		}
		
		
		/**
		 * @param searchListData
		 */
		public void setData(List<Map<String,Object>> listData) {
			this.listData = listData;
			Log.i(TAG, "listPullToRefreshAdapter()...setData()....");
		}	
 	}
 	
 	/**
 	 * ui for adapter
 	 */
 	class ViewHolder { 
 	    TextView  tv_title;
 	    TextView  tv_info;  
 	}
}
