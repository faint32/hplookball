package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hupu.games.R;
import com.hupu.games.data.game.quiz.QuizListResp;

public class MyQuizListingAdapter extends XSectionedBaseAdapter {

	private LinkedList<QuizListResp> QuizList;

	private LayoutInflater mInflater;

	OnClickListener mClick;

	public MyQuizListingAdapter(Context context, OnClickListener click) {
		mClick = click;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(LinkedList<QuizListResp> list) {
		if (list == null) {
			QuizList = null;
		} else {
			QuizList = list;
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
		if (QuizList != null)
			return QuizList.size();

		return 0;
	}

	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		if (QuizList != null)
			return QuizList.get(section).mQuizList.size();
		return 0;
	}

	@Override
	public View getItemView(int section, int pos, View convertView,
			ViewGroup parent) {
		int position = pos;
		// TODO Auto-generated method stub
		QuizOpen quizOpen = null;
		if (convertView == null) {
			quizOpen = new QuizOpen();
			convertView = initQuizOpen(quizOpen);

		} else {
			Object tag = convertView.getTag();
			if (tag instanceof QuizOpen)
				quizOpen = (QuizOpen) tag;
			else {
				quizOpen = new QuizOpen();
				convertView = initQuizOpen(quizOpen);
			}
		}

		quizOpen.textContent
				.setText(QuizList.get(section).mQuizList.get(position).content
						+ (QuizList.get(section).mQuizList.get(position).max_bet > 0 ? " (最多投入"
								+ QuizList.get(section).mQuizList.get(position).max_bet
								+ "金豆)"
								: ""));
		quizOpen.myChoice.setText("我猜："
				+ QuizList.get(section).mQuizList.get(position).user_answer
				+ "(" + QuizList.get(section).mQuizList.get(position).coin
				+ "金豆)");
		quizOpen.quizResult.setText("开奖："
				+ QuizList.get(section).mQuizList.get(position).right_answer);

		if (QuizList.get(section).mQuizList.get(position).win_coin > 0) {
			convertView.findViewById(R.id.open_result).setVisibility(
					View.VISIBLE);
			quizOpen.coinNum.setText("+"
					+ QuizList.get(section).mQuizList.get(position).win_coin);
			((LinearLayout) convertView.findViewById(R.id.open_result))
					.setBackgroundResource(R.drawable.quiz_win_label);
		} else {
			quizOpen.coinNum.setText(""
					+ QuizList.get(section).mQuizList.get(position).win_coin);
			convertView.findViewById(R.id.open_result).setVisibility(View.GONE);
			// ((LinearLayout)
			// convertView.findViewById(R.id.open_result)).setBackgroundResource(R.drawable.quiz_loss_label);
		}
		quizOpen.boxImg.setVisibility(View.VISIBLE);
		quizOpen.boxNum.setVisibility(QuizList.get(section).mQuizList
				.get(position).box_name == 0 ? View.GONE : View.VISIBLE);

		quizOpen.boxImg.setVisibility(View.VISIBLE);
		quizOpen.boxNum.setVisibility(QuizList.get(section).mQuizList
				.get(position).box_name == 0 ? View.GONE : View.VISIBLE);

		// 开奖开到宝箱时 根据返回对应相应宝箱icon
		switch (QuizList.get(section).mQuizList.get(position).box_name) {
		case 0:
			quizOpen.boxImg.setVisibility(View.GONE);
			break;
		case 1:
			quizOpen.boxImg.setBackgroundResource(R.drawable.icon_box_copper_s);
			break;
		case 2:
			quizOpen.boxImg.setBackgroundResource(R.drawable.icon_box_silver_s);
			break;
		case 3:
			quizOpen.boxImg.setBackgroundResource(R.drawable.icon_box_gold_s);
			break;

		default:
			quizOpen.boxImg.setVisibility(View.GONE);
			quizOpen.boxNum.setVisibility(View.GONE);
			break;
		}

		// quizOpen.boxImg.setOnClickListener(mClick);
		quizOpen.openResult.setOnClickListener(mClick);

		return convertView;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		Header header = null;
		convertView = mInflater.inflate(R.layout.item_my_quiz_header, null);
		header = new Header();
		header.name = (TextView) convertView.findViewById(R.id.txt_date);
		header.headerView = (RelativeLayout) convertView
				.findViewById(R.id.header_view);

		if (QuizList != null) {
			try {
				header.name.setText(QuizList.get(section).date + "  "
						+ QuizList.get(section).vsName);
				header.headerView.setTag(section);
				// header.headerView.setOnClickListener(mClick);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return convertView;
	}

	private View initQuizOpen(QuizOpen quizOpen) {
		View v = mInflater.inflate(R.layout.item_my_quiz_open, null);

		quizOpen.openResult = (LinearLayout) v.findViewById(R.id.open_result);
		quizOpen.textContent = (TextView) v.findViewById(R.id.txt_content);
		quizOpen.myChoice = (TextView) v.findViewById(R.id.my_choice);
		quizOpen.quizResult = (TextView) v.findViewById(R.id.quiz_result);
		quizOpen.coinNum = (TextView) v.findViewById(R.id.coin_num);
		quizOpen.boxImg = (ImageView) v.findViewById(R.id.box_img);
		quizOpen.popToast = (TextView) v.findViewById(R.id.pop_box_toast);
		quizOpen.boxNum = (TextView) v.findViewById(R.id.txt_box_num);

		v.setTag(quizOpen);

		return v;
	}

	class QuizOpen {

		LinearLayout openResult;
		TextView textContent;
		TextView myChoice;
		TextView quizResult;
		TextView coinNum;
		ImageView boxImg;
		TextView boxNum;
		TextView popToast;

	}

	class Header {
		TextView name;
		RelativeLayout headerView;
	}

}
