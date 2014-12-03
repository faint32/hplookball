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
import com.hupu.games.data.game.football.SoccerTeamDataEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;

/**
 * 
 * 足球球队赛程页
 * @author panyongjun
 * */
public class SoccerTeamInfoListAdapter extends BaseListAdapter<SoccerTeamDataEntity> {

	int col1;
	int col2;
	int col3;

	public SoccerTeamInfoListAdapter(Context context) {
		super(context);
		col1 = context.getResources().getColor(R.color.res_cor3);
		col2 = context.getResources().getColor(R.color.res_cor8);
		col3 = context.getResources().getColor(R.color.res_cor2);
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
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		SoccerTeamDataEntity entity = getItem(pos);
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
}
