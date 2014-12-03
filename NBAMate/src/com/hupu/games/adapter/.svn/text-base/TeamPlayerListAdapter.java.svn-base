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
import com.hupu.games.data.game.basketball.NbaTeamPlayerEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;

/**
 * nba球员信息
 * 
 * @author panyongjun
 * */
public class TeamPlayerListAdapter extends BaseListAdapter<NbaTeamPlayerEntity> {


	public TeamPlayerListAdapter(Context context) {
		super(context);
	}

	class Holder {
		TextView txtNum;
		ImageView imgPlayer;
		TextView txtName;
		TextView txtPosition;
		TextView txSalary;
	}

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		NbaTeamPlayerEntity entity = getItem(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_nba_team_player, null);
			item = new Holder();
			item.txtNum = (TextView) contentView.findViewById(R.id.txt_num);
			item.txtName = (TextView) contentView.findViewById(R.id.player_name);
			item.txtPosition = (TextView) contentView.findViewById(R.id.player_position);
			item.txSalary = (TextView) contentView.findViewById(R.id.player_salary);
			item.imgPlayer = (ImageView) contentView.findViewById(R.id.img_player);
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}

		item.txtNum.setText(entity.number);
		item.txtName.setText(entity.player_name);
		item.txtPosition.setText(entity.position);
		item.txSalary.setText(entity.salary);
		UrlImageViewHelper.setUrlDrawable(item.imgPlayer,entity.player_header,R.drawable.bg_1x1);
		return contentView;
	}

}
