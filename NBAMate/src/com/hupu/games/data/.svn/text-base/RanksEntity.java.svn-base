package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RanksEntity extends BaseEntity
{
    public String title;
    public String rank_type;
    public String online;
    public String offline;
    public String web_url;
    public ArrayList<ArrayList<String>> ranks_data;
   // public ArrayList<String> pids;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
        title = json.optString("title");
        rank_type = json.optString("rank_type");
        online = json.optString("online");
        offline = json.optString("offline");
        web_url = json.optString("web_url");
        
        JSONArray array = json.optJSONArray("ranks_data");

        if (array != null)
        {
            ranks_data = new ArrayList<ArrayList<String>>();
            JSONArray stringArray;
            ArrayList<String> list;
            for (int k = 0; k < array.length(); k++)
            {
                stringArray = array.optJSONArray(k);

                list = new ArrayList<String>();
                if (stringArray != null)
                {

                    for (int j = 0; j < stringArray.length(); j++)
                    {
                        list.add(stringArray.optString(j));
                    }
                }
                ranks_data.add(list);
            }

        }
        
//        JSONArray pidArray = json.optJSONArray("pids");
//        if (pidArray != null)
//        {
//        	for (int i = 0; i < array.length(); i++)
//            {
//        		pids.add(pidArray.optString(i));
//            }
//        }

    }

}
