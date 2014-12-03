package com.hupu.games.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.activity.NBATeamActivity;
import com.hupu.games.adapter.BSStandingsListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.StandingsResp;
import com.hupu.games.data.TeamRankEntity;

/**
 * NBA 排行
 * 
 * @author panyongjun
 * */
public class NbaStandingFragment extends BaseFragment {

	private ListView mLvStandings;

	private Button mBtnWest;

	private Button mBtnEast;

	private BSStandingsListAdapter mStandingAdapter;

	private int curFrame;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mAct = (HupuHomeActivity) getActivity();
		super.onActivityCreated(savedInstanceState);
	}

	Click click;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_standing, container, false);
		mBtnWest = (Button) v.findViewById(R.id.btn_west);
		mBtnEast = (Button) v.findViewById(R.id.btn_east);
		click = new Click();
		mBtnWest.setOnClickListener(click);
		mBtnEast.setOnClickListener(click);
		mLvStandings = (ListView) v.findViewById(R.id.list_standings);
		mLvStandings.setOnItemClickListener(new ListClick());
		if (mStandingAdapter == null) {
			mStandingAdapter = new BSStandingsListAdapter(getActivity());
		} else {
			if (curFrame == 1) {
				mBtnWest.setTextColor(Color.WHITE);
				mBtnWest.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				mBtnEast.setBackgroundResource(R.drawable.btn_rank_type_selector);
				mBtnEast.setTextColor(Color.GRAY);
			}
			mLvStandings.setAdapter(mStandingAdapter);
		}
		if(data ==null)
			reqStandings();
		return v;
	}
	public void setData(StandingsResp resp) {
		if (mStandingAdapter != null)
			mStandingAdapter.setData(resp);
		data =resp;
		mLvStandings.setAdapter(mStandingAdapter);
	}

	StandingsResp data;


	private class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_west:
				curFrame = 1;
				mStandingAdapter.switchToWest();
				if (mStandingAdapter != null && mStandingAdapter.getCount() > 0)
					mLvStandings.setSelection(0);

				mBtnWest.setTextColor(Color.WHITE);
				mBtnWest.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				mBtnEast.setBackgroundResource(R.drawable.btn_rank_type_selector);
				mBtnEast.setTextColor(Color.GRAY);
				break;
			case R.id.btn_east:
				curFrame = 0;
				mStandingAdapter.switchToEast();
				if (mStandingAdapter != null && mStandingAdapter.getCount() > 0)
					mLvStandings.setSelection(0);

				mBtnEast.setTextColor(Color.WHITE);
				mBtnEast.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				mBtnWest.setBackgroundResource(R.drawable.btn_rank_type_selector);
				mBtnWest.setTextColor(Color.GRAY);
				break;
			}
		}
	}

	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			((HupuBaseActivity) getActivity()).sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_ENTRANCE,HuPuRes.UMENG_VALUE_NBA_RANK);
			TeamRankEntity entity = mStandingAdapter.getItem(pos);
			Intent in = new Intent(getActivity(),
					NBATeamActivity.class);
			in.putExtra("tid", entity.i_tid);
			startActivity(in);

		}

	}
	/** 请求排行数据 */
	private void reqStandings() {
		mParams = mAct.getHttpParams(true);
		if (!mAct.isActiveFragment(this)) {
			mParams.put("preload", "1");
		}
		mAct.sendAppRequest(HuPuRes.REQ_METHOD_GET_STANDINGS,null, mParams,new FragmentHttpResponseHandler());
	}
	
	@Override
	public void onSuccess(Object o, int reqType) {
		StandingsResp entity =  (StandingsResp) o;
		setData(entity);
	}
	

	public void onFailure(Throwable error, int reqType) {

	}

}
