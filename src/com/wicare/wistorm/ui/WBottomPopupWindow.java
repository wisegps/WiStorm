package com.wicare.wistorm.ui;

import com.wisegps.wistorm.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class WBottomPopupWindow extends PopupWindow {
	
	private Button btnSave;
	private Button btnExit;
	private Button btnCancel;
	private View mMenuView;
	
	@SuppressLint({ "InflateParams", "ClickableViewAccessibility" }) 
	public WBottomPopupWindow(Context context,OnClickListener itemsOnClickListener){
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.ws_buttom_popupwindow, null);
		btnSave =   (Button) mMenuView.findViewById(R.id.btn_save);
		btnExit =   (Button) mMenuView.findViewById(R.id.btn_exit);
		btnCancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		
		btnSave .setOnClickListener(itemsOnClickListener);
		btnExit .setOnClickListener(itemsOnClickListener);
		btnCancel .setOnClickListener(itemsOnClickListener);
		
		//设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.wise_anim_menu_bottom_bar);//下到上
        //实例化一个ColorDrawable颜色为全透明  
        ColorDrawable dw = new ColorDrawable(0xffffffff);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框 
        mMenuView.setOnTouchListener(new OnTouchListener() {    
	        public boolean onTouch(View v, MotionEvent event) {   
	            int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
	            int y=(int) event.getY();  
	            if(event.getAction()==MotionEvent.ACTION_UP){  
	                if(y<height){  
	                    dismiss();  
	                }  
	            }                 
	            return true;  
	        }  
	    });  
	}	
	

}
