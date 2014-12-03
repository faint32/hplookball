package com.hupu.games.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.FootballGameActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.game.football.FootballStatisticResp;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.fragment.FootballLiveFragment.FootballTimeTask;

/**
 * 统计页面
 * */
@SuppressLint("ValidFragment")
public class FootballStatisticFragment extends BaseFragment {

	View mProgressBar;

	ListView mListView;
	ListAdapter adapter;
	// TextView titleLeft;
	// TextView titleRight;
	// TextView titleMid;

	FootballStatisticResp entity;

	boolean bGetData;
	int size;
	LayoutInflater mInflater;

	FootballGameActivity mAct;

	FootballTimeTask task;

	Handler timeTaskHandler;

	private int delayTime=30000;

	private boolean startTime;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mInflater = LayoutInflater.from(activity);
		adapter = new ListAdapter();
		mAct = (FootballGameActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Log.d("StatisticFragment", "onCreateView");
		View v = inflater.inflate(R.layout.fragment_football_statistic,
				container, false);

		mProgressBar = v.findViewById(R.id.probar);
		mListView = (ListView) v.findViewById(R.id.list_football_statistic);
		// titleLeft = (TextView) v.findViewById(R.id.txt_title_left);
		// titleRight = (TextView) v.findViewById(R.id.txt_title_right);
		// titleMid = (TextView) v.findViewById(R.id.txt_title_right);
		mListView.setAdapter(adapter);

		if (entity != null) {
			setData(entity);
		} else {
			reqData();
		}
		return v;
	}

	/***/
	public void entry() {

		if (mProgressBar != null) {
			reqData();
			if (adapter != null && adapter.getCount() > 0)
				mListView.setSelection(0);
		}
	}

	public void setData(FootballStatisticResp en) {
		entity = en;

		if (en.mTitles != null) {
			mProgressBar.setVisibility(View.GONE);
			size = en.mTitles.size();
			// titleLeft.setText(entity.homeValue.get(0));
			// titleRight.setText(entity.awayValue.get(0));
			// titleMid.setText(entity.mTitles.get(0));
			adapter.notifyDataSetChanged();
			if (en.scoreBoard.code == ScoreboardEntity.STATUS_END)
				EndTimer();
			else {
				// delayTime =30000;
				startTimer();
			}
		}
	}

	public void clear() {
		size = 0;
		entity = null;
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}


	private class ListAdapter extends BaseAdapter {

		class Holder {
			TextView txtLeft;
			TextView txtRight;
			TextView txtMid;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder item;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_football_statistic, null);
				item = new Holder();
				item.txtLeft = (TextView) convertView
						.findViewById(R.id.txt_left);
				item.txtRight = (TextView) convertView
						.findViewById(R.id.txt_right);

				item.txtMid = (TextView) convertView.findViewById(R.id.txt_mid);
				convertView.setTag(item);
			} else {
				item = (Holder) convertView.getTag();
			}

			item.txtLeft.setText(entity.homeValue.get(position));
			item.txtRight.setText(entity.awayValue.get(position));
			item.txtMid.setText(entity.mTitles.get(position));
			if ((position & 1) == 0) {
				convertView.setBackgroundResource(R.color.football_statistic_down);
			} else {
				convertView.setBackgroundResource(R.color.football_statistic_up);
			}
			return convertView;
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

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
}
