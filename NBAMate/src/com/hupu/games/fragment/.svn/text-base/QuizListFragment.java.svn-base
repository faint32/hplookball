package com.hupu.games.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.FAQActivity;
import com.hupu.games.activity.MyCaipiaoListActivity;
import com.hupu.games.activity.UserGoldActivity;
import com.hupu.games.adapter.QuizListingAdapter;
import com.hupu.games.casino.CaipiaoCasinoDialog;
import com.hupu.games.casino.CasinoDialog;
import com.hupu.games.casino.MyBoxActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BitCoinReq;
import com.hupu.games.data.IncreaseEntity;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity.CaipiaoAnswer;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.game.quiz.QuizEntity;
import com.hupu.games.data.game.quiz.QuizListResp;
import com.hupu.games.data.game.quiz.QuizResp;
import com.hupu.games.data.goldbean.GoldBeanManager;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.view.PinnedHeaderXListView;
import com.hupu.games.view.PinnedHeaderXListView.IXListViewListener;

public class QuizListFragment extends BaseFragment {

	PinnedHeaderXListView mListView;

	QuizListingAdapter mAdapter;
	LayoutInflater mInflater;
	View v;
	TextView userInfo, coinNum;
	ImageView goldImg, addImg;

	private View topInfo;
	private boolean isIncrease = false;

	private int[] bets;

	public String CoinNum = "0";

	public float caipiaoBalance = 0f;


	public void setTag(String t) {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_quiz_list, container, false);
		coinNum = (TextView) ((BaseGameActivity) getActivity())
				.findViewById(R.id.coin_num);
		goldImg = (ImageView) ((BaseGameActivity) getActivity())
				.findViewById(R.id.gold_img);

		addImg = (ImageView) ((BaseGameActivity) getActivity())
				.findViewById(R.id.add_img);

		mInflater = LayoutInflater.from(getActivity());

		// openQid = 10;
		mListView = (PinnedHeaderXListView) v.findViewById(R.id.list_player);
		if (topInfo == null) {
			topInfo = mInflater.inflate(R.layout.quiz_list_head, null);
			userInfo = (TextView) topInfo.findViewById(R.id.user_info);
		}
		topInfo.findViewById(R.id.user_quiz_info).setOnClickListener(
				new Click());
		mListView.addHeaderView(topInfo);
		mListView.setPullLoadEnable(false);

		mListView.setXListViewListener(new pullListener());

		if (mAdapter == null) {
			mAdapter = new QuizListingAdapter(getActivity(), new Click());
		}
		mListView.setAdapter(mAdapter);
		// mListView.setFreshState();
		reqNewData();
		return v;
	}

	/** 获取最新数据 */
	public void reqNewData() {
		((BaseGameActivity) getActivity()).getQuizList();
	}

	@Override
	public void entry() {
		super.entry();
		// if (mAct != null)
		// reqNewData();
	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			reqNewData();
		}

		@Override
		public void onLoadMore() {

		}

	}

	public void onReqResponse(Object o, int methodId, int mQid) {
		// super.onReqResponse(o, methodId);
		//
		if (methodId == HuPuRes.REQ_METHOD_QUIZ_LIST) {

			if (o != null) {
				quizList = (QuizResp) o;
				if (quizList.walletBalance == null || quizList.walletBalance.equals("")) 
					caipiaoBalance = 0f;
				else
					caipiaoBalance = Float.parseFloat(quizList.walletBalance);
				
				mAdapter.setData(quizList.list, quizList.caipiaoResp);

				mListView.stopRefresh();
				mListView.stopLoadMore();

				mAdapter.notifyDataSetChanged();
				int winCoin = quizList.coin > 0 ? quizList.coin : 0;
				if (quizList.join > 0) {
					topInfo.setVisibility(View.VISIBLE);
				}
				bets = quizList.bets;

				userInfo.setText(Html
						.fromHtml("参与"
								+ quizList.join
								+ "场竞猜，猜赢<font color=\"#FF0000\">"
								+ quizList.win
								+ (quizList.win <= 0 ? "</font>场，暂无排名"
										: (quizList.winRank <= 100 ? "</font>场，排名第<font color=\"#FF0000\">"
												+ quizList.winRank + "</font>"
												: "</font>场，排名第"
														+ quizList.winRank))
								+ "，总奖金<font color=\"#FF0000\">"
								+ winCoin
								+ (quizList.coinRank <= 0 ? "</font>金豆，暂无排名 。"
										: (quizList.coinRank <= 100 ? "</font>金豆，排名第<font color=\"#FF0000\">"

												+ quizList.coinRank + "</font>"
												: "</font>金豆，排名第"
														+ quizList.coinRank+"。"))));
				topInfo.setVisibility(View.VISIBLE);
				// 宝箱数 未0 宝箱图标隐藏 数字不显示0
				if (((BaseGameActivity) getActivity()).mToken != null) {

					coinNum.setText(""
							+ (quizList.balance >= 1000000 ? (quizList.balance / 10000)
									+ "万"
									: quizList.balance));
					goldImg.setVisibility(View.VISIBLE);
					addImg.setVisibility(View.VISIBLE);

				} else {
					coinNum.setText(SharedPreferencesMgr.getString(
							"BindBtnText", quizList.balance + ""));
					goldImg.setVisibility(View.GONE);
					addImg.setVisibility(View.GONE);
				}

				if (quizList.balance == 0) {
					CoinNum = "0";
				} else if (quizList.balance > 0 && quizList.balance < 10) {
					CoinNum = "<10";
				} else if (quizList.balance >= 10 && quizList.balance < 100) {
					CoinNum = "10-100";
				} else if (quizList.balance >= 100 && quizList.balance < 1000) {
					CoinNum = "100-1000";
				} else if (quizList.balance >= 1000) {
					CoinNum = ">1000";
				}

				// 有小红点 跳过去
				if (mQid > 0) {
					setSelection(mQid);
				}

				// 更新直播流中的加注状态
				getJoinQuiz();
				((BaseGameActivity) getActivity()).updateBet(increaseList);
			}

		} else if (methodId == HuPuRes.REQ_METHOD_QUIZ_LIST_COMMIT
				|| methodId == HuPuRes.REQ_METHOD_POST_INCREASE) {
			// 提交竞猜
			QuizCommitResp entity = (QuizCommitResp) o;
			if (entity.result == -1)
				((BaseGameActivity) getActivity()).showToast("您已经参加过该竞猜了");
			else if (entity.result == 1) {
				reqNewData();
				((BaseGameActivity) getActivity()).showToast("参与成功");
			} else if (entity.result == 2) {
				reqNewData();
				// 通知领救济金
				((BaseGameActivity) getActivity())
						.showToast(getString(R.string.get_now_info));
			} else if (entity.result == -2) {
//				showChargeNotify(mCasinoDialog.getBetDif());// 没钱提示去冲钱
				if(!((BaseGameActivity) getActivity()).isExchange)
				{
					GoldBeanManager.getInstance().onExchangeGoldBeanDiaog((BasePayActivity) getActivity(), entity.eGoldBean);
				}
			} else if (entity.result == -4) {
				((BaseGameActivity) getActivity()).showToast("该竞猜已关闭");
			} else {
				((BaseGameActivity) getActivity()).showToast("投注金额超出限额");
			}
		} else if (methodId == HuPuRes.REQ_METHOD_BET_COINS) {
			BitCoinReq req = (BitCoinReq) o;
			if (req.balance == -1) {
				((BaseGameActivity) getActivity()).showToast("token无效，请重新登录");
			} else {
				updateMoney(req.betCoins, req.balance);
			}
		} else if (methodId == HuPuRes.REQ_METHOD_CAIPIAO_COMMIT) {
			// 提交彩票
			QuizCommitResp entity = (QuizCommitResp) o;
			if (entity.result == -1)
				((BaseGameActivity) getActivity()).showToast("余额不足");
			else if (entity.result == 1) {
				reqNewData();
				((BaseGameActivity) getActivity()).showToast("购买成功");
			}
		}
	}

	private Answer answer;
	private QuizResp quizList;

	CasinoDialog mCasinoDialog;

	/**  */
	public void showCasinoDialog(final Answer answer, int blc, boolean isB) {
		// if (mCasinoDialog != null && mCasinoDialog.isShowing()) {
		// return;
		// }

		if (bets != null && bets.length > 4) {
			reqBitCoin(answer.casino_id);
			bets[6] = answer.max_bet;
			mCasinoDialog = new CasinoDialog(getActivity(), new Click(), bets,
					isB);
			mCasinoDialog.goShow(answer);
		}
	}

	CaipiaoCasinoDialog mCaipiaoCasinoDialog;

	/**  */
	public void showCaipiaoCasinoDialog(final QuizCaipiaoEntity caipiao,
			int blc, boolean isB, int index) {
		mCaipiaoCasinoDialog = new CaipiaoCasinoDialog(getActivity(),
				new Click(), caipiao, index);
		mCaipiaoCasinoDialog.setBet(caipiaoBalance);
		mCaipiaoCasinoDialog.goShow(caipiao.title);
	}
	
	public void showCaipiaoCasinoDialog(final CaipiaoAnswer scoreAnswer,
			int blc, boolean isB) {
		mCaipiaoCasinoDialog = new CaipiaoCasinoDialog(getActivity(),
				new Click(), scoreAnswer);
		mCaipiaoCasinoDialog.setBet(caipiaoBalance);
		mCaipiaoCasinoDialog.goShow(scoreAnswer.caipiao_title);
	}
	

	// 更新余额
	public void updateMoney(int betCoin, int balance) {
		if (mCasinoDialog != null && mCasinoDialog.isShowing()) {
			mCasinoDialog.setBet(betCoin, balance);
		}
	}

	/** 请求余额 */
	public void reqBitCoin(int qid) {
		((BaseGameActivity) getActivity()).reqBitCoin(qid);
	}

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		}
	}

	QuizCaipiaoEntity caipiaoEntity;
	CaipiaoAnswer scoreAnswer;
	boolean isSendScoreAnswer = false;  //除了比分选项点击会为true 其他全部为false
	int answer_index = 0;

	// 竞猜列表所有点击事件的处理
	public class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_answer1:
				isIncrease = false;
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
							.showBindDialog(SharedPreferencesMgr.getString(
									"dialogQuize",
									getString(R.string.casino_notify)));
				} else {
					answer = (Answer) v.getTag();
					if (answer != null)
						showCasinoDialog(answer, 0, false);
				}
				break;
			case R.id.btn_answer2:
				isIncrease = false;
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
							.showBindDialog(SharedPreferencesMgr.getString(
									"dialogQuize",
									getString(R.string.casino_notify)));

				} else {
					answer = (Answer) v.getTag();
					if (answer != null)
						showCasinoDialog(answer, 0, false);
				}
				break;
			case R.id.btn_caipiao_answer1:
				((BaseGameActivity) getActivity()).sendUmeng(
						HuPuRes.UMENG_EVENT_LOTTERY,
						HuPuRes.UMENG_KEY_ANSWER_BTN,
						HuPuRes.UMENG_VALUE_LEFT_ANSWER);
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
							.showBindDialog(SharedPreferencesMgr.getString(
									"caipiaoLoginAlert",
									getString(R.string.casino_notify)));
				} else {
					answer_index = 0;
					caipiaoEntity = (QuizCaipiaoEntity) v.getTag();
					if (caipiaoEntity.mList.get(0).disable == 1) {
						((BaseGameActivity) getActivity()).showToast(SharedPreferencesMgr.getString("caipiaoLoseAlert", getActivity().getString(R.string.caipiao_lose_alert)));
					}else
						showCaipiaoCasinoDialog(caipiaoEntity, 0, false, 0);
				}
				break;

			case R.id.btn_caipiao_answer2:
				((BaseGameActivity) getActivity()).sendUmeng(
						HuPuRes.UMENG_EVENT_LOTTERY,
						HuPuRes.UMENG_KEY_ANSWER_BTN,
						HuPuRes.UMENG_VALUE_RIGHT_ANSWER);
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
							.showBindDialog(SharedPreferencesMgr.getString(
									"caipiaoLoginAlert",
									getString(R.string.casino_notify)));
				} else {
					answer_index = 1;
					caipiaoEntity = (QuizCaipiaoEntity) v.getTag();
					if (caipiaoEntity.mList.get(1).disable == 1) {
						((BaseGameActivity) getActivity()).showToast(SharedPreferencesMgr.getString("caipiaoLoseAlert", getActivity().getString(R.string.caipiao_lose_alert)));
					}else
						showCaipiaoCasinoDialog(caipiaoEntity, 0, false, 1);
				}
				break;
				
			case R.id.btn_caipiao_answer3:
				((BaseGameActivity) getActivity()).sendUmeng(
						HuPuRes.UMENG_EVENT_LOTTERY,
						HuPuRes.UMENG_KEY_ANSWER_BTN,
						HuPuRes.UMENG_VALUE_RIGHT_ANSWER);
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
					.showBindDialog(SharedPreferencesMgr.getString(
							"caipiaoLoginAlert",
							getString(R.string.casino_notify)));
				} else {
					answer_index = 2;
					caipiaoEntity = (QuizCaipiaoEntity) v.getTag();
					if (caipiaoEntity.mList.get(2).disable == 1) {
						((BaseGameActivity) getActivity()).showToast(SharedPreferencesMgr.getString("caipiaoLoseAlert", getActivity().getString(R.string.caipiao_lose_alert)));
					}else
						showCaipiaoCasinoDialog(caipiaoEntity, 0, false, 2);
				}
				break;
			case R.id.btn_faq:
				caipiaoEntity = (QuizCaipiaoEntity) v.getTag();
				Intent FAQ = new Intent(getActivity(), FAQActivity.class);
				FAQ.putExtra("url", caipiaoEntity.faq_link);
				startActivity(FAQ);
				break;
			case R.id.open_caipiao_result:
				Intent walletIntent = new Intent(getActivity(),
						MyCaipiaoListActivity.class);
				startActivity(walletIntent);
				break;

			case R.id.open_result:
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
							.showBindDialog(SharedPreferencesMgr.getString(
									"dialogQuize",
									getString(R.string.casino_notify)));
				} else {

					Intent info = new Intent(getActivity(),
							UserGoldActivity.class);
					startActivity(info);
				}
				break;
			case R.id.pop_box_toast:
				Intent pop = new Intent(getActivity(), MyBoxActivity.class);
				startActivityForResult(pop,
						((BaseGameActivity) getActivity()).REQ_SHOW_BOX);
				break;
			case R.id.btn_increase:
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
							.showBindDialog(SharedPreferencesMgr.getString(
									"dialogQuize",
									getString(R.string.casino_notify)));
				} else {
					isIncrease = true;
					answer = (Answer) v.getTag();
					if (answer != null) {
						showCasinoDialog(answer, 0, true);
					}
				}
				break;

			case R.id.btn_cancel:
				isIncrease = false;
				if (mCasinoDialog != null)
					mCasinoDialog.dismiss();

				break;
			case R.id.btn_confirm:

				// int cb = mCasinoDialog.canBet();
				// if (cb > 0)
				((BaseGameActivity) getActivity()).isExchange = false;
				((BaseGameActivity) getActivity()).sendQuizListCommit(answer,
						mCasinoDialog.getInputCoin(), isIncrease);
				// else if (cb == -1)
				// showChargeNotify(mCasinoDialog.getBetDif());// 没钱提示去冲钱

				if (mCasinoDialog != null)
					mCasinoDialog.dismiss();

				break;
			case R.id.btn_caipiao_confirm:
				if (mCaipiaoCasinoDialog.isAgree) {
					((BaseGameActivity) getActivity()).sendUmeng(HuPuRes.UMENG_EVENT_LOTTERY, HuPuRes.UMENG_KEY_LOTTERY_BET,HuPuRes.UMENG_VALUE_CONFIRM_BTN);
					int cBet = mCaipiaoCasinoDialog.canBet();
					if (cBet > 0)

						if (!isSendScoreAnswer) {
							((BaseGameActivity) getActivity()).showBuyCaipiao(
									caipiaoEntity,
									mCaipiaoCasinoDialog.getInputCoin(),
									answer_index);
						}else {
							((BaseGameActivity) getActivity()).showBuyScoreCaipiao(scoreAnswer,mCaipiaoCasinoDialog.getInputCoin());
						}
					else if (cBet == -1)
						showChargeNotify(getActivity(),
								mCaipiaoCasinoDialog.getBetDif());// 没钱提示去冲钱
					if (mCaipiaoCasinoDialog != null)
						mCaipiaoCasinoDialog.dismiss();

				} else {
					((BaseGameActivity) getActivity())
							.showToast("请先仔细阅读《彩票用户协议》并勾选同意，否则无法参与彩票购买");
				}
				isSendScoreAnswer = false;
				break;
			case R.id.btn_caipiao_cancel:
				isSendScoreAnswer = false;
				
				((BaseGameActivity) getActivity()).sendUmeng(HuPuRes.UMENG_EVENT_LOTTERY, HuPuRes.UMENG_KEY_LOTTERY_BET,HuPuRes.UMENG_VALUE_CANCEL_BTN);
				if (mCaipiaoCasinoDialog != null)
					mCaipiaoCasinoDialog.dismiss();
				
				break;
			case R.id.user_quiz_info:
				((BaseGameActivity) getActivity()).switchToGuessRank();
				break;
			case R.id.txt_agreement:
				Intent aIntent = new Intent(getActivity(), FAQActivity.class);
				aIntent.putExtra("url",
						HuPuRes.getUrl(HuPuRes.REQ_METHOD_CAIPIAO_AGREEMENT));
				startActivity(aIntent);
				break;

			case R.id.txt_faq:
				Intent fIntent = new Intent(getActivity(), FAQActivity.class);
				fIntent.putExtra("url",
						HuPuRes.getUrl(HuPuRes.REQ_METHOD_CAIPIAO_FAQ));
				startActivity(fIntent);
				break;
				
			case R.id.score_item_layout:
				
				if (((BaseGameActivity) getActivity()).mToken == null) {
					// 需要登录
					((BaseGameActivity) getActivity())
					.showBindDialog(SharedPreferencesMgr.getString(
							"caipiaoLoginAlert",
							getString(R.string.casino_notify)));
				} else {
//					caipiaoEntity = (QuizCaipiaoEntity) v.getTag();
					isSendScoreAnswer = true;
					scoreAnswer = (CaipiaoAnswer)v.getTag();
					showCaipiaoCasinoDialog(scoreAnswer,0,false);
				}
				
				break;

			default:
				break;
			}
			

		}

	}
	
	public void sendQuizCommit() {
		((BaseGameActivity) getActivity()).sendQuizListCommit(answer,
				mCasinoDialog.getInputCoin(), isIncrease);
		
		if (mCasinoDialog != null)
			mCasinoDialog.dismiss();
	}
	private void showChargeNotify(int bet) {
		String s = getResources().getString(R.string.no_charge1);
		((BaseGameActivity) getActivity()).showCustomDialog(
				((BaseGameActivity) getActivity()).DIALOG_SHOW_CHARGE_NOTIFY,
				s.format(s, bet), BaseGameActivity.TOW_BUTTONS,
				R.string.buy_at_once, R.string.cancel);
	}

	private void showChargeNotify(Context context, int bet) {
		String s = getResources().getString(R.string.no_wallet);
		((BaseGameActivity) getActivity()).showRechargeCaipiao(context, bet);
	}

	public ArrayList<IncreaseEntity> increaseList;

	private void getJoinQuiz() {
		IncreaseEntity increase;
		increaseList = new ArrayList<IncreaseEntity>();
		if (quizList.list != null) {

			for (QuizListResp listResp : quizList.list) {
				if (listResp.status == 0) {
					for (QuizEntity entity : listResp.mQuizList) {
						if (entity.is_user_join == 1) {
							increase = new IncreaseEntity();
							increase.qid = entity.qid;
							increase.answerId = entity.user_choose;
							increaseList.add(increase);
						}
					}
				}
			}
		}
	}

	// 自动跳到 已经开奖
	private void setSelection(int qid) {
		int size = 0;
		int i = 0;
		for (QuizListResp quizs : quizList.list) {

			if (quizs.status == 2) {

				for (QuizEntity entity : quizs.mQuizList) {
					if (entity.qid == qid) {
						size = size + i;
						break;
					}
					i++;
				}
				break;
			} else
				size = size + quizs.mQuizList.size();
		}
		size = size + quizList.list.size() + 1;
		HupuLog.e("papa", "qid===" + qid + "----size====" + size
				+ "-----count==" + mAdapter.getCount());
		if (size > 0 && size < mAdapter.getCount()) {
			mListView.setSelection(size);
		}
		// 红点id 复位
		((BaseGameActivity) getActivity()).mQid = -1;
	}

}
