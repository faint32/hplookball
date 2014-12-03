package com.hupu.games.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.FootballGameActivity;
import com.hupu.games.activity.FootballPlayerInfoActivity;
import com.hupu.games.adapter.FootballEventsListAdapter;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.data.game.football.SoccerOutsReq;

/**
 * 足球赛况，主要是把统计和事件直播加到一起
 * */
@SuppressLint("ValidFragment")
public class SoccerPlayByPlayFragment extends BaseFragment {

	View mProgressBar;

	ListView mListView;
	
	private FootballEventsListAdapter mListAdapter;


	private SoccerOutsReq mEntity;

	/**
	 * 统计项的列数
	 * */
	private int mSize;
	
	private  LayoutInflater mInflater;

	private FootballGameActivity mAct;

	private FootballTimeTask task;

	private Handler timeTaskHandler;

	private int delayTime=30000;

	private boolean startTime;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mInflater = LayoutInflater.from(activity);
		mListAdapter = new FootballEventsListAdapter(activity,new Click());
		mAct = (FootballGameActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Log.d("StatisticFragment", "onCreateView");
		View v = inflater.inflate(R.layout.fragment_football_statistic,
				container, false);
		mViewFooter=null;
		mProgressBar = v.findViewById(R.id.probar);
		mListView = (ListView) v.findViewById(R.id.list_football_statistic);
		
		if (mEntity != null) {
			setData(mEntity,tag);
			//mListView.setVisibility(View.VISIBLE);
		} else {
			reqData();
		}
		return v;
	}


	public void entry() {
		//if (mProgressBar != null) {
		if(mAct!=null)
		{
			reqData();
			if (mListAdapter != null && mListAdapter.getCount() > 0)
				mListView.setSelection(0);
		}
		//}
	}

	public void setData(SoccerOutsReq en,String stag) {
		tag = stag;
		mListView.setVisibility(View.VISIBLE);
		if(mProgressBar!=null)
			mProgressBar.setVisibility(View.GONE);
		mEntity = en;
		if (en.mTitles != null) {
			mSize = en.mTitles.size();
			mListView.setVisibility(View.VISIBLE);
			if(mViewFooter==null)
			{
				buildFootView(en);
				mListView.addFooterView(mViewFooter);
				mListView.setAdapter(mListAdapter);
			}
			updateStatisticData(en);
			mListAdapter.setData(en.mLiveDatas);
			if (en.scoreBoard.code == ScoreboardEntity.STATUS_END)
				EndTimer();
			else {
				// delayTime =30000;
				startTimer();
			}
		}
	}

	public void clear() {
		mSize = 0;
		mEntity = null;
		if (mListAdapter != null)
			mListAdapter.notifyDataSetChanged();
	}

	class Holder {
		TextView txtLeft;
		TextView txtRight;
		TextView txtMid;
	}

	/**初始化统计View*/
	LinearLayout mViewFooter;
	void buildFootView(SoccerOutsReq en)
	{
		if(mViewFooter ==null && getActivity()!=null)
		{
			mViewFooter =  new LinearLayout(getActivity());
			mViewFooter.setOrientation(LinearLayout.VERTICAL);
		
			 LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(     
	                   LinearLayout.LayoutParams.MATCH_PARENT,     
	                   LinearLayout.LayoutParams.WRAP_CONTENT     
	           ); 
			 TextView title=  new TextView(getActivity());
			 title.setTextColor(Color.WHITE);
			 title.setGravity(Gravity.CENTER);
			 title.setText("球队数据");
			 title.setPadding(0, 6, 0, 6);
			 title.setBackgroundColor(Color.BLACK);
			 title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			 mViewFooter.addView(title, p);
			for(int i =0;i<mSize;i++)
			{
				View convertView=mInflater.inflate(
						R.layout.item_football_statistic, null);
				Holder item= new Holder();
				item.txtLeft = (TextView) convertView
						.findViewById(R.id.txt_left);
				item.txtRight = (TextView) convertView
						.findViewById(R.id.txt_right);

				item.txtMid = (TextView) convertView.findViewById(R.id.txt_mid);
				convertView.setTag(item);	
				mViewFooter.addView(convertView,p);
			}
		}
	}
	
	/**设置足球统计数据
	 * */
	void updateStatisticData(SoccerOutsReq en)
	{

		if(mViewFooter!=null)
		{
			Holder item;
			View v;
			for(int i =0;i<mSize;i++)
			{
				v=mViewFooter.getChildAt(i+1);
				item=(Holder)v.getTag();
				item.txtLeft.setText(mEntity.mHomeValue.get(i));
				item.txtRight.setText(mEntity.mAwayValue.get(i));
				item.txtMid.setText(mEntity.mTitles.get(i));
				if ((i & 1) == 0) {
					v.setBackgroundResource(R.color.football_statistic_down);
				} else {
					v.setBackgroundResource(R.color.football_statistic_up);
				}
			}
		}
			
	}

	
	@Override
	public void onDetach() {
		super.onDetach();
		if(mViewFooter!=null)
			mViewFooter.removeAllViews();
		mViewFooter=null;
	}

	private void startTimer() {
		startTime = true;
		if (timeTaskHandler == null) {
			timeTaskHandler = new Handler();
		}
		task = new FootballTimeTask();
		timeTaskHandler.postDelayed(task, delayTime);
	}

	private void EndTimer() {
		if (timeTaskHandler != null) {
			timeTaskHandler.removeCallbacks(task);
			timeTaskHandler = null;
		}
		startTime = false;
	}

	private void reqData() {
		mAct.req();
	}

	class FootballTimeTask implements Runnable {
		@Override
		public void run() {
			reqData();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EndTimer();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (timeTaskHandler != null)
			timeTaskHandler.removeCallbacks(task);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (startTime)
			startTimer();
	}
	private String tag;
	//战况  中点击event 事件中的
	public class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pid = 0;
			if ("".equals(v.getTag().toString()) && v.getTag() !=null) {
				pid = 0;
			}else {
				pid = Integer.parseInt(v.getTag().toString());
			}
			
			if (pid != 0) {
				Intent in = new Intent(getActivity(), FootballPlayerInfoActivity.class);
				in.putExtra("pid", pid);
				in.putExtra("tag", tag);
				startActivity(in);
			}
			
		}
		
	}
}
