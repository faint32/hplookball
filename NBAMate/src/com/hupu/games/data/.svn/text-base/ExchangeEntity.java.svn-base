package com.hupu.games.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExchangeEntity extends BaseEntity
{
	/**
	 * 物品ID
	 */
	public int id;
	/**
     * 兑换物品名
     */
    public String name;
    /**
     * 剩余数量
     */
    public int remain;  
    /**
     * 兑换所需金币
     */
    public int coin;
    /**
     * 物品缩略图
     */
    public String imgUrl;

    @Override
    public void paser(JSONObject json) throws Exception
    {
        // TODO Auto-generated method stub
    	id = json.optInt("id");
    	name = json.optString("name");
    	remain = json.optInt("remain");
    	coin = json.optInt("coin");
    	imgUrl = json.optString("img");
    }
}
