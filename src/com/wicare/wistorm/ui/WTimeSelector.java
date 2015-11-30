package com.wicare.wistorm.ui;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wicare.wistorm.R;
import com.wicare.wistorm.ui.WDateSelector.OnDateChangedListener;


/**
 * TimeSelector
 * @author c
 * @date 2015-10-9
 * 
 */
public class WTimeSelector {

	private Context mContext;
	private int mHour,mMinute;
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
				// TODO Auto-generated method stub
		    	setmHour(hourOfDay);
		    	setmMinute(minute);
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
            mOnTimeChangedListener.onTimeChanged(
            		String.valueOf(getmHour()),String.valueOf(getmMinute()));
        }
    }

	
	

	private int getmHour() {
		return mHour;
	}

	private void setmHour(int mHour) {
		this.mHour = mHour;
	}

	private int getmMinute() {
		return mMinute;
	}

	private void setmMinute(int mMinute) {
		this.mMinute = mMinute;
	}
}
