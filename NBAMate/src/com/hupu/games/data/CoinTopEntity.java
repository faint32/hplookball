package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CoinTopEntity extends BaseEntity
{
    public String rank;
    public String name;
    public String coins;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
    	rank = json.optString("rank");
    	name = json.optString("name");
    	coins = json.optString("coins");
    }
}
