package com.wicare.wistorm.ui;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wicare.wistorm.R;

/**
 * @author Wu
 * 
 * 自定义加载框
 */
public class WLoading extends Dialog{
	 
	private Context context;
	private static WLoading wLoading;
	
	public WLoading(Context context) {
		super(context);
		this.context = context;
	}
	public WLoading(Context context, int theme) {
	    super(context, theme);
	}
	     
    /**
     * 设置WLoading的参数
     * @param context
     * @return
     */
    public static WLoading createDialog(Context context){
    	
        wLoading = new WLoading(context,R.style.progressDialog);
        wLoading.setContentView(R.layout.ws_progressbar_loading);
        wLoading.getWindow().getAttributes().gravity = Gravity.CENTER;
        wLoading.getWindow().getAttributes().width  =  LayoutParams.WRAP_CONTENT;
        wLoading.getWindow().getAttributes().height =  LayoutParams.WRAP_CONTENT;
        return wLoading;
    }
    
	  
//    public void onWindowFocusChanged(boolean hasFocus){  
//        if (wLoading == null){
//            return;
//        } 
//        ProgressBar progressBar = (ProgressBar) wLoading.findViewById(R.id.p_loading);
//        AnimationDrawable animationDrawable = (AnimationDrawable) progressBar.getBackground();
//        animationDrawable.start();
//    }
	  
    
    /**
     * 设置加载框显示的信息
     * @param strMessage
     * @return
     *
     */
    public WLoading setMessage(String strMessage){
        TextView tvMsg = (TextView)wLoading.findViewById(R.id.tv_loading_msg);
        if (tvMsg != null){
            tvMsg.setText(strMessage);
        } 
        return wLoading;
    }
}
