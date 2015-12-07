package com.wicare.wistorm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wicare.wistorm.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Wu
 * 
 * 九宫格滑动页面导航.具体引用参考demo在布局xml中的引用方式
 */
public class WNavigationBar extends LinearLayout implements OnPageChangeListener{
	
	private Context mContext;
	private List<View> views = new ArrayList<View>();
	private MyViewPagerAdapter adapter;//viewPager 适配器
	
	ImageView imgCircleDot0;//左边的圆点
	ImageView imgCircleDot1;//右边的圆点
	private OnGridviewClickListener onGridviewClickListener;//Gridview 接口

	public WNavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initView();//加载完初始化ui
	}
	
	
	/**
	 * 初始化页面
	 */
	public void initView(){

		WWrapContentHeightViewPager vp = (WWrapContentHeightViewPager)this.getChildAt(0);
		// 子项适配器
		SimpleAdapter simpleAdapter0 = getAdapter(0);
		SimpleAdapter simpleAdapter1 = getAdapter(1);
        
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view1 = layoutInflater.inflate(R.layout.ws_navigation_item, null);
		GridView gridView_1 = (GridView) view1.findViewById(R.id.gview_1);
		gridView_1.setAdapter(simpleAdapter0);
		gridView_1.setOnItemClickListener(onGridview_1_ClickListener);
		
		View view2 = layoutInflater.inflate(R.layout.ws_navigation_item, null);
		GridView gridView_2 = (GridView) view2.findViewById(R.id.gview_1);
		gridView_2.setAdapter(simpleAdapter1);
		gridView_2.setOnItemClickListener(onGridview_2_ClickListener);

		views.add(view1);
		views.add(view2);
		
		adapter = new MyViewPagerAdapter(views);
		vp.setAdapter(adapter); 	
		vp.setOnPageChangeListener(this);
		
		LinearLayout ll = (LinearLayout)this.getChildAt(1);
		imgCircleDot0 = (ImageView) ll.getChildAt(0).findViewById(R.id.iv_circle_page_0);
		imgCircleDot1 = (ImageView) ll.getChildAt(1).findViewById(R.id.iv_circle_page_1);
	}
	
	
	/* (non-Javadoc)
	 * @see android.widget.LinearLayout#onLayout(boolean, int, int, int, int)
	 */
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	};
	
	
	/**
	 * 第一个页面点击监听事件
	 */
	OnItemClickListener onGridview_1_ClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {	
			onGridviewClickListener.onGridview_1_Click(position);
		}
	};
    
	/**
	 * 第二个页面点击监听事件
	 */
	OnItemClickListener onGridview_2_ClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {	
			onGridviewClickListener.onGridview_2_Click(position);
		}
	};
	
	
	
	/**
	 * @param onTabChangedListener 切换选项卡监听
	 */
	public void setOnGridviewClickListener(
			OnGridviewClickListener onGridviewClickListener) {
		this.onGridviewClickListener = onGridviewClickListener;
	}
	
	/**
	 * 选项卡切换监听接口
	 */
	public interface OnGridviewClickListener {
		public void onGridview_1_Click(int position);
		public void onGridview_2_Click(int position);
	}
	
	
	
	/**
	 * GridView适配器
	 */
	public SimpleAdapter getAdapter(int page) {
		// 生成动态数组，并且转入数据
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		int[] imgId = null;
		if (page == 0) {
			imgId = new int[] { R.drawable.ws_ico_poi_home,
					R.drawable.ws_ico_poi_company, R.drawable.ws_ico_poi_gas,
					 R.drawable.ws_ico_poi_charge, R.drawable.ws_ico_poi_parking,
					R.drawable.ws_ico_poi_hotel, R.drawable.ws_ico_poi_movie,
					R.drawable.ws_ico_poi_scenic };
		} else {
			imgId = new int[] { R.drawable.ws_ico_poi_bank,
					R.drawable.ws_ico_poi_hospital, R.drawable.ws_ico_poi_shop,

					R.drawable.ws_ico_poi_atm, R.drawable.ws_ico_poi_metro,
					R.drawable.ws_ico_poi_bus, R.drawable.ws_ico_poi_repair,
					R.drawable.ws_ico_poi_beauty };
		}

		String[] text = null;
		if (page == 0) {
			text =  new String[] { "家", "公司", "加油站",  "充电柱", "停车场","酒店", "电影",
					"景点" };
		} else {
			text =  new String[] { "银行", "医院", "商场", "ATM", "地铁站", "公交站",
					"汽车维修", "汽车美容" };
		}
		
		HashMap<String, Object> map;
		for (int i = 0; i < imgId.length; i++) {
			map = new HashMap<String, Object>();
			map.put("imgItem", imgId[i]);// 添加图像资源的ID
			map.put("textItem", text[i]);// 文字描述
			listItem.add(map);
		}

		SimpleAdapter saImageItems = new SimpleAdapter(mContext,
				listItem, R.layout.ws_navigation_gridview_item, new String[] { "imgItem",
						"textItem" }, new int[] { R.id.image, R.id.text });
		return saImageItems;
	}

	
	/**
	 * ViewPager 滑动监听
	 */
	
	@Override
	public void onPageScrollStateChanged(int arg0) {	
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int page) {
		imgCircleDot0 = (ImageView) this.findViewById(R.id.iv_circle_page_0);
		imgCircleDot1 = (ImageView) this.findViewById(R.id.iv_circle_page_1);
		
		if(page%2 == 0){
			imgCircleDot0.setImageResource(R.drawable.ws_img_circle_dot_blue);
			imgCircleDot1.setImageResource(R.drawable.ws_img_circle_dot_blue_light);
		}else{
			imgCircleDot0.setImageResource(R.drawable.ws_img_circle_dot_blue_light);
			imgCircleDot1.setImageResource(R.drawable.ws_img_circle_dot_blue);
		}
	}
	
	
    /**
     * @author Wu
     * viewPager 适配器
     */
    class MyViewPagerAdapter extends PagerAdapter{
    	
    	List<View> viewLists;
    	
    	public MyViewPagerAdapter(List<View> viewLists){
    		this.viewLists = viewLists;
    	}

    	/*获取窗体数*/
		@Override
		public int getCount() {		
			if (viewLists != null && viewLists.size() > 0) {  
	            return viewLists.size();  
	        } else {  
	            return 0;  
	        }  
		}
		
		/*这个方法用于判断是否由对象生成界面*/
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}
		
		@Override  
	    public void destroyItem(ViewGroup container, int position, Object object) {  
	        container.removeView(viewLists.get(position));
	    }  
	  
	    @Override  
	    public Object instantiateItem(ViewGroup container, int position) {  
	        container.addView(viewLists.get(position));  
	        return viewLists.get(position);  
	    }  
	  
	    @Override  
	    public int getItemPosition(Object object) {  
	        return POSITION_NONE;  
	    }  
    }

}
