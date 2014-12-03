package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.data.game.basketball.BasketBallGamesBlock;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.common.MyUtility;

public class BasketBallGamesListAdapter extends XSectionedBaseAdapter {

	private ArrayList<BasketBallGamesBlock> mListGames;

	private LayoutInflater mInflater;
	private OnClickListener mClick;

	/** 0 nba,1 cba */
	private int mode;

	private String STR_FORMAT = "%s<font color='red'>(%d)</font>";

	public BasketBallGamesListAdapter(Context context, OnClickListener c, int m) {
		mInflater = LayoutInflater.from(context);
		mClick = c;
		mode = m;
	}

	public void clear() {
		if (mListGames != null)
			mListGames.clear();
	}

	@Override
	public BasketballGameEntity getItem(int section, int position) {
		if (section == -1 || position == -1)
			return null;
		if (mListGames != null) {
			// 这里有个风险点，就是突然赛程中加入一场比赛的情况下 会出现奔溃 try catch 一下
			try {
				return mListGames.get(section).mGames.get(position);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return null;
	}

	public BasketballGameEntity getItemAt(int pos) {
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

	@Override
	public int getSectionCount() {
		if (mListGames != null)
			return mListGames.size();
		return 0;
	}

	@Override
	public int getCountForSection(int section) {
		if (mListGames != null)
			return mListGames.get(section).mGames.size();
		return 0;
	}

	@Override
	public View getItemView(int section, int position, View convertView,
			ViewGroup parent) {
		BasketballGameEntity entity = getItem(section, position);
		Holder holder = null;
		if (convertView == null) {
			convertView = initItemView(new Holder());
		}
		holder = (Holder) convertView.getTag();

		if (entity != null) {

			if (entity.home_series >= 0) {
				// /**季后赛大比分*/
				holder.txtLeftName.setText(Html.fromHtml(String.format(
						STR_FORMAT, entity.str_home_name, entity.home_series)));
				holder.txtRightName.setText(Html.fromHtml(String.format(
						STR_FORMAT, entity.str_away_name, entity.away_series)));
			} else {
				holder.txtLeftName.setText(entity.str_home_name);
				holder.txtRightName.setText(entity.str_away_name);
			}

			// 新增根据LOGO_URL设置球队logo
			if (entity.home_logo == null)
				setIcon(holder.imgLeftLogo, entity.i_home_tid);
			else
				UrlImageViewHelper.setUrlDrawable(holder.imgLeftLogo,
						entity.home_logo, R.drawable.bg_home_nologo1);
			if (entity.away_logo == null)
				setIcon(holder.imgRightLogo, entity.i_away_tid);
			else
				UrlImageViewHelper.setUrlDrawable(holder.imgRightLogo,
						entity.away_logo, R.drawable.bg_home_nologo1);

			updateViewByStatus(entity, holder, position);

		}
		return convertView;
	}

	private View initItemView(Holder holder) {
		//
		View convertView = mInflater.inflate(R.layout.item_nba_child, null);
		holder = new Holder();
		holder.txtLeftName = (TextView) convertView
				.findViewById(R.id.txt_team_left);
		holder.txtRightName = (TextView) convertView
				.findViewById(R.id.txt_team_right);

		holder.imgLeftLogo = (ImageView) convertView
				.findViewById(R.id.img_team_left);
		holder.imgRightLogo = (ImageView) convertView
				.findViewById(R.id.img_team_right);
		holder.txtProccess = (TextView) convertView
				.findViewById(R.id.txt_proccess);
		holder.txtScore = (TextView) convertView.findViewById(R.id.txt_score);

		// holder.followLayout = convertView.findViewById(R.id.layout_follow);
		holder.imgFollow = (ImageView) convertView
				.findViewById(R.id.img_follow);
		holder.imgLive = (ImageView) convertView.findViewById(R.id.img_live);
		convertView.setTag(holder);
		return convertView;
	}

	private void updateViewByStatus(BasketballGameEntity entity, Holder holder,
			int position) {
		byte status = getStatus(entity.byt_status);
		if (status == STATUS_WAITING) {
			// 等待开始
			holder.txtScore.setVisibility(View.GONE);
			holder.imgFollow.setVisibility(View.VISIBLE);

			if (entity.i_live < 1)
				holder.imgLive.setVisibility(View.INVISIBLE);
			else if (entity.i_live == 1) {
				holder.imgLive.setVisibility(View.VISIBLE);
				if (entity.casino == 1) {
					holder.imgLive.setImageResource(R.drawable.icon_guess_up);
				} else {
					holder.imgLive.setImageResource(R.drawable.icon_live_up);
				}
			}
			holder.txtProccess.setText(MyUtility
					.getStartTime(entity.l_begin_time * 1000));
			holder.imgFollow.setTag(entity);
			holder.imgFollow.setOnClickListener(mClick);
			if (entity.bFollow == 1) {
				holder.imgFollow.setImageResource(R.drawable.btn_dated);
			} else {
				holder.imgFollow.setImageResource(R.drawable.btn_date);
			}
		} else if (status == STATUS_START) {
			// 已经开始
			holder.txtScore.setVisibility(View.VISIBLE);
			// holder.imgFollow.setVisibility(View.GONE);

			if (entity.i_live < 1)
				holder.imgLive.setVisibility(View.INVISIBLE);
			else if (entity.i_live == 1) {
				holder.imgLive.setVisibility(View.VISIBLE);
				if (entity.casino == 1) {
					holder.imgLive.setImageResource(R.drawable.icon_guess_down);
				} else {
					holder.imgLive.setImageResource(R.drawable.icon_live_down);
				}
			}
			setSore(holder.txtScore, entity.i_home_score, entity.i_away_score);
			if (entity.str_process != null)
				holder.txtProccess.setText(entity.str_process);
		} else {
			// 结束
			holder.imgFollow.setVisibility(View.GONE);
			holder.txtScore.setVisibility(View.VISIBLE);
			holder.imgLive.setVisibility(View.INVISIBLE);
			setSore(holder.txtScore, entity.i_home_score, entity.i_away_score);
			if (entity.str_process != null)
				holder.txtProccess.setText(entity.str_process);
		}
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
			header.txtRound = (TextView) convertView
					.findViewById(R.id.txt_round);
			convertView.setTag(header);
		} else
			header = (Header) convertView.getTag();
		if (mListGames != null) {
			header.txtDate.setText(mListGames.get(section).mDateBlock);
			header.txtRound.setText(mListGames.get(section).type_block);
		}
		return convertView;
	}

	public void setData(ArrayList<BasketBallGamesBlock> gameList) {
		mListGames = gameList;
		notifyDataSetChanged();
	}

	private void setIcon(ImageView tv, int res) {
		tv.setImageResource(HuPuApp.getTeamData(res).i_logo_small);
	}

	private void setSore(TextView tv, int homeScore, int awayScore) {
		tv.setText(homeScore + "-" + awayScore);
	}

	final static byte STATUS_START = 1;
	final static byte STATUS_WAITING = 2;
	final static byte STATUS_END = 3;

	private byte getStatus(byte status) {
		if (mode == 0) {
			if (status == BasketballGameEntity.STATUS_WAITING)
				return STATUS_WAITING;
			else if (status == BasketballGameEntity.STATUS_START)
				return STATUS_START;
			else
				return STATUS_END;
		} else {
			if (status == BasketballGameEntity.CBA_STATUS_WAITING)
				return STATUS_WAITING;
			else if (status == BasketballGameEntity.CBA_STATUS_START)
				return STATUS_START;
			else
				return STATUS_END;
		}
	}

	static class Holder {

		TextView txtLeftName;
		TextView txtRightName;

		ImageView imgLeftLogo;
		ImageView imgRightLogo;

		TextView txtProccess;
		TextView txtScore;

		ImageView imgFollow;
		ImageView imgLive;
		// View followLayout;
	}

	static class Header {
		TextView txtDate;
		TextView txtRound;
	}

}
