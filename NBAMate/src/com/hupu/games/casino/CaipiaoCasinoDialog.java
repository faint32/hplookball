/**
 * 
 */
package com.hupu.games.casino;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity;
import com.hupu.games.data.game.quiz.QuizCaipiaoListResp;
import com.hupu.games.data.game.quiz.QuizCaipiaoEntity.CaipiaoAnswer;

/**
 * @author dongshuming
 * 
 */
public class CaipiaoCasinoDialog extends Dialog {


	private static final String balance2 = "余额不足，需兑换<font color=\"#FF0000\">%d</font>金豆";

	/** 题目 */
	TextView txtContent;
	/** 选择的答案 */
	TextView txtChoice;

	TextView txtBetInfo;

	GridView gdView;
	/** 用户下注 */
	int bet;
	/** 可以下注的选项 */
	int[] bets;

	/** 是不是无限制下注 */
	boolean bNoLimited;

	/** 余额 */
	private float balance;
	/** 已投入金额 */
	private int beted;

	private int res[] = { R.id.radio0, R.id.radio1, R.id.radio2, R.id.radio3,
			R.id.radio4, R.id.radio5 };
	private TextView tvs[], txtAgreement;
	/** 最大下注额 */
	// private int maxBet;

	BaseGameActivity mContext;
	private ImageButton btnAgree;
	private TextView txtTips;

	/**
	 * 
	 * @param context
	 * @param click
	 * @param bs
	 *            下注选项
	 * @param caipiao
	 *            彩票实体
	 * @param answerIndex
	 *            选项ID
	 */
	public CaipiaoCasinoDialog(Context context, View.OnClickListener click,
			QuizCaipiaoEntity caipiao, int answerIndex) {
		super(context, R.style.MyWebDialog);
		mContext = (BaseGameActivity) context;
		View v = LayoutInflater.from(context).inflate(
				R.layout.dialog_caipiao_casino, null);
		txtContent = (TextView) v.findViewById(R.id.txt_casino_title);
		txtChoice = (TextView) v.findViewById(R.id.txt_casino_choice);
		txtBetInfo = (TextView) v.findViewById(R.id.txt_bet_info);

		if (bets == null) {
			bets = new int[6];
		}
		// reqBitCoin(answer.casino_id);
		if (caipiao.mList.get(answerIndex).bets != null) {
			for (int i = 0; i < caipiao.mList.get(answerIndex).bets.size(); i++) {
				bets[i] = caipiao.mList.get(answerIndex).bets.get(i).bet;
			}
		}
		// maxBet = bets[6];
		// bNoLimited = maxBet == 0;
		v.findViewById(R.id.btn_caipiao_cancel).setOnClickListener(click);
		v.findViewById(R.id.btn_caipiao_confirm).setOnClickListener(click);
		txtAgreement = (TextView) v.findViewById(R.id.txt_agreement);
		btnAgree = (ImageButton) v.findViewById(R.id.img_agree);
		txtTips = (TextView) v.findViewById(R.id.txt_bet_tips);
		txtAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		txtAgreement.setOnClickListener(click);
		btnAgree.setOnClickListener(new agreeButtonListener());
		txtTips.setText(SharedPreferencesMgr.getString("caipiaoTips",context.getString(R.id.txt_bet_tips).toString()).toString());
		setContentView(v);
		getWindow().setGravity(Gravity.CENTER);

		// 初始化 相关选项实体
		answer = caipiao.mList.get(answerIndex);

		initRadio(v);
	}
	
	public CaipiaoCasinoDialog(Context context, View.OnClickListener click,
			CaipiaoAnswer scoreAnswer) {
		super(context, R.style.MyWebDialog);
		mContext = (BaseGameActivity) context;
		View v = LayoutInflater.from(context).inflate(
				R.layout.dialog_caipiao_casino, null);
		txtContent = (TextView) v.findViewById(R.id.txt_casino_title);
		txtChoice = (TextView) v.findViewById(R.id.txt_casino_choice);
		txtBetInfo = (TextView) v.findViewById(R.id.txt_bet_info);

		if (bets == null) {
			bets = new int[6];
		}
		// reqBitCoin(answer.casino_id);
		if (scoreAnswer.bets != null) {
			for (int i = 0; i < scoreAnswer.bets.size(); i++) {
				bets[i] = scoreAnswer.bets.get(i).bet;
			}
		}
		// maxBet = bets[6];
		// bNoLimited = maxBet == 0;
		v.findViewById(R.id.btn_caipiao_cancel).setOnClickListener(click);
		v.findViewById(R.id.btn_caipiao_confirm).setOnClickListener(click);
		txtAgreement = (TextView) v.findViewById(R.id.txt_agreement);
		btnAgree = (ImageButton) v.findViewById(R.id.img_agree);
		txtTips = (TextView) v.findViewById(R.id.txt_bet_tips);
		txtAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		txtAgreement.setOnClickListener(click);
		btnAgree.setOnClickListener(new agreeButtonListener());
		txtTips.setText(SharedPreferencesMgr.getString("caipiaoTips",context.getString(R.id.txt_bet_tips).toString()).toString());
		setContentView(v);
		getWindow().setGravity(Gravity.CENTER);

		// 初始化 相关选项实体
		answer = scoreAnswer;

		initRadio(v);
	}
	
	

	private void initRadio(View v) {
		RadiobuttonListener n = new RadiobuttonListener();
		tvs = new TextView[6];
		for (int i = 0; i < 6; i++) {
			tvs[i] = (TextView) v.findViewById(res[i]);
			tvs[i].setOnClickListener(n);
			if (i == 1)
				tvs[i].performClick();
			tvs[i].setText(bets[i] + "元");
		}
	}

	/**
	 * 显示对话框
	 * */
	private CaipiaoAnswer answer;

	public void goShow(String title) {

		txtContent.setText(title);
		txtChoice.setText("我猜：" + answer.answer_title+("".equals(answer.odd)?"":("("+answer.odd+")"))); //如果有赔率需要加上赔率
		show();
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 设置余额
	 * 
	 * @param v1
	 *            已投入
	 * @param v2
	 *            余额
	 * */
	public void setBet(float caipiaoBalance) {
		balance = caipiaoBalance;
	}

	private void updateValue() {
		HupuLog.d("updateValue", "bet=" + bet);

	}

	public int getInputCoin() {
		return bet;
	}

	/** 是否有钱下注 */
	public int canBet() {
		if (bet == 0)
			return -2;// 未选择下注金额
		if (balance < 1f || balance < bet)
			return -1;// 余额不足
		return 1;
	}

	public int getBetDif() {
		float coin = bet - balance;
		return ((int)coin)-coin < 0 ? ((int)coin) + 1:(int)coin;
		
	}

	View lastView;

	private void updateBetInfo(int index) {
		txtBetInfo.setText(Html.fromHtml("投入：<font color=\"#FF0000\">" + bet
				+ "</font>" + "元，预计奖金" + answer.bets.get(index).bonus + "元"));
	}

	private class RadiobuttonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (lastView != null)
				lastView.setSelected(false);
			v.setSelected(true);
			lastView = v;
			switch (id) {
			case R.id.radio0:
				bet = bets[0];
				updateBetInfo(0);
				break;
			case R.id.radio1:
				bet = bets[1];
				updateBetInfo(1);
				break;
			case R.id.radio2:
				bet = bets[2];
				updateBetInfo(2);
				break;
			case R.id.radio3:
				bet = bets[3];
				updateBetInfo(3);
				break;
			case R.id.radio4:
				bet = bets[4];
				updateBetInfo(4);
				break;
			case R.id.radio5:
				bet = bets[5];
				updateBetInfo(5);
				break;
			}

			updateValue();

		}

	}

	public boolean isAgree = true;

	private class agreeButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (isAgree) {
				v.setBackgroundResource(R.drawable.choose_btn_red);
				isAgree = false;
			} else {
				v.setBackgroundResource(R.drawable.choose_btn_down);
				isAgree = true;
			}

		}

	}
}
