package com.wicare.wistorm.ui;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Wu
 * 自定义viewpager 自适应高度
 */
public class WWrapContentHeightViewPager extends ViewPager{

	public WWrapContentHeightViewPager(Context context) {
        super(context);
    }
 
    public WWrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 
        int height = 0;
        //遍历每个子控件
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight()*2;//这里要乘以 不然显示一行
            if (h > height) 
                height = h;
        }
 
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
 
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
