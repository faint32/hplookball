package com.hupu.games.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class LeaguesEntity extends BaseEntity {
	public int lid;
	public String en;
	public String name;
	public String logo;
	public String game_type;
	public int is_new;
	public int is_follow; // 是否关注这个联赛
	public String template; // 联赛类型
	public String show_default_tab;
	public LinkedList<TeamsEntity> mList;
	/**发现数据*/
	public ArrayList<ArrayList<DiscoveryEntity>> mDiscoverList;
    /**是否直接将原排行榜接口地址以web方式显示季后赛对阵图，api，web*/
    public String showStandings="api";
	// show_tabs :接口完成后写

	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		lid = json.optInt("lid");
		name = json.optString(KEY_NAME, null);
		logo = json.optString(KEY_LOGO, null);
		game_type = json.optString("game_type", "");
		en = json.optString("en", null);
		template = json.optString("show_template", null);
		is_follow = json.optInt(KEY_ISFOLLOW);
		is_new = json.optInt("show_new");
		show_default_tab = json.optString("show_default_tab", null);
		showStandings= json.optString("show_standings","api");

		JSONArray array = json.optJSONArray("teams");
		if (array != null) {

			mList = new LinkedList<TeamsEntity>();
			TeamsEntity team;
			for (int i = 0; i < array.length(); i++) {
				team = new TeamsEntity();
				team.paser(array.getJSONObject(i));
				mList.add(team);
			}
			array = null;
		}
		if("discovery".equals(en))
		{
			//发现
			mDiscoverList =new ArrayList<ArrayList<DiscoveryEntity>>();
			ArrayList<DiscoveryEntity> list=null;
			DiscoveryEntity entity=null;
			array = json.optJSONArray("sections");
			JSONArray array2=null;
	
			int size =array.length();
			for(int i=0;i<size;i++)
			{
				array2 =array.getJSONArray(i);
				int size2=array2.length();
				list =new ArrayList<DiscoveryEntity>();
				for(int j=0;j<size2;j++)
				{
					entity =new DiscoveryEntity();
					entity.paser(array2.getJSONObject(j));
					list.add(entity);
				}
				mDiscoverList.add(list);
			}
			
		}
		json = null;
	}

}
