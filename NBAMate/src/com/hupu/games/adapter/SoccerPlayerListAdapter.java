package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.football.SoccerPlayerEntity;
import com.hupu.games.data.game.football.SoccerPlayerReq;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * 球员列表
 * 
 * @author panyongjun
 * */
public class SoccerPlayerListAdapter extends SectionedBaseAdapter {

	private LayoutInflater mInflater;
	private LinkedHashMap<String, ArrayList<SoccerPlayerEntity>> playersMap;

	private String keys[]; 
	public SoccerPlayerListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	class Holder {
		TextView txtNum;
		ImageView imgPlayer;
		TextView txtName;
		TextView txMaketValue;
	}

	public void setData(SoccerPlayerReq data) {
		playersMap = data.mPlayerMap;
		keys =data.keys;
		notifyDataSetChanged();
	}

	public SoccerPlayerEntity getItemAt(int pos) {
		if (playersMap != null) {
			int section = getSectionForPosition(pos);
			int child = getPositionInSectionForPosition(pos);
			return getItem(section, child);
		}
		return null;
	}

	@Override
	public SoccerPlayerEntity getItem(int section, int position) {
		if (section == -1 || position == -1 || playersMap == null)
			return null;
		else
			return playersMap.get(keys[section]).get(position);

	}

	@Override
	public View getItemView(int section, int position, View contentView,
			ViewGroup parent) {
		Holder item = null;
		SoccerPlayerEntity entity = getItem(section, position);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_soccer_team_player,
					null);
			item = new Holder();
			item.txtNum = (TextView) contentView.findViewById(R.id.txt_num);
			item.txtName = (TextView) contentView
					.findViewById(R.id.player_name);
			item.txMaketValue = (TextView) contentView
					.findViewById(R.id.player_value);
			item.imgPlayer = (ImageView) contentView
					.findViewById(R.id.img_player);
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		HupuLog.d("name", entity.player_name);
		item.txtNum.setText(entity.number);
		item.txtName.setText(entity.player_name);
		item.txMaketValue.setText(entity.market_values);
		UrlImageViewHelper.setUrlDrawable(item.imgPlayer, entity.player_header,
				R.drawable.bg_1x1);
		return contentView;
	}

	@Override
	public long getItemId(int section, int position) {
		return 0;
	}

	@Override
	public int getSectionCount() {
		if (keys != null)
			return keys.length;
		return 0;
	}

	@Override
	public int getCountForSection(int section) {
		if (playersMap != null)
			return playersMap.get(keys[section]).size();
		return 0;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		String group = keys[section];
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.txt_soccer_group, null);
		}
		if(group!=null)
			((TextView) convertView.findViewById(R.id.txt_group)).setText(group);
		return convertView;
	}

}
