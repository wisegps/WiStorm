//package com.wicare.wistorm.ui;
//
//import java.util.Calendar;
//
//import android.annotation.SuppressLint;
//import android.app.DatePickerDialog;
//import android.app.DatePickerDialog.OnDateSetListener;
//import android.content.Context;
//import android.widget.DatePicker;
//
//import com.wicare.wistorm.R;
//
//
///**
// * @author Wu
// * 
// * 日期选择
// */
//public class WDateSelector {
//
//	private Context mContext;
//	private int mYear, mMonthOfYear, mDayOfMonth;
//	private String strYear,strMonthOfYear,strDayOfMonth;
//	private OnDateChangedListener mOnDateChangedListener;//接口
//
//	public void setStrDayOfMonth(String strDayOfMonth) {
//		this.strDayOfMonth = strDayOfMonth;
//	}
//
//	
//	public WDateSelector(Context context){		
//		mContext = context; 
//		// get current date
//		final Calendar c = Calendar.getInstance();  
//        mYear = c.get(Calendar.YEAR);  
//        mMonthOfYear = c.get(Calendar.MONTH);  
//        mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);  
//        
//	}
//	 
//
//	/**
//	 * 设置的日期
//	 */
//	public void setDate(){
//		  
//		DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, 
//			new OnDateSetListener() {     
//		@SuppressLint("ShowToast") @Override
//		public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
//				// TODO Auto-generated method stub	
//			
//			if(year <10){
//				setStrYear("0" + year);
//			}else{
//				setStrYear("" + year);
//			}
//			int month = monthOfYear + 1;
//			if(month <10){
//				setStrMonthOfYear("0" + month);
//			}else{
//				setStrMonthOfYear("" + month);
//			}
//			if(dayOfMonth <10){
//				setStrDayOfMonth("0" + dayOfMonth);
//			}else{
//				setStrDayOfMonth("" + dayOfMonth);
//			}
//
//			onDateChanged();//监听日期变化
//	
//		}},mYear, mMonthOfYear, mDayOfMonth);
//		datePickerDialog.setTitle(R.string.set_Date);
//		datePickerDialog.show();
//	}
//
//	/**
//     * 接口回调：选择日期变化接口回调
//     */
//    public interface OnDateChangedListener {
//        void onDateChanged(String year,String month,String day);
//    }
//    
//    /**
//     * 对外的公开方法 ：设置日期变化的监听
//     */
//    public void setOnDateChangedListener(OnDateChangedListener callback){
//        mOnDateChangedListener = callback;
//    }
//     
//    /**
//     *  numberPicker 变化时候监听函数
//     */
//    private void onDateChanged() {
//        if (mOnDateChangedListener != null) {
//            mOnDateChangedListener.onDateChanged(
//            		getStrYear(),getStrMonthOfYear(),getStrDayOfMonth());
//        }
//    }
//
//    private String getStrYear() {
//		return strYear;
//	}
//
//
//    private void setStrYear(String strYear) {
//		this.strYear = strYear;
//	}
//
//
//    private String getStrMonthOfYear() {
//		return strMonthOfYear;
//	}
//
//
//    private void setStrMonthOfYear(String strMonthOfYear) {
//		this.strMonthOfYear = strMonthOfYear;
//	}
//
//
//    private String getStrDayOfMonth() {
//		return strDayOfMonth;
//	}
//
//}
