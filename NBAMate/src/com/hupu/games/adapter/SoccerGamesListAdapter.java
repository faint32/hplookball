package com.hupu.games.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.data.game.football.SoccerGamesBlock;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.common.MyUtility;

public class SoccerGamesListAdapter extends XSectionedBaseAdapter {

	private ArrayList<SoccerGamesBlock> mListGames;
	private LayoutInflater mInflater;
	OnClickListener mClick;

	public SoccerGamesListAdapter(Context context, OnClickListener click) {
		mInflater = LayoutInflater.from(context);
		mClick = click;
	}

	public void setData(ArrayList<SoccerGamesBlock> en) {
		mListGames=en;
		notifyDataSetChanged();
	}

	static class Holder {
		// 第一列
		TextView txtTeamLeft;
		TextView txtTeamRight;
		TextView txtScore;
		// 点球比分
		TextView txtShootout1;
		TextView txtShootout2;
		ImageView imgFollow;
		// 第二列
		TextView txtProccess;
		ImageView imgTeamLeft;
		ImageView imgTeamRight;

		ImageView imgLive;
	}

	static class Header {
		TextView txtDate;
		TextView txtRound;
	}

	@Override
	public ScoreboardEntity getItem(int section, int position) {
		if (section == -1 || position == -1)
			return null;
		if (mListGames != null) {
			return mListGames.get(section).mGames.get(position);
		}
		return null;
	}

	public ScoreboardEntity getItemAt(int pos) {
		if (mListGames != null) {
			int section = getSectionForPosition(pos);
			int child = getPositionInSectionForPosition(pos);
			return getItem(section, child);

		}
		return null;
	}

	@Override
	public long getItemId(int section, int position) {

		return 0;
	}

	int dif;

	/** 更新时间 */
	public void updateTime() {
		dif++;
		notifyDataSetChanged();
	}

	public void initTime() {
		dif = 0;
		notifyDataSetChanged();
	}

	@Override
	public int getSectionCount() {
		if (mListGames != null)
			return mListGames.size();
		return 0;
	}

	/** 清空所有数据 */
	public void clear() {
		if (mListGames != null) {
			mListGames.clear();
			mListGames = null;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCountForSection(int section) {

		if (mListGames != null) {
			
			return mListGames.get(section).mGames.size();
		}
		return 0;
	}

	public static SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"H:mm开球", java.util.Locale.CHINESE);

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {

		ScoreboardEntity entity = getItem(section, position);
		Holder holder = null;
		if (convertView == null) {
			//
			convertView = mInflater.inflate(R.layout.item_football_child, null);
			holder = new Holder();
			holder.txtTeamLeft = (TextView) convertView
					.findViewById(R.id.txt_team_left);
			holder.txtTeamRight = (TextView) convertView
					.findViewById(R.id.txt_team_right);
			holder.txtProccess = (TextView) convertView
					.findViewById(R.id.txt_proccess);
			holder.txtScore = (TextView) convertView
					.findViewById(R.id.txt_score);

			holder.imgTeamLeft = (ImageView) convertView
					.findViewById(R.id.img_team_left);
			holder.imgTeamRight = (ImageView) convertView
					.findViewById(R.id.img_team_right);
			holder.imgFollow = (ImageView) convertView
					.findViewById(R.id.img_follow);
			holder.imgFollow.setOnClickListener(mClick);

			holder.txtShootout1 = (TextView) convertView
					.findViewById(R.id.txt_shootout1);
			holder.txtShootout2 = (TextView) convertView
					.findViewById(R.id.txt_shootout2);
			holder.imgLive = (ImageView) convertView
					.findViewById(R.id.img_live);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.imgFollow.setTag(entity);

		UrlImageViewHelper.setUrlDrawable(holder.imgTeamLeft, entity.home_logo,
				R.drawable.bg_home_nologo1);
		UrlImageViewHelper.setUrlDrawable(holder.imgTeamRight,
				entity.away_logo, R.drawable.bg_home_nologo1);
		holder.txtTeamLeft.setText(entity.str_home_name);
		holder.txtTeamRight.setText(entity.str_away_name);
		// Log.d("getItemView", "game code=" + entity.code);
		holder.txtProccess.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		switch (entity.code) {
		case ScoreboardEntity.STATUS_START:
		case ScoreboardEntity.STATUS_END:
			holder.imgFollow.setVisibility(View.GONE);
			holder.txtScore.setVisibility(View.VISIBLE);
			// 比分
			String s = entity.i_home_score + " - " + entity.i_away_score;
			holder.txtScore.setText(s);
			if (entity.code == ScoreboardEntity.STATUS_START) {
				// 已经开始
				showTime(entity, holder.txtProccess); // 时间
				if (entity.period == 8) {
					// 显示点球比分
					HupuBaseActivity.showShootOut(holder.txtShootout1,
							holder.txtShootout2, entity.home_out_goals,
							entity.away_out_goals);

				} else {
					HupuBaseActivity.hideShootOut(holder.txtShootout1,
							holder.txtShootout2);
					if (entity.period == 5 || entity.period == 6) {
						// 5加时赛上，6加时赛下
					}
				}
			} else {
				holder.txtProccess.setText(entity.str_desc);
				if (entity.home_out_goals>0 || entity.away_out_goals>0) {
					// 结束后没有period字段，只能通过比分来判断。
					HupuBaseActivity.showShootOut(holder.txtShootout1,
							holder.txtShootout2, entity.home_out_goals,
							entity.away_out_goals);
					holder.txtProccess.setCompoundDrawablesWithIntrinsicBounds(
							0, 0, R.drawable.ic_penalty, 0);

				} else {
					if (entity.is_extra>0)
					{
						holder.txtProccess.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_overtime, 0);
					}
					HupuBaseActivity.hideShootOut(holder.txtShootout1,
							holder.txtShootout2);
				}
			}

			break;
		case ScoreboardEntity.STATUS_NOT_START:
		case ScoreboardEntity.STATUS_EXTEND:
		case ScoreboardEntity.STATUS_CANCEL:
			holder.txtScore.setVisibility(View.GONE);
			holder.txtShootout2.setVisibility(View.GONE);
			holder.txtShootout1.setVisibility(View.GONE);
			holder.imgFollow.setVisibility(View.VISIBLE);
			if (entity.bFollow == 1)
				holder.imgFollow.setImageResource(R.drawable.btn_dated);
			else
				holder.imgFollow.setImageResource(R.drawable.btn_date);

			if (entity.code == ScoreboardEntity.STATUS_NOT_START) {
				// 显示开赛时间
				holder.txtProccess.setText(MyUtility.getStartTime(
						entity.l_begin_time * 1000, sdf));
			} else {
				holder.txtProccess.setText(entity.str_desc);
			}
			break;
		}
		if (entity.i_live < 1 || ScoreboardEntity.STATUS_CANCEL == entity.code
				|| ScoreboardEntity.STATUS_END == entity.code) {
			holder.imgLive.setVisibility(View.INVISIBLE);
		} else {
			// HupuLog.d("status=" + entity.code);
			if (ScoreboardEntity.STATUS_NOT_START == entity.code) {
				holder.imgLive.setVisibility(View.VISIBLE);
				if (entity.casino == 1) {
					holder.imgLive.setImageResource(R.drawable.icon_guess_up);
				} else {
					holder.imgLive.setImageResource(R.drawable.icon_live_up);
				}

			} else {

				holder.imgLive.setVisibility(View.VISIBLE);
				if (entity.casino == 1) {
					holder.imgLive.setImageResource(R.drawable.icon_guess_down);
				} else {
					holder.imgLive.setImageResource(R.drawable.icon_live_down);
				}

			}
		}
		// convertView.setBackgroundResource(R.drawable.selector_football_game_list);

		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		Header header = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.item_football_header, null);
			header = new Header();
			header.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
			header.txtRound= (TextView) convertView.findViewById(R.id.txt_round);
			convertView.setTag(header);
		} else
			header = (Header) convertView.getTag();
		if (mListGames != null)
		{
			header.txtDate.setText(mListGames.get(section).mDateBlock);
			header.txtRound.setText(mListGames.get(section).type_block);
		}
		return convertView;
	}

	/** 需要自己计算时间 */
	private void showTime(ScoreboardEntity entity, TextView tv) {
//		HupuLog.d("showTime=" + entity.process);
		HupuBaseActivity.showTime(entity, tv, dif);
	}
}
