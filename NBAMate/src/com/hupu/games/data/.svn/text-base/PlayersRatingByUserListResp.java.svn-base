package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author panyongjun
 * 球员列表
 * */
public class PlayersRatingByUserListResp extends BaseEntity {

	public ArrayList<PlayerRatingByUserEntity> mList;

	public PlayerRatingEntity profile;
	
	public boolean hasMore;
	

	@Override
	public void paser(JSONObject json) throws Exception {

		json = json.optJSONObject(KEY_RESULT);
		profile =new PlayerRatingEntity();
		profile.paser(json.optJSONObject("profile"));		
		JSONArray array = json.optJSONArray(KEY_DATA);
		int size = 0;
		if (array != null && (size = array.length()) > 0) {
			mList = new ArrayList<PlayerRatingByUserEntity>();
			PlayerRatingByUserEntity player;
			for (int i = 0; i < size; i++) {
				player = new PlayerRatingByUserEntity();
				player.paser(array.getJSONObject(i));
				mList.add(player);
			}
		}
		hasMore =json.optInt("nextDataExists",0)==0?false:true;
	}

}
