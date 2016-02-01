package com.wicare.wistorm.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;

import com.wicare.wistorm.R;
import com.wicare.wistorm.ui.slidingmuen.SlidingFragmentActivity;
import com.wicare.wistorm.ui.slidingmuen.SlidingMenu;

/**
 * @author Wu
 * 
 * 侧滑菜单Activity 。可以直接继承
 */
public class WSlidingMenu extends SlidingFragmentActivity{
	
	static final String TAG = "WSlidingMenu";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ws_slidingmenu_right_content);
		setBehindContentView(R.layout.ws_slidingmenu_left_content);
		initSlidingMenu();
	}
	
	 /**
     * 初始化侧边栏
     */
    @SuppressWarnings("deprecation")
	private void initSlidingMenu( ) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_left_content, new WSlidingMenuLeftFragment()).commit();
        
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.menu_right_content, new WSlidingMenuRightFragment()).commit();
        
        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
        // 设置可以左右滑动的菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth()*2/5);
        // 设置触摸屏幕的模式,这里设置为全屏
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.5f);
        // 设置背景
        sm.setBackgroundImage(R.drawable.ws_login_bg);
        //设置淡入淡出的比例 
        sm.setFadeEnabled(false);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
        // 设置侧滑菜单滑动时候的菜单栏动画
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, -canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		});
        //设置侧滑菜单滑动时候的主页面动画
		sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (1 - percentOpen * 0.25);
				canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
			}
		});
    }
    
    
    /**
	 * @param fragment 侧滑菜单的菜单栏页面的fragment
	 */
	public void setLeftSlidingmenu (Fragment fragment){
		getSupportFragmentManager().beginTransaction()
	    .replace(R.id.menu_left_content, fragment).commit();
	}

	/**
	 * @param fragment 点击侧滑菜单选项后切换的fragment 替换到右边的FrameLayout
	 */
	public void setRightSlidingmenu (Fragment fragment){
		getSupportFragmentManager().beginTransaction()
	    .replace(R.id.menu_right_content, fragment).commit();
	}
	
	/**
	 * @param offset 设置侧滑菜单栏的滑出宽度
	 */
	public void setSlidingmenuBehindOffset(int offset){
		SlidingMenu sm =getSlidingMenu();
		sm.setBehindOffset(offset);
	}
	
	
	/**
	 * @param resid 设置侧滑菜单栏的背景图片
	 */
	public void setSlidingmenuBackgroundImage(int resid){
		SlidingMenu sm =getSlidingMenu();
		sm.setBackgroundImage(resid);
	}
	
	
	/**
	 * @param f 设置下方视图的在滚动时的缩放比例(侧滑菜单滑动和右边二级菜单移动比例 f=0 侧滑菜单栏不移动)
	 */
	public void setSlidingmenuBehindScrollScale(float f){
		SlidingMenu sm =getSlidingMenu();
		sm.setBehindScrollScale(f);
	}
	

    
    /**
	 *  切换Fragment
	 * @param fragment
	 */
/*	public void switchConent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_right_content, fragment).commit();
		getSlidingMenu().showContent();
	}*/
	
	
	
}
