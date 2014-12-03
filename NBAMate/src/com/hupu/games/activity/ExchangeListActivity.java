/**
 * 
 */
package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.hupu.games.R;
import com.hupu.games.adapter.ExchangeListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BalanceReq;
import com.hupu.games.data.CommitExchangeReq;
import com.hupu.games.data.ExchangeResp;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.PrizeEntity;
import com.hupu.games.pay.PhoneBindActivity;
import com.hupu.http.HupuHttpHandler;
import com.pyj.http.RequestParams;

@SuppressLint("NewApi")
public class ExchangeListActivity extends HupuBaseActivity {

	GridView mListView;

	ExchangeListAdapter mAdapter;
	LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_exchange_list);

		mInflater = LayoutInflater.from(this);

		getExchangeList();
		// reqBalance();
		mListView = (GridView) findViewById(R.id.list_exchange);

		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_my_prize);
	}

	@Override
	public void treatClickEvent(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.commit_exchange:
			initParameter();
			mParams.put("token", mToken);
			mParams.put("pid", view.getTag().toString());
			sendRequest(HuPuRes.REQ_METHOD_POST_EXCHANGE, mParams,
					new HupuHttpHandler(this), false);
			break;
		case R.id.btn_my_prize:
			if (mToken == null) {
				showBindDialog(SharedPreferencesMgr.getString("dialogPrizeListToMyPrize", getString(R.string.bind_phone_dialog)));
			} else {
				sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_MY_AWARD, HuPuRes.UMENG_VALUE_FROM_MALL_PAGE);
				
				Intent myPrize = new Intent(this, MyPrizeListActivity.class);
				startActivity(myPrize);
			}
			break;

		default:
			break;
		}

		// if (id == R.id.btn_answer1) {
		// if (mToken == null) {
		// // 需要登录
		// switchToPhoneBindAct();
		// } else {
		//
		// }
		// } else if (R.id.btn_answer2 == id) {
		// if (mToken == null) {
		// // 需要登录
		// switchToPhoneBindAct();
		//
		// } else {
		//
		// }
		// }
	}

	void switchToPhoneBindAct() {
		Intent intent = new Intent(ExchangeListActivity.this,
				PhoneBindActivity.class);
		startActivityForResult(intent, HupuBaseActivity.REQ_GO_BIND_PHONE);
	}

	private void getExchangeList() {
		initParameter();
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_GET_EXCHANGE_LIST, mParams,
				new HupuHttpHandler(this), false);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		//
		if (methodId == HuPuRes.REQ_METHOD_GET_EXCHANGE_LIST) {

			if (o != null) {
				ExchangeResp exchangeResp = (ExchangeResp) o;

				mAdapter = new ExchangeListAdapter(this, click);
				if (exchangeResp.list != null && exchangeResp.list.size() > 0) {
					mAdapter.setData(exchangeResp.list);
				}
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();

				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						// TODO Auto-generated method stub
						
						
						Intent prizeIntent = new Intent(
								ExchangeListActivity.this,
								ExchangePrizeActivity.class);
						PrizeEntity entity = (PrizeEntity) mAdapter
								.getItem(pos);
						prizeIntent.putExtra("id", entity.id + "");
						prizeIntent.putExtra("coin", entity.coin);
						prizeIntent.putExtra("name", entity.name);
						prizeIntent.putExtra("img", entity.img);
						prizeIntent.putExtra("memo", entity.memo);
						prizeIntent.putExtra("exchange_num",
								entity.exchange_count);
						
						sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_LIST, HuPuRes.UMENG_VALUE_TAP_ONE_ITEM);
						
						startActivity(prizeIntent);

					}
				});
			}

		} else if (methodId == HuPuRes.REQ_METHOD_POST_EXCHANGE) {
			CommitExchangeReq commitreq = (CommitExchangeReq) o;
			switch (commitreq.msg) {
			case 1:
				showToast("兑换成功");
				getExchangeList();
				break;
			case -1:
				showToast("奖品不存在");
				break;
			case -2:
				showToast("账户余额不足");
				break;
			case -3:
				showToast("兑换失败");
				break;

			default:
				break;
			}

		} else if (methodId == HuPuRes.REQ_METHOD_GET_BALANCE) {
			BalanceReq req = (BalanceReq) o;
			Log.e("papa", "balan" + req.balance);
			if (req.balance == -1) {
				showToast("token无效，请重新登录");
			} else {
				money = req.balance;
			}
		}
	}

	// public void onClick(){
	// Answer answer = new Answer();
	// answer.answer_id = 1;
	// answer.casino_id = 1;
	// answer.content = "content";
	// answer.title = "绝壁不对";
	// showCasinoDialog(answer, 0);
	// }

	/** 请求余额 */
	public void reqBalance() {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			sendRequest(HuPuRes.REQ_METHOD_GET_BALANCE, mParams,
					new HupuHttpHandler(this), true);
		}

	}

	private int money;

	/*
	 * private void sendQuiz() { // -- try { int c =
	 * Integer.parseInt(edtCoin.getEditableText().toString());
	 * 
	 * sendQuizCommit(mAnswer, c); } catch (NumberFormatException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

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
				ExchangeListActivity.this), false);
	}

}
