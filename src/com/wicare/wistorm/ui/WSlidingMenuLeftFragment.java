package com.wicare.wistorm.ui;

import com.wicare.wistorm.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WSlidingMenuLeftFragment extends Fragment implements OnItemClickListener {
	
	private View mView;
	Fragment newContent = null;
	// 列表
	public ListView mListView;
	// 侧滑菜单列表 适配器
    private MenuListViewAdapter menuAdapter;  
    // 菜单标题
    private int[] mMenu = { 
    		R.string.slidingmen_mene_1, 
    		R.string.slidingmen_mene_2,
    		R.string.slidingmen_mene_3 };  
	
	private int[] mMenuImg = {
			R.drawable.ws_ic_slidingmenu_menu_1,
			R.drawable.ws_ic_slidingmenu_menu_2,
			R.drawable.ws_ic_slidingmenu_menu_3};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView == null){
			initView(inflater, container);
		}
		return mView;
	}
	
	private void initView(LayoutInflater inflater,ViewGroup container){
		mView = inflater.inflate(R.layout.ws_slidingmenu_left_fragment, container, false);
		mListView = (ListView) mView.findViewById(R.id.lv_left_menu);
		menuAdapter = new MenuListViewAdapter(getActivity(), mMenu, mMenuImg);
		mListView.setAdapter(menuAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		switch (position) {
		case 0:
			// add your code 
			Intent i1 = new Intent(getActivity(),WSlidingmenu_One.class);
			startActivity(i1);
			break;
		case 1:
			// add your code 
			Intent i2 = new Intent(getActivity(),WSlidingmenu_Two.class);
			startActivity(i2);
			break;
		case 2:
			// add your code 
			Intent i3 = new Intent(getActivity(),WSlidingmenu_Three.class);
			startActivity(i3);
			break;
		}	
	} 
	


	/**
	 * 切换 fragment
	 * @param fragment 切换的碎片
	 * @param title    当前碎片的标题
	 */
/*	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof WSlidingMenu) {
			WSlidingMenu fragChange = (WSlidingMenu) getActivity();
			fragChange.switchConent(fragment);
		}
	}*/
	
	
	
	
	/**
	 * MenuListViewAdapter  侧滑菜单栏的菜单列表适配器
	 */
	public class MenuListViewAdapter extends BaseAdapter {
		private Context  mContext;  
	    private int[] mTitle= null;   
	    private int[] mPhoto= null;  
	      
	    /**
	     * @param context  上下文
	     * @param name     菜单标题
	     * @param phohtNum 菜单头像
	     */
	    public MenuListViewAdapter(Context context,int[] title,int[] phohtNum){  
	        this.mContext = context;  
	        this.mTitle  = title;  
	        this.mPhoto = phohtNum;   
	    }  

	    @Override  
	    public int getCount() {  
	        // TODO Auto-generated method stub  
	        return mTitle.length;  
	    }  
	  
	  
	    @Override  
	    public Object getItem(int position) {  
	        // TODO Auto-generated method stub  
	        return mTitle[position];  
	    }  
	  
	  
	    @Override  
	    public long getItemId(int position) {  
	        // TODO Auto-generated method stub  
	        return position;  
	    }  
	      
	    @SuppressLint("InflateParams") @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        // TODO Auto-generated method stub    
	        View view;  
	        ViewHolderMenu viewHolder;  
	        if (convertView == null) {  
	            view = LayoutInflater.from(mContext).inflate(R.layout.ws_item_list_slidingmenu, null);  
	            viewHolder = new ViewHolderMenu();  
	            viewHolder.img = (ImageView)view.findViewById(R.id.img);  
	            viewHolder.title = (TextView)view.findViewById(R.id.title);
	            view.setTag(viewHolder);//将viewholder存储在view中  
	        }else{  
	            view = convertView;  
	            viewHolder = (ViewHolderMenu)view.getTag();//重新获取viewholder       
	        }  
	        if(mPhoto!=null)  
	        	viewHolder.img.setImageResource(mPhoto[position]);  
	        if(mTitle!=null)  
	        	viewHolder.title.setText(mTitle[position]);   
	        return view;  
	    }  
	}  


	/**
	 * @author Wu
	 * item_list UI控件
	 */
	class ViewHolderMenu {  
	    ImageView img;  
	    TextView  title;   
	   }



}
