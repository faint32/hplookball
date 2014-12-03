package com.hupu.games.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.NBAPlayerInfoActivity;
import com.hupu.games.adapter.TeamPlayerListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.game.basketball.NbaTeamPlayerEntity;
import com.hupu.games.data.game.basketball.NbaTeamPlayerReq;
import com.hupu.games.data.game.basketball.NbaTeamReq;

public class NbaTeamPlayerFragment extends BaseFragment {

	TeamPlayerListAdapter mAdapter;

	ListView mListView;

	View progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_team_player, container, false);
		mListView = (ListView) v.findViewById(R.id.list_player);
		mListView.setOnItemClickListener(new PlayerListClick());
		progress = (ProgressBar) v.findViewById(R.id.probar);
		
		if (mAdapter == null) {
			mAdapter = new TeamPlayerListAdapter(getActivity());
		}
		
		if (mData != null) {
			setData(mData);
		}
		return v;
	}

	boolean getData;
	NbaTeamPlayerReq mData;
	int size;

	/** 重新刷新数据 */
	public void setData(NbaTeamPlayerReq data) {
		mData = data;
		if (mAdapter != null) {
			if (data.mDataList != null) {
				mListView.setAdapter(mAdapter);
				mAdapter.setData(data.mDataList);
			}
			progress.setVisibility(View.GONE);
		}
		
	}



	@Override
	public void entry() {
		super.entry();

	}

	class PlayerListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			((HupuBaseActivity) getActivity()).sendUmeng(
					HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_ROSTER,
					HuPuRes.UMENG_VALUE_TAP_ONE_PLAYER);
			
			NbaTeamPlayerEntity entity =mAdapter.getItem(pos);
			Intent in =new Intent(getActivity(),NBAPlayerInfoActivity.class);
			in.putExtra("pid", entity.player_id);
			startActivity(in);
//			Intent playerIntent = new Intent(getActivity(), NBAPlayerInfoActivity.class);
//			playerIntent.putExtra("pid", mData.mDataList.get(pos).player_id);
//			startActivity(playerIntent);


		}

	}

}
