package com.hupu.games.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.NBAGameActivity;
import com.hupu.games.adapter.NBATeamScheduleAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.game.basketball.NbaTeamDataEntity;
import com.hupu.games.data.game.basketball.NbaTeamScheduleReq;
import com.hupu.games.view.PinnedHeaderListView;

/**
 * @author panyongjun 球队完整赛程
 * */
public class NbaTeamScheduleFragment extends BaseFragment {

	ListClick listClick;

	int i_tid;

	View progress;

	private NBATeamScheduleAdapter mAdapter;

	private PinnedHeaderListView mLvSchedule;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (i_tid == 0)
			i_tid = getArguments().getInt("tid");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}
int headerHeight;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_team_schedule, container,
				false);
		mLvSchedule = (PinnedHeaderListView) v.findViewById(R.id.list_teams);
		mLvSchedule.setOnItemClickListener(new ListClick());
		progress = v.findViewById(R.id.probar);
		measure();
		if (mAdapter == null) {
			mAdapter = new NBATeamScheduleAdapter(getActivity());
		}
		if (mData != null) {
			setData(mData);
		}
		//mLvSchedule.setAdapter(mAdapter);
		return v;
	}

	private void measure()
	{
		View v =View.inflate(getActivity(), R.layout.header_team_schedule_date, null);
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        v.measure(w, h);  
    	headerHeight =v.getMeasuredHeight();
//		HupuLog.d("headerHeight = " + headerHeight);
	}
	NbaTeamScheduleReq mData;

	/** 重新刷新数据 */
	public void setData(NbaTeamScheduleReq data) {

		mData = data;
		if (mAdapter != null ) {
			mLvSchedule.setAdapter(mAdapter);
			if (data.blocks != null) {
				mAdapter.setData(data);
			}
			int ps = mAdapter.getSectionForPosition(data.pos);
			int pos = data.pos + ps + 1;
//			mLvSchedule.setSelection(pos);
			if(mLvSchedule.pinnedHeaderHeight>0)
				mLvSchedule.setSelectionFromTop(pos, (int)mLvSchedule.pinnedHeaderHeight);
			else
				mLvSchedule.setSelectionFromTop(pos,headerHeight);
			
			progress.setVisibility(View.GONE);
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
		

			NbaTeamDataEntity data = mAdapter.getItemAt(pos);
			if (data != null) {
				((HupuBaseActivity) getActivity()).sendUmeng(
						HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_FULL_SCHEDULE,
						HuPuRes.UMENG_VALUE_TAP_ONE_GAME);
				
				Intent in = new Intent(getActivity(), NBAGameActivity.class);
				in.putExtra("gid", data.gid);
				// in.putExtra("st", data.status);
				startActivity(in);
			}

		}

	}

}
