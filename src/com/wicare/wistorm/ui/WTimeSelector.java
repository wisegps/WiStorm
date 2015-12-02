package com.wicare.wistorm.ui;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.TimePicker;

import com.wicare.wistorm.R;


/**
 * TimeSelector
 * @author c
 * @date 2015-10-9
 * 
 */
public class WTimeSelector {

	private Context mContext;
	private int mHour,mMinute;
	
	private String strHour,strMinute;
	
	

	private OnTimeChangedListener mOnTimeChangedListener;
	public WTimeSelector(Context context){
		
		mContext = context;
		// get current 
        final Calendar c = Calendar.getInstance();  
        mHour = c.get(Calendar.HOUR_OF_DAY);  
        mMinute = c.get(Calendar.MINUTE);  
	}
	
	/**
	 * @param tv 
	 * @param ed
	 */
	public void setTime(){
		
		TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, 
				new OnTimeSetListener() {						   
		    @Override
			public void onTimeSet(TimePicker view,
					int hourOfDay, int minute) {
				
		    	if(hourOfDay<10){
		    		setStrHour("0" + hourOfDay);
		    	}else{
		    		setStrHour("" + hourOfDay);
		    	}
		    	if(minute<10){
		    		setStrMinute("0" + minute);
		    	}else{
		    		setStrMinute("" + minute);
		    	}
		    	onDateChanged();//监听时间变化
			}
		},mHour, mMinute, true);
		timePickerDialog.setTitle(R.string.set_time);
		timePickerDialog.show();
	}
	
	
	 /*
     *接口回调 参数是当前的View
     */
    public interface OnTimeChangedListener {
        void onTimeChanged(String hour,String minute);
    }
    /*
     *对外的公开方法 
     */
    public void setOnTimeChangedListener(OnTimeChangedListener callback){
        mOnTimeChangedListener = callback;
    }
     
    /**
     *  numberPicker 变化时候监听函数
     */
    private void onDateChanged() {
        if (mOnTimeChangedListener != null) {
            mOnTimeChangedListener.onTimeChanged(getStrHour(),getStrMinute());
        }
    }

    public String getStrHour() {
		return strHour;
	}

	public void setStrHour(String strHour) {
		this.strHour = strHour;
	}

	public String getStrMinute() {
		return strMinute;
	}

	public void setStrMinute(String strMinute) {
		this.strMinute = strMinute;
	}
}
