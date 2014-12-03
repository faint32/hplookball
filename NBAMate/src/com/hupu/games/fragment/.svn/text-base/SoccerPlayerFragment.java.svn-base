package com.hupu.games.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.FootballCoachInfoActivity;
import com.hupu.games.activity.FootballPlayerInfoActivity;
import com.hupu.games.activity.NBAPlayerInfoActivity;
import com.hupu.games.adapter.SoccerPlayerListAdapter;
import com.hupu.games.data.game.football.SoccerPlayerEntity;
import com.hupu.games.data.game.football.SoccerPlayerReq;
import com.hupu.games.view.PinnedHeaderListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class SoccerPlayerFragment extends BaseFragment {

	SoccerPlayerListAdapter mAdapter;

	private PinnedHeaderListView mListView;

	View progress;

	TextView coachName,noDate,titleWorth;
	ImageView imgCoach;
	LinearLayout coachLayout,headLayout,topLayout;
	String mTag;

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

		View v = inflater.inflate(R.layout.fragment_soccer_player, container,
				false);
		mListView = (PinnedHeaderListView) v.findViewById(R.id.list_player);
		mListView.setOnItemClickListener(new PlayerListClick());
		coachName = (TextView) v.findViewById(R.id.player_name);
		noDate = (TextView) v.findViewById(R.id.txt_no_data);
		imgCoach = (ImageView) v.findViewById(R.id.img_player);
		progress = (ProgressBar) v.findViewById(R.id.probar);

		titleWorth = (TextView) v.findViewById(R.id.title_worth);
		if (mAdapter == null) {
			mAdapter = new SoccerPlayerListAdapter(getActivity());
		}

		if (mData != null) {
			setData(mData,mTag);
		}
		headLayout =  (LinearLayout) v.findViewById(R.id.layout_head);
		topLayout =  (LinearLayout) v.findViewById(R.id.layout_coach);
		coachLayout = (LinearLayout) v.findViewById(R.id.coach_layout);
		coachLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mData !=null) {
					Intent in = new Intent(getActivity(), FootballCoachInfoActivity.class);
					in.putExtra("pid", mData.coach_id);
					in.putExtra("tag", mTag);
					startActivity(in);
				}
			}
		});
		return v;
	}

	boolean getData;
	SoccerPlayerReq mData;
	int size;
	View header;

	/** 重新刷新数据 */
	public void setData(SoccerPlayerReq o,String tag) {
		mTag = tag;
		mData = o;
		if (!mData.have_worth) {
			titleWorth.setVisibility(View.GONE);
		}
		if (mAdapter != null && getActivity()!=null) {
			if (o.mPlayerMap != null) {
				coachName.setText(o.coach_name);
				UrlImageViewHelper.setUrlDrawable(imgCoach, o.coach_header,
						R.drawable.bg_1x1);
				mListView.setAdapter(mAdapter);
				mAdapter.setData(o);
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
			// ((HupuBaseActivity) getActivity()).sendUmeng(
			// HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_ROSTER,
			// HuPuRes.UMENG_VALUE_TAP_ONE_PLAYER);

			SoccerPlayerEntity entity = mAdapter.getItemAt(pos);
			if(entity!=null)
			{
				Intent in = new Intent(getActivity(), FootballPlayerInfoActivity.class);
				in.putExtra("pid", entity.player_id);
				in.putExtra("tag", mTag);
				startActivity(in);
			}

		}

	}
	
	public void setNoData(){
		noDate.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		progress.setVisibility(View.GONE);
		headLayout.setVisibility(View.GONE);
		topLayout.setVisibility(View.GONE);
		
	}

}
