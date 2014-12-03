package com.hupu.games.data.news;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class NewsResp extends BaseEntity {

	public LinkedList<NewsEntity> mList;
	public int nextDataExists;

	public long lastNId;
	@Override
	public void paser(JSONObject json) throws Exception {
		json =json.optJSONObject("result");
		JSONArray array = json.optJSONArray("data");
		if (array != null) {
			int size = array.length();
			mList = new LinkedList<NewsEntity>();
			NewsEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new NewsEntity();
				temp.paser(array.getJSONObject(i));
				mList.add(temp);
				if(i==size -1)
					lastNId =temp.nid;
			}
		}
		nextDataExists =json.optInt("nextDataExists"   );
		
	}
}
