package com.hupu.games.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class TeamLineupResp extends BaseEntity {

	public TeamLineupEntity homeEntity;
	public TeamLineupEntity awayEntity;
	public String preview;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		json =json.optJSONObject("result");
		preview = json.optString("preview");
		homeEntity = new TeamLineupEntity();
		awayEntity = new TeamLineupEntity();
		JSONObject homeObj = json.optJSONObject("home_roster");
		JSONObject awayObj = json.optJSONObject("away_roster");
		homeEntity.paser(homeObj);
		awayEntity.paser(awayObj);
	}
}
