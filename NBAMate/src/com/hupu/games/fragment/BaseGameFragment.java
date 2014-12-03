package com.hupu.games.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hupu.games.R;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.view.PinnedHeaderXListView;
import com.hupu.games.view.PinnedHeaderXListView.IXListViewListener;
import com.pyj.http.RequestParams;

public class BaseGameFragment extends BaseFragment {

	protected RequestParams mParams;


	protected View mProgressBar;

	HupuHomeActivity mAct;

	PinnedHeaderXListView listview;

	/** 请求的比赛列表的队列，主要避免了重复请求同一天的比赛 */
	protected ArrayList<String> mListReqQue;

	/** 请求之前的数据的日期参数 */
	protected int preDate;
	/** 请求之后的数据的日期参数 */
	protected int nextDate;

	protected String mToday;

	int mMinDate;

	int mMaxDate;

	/** 请求的日期队列 */
	protected ArrayList<String> mDateList;
	protected ArrayList<String> mBlockTypeList;

	public String mTag;

	protected int headerHeight;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListReqQue = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		HupuLog.d("fb", "onCreateView=" + getClass().getSimpleName()
//				+ "  ;tag=" + mTag);
		View v = inflater.inflate(R.layout.fragment_games, null);
		if(headerHeight==0)
			setHeight();
		   
		mAct = (HupuHomeActivity) getActivity();

		listview = (PinnedHeaderXListView) v
				.findViewById(R.id.list_games);
		mProgressBar = v.findViewById(R.id.probar);

		listview.setXListViewListener(new pullListener());
		listview.setLoadTextEnable(false);
		listview.setRefreshViewEnable(false);
		setAdapter();
		if (mDateList == null || mDateList.size() == 0) {
			reqDefaultData();// 如果没有数据，先请求最新数据
			listview.setPullRefreshEnable(false);
		} else {
			if(mMinDate>0){
				listview.setPullRefreshEnable(preDate>mMinDate);
				listview.setPullLoadEnable(nextDate<mMaxDate);
			}
		}
//		HupuLog.d("fb", " onCreateView ＝" + mTag);
		return v;
	}

	public void setAdapter() {

	}


	/** 请求默认数据 */
	public void reqDefaultData() {

	}

//	/** 下拉和上拉接口 */
//	class PullListener implements IXListViewListener {
//
//		@Override
//		public void onRefresh() {
//			prev();
//		}
//
//		@Override
//		public void onLoadMore() {
//			next();
//		}
//
//
//	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			prev();
		}

		@Override
		public void onLoadMore() {
			next();
		}

	}
	
	/** 下拉请求 */
	public void prev() {
		// req(preDate, -1);
	}

	/** 上拉请求 */
	public void next() {
		// req(nextDate, 1);
	}

	/**
	 * 请求http比赛列表数据
	 * */
	public void req(long date, int direct, int reqType) {
//		HupuLog.d("date=" + date + " dir=" + direct);
		if (date < 0)
			date = 0;
		if (mListReqQue.contains(date + ""))
			return;

		mParams = mAct.getHttpParams(true);
		// 不传时间信息获取最近一天的消息
		if (!mAct.isActiveFragment(this)) {
			mParams.put("preload", "1");
		}
		if (date > 0) {
			mParams.put(BaseEntity.KEY_DAY, "" + date);
			if (direct > 0) {
				mParams.put(BaseEntity.KEY_DIREC, BaseEntity.KEY_NEXT);
			} else if (direct < 0) {
				mParams.put(BaseEntity.KEY_DIREC, BaseEntity.KEY_PREV);
			}
		}
//		mAct.sendAppRequest(reqType, mTag, mParams,
//				new FragmentHttpResponseHandler());		
		if (mAct.sendAppRequest(reqType, mTag, mParams,
				new FragmentHttpResponseHandler()))
			mListReqQue.add(date + "");
		else
			mListReqQue.clear();
	}

	public void itemClick(AdapterView<?> arg0, View v, int pos, long arg3) {

	}

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
			itemClick(arg0, v, --pos, arg3);
		}

	}

	/** 设置listview点击监听器 */
	public void click(View v) {

	}

	class Click implements OnClickListener {
		@Override
		public void onClick(View v) {
			click(v);
		}
	}

	public void onFailure(Throwable error, int reqType) {
		if (listview != null) {
			listview.stopRefresh();
		}
		if(mListReqQue!=null)
			mListReqQue.clear();
		if (mProgressBar != null && mProgressBar.isShown())
			mProgressBar.setVisibility(View.GONE);
	}


	int iPos;
	int iTop;

	@Override
	public void onStop() {

		if (listview != null) {
			iPos = listview.getFirstVisiblePosition();
			if (listview.getCount() >0 && listview.getChildAt(0) != null) 
				try {
					iTop = listview.getChildAt(0).getTop();
				} catch (Exception e) {
					// TODO: handle exception
					iTop = 0;
				}
			else 
				iTop = 0;
			
			listview.stopRefresh();
		}
		super.onStop();

	}
	
	
	private void setHeight()
	{
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		headerHeight = (int)(30*metrics.density);
	}

	SimpleDateFormat format =new SimpleDateFormat("yyyyMMdd");
	protected int getDate(int day,int dif)
	{
		int d=0;
		try {
			HupuLog.e("fb", "input day1="+day);
			Calendar c=Calendar.getInstance();
			c.set(day/10000, (day/100%100)-1, day%100);
			c.roll(c.DATE, dif);
			d=Integer.parseInt(format.format(c.getTime()).toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			HupuLog.e("fb", "input day2="+day);
			d =day+dif;
		}
		return d;
	}

}
