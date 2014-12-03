package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyPrizeResp extends BaseEntity
{
    public ArrayList<MyPrizeEntity> list;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
        JSONArray array = json.optJSONArray(KEY_RESULT);
        MyPrizeEntity entity;
        if (array != null)
        {
        	list = new ArrayList<MyPrizeEntity>();
            for (int k = 0; k < array.length(); k++)
            {
            	entity = new MyPrizeEntity();
            	entity.paser(array.getJSONObject(k));
            	list.add(entity);
            }

        }

    }

}
