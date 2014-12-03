package com.hupu.games.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.football.FootballEventData;
import com.pyj.adapter.BaseListAdapter;

public class FootballEventsListAdapter extends
		BaseListAdapter<FootballEventData> {

	OnClickListener mClick;
	public FootballEventsListAdapter(Context context,OnClickListener click) {
		super(context);
		mClick = click;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FootballEventData entity = getItem(position);
		Holder holder = null;
		if (convertView == null) {
			//
			convertView = mInflater.inflate(R.layout.item_football_live, null);
			holder = new Holder();
			holder.home = convertView
					.findViewById(R.id.layout_left);
			holder.away =  convertView
					.findViewById(R.id.layout_right);
			holder.txtHomeTxt1 = (TextView) convertView
					.findViewById(R.id.txt_home_event1);
			holder.txtHomeTxt2 = (TextView) convertView
					.findViewById(R.id.txt_home_event2);
			holder.homeScore = (TextView) convertView
					.findViewById(R.id.txt_home_score);

			holder.txtAwayTxt1 = (TextView) convertView
					.findViewById(R.id.txt_away_event1);
			holder.txtAwayTxt2 = (TextView) convertView
					.findViewById(R.id.txt_away_event2);
			holder.awayScore = (TextView) convertView
					.findViewById(R.id.txt_away_score);
			holder.txtTime = (TextView) convertView
					.findViewById(R.id.txt_event_time);

			holder.lineTop = convertView.findViewById(R.id.line_top);
			holder.lineBottom = convertView.findViewById(R.id.line_bottom);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		if (position == 0)
		{
			holder.lineTop.setVisibility(View.INVISIBLE);
//			if(getCount()==1)
//			{
//				holder.home.setVisibility(View.VISIBLE);
//				holder.away.setVisibility(View.INVISIBLE);
//				holder.txtHomeTxt1.setText("比赛开始");
//			}
		}
		else
			holder.lineTop.setVisibility(View.VISIBLE);

		if (position == getCount() - 1)
			holder.lineBottom.setVisibility(View.INVISIBLE);
		else
			holder.lineBottom.setVisibility(View.VISIBLE);

		if (entity.eventType == 1) {
			// 主队消息
			holder.home.setVisibility(View.VISIBLE);
			holder.away.setVisibility(View.INVISIBLE);
			setScore(holder.homeScore, entity);
		} else if (entity.eventType == 2) {
			// 客队消息
			holder.away.setVisibility(View.VISIBLE);
			holder.home.setVisibility(View.INVISIBLE);
			setScore(holder.awayScore, entity);
		} else {
			// 比赛消息
			if(position ==0)
				holder.home.setVisibility(View.VISIBLE);
			else
				holder.home.setVisibility(View.INVISIBLE);
			holder.away.setVisibility(View.INVISIBLE);
		}
HupuLog.e("papa", "-------eventId="+entity.id+"player="+entity.player_name);
		if (entity.id == FootballEventData.EVENT_SUBSTITUTION) {
			setSustitutionEvent(entity, holder);
		} else
			setOneLineMsg(entity, holder);
		if (entity.id == 10)
			holder.txtTime.setText("完");// 比赛结束
		else
			holder.txtTime.setText(entity.live_time + "'");
		
		holder.txtHomeTxt1.setTag(entity.player_id);
		holder.txtHomeTxt2.setTag(entity.rel_id);
		holder.txtAwayTxt1.setTag(entity.player_id);
		holder.txtAwayTxt2.setTag(entity.rel_id);
		holder.txtHomeTxt1.setOnClickListener(mClick);
		holder.txtHomeTxt2.setOnClickListener(mClick);
		holder.txtAwayTxt1.setOnClickListener(mClick);
		holder.txtAwayTxt2.setOnClickListener(mClick);
		return convertView;
	}

	void setScore(TextView tv, FootballEventData entity) {
		if (entity.score != null)
			tv.setText(entity.score);
		else
			tv.setText("");
	}

	/** 换人 */
	void setSustitutionEvent(FootballEventData entity, Holder holder) {
		if (entity.eventType == 1) {
			holder.txtHomeTxt1.setText(entity.player_name);// 换上
			holder.txtHomeTxt2.setVisibility(View.VISIBLE);
			holder.txtHomeTxt2.setText(entity.rel_alias);
			setIcon(holder.txtHomeTxt1, 0, R.drawable.icon_under);
			setIcon(holder.txtHomeTxt2, 0, R.drawable.icon_on);
		} else if (entity.eventType == 2) {
			holder.txtAwayTxt1.setText(entity.player_name);// 换上

			holder.txtAwayTxt2.setVisibility(View.VISIBLE);
			holder.txtAwayTxt2.setText(entity.rel_alias);
			setIcon(holder.txtAwayTxt1, R.drawable.icon_under, 0);
			setIcon(holder.txtAwayTxt2, R.drawable.icon_on, 0);
		}
	}

	/** 进球，红黄牌等 */
	void setOneLineMsg(FootballEventData entity, Holder holder) {
		if (entity.eventType == 1 ) {
			// 主队
			if (entity.player_name == null)
				holder.txtHomeTxt1.setText(entity.desc);
			else
				holder.txtHomeTxt1.setText(entity.player_name);
			setIcon(holder.txtHomeTxt1, 0, getIcon(entity.id));

		} else if (entity.eventType == 2) {
			// 客队
			if (entity.player_name == null)
				holder.txtAwayTxt1.setText(entity.desc);
			else
				holder.txtAwayTxt1.setText(entity.player_name);
			setIcon(holder.txtAwayTxt1, getIcon(entity.id), 0);

		}
		else //比赛开始
		{
			if(entity.desc!=null)
				holder.txtHomeTxt1.setText(entity.desc);
			holder.homeScore.setText("");
			setIcon(holder.txtHomeTxt1, 0, 0);
		}
		holder.txtHomeTxt2.setVisibility(View.GONE);
		holder.txtAwayTxt2.setVisibility(View.GONE);
		
		

	}

	int getIcon(int eventId) {
		switch (eventId) {
		case FootballEventData.EVENT_YELLOW_CARD:
			return R.drawable.icon_yellow;
		case FootballEventData.EVENT_RED_CARD:
			return R.drawable.icon_red;
		case FootballEventData.EVENT_GOAL:
			return R.drawable.icon_goal;
		case FootballEventData.EVENT_OWN_GOAL:
			return R.drawable.icon_own_goal;
		case FootballEventData.EVENT_PENALTY_GOAL:
			return R.drawable.icon_penalty;
		case FootballEventData.EVENT_SHOOT_OUT_MISS:
			return R.drawable.icon_penalty_1;

		case FootballEventData.EVENT_SHOOT_OUT_GOAL:
		case FootballEventData.EVENT_GAME_START:
		case FootballEventData.EVENT_GAME_END:
			return 0;
		}
		return 0;
	}

	int getStringRes(int eventId) {
		switch (eventId) {
		case FootballEventData.EVENT_SHOOT_OUT_GOAL:
			return R.string.football_shoot_out_goal;
		case FootballEventData.EVENT_GAME_START:
			return R.string.football_start;
		case FootballEventData.EVENT_GAME_END:
			return R.string.football_end;
		}
		return R.string.football_shoot_out_goal;
	}

	void setIcon(TextView tv, int drawableLeft, int drawableRight) {
		tv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0,
				drawableRight, 0);
	}

	static class Holder {

		public View home;
		public View away;
		// 第一列
		TextView txtHomeTxt1;
		TextView txtHomeTxt2;

		TextView txtAwayTxt1;
		TextView txtAwayTxt2;

		TextView txtTime;
		TextView homeScore;
		TextView awayScore;

		View lineTop;
		View lineBottom;

	}

}
