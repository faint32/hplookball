package com.hupu.games.data.game.basketball;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;


public class NbaPlayerInfoReq extends BaseEntity {

	public ArrayList<NbaPlayerInfoEntity> mDataList;

	public int tid; // 球队id
	public String t_name; // 中文短名称
	public String t_full_name; // 中文全名称

	public int p_id; // 球员ID
	public String p_name; // 中文名
	public String p_s_name; // 简称
	public String p_en_name; // 英文名称
	public String p_header; // 头像
	public String p_number; // 号码
	public String p_position; // 位置
	public String p_birth_date; // 生日
	public String p_age; // 年纪
	public String p_height; // 身高
	public String p_weight; // 体重
	public String p_salary; // 薪金
	public String p_contract; // 合同
	public String p_draft; // 选秀
	public String p_college; // 学校
	
	public ArrayList<NbaPlayerInfoDataEntity> infoDateList;
	public ArrayList<NbaPlayerInfoEntity> regularStats;
	public ArrayList<NbaPlayerInfoEntity> playoffStats;

	public String[] regularKeys;
	public String[] regularValues;

	public String[] playoffKeys;
	public String[] playoffValues;

	public String[] headerKeys;
	public int pos = 0;

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
				p_name = profile.optString("player_name");
				p_s_name = profile.optString("player_short_name");
				p_en_name = profile.optString("player_enname");
				p_header = profile.optString("player_header");
				p_position = profile.optString("position");
				p_birth_date = profile.optString("birth_date");
				p_age = profile.optString("age");
				p_height = profile.optString("height");
				p_weight = profile.optString("weight");
				p_salary = profile.optString("salary");
				p_contract = profile.optString("contract");
				p_draft = profile.optString("draft");
				p_college = profile.optString("college");

			}
			
			JSONArray seasonArr = obj.optJSONArray("season_stats");
			if (seasonArr != null) {
				NbaPlayerInfoDataEntity infoDataEntity;
				infoDateList = new ArrayList<NbaPlayerInfoDataEntity>();
				for (int i = 0; i < seasonArr.length(); i++) {
					infoDataEntity = new NbaPlayerInfoDataEntity();
					infoDataEntity.paserArr(seasonArr.optJSONArray(i));
					infoDateList.add(infoDataEntity);
				}
				
			}
			JSONArray regularheader = obj
					.optJSONArray("career_regular_glossary");
			if (regularheader != null) {
				// 解析字典
				JSONArray arr1 = regularheader.getJSONArray(0);
				JSONArray arr2 = regularheader.getJSONArray(1);
				if (arr1 != null && arr2 != null) {

					int size = arr1.length();
					regularKeys = new String[size];
					regularValues = new String[size];
					for (int i = 0; i < size; i++) {
						regularKeys[i] = arr1.optString(i);
						regularValues[i] = arr2.optString(i);
					}
				}
			}
			
			JSONArray playoffheader = obj
					.optJSONArray("career_playoff_glossary");
			if (playoffheader != null) {
				// 解析字典
				JSONArray arr1 = playoffheader.getJSONArray(0);
				JSONArray arr2 = playoffheader.getJSONArray(1);
				if (arr1 != null && arr2 != null) {
					int size = arr1.length();
					playoffKeys = new String[size];
					playoffValues = new String[size];
					for (int i = 0; i < size; i++) {
						playoffKeys[i] = arr1.optString(i);
						playoffValues[i] = arr2.optString(i);
					}
				}
			}

			JSONArray regularInfo = obj.optJSONArray("career_regular_stats");
			if (regularInfo != null) {
				pos++;
				regularStats = new ArrayList<NbaPlayerInfoEntity>();
				NbaPlayerInfoEntity info;
				headerKeys = regularKeys;
				for (int i = 0; i < regularInfo.length(); i++) {
					info = new NbaPlayerInfoEntity();
					info.paser(regularInfo.optJSONObject(i));
					
					regularStats.add(info);
				}
			}
			
			JSONArray playoffInfo = obj.optJSONArray("career_playoff_stats");
			if (playoffInfo != null) {
				pos++;
				playoffStats = new ArrayList<NbaPlayerInfoEntity>();
				NbaPlayerInfoEntity info;
				headerKeys = playoffKeys;
				for (int i = 0; i < playoffInfo.length(); i++) {
					info = new NbaPlayerInfoEntity();
					info.paser(playoffInfo.optJSONObject(i));
					
					playoffStats.add(info);
				}
			}

			// JSONArray arr =obj.optJSONArray("list");
			// if(arr !=null)
			// {
			// int size =arr.length();
			// mDataList=new ArrayList<NbaPlayerInfoEntity> ();
			// NbaPlayerInfoEntity data ;
			// for(int i =0;i<size;i++)
			// {
			// data =new NbaPlayerInfoEntity();
			// data.paser(arr.getJSONObject(i));
			// mDataList.add(data);
			// }
			// }
		}
	}

	public class NbaPlayerInfoEntity extends BaseEntity {
		public int t_id;// 球队id
		public String values[];

		@Override
		public void paser(JSONObject json) throws Exception {
			t_id = json.optInt("tid");
			values = new String[headerKeys.length];
			for (int i = 0; i < values.length; i++) {
				values[i] = json.optString(headerKeys[i]);
			}
		}
	}
	
	public class NbaPlayerInfoDataEntity extends BaseEntity{
		public String values[]; 
		@Override
		public void paser(JSONObject json) throws Exception {
			// TODO Auto-generated method stub
			
		}
		public void paserArr(JSONArray arr) throws Exception {
			// TODO Auto-generated method stub
			if (arr !=null) {
				values = new String[arr.length()];
				for (int i = 0; i < arr.length(); i++) {
					values[i] = arr.optString(i);
				}
			}
		}
		
	}

}
