package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author panyongjun
 * 球员列表
 * */
public class PlayersRatingListResp extends BaseEntity {

	public ArrayList<PlayerRatingEntity> mList;

	@Override
	public void paser(JSONObject json) throws Exception {

		JSONArray array = json.optJSONArray(KEY_RESULT);
		int size = 0;
		if (array != null && (size = array.length()) > 0) {
			mList = new ArrayList<PlayerRatingEntity>();
			PlayerRatingEntity player;
			for (int i = 0; i < size; i++) {
				player = new PlayerRatingEntity();
				player.paser(array.getJSONObject(i));
				mList.add(player);
			}
		}
	}

}
