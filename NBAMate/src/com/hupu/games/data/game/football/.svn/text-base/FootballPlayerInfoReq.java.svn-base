package com.hupu.games.data.game.football;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;


public class FootballPlayerInfoReq extends BaseEntity {

	public int tid; // 球队id
	public String t_name; // 中文短名称
	public String t_full_name; // 中文全名称

	public int p_id; // 球员ID
	public String p_name; // 中文名
	public String p_s_name; // 简称
	public String p_en_name; // 英文名称
	public String p_header; // 头像
	public String p_number; // 号码
	public String p_age; // 年纪
	public String p_height; // 身高
	public String p_weight; // 体重
	public String p_dataInfo; // 信息
	public String p_intro; // 信息
	public ArrayList<FootballPlayerDataListEntity> infoList;

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
				p_id = profile.optInt("player_id");
				p_number = profile.optString("number");
				p_name = profile.optString("player_full_name");
				p_s_name = profile.optString("player_name");
				p_en_name = profile.optString("player_enname");
				p_header = profile.optString("player_header");
				p_age = profile.optString("age");
				p_height = profile.optString("height");
				p_weight = profile.optString("weight");
				p_dataInfo = profile.optString("detail");
				p_intro = profile.optString("intro");

			}
			
			JSONArray seasonArr = obj.optJSONArray("stats");
			if (seasonArr != null) {
				FootballPlayerDataListEntity infoDataEntity;
				infoList = new ArrayList<FootballPlayerDataListEntity>();
				for (int i = 0; i < seasonArr.length(); i++) {
					infoDataEntity = new FootballPlayerDataListEntity();
					infoDataEntity.paser(seasonArr.optJSONObject(i));
					infoList.add(infoDataEntity);
				}
			}
			
		}
	}
	
	public class FootballPlayerDataListEntity extends BaseEntity{

		public String block;
		public ArrayList<FootballPlayerInfoDataEntity> infoDateList;
		@Override
		public void paser(JSONObject json) throws Exception {
			// TODO Auto-generated method stub
			block = json.optString("block");
			JSONArray seasonArr = json.optJSONArray("data");
			if (seasonArr != null) {
				FootballPlayerInfoDataEntity infoDataEntity;
				infoDateList = new ArrayList<FootballPlayerInfoDataEntity>();
				for (int i = 0; i < seasonArr.length(); i++) {
					infoDataEntity = new FootballPlayerInfoDataEntity();
					infoDataEntity.paserArr(seasonArr.optJSONArray(i));
					infoDateList.add(infoDataEntity);
				}
				
			}
		}
		
	}
	
	public class FootballPlayerInfoDataEntity extends BaseEntity{
		public String name;
		public String num;
		@Override
		public void paser(JSONObject json) throws Exception {
			// TODO Auto-generated method stub
			
		}
		public void paserArr(JSONArray arr) throws Exception {
			// TODO Auto-generated method stub
			if (arr !=null) {
				for (int i = 0; i < arr.length(); i++) {
					name = arr.optString(0);
					num = arr.optString(1);
				}
			}
		}
		
	}

}
