package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.LinkedList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 
 * */
public class HupuPagerAdapter extends FragmentStatePagerAdapter {

	private LinkedList<Fragment> fragments;

	public HupuPagerAdapter(FragmentActivity fa) {
		super(fa.getSupportFragmentManager());
		fragments = new LinkedList<Fragment>();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	public void addFragment(Fragment f) {
		fragments.add(f);
	}
}
