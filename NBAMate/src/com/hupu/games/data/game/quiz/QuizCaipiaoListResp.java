package com.hupu.games.data.game.quiz;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.data.BaseEntity;

/**
 * 竞猜组
 * 
 * @author papa
 * */
public class QuizCaipiaoListResp extends BaseEntity {

	public ArrayList<QuizCaipiaoEntity> mQuizList;

	public String name; // 组 名称
	public String top_right_notice; // 彩票上部右侧提示文字
	public String bottom_notice; // 彩票底部提示文字
	public int bottom;

	@Override
	public void paser(JSONObject json) throws Exception {
		// JSONObject mjson = json.optJSONObject("rs");

		top_right_notice = json.optString("top_right_notice");
		bottom_notice = json.optString("bottom_notice");
		bottom = json.optInt("bottom", 0);
		JSONArray array = json.optJSONArray("data");
		if (array != null) {
			mQuizList = new ArrayList<QuizCaipiaoEntity>();
			int size = array.length();
			QuizCaipiaoEntity entity;
			for (int i = 0; i < size; i++) {
				entity = new QuizCaipiaoEntity();
				entity.paser(array.getJSONObject(i));
				mQuizList.add(entity);

			}
		}
	}

}
