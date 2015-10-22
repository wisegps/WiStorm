package com.wicare.wistorm.ui;

import java.util.ArrayList;

import com.wisegps.wistorm.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TabBar
 * @author c
 * @date 2015-10-9
 */
public class WTabBar extends Activity{
	
	final String TAG = WTabBar.this.toString();
	// 缺省未选中字体颜色
	private int textDefaultNormalColor = Color.rgb(195, 195, 195);
	// 缺省选中字体颜色
	private int textDefaultPressColor = Color.rgb(95, 180, 217);
	// 缺省选项卡未选中图片
	private int[] imgDefaultNormal = { 
		R.drawable.ws_ico_tab_home,
		R.drawable.ws_ico_tab_friends,
		R.drawable.ws_ico_tab_msg,
		R.drawable.ws_ico_tab_more };
	// 缺省选项卡选中图片
	private int[] imgDefaultPress = { 
			R.drawable.ws_ico_tab_home_c,
			R.drawable.ws_ico_tab_friends_c,
			R.drawable.ws_ico_tab_msg_c, 
			R.drawable.ws_ico_tab_more_c };
	//是否用户自定义了选项卡图片
	private boolean isUserImage = false;
	private boolean isUserTextColor = false;
	// 用户自定义选项卡未选中图片
	private int[] imgNormal = new int[4];
	// 用户自定义选项卡选中图片
	private int[] imgPress  = new int[4];
	// 用户自定义选项卡未选中时文字颜色
	private int textNormalColor;
	// 用户自定义选项卡选中时文字颜色
	private int textPressColor;

	//存放导航栏选项卡的 textview
	private ArrayList<TextView> textTabBar = new ArrayList<TextView>();
	//存放导航栏选项卡的 ImageView
	private ArrayList<ImageView> imageTabBar = new ArrayList<ImageView>();
	//导航栏选项卡位置numbers
	private int itemBarIndex = 0;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ws_tabbar);
		initTabBar();
	}

	/**
	 * TabBar 点击事件
	 */
	private OnClickListener tabBarListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.tabbar_item0){
				itemBarIndex = 0;
				Toast.makeText(WTabBar.this, "选项卡一", Toast.LENGTH_SHORT).show();
			}
			if(v.getId() == R.id.tabbar_item1){
				itemBarIndex = 1;
				Toast.makeText(WTabBar.this, "选项卡二", Toast.LENGTH_SHORT).show();
			}
			if(v.getId() == R.id.tabbar_item2){
				itemBarIndex = 2;
				Toast.makeText(WTabBar.this, "选项卡三", Toast.LENGTH_SHORT).show();
			}
			if(v.getId() == R.id.tabbar_item3){
				itemBarIndex = 3;
				Toast.makeText(WTabBar.this, "选项卡四", Toast.LENGTH_SHORT).show();
			}
			setTabBarView(itemBarIndex);
		}
	};
	
	
	/**
	 * 设置TabBar 初始界面
	 * @param imageNormal 选项卡未选中图片
	 * @param imagePress  选项卡选中图片
	 */
	public void setItemImage(int[] imageNormal,int[] imagePress){
		
		Toast.makeText(WTabBar.this, "选项卡四"+imgNormal.length, Toast.LENGTH_SHORT).show();
		for(int i=0;i<imageNormal.length;i++){
			if(i == 0){
				imageTabBar.get(i).setImageResource(imagePress[i]);
			}else{
				imageTabBar.get(i).setImageResource(imageNormal[i]);
			}	
			this.imgNormal[i] = imageNormal[i];
			this.imgPress[i]= imagePress[i];
		}
		isUserImage = true;
	}
	
	
	/**
	 * 设置导航栏的选项卡标题
	 * @param title 选项卡的标题
	 */
	public void setItemTitle(String[] title){
		for(int i=0;i<title.length;i++){
			textTabBar.get(i).setText(title[i]);
		}
	}
	
	/**
	 * @param textNormalColor  自定义选项卡未选中的颜色
	 * @param textPressColor   自定义选项卡选中的颜色
	 */
	public void setItemTitleColor(int textNormalColor,int textPressColor){
		this.isUserTextColor = true;
		this.textNormalColor = textNormalColor;
		this.textPressColor  = textPressColor;
		for(int i=0;i<textTabBar.size();i++){
			if(i==0){
				textTabBar.get(i).setTextColor(textPressColor);
			}else{
				textTabBar.get(i).setTextColor(textNormalColor);
			}
		}
	}

	
	/**
	 * 点击导航栏设置导航栏view属性
	 * @param index
	 */
	private void setTabBarView(int index){
			setTabBarImageView(index);
			setTabBarTextColor(index);
	}
	
	/**
	 * 设置导航栏选项卡字体 颜色属性
	 * @param index
	 */
	private void setTabBarTextColor(int index){
		Log.i(TAG, "setTabBarTextColor()" + index);

		for(int i = 0;i < textTabBar.size();i++){
			if(index == i){
				if(isUserTextColor){
					textTabBar.get(i).setTextColor(textPressColor);
				}else{
					textTabBar.get(i).setTextColor(textDefaultPressColor);
				}
			}else{
				if(isUserTextColor){
					textTabBar.get(i).setTextColor(textNormalColor);
				}else{
					textTabBar.get(i).setTextColor(textDefaultNormalColor);
				}
			}
		}
	}
	
	/**
	 * 设置导航栏选项卡图片背景属性
	 * @param index
	 */
	private void setTabBarImageView(int index){
		Log.i(TAG, "setTabBarImageView()" + index);
		for(int i = 0;i < imageTabBar.size();i++){
			if(index == i){
				if(isUserImage){
					imageTabBar.get(i).setImageResource(imgPress[i]);
				}else{
					imageTabBar.get(i).setImageResource(imgDefaultPress[i]);
				}
			}else{
				if(isUserImage){
					imageTabBar.get(i).setImageResource(imgNormal[i]);
				}else{
					imageTabBar.get(i).setImageResource(imgDefaultNormal[i]);
				}
			}
		}
	}
	
	/**
	 * 初始化TabBarUI
	 */
	private void initTabBar(){
		this.findViewById(R.id.tabbar_item0).setOnClickListener(tabBarListener);
		this.findViewById(R.id.tabbar_item1).setOnClickListener(tabBarListener);
		this.findViewById(R.id.tabbar_item2).setOnClickListener(tabBarListener);
		this.findViewById(R.id.tabbar_item3).setOnClickListener(tabBarListener);
		ImageView tabBarItem_iv0 = (ImageView) findViewById(R.id.iv_tabbar_item0);
		ImageView tabBarItem_iv1 = (ImageView) findViewById(R.id.iv_tabbar_item1);
		ImageView tabBarItem_iv2 = (ImageView) findViewById(R.id.iv_tabbar_item2);
		ImageView tabBarItem_iv3 = (ImageView) findViewById(R.id.iv_tabbar_item3);
		
		imageTabBar.add(tabBarItem_iv0);
		imageTabBar.add(tabBarItem_iv1);
		imageTabBar.add(tabBarItem_iv2);
		imageTabBar.add(tabBarItem_iv3);
		
		TextView tabBarItem_tv0 = (TextView) findViewById(R.id.tv_tabbar_title0);
		TextView tabBarItem_tv1 = (TextView) findViewById(R.id.tv_tabbar_title1);
		TextView tabBarItem_tv2 = (TextView) findViewById(R.id.tv_tabbar_title2);
		TextView tabBarItem_tv3 = (TextView) findViewById(R.id.tv_tabbar_title3);
		
		textTabBar.add(tabBarItem_tv0);
		textTabBar.add(tabBarItem_tv1);
		textTabBar.add(tabBarItem_tv2);
		textTabBar.add(tabBarItem_tv3);
	}
}
