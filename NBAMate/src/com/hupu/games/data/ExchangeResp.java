package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExchangeResp extends BaseEntity
{
    public ArrayList<PrizeEntity> list;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
        JSONArray array = json.optJSONArray(KEY_RESULT);
        PrizeEntity entity;
        if (array != null)
        {
        	list = new ArrayList<PrizeEntity>();
            for (int k = 0; k < array.length(); k++)
            {
            	entity = new PrizeEntity();
            	entity.paser(array.getJSONObject(k));
            	list.add(entity);
            }

        }

    }

}
