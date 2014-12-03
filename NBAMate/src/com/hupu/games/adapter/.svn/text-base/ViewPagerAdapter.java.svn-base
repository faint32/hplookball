package com.hupu.games.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {

	private List<View> pageViews;
    int pageSize;
	public ViewPagerAdapter(List<View> pageViews) {
		super();
		this.pageViews = pageViews;
		pageSize=pageViews.size();
		
	}

	// 显示数目
	@Override
	public int getCount() {
		return pageSize;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(pageViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int pos) {
		((ViewPager) arg0).addView(pageViews.get(pos));
		return pageViews.get(pos);
	}

}
