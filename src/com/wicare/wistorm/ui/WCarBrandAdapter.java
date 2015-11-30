package com.wicare.wistorm.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wicare.wistorm.R;
import com.wicare.wistorm.model.CarBrandData; 
import com.wicare.wistorm.toolkit.WCarBrandSelector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WCarBrandAdapter extends BaseAdapter {
	
	
	static final String TAG = "WCarBrandAdapter";
	private Context mContext;
	private List<CarBrandData> mCarBrandDataList = new ArrayList<CarBrandData>();

	
	public WCarBrandAdapter(Context context){
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mCarBrandDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCarBrandDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;  
		ViewHolder viewHolder;  
		if(convertView == null){
			view = LayoutInflater.from(mContext).inflate(R.layout.ws_brand_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.carLetter = (TextView) view.findViewById(R.id.tv_carbrand_list_letter);
			viewHolder.carBrandLogo = (ImageView) view.findViewById(R.id.iv_carbrand_logo);
			viewHolder.carBrand = (TextView)view.findViewById(R.id.tv_carbrand);
			view.setTag(viewHolder);
		}else {
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();//重新获取viewholder 
		}
		
		//根据position获取分类的首字母的Char ascii值   TODO
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.carLetter.setVisibility(View.VISIBLE);
			viewHolder.carLetter.setText(this.mCarBrandDataList.get(position).getLetter());
		}else{
			viewHolder.carLetter.setVisibility(View.GONE);
		}
		
		viewHolder.carBrand.setText(mCarBrandDataList.get(position).getBrand());
		if(new File(WCarBrandSelector.VehicleLogoPath + this.mCarBrandDataList.get(position).getId() + ".png").exists()){
			Bitmap image = BitmapFactory.decodeFile(WCarBrandSelector.VehicleLogoPath+this.mCarBrandDataList.get(position).getId() + ".png");
			viewHolder.carBrandLogo.setImageBitmap(image);
		}else{
			viewHolder.carBrandLogo.setImageResource(R.drawable.icon_car_default);
			if(!"".equals(this.mCarBrandDataList.get(position).getLogoUrl()) && this.mCarBrandDataList.get(position).getLogoUrl() != null){
			}
		}
		return view;
	}

	
	/**
	 * @param carBrandList
	 */
	public void setData(List<CarBrandData> carBrandList) {
		this.mCarBrandDataList = carBrandList;
	}
	
	
	
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mCarBrandDataList.get(position).getLetter().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mCarBrandDataList.get(i).getLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
//	
//	/**
//	 * 提取英文的首字母，非英文字母用#代替。
//	 * 
//	 * @param str
//	 * @return
//	 */
//	private String getAlpha(String str) {
//		String  sortStr = str.trim().substring(0, 1).toUpperCase();
//		// 正则表达式，判断首字母是否是英文字母
//		if (sortStr.matches("[A-Z]")) {
//			return sortStr;
//		} else {
//			return "#";
//		}
//	}
	
	
	
	private class ViewHolder{
		ImageView carBrandLogo;
		TextView  carBrand ;
		TextView  carLetter ;
	}

}
