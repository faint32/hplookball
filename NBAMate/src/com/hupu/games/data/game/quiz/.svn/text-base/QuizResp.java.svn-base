package com.hupu.games.data.game.quiz;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HupuLog;
import com.hupu.games.data.BaseEntity;

/**
 * 竞猜列表
 * 
 * @author papa
 * */
public class QuizResp extends BaseEntity {

	public LinkedList<QuizListResp> list;
	public LinkedList<QuizCaipiaoListResp> caipiaoList;
	public QuizCaipiaoListResp caipiaoResp;

	public int join; // 参与的数量
	public int win; // 等待结果的竞猜数量
	public int coin; // 赢取金币
	public int winRank; // 猜对次数排名
	public int coinRank; // 赢取金币排名
	public int box; // 宝箱数

	public int currPage;
	public int totalPage;

	public int balance;
	public int boxNum;

	public int bets[];
	
	public String walletBalance;

	@Override
	public void paser(JSONObject json) throws Exception {
		JSONObject mjson = json.optJSONObject(KEY_RESULT);
		
		//竞猜相关数据解析
		JSONObject quiz = mjson.optJSONObject("quiz");
		if (quiz != null) {

			JSONObject object = quiz.optJSONObject(KEY_INFO);
			join = object.optInt("join");
			win = object.optInt("win");
			coin = object.optInt("coin");
			winRank = object.optInt("win_rank");
			coinRank = object.optInt("coin_rank");
			box = object.optInt("box");
			balance = object.optInt("balance");
			boxNum = object.optInt("box_count");
			
			walletBalance = object.optString("wallet_balance");

			currPage = object.optInt("currPage");
			totalPage = object.optInt("totalPage");
			JSONArray betArray = object.optJSONArray("bets");

			if (betArray != null) {

				bets = new int[betArray.length() + 1];
				for (int i = 0; i < betArray.length(); i++) {
					bets[i] = betArray.getInt(i);
				}
			}

			JSONArray quizArray = quiz.optJSONArray(KEY_LIST);
			if (quizArray != null) {
				list = new LinkedList<QuizListResp>();
				int size = quizArray.length();
				QuizListResp entity;
				for (int i = 0; i < size; i++) {
					entity = new QuizListResp();
					entity.paser(quizArray.getJSONObject(i));
					list.add(entity);
				}
			}
		}
		
		//彩票 数据解析
		JSONObject caipiao = mjson.optJSONObject("caipiao");
		if (caipiao != null) {
			caipiaoResp = new QuizCaipiaoListResp();
			caipiaoResp.paser(caipiao);
		}
//		JSONArray caipiaoArray = caipiao.optJSONArray("data");
//		if (caipiaoArray != null) {
//			caipiaoList = new LinkedList<QuizCaipiaoListResp>();
//			int size = caipiaoArray.length();
//			QuizCaipiaoListResp entity;
//			for (int i = 0; i < size; i++) {
//				entity = new QuizCaipiaoListResp();
//				entity.paser(caipiaoArray.getJSONObject(i));
//				caipiaoList.add(entity);
//			}
//		}
	}

}
