package com.hupu.games.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.FootballCoachInfoActivity;
import com.hupu.games.activity.FootballGameActivity;
import com.hupu.games.activity.FootballPlayerInfoActivity;
import com.hupu.games.activity.PlayerRatingActivity;
import com.hupu.games.activity.PlayersRatingActivity;
import com.hupu.games.adapter.FootballLineupListAdapter;
import com.hupu.games.adapter.LineupMapListAdapter;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.FormationEntity;
import com.hupu.games.data.LineupEntity;
import com.hupu.games.data.TeamLineupResp;
import com.hupu.games.view.HupuPinnedHeaderListView;

/**
 * 阵容界面
 * */
public class FootballLineupFragment extends BaseBasketballFragment {

	HupuPinnedHeaderListView mListView;
	private GridView mHeaderListView;
	LineupMapListAdapter mHeaderAdapter;
	FootballLineupListAdapter mAdapter;
	View header;
	Button btnHome, btnAway;
	TeamLineupResp lineupResp;
	private int teamType = 1;
	ProgressBar pBar;
	TextView noData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_football_lineup, container,
				false);
		btnHome = (Button) v.findViewById(R.id.btn_home);
		btnAway = (Button) v.findViewById(R.id.btn_away);
		btnHome.setOnClickListener(new Click());
		btnAway.setOnClickListener(new Click());
		pBar = (ProgressBar) v.findViewById(R.id.probar);
		noData = (TextView) v.findViewById(R.id.txt_no_data);
		mListView = (HupuPinnedHeaderListView) v.findViewById(R.id.lineup_list);

		header = LayoutInflater.from(getActivity()).inflate(
				R.layout.item_lineup_header, null);
		mHeaderListView = (GridView) header.findViewById(R.id.list_lineup);
		mHeaderAdapter = new LineupMapListAdapter(getActivity());
		mListView.addHeaderView(header);
		mAdapter = new FootballLineupListAdapter(getActivity());
		pBar.setVisibility(View.VISIBLE);
		reqNewData();

		mHeaderListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				for (FormationEntity pleryInfo : teamType == 1 ? lineupResp.homeEntity.formationList
						: lineupResp.awayEntity.formationList) {
					int pos = pleryInfo.coordinateY * 5 + pleryInfo.coordinateX;
					if (pos == position) {
						Intent headerIntent;
						if (pleryInfo.rating_oid != 0) {
							headerIntent = new Intent(getActivity(),
									PlayerRatingActivity.class);
							headerIntent.putExtra("oid", pleryInfo.rating_oid);
							headerIntent.putExtra("obj_type", 1);
						} else {
							headerIntent = new Intent(getActivity(),
									FootballPlayerInfoActivity.class);
						}
						
						if (pleryInfo.player_id != 0) {
							headerIntent.putExtra("tag",
									((FootballGameActivity) getActivity())
											.getTag());
							headerIntent.putExtra("pid", pleryInfo.player_id);
							
							startActivity(headerIntent);
						}
						break;
					}
				}

			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LineupEntity entity = mAdapter.getItemAt(position - 1);
				if (entity == null) {
					return;
				} else {
					Intent in;
					if (entity.rating_oid != 0) {
						in = new Intent(getActivity(),
								PlayerRatingActivity.class);
						in.putExtra("oid", entity.rating_oid);
						in.putExtra("tag",
								((FootballGameActivity) getActivity()).getTag());
						in.putExtra("obj_type", entity.type <= 2?1:3);
						
					} else {
						in = new Intent(
								getActivity(),
								entity.type == 3 ? FootballCoachInfoActivity.class
										: FootballPlayerInfoActivity.class);
					}
					
					HupuLog.e("papa", "playerId========="+entity.player_id);
					if (entity.player_id != 0) {
						in.putExtra("tag",
								((FootballGameActivity) getActivity()).getTag());
						in.putExtra("pid", entity.player_id);
						startActivity(in);
					}
				}
			}
		});
		return v;
	}

	private class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			btnAway.setBackgroundResource(R.drawable.btn_rank_type_selector);
			btnHome.setBackgroundResource(R.drawable.btn_rank_type_selector);
			switch (v.getId()) {
			case R.id.btn_home:
				btnHome.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				if (teamType != 1) {
					teamType = 1;
					if (lineupResp != null && lineupResp.homeEntity != null
							&& lineupResp.homeEntity.lineupList != null
							&& lineupResp.homeEntity.formationList != null) {

						mHeaderAdapter.setData(
								lineupResp.homeEntity.formationList, teamType);
						mHeaderAdapter.notifyDataSetChanged();
						mHeaderListView.setAdapter(mHeaderAdapter);
						mAdapter = new FootballLineupListAdapter(getActivity());
						mAdapter.setData(lineupResp.homeEntity.lineupList,
								lineupResp.homeEntity.formation_type);
						mListView.setAdapter(mAdapter);
						mListView.setVisibility(View.VISIBLE);
						checkData();
					}
				}
				break;

			case R.id.btn_away:
				btnAway.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				if (teamType != 2) {
					teamType = 2;
					if (lineupResp != null && lineupResp.awayEntity != null
							&& lineupResp.awayEntity.lineupList != null
							&& lineupResp.awayEntity.formationList != null) {
						mHeaderAdapter.setData(
								lineupResp.awayEntity.formationList, teamType);
						mHeaderAdapter.notifyDataSetChanged();
						mHeaderListView.setAdapter(mHeaderAdapter);
						mAdapter = new FootballLineupListAdapter(getActivity());
						mAdapter.setData(lineupResp.awayEntity.lineupList,
								lineupResp.awayEntity.formation_type);
						mListView.setAdapter(mAdapter);
						mListView.setVisibility(View.VISIBLE);
						checkData();
					}
				}

				break;

			default:
				break;
			}

		}

	}

	/** 获取最新数据 */
	public void reqNewData() {
		((FootballGameActivity) getActivity()).getLineupList();
	}

	/**
	 * 检测是否需要线上无阵容文字
	 */
	private void checkData() {
		if (lineupResp.homeEntity.formationList != null
				&& lineupResp.homeEntity.formationList.size() > 0) {
			mListView.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
		} else {
			mListView.setVisibility(View.GONE);
			noData.setText(Html.fromHtml(lineupResp.preview));
			noData.setVisibility(View.VISIBLE);
		}
	}

	public void onReqResponse(Object o, int methodId, int mQid) {
		pBar.setVisibility(View.GONE);
		if (o != null) {
			lineupResp = (TeamLineupResp) o;
			if (lineupResp.homeEntity != null) {
				btnAway.setText(lineupResp.awayEntity.name);
				btnHome.setText(lineupResp.homeEntity.name);
				teamType = 1;
				mHeaderAdapter.setData(lineupResp.homeEntity.formationList,
						teamType);
				mHeaderListView.setAdapter(mHeaderAdapter);
				mAdapter.setData(lineupResp.homeEntity.lineupList,
						lineupResp.homeEntity.formation_type);
				mListView.setAdapter(mAdapter);

				checkData();
			}
		}
	}

	/* 比赛是否结束 */
	boolean bGameEnd;

	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			if (bGameEnd) {
				// 比赛结束跳转到评分页
				FormationEntity entity = mHeaderAdapter.getItem(pos);
				if (entity != null) {
					Intent in = new Intent(getActivity(),
							PlayersRatingActivity.class);
					if (getActivity() instanceof FootballGameActivity) {
						in.putExtra("tag",
								((FootballGameActivity) getActivity()).getTag());
						in.putExtra("oid", entity.player_id);
						startActivity(in);
					}
				}
			} else {
				// 球员详情页
			}
		}
	}

}
