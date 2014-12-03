package com.hupu.games.data.game.quiz;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class QuizCaipiaoEntity extends BaseEntity {

	public int ubid;// "流水id",
	public int bid;// "彩票id",
	public int type;// "彩票id",
	public String title;// "彩票题目",
	public String description;// "彩票info",
	public int status;// "彩票状态",
	public String status_desc;// "彩票状态说明",
	public String win_coin;
	public String faq_link;

	public ArrayList<CaipiaoAnswer> mList;
	public ArrayList<CaipiaoScoreEntity> mScoreList;

	//tag 需要的index
	public int index;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		ubid = json.optInt("ubid");
		bid = json.optInt("bid");
		type = json.optInt("type");
		title = json.optString("title");
		description = json.optString("description");
		win_coin = json.optString("win_coin");
		faq_link = json.optString("faq_link");
		JSONObject statusInfo = json.optJSONObject("status");
		if (statusInfo != null) {
			status = statusInfo.optInt("id");
			status_desc = statusInfo.optString("desc");
		}

		JSONArray dataList = json.optJSONArray("answers");
		if (dataList != null) {
			if (type == 6) {
				mScoreList = new ArrayList<CaipiaoScoreEntity>();
				CaipiaoScoreEntity scoreEntity;
				for (int i = 0; i < dataList.length(); i++) {
					scoreEntity = new CaipiaoScoreEntity();
					scoreEntity.paser(dataList.optJSONObject(i));
					mScoreList.add(scoreEntity);
				}
			} else {
				mList = new ArrayList<CaipiaoAnswer>();
				CaipiaoAnswer answer;
				for (int i = 0; i < dataList.length(); i++) {
					answer = new CaipiaoAnswer();
					answer.paser(dataList.optJSONObject(i));
					mList.add(answer);
				}
			}
		}

	}

	public class CaipiaoAnswer extends BaseEntity {
		public int answer_id;
		public String answer_title;
		public String answer_notice;
		public String user_count;
		public String odd;
		public int is_bet;
		public int disable;		//是否可以下注
		public ArrayList<CaipiaoBet> bets;
		
		//比分投注逻辑需要
		public int bid;
		public String caipiao_title;
		@Override
		public void paser(JSONObject json) throws Exception {
			// TODO Auto-generated method stub
			answer_id = json.optInt("answer");
			answer_title = json.optString("title");
			answer_notice = json.optString("notice");
			user_count = json.optString("user_count");
			odd = json.optString("odd");
			is_bet = json.optInt("is_bet");
			disable = json.optInt("disable");
			JSONArray betList = json.optJSONArray("bet_coin_option");
			if (betList != null) {
				bets = new ArrayList<CaipiaoBet>();
				CaipiaoBet bet;
				for (int i = 0; i < betList.length(); i++) {
					bet = new CaipiaoBet();
					bet.paser(betList.optJSONObject(i));
					bets.add(bet);
				}
			}
		}
	}

	/**
	 * 比分answer 实体；
	 * 
	 * @author papa
	 * 
	 */
	public class CaipiaoScoreEntity extends BaseEntity {
		public String scoreTitle;
		public ArrayList<CaipiaoAnswer> scoreAnswers;

		@Override
		public void paser(JSONObject json) throws Exception {
			scoreTitle = json.optString("title");
			JSONArray scoreDataList = json.optJSONArray("data");

			if (scoreDataList != null) {
				scoreAnswers = new ArrayList<CaipiaoAnswer>();
				CaipiaoAnswer answer;
				for (int i = 0; i < scoreDataList.length(); i++) {
					answer = new CaipiaoAnswer();
					answer.paser(scoreDataList.optJSONObject(i));
					scoreAnswers.add(answer);
				}
			}

		}
	}

	/**
	 * 比分彩票组内容
	 * 
	 * @author papa
	 * 
	 */

	public class CaipiaoBet extends BaseEntity {
		public int bet;
		public String bonus;

		@Override
		public void paser(JSONObject json) throws Exception {
			// TODO Auto-generated method stub
			bet = json.optInt("coin");
			bonus = json.optString("bonus");
		}

	}

}
