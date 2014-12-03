package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.ChatEntity;
import com.hupu.games.data.game.basketball.NbaTeamDataEntity;
import com.pyj.adapter.BaseListAdapter;

/**
 * 
 * nba球员数据列表
 * @author panyongjun
 * */
public class NbaTeamInfoListAdapter extends BaseListAdapter<NbaTeamDataEntity> {

	int col1;
	int col2;
	int col3;

	public NbaTeamInfoListAdapter(Context context) {
		super(context);
		col1 = context.getResources().getColor(R.color.res_cor3);
		col2 = context.getResources().getColor(R.color.res_cor8);
		col3 = context.getResources().getColor(R.color.res_cor2);
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
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		NbaTeamDataEntity entity = getItem(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_nba_team, null);
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
		if(entity.side==null)
			item.txtHome.setVisibility(View.GONE);
		else
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

	private void setIcon(ImageView tv, int res) {
		// tv.setCompoundDrawablesWithIntrinsicBounds(
		// HuPuApp.getTeamData(res).i_logo_small, 0, 0, 0);
		tv.setImageResource(HuPuApp.getTeamData(res).i_logo_small);
	}
}
