package com.hupu.games.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

@SuppressWarnings("serial")
public class PlayerEntity extends BaseEntity implements Serializable{

	// public int i_gId;
	// public int i_tId;
	public byte byt_player_type;
	public String str_player_id;
	public String str_name;
	/***/
	public byte byt_season;
	/***/
	public int i_mins;
	/***/
	public int i_pts;
	/***/
	public int i_fga;

	public int i_fgm;

	public int i_tpa;

	public int i_tpm;

	public int i_fta;

	public int i_ftm;

	public int i_dreb;

	public int i_oreb;

	public int i_reb;

	public int i_asts;

	public int i_stl;

	public int i_blk;

	public int i_to;

	public int i_pf;

	public int i_net_pts;

	public String str_pos;

	public String str_dnp;

	public LinkedHashMap<String, String> mapDatas;

	public int on_court;

	public void paser(JSONObject json, Set<String> names) throws Exception {

		str_player_id = json.optString(KEY_PLAY_ID);
		str_name = json.optString(KEY_NAME, null);
        if(str_name ==null)
        	str_name= json.optString("player_name", null);
        on_court= json.optInt("on_court",-1);
//        Log.d("on_court", "on_court="+on_court);
		// 修改的逻辑 ，需要排序
		mapDatas = new LinkedHashMap<String, String>();		
		Iterator<String> it =null;
		if(names==null)
			it =json.keys();
		else
			it =names.iterator();
		
		String key;
		String value;
		while (it.hasNext()) {
			key = it.next();
			value = json.optString(key, null);
			if (value != null)
				mapDatas.put(key, value);
		}
		//
		// i_gId = json.optInt(KEY_GAME_ID);
		// i_tId = json.optInt(KEY_TEAM_ID);
		// byt_player_type = (byte) json.optInt(KEY_PLAY_TYPE);
		// byt_season = (byte) json.optInt(KEY_SEASON);
		// i_net_pts = json.optInt(KEY_NET_POINTS);
		// str_pos = json.optString(KEY_POSITION, null);
		// str_dnp = json.optString(KEY_DNP, null);

		// i_mins = json.optInt(KEY_MINS);
		// i_pts = json.optInt(KEY_PTS);
		// i_fga = json.optInt(KEY_FGA);
		// i_fgm = json.optInt(KEY_FGA);
		// i_tpa = json.optInt(KEY_TPA);
		// i_tpm = json.optInt(KEY_TPM);
		// i_fta = json.optInt(KEY_FTA);
		// i_ftm = json.optInt(KEY_FTM);
		// i_dreb = json.optInt(KEY_DREB);
		// i_oreb = json.optInt(KEY_OREB);
		// i_reb = json.optInt(KEY_REB);
		// i_asts = json.optInt(KEY_ASTS);
		// i_stl = json.optInt(KEY_STL);
		// i_blk = json.optInt(KEY_BLK);
		// i_to = json.optInt(KEY_TO);
		// i_pf = json.optInt(KEY_PF);
	}



	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
