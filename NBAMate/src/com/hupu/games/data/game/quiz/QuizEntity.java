package com.hupu.games.data.game.quiz;

import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

public class QuizEntity extends BaseEntity {

	public int qid;// "竞猜id",
	public String content;// "竞猜题目",
	public int lid; //"联赛id",
	public int gid; //"比赛id",
	public String answer1;// "答案1",
	public String odds1;// "赔率1",
	public String answer2;//"答案2",
	public String odds2;// "赔率2",
	public String right_answer;// "正确答案",
	public int max_bet;// "正确答案",
	public long endtime;// "结束时间",
	public int process;//"竞猜状态",
	
	public int isShowOdds; //是否显示赔率
	
	public int box_name;
	public String joinCount;
	
	public String userBetMax;
	public String userWinMax;
	

	//---如果用户参与了--//

	public int is_user_join;//是否参与,
	public int coin;//"用户投注金币数",
	public String user_answer;// "用户选择的答案",
	public int user_choose;// "用户选择的答案",
	public int bet_status; //"是否开奖",
	public int win_coin;// "赢得的金币数(输了为负数)"
		
	@Override
	public void paser(JSONObject json) throws Exception {
		qid=json.optInt("qid");
		content=json.optString("content", null);
		lid=json.optInt("lid");
		gid=json.optInt("gid");
		answer1=json.optString("answer1", null);
		odds1=json.optString("odds1", null);
		answer2=json.optString("answer2", null);
		odds2=json.optString("odds2", null);
		right_answer=json.optString("right_answer", null);
		max_bet=json.optInt("max_bet");
		endtime=json.optLong("endtime", 0);
		process=json.optInt("process");
		
		isShowOdds=json.optInt("is_show_odds");
		
		box_name = json.optInt("box");
		joinCount = json.optString("join_count");
		
		userBetMax = json.optString("user_bet_max");
		userWinMax = json.optString("user_win_max");

		//---如果用户参与了--//

		is_user_join=json.optInt("is_user_join");
		coin=json.optInt("coin");
		user_answer=json.optString("user_answer", null);
		user_choose=json.optInt("user_choose");
		bet_status=json.optInt("bet_status");
		win_coin=json.optInt("win_coin");
		
	}

}
