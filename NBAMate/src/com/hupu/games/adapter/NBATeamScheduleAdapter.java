package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.basketball.NbaTeamDataEntity;
import com.hupu.games.data.game.basketball.NbaTeamScheduleReq;

public class NBATeamScheduleAdapter extends SectionedBaseAdapter {

	private HashMap<String, ArrayList<NbaTeamDataEntity>> mMapSchedule;
	private String[] mKeys;
	private LayoutInflater mInflater;

	int col1;
	int col2;
	int col3;
	public NBATeamScheduleAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		col1 = context.getResources().getColor(R.color.res_cor3);
		col2 = context.getResources().getColor(R.color.res_cor8);
		col3 = context.getResources().getColor(R.color.res_cor2);
	}

	public void setData(NbaTeamScheduleReq en) {
		if (en == null) {
			mMapSchedule = null;
			mKeys = null;
		} else {
			mMapSchedule = en.mDataMap;
			mKeys = en.blocks;
		}

		notifyDataSetChanged();
	}

	public void entry() {
		
	}

	

	static class Header {
		TextView txtDate;
	}

	@Override
	public NbaTeamDataEntity getItem(int section, int position) {
		if (section == -1 || position == -1)
			return null;
		if (mMapSchedule != null) {
			return mMapSchedule.get(mKeys[section]).get(position);
		}
		return null;
	}

	public NbaTeamDataEntity getItemAt(int pos) {
		if (mMapSchedule != null) {
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
		if (mKeys != null)
			return mKeys.length;
		return 0;
	}

	/** 清空所有数据 */
	public void clear() {
		if (mMapSchedule != null) {
			mMapSchedule.clear();
			mMapSchedule = null;
			mKeys = null;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCountForSection(int section) {

		if (mMapSchedule != null) {
			ArrayList<NbaTeamDataEntity> datas = mMapSchedule.get(mKeys[section]);
			return datas.size();
		}
		return 0;
	}

	class Holder {
		TextView txtDate;
		TextView txtHome;
		ImageView imgTeam;
		TextView txtScore;
		TextView txtTeam;
		TextView txtWin;
	}

	@Override
	public View getItemView(int section, int position, View contentView,
			ViewGroup parent) {

		NbaTeamDataEntity entity = getItem(section, position);
		Holder item = null;
		if (contentView == null) {
			//
			contentView = mInflater.inflate(R.layout.item_nba_team_schedule, null);
			item = new Holder();
			item.txtDate = (TextView) contentView.findViewById(R.id.txt_date);
			item.txtHome = (TextView) contentView
					.findViewById(R.id.txt_home_away);
			item.txtTeam = (TextView) contentView.findViewById(R.id.txt_team);
			item.txtWin = (TextView) contentView.findViewById(R.id.txt_win);
			item.txtScore = (TextView) contentView.findViewById(R.id.txt_score);
			item.imgTeam = (ImageView) contentView
					.findViewById(R.id.img_team_logo);
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		item.txtDate.setText(entity.begin_time);
		item.txtHome.setText(entity.side);
		item.txtTeam.setText(entity.vs_team_name);
		if (entity.is_win.equals("胜"))
			item.txtWin.setTextColor(col1);
		else if (entity.is_win.equals("负"))
			item.txtWin.setTextColor(col2);
		else
			item.txtWin.setTextColor(col3);
		
		item.txtWin.setText(entity.is_win);
		setIcon(item.imgTeam, entity.vs_tid);
		item.txtScore.setText(entity.score);
		return contentView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		Header header = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.header_team_schedule_date, null);
			header = new Header();
			header.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
//			header.txtDate =(TextView)convertView;
			convertView.setTag(header);
		} else
			header = (Header) convertView.getTag();
		if (mKeys != null)
			header.txtDate.setText(mKeys[section]+"赛程");
		return convertView;
	}

	private void setIcon(ImageView tv, int res) {
		// tv.setCompoundDrawablesWithIntrinsicBounds(
		// HuPuApp.getTeamData(res).i_logo_small, 0, 0, 0);
		tv.setImageResource(HuPuApp.getTeamData(res).i_logo_small);
	}
}
