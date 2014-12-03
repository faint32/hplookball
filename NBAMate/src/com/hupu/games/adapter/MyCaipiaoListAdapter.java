package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.MyCaipiaoResp;

public class MyCaipiaoListAdapter extends XSectionedBaseAdapter {

	private MyCaipiaoResp caipiaoList;

	private LayoutInflater mInflater;

	OnClickListener mClick;

	public MyCaipiaoListAdapter(Context context, OnClickListener click) {
		mClick = click;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(MyCaipiaoResp cList) {

		if (cList == null) {
			caipiaoList = null;
		} else {
			caipiaoList = cList;
		}

	}

	public int getSection(int pos) {
		return this.getSectionForPosition(pos);
	}

	public int getChildPosition(int pos) {
		return getPositionInSectionForPosition(pos);

	}

	@Override
	public Object getItem(int section, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		 if (caipiaoList != null && caipiaoList.mList != null) {
			 return caipiaoList.mList.size();
		 }

		return 0;
	}

	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		if (caipiaoList != null && caipiaoList.mList != null) {
			return caipiaoList.mList.get(section).mQuizList.size();
		}

		return 0;
	}

	@Override
	public View getItemView(int section, int pos, View convertView,
			ViewGroup parent) {
		return getCaipiaoView(section, pos, convertView, parent);
	}

	// private void showPop(QuizOpen quizOpen){
	// if (!SharedPreferencesMgr.getBoolean("is_show_box_pop", false)) {
	// quizOpen.popToast.setVisibility(View.VISIBLE);
	// quizOpen.popToast.setOnClickListener(mClick);
	// SharedPreferencesMgr.setBoolean("is_show_box_pop", true);
	// }
	// }

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		Header header = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_my_caipiao_quiz_header,
					null);
			header = new Header();
			header.name = (TextView) convertView.findViewById(R.id.txt_date);
			convertView.setTag(header);
		} else
			header = (Header) convertView.getTag();

		// 如果彩票存在
		header.name.setText(caipiaoList.mList.get(section).block);

		return convertView;
	}

	private View initQuizWait(QuizWait quizWait) {
		View v = mInflater.inflate(R.layout.item_quiz_wait, null);

		quizWait.textContent = (TextView) v.findViewById(R.id.txt_content);
		quizWait.myChoice = (TextView) v.findViewById(R.id.my_choice);
		// quizWait.quizeState = (TextView) v.findViewById(R.id.quiz_state);
		quizWait.joinNum = (TextView) v.findViewById(R.id.user_join_num);

		v.setTag(quizWait);

		return v;
	}

	private View initQuizIncrease(quizIncrease increase) {
		View v = mInflater.inflate(R.layout.item_quiz_increase, null);

		increase.textContent = (TextView) v.findViewById(R.id.txt_content);

		increase.myChoice = (TextView) v.findViewById(R.id.my_choice);
		// increase.quizeState = (TextView) v.findViewById(R.id.quiz_state);

		increase.increase = (Button) v.findViewById(R.id.btn_increase);
		increase.joinNum = (TextView) v.findViewById(R.id.user_join_num);

		v.setTag(increase);

		return v;
	}

	private View initQuizOpen(QuizOpen quizOpen) {
		View v = mInflater.inflate(R.layout.item_quiz_open, null);

		quizOpen.openResult = (LinearLayout) v.findViewById(R.id.open_result);
		quizOpen.textContent = (TextView) v.findViewById(R.id.txt_content);

		quizOpen.myChoice = (TextView) v.findViewById(R.id.my_choice);

		quizOpen.quizResult = (TextView) v.findViewById(R.id.quiz_result);

		quizOpen.coinNum = (TextView) v.findViewById(R.id.coin_num);
		quizOpen.boxImg = (ImageView) v.findViewById(R.id.box_img);
		quizOpen.boxNum = (TextView) v.findViewById(R.id.txt_box_num);
		quizOpen.joinNum = (TextView) v.findViewById(R.id.user_join_num);

		v.setTag(quizOpen);

		return v;
	}

	private View initQuiz(Quiz quiz) {
		View v = mInflater.inflate(R.layout.item_quiz, null);
		quiz.answer1 = (Button) v.findViewById(R.id.btn_answer1);
		quiz.answer2 = (Button) v.findViewById(R.id.btn_answer2);
		quiz.textContent = (TextView) v.findViewById(R.id.txt_content);
		quiz.joinNum = (TextView) v.findViewById(R.id.user_join_num);

		quiz.odds1 = (TextView) v.findViewById(R.id.odds1);
		quiz.odds2 = (TextView) v.findViewById(R.id.odds2);

		v.setTag(quiz);
		return v;
	}

	class QuizWait {
		TextView textContent;
		// TextView quizeState;
		TextView myChoice;
		TextView joinNum;

	}

	class QuizOpen {

		LinearLayout openResult;
		TextView textContent;
		TextView myChoice;
		TextView quizResult;
		TextView coinNum;
		ImageView boxImg;
		TextView boxNum;
		TextView joinNum;

	}

	class Quiz {
		Button answer1;
		Button answer2;
		TextView textContent;
		TextView joinNum;

		TextView odds1;
		TextView odds2;
	}

	class quizIncrease {
		TextView textContent;
		// TextView quizeState;
		TextView myChoice;
		Button increase;
		TextView joinNum;

	}

	class CaipiaoWait {
		TextView title;
		TextView description;
	}

	private View initCaipiaoWait(CaipiaoWait caipiaoWait) {
		View v = mInflater.inflate(R.layout.item_caipiao_quiz_wait, null);
		caipiaoWait.title = (TextView) v.findViewById(R.id.txt_content);
		caipiaoWait.description = (TextView) v.findViewById(R.id.join_info);
		v.setTag(caipiaoWait);
		return v;
	}

	class CaipiaoOpen {
		TextView title;
		TextView description;
		TextView open_result;
	}

	private View initCaipiaoOpen(CaipiaoOpen caipiaoOpen) {
		View v = mInflater.inflate(R.layout.item_caipiao_quiz_open, null);
		caipiaoOpen.title = (TextView) v.findViewById(R.id.txt_content);
		caipiaoOpen.description = (TextView) v.findViewById(R.id.join_info);
		caipiaoOpen.open_result = (TextView) v.findViewById(R.id.open_caipiao_result);
		v.setTag(caipiaoOpen);
		return v;
	}

	class Caipiao {
		TextView title;
		Button answer1;
		Button answer2;
		TextView description;
	}

//	private View initCaipiao(Caipiao caipiao) {
//		View v = mInflater.inflate(R.layout.item_caipiao_quiz, null);
//		caipiao.answer1 = (Button) v.findViewById(R.id.btn_caipiao_answer1);
//		caipiao.answer2 = (Button) v.findViewById(R.id.btn_caipiao_answer2);
//		caipiao.title = (TextView) v.findViewById(R.id.txt_content);
//		caipiao.description = (TextView) v.findViewById(R.id.join_info);
//
//		v.setTag(caipiao);
//		return v;
//	}

	class Header {
		TextView name;
	}

	private View getCaipiaoView(int section, int pos, View convertView,
			ViewGroup parent) {

		CaipiaoOpen caipiaoOpen;

			caipiaoOpen = new CaipiaoOpen();
			convertView = initCaipiaoOpen(caipiaoOpen);
			caipiaoOpen.title.setText(caipiaoList.mList.get(section).mQuizList.get(pos).title);
			caipiaoOpen.description.setText(Html.fromHtml(caipiaoList.mList.get(section).mQuizList
					.get(pos).description));
			if (Float.parseFloat(caipiaoList.mList.get(section).mQuizList.get(pos).win_coin) > 0) {
				caipiaoOpen.open_result.setVisibility(View.VISIBLE);
				caipiaoOpen.open_result.setText("+"
						+ caipiaoList.mList.get(section).mQuizList.get(pos).win_coin + "元");
				caipiaoOpen.open_result.setOnClickListener(mClick);
			} else {
				caipiaoOpen.open_result.setVisibility(View.GONE);
			}
		// Object caipiaoTag = convertView.getTag();

		return convertView;
	}

}
