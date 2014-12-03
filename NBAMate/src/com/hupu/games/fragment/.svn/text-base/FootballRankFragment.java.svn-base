package com.hupu.games.fragment;

import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.FootballPlayerInfoActivity;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.activity.SoccerTeamActivity;
import com.hupu.games.adapter.RanksAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.RanksEntity;
import com.hupu.games.data.RanksResp;
import com.hupu.games.fragment.BaseFragment.FragmentHttpResponseHandler;
import com.hupu.games.view.HupuWebView;
import com.mato.sdk.proxy.Proxy;

/**
 * 足球 排行
 * 
 * @author papa
 * */
@SuppressLint("ValidFragment")
public class FootballRankFragment extends BaseFragment {

	private LinkedList<RanksEntity> ranks;
	private LayoutInflater inflater;
	private LinearLayout standingsLayout, assistsLayout, goalsLayout;
	private Button standingsButton, assistsButton, goalsButton;
	private HupuWebView rankWebView;

	RanksAdapter ranksAdapter;

	ListView rankListView;
	ProgressBar loadProgressBar;
	private String mTag;
	TextView footTv; 


	public FootballRankFragment(String tag) {
		super();
		mTag = tag;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mAct = (HupuHomeActivity) getActivity();
		// 设置点击监听
		click = new Click();
		standingsButton.setOnClickListener(click);
		assistsButton.setOnClickListener(click);
		goalsButton.setOnClickListener(click);
		super.onActivityCreated(savedInstanceState);
	}

	Click click;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View v = inflater.inflate(R.layout.fragment_football_standing,
				container, false);
		v = init(v);
		return v;
	}

	private View init(View v) {
		// 实例化
		standingsLayout = (LinearLayout) v
				.findViewById(R.id.layout_standings_top_title);
		assistsLayout = (LinearLayout) v
				.findViewById(R.id.layout_assists_top_title);
		goalsLayout = (LinearLayout) v
				.findViewById(R.id.layout_goals_top_title);
		standingsButton = (Button) v.findViewById(R.id.btn_standings);
		assistsButton = (Button) v.findViewById(R.id.btn_assists);
		goalsButton = (Button) v.findViewById(R.id.btn_goals);
		rankListView = (ListView) v.findViewById(R.id.list_standings);
		loadProgressBar = (ProgressBar) v.findViewById(R.id.load_progress);
		rankWebView = (HupuWebView) v.findViewById(R.id.rank_webview);
		Proxy.supportWebview(getActivity());
		// 设置数据适配器

		ranksAdapter = new RanksAdapter(mAct, new Click());
		reqData();

		rankListView.setAdapter(ranksAdapter);
		// rankListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// if ("worldcup".equals(tag)) {
		// Intent in = new Intent(getActivity(),
		// FootballPlayerInfoActivity.class);
		// in.putExtra("pid", entity.player_id);
		// startActivity(in);
		// }
		// }
		// });
		// 初始化专题
		ranksAdapter.notifyDataSetChanged();
		clearAll();
		return v;
	}
	private void addRealtimeTips(){
		if(rankListView.getFooterViewsCount()==0 && !mTag.equalsIgnoreCase("csl")){//5大联赛和欧冠（排除中超）
			footTv = new TextView(mAct);
			try {
				footTv.setText(SharedPreferencesMgr.getString("leagueScoreboardTips", getActivity().getString(R.string.league_standinglist_tips)));
				footTv.setPadding(10, 15, 10, 30);
				footTv.setTextColor(getActivity().getResources().getColor(R.color.txt_status));
				rankListView.addFooterView(footTv);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	private void showRealtimetips(boolean isStanding){
		if( rankListView.getFooterViewsCount()<=0)
			return;
		if(isStanding){
			footTv.setVisibility(View.VISIBLE);
		}else{
			footTv.setVisibility(View.GONE);
		}
	}

	public void setData(RanksResp resp) {
		// if(resp.ranks!=null && ranks ==null)
		// {
		// ranks = resp.ranks;
		// showRanking();
		// }
		// else
		// {
		// //
		// ranks = resp.ranks;
		// }

		if (resp.ranks != null) {
			ranks = resp.ranks;
			showRanking();

			// 去掉头部标示 此标示被配置文件写死了！
			for (int i = 0; i < ranks.size(); i++) {
				if ("".equals(ranks.get(i).web_url)) {
					ranks.get(i).ranks_data.remove(0);
				}
			}
		} else {
			if (loadProgressBar != null) {
				loadProgressBar.setVisibility(View.GONE);
			}
		}

	}

	private void clearAll() {
		standingsLayout.setVisibility(View.GONE);
		assistsLayout.setVisibility(View.GONE);
		goalsLayout.setVisibility(View.GONE);

		standingsButton
		.setBackgroundResource(R.drawable.btn_rank_type_selector);
		assistsButton.setBackgroundResource(R.drawable.btn_rank_type_selector);
		goalsButton.setBackgroundResource(R.drawable.btn_rank_type_selector);
	}

	private class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_standings:
				clearAll();
				standingsButton
				.setBackgroundResource(R.drawable.btn_rank_type_bg_down);

				for (RanksEntity rank : ranks) {
					if (rank.rank_type.equals("standings")) {//积分榜
						if ("".equals(rank.web_url)) {
							standingsLayout.setVisibility(View.VISIBLE);
							ranksAdapter.setData(rank.ranks_data,
									rank.rank_type,
									Integer.parseInt(rank.online),
									Integer.parseInt(rank.offline));
							rankListView.setVisibility(View.VISIBLE);
							rankWebView.setVisibility(View.GONE);
							rankListView.setAdapter(ranksAdapter);
							addRealtimeTips();
							ranksAdapter.notifyDataSetChanged();

						} else {
							setWebview(rank.web_url);
							break;
						}
					}
				}
				showRealtimetips(true);
				break;
			case R.id.btn_assists:
				clearAll();
				assistsButton
				.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				for (RanksEntity rank : ranks) {
					if (rank.rank_type.equals("assists")) {
						if ("".equals(rank.web_url)) {
							assistsLayout.setVisibility(View.VISIBLE);
							ranksAdapter.setData(rank.ranks_data,
									rank.rank_type,
									Integer.parseInt(rank.online),
									Integer.parseInt(rank.offline));
							rankListView.setAdapter(ranksAdapter);
							ranksAdapter.notifyDataSetChanged();
							rankListView.setVisibility(View.VISIBLE);
							rankWebView.setVisibility(View.GONE);
						} else {
							setWebview(rank.web_url);
						}
						break;
					}
				}
				showRealtimetips(false);
				break;
			case R.id.btn_goals:
				clearAll();
				goalsButton
				.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				for (RanksEntity rank : ranks) {
					if (rank.rank_type.equals("goals")) {
						if ("".equals(rank.web_url)) {
							goalsLayout.setVisibility(View.VISIBLE);
							ranksAdapter.setData(rank.ranks_data,
									rank.rank_type,
									Integer.parseInt(rank.online),
									Integer.parseInt(rank.offline));
							rankListView.setAdapter(ranksAdapter);
							ranksAdapter.notifyDataSetChanged();
							rankListView.setVisibility(View.VISIBLE);
							rankWebView.setVisibility(View.GONE);
						} else {
							setWebview(rank.web_url);
						}

						break;
					}
				}
				showRealtimetips(false);
				break;
			case R.id.rank_layout:
				int pid = Integer.parseInt(v.getTag().toString());
				HupuLog.e("papa", "pid===" + pid);
				if (pid != 0) {
					Intent in = new Intent(getActivity(),
							FootballPlayerInfoActivity.class);
					in.putExtra("pid", pid);
					in.putExtra("tag", mTag);
					startActivity(in);
				}
				break;

			case R.id.lay_team:
				int tId = Integer.parseInt(v.getTag().toString());
				if (tId != 0) {
					Intent in = new Intent(getActivity(), SoccerTeamActivity.class);
					in.putExtra("tid", tId);
					in.putExtra("tag", mTag);
					startActivity(in);

				}
				break;

			default:
				break;
			}
		}
	}

	void reqData() {
		loadProgressBar.setVisibility(View.VISIBLE);
		mParams = mAct.getHttpParams(true);
		if (!mAct.isActiveFragment(this)) {
			mParams.put("preload", "1");
		}
		//		mAct.sendTagRequest(HuPuRes.REQ_METHOD_GET_FOOTBALL_RANK, mParams);
		mAct.sendAppRequest(HuPuRes.REQ_METHOD_GET_FOOTBALL_RANK,mTag, mParams,new FragmentHttpResponseHandler());
	}

	void showRanking() {
		if (ranks != null) {
			if (ranks.get(0).rank_type.equals("standings")) {
				standingsButton
				.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				standingsLayout.setVisibility(View.VISIBLE);
				addRealtimeTips();

			} else if (ranks.get(0).rank_type.equals("assists")) {
				assistsButton
				.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				assistsLayout.setVisibility(View.VISIBLE);
			} else {
				goalsButton
				.setBackgroundResource(R.drawable.btn_rank_type_bg_down);
				goalsLayout.setVisibility(View.VISIBLE);
			}

			if ("".equals(ranks.get(0).web_url)) {

				ranksAdapter.setData(ranks.get(0).ranks_data,
						ranks.get(0).rank_type,
						Integer.parseInt(ranks.get(0).online),
						Integer.parseInt(ranks.get(0).offline));

				rankListView.setVisibility(View.VISIBLE);
				rankWebView.setVisibility(View.GONE);
				rankListView.setAdapter(ranksAdapter);
				// 初始化专题
				ranksAdapter.notifyDataSetChanged();

				loadProgressBar.setVisibility(View.GONE);
				setTouchModeAbove(false);
			} else {
				setWebview(ranks.get(0).web_url);
				setTouchModeAbove(true);
			}

			standingsButton.setVisibility(View.GONE);
			assistsButton.setVisibility(View.GONE);
			goalsButton.setVisibility(View.GONE);
			
//			StringBuffer sb = new Stringbuffer();

			for (RanksEntity rank : ranks) {
				if (rank.rank_type.equals("standings")) {
					standingsButton.setVisibility(View.VISIBLE);
					standingsButton.setText(rank.title);
				}
				if (rank.rank_type.equals("assists")) {
					assistsButton.setVisibility(View.VISIBLE);
					assistsButton.setText(rank.title);
				}
				if (rank.rank_type.equals("goals")) {
					goalsButton.setVisibility(View.VISIBLE);
					goalsButton.setText(rank.title);
				}
			}
		}

	}

	private void setTouchModeAbove(boolean bMargin) {
		if (getActivity() != null) {
			((HupuHomeActivity) getActivity()).setTouchModeAbove(bMargin);
		}
	}

	private void setWebview(String url) {
		standingsLayout.setVisibility(View.GONE);
		assistsLayout.setVisibility(View.GONE);
		goalsLayout.setVisibility(View.GONE);
		rankListView.setVisibility(View.GONE);
		rankWebView.setVisibility(View.VISIBLE);
		rankWebView.loadUrl(url);
		loadProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void entry() {
		if (ranksAdapter != null) {
			ranks = new LinkedList<RanksEntity>();
			ranksAdapter.notifyDataSetChanged();

		}
	}

	@Override
	public void onSuccess(Object o, int reqType) {
		setData((RanksResp) o);
	}

	public void onFailure(Throwable error, int reqType) {

	}
}
