package com.hupu.games.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableLayout;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.NBAGameActivity.BoxscoreDatas;
import com.hupu.games.activity.NBAPlayerInfoActivity;
import com.hupu.games.adapter.GameDataListAdapter;
import com.hupu.games.adapter.GameDataListAdapter.TagData;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.PlayerEntity;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.personal.box.BoxScoreResp;
import com.hupu.games.view.PinnedHeaderXListView;

/**
 * 统计页面
 * */
@SuppressLint("ValidFragment")
public class StatisticFragment extends BaseBasketballFragment {

	private int i_homeId;
	private int i_awayId;

	private BoxScoreResp mData;

	PlayerClick mClick;

	public StatisticFragment(BasketballGameEntity entityGame) {
		super();
		i_homeId = entityGame.i_home_tid;
		i_awayId = entityGame.i_away_tid;
		homeName = HuPuApp.getTeamData(i_homeId).str_name;
		awayName = HuPuApp.getTeamData(i_awayId).str_name;
		iHomeSeries=entityGame.home_series;
		iAwaySeries=entityGame.away_series;
	}

	public StatisticFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Log.d("StatisticFragment", "onCreateView");
		View v = inflater.inflate(R.layout.fragment_statistics, container,
				false);

		mProgressBar = v.findViewById(R.id.probar);
		mTable = (TableLayout) v.findViewById(R.id.table_score);
		mLvDatas = (PinnedHeaderXListView) v.findViewById(R.id.list_players);
		mLvDatas.setPullLoadEnable(false);
		mLvDatas.setPullRefreshEnable(false);
		touchListener = new ListViewTouchLinstener();
		initGesture();
		if (mClick == null)
			mClick = new PlayerClick();
		mDataListAdapter = new GameDataListAdapter(getActivity(), i_homeId,
				i_awayId, this, mClick);

		mLvDatas.setOnTouchListener(touchListener);
		mLvDatas.setAdapter(mDataListAdapter);
		mLvDatas.setOnItemClickListener(new ListClick());

		initRow();
		if (bGetData) {
			setData(mData, false);
		}
		return v;
	}

	public void setData(BoxScoreResp data, boolean byMan) {
		bGetData = true;
		if (mProgressBar != null && !byMan)
			mProgressBar.setVisibility(View.GONE);
		mData = data;
		// 需要先设置title
		if (data != null && mLvDatas != null) {

			mLvDatas.setVisibility(View.VISIBLE);
			mTable.setVisibility(View.VISIBLE);
			mDataListAdapter.setData(data);
			setTableData(data.mEntityHome, data.mEntityAway);
		}
	}

	public void updateData(BoxScoreResp data) {

		setTableData(data.mEntityHome, data.mEntityAway);
		mDataListAdapter.updateData(data);
	}

	public void updateBoxScoreData(BoxscoreDatas boxscoreData) {
		if (mDataListAdapter != null)
			mDataListAdapter.updatemBoxscoreDatas(boxscoreData);
	}

	public void copyTableData(boolean home, String key, String value) {
		// Log.d(key, value);
		if (home)
			mData.mEntityHome.mapScore.put(key, value);
		else
			mData.mEntityAway.mapScore.put(key, value);
	}

	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {

			PlayerEntity data = (PlayerEntity) mDataListAdapter.getItemAt(pos);
			if (data != null) {

				int id = -1;
				try {
					id = Integer.parseInt(data.str_player_id);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (id > 0) {
					((HupuBaseActivity) getActivity()).sendUmeng(
							HuPuRes.UMENG_EVENT_NBAPLAYERS,
							HuPuRes.UMENG_KEY_ENTRANCE,
							HuPuRes.UMENG_VALUE_NBA_BOX_SCORE);
					Intent in = new Intent(getActivity(),
							NBAPlayerInfoActivity.class);
					in.putExtra("pid", id);
					startActivity(in);
				}

			}

		}
	}

	class PlayerClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			final TagData tag = (TagData) v.getTag();
			if (tag != null && tag.entity!=null) {
				int id = -1;
				try {
					id = Integer.parseInt(tag.entity.str_player_id);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (id > 0) {
					((HupuBaseActivity) getActivity()).sendUmeng(
							HuPuRes.UMENG_EVENT_NBAPLAYERS,
							HuPuRes.UMENG_KEY_ENTRANCE,
							HuPuRes.UMENG_VALUE_NBA_BOX_SCORE);
					Intent in = new Intent(getActivity(),
							NBAPlayerInfoActivity.class);
					in.putExtra("pid", id);
					startActivity(in);
					//
					tag.view.setSelected(true);
					tag.view.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							tag.view.setSelected(false);						
						}
					}, 80);
				}
			}

		}
	}

}
