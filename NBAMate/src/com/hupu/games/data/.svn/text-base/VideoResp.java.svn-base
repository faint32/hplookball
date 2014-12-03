package com.hupu.games.data;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class VideoResp extends BaseEntity {

	public LinkedList<VideoEntity> mList;
	public int nextDataExists;

	public long lastVId;
	
	public byte open;
	
	public String tabs1;
	
	public String tabsTap1;
	
	public String tabs2;
	
	public String tabsTap2;
	
	//--显示哪个tab
	public int on;
	@Override
	public void paser(JSONObject json) throws Exception {
		json =json.optJSONObject("result");
		if(json.has("tabs"))
		{
			JSONArray arr =json.getJSONArray("tabs");
			JSONObject obj =arr.getJSONObject(0);
			tabs1=obj.getString(KEY_TITLE);
			tabsTap1 =obj.getString("type");
			on =obj.getInt("on");
			obj =arr.getJSONObject(1);
			tabsTap2 =obj.getString("type");
			tabs2=obj.getString(KEY_TITLE);
			if(on ==1)
				on =0;
			else
				on =1;
			obj =null;
		}
		JSONArray array = json.optJSONArray("data");
		if (array != null) {
			int size = array.length();
			mList = new LinkedList<VideoEntity>();
			VideoEntity temp;
			for (int i = 0; i < size; i++) {
				temp = new VideoEntity();
				temp.paser(array.getJSONObject(i));
				mList.add(temp);
				if(i==size -1)
					lastVId =temp.vid;
			}
		}
		nextDataExists =json.optInt("nextDataExists"   );
		open= (byte)json.optInt("open");
	}
}
