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
			setmYear(year);
			setmMonthOfYear(monthOfYear + 1);
			setmDayOfMonth(dayOfMonth);
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
            		String.valueOf(getmYear()),String.valueOf(getmMonthOfYear()),String.valueOf(getmDayOfMonth()));
        }
    }

	
	public int getmYear() {
		return mYear;
	}

	public void setmYear(int mYear) {
		this.mYear = mYear;
	}

	public int getmMonthOfYear() {
		return mMonthOfYear;
	}

	public void setmMonthOfYear(int mMonthOfYear) {
		this.mMonthOfYear = mMonthOfYear;
	}

	public int getmDayOfMonth() {
		return mDayOfMonth;
	}

	public void setmDayOfMonth(int mDayOfMonth) {
		this.mDayOfMonth = mDayOfMonth;
	}

}
