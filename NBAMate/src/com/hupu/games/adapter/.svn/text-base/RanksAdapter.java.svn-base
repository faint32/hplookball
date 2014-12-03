package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;

public class RanksAdapter extends BaseListAdapter<ArrayList<String>> {

	LayoutInflater inflater;
	ArrayList<ArrayList<String>> ranks;
	String rankType;
	int online = 0, offline = 0;

	// private int height;
	OnClickListener mClick;
	public RanksAdapter(Context context,OnClickListener click) {
		super(context);
		mClick = click;
	}

	public void setData(ArrayList<ArrayList<String>> list, String type,
			int top, int end) {
		HupuLog.d("papa", "size=" + list.size());
		if (list != null) {

			ranks = list;
			rankType = type;
			online = top;
			offline = end;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (ranks != null)
			return ranks.size();
		return 0;
	}

	@Override
	public ArrayList<String> getItem(int position) {
		// TODO Auto-generated method stub
		return ranks.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (rankType.equals("standings")) {
			Standings rank = null;
			rank = new Standings();
			convertView = initStandings(rank);

			rank.rank_num.setText(ranks.get(position).get(0));
			// 名词加红色底背景 突出
			if (position < online) {
				rank.rank_num.setBackgroundResource(R.drawable.bg_red_garden);
			}
			if (position + offline >= ranks.size()) {
				rank.rank_num.setTextColor(Color.parseColor("#525252"));
			}
			rank.team_name.setText(ranks.get(position).get(1));
			rank.played.setText(ranks.get(position).get(3));
			rank.win_num.setText(ranks.get(position).get(4));
			rank.tie_num.setText(ranks.get(position).get(5));
			rank.losses_num.setText(ranks.get(position).get(6));
			rank.goals_for_against.setText(ranks.get(position).get(7));
			rank.points.setText(ranks.get(position).get(8));
			UrlImageViewHelper.setUrlDrawable(rank.logo, ranks.get(position)
					.get(2), R.drawable.bg_home_nologo);
			
			if (ranks.get(position).size() > 9) {
				rank.lay_team.setTag(ranks.get(position).get(9));
				rank.lay_team.setOnClickListener(mClick);
			}

		} else {
			Assists assists = null;
			assists = new Assists();
			convertView = initAssists(assists);

			assists.rank_num.setText(ranks.get(position).get(0));
			// 名词加红色底背景 突出
			if (position < online) {
				assists.rank_num
						.setBackgroundResource(R.drawable.bg_red_garden);
			}
			if (position + offline >= ranks.size()) {
				assists.rank_num.setTextColor(Color.parseColor("#525252"));
			}

			assists.name.setText(ranks.get(position).get(1));
			UrlImageViewHelper.setUrlDrawable(assists.logo, ranks.get(position)
					.get(2), R.drawable.no_photo);
			assists.team_name.setText(ranks.get(position).get(3));
			assists.num.setText(ranks.get(position).get(4));
			
			if (ranks.get(position).size() > 5) {
				assists.rank_layout.setTag(ranks.get(position).get(5));
				assists.rank_layout.setOnClickListener(mClick);
			}
		}
		convertView.setBackgroundResource(R.drawable.bg_rank_item_selector);
//		if (position % 2 == 0) {
//			convertView.setBackgroundResource(R.drawable.bg_lineup_list1_selector);
//		} else {
//			convertView.setBackgroundResource(R.drawable.bg_lineup_list2_selector);
//		}

		return convertView;
	}

	private View initStandings(Standings standings) {
		View content = mInflater.inflate(R.layout.item_football_standings,
				null, false);
		
		standings.lay_team = (LinearLayout) content.findViewById(R.id.lay_team);
		standings.rank_num = (TextView) content.findViewById(R.id.ranking);
		standings.logo = (ImageView) content.findViewById(R.id.team_logo);
		standings.team_name = (TextView) content.findViewById(R.id.team_name);
		standings.played = (TextView) content.findViewById(R.id.played);
		standings.win_num = (TextView) content.findViewById(R.id.wins);
		standings.tie_num = (TextView) content.findViewById(R.id.ties);
		standings.losses_num = (TextView) content.findViewById(R.id.losses);
		standings.goals_for_against = (TextView) content
				.findViewById(R.id.goals_for_against);
		standings.points = (TextView) content.findViewById(R.id.points);
		// content.setTag(standings);
		return content;
	}

	private View initAssists(Assists assists) {

		View content = mInflater.inflate(R.layout.item_football_assists, null,
				false);
		assists.rank_layout = (LinearLayout) content.findViewById(R.id.rank_layout);
		assists.rank_num = (TextView) content.findViewById(R.id.ranking);
		assists.logo = (ImageView) content.findViewById(R.id.logo);
		assists.name = (TextView) content.findViewById(R.id.name);
		assists.team_name = (TextView) content.findViewById(R.id.team_name);
		assists.num = (TextView) content.findViewById(R.id.num);
		// content.setTag(assists);

		return content;
	}

	class Standings {
		LinearLayout lay_team;
		TextView rank_num;
		ImageView logo;
		TextView team_name;
		TextView played;
		TextView win_num;
		TextView tie_num;
		TextView losses_num;
		TextView goals_for_against;
		TextView points;
	}

	class Assists {
		LinearLayout rank_layout;
		TextView rank_num;
		ImageView logo;
		TextView name;
		TextView team_name;
		TextView num;
	}
}
