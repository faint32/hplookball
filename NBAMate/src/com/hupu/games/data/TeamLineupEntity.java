package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TeamLineupEntity extends BaseEntity {

	public int tid;
	public String name;
	public String color;
	public String formation_type;
	public LinkedList<FormationEntity> formationList;
	public LinkedList<LineupEntity> lineupList;
	@Override
	public void paser(JSONObject json) throws Exception {
		tid =json.optInt("tid");
		name= json.optString("name");
		color= json.optString("color");
		formation_type= json.optString("formation_type");
		JSONArray formationArray = json.optJSONArray("formation");
		JSONArray lineupArray = json.optJSONArray("lineup");
		if (formationArray != null) {
			int size = formationArray.length();
			formationList = new LinkedList<FormationEntity>();
			FormationEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new FormationEntity();
				temp.paser(formationArray.getJSONObject(i));
				formationList.add(temp);
			}
		}
		
		if (lineupArray != null) {
			int size = lineupArray.length();
			lineupList = new LinkedList<LineupEntity>();
			LineupEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new LineupEntity();
				temp.paser(lineupArray.getJSONObject(i));
				lineupList.add(temp);
			}
		}
	}
}
