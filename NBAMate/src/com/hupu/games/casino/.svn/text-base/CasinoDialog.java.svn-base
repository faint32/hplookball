/**
 * 
 */
package com.hupu.games.casino;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.LiveEntity.Answer;

/**
 * @author panyongjun
 * 
 */
public class CasinoDialog extends Dialog {

	private static final String choice = "我猜：%s";

	private static final String balance1 = "当前余额：<font color=\"#FF0000\">%d</font>金豆";


//	private static final String bet1 = "投入:<font color=\"#FF0000\">%d</font>金币";
//
//	private static final String bet2 = "已投入:<font color=\"#FF0000\">%d</font>金币";
//
//	private static final String bet3 = "共投入:<font color=\"#FF0000\">%d</font>金币";
//
//	private static final String bet4 = "加注:<font color=\"#FF0000\">%d</font>金币";

	private static final String balance2 = "余额不足，需兑换<font color=\"#FF0000\">%d</font>金豆";

	/** 题目 */
	TextView txtContent;
	/** 选择的答案 */
	TextView txtChoice;

	TextView txtBalance;

	GridView gdView;
	/** 用户下注 */
	int bet;
	/** 可以下注的选项 */
	int[] bets;


	/** 是不是无限制下注 */
	boolean bNoLimited;

	/** 余额 */
	private int balance;
	/** 已投入金额 */
	private int beted;

	private int res[] = { R.id.radio0, R.id.radio1, R.id.radio2, R.id.radio3,
			R.id.radio4, R.id.radio5 };
	private TextView tvs[];
	/** 最大下注额 */
	private int maxBet;
	/** 是否是加注 */
	private boolean isBeted;
	
	BaseGameActivity mContext;

	/**
	 * @param b 是否是加注
	 * */
	public CasinoDialog(Context context, View.OnClickListener click, int[] bs,
			boolean b) {
		super(context, R.style.MyWebDialog);
		mContext = (BaseGameActivity)context;
		isBeted = b;
		View v = LayoutInflater.from(context).inflate(R.layout.dialog_casino,
				null);
		txtContent = (TextView) v.findViewById(R.id.txt_casino_title);
		txtChoice = (TextView) v.findViewById(R.id.txt_casino_choice);
		txtBalance = (TextView) v.findViewById(R.id.txt_1);

		bets = bs;
		maxBet =bets[6];
		bNoLimited = maxBet== 0;
		v.findViewById(R.id.btn_cancel).setOnClickListener(click);
		v.findViewById(R.id.btn_confirm).setOnClickListener(click);

		setContentView(v);
		getWindow().setGravity(Gravity.CENTER);
		initRadio(v);
	}

	private void initRadio(View v) {
		RadiobuttonListener n = new RadiobuttonListener();
		tvs = new TextView[6];
		for (int i = 0; i < 6; i++) {
			tvs[i] = (TextView) v.findViewById(res[i]);
			tvs[i].setOnClickListener(n);			
			if (!bNoLimited)//有限额
				tvs[i].setEnabled(false);
			else
			{
				if(i==0)
					tvs[i].performClick();	
			}

			tvs[i].setText(bets[i] + "金豆");				

		}
	}

	/**
	 * 显示对话框
	 * */
	public void goShow(Answer answer) {
		HupuLog.d("goShow","answer=" +answer.content+" max="+bets[6]);

		txtContent.setText(answer.content+(bets[6]>0?"(最多投入"+bets[6]+"金豆)":""));

		txtChoice.setText(String.format(choice, answer.title));
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
	public void setBet(int v1, int v2) {
		balance = v2;
		beted = v1;
		HupuLog.d("setBet", "type=" + isBeted);
		HupuLog.d("setBet", "balance=" + balance + " ;beted=" + beted);
		if (isBeted) {
			//加注显示已加注金额
			String s =txtChoice.getText()+"(已投入"+beted+")";
			txtChoice.setText(s);	
		}
		if (!bNoLimited)//有限额
		{
			boolean set=false;
			for (int i = 0; i < 6; i++) {				
				if ( bets[i]+beted<= maxBet)
				{
					tvs[i].setEnabled(true);
					if(!set)
					{
						tvs[i].performClick();
						set =true;
					}
				}
			}
		}
		txtBalance.setText( Html.fromHtml(String.format(balance1, balance)) );// 余额
	

	}

	private void updateValue() {
		HupuLog.d("updateValue", "bet=" + bet);
	
	}

	public int getInputCoin() {
		return bet;
	}

	/**是否有钱下注*/
	public int canBet()
	{
		if(bet ==0)
			return -2;//未选择下注金额
		if(balance<5||balance<bet)
			return -1;//余额不足	
		return 1;
	}
	
	public int getBetDif()
	{
		return bet-balance;
	}
	View lastView;

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
				break;
			case R.id.radio1:
				bet = bets[1];
				break;
			case R.id.radio2:
				bet = bets[2];
				break;
			case R.id.radio3:
				bet = bets[3];
				break;
			case R.id.radio4:
				bet = bets[4];
				break;
			case R.id.radio5:
				bet =  bets[5];
				break;
			}
			
			updateValue();

		}

	}
}
