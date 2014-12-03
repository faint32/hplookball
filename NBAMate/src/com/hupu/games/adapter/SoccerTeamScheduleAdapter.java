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
import com.hupu.games.adapter.SoccerTeamInfoListAdapter.Holder;
import com.hupu.games.data.game.basketball.NbaTeamDataEntity;
import com.hupu.games.data.game.football.SoccerTeamDataEntity;
import com.hupu.games.data.game.football.SoccerTeamScheduleReq;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class SoccerTeamScheduleAdapter extends SectionedBaseAdapter {

	private HashMap<String, ArrayList<SoccerTeamDataEntity>> mMapSchedule;
	private String[] mKeys;
	private LayoutInflater mInflater;

	int col1;
	int col2;
	int col3;
	public SoccerTeamScheduleAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		col1 = context.getResources().getColor(R.color.res_cor3);
		col2 = context.getResources().getColor(R.color.res_cor8);
		col3 = context.getResources().getColor(R.color.res_cor2);
	}

	public void setData(SoccerTeamScheduleReq en) {
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
	public SoccerTeamDataEntity getItem(int section, int position) {
		if (section == -1 || position == -1)
			return null;
		if (mMapSchedule != null) {
			return mMapSchedule.get(mKeys[section]).get(position);
		}
		return null;
	}

	public SoccerTeamDataEntity getItemAt(int pos) {
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
			ArrayList<SoccerTeamDataEntity> datas = mMapSchedule.get(mKeys[section]);
			return datas.size();
		}
		return 0;
	}

	class Holder {
		TextView txtDate;
		TextView txtStage;
		ImageView imgTeam;
		TextView txtScore;
		TextView txtShootOut;
		TextView txtTeam;
		TextView txtWin;
		ImageView imgStatus;
		TextView txtHome;
	}

	@Override
	public View getItemView(int section, int position, View contentView,
			ViewGroup parent) {

		Holder item = null;
		SoccerTeamDataEntity entity = getItem(section,position);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_soccer_team, null);
			item = new Holder();
			item.txtDate = (TextView) contentView.findViewById(R.id.txt_date);
			item.txtHome = (TextView) contentView
					.findViewById(R.id.txt_home_away);
			item.txtStage= (TextView) contentView.findViewById(R.id.txt_stage);
			item.txtShootOut = (TextView) contentView
					.findViewById(R.id.txt_shootout);
			item.txtTeam = (TextView) contentView.findViewById(R.id.txt_team);
			item.txtWin = (TextView) contentView.findViewById(R.id.txt_win);
			item.txtScore = (TextView) contentView.findViewById(R.id.txt_score);
			item.imgTeam = (ImageView) contentView
					.findViewById(R.id.img_team_logo);
			item.imgStatus = (ImageView) contentView
					.findViewById(R.id.img_status);
			
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}

		item.txtDate.setText(entity.begin_time);
		item.txtHome.setText(entity.side);
//		if(entity.side==null)
//			item.txtHome.setVisibility(View.GONE);
//		else
//			item.txtHome.setText(entity.side);
		item.txtStage.setText(entity.stage);
		item.txtTeam.setText(entity.vs_team_name);
		setIcon(item.imgTeam, entity.vs_team_logo);
		
		if (entity.is_win.equals("胜"))
			item.txtWin.setTextColor(col1);
		else if (entity.is_win.equals("负"))
			item.txtWin.setTextColor(col2);
		else
			item.txtWin.setTextColor(col3);
		item.txtWin.setText(entity.is_win);	
//		entity.score="1-1";
		item.txtScore.setText(entity.score);
//		entity.penalty_score="3-1";
		if(entity.penalty_score!=null && !entity.penalty_score.equals(""))
		{
			//点球
			item.txtShootOut.setVisibility(View.VISIBLE);
			item.txtShootOut.setText("("+entity.penalty_score+")");
			item.imgStatus.setVisibility(View.VISIBLE);
			item.imgStatus.setImageResource(R.drawable.ic_penalty);
		}
		else 
		{
			if(entity.is_extra==1)
			{//加时
				item.imgStatus.setVisibility(View.VISIBLE);
				item.imgStatus.setImageResource(R.drawable.ic_overtime);
			}
			else
				item.imgStatus.setVisibility(View.GONE);
			item.txtShootOut.setVisibility(View.GONE);
		}

		return contentView;
	}

	private void setIcon(ImageView tv, String url) {
		// tv.setCompoundDrawablesWithIntrinsicBounds(
		// HuPuApp.getTeamData(res).i_logo_small, 0, 0, 0);
		UrlImageViewHelper.setUrlDrawable(tv,url,R.drawable.bg_home_nologo);
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
		try {
			if (mKeys != null)
				header.txtDate.setText(mKeys[section]+"赛程");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return convertView;
	}

}
