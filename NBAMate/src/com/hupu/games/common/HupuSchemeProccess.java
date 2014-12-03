package com.hupu.games.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.BasketballActivity;
import com.hupu.games.activity.FootballGameActivity;
import com.hupu.games.activity.FootballPlayerInfoActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.NBAGameActivity;
import com.hupu.games.activity.NBAPlayerInfoActivity;
import com.hupu.games.activity.NBATeamActivity;
import com.hupu.games.activity.NewsDetailActivity;
import com.hupu.games.activity.NickNameActivity;
import com.hupu.games.activity.SoccerEventsActivity;
import com.hupu.games.activity.SoccerLiveActivity;
import com.hupu.games.activity.SoccerTeamActivity;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.casino.AuthDialog;
import com.hupu.games.pay.AccountActivity;

public class HupuSchemeProccess {

	/**
	 * 根据自定义的scheme规则进行跳转 kanqiu://联赛模板/联赛/模块/数字ID （数字ID可以为空）
	 * kanqiu://soccerleagues/csl/news/123445
	 * kanqiu://soccerleagues/csl/events/180405(gid)
	 * kanqiu://soccercupleagues/worldcup/events/10015(gid) 球队：
	 * kanqiu://soccercupleagues/worldcup/team/1[teamid] 球员：
	 * kanqiu://soccercupleagues/worldcup/player/1[playerid] 教练：
	 * kanqiu://soccercupleagues/worldcup/coach/1[coachid]
	 * kanqiu://soccercupleagues/worldcup/casino/gid
	 * 
	 * @param -1传人的参数有误 0不处理 1成功处理
	 * */

	public static int proccessScheme(Context context, Uri uri) {
		// uri=Uri.parse("kanqiu://soccerleagues/epl/chat/1328922");
		HupuScheme scheme = new HupuScheme();
		scheme.paser(uri);
		HupuLog.d("uri=" + uri.toString());

		// 如果是新闻，直接跳转
		if (HuPuRes.SUB_TAB_NEWS.equalsIgnoreCase(scheme.mode)) {
			if (scheme.id <= 0)// 为了应付董大师的高端测试
				return -1;
			Intent in = new Intent(context, NewsDetailActivity.class);
			in.putExtra("nid", (long) scheme.id);
			in.putExtra("reply", 0);
			in.putExtra("tag", scheme.game);
			in.putExtra("Query", scheme.getQuery());
			// in.putExtra("cntag", cnTag);
			context.startActivity(in);
			return 1;
		}
		
		if (HuPuRes.TEMPLATE_ACCOUNT.equalsIgnoreCase(scheme.template)) {
			if (HuPuRes.SUB_TAB_ACCOUNT.equals(scheme.game)) {
//				Intent intent = new Intent(context, AccountActivity.class);
//				context.startActivity(intent);
				((HupuBaseActivity)context).showBindDialog(SharedPreferencesMgr.getString("webviewLoginAlert",
						context.getString(R.string.bind_phone_dialog)));
//				AuthDialog dialog = new AuthDialog(context, (HupuBaseActivity)context,
//						dialogTitle);
//				dialog.show();
				
			}else if (HuPuRes.SUB_TAB_NICKNAME.equals(scheme.game)) {
				Intent intent = new Intent(context, NickNameActivity.class);
				context.startActivity(intent);
			}
			return 1;
		}

		if (HuPuRes.TAB_VIDEO.equalsIgnoreCase(scheme.mode)) {
			Intent in = new Intent(context, WebViewActivity.class);
			in.putExtra("url", scheme.getParameter("url"));
			in.putExtra("source", scheme.getParameter("source"));// 视频的真实地址
			in.putExtra("content", scheme.getParameter("title"));// 分享的内容
			context.startActivity(in);
			return 1;
		}
		if (HuPuRes.SUB_TAB_EVENTS.equalsIgnoreCase(scheme.mode)) {
			if (scheme.id <= 0)
				return -1;
			if (context instanceof FootballGameActivity)// 如果是从足球战报跳转
				((FootballGameActivity) context).toEventsActivity(scheme);
			else {// 从其他方式跳转，比如新闻，通知等
				Intent in = new Intent(context, SoccerEventsActivity.class);
				in.putExtra("gid", scheme.id);
				in.putExtra("tag", scheme.game);
				context.startActivity(in);
			}
			return 1;
		}

		if (HuPuRes.SUB_TAB_CASINO.equals(scheme.mode)) {
			if (scheme.id <= 0)
				return -1;
			Intent in = null;
			if (HuPuRes.TEMPLATE_SOCCER_LEAGUE
					.equalsIgnoreCase(scheme.template)
					|| HuPuRes.TEMPLATE_SOCCER_CUP_LEAGUE
							.equalsIgnoreCase(scheme.template)) {
				in = new Intent(context, FootballGameActivity.class);
			} else if (scheme.template.equals(HuPuRes.TEMPLATE_NBA)) {
				in = new Intent(context, NBAGameActivity.class);
			} else if (scheme.template.equals(HuPuRes.TEMPLATE_CBA)) {
				in = new Intent(context, BasketballActivity.class);
			}

			in.putExtra("gid", scheme.id);
			in.putExtra("tag", scheme.game);
			in.putExtra("tab", BaseGameActivity.TAB_GUESS);
			context.startActivity(in);
			return 1;
		}
		if (HuPuRes.TEMPLATE_NBA.equalsIgnoreCase(scheme.template)) {
			if (HuPuRes.SUB_TAB_PLAYER.equalsIgnoreCase(scheme.mode)) {
				// 球员数据
				// ((HupuBaseActivity) getContext()).sendUmeng(
				// HuPuRes.UMENG_EVENT_NBAPLAYERS, HuPuRes.UMENG_KEY_ENTRANCE,
				// HuPuRes.UMENG_VALUE_NBA_STATS);
				if (scheme.id <= 0)// 为了应付董大师的高端测试
					return -1;
				Intent in = new Intent(context, NBAPlayerInfoActivity.class);
				in.putExtra("pid", scheme.id);
				context.startActivity(in);
				return 1;
			} else if (HuPuRes.SUB_TAB_TEAM.equalsIgnoreCase(scheme.mode)) {
				// 球队
				if (scheme.id <= 0)// 为了应付董大师的高端测试
					return -1;
				Intent in = new Intent(context, NBATeamActivity.class);
				in.putExtra("tid", scheme.id);
				context.startActivity(in);
				return 1;
			}
		}
		if (HuPuRes.TEMPLATE_SOCCER_LEAGUE.equalsIgnoreCase(scheme.template)
				|| HuPuRes.TEMPLATE_SOCCER_CUP_LEAGUE
						.equalsIgnoreCase(scheme.template)) {
			// 足球分类
			if (HuPuRes.SUB_TAB_PLAYER.equalsIgnoreCase(scheme.mode)) {
				if (scheme.id <= 0)// 为了应付董大师的高端测试
					return -1;
				Intent in = new Intent(context,
						FootballPlayerInfoActivity.class);
				in.putExtra("tag", scheme.game);
				in.putExtra("pid", scheme.id);
				context.startActivity(in);
				return 1;
			} else if (HuPuRes.SUB_TAB_TEAM.equalsIgnoreCase(scheme.mode)) {
				// 球队
				if (scheme.id <= 0)// 为了应付董大师的高端测试
					return -1;
				Intent in = new Intent(context, SoccerTeamActivity.class);
				in.putExtra("tag", scheme.game);
				in.putExtra("tid", scheme.id);
				context.startActivity(in);
				return 1;
			} else if (HuPuRes.SUB_TAB_RATINGS.equalsIgnoreCase(scheme.mode)) {

			} else if (HuPuRes.SUB_TAB_LIVE.equalsIgnoreCase(scheme.mode)) {
				Intent in = new Intent(context, SoccerLiveActivity.class);
				in.putExtra("gid", scheme.id);
				in.putExtra("tag", scheme.game);
				context.startActivity(in);
			}

		}
		
		return 0;
	}
}
