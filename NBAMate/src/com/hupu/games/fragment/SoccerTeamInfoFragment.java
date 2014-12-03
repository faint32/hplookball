package com.hupu.games.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.SoccerTeamActivity;
import com.hupu.games.adapter.SoccerTeamInfoListAdapter;
import com.hupu.games.data.game.football.SoccerTeamDataEntity;
import com.hupu.games.data.game.football.SoccerTeamReq;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * 足球球队最近的赛程
 * */
public class SoccerTeamInfoFragment extends BaseFragment {

	SoccerTeamInfoListAdapter mAdapter;

	ListView mListView;

	ListClick listClick;

	TextView t2;
	TextView t3;

	ImageView imgTeam;

	TextView txtName,noDate;

	View progress;

	View vInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_soccer_team, container,
				false);
		vInfo = v.findViewById(R.id.layout_info);
		mListView = (ListView) v.findViewById(R.id.list_teams);
		mListView.setOnItemClickListener(new ListClick());
		progress = v.findViewById(R.id.probar);
		imgTeam = (ImageView) v.findViewById(R.id.img_team_logo);
		t2 = (TextView) v.findViewById(R.id.txt_2);
		t3 = (TextView) v.findViewById(R.id.txt_3);
		txtName = (TextView) v.findViewById(R.id.txt_team);
		noDate = (TextView) v.findViewById(R.id.txt_no_data);

		if (mAdapter == null) {
			mAdapter = new SoccerTeamInfoListAdapter(getActivity());
		}
		if (mData != null) {
			setData(mData);
		}
		return v;
	}

	boolean getData;
	SoccerTeamReq mData;
	int size;

	View footView;

	/** 重新刷新数据 */
	public void setData(SoccerTeamReq data) {

		mData = data;

		if (mAdapter != null && getActivity() != null) {
			if (data.logo != null)
				UrlImageViewHelper.setUrlDrawable(imgTeam, mData.logo,
						R.drawable.bg_home_nologo);
			txtName.setText(data.name);
			if (data.market_values != null)
				t2.setText(data.market_values);
			if (data.rank != null)
				t3.setText(data.rank);

			if (data.mDataList != null) {
				size = data.mDataList.size();
				if (size == 0) {
					mListView.setVisibility(View.GONE);
				}

				if (mListView.getHeaderViewsCount() == 0) {
					mListView.addHeaderView(getHeader());
					if (data.intro.length() > 1) {
						// 球队介绍不为空的情况下添加到尾部
						footView = getFoot(data.intro);
						mListView.addFooterView(footView);
						size++;
					}
					mListView.addFooterView(getHeader());
					mListView.setAdapter(mAdapter);
				}

				mAdapter.setData(data.mDataList);
			}
			UrlImageViewHelper.setUrlDrawable(imgTeam, mData.logo,
					R.drawable.bg_home_nologo);
			vInfo.setVisibility(View.VISIBLE);
			txtName.setText(data.full_name);
			progress.setVisibility(View.GONE);
		}
	}

	private View getHeader() {
		TextView tx = (TextView) LayoutInflater.from(getActivity()).inflate(
				R.layout.header_team_schedule_more, null);
		// TextView tx= (TextView) v.findViewById(R.id.txt_date);
		// TextView tx = (TextView) LayoutInflater.from(getActivity()).inflate(
		// R.layout.item_schedule_header, null);
		tx.setText("查看完整赛程");
		return tx;
	}

	private View getFoot(String intro) {
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.foot_soccer_team, null);
		TextView tv = (TextView) v.findViewById(R.id.txt_intro);
		tv.setText(intro);
		return v;
	}

	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {

			if (pos == 0 || pos == size + 1)
				((SoccerTeamActivity) getActivity()).showFullProgram();
			else {
				SoccerTeamDataEntity data = mAdapter.getItem(pos - 1);
				if (data != null)
					((SoccerTeamActivity) getActivity())
							.switchToGameActivity(data.gid,data.tag,data.lid);
			}

		}

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	
	public void setNoData(){
		noDate.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		progress.setVisibility(View.GONE);
	}

}
