package com.hupu.games.data.account;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class TaskEntity extends BaseEntity
{
	public int id;
	public String name;
	public String coin;
	public String process_text;
    public int status;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
    	id = json.optInt("id");
    	status = json.optInt("status");
    	process_text = json.optString("process_text");
    	name = json.optString("name");
    	coin = json.optString("coin");
    }
}
