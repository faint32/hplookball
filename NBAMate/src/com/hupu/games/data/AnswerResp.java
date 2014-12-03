package com.hupu.games.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnswerResp extends BaseEntity
{
    public ArrayList<AnswerEntity> answers;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
        JSONArray array = json.optJSONArray("answers");
        AnswerEntity answer;
        if (array != null)
        {
        	answers = new ArrayList<AnswerEntity>();
            for (int k = 0; k < array.length(); k++)
            {
            	answer = new AnswerEntity();
            	answer.paser(array.getJSONObject(k));
            	answers.add(answer);
            }
        }
    }
}
