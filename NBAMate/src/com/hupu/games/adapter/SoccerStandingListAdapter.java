package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.PlayerRatingByUserEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 足球排行
 * */
public class SoccerStandingListAdapter extends BaseListAdapter<PlayerRatingByUserEntity> {


	int clrLiked;
	public SoccerStandingListAdapter(Context context) {
		super(context);
	}

	public int lastSoid;	
	@Override
	public void setData(ArrayList<PlayerRatingByUserEntity> data) {
		super.setData(data);

	}


	class Holder {

		/** 球队名字 */
		TextView txtTeam;
		/** 球员 */
		TextView txtPlayer;
		/** 排名 */
		TextView txtRank;
		/** 进球数 或 助攻 */
		TextView txtScored;

	}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		PlayerRatingByUserEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_player_rate,
					null);
			item = new Holder();
	
			item.txtTeam = (TextView) contentView
					.findViewById(R.id.txt_rating_name);
			item.txtPlayer = (Button) contentView
					.findViewById(R.id.btn_like);
			item.txtRank = (TextView) contentView
					.findViewById(R.id.txt_rating_score);
			item.txtScored = (TextView) contentView.findViewById(R.id.txt_rating_desc);

			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
	
		
		return contentView;
	}

}
