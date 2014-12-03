package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
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
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity;
import com.hupu.games.data.game.quiz.QuizCaipiaoListResp;
import com.hupu.games.data.game.quiz.QuizListResp;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity.CaipiaoScoreEntity;

public class QuizListingAdapter extends XSectionedBaseAdapter {

	private LinkedList<QuizListResp> QuizList;

	private QuizCaipiaoListResp caipiaoList;

	private LayoutInflater mInflater;

	OnClickListener mClick;

	public QuizListingAdapter(Context context, OnClickListener click) {
		mClick = click;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(LinkedList<QuizListResp> list, QuizCaipiaoListResp cList) {
		if (list == null) {
			QuizList = null;
		} else {
			QuizList = list;
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hupu.games.adapter.SectionedBaseAdapter#getSectionCount()
	 * 两个数据结构组成，so 需要进行一定的逻辑 确定section 的数量
	 */
	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		if (caipiaoList != null && caipiaoList.mQuizList != null
				&& caipiaoList.mQuizList.size() > 0) {
			if (QuizList != null)
				return QuizList.size() + 1;
		} else {

			if (QuizList != null)
				return QuizList.size();
		}

		return 0;
	}

	/**
	 * 彩票结构 根据状态会出现顶端或者 最底部 所以含有相应的逻辑在其中，以下不做过多阐述
	 */
	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		if (caipiaoList != null && caipiaoList.mQuizList != null
				&& caipiaoList.mQuizList.size() > 0) {
			if (caipiaoList.bottom == 0) {// 彩票显示在头部

				if (section == 0) {
					return caipiaoList.mQuizList.size() + 1;
				} else {
					if (QuizList != null)
						return QuizList.get(section - 1).mQuizList.size();
				}

			} else {
				if (section == QuizList.size())
					return caipiaoList.mQuizList.size() + 1;
				else
					return QuizList.get(section).mQuizList.size();

			}
		} else {
			if (QuizList != null)
				return QuizList.get(section).mQuizList.size();
		}
		return 0;
	}

	@Override
	public View getItemView(int section, int pos, View convertView,
			ViewGroup parent) {
		if (caipiaoList != null && caipiaoList.mQuizList != null
				&& caipiaoList.mQuizList.size() > 0) {
			if (caipiaoList.bottom == 0) {// 彩票显示在头部
				if (section != 0)
					return getQuizView(section - 1, pos, convertView, parent);
				else
					return getCaipiaoView(section, pos, convertView, parent);

			} else {
				if (section == QuizList.size())
					return getCaipiaoView(section, pos, convertView, parent);
				else
					return getQuizView(section, pos, convertView, parent);

			}
		} else {
			return getQuizView(section, pos, convertView, parent);
		}

	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		Header header = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_caipiao_quiz_header,
					null);
			header = new Header();
			header.name = (TextView) convertView.findViewById(R.id.txt_date);
			header.deadline = (TextView) convertView
					.findViewById(R.id.deadline);
			convertView.setTag(header);
		} else
			header = (Header) convertView.getTag();

		// 如果彩票存在
		if (caipiaoList != null && caipiaoList.mQuizList != null
				&& caipiaoList.mQuizList.size() > 0) {
			if (caipiaoList.bottom == 0) {// 彩票显示在头部
				if (section == 0) {
					header.name.setText("彩票");
					header.deadline.setVisibility(View.VISIBLE);
					header.deadline.setText(Html
							.fromHtml(caipiaoList.top_right_notice));
				} else {
					if (QuizList != null) {
						try {
							header.deadline.setVisibility(View.GONE);
							header.name.setText(QuizList.get(section - 1).name);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			} else {
				if (section == QuizList.size()) {
					header.name.setText("彩票");
					header.deadline.setVisibility(View.VISIBLE);
					header.deadline.setText(Html
							.fromHtml(caipiaoList.top_right_notice));
				} else {
					if (QuizList != null) {
						try {
							header.deadline.setVisibility(View.GONE);
							header.name.setText(QuizList.get(section).name);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

			}
		} else { // 单纯的金币竞猜
			if (QuizList != null) {
				try {
					header.deadline.setVisibility(View.GONE);
					header.name.setText(QuizList.get(section).name);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

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
		ImageView FAQImg;
	}

	private View initCaipiaoWait(CaipiaoWait caipiaoWait) {
		View v = mInflater.inflate(R.layout.item_caipiao_quiz_wait, null);
		caipiaoWait.title = (TextView) v.findViewById(R.id.txt_content);
		caipiaoWait.description = (TextView) v.findViewById(R.id.join_info);
		caipiaoWait.FAQImg = (ImageView) v.findViewById(R.id.btn_faq);
		v.setTag(caipiaoWait);
		return v;
	}

	class CaipiaoOpen {
		TextView title;
		TextView description;
		TextView open_result;
		ImageView FAQImg;
	}

	private View initCaipiaoOpen(CaipiaoOpen caipiaoOpen) {
		View v = mInflater.inflate(R.layout.item_caipiao_quiz_open, null);
		caipiaoOpen.title = (TextView) v.findViewById(R.id.txt_content);
		caipiaoOpen.description = (TextView) v.findViewById(R.id.join_info);
		caipiaoOpen.open_result = (TextView) v
				.findViewById(R.id.open_caipiao_result);
		caipiaoOpen.FAQImg = (ImageView) v.findViewById(R.id.btn_faq);
		v.setTag(caipiaoOpen);
		return v;
	}

	class Caipiao {
		TextView title;
		LinearLayout answer1;
		LinearLayout answer2;
		LinearLayout answer3;
		TextView answerTxt1;
		TextView answerTxt2;
		TextView answerTxt3;
		TextView numTxt1;
		TextView numTxt2;
		TextView numTxt3;
		TextView description;
		ImageView FAQImg;
	}

	private View initCaipiao(Caipiao caipiao) {
		View v = mInflater.inflate(R.layout.item_caipiao_quiz, null);
		caipiao.answer1 = (LinearLayout) v
				.findViewById(R.id.btn_caipiao_answer1);
		caipiao.answer2 = (LinearLayout) v
				.findViewById(R.id.btn_caipiao_answer2);
		caipiao.answer3 = (LinearLayout) v
				.findViewById(R.id.btn_caipiao_answer3);
		caipiao.answerTxt1 = (TextView) v.findViewById(R.id.answer_txt1);
		caipiao.answerTxt2 = (TextView) v.findViewById(R.id.answer_txt2);
		caipiao.answerTxt3 = (TextView) v.findViewById(R.id.answer_txt3);
		caipiao.numTxt1 = (TextView) v.findViewById(R.id.num_txt1);
		caipiao.numTxt2 = (TextView) v.findViewById(R.id.num_txt2);
		caipiao.numTxt3 = (TextView) v.findViewById(R.id.num_txt3);
		caipiao.title = (TextView) v.findViewById(R.id.txt_content);
		caipiao.description = (TextView) v.findViewById(R.id.join_info);
		caipiao.FAQImg = (ImageView) v.findViewById(R.id.btn_faq);

		v.setTag(caipiao);
		return v;
	}

	class ScoreCaipiao {
		TextView title;
		LinearLayout scoreLayout;
		TextView description;
		ImageView FAQImg;

	}

	private View initScoreCaipiao(ScoreCaipiao scoreCaipiao) {
		View v = mInflater.inflate(R.layout.item_caipiao_score_quiz, null);
		scoreCaipiao.title = (TextView) v.findViewById(R.id.txt_content);
		scoreCaipiao.scoreLayout = (LinearLayout) v
				.findViewById(R.id.score_layout);
		scoreCaipiao.description = (TextView) v.findViewById(R.id.join_info);
		scoreCaipiao.FAQImg = (ImageView) v.findViewById(R.id.btn_faq);

		v.setTag(scoreCaipiao);
		return v;
	}

	class Header {
		TextView name;
		TextView deadline;
	}

	private View getCaipiaoView(int section, int pos, View convertView,
			ViewGroup parent) {

		if (pos == caipiaoList.mQuizList.size()) {
			convertView = mInflater.inflate(R.layout.item_caipiao_declaration,
					null);
			TextView declaration = (TextView) convertView
					.findViewById(R.id.caipiao_declaration);
			TextView faq_link = (TextView) convertView
					.findViewById(R.id.txt_faq);
			declaration.setText(Html.fromHtml(SharedPreferencesMgr.getString(
					"caipiaoDeclaration", "投入彩票有风险，在线投注需谨慎。")));
			faq_link.setOnClickListener(mClick);

			return convertView;
		}

		Caipiao caipiao;
		CaipiaoWait caipiaoWait;
		CaipiaoOpen caipiaoOpen;
		ScoreCaipiao scoreCaipiao;

		caipiao = new Caipiao();
		switch (caipiaoList.mQuizList.get(pos).status) {
		case 2:// 进行中
			if (caipiaoList.mQuizList.get(pos).type == 6) {
				scoreCaipiao = new ScoreCaipiao();
				convertView = initScoreCaipiao(scoreCaipiao);
				scoreCaipiao.title
						.setText(caipiaoList.mQuizList.get(pos).title);
				scoreCaipiao.description.setText(Html
						.fromHtml(caipiaoList.mQuizList.get(pos).description));
				if (caipiaoList.mQuizList.get(pos).faq_link != null
						&& !caipiaoList.mQuizList.get(pos).faq_link.equals("")) {
					scoreCaipiao.FAQImg.setTag(caipiaoList.mQuizList.get(pos));
					scoreCaipiao.FAQImg.setVisibility(View.VISIBLE);
					scoreCaipiao.FAQImg.setOnClickListener(mClick);

				}
				// 比分选项部分
				if (caipiaoList.mQuizList.get(pos).mScoreList != null) {
					for (CaipiaoScoreEntity entity:caipiaoList.mQuizList.get(pos).mScoreList) {
						View view = mInflater.inflate(
								R.layout.item_caipiao_score_quiz_titlebar, null,false);
						
						scoreCaipiao.scoreLayout.addView(view);
						
						((TextView)view.findViewById(R.id.win_title)).setText(entity.scoreTitle);
						if (entity.scoreAnswers != null) {
							LinearLayout scoreView = null;
							int scoreAnswerSize = entity.scoreAnswers.size();
							for (int i = 0; i < scoreAnswerSize; i++) {
								
								if (i%5 == 0) {
									scoreView = (LinearLayout)mInflater.inflate(
											R.layout.item_score_line, null);
									scoreCaipiao.scoreLayout.addView(scoreView);
								}
								View item = mInflater.inflate(
										R.layout.item_score, null,false);
								((TextView)item.findViewById(R.id.score_info)).setText(entity.scoreAnswers.get(i).answer_title);
								if (!"".equals(entity.scoreAnswers.get(i).odd)) {
									item.findViewById(R.id.odd_info).setVisibility(View.VISIBLE);
									((TextView)item.findViewById(R.id.odd_info)).setText(entity.scoreAnswers.get(i).odd);
								}else {
									item.findViewById(R.id.odd_info).setVisibility(View.GONE);
								}
								if (scoreAnswerSize == i+1) 
									item.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, scoreAnswerSize%5 ==0 ?1:scoreAnswerSize%5));
								else
									item.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
								if (entity.scoreAnswers.get(i).disable == 0) 
									item.findViewById(R.id.score_item_bg).setBackgroundColor(Color.parseColor("#ffffff"));
								else 
									item.findViewById(R.id.score_item_bg).setBackgroundColor(Color.parseColor("#dedede"));
								
								//由于彩票和比分结构层级不同。。这边为了投注的时候结构统一。特意强制把一些外层的数据设到子集里面！临时性的。
								entity.scoreAnswers.get(i).bid = caipiaoList.mQuizList.get(pos).bid;
								entity.scoreAnswers.get(i).caipiao_title = caipiaoList.mQuizList.get(pos).title;
								item.setTag(entity.scoreAnswers.get(i));
								item.setOnClickListener(mClick);
								scoreView.addView(item);
							}
						}
						
					}
				}

			} else {
				caipiao = new Caipiao();
				convertView = initCaipiao(caipiao);
				if (caipiaoList.mQuizList.get(pos) != null
						&& caipiaoList.mQuizList.get(pos).mList != null) {
					int size = caipiaoList.mQuizList.get(pos).mList.size();
					if (size > 1) {

						caipiao.title
								.setText(caipiaoList.mQuizList.get(pos).title);
						caipiao.description
								.setText(Html.fromHtml(caipiaoList.mQuizList
										.get(pos).description));
						caipiao.answer1.setTag(caipiaoList.mQuizList.get(pos));
						caipiao.answer1.setOnClickListener(mClick);
						caipiao.answer2.setTag(caipiaoList.mQuizList.get(pos));
						caipiao.answer2.setOnClickListener(mClick);
						//赔率过低会灰掉显示
						if (caipiaoList.mQuizList.get(pos).mList.get(0).disable == 0) 
							caipiao.answer1.setBackgroundResource(R.drawable.quiz_btn_selector);
						else 
							caipiao.answer1.setBackgroundColor(Color.parseColor("#dedede"));
						
						if (caipiaoList.mQuizList.get(pos).mList.get(1).disable == 0) 
							caipiao.answer2.setBackgroundResource(R.drawable.quiz_btn_selector);
						else 
							caipiao.answer2.setBackgroundColor(Color.parseColor("#dedede"));
						

						caipiao.answerTxt1.setText(caipiaoList.mQuizList
								.get(pos).mList.get(0).answer_title);
						caipiao.answerTxt2.setText(caipiaoList.mQuizList
								.get(pos).mList.get(1).answer_title);

						caipiao.numTxt1
								.setText(caipiaoList.mQuizList.get(pos).mList
										.get(0).user_count);
						caipiao.numTxt2
								.setText(caipiaoList.mQuizList.get(pos).mList
										.get(1).user_count);

						if (size == 3) {
							caipiao.answer3.setVisibility(View.VISIBLE);
							caipiao.answer3.setTag(caipiaoList.mQuizList
									.get(pos));
							caipiao.answer3.setOnClickListener(mClick);
							//赔率过低会灰掉显示
							if (caipiaoList.mQuizList.get(pos).mList.get(2).disable == 0) 
								caipiao.answer3.setBackgroundResource(R.drawable.quiz_btn_selector);
							else 
								caipiao.answer3.setBackgroundColor(Color.parseColor("#dedede"));
							
							caipiao.answerTxt3.setText(caipiaoList.mQuizList
									.get(pos).mList.get(2).answer_title);
							caipiao.numTxt3.setText(caipiaoList.mQuizList
									.get(pos).mList.get(2).user_count);
						} else {
							caipiao.answer3.setVisibility(View.GONE);
						}

					}
					if (caipiaoList.mQuizList.get(pos).faq_link != null
							&& !caipiaoList.mQuizList.get(pos).faq_link
									.equals("")) {
						caipiao.FAQImg.setTag(caipiaoList.mQuizList.get(pos));
						caipiao.FAQImg.setVisibility(View.VISIBLE);
						caipiao.FAQImg.setOnClickListener(mClick);

					}
				}
			}
			break;
		case 5:// 开奖
			caipiaoOpen = new CaipiaoOpen();
			convertView = initCaipiaoOpen(caipiaoOpen);
			caipiaoOpen.title.setText(caipiaoList.mQuizList.get(pos).title);
			caipiaoOpen.description.setText(Html.fromHtml(caipiaoList.mQuizList
					.get(pos).description));
			if (Float.parseFloat(caipiaoList.mQuizList.get(pos).win_coin) > 0) {
				caipiaoOpen.open_result.setVisibility(View.VISIBLE);
				caipiaoOpen.open_result.setText("+"
						+ caipiaoList.mQuizList.get(pos).win_coin + "元");
				caipiaoOpen.open_result.setOnClickListener(mClick);
			} else {
				caipiaoOpen.open_result.setVisibility(View.GONE);
			}
			if (caipiaoList.mQuizList.get(pos).faq_link != null
					&& !caipiaoList.mQuizList.get(pos).faq_link.equals("")) {
				caipiaoOpen.FAQImg.setTag(caipiaoList.mQuizList.get(pos));
				caipiaoOpen.FAQImg.setVisibility(View.VISIBLE);
				caipiaoOpen.FAQImg.setOnClickListener(mClick);
			}
			break;

		default: // 待开奖 出票中 等等状态
			caipiaoWait = new CaipiaoWait();
			convertView = initCaipiaoWait(caipiaoWait);
			caipiaoWait.title.setText(caipiaoList.mQuizList.get(pos).title);
			caipiaoWait.description.setText(Html.fromHtml(caipiaoList.mQuizList
					.get(pos).description));
			if (caipiaoList.mQuizList.get(pos).faq_link != null
					&& !caipiaoList.mQuizList.get(pos).faq_link.equals("")) {
				caipiaoWait.FAQImg.setTag(caipiaoList.mQuizList.get(pos));
				caipiaoWait.FAQImg.setVisibility(View.VISIBLE);
				caipiaoWait.FAQImg.setOnClickListener(mClick);
			}
			break;
		}
		// Object caipiaoTag = convertView.getTag();

		return convertView;
	}

	private View getQuizView(int section, int pos, View convertView,
			ViewGroup parent) {

		int position = pos;
		// TODO Auto-generated method stub
		QuizWait quizWait = null;
		quizIncrease quizIncrease = null;
		QuizOpen quizOpen = null;
		Quiz quiz = null;
		if (convertView == null) {
			switch (QuizList.get(section).status) {
			case 0:
				quiz = new Quiz();

				quizIncrease = new quizIncrease();
				if (QuizList.get(section).mQuizList.get(position).is_user_join == 1) {
					convertView = initQuizIncrease(quizIncrease);
				} else {
					convertView = initQuiz(quiz);
				}
				break;
			case 1:
				quizWait = new QuizWait();
				convertView = initQuizWait(quizWait);
				break;
			case 2:
				quizOpen = new QuizOpen();
				convertView = initQuizOpen(quizOpen);
				break;

			default:
				break;
			}
		} else {
			Object tag = convertView.getTag();
			switch (QuizList.get(section).status) {
			case 0:
				if (tag instanceof Quiz) {
					quiz = (Quiz) tag;
				} else if (tag instanceof quizIncrease) {
					quizIncrease = (quizIncrease) tag;
				} else {
					if (QuizList.get(section).mQuizList.get(position).is_user_join == 1) {
						quizIncrease = new quizIncrease();
						convertView = initQuizIncrease(quizIncrease);
					} else {
						quiz = new Quiz();
						convertView = initQuiz(quiz);
					}
				}

				break;
			case 1:
				if (tag instanceof QuizWait) {
					quizWait = (QuizWait) tag;
				} else {
					quizWait = new QuizWait();
					convertView = initQuizWait(quizWait);
				}

				break;
			case 2:
				if (tag instanceof QuizOpen)
					quizOpen = (QuizOpen) tag;
				else {
					quizOpen = new QuizOpen();
					convertView = initQuizOpen(quizOpen);
				}
				break;

			default:
				break;
			}
		}

		switch (QuizList.get(section).status) {
		case 0:
			if (QuizList.get(section).mQuizList.get(position).is_user_join == 1) {

				if (quizIncrease == null) {
					quizIncrease = new quizIncrease();
					convertView = initQuizIncrease(quizIncrease);
				}
				quizIncrease.textContent
						.setText(QuizList.get(section).mQuizList.get(position).content
								+ (QuizList.get(section).mQuizList
										.get(position).max_bet > 0 ? " (最多投入"
										+ QuizList.get(section).mQuizList
												.get(position).max_bet + "金豆)"
										: ""));
				quizIncrease.myChoice
						.setText("我猜："
								+ QuizList.get(section).mQuizList.get(position).user_answer
								+ "("
								+ QuizList.get(section).mQuizList.get(position).coin
								+ "金豆)");
				quizIncrease.joinNum
						.setText(QuizList.get(section).mQuizList.get(position).joinCount
								+ "人参与"
								+ (QuizList.get(section).mQuizList
										.get(position).userBetMax.equals("") ? ""
										: "，"
												+ QuizList.get(section).mQuizList
														.get(position).userBetMax));

				Answer answer = new Answer();

				answer.answer_id = QuizList.get(section).mQuizList
						.get(position).user_choose;
				answer.casino_id = QuizList.get(section).mQuizList
						.get(position).qid;
				answer.content = QuizList.get(section).mQuizList.get(position).content;
				answer.title = QuizList.get(section).mQuizList.get(position).user_answer;
				answer.max_bet = QuizList.get(section).mQuizList.get(position).max_bet;

				quizIncrease.increase.setTag(answer);
				quizIncrease.increase.setOnClickListener(mClick);
			} else {
				if (quiz == null) {
					quiz = new Quiz();
					convertView = initQuiz(quiz);
				}

				quiz.textContent
						.setText(QuizList.get(section).mQuizList.get(position).content
								+ (QuizList.get(section).mQuizList
										.get(position).max_bet > 0 ? " (最多投入"
										+ QuizList.get(section).mQuizList
												.get(position).max_bet + "金豆)"
										: ""));
				quiz.joinNum
						.setText(QuizList.get(section).mQuizList.get(position).joinCount
								+ "人参与"
								+ (QuizList.get(section).mQuizList
										.get(position).userBetMax.equals("") ? ""
										: "，"
												+ QuizList.get(section).mQuizList
														.get(position).userBetMax));

				quiz.answer1.setText(QuizList.get(section).mQuizList
						.get(position).answer1);
				quiz.answer2.setText(QuizList.get(section).mQuizList
						.get(position).answer2);

				Answer answer1 = new Answer();
				answer1.answer_id = 1;
				answer1.casino_id = QuizList.get(section).mQuizList
						.get(position).qid;
				answer1.content = QuizList.get(section).mQuizList.get(position).content;
				answer1.title = QuizList.get(section).mQuizList.get(position).answer1;
				answer1.max_bet = QuizList.get(section).mQuizList.get(position).max_bet;
				quiz.answer1.setTag(answer1);
				quiz.answer1.setOnClickListener(mClick);

				Answer answer2 = new Answer();
				answer2.answer_id = 2;
				answer2.casino_id = QuizList.get(section).mQuizList
						.get(position).qid;
				answer2.content = QuizList.get(section).mQuizList.get(position).content;
				answer2.title = QuizList.get(section).mQuizList.get(position).answer2;
				answer2.max_bet = QuizList.get(section).mQuizList.get(position).max_bet;

				// 如果赔率是0.00 就不显示赔率
				quiz.odds1
						.setVisibility(Float.parseFloat(QuizList.get(section).mQuizList
								.get(position).odds1) == 0f
								|| QuizList.get(section).mQuizList
										.get(position).isShowOdds == 0 ? View.GONE
								: View.VISIBLE);
				quiz.odds2
						.setVisibility(Float.parseFloat(QuizList.get(section).mQuizList
								.get(position).odds2) == 0f
								|| QuizList.get(section).mQuizList
										.get(position).isShowOdds == 0 ? View.GONE
								: View.VISIBLE);

				quiz.odds1.setText("1 赔 "
						+ QuizList.get(section).mQuizList.get(position).odds1);
				quiz.odds2.setText("1 赔 "
						+ QuizList.get(section).mQuizList.get(position).odds2);

				quiz.answer2.setTag(answer2);
				quiz.answer2.setOnClickListener(mClick);
			}
			break;
		case 1:
			quizWait.textContent
					.setText(QuizList.get(section).mQuizList.get(position).content
							+ (QuizList.get(section).mQuizList.get(position).max_bet > 0 ? " (最多投入"
									+ QuizList.get(section).mQuizList
											.get(position).max_bet + "金豆)"
									: ""));
			quizWait.myChoice.setText("我猜："
					+ QuizList.get(section).mQuizList.get(position).user_answer
					+ "(" + QuizList.get(section).mQuizList.get(position).coin
					+ "金豆)");
			quizWait.joinNum.setText(QuizList.get(section).mQuizList
					.get(position).joinCount
					+ "人参与"
					+ (QuizList.get(section).mQuizList.get(position).userBetMax
							.equals("") ? ""
							: "，"
									+ QuizList.get(section).mQuizList
											.get(position).userBetMax));
			break;
		case 2:
			quizOpen.textContent
					.setText(QuizList.get(section).mQuizList.get(position).content
							+ (QuizList.get(section).mQuizList.get(position).max_bet > 0 ? " (最多投入"
									+ QuizList.get(section).mQuizList
											.get(position).max_bet + "金豆)"
									: ""));
			quizOpen.myChoice.setText("我猜："
					+ QuizList.get(section).mQuizList.get(position).user_answer
					+ "(" + QuizList.get(section).mQuizList.get(position).coin
					+ "金豆)");
			quizOpen.quizResult
					.setText("开奖："
							+ QuizList.get(section).mQuizList.get(position).right_answer);

			if (QuizList.get(section).mQuizList.get(position).win_coin > 0) {
				convertView.findViewById(R.id.open_result).setVisibility(
						View.VISIBLE);
				quizOpen.coinNum
						.setText("+"
								+ QuizList.get(section).mQuizList.get(position).win_coin);
				((LinearLayout) convertView.findViewById(R.id.open_result))
						.setBackgroundResource(R.drawable.quiz_win_label);
				((LinearLayout) convertView.findViewById(R.id.open_result)).setOnClickListener(mClick);
			} else {
				quizOpen.coinNum
						.setText(""
								+ QuizList.get(section).mQuizList.get(position).win_coin);
				convertView.findViewById(R.id.open_result).setVisibility(
						View.GONE);
				// ((LinearLayout)
				// convertView.findViewById(R.id.open_result)).setBackgroundResource(R.drawable.quiz_loss_label);
			}
			quizOpen.boxImg.setVisibility(View.VISIBLE);
			quizOpen.boxNum.setVisibility(QuizList.get(section).mQuizList
					.get(position).box_name == 0 ? View.GONE : View.VISIBLE);
			quizOpen.joinNum.setText(QuizList.get(section).mQuizList
					.get(position).joinCount
					+ "人参与"
					+ (QuizList.get(section).mQuizList.get(position).userWinMax
							.equals("") ? ""
							: "，"
									+ QuizList.get(section).mQuizList
											.get(position).userWinMax));

			// 开奖开到宝箱时 根据返回对应相应宝箱icon
			switch (QuizList.get(section).mQuizList.get(position).box_name) {
			case 0:
				quizOpen.boxImg.setVisibility(View.GONE);
				break;
			case 1:
				quizOpen.boxImg
						.setBackgroundResource(R.drawable.icon_box_copper_s);
				break;
			case 2:
				quizOpen.boxImg
						.setBackgroundResource(R.drawable.icon_box_silver_s);
				break;
			case 3:
				quizOpen.boxImg
						.setBackgroundResource(R.drawable.icon_box_gold_s);
				break;

			default:
				quizOpen.boxImg.setVisibility(View.GONE);
				quizOpen.boxNum.setVisibility(View.GONE);
				break;
			}

			// quizOpen.boxImg.setOnClickListener(mClick);
			quizOpen.openResult.setOnClickListener(mClick);

			break;
		default:
			break;
		}

		return convertView;
	}

}
