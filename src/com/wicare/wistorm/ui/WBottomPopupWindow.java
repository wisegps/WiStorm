package com.wicare.wistorm.ui;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.wicare.wistorm.R;

/**
 * @author Wu
 * 底部弹窗
 */
public class WBottomPopupWindow extends PopupWindow {
	private Context mContext;
	private ListView lv_pop; //listview popupwindow上面的按键
	private OnItemClickListener onItemClickListener;//按键监听的接口
	private PopupWindow mPopupWindow;
	
	public WBottomPopupWindow(Context context){
		mContext = context;
	}
	
	/**
	 * @param v  A parent view to get the android.view.View.getWindowToken() token from
	 */
	public void initView(View v){
		LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View popunwindwow = mLayoutInflater.inflate(R.layout.ws_buttom_popupwindow,null);
        mPopupWindow = new PopupWindow(popunwindwow, LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        //mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        mPopupWindow.setAnimationStyle(R.style.ws_anim_menu_bottom_bar);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        lv_pop = (ListView)popunwindwow.findViewById(R.id.lv_pop);
	}
	
	
	/**
	 * 隐藏pupupwindow 
	 */
	public void dismiss(){
        mPopupWindow.dismiss();
	}
	
	/**
	 * @param items pupupwindow的按键
	 */
	public void setData(List<String> items){
		lv_pop.setAdapter(new ItemAdapter(items));
	}
	
	/**
	 * @author Wu
	 * popupwindow 按键的点击事件的接口
	 */
	public interface OnItemClickListener{
		public abstract void OnItemClick(int index);
	}
	
	/**
	 * @param onItemClickListener popupwindow 按键点击事件监听
	 */
	public void SetOnItemClickListener(OnItemClickListener onItemClickListener){
		this.onItemClickListener = onItemClickListener;
	}
	
	/**
	 * @author Wu
	 * popupwindow 的按键适配器
	 */
	class ItemAdapter extends BaseAdapter{
		private LayoutInflater layoutInflater;
		List<String> datas;
		public ItemAdapter(List<String> items){
			layoutInflater = LayoutInflater.from(mContext);
			datas = items;
		}
		@Override
		public int getCount() {
			return datas.size();
		}
		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = layoutInflater.inflate(R.layout.ws_buttom_pup_item, null);
	            holder = new ViewHolder();
	            holder.bt_item_pop = (Button) convertView.findViewById(R.id.bt_item_pop);
	            convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.bt_item_pop.setText(datas.get(position));
			holder.bt_item_pop.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if(onItemClickListener != null){
						onItemClickListener.OnItemClick(position);
					}
				}
			});
			return convertView;
		}
		private class ViewHolder {
	        Button bt_item_pop;
	    }
	}
}
