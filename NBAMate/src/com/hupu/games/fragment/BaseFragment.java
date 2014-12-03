package com.hupu.games.fragment;

import java.util.LinkedList;

import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.JsonPaserFactory;
import com.hupu.games.data.LeaguesEntity;
import com.pyj.http.AsyncHttpResponseHandler;
import com.pyj.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

public class BaseFragment extends Fragment {
	protected boolean bQuit;

	protected HupuHomeActivity mAct;

	public boolean bBlank;


	protected RequestParams mParams;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof HupuHomeActivity)
			mAct = (HupuHomeActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		HupuLog.d("onCreate", getClass().getName());
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (bQuit)
			// ((BaseActivity)getActivity()).quit();
			bBlank = true;
	}

	/**
	 * 由其他的页面切换进入
	 * */
	public void entry() 
	{
		
	}
	public void runTimer() {
		// TODO Auto-generated method stub
		
	}

	public void onSuccess(Object content, int reqType) {

	}
	

	public void onFailure(Throwable error, int reqType) {

	}
	class FragmentHttpResponseHandler extends AsyncHttpResponseHandler {

	    Object mObj;
		public void setObj(Object o){
			mObj=o;
		}
		@Override
		public void onSuccess(String content, int reqType) {
			BaseEntity entity =JsonPaserFactory.paserObj(content, reqType);
			BaseFragment.this.onSuccess( entity,  reqType) ;
		}

		@Override
		public void onFailure(Throwable error, int reqType) {
			BaseFragment.this.onFailure( error,  reqType) ;
		}
	}

}
