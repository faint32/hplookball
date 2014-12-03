/**
 * 
 */
package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.CommitExchangeReq;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.pay.PhoneBindActivity;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.http.RequestParams;


@SuppressLint("NewApi")
public class ExchangePrizeActivity extends BasePayActivity {

	private int id;
	Intent mIntent;
	TextView prizeName,coinNum,exchangeNum,memotTextView;
	ImageView prizeImg;
	int eNum = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_prize_exchange);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.commit_exchange);
		prizeName = (TextView)findViewById(R.id.prize_name);
		coinNum = (TextView)findViewById(R.id.coin_num);
		exchangeNum = (TextView)findViewById(R.id.exchange_num);
		memotTextView = (TextView)findViewById(R.id.prize_memo);
		prizeImg = (ImageView) findViewById(R.id.prize_img);
		mIntent = getIntent();
		UrlImageViewHelper.setUrlDrawable(prizeImg,mIntent.getStringExtra("img"),R.drawable.bg_mall_no_pic_s);
		prizeName.setText(mIntent.getStringExtra("name"));
		coinNum.setText(Html.fromHtml("售价："+"<font color=\"#FF0000\">"+mIntent.getIntExtra("coin", 0)+"</font>"));
		eNum = mIntent.getIntExtra("exchange_num", 0);
		exchangeNum.setText(Html.fromHtml("已有 "+"<font color=\"#FF0000\">"+eNum+"</font>"+" 人兑换"));
		memotTextView.setText(mIntent.getStringExtra("memo"));
	}


	@Override
	public void treatClickEvent(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.commit_exchange:
			sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_DETAIL, HuPuRes.UMENG_VALUE_EXCHANGE);
			
			if (mToken != null) {

				showCustomDialog(DIALOG_EXCHANGE_PRIZE,"确认花费"+mIntent.getIntExtra("coin", 0)+"金豆兑换"+mIntent.getStringExtra("name"),
						BaseGameActivity.TOW_BUTTONS, R.string.btn_exchange_prize,
						R.string.cancel);
			}else {
				showBindDialog(SharedPreferencesMgr.getString("dialogExchange", getString(R.string.bind_phone_dialog)));
				//dialog(this,SharedPreferencesMgr.getString("dialogExchange", getString(R.string.bind_phone_dialog)));
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		if (dialogId == DIALOG_EXCHANGE_PRIZE) {
			sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_DETAIL, HuPuRes.UMENG_VALUE_EXCHANGE_CONFIRM);
			
			
			initParameter();
			mParams.put("token", mToken);
			HupuLog.e("id", "pid"+mIntent.getStringExtra("id"));
			mParams.put("id", mIntent.getStringExtra("id"));
			sendRequest(HuPuRes.REQ_METHOD_POST_EXCHANGE, mParams, new HupuHttpHandler(
					this), false);
		}else if (dialogId == DIALOG_EXCHANGE_SUCCESS) {
			exchangeNum.setText(Html.fromHtml("已有 "+"<font color=\"#FF0000\">"+eNum+"</font>"+" 人兑换"));
		}
	}
	
	@Override
	public void clickNegativeButton(int dialogId) {
		// TODO Auto-generated method stub
		if (dialogId == DIALOG_EXCHANGE_PRIZE) {
			sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_DETAIL, HuPuRes.UMENG_VALUE_EXCHANGE_CANCEL);
		}
		super.clickNegativeButton(dialogId);
	}


	@Override
	public void onErrResponse(Throwable error,  int type) {
		super.onErrResponse(error, type);
		String content =error.toString();
		showToast(content);
	}

	public void switchToPhoneBindAct() {
		Intent intent = new Intent(ExchangePrizeActivity.this,
				PhoneBindActivity.class);
		startActivityForResult(intent, HupuBaseActivity.REQ_GO_BIND_PHONE);
	}

	private void getExchangeList() {
		initParameter();
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_GET_EXCHANGE_LIST, mParams, new HupuHttpHandler(
				this), false);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		//
		if (methodId == HuPuRes.REQ_METHOD_POST_EXCHANGE) {
			QuizCommitResp code = (QuizCommitResp) o;
			switch (code.result) {
			case 1:
				//兑换成功后  兑换人数+1    为了体验。。。
				eNum++;
				showCustomDialog(DIALOG_EXCHANGE_SUCCESS, 0,
						R.string.exchange_success, ONE_BUTTON, R.string.title_confirm,
						0);
				sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_DETAIL, HuPuRes.UMENG_VALUE_EXCHANGE_SUCCESS);
				//showToast("兑换成功！您可以在“我的奖品”查看发货进度。客服会与您联系发货相关事宜，请注意保持手机开机");
				break;
			case -1:
				showToast("奖品不存在");
				break;
			case -2:
				
				showCustomDialog(DIALOG_EXCHANGE_ERROR, 0,
						R.string.exchange_error, ONE_BUTTON, R.string.title_confirm,
						0);
				sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_DETAIL, HuPuRes.UMENG_VALUE_NO_COIN_ALERT_CONFIRM);
				break;
			case -3:
				showToast("兑换失败");
				break;

			default:
				break;
			}
			
		}
	}


	public void sendQuizCommit(Answer answer, int coin) {
		//
		RequestParams p = new RequestParams();
		p.clear();
		p.put("client", mDeviceId);
		if (mToken != null)
			p.put("token", mToken);

		p.put("qid", "" + answer.casino_id);
		p.put("coin", "" + coin);
		p.put("answer", "" + answer.answer_id);
		sendRequest(HuPuRes.REQ_METHOD_QUIZ_COMMIT, p, new HupuHttpHandler(
				ExchangePrizeActivity.this), false);
	}

}
