package com.hupu.games.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RanksResp extends BaseEntity
{
    public LinkedList<RanksEntity> ranks;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
        JSONArray array = json.optJSONArray(KEY_RESULT);
        RanksEntity rank;
        if (array != null)
        {
            ranks = new LinkedList<RanksEntity>();
            for (int k = 0; k < array.length(); k++)
            {
                rank = new RanksEntity();
                rank.paser(array.getJSONObject(k));
                ranks.add(rank);
            }

        }

    }

}
