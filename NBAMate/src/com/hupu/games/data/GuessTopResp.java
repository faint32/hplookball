package com.hupu.games.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class GuessTopResp extends BaseEntity
{
    public ArrayList<GuessTopEntity> ranks;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
        JSONArray array = json.optJSONArray(KEY_RESULT);
        GuessTopEntity rank;
        if (array != null)
        {
            ranks = new ArrayList<GuessTopEntity>();
            for (int k = 0; k < array.length(); k++)
            {
                rank = new GuessTopEntity();
                rank.paser(array.getJSONObject(k));
                ranks.add(rank);
            }
        }
    }
}
