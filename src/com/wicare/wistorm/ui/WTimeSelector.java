package com.wicare.wistorm.ui;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wisegps.wistorm.R;

public class WTimeSelector {
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
			// get current 
	        final Calendar c = Calendar.getInstance();  
	        mHour = c.get(Calendar.HOUR_OF_DAY);  
	        mMinute = c.get(Calendar.MINUTE);  
		}
		
		/**
		 * @param tv 
		 * @param ed
		 */
		public void setTime(final TextView textView,final EditText editText,final Button button){
			
			TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, 
					new OnTimeSetListener() {						   
			    @Override
				public void onTimeSet(TimePicker view,
						int hourOfDay, int minute) {
					// TODO Auto-generated method stub
			    	setmHour(hourOfDay);
			    	setmMinute(minute);
			    	if(textView!=null){
			    		textView.setText(getmHour() + ":" + getmMinute());		
			    	}
			    	if(editText!=null){
			    		editText.setText(getmHour() + ":" + getmMinute());
			    	}
			    	if(button !=null){
			    		button.setText(getmHour() + ":" + getmMinute());
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

}
