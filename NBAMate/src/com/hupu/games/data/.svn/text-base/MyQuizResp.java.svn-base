package com.hupu.games.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.game.quiz.QuizListResp;
/**
 * 竞猜列表
 * @author papa
 * */
public class MyQuizResp extends BaseEntity {

	public LinkedList<QuizListResp> list;
	public int join;  //参与的数量
	public int win;  //等待结果的竞猜数量
	public int coin;  //赢取金币
	public int winRank;  //猜对次数排名
	public int coinRank;  //赢取金币排名
	public int box;      //宝箱数
	
	public int currPage;
	public int totalPage;
	
	public int balance;
	public int boxNum;
	
	public int bets[];

	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject mjson = json.optJSONObject(KEY_RESULT);
		
		JSONObject object = mjson.optJSONObject(KEY_INFO);
			join = object.optInt("join");
			win = object.optInt("win");
			coin = object.optInt("coin");
			winRank = object.optInt("win_rank");
			coinRank = object.optInt("coin_rank");
			box = object.optInt("box");
			balance = object.optInt("balance");
			boxNum = object.optInt("box_count");
			
			currPage = object.optInt("currPage");
			totalPage = object.optInt("totalPage");
			JSONArray betArray = object.optJSONArray("bets");
		if (betArray != null) {

			bets =new int[betArray.length()+1];
			for (int i = 0; i < betArray.length(); i++) {
				bets[i] = betArray.getInt(i);
			}
		}
			
		JSONArray array =mjson.optJSONArray(KEY_LIST);
		if(array!=null )
		{
			list =new LinkedList<QuizListResp>();
			int size =array.length();
			QuizListResp entity;
			for(int i =0;i<size;i++)
			{
				entity =new QuizListResp();
				entity.paser(array.getJSONObject(i));
				list.add(entity);
			}
		}
	}

}
