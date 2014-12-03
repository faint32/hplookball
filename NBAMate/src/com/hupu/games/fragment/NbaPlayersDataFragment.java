package com.hupu.games.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.NBAPlayerInfoActivity;
import com.hupu.games.adapter.NbaTeamPlayersDataAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.basketball.NbaPlayersDataReq;
import com.hupu.games.data.game.basketball.NbaPlayersDataReq.PlayerDataEntity;
import com.hupu.games.view.HScrollView;
import com.hupu.games.view.HScrollView.ScrollViewObserver1;

/**
 * 球队球员数据页
 * */
public class NbaPlayersDataFragment extends BaseFragment {

	NbaTeamPlayersDataAdapter mAdapter;

	ListView mListView;

	ListClick listClick;

	int i_tid;

	View progress;

	RelativeLayout mHead;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (i_tid == 0)
			i_tid = getArguments().getInt("tid");
	}

	int txtCol;
	LayoutInflater mLayoutInflater;
	int txtWidth;
	int txtHeight;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		txtCol = activity.getResources().getColor(R.color.res_cor1);
		mLayoutInflater = LayoutInflater.from(activity);
		txtWidth = activity.getResources().getDimensionPixelSize(
				R.dimen.txt_player);
		txtHeight = activity.getResources().getDimensionPixelSize(
				R.dimen.txt_player_height);
	}

	ScrollViewObserver1 sob;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_nba_player_data, container,
				false);
		mListView = (ListView) v.findViewById(R.id.list_players);
		mListView.setOnItemClickListener(new ListClick());
		progress = v.findViewById(R.id.probar);

		mListView.setOnTouchListener(new ListViewTouchLinstener());
		mHead = (RelativeLayout) v.findViewById(R.id.head);

		if (mAdapter == null) {
			sob = new ScrollViewObserver1();
			mAdapter = new NbaTeamPlayersDataAdapter(getActivity(), sob);
		}
		sob.clear();
		((HScrollView) mHead.findViewById(R.id.hscrollview)).setNoHeader(sob);
		mListView.setAdapter(mAdapter);
		if (mData != null) {
			setData(mData);
		}
		return v;
	}

	TextView txtHeaderName;

	private void buildHeader() {
		txtHeaderName = (TextView) mHead.findViewById(R.id.txt_player_name);
		txtHeaderName.setText(mData.headerValues[0]);
		LinearLayout container = (LinearLayout) mHead
				.findViewById(R.id.layout_containter);
		container.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				txtWidth, txtHeight);
		params.gravity = Gravity.CENTER_VERTICAL;
		TextView[] tvs = new TextView[mData.headerValues.length - 1];
		for (int i = 0; i < mData.headerValues.length - 1; i++) {
			tvs[i] = buildTextView();
			tvs[i].setText(mData.headerValues[i + 1]);
			container.addView(tvs[i], params);
		}
	}

	/**
	 * @param type
	 *            0 表示标题 1表示数据
	 * */
	private TextView buildTextView() {
		TextView tv = (TextView) mLayoutInflater.inflate(
				R.layout.txt_player_data, null);
		return tv;
	}

	boolean getData;
	NbaPlayersDataReq mData;
	int size;

	/** 重新刷新数据 */
	public void setData(NbaPlayersDataReq data) {

		mData = data;
		if (mAdapter != null) {
			mHead.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			if (data.headerKeys != null) {
				buildHeader();
				mAdapter.setData(data.mDataList);
			}

		}

	}

	@Override
	public void entry() {
		super.entry();

	}

	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			((HupuBaseActivity) getActivity()).sendUmeng(
					HuPuRes.UMENG_EVENT_NBAPLAYERS, HuPuRes.UMENG_KEY_ENTRANCE,
					HuPuRes.UMENG_VALUE_NBA_PLAYER_REVIEW);

			PlayerDataEntity entity = mAdapter.getItem(pos);
			Intent in = new Intent(getActivity(), NBAPlayerInfoActivity.class);
			in.putExtra("pid", entity.player_id);
			startActivity(in);

		}

	}

	class ListViewTouchLinstener implements View.OnTouchListener {

		float historicX = Float.NaN;
		static final int DELTA_ON_CLICK = 20;

		float x0;
		float y0;

		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(View arg0, MotionEvent event) {
			// 当在listView控件上touch时，将这个touch的事件分发给 ScrollView
			// HupuLog.d("ListViewTouchLinstener",
			// "action="+event.getAction()+"  X="+event.getX()+" y="+event.getY());
			HorizontalScrollView scrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.hscrollview);
			scrollView.onTouchEvent(event);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x0 = event.getX();
				y0 = event.getY();
				break;

			case MotionEvent.ACTION_UP:
				float cx = event.getX();
				float cy = event.getY();
				if (Math.abs(cx - x0) > DELTA_ON_CLICK
						|| Math.abs(cy - y0) > DELTA_ON_CLICK)
					return true;
				break;

			}
			return false;
		}
	}

}
