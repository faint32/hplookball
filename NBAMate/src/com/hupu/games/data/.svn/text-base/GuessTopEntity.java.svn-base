package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GuessTopEntity extends BaseEntity
{
    public String rank;
    public String name;
    public int win_count;
    public String rate;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
    	rank = json.optString("rank");
    	name = json.optString("name");
    	win_count = json.optInt("win_count");
    	rate = json.optString("rate");
    }

}
