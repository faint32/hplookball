package com.hupu.games.data.game.football;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;


public class FootballCoachInfoReq extends BaseEntity {

	public int tid; // 球队id
	public String t_name; // 中文短名称
	public String t_full_name; // 中文全名称

	public int c_id; // 球员ID
	public String c_s_name; // 简称
	public String c_name; // 中文名
	public String c_en_name; // 英文名称
	public String c_header; // 头像
	public String c_role; // 号码
	public String intro; // 介绍
	public String detail; // 详情

	@Override
	public void paser(JSONObject json) throws Exception {

		JSONObject obj = json.getJSONObject(KEY_RESULT);
		if (obj != null) {
			JSONObject temp = obj.optJSONObject("team_info");
			if (temp != null) {
				tid = temp.optInt("tid");
				t_name = temp.optString("name");
				t_full_name = temp.optString("full_name");
			}

			JSONObject profile = obj.optJSONObject("profile");
			if (profile != null) {
				c_id = profile.optInt("coach_id");
				c_role = profile.optString("role");
				c_name = profile.optString("coach_full_name");
				c_s_name = profile.optString("coach_name");
				c_en_name = profile.optString("coach_enname");
				c_header = profile.optString("coach_header");
				intro = profile.optString("intro");
				detail = profile.optString("detail");
			}
			
		}
	}

}
