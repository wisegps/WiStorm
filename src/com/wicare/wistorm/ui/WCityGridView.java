package com.wicare.wistorm.ui;

import android.widget.GridView;

/**
 * @author Wu
 * 
 * 热门城市显示的gridview
 */
public class WCityGridView extends GridView {
	
	public WCityGridView(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}