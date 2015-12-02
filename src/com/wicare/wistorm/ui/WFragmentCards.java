package com.wicare.wistorm.ui;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.wicare.wistorm.R;
import com.wicare.wistorm.toolkit.DensityUtil;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 卡片管理类 FragmentCards
 * 
 * @author c
 * @date 2015-12-2
 */
public class WFragmentCards extends Fragment {

	// 根视图
	private View rootView;
	// 卡片容器
	private LinearLayout ll_cards;
	// 碎片管理
	private FragmentManager fragmentManager;
	// 开启增删业务
	private FragmentTransaction transaction;
	// 所有卡片
	Map<String, Fragment> cards = new LinkedHashMap<String, Fragment>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_cards, container,
					false);
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (null != parent) {
				parent.removeView(rootView);
			}
		}
		
	
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ll_cards = (LinearLayout) getActivity().findViewById(R.id.ll_cards);
		fragmentManager = this.getChildFragmentManager();
		
	}

	/**
	 * 添加一个卡片 addCard
	 * 
	 * @param fragment
	 *            自定义的卡片fragment
	 * @param tag
	 *            设置一个标签
	 */
	public void addCard(Fragment fragment, String tag) {
		remove(tag);
		transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.ll_cards, fragment, tag);
		cards.put(tag, fragment);
		transaction.commit();
	}

	/**
	 * 根据tag移除一个卡片 remove
	 * 
	 * @param tag
	 */
	public void remove(String tag) {
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		if (fragment != null && fragment.isAdded()) {
			fragmentManager.beginTransaction().remove(fragment).commit();
			if(cards.containsKey(tag)){
				cards.remove(tag);
			}
			
		}
	}

	/**
	 * 删除所有卡片 
	 * removeAll
	 */
	public void removeAll() {
	String[] carNames =  cards.keySet().toArray(new String[0]);
		for(int i=0;i<carNames.length;i++){
			remove(carNames[i]);
		}

	}

	/**
	 * 根据tag置顶 top
	 * 
	 * @param tag
	 */
	public void top(String tag) {
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		View view = fragment.getView();
		float y = view.getY();
		int height = view.getHeight();

		Log.i("top", "当前卡片的顶部坐标:" + y);

		Log.i("top", "当前卡片高度:" + height);

		Iterator it = cards.keySet().iterator();

		float margin = DensityUtil.dip2px(this.getActivity(), 10);
		float base = DensityUtil.dip2px(this.getActivity(), 10);

		while (it.hasNext()) {
			String key = (String) it.next();
			Fragment fg = fragmentManager.findFragmentByTag(key);
			View otherView = fg.getView();
			float otherY = otherView.getY();
			if (otherY < y) {
				ObjectAnimator otherAnimator = ObjectAnimator.ofFloat(
						otherView, "y", otherY, otherY + height + margin);
				otherAnimator.setDuration(1000);
				otherAnimator.start();
			}
		}
		ObjectAnimator bbjectAnimator = ObjectAnimator.ofFloat(view, "y", y,
				base);

		bbjectAnimator.setDuration(1000);

		bbjectAnimator.start();

	}
}
