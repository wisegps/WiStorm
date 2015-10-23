package com.wicare.wistorm.ui;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wicare.wistorm.R;

/**
 * TabBar
 * @author c
 * @date 2015-10-9
 */
public class WTabBar extends LinearLayout implements OnClickListener{
	
	final String TAG = WTabBar.this.toString();
	
	// 选项卡未选中时的缺省图片背景
	private int[] imgNormalDefault = { 
			R.drawable.ws_ico_tab_home,
			R.drawable.ws_ico_tab_msg, 
			R.drawable.ws_ico_tab_friends,
			R.drawable.ws_ico_tab_more };
	// 选项卡选中时的缺省图片背景
	private int[] imgPressDefault = { 
			R.drawable.ws_ico_tab_home_c,
			R.drawable.ws_ico_tab_msg_c, 
			R.drawable.ws_ico_tab_friends_c,
			R.drawable.ws_ico_tab_more_c };
	// 缺省选项卡标题
	private String[] textTitleDefault = { "首页", "信息", "好友", "更多" };
	// 缺省字体颜色
	private int textNormalColorDefault = Color.rgb(195, 195, 195);
	private int textPressColorDefault = Color.rgb(95, 180, 217);
	// 选项卡当前选中tab
	private int index = 0;
	// 选项卡切换监听
	private OnTabChangedListener onTabChangedListener;
	// 是否用户自定义的选项卡标题
	private boolean isUserTextTitle = false;
	// 存放自定义的标题内容
	private String[] textTitle = new String[4];
	
	// 是否用户自定义的选项卡标题颜色
	private boolean isUserTextColor = false;
	// 自定义选项卡未选中时文字颜色
	private int textNormalColor;
	// 自定义选项卡选中时文字颜色
	private int textPressColor;
	
	private boolean isUserImage = false;
	// 用户自定义选项卡未选中图片
	private int[] imgNormal = new int[4];
	// 用户自定义选项卡选中图片
	private int[] imgPress  = new int[4];

	public WTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	/**
	 * 设置选项卡标题
	 * @param tabText 自定义的选项卡标题
	 */
	public void setTabText(String[] tabText){
		isUserTextTitle = true;
		for(int i=0;i<tabText.length;i++){
			textTitle[i] = tabText[i];
		}
		initView();
	}
	
	
	/**
	 * 设置选项卡标题颜色
	 * @param normalColor 选项卡未选中 时标题颜色
	 * @param pressColor  选项卡选中时标题颜色
	 */
	public void setTabTextColor(int normalColor,int pressColor){
		isUserTextColor = true;
		textNormalColor = normalColor;
		textPressColor  = pressColor;
		initView();
	}
	
	
	/**
	 * 设置选项卡图片
	 * @param imageNormal 选项卡未选中的图片
	 * @param imagePress  选项卡选中的图片
	 */
	public void setTabImage(int[] imageNormal,int[] imagePress){
		isUserImage = true;
		for(int i=0;i<imageNormal.length;i++){
			imgNormal[i] = imageNormal[i];
			imgPress[i]  = imagePress[i];
		}
		initView();
	}
	
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		initView();
	}
	
	/**
	 * 初始化导航栏界面
	 */
	public void initView(){
		for (int i = 0; i < this.getChildCount(); i++) {
			LinearLayout llytTab = (LinearLayout) this.getChildAt(i);
			llytTab.setTag(i);
			llytTab.setOnClickListener(this);
			TextView textTab = (TextView) llytTab.getChildAt(1);
			ImageView imgTab = (ImageView) llytTab.getChildAt(0);
			
			if(isUserTextTitle){
				textTab.setText(textTitle[i]);
			}else{
				textTab.setText(textTitleDefault[i]);
			}
			if (index == i) {
				if(isUserImage){
					imgTab.setImageResource(imgPress[i]);
				}else{
					imgTab.setImageResource(imgPressDefault[i]);
				}
				if(isUserTextColor){
					textTab.setTextColor(textPressColor);
				}else{
					textTab.setTextColor(textPressColorDefault);
				}
			} else {
				if(isUserImage){
					imgTab.setImageResource(imgNormal[i]);
				}else{
					imgTab.setImageResource(imgNormalDefault[i]);
				}
				if(isUserTextColor){
					textTab.setTextColor(textNormalColor);
				}else{
					textTab.setTextColor(textNormalColorDefault);
				}
			}
		}
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		this.index = (Integer) view.getTag();
		initView();
		if (onTabChangedListener != null) {
			this.onTabChangedListener.onTabClick(index);
		}
	}
	
	public void performTabClick(int index){
		this.index = index;
		initView();
		if (onTabChangedListener != null) {
			this.onTabChangedListener.onTabClick(index);
		}
	}

	/**
	 * @param onTabChangedListener 切换选项卡监听
	 */
	public void setOnTabChangedListener(
			OnTabChangedListener onTabChangedListener) {
		this.onTabChangedListener = onTabChangedListener;
	}
	
	/**
	 * 选项卡切换监听接口
	 */
	public interface OnTabChangedListener {
		public void onTabClick(int index);
	}

}
