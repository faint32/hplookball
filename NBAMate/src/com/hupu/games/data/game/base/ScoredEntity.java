package com.hupu.games.data.game.base;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

/**助攻和进球榜*/
public class ScoredEntity extends BaseEntity {

	public int ranking;// 名次
	public String header_img;// 头像
	public String player_name;// 球员名称
	public String tid;// 球队ID
	public String goals;// 进球数（点球进球数）
	public String assits;// 助攻数

	@Override
	public void paser(JSONObject json) throws Exception {

		ranking =json.optInt("ranking", 0);
		header_img =json.getString("header_img");
		player_name =json.getString("player_name");
		tid =json.getString("tid");
		goals =json.getString("goals");
		assits=json.getString("assits");
	}

}
