package com.wicare.wistorm.ui;

import com.wicare.wistorm.R;
import com.wicare.wistorm.ui.WTabBar.OnTabChangedListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class WSlidingMenuRightFragment extends Fragment implements OnTabChangedListener{
	
	private View mView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.ws_slidingmenu_right_tabbar, container, false);
		WTabBar ll = (WTabBar) mView. findViewById(R.id.tabbarLayout);
		ll.setOnTabChangedListener(this);
		return mView;
	}

	@Override
	public void onTabClick(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			// add your code 
			Toast.makeText(getActivity(), "选项卡一", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			// add your code
			Toast.makeText(getActivity(), "选项卡二", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			// add your code
			Toast.makeText(getActivity(), "选项卡三", Toast.LENGTH_SHORT).show();
			break;
	
		case 3:
			// add your code
			Toast.makeText(getActivity(), "选项卡四", Toast.LENGTH_SHORT).show();
			break;
	
		default:
			break;
		}
	}

}
