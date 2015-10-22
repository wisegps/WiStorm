package com.wicare.wistorm.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wisegps.wistorm.R;

/**
 * 自定义  alert Dialog
 * @author Wu
 *
 */
public class WDialog extends Dialog{
	//实现默认构造函数  
	public WDialog(Context context, int theme) {   
	    super(context, theme);  
	}  
	  
	protected WDialog(Context context, boolean cancelable,  
	        OnCancelListener cancelListener) {  
	    super(context, cancelable, cancelListener);  
	}  
	  
	public WDialog(Context context) {  
	    super(context);  
	} 
	
	
	 /**
	 * 所有的方法执行完都会返回一个Builder
	 * 使得后面可以直接create和show 
	 */
	public static class Builder {  
        private Context context;  
        private String title;  
        private String message;  
        private String positiveButtonText;//确定按钮  
        private String negativeButtonText;//取消按钮  
        private View contentView;  
        private BaseAdapter adapter;//listview的adapter  
        //确定按钮事件  
        private DialogInterface.OnClickListener positiveButtonClickListener;  
        //取消按钮事件  
        private DialogInterface.OnClickListener negativeButtonClickListener;  
        //listview的item点击事件  
        private AdapterView.OnItemClickListener listViewOnclickListener;  
  
        public Builder(Context context) {  
            this.context = context;  
        } 
        
        /** 
         * 从资源文件中设置 dialog的消息
         * @param message 
         * @return 
         */  
        public Builder setMessage(int message) {  
            this.message = (String) context.getText(message);  
            return this;  
        }  
        
        
        /**
         * 用字符串设置 dialog的消息 
         * @param message
         * @return
         */
        public Builder setMessage(String message) {  
            this.message = message;  
            return this;  
        }  
   
  
        /** 
         * 从资源文件中设置 dialog的标题
         * @param title 
         * @return 
         */  
        public Builder setTitle(int title) {  
            this.title = (String) context.getText(title);  
            return this;  
        }  
        
        
        /** 
         * 用字符串设置 dialog的标题 
         * @param title 
         * @return 
         */  
        public Builder setTitle(String title) {  
            this.title = title;  
            return this;  
        }  
        

        /**
         * 设置适配器
         * @param items[]
         * @return
         */
        public Builder setItems(String[] items) {    	
            this.adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);  
            return this;  
        }  
        
        /**
         * 设置点击事件
         * @param listViewOnclickListener
         * @return
         */
        public Builder setOnClickListener(AdapterView.OnItemClickListener listViewOnclickListener) {  
            this.listViewOnclickListener = listViewOnclickListener;  
            return this;  
        } 
        
        /**
         * 设置背景
         * @param adapter
         * @return
         */ 
        public Builder setContentView(View v) {  
            this.contentView = v;  
            return this;  
        } 
        
        
        /** 
         * 设置确定按钮和其点击事件   
         * @param positiveButtonText 
         * @return 
         */    
        public Builder setPositiveButton(String positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = positiveButtonText;  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
        
        /** 
        * 设置取消按钮和其点击事件   
        * @param positiveButtonText 
        * @return 
        */
        public Builder setNegativeButton(String negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = negativeButtonText;  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  

        /**
         * 对话框的 createview方法 
         * 
         * @return
         */
        @SuppressLint("InflateParams") 
        public WDialog create() {  
            LayoutInflater inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            // 设置其风格  
            final WDialog dialog = new WDialog(context,R.style.progressDialog);  
            View layout = inflater.inflate(R.layout.ws_customs_dialog, null);  
            dialog.addContentView(layout, new LayoutParams(  
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));  
            // 设置标题  
            ((TextView) layout.findViewById(R.id.title)).setText(title);  
          //设置listview的adapter如果没有就隐藏listview  
            if(adapter != null && adapter.getCount()>0){  
                ListView listView = (ListView)layout.findViewById(R.id.listView);  
                listView.setAdapter(adapter);  
                if(listViewOnclickListener!=null){  
                    listView.setOnItemClickListener(listViewOnclickListener);  
                }  
  
            }else{  
                layout.findViewById(R.id.listView).setVisibility(  
                        View.GONE);  
                layout.findViewById(R.id.lv_view).setVisibility(View.GONE);
            }  
  
  
            //设置确定按钮  
            if (positiveButtonText != null) {  
                ((TextView) layout.findViewById(R.id.positiveButton))  
                        .setText(positiveButtonText);  
                if (positiveButtonClickListener != null) {  
                    ((TextView) layout.findViewById(R.id.positiveButton))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    positiveButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_POSITIVE);  
                                }  
                            });  
                }  
            } else {  
                 // 如果没有确定按钮就将其隐藏  
                layout.findViewById(R.id.positiveButton).setVisibility(  
                        View.GONE);  
            }  
            // 设置取消按钮  
            if (negativeButtonText != null) {  
                ((TextView) layout.findViewById(R.id.negativeButton))  
                        .setText(negativeButtonText);  
                if (negativeButtonClickListener != null) {  
                    ((TextView) layout.findViewById(R.id.negativeButton))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    negativeButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_NEGATIVE);  
                                }  
                            });  
                }  
            } else {  
                // 如果没有取消按钮就将其隐藏  
                layout.findViewById(R.id.negativeButton).setVisibility(  
                        View.GONE);  
            }  
            // 设置内容  
            if (message != null) {  
                ((TextView) layout.findViewById(R.id.message)).setText(message);  
            } else if (contentView != null) {  
                // if no message set  
                // 添加view  
                ((LinearLayout) layout.findViewById(R.id.message))  
                        .removeAllViews();  
                ((LinearLayout) layout.findViewById(R.id.message)).addView(  
                        contentView, new LayoutParams(  
                                LayoutParams.WRAP_CONTENT,  
                                LayoutParams.WRAP_CONTENT));  
            }  
            dialog.setContentView(layout);  
            return dialog;  
        }  
  
    }  
}
