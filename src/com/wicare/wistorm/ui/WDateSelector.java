package com.wicare.wistorm.ui;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.wicare.wistorm.R;


/**
 * DateSlector
 * @author Wu
 * @date 2015-10-9
 */
public class WDateSelector {

	private Context mContext;
	private int mYear, mMonthOfYear, mDayOfMonth;
	private String strYear,strMonthOfYear,strDayOfMonth;
	


	public void setStrDayOfMonth(String strDayOfMonth) {
		this.strDayOfMonth = strDayOfMonth;
	}

	private OnDateChangedListener mOnDateChangedListener;
	
	public WDateSelector(Context context){		
		mContext = context; 
		// get current date
		final Calendar c = Calendar.getInstance();  
        mYear = c.get(Calendar.YEAR);  
        mMonthOfYear = c.get(Calendar.MONTH);  
        mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);  
        
	}
	  

	/**
	 * select date for @param (TextView、EditText or Button)
	 * 
	 * @param textView
	 * @param editText
	 * @param button
	 */
	public void setDate(){
		  
		DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, 
			new OnDateSetListener() {     
		@SuppressLint("ShowToast") @Override
		public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub	
			
			if(year <10){
				setStrYear("0" + year);
			}else{
				setStrYear("" + year);
			}
			int month = monthOfYear + 1;
			if(month <10){
				setStrMonthOfYear("0" + month);
			}else{
				setStrMonthOfYear("" + month);
			}
			if(dayOfMonth <10){
				setStrDayOfMonth("0" + dayOfMonth);
			}else{
				setStrDayOfMonth("" + dayOfMonth);
			}

			onDateChanged();//监听日期变化
	
		}},mYear, mMonthOfYear, mDayOfMonth);
		datePickerDialog.setTitle(R.string.set_Date);
		datePickerDialog.show();
	}

	 /*
     *接口回调 参数是当前的View
     */
    public interface OnDateChangedListener {
        void onDateChanged(String year,String month,String day);
    }
    /*
     *对外的公开方法 
     */
    public void setOnDateChangedListener(OnDateChangedListener callback){
        mOnDateChangedListener = callback;
    }
     
    /**
     *  numberPicker 变化时候监听函数
     */
    private void onDateChanged() {
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(
            		getStrYear(),getStrMonthOfYear(),getStrDayOfMonth());
        }
    }

    public String getStrYear() {
		return strYear;
	}


	public void setStrYear(String strYear) {
		this.strYear = strYear;
	}


	public String getStrMonthOfYear() {
		return strMonthOfYear;
	}


	public void setStrMonthOfYear(String strMonthOfYear) {
		this.strMonthOfYear = strMonthOfYear;
	}


	public String getStrDayOfMonth() {
		return strDayOfMonth;
	}

}
