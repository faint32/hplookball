package com.hupu.games.data;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class LiveEntity extends BaseEntity implements Serializable {

	// public int i_pId;
	// public int i_gId;
	public String str_event;
	/** 新字段 */
	public String str_new_event;

	public int i_section;
	public String i_endTime;
	/** 消息的所属 0 all ,1 home,2 away */
	public byte byt_team;

	private String str_vs;

	public int i_home_score;

	public int i_away_score;

	/** 内容文字颜色，RGB16进制颜色值，不带井号 */
	public int i_color = -1;
	/** 内容链接 */
	public String str_link;
	/** 内容附加图片缩略地址 */
	public String str_img_thumb;
	/** 内容附加图片原地址 */
	public String str_img;
	/** type 值为0、为空或者该键不存在则为普通信息，值为1为竞猜信息 **/
	public int type;
	// ------征途-----//
	// public int casino_id;// 竞猜ID

	public Answer[] answers;// 竞猜答案 [{answer_id:1, title: xxx}, {answer_id: 2,
							// title: yyy}, ...]

	public String content;

	public int userCount;
	/** id 状态ID（1：进行中，2：已封盘，3：已开奖, 6:流盘） */
	public int quizStatus;

	public String quizStr;

	public boolean is_show_odds;//     是否显示赔率，0：不显示，1：显示
	
	public int max_bet;//    最大投注金额 max_bet    最大投注金额，0：无限额，大于0的数字：限制金额

	public int rightId;
	public static class Answer {
		public int answer_id;// 答案ID
		public String title;// 答案标题
		public int casino_id;
		public String content; // 竞猜内容
		public String odds;
		public int max_bet;
	
	}

	/**是否参与了下注
	 * 0：未参与，1：已参与且选择的答案是1，2：已参与且选择的答案是2
	 * */
	public int isCasino;


	@Override
	public void paser(JSONObject json) throws Exception {

		type = json.optInt("type");
		// type=0;
		if (type == 0) {
			if (json.has("promotion_event"))
				str_event = json.optString("promotion_event");
			else
				str_event = json.optString(KEY_EVENT);
			// i_section=json.getInt(KEY_SECTION);
			i_endTime = json.optString(KEY_END_TIME);
			byt_team = (byte) json.optInt(KEY_TEAM);

			String color = null;
			if (json.has("promotion_color"))
				color = json.getString("promotion_color");
			else
				color = json.optString("color", null);
			if (color != null) {
				i_color = Integer.parseInt(color, 16);
				i_color |= 0xff000000;
			}

			if (json.has("promotion_link"))
				str_link = json.getString("promotion_link");
			else
				str_link = json.optString("link", null);

			if (json.has("promotion_img_thumb"))
				str_img_thumb = json.getString("promotion_img_thumb");
			else
				str_img_thumb = json.optString("img_thumb", null);

			if (json.has("promotion_img"))
				str_img = json.getString("promotion_img");
			else
				str_img = json.optString("img", null);
		}

		else if (type == 1) {
			// -----竞猜解析-------//
			JSONObject casino = null;

			casino = json.getJSONObject("casino");
			i_endTime = json.optString(KEY_END_TIME);
			i_color = 0xffff0000;
		
			if (casino != null) {
				content = casino.optString("content", null);
				is_show_odds =casino.optInt("is_show_odds")==1;
				max_bet =casino.optInt("max_bet");
				JSONArray as = casino.optJSONArray("answers");
				answers = new Answer[as.length()];
				for (int i = 0; i < answers.length; i++) {
					answers[i] = new Answer();
					answers[i].answer_id = as.getJSONObject(i).optInt(
							"answer_id");
					answers[i].title = as.getJSONObject(i).optString("title");
					answers[i].casino_id = casino.optInt("casino_id");
					answers[i].content = content;
					answers[i].odds = as.getJSONObject(i).optString("odds");
				}
				userCount = casino.optInt("user_count");
				JSONObject oo = casino.optJSONObject("status");
				if (oo != null) {
					quizStatus = oo.optInt("id");
					quizStr = oo.optString("desc");
				}
				isCasino= casino.optInt("is_casino");
				rightId =casino.optInt("right_answer");
			}

			
		}

	}

}
