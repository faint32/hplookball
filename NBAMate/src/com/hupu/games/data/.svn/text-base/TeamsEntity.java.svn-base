package com.hupu.games.data;

import org.json.JSONObject;

import com.hupu.games.common.SharedPreferencesMgr;

public class TeamsEntity extends BaseEntity
{
    public int tid;
    public String enName;
    public String name;
    public String logo;
    public String color;
    public int is_follow;
   
    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
        tid=json.optInt(KEY_TEAM_ID);
        enName = json.optString(KEY_EN_NAME,null);
        name =json.optString(KEY_NAME, null);
        logo =json.optString(KEY_LOGO, null);
        color = json.optString(KEY_COLOR, null);
        is_follow =json.optInt(KEY_ISFOLLOW);
        
    }

}
