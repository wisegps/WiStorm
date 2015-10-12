package com.wicare.wistorm.ui;

import java.util.Calendar;

import com.wisegps.wistorm.R;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * TimeSelector
 * @author c
 * @date 2015-10-9
 */
public class TimeSelector {

	private Context mContext;
	private int mHour,mMinute;
	
	public TimeSelector(Context context){
		
		mContext = context;
		// 获取系统本机当前的时间 
        final Calendar c = Calendar.getInstance();  
        mHour = c.get(Calendar.HOUR_OF_DAY);  
        mMinute = c.get(Calendar.MINUTE);  
	}
	
	public void setTime(final TextView tv,final EditText ed){
		TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, 
				new OnTimeSetListener() {						   
		    @Override
			public void onTimeSet(TimePicker view,
					int hourOfDay, int minute) {
				// TODO Auto-generated method stub
		    	setmHour(hourOfDay);
		    	setmMinute(minute);
		    	if(tv!=null){
		    		tv.setText(getmHour() + ":" + getmMinute());		
		    	}
		    	if(ed!=null){
		    		ed.setText(getmHour() + ":" + getmMinute());
		    	}
			}
		},mHour, mMinute, true);
		timePickerDialog.setTitle(R.string.set_time);
		timePickerDialog.show();
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
