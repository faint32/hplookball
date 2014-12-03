package com.hupu.games.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.FootballGameActivity;
import com.hupu.games.activity.FootballPlayerInfoActivity;
import com.hupu.games.adapter.FootballEventsListAdapter;
import com.hupu.games.data.game.football.SoccerEventsResp;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.view.XListView.IXListViewListener;

/**
 * 直播fragment
 * */
@SuppressLint("ValidFragment")
public class FootballLiveFragment extends BaseFragment {

	private ListView mLvLive;

	private FootballEventsListAdapter mListAdapter;

	private View mProgressBar;

	FootballGameActivity mAct;

	TextView nodata,nodata2;

	boolean bGetData;

	FootballTimeTask task;

	Handler timeTaskHandler;

	private int delayTime=30000;

	private boolean startTime;

	SoccerEventsResp liveData;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAct = (FootballGameActivity) activity;
		mListAdapter = new FootballEventsListAdapter(mAct,new Click());
	}

	boolean start;

	/** 比赛是否开始了 */
	public void isStart(boolean s) {
		start = s;
		if (start && nodata != null){
			nodata.setVisibility(View.GONE);
			nodata2.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setUserVisibleHint(true);
	}

	public void entry() {
		if(mAct!=null)
		{
			reqData();
			if(bGetData && mListAdapter.getCount() >0)
				mLvLive.setSelection(0);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_football_live, container,
				false);
		mProgressBar = v.findViewById(R.id.probar);
		nodata = (TextView)v.findViewById(R.id.txt_no_data);
		nodata2 = (TextView)v.findViewById(R.id.txt_no_data2);
		
		if (bGetData  || start)
			mProgressBar.setVisibility(View.GONE);
		if (start){
			nodata.setVisibility(View.GONE);
			nodata2.setVisibility(View.GONE);
		}
        if(liveData ==null)
        	reqData();
		mLvLive = (ListView) v.findViewById(R.id.list_live);
		mLvLive.setAdapter(mListAdapter);
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	public void setData(SoccerEventsResp data,String stag) {
		tag = stag;
		liveData = data;
		bGetData = true;
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		if (mListAdapter != null) {
			mListAdapter.setData(liveData.mDatas);
		}
		if(mListAdapter.getCount() ==0  )
		{
			nodata.setVisibility(View.VISIBLE);
			nodata2.setVisibility(View.VISIBLE);
		}
		else if(mListAdapter.getCount() ==1  )
		{
			//只有比赛开始事件的时候
			nodata.setVisibility(View.VISIBLE);
			nodata2.setVisibility(View.VISIBLE);
		}
		else
		{
			nodata.setVisibility(View.GONE);
			nodata2.setVisibility(View.GONE);
		}
		
		if (liveData.scoreBoard.code == ScoreboardEntity.STATUS_END)
			EndTimer();
		else {
			// delayTime =30000;
			startTimer();
		}
		if (liveData.scoreBoard.code == ScoreboardEntity.STATUS_START
				|| liveData.scoreBoard.code == ScoreboardEntity.STATUS_END)
			isStart(true);
	}

	/** 网络请求失败后重试 */
	private void retry() {
		startTimer();
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

	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {

		}

		@Override
		public void onLoadMore() {

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
