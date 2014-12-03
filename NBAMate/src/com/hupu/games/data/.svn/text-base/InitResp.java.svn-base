package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;

/**
 * 
 * @author papa
 * 
 *         初始化
 */
public class InitResp extends BaseEntity {
	public LinkedList<LeaguesEntity> mList;

	public String nickName;

	public String uuid;

	public int showFollow = 0;
	public int showBind = 0;

	public String redirectors[];

	public String adImg; // 广告图片
	public int adShowTime;// 广告显示时间
	public String adLink; // 广告链接
	public int adShow; // 广告是否显示 1显示/预加载
	public String adTitle; // 广告是否显示 1显示/预加载

	public String dialogQuize; // 竞猜时提示去绑定的提示
	public String dialogMyPrize; // 我的奖品提示去绑定的提示
	public String dialogExchange; // 兑换时提示去绑定的提示
	public String dialogPrizeListToMyPrize; // 奖品列表跳我的奖品提示去绑定的提示
	public String dialogTask; // 任务提示去绑定的提示

	@Override
	public void paser(JSONObject json) throws Exception {
		SharedPreferencesMgr.setInt("is_login", json.optInt("is_login"));
		// TODO Auto-generated method stub
		json = json.optJSONObject(KEY_RESULT);

		JSONObject ad = json.optJSONObject("core");
		if (ad != null) {
			adImg = ad.optString("img");
			adShowTime = ad.optInt("show_time");
			adLink = ad.optString("link");
			adShow = ad.optInt("show");
			adTitle = ad.optString("title");
		}
		ad = null;

		SharedPreferencesMgr.setString("sdv", json.optString("sdv"));
		SharedPreferencesMgr.setInt("is_lite", json.optInt("is_lite"));
		SharedPreferencesMgr.setInt("is_maa", json.optInt("is_maa"));
		SharedPreferencesMgr.setInt("show_vip", json.optInt("show_vip"));
		SharedPreferencesMgr.setInt("show_mall", json.optInt("show_mall"));
		SharedPreferencesMgr.setInt("show_bind", json.optInt("show_bind"));
		showBind = json.optInt("show_bind");
		// 支付需要的状态
		SharedPreferencesMgr.setInt("mobile_pay", json.optJSONObject("pay")
				.optInt("mobile_pay"));

		// 绑定手机提示信息 相关备注在
		// http://wiki.hc.dev/pages/viewpage.action?pageId=58884138
		if (json.optJSONObject("lang") != null) {

			JSONObject lang = json.optJSONObject("lang");
			setValue(lang, "dialogQuize", "casino_bet_login_alert"); // 竞猜时提示去绑定的提示
			setValue(lang, "dialogMyPrize", "my_myprize_login_alert");// 我的奖品提示去绑定的提示
			setValue(lang, "dialogExchange",
					"my_prizelist_single_exchange_login_alert");// 兑换时提示去绑定的提示
			setValue(lang, "dialogPrizeListToMyPrize",
					"my_prizelist_myprize_login_alert"); // 奖品列表跳我的奖品提示去绑定的提示
			setValue(lang, "dialogTask", "my_task_item_login_alert");// 任务提示去绑定的提示
			setValue(lang, "dialogRecharge", "casino_bet_login_alert");// 直播按钮提示去绑定的提示
			setValue(lang, "dialogBtnText", "casino_list_award_login_alert"); // 直播按钮文字
			setValue(lang, "BindBtnText", "casino_list_bind_mobile_btn");// 直播按钮文字
			setValue(lang, "loginTipsGrey", "my_account_login_tips_grey");// 未登录状态下
			setValue(lang, "loginTipsRed", "my_account_login_tips_red"); // 未登录状态下																		// 下面提示文字红色部分
			setValue(lang, "bindTips", "my_account_binding_tips"); // 登录状态下绑定下面提示文字
			setValue(lang, "logoutAlert", "my_account_logout_alert"); // 退出登录
			setValue(lang, "unboundAlert", "my_account_unbound_alert"); // 解除绑定
			setValue(lang, "tokenExpires", "my_account_token_expires"); // token过期
			setValue(lang, "caipiaoDeclaration",
					"casino_list_caipiao_declaration"); // 彩票的说明
			setValue(lang, "caipiaoTips", "caipiao_alert_expBonus_tips"); // 下注的预期收入说明
			setValue(lang, "caipiaoLoginAlert", "casino_caipiao_login_alert"); // 下注的预期收入说明
			
			//6.0.0新加语言包
			setValue(lang, "accountNicknameIntro", "my_account_nickname_intro"); // 
			setValue(lang, "soccerScoreboardTips", "soccer_scoreboard_tips"); // 
			setValue(lang, "myAccountNicknameTips", "my_account_nickname_tips"); // 
			setValue(lang, "caipiaoLoginAlert", "casino_caipiao_login_alert"); // 
			setValue(lang, "caipiaoDiscountTips", "my_caipiao_discount_tips"); // 我的彩票下面 tips
			setValue(lang, "leagueScoreboardTips", "league_scoreboard_tips"); // 五大联赛及欧冠的积分榜页面提示
			setValue(lang, "webviewLoginAlert", "webview_login_alert"); // 所有的webview页面，调用该登录提示（如论坛、彩票）
			setValue(lang, "caipiaoLoseAlert", "caipiao_list_loseMoney_alert"); // 彩票下注赔率过低的提示语言包

		}

		JSONArray ss = json.optJSONArray("redirector");
		if (ss != null) {
			int size = ss.length();
			redirectors = new String[size];
			for (int i = 0; i < size; i++) {
				redirectors[i] = ss.getString(i);
			}
		}
		ss = null;

		JSONObject client = json.optJSONObject("client");
		if (client != null) {
			uuid = client.optString("uuid");
			showFollow = client.optInt("show_follow");
			nickName = client.optString("nickname");
			// 存储数据版本号
			HupuLog.e("papa", "sugar----" + client.optString("sugar"));
			SharedPreferencesMgr.setString("sugar", client.optString("sugar"));
			SharedPreferencesMgr.setInt("uid", client.optInt("uid"));
			SharedPreferencesMgr.setBoolean("is_push",
					client.optInt("is_notific") == 0 ? false : true);

			JSONArray bindArray = client.optJSONArray("bind");
			if (bindArray != null) {
				BindEntity bind;
				for (int i = 0; i < bindArray.length(); i++) {
					bind = new BindEntity();
					bind.paser(bindArray.getJSONObject(i));
					SharedPreferencesMgr.setInt("channel" + bind.channel,
							bind.is_bind);
					if (bind.channel == 1) {
						SharedPreferencesMgr.setString("bp", bind.bind_name);
					}
					if (bind.channel == 2) {
						SharedPreferencesMgr.setString("qq_name",
								bind.bind_name);
					}
				}
			}
		}
		client = null;

		JSONArray array = json.optJSONArray("leagues");

		if (array != null) {
			mList = new LinkedList<LeaguesEntity>();
			int size = array.length();
			LeaguesEntity league;
			for (int i = 0; i < size; i++) {
				league = new LeaguesEntity();
				league.paser(array.getJSONObject(i));
				mList.add(league);
			}

			array = null;
			json = null;
		}
	}

	private void setValue(JSONObject lang, String key1, String key2) {
		SharedPreferencesMgr.setString(key1, lang.optString(key2));
	}
}
