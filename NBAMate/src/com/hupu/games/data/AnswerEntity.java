package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnswerEntity extends BaseEntity
{
    public int answer_id;
    public String title;
    public String odds;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
    	answer_id = json.optInt("answer_id");
    	title = json.optString("title");
    	odds = json.optString("odds");
    }

}
