package com.hupu.games.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.NBAGameActivity;
import com.hupu.games.activity.NBATeamActivity;
import com.hupu.games.adapter.NbaTeamInfoListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.game.basketball.NbaTeamDataEntity;
import com.hupu.games.data.game.basketball.NbaTeamReq;

public class NbaTeamInfoFragment extends BaseFragment {

	NbaTeamInfoListAdapter mAdapter;

	ListView mListView;

	ListClick listClick;

	TextView t1;
	TextView t2;
	TextView t3;
	TextView t4;

	ImageView imgTeam;

	TextView txtName;

	int i_tid;

	View progress;

	View vInfo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(i_tid==0)
			i_tid = getArguments().getInt("tid");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_team, container, false);
		vInfo= v.findViewById(R.id.layout_info);
		mListView = (ListView) v.findViewById(R.id.list_teams);
		mListView.setOnItemClickListener(new ListClick());
		progress = v.findViewById(R.id.probar);
		imgTeam = (ImageView) v.findViewById(R.id.img_team_logo);
		t1 = (TextView) v.findViewById(R.id.txt_1);
		t2 = (TextView) v.findViewById(R.id.txt_2);
		t3 = (TextView) v.findViewById(R.id.txt_3);
		t4 = (TextView) v.findViewById(R.id.txt_4);
		txtName = (TextView) v.findViewById(R.id.txt_team);
		//txtName.setText(HuPuApp.getTeamData(i_tid).str_name);
		imgTeam.setImageResource(HuPuApp.getTeamData(i_tid).i_logo_small);
		if (mAdapter == null) {
			mAdapter = new NbaTeamInfoListAdapter(getActivity());
		}
		if (mData != null) {
			setData(mData);
		}
		return v;
	}

	boolean getData;
	NbaTeamReq mData;
	int size;

	/** 重新刷新数据 */
	public void setData(NbaTeamReq data) {

		mData = data;
		if (mAdapter != null && getActivity()!=null) {
			if (data.record != null)
				t1.setText(data.record);
			if (data.rank != null)
				t2.setText(data.rank);
			if (data.home_record != null)
				t3.setText(data.home_record);
			if (data.away_record != null)
				t4.setText(data.away_record);

			if (data.mDataList != null) {
                if(mListView.getHeaderViewsCount()==0)
                {
                	mListView.addHeaderView(getHeader());
    				mListView.addFooterView(getHeader());
    				mListView.setAdapter(mAdapter);
                }
				mAdapter.setData(data.mDataList);
			}
			vInfo.setVisibility(View.VISIBLE);
			txtName.setText(data.full_name);
			if (data != null && data.mDataList != null) {
				size = data.mDataList.size();
			}
			progress.setVisibility(View.GONE);
		}

	}

	private View getHeader() {
		TextView tx = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.header_team_schedule_more,null);
//		TextView tx= (TextView) v.findViewById(R.id.txt_date);
//		TextView tx = (TextView) LayoutInflater.from(getActivity()).inflate(
//				R.layout.item_schedule_header, null);
		tx.setText("查看完整赛程");
		return tx;
	}

	@Override
	public void entry() {
		super.entry();

	}

	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {

			if (pos == 0 || pos == size + 1) {
				// 跳转
				((HupuBaseActivity) getActivity()).sendUmeng(
						HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_TEAMS,
						HuPuRes.UMENG_VALUE_FULLSCHEDULE);
				
				((NBATeamActivity) getActivity()).showFullProgram();
			} else {
				// 跳转到比赛
				((HupuBaseActivity) getActivity()).sendUmeng(
						HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_TEAMS,
						HuPuRes.UMENG_VALUE_TAP_ONE_GAME);
				
				NbaTeamDataEntity data =mAdapter.getItem(pos-1);
				Intent in =new Intent(getActivity() , NBAGameActivity.class);
				in.putExtra("gid", data.gid);
				//in.putExtra("st", data.status);
				startActivity(in);		
			}

		}

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
