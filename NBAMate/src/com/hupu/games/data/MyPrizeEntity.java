package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyPrizeEntity extends BaseEntity
{
	/**
	 * 物品ID
	 */
	public String id;
	/**
     * 兑换物品名
     */
    public String name;
    /**
     * 是否已经兑换
     */
    public String status;
    /**
     * 物品缩略图
     */
    public String imgUrl;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
    	id = json.optString("id");
    	name = json.optString("name");
    	status = json.optString("status");
    	imgUrl = json.optString("img_small");
    }
}
