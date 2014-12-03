/**
 * 
 */
package com.hupu.games.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.MyCaipiaoListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.MyCaipiaoResp;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.game.quiz.QuizEntity;
import com.hupu.games.data.game.quiz.QuizListResp;
import com.hupu.games.data.game.quiz.QuizResp;
import com.hupu.games.pay.PhoneInputActivity;
import com.hupu.games.view.PinnedHeaderXListView;
import com.hupu.games.view.PinnedHeaderXListView.IXListViewListener;
import com.hupu.http.HupuHttpHandler;

public class MyCaipiaoListActivity extends HupuBaseActivity {

	PinnedHeaderXListView mListView;

	MyCaipiaoListAdapter mAdapter;
	Intent in;
	LayoutInflater mInflater;
	private View walletInfo;
	TextView txtBalance,txtRechargeDiscount;
	View v;
	int qid = 0;
	int page = 0;
	private String money = "0.00";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_my_caipiao_list);
		in = getIntent();
		mInflater = LayoutInflater.from(this);
		walletInfo = mInflater.inflate(R.layout.item_header_my_wallet, null);
		txtBalance = (TextView) walletInfo.findViewById(R.id.txt_wallet_num);
		txtRechargeDiscount = (TextView) walletInfo.findViewById(R.id.recharge_explain);
		qid = in.getIntExtra("qid", 0);
		mListView = (PinnedHeaderXListView) findViewById(R.id.list_player);
		mListView.setPullLoadEnable(false, true);
		mListView.addHeaderView(walletInfo);
		mListView.mFooterView.findViewById(R.id.xlistview_footer_text)
				.setVisibility(View.GONE);
		((TextView) mListView.mFooterView
				.findViewById(R.id.xlistview_footer_text))
				.setText(getString(R.string.no_more_caipiao));

		mListView.setXListViewListener(new pullListener());
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.gold_num);
		setOnClickListener(R.id.btn_recharge);
		setOnClickListener(R.id.btn_atm);
		setOnClickListener(R.id.layout_coin_info);
		findViewById(R.id.gold_num).setVisibility(View.GONE);

		mListView.setOnItemClickListener(new PlayerListClick());
		
		txtBalance.setText(money);
		txtRechargeDiscount.setText(SharedPreferencesMgr.getString("caipiaoDiscountTips", ""));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getCaipiaoList();
		super.onResume();
	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {

			// 加载最新竞猜数据
			getCaipiaoList();
		}

		@Override
		public void onLoadMore() {
			if (page > 1) {
				reqMoreData();
			}
		}

	}

	private MyCaipiaoResp caipiaoList;

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_recharge:
			sendUmeng(HuPuRes.UMENG_EVENT_MY_WALLET, HuPuRes.UMENG_KEY_WALLET_CHARGE);
			Intent order = new Intent(this, UserRechargeActivity.class);
			order.putExtra("balance", money);
			startActivity(order);
			break;
		case R.id.layout_coin_info:
			sendUmeng(HuPuRes.UMENG_EVENT_MY_WALLET, HuPuRes.UMENG_KEY_WALLET_ACCOUNT);
			Intent infoIntent = new Intent(this, CoinInfoActivity.class);
			infoIntent.putExtra("info_type", HuPuRes.REQ_METHOD_GET_CAIPIAO_COIN_INFO);
			startActivity(infoIntent);
			break;
		case R.id.btn_atm:
			sendUmeng(HuPuRes.UMENG_EVENT_MY_WALLET, HuPuRes.UMENG_KEY_WALLET_WITHDRAW);
			Intent atmIntent = new Intent(this, CoinInfoActivity.class);
			atmIntent.putExtra("info_type", HuPuRes.REQ_METHOD_GET_CAIPIAO_ATM);
			startActivity(atmIntent);
			break;
		}
	}

	@Override
	public void treatClickEvent(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.open_caipiao_result:
//			Intent info = new Intent(this, UserWalletActivity.class);
//			startActivity(info);
			break;
		}
	}

	void switchToPhoneBindAct() {
		Intent intent = new Intent(MyCaipiaoListActivity.this,
				PhoneInputActivity.class);
		startActivityForResult(intent, HupuBaseActivity.REQ_GO_BIND_PHONE);
	}

	private void getCaipiaoList() {
		initParameter();
		mParams.put("token", mToken);
		mParams.put("page", "1");

		String sign = SSLKey.getSSLSign(mParams,
				SharedPreferencesMgr.getString("sugar", ""));// salt
																// 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);

		sendRequest(HuPuRes.REQ_METHOD_MY_CAIPIAO_LIST, mParams,
				new HupuHttpHandler(this), false);
	}

	private void reqMoreData() {

		initParameter();
		mParams.put("token", mToken);
		mParams.put("page", page + "");
		String sign = SSLKey.getSSLSign(mParams,
				SharedPreferencesMgr.getString("sugar", ""));// salt
																// 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_MY_CAIPIAO_MORE_LIST, mParams,
				new HupuHttpHandler(this), false);
	}

	private void setSelection(QuizResp list, int qid) {
		int size = 0;
		for (QuizListResp quizList : list.list) {
			if (quizList.status == 2) {
				int i = 0;
				for (QuizEntity entity : quizList.mQuizList) {
					if (entity.qid == qid) {
						size += i;
						break;
					}
					i++;
				}
				break;
			}
			size += quizList.mQuizList.size();
		}
		if (size > 0 && size < mAdapter.getCount()) {
			mListView.setSelection(size);
		}
		qid = 0;
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		//
		if (o != null) {
			HupuLog.e("papa", "methodId" + methodId);
			if (methodId == HuPuRes.REQ_METHOD_MY_CAIPIAO_LIST) {

				caipiaoList = (MyCaipiaoResp) o;
				money = caipiaoList.walletBalance;
				txtBalance.setText(caipiaoList.walletBalance+"元");
				if (caipiaoList.current < caipiaoList.total) {
					page = caipiaoList.current + 1;
					// 开启加载更多
					mListView.setPullLoadEnable(true, true);
				} else {
					mListView.setPullLoadEnable(false, true);
				}
				if (caipiaoList.mList != null && caipiaoList.mList.size() > 0) {
					findViewById(R.id.img_no_date).setVisibility(View.GONE);
				} else {
					findViewById(R.id.img_no_date).setVisibility(View.VISIBLE);
					mListView.setPullLoadEnable(false, false);
				}

				mAdapter = new MyCaipiaoListAdapter(this, click);

				mAdapter.setData(caipiaoList);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();

				mListView.stopRefresh();
				mListView.stopLoadMore();

			} else if (methodId == HuPuRes.REQ_METHOD_MY_CAIPIAO_MORE_LIST) {

				MyCaipiaoResp moreCaipiaoList = (MyCaipiaoResp) o;
				if (moreCaipiaoList.current < moreCaipiaoList.total) {
					page++;
					mListView.setPullLoadEnable(true, true);
				} else {
					mListView.setPullLoadEnable(false, true);
				}
				HupuLog.e("papa", "moresize===" + moreCaipiaoList.mList.size());
				for (int i = 0; i < moreCaipiaoList.mList.size(); i++) {
					caipiaoList.mList.add(moreCaipiaoList.mList.get(i));
				}
				mAdapter.setData(caipiaoList);
				mAdapter.notifyDataSetChanged();

				mListView.stopRefresh();
				mListView.stopLoadMore();

			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return false;
	}

	class PlayerListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			int section = mAdapter.getSectionForPosition(pos - 2);
			int child = mAdapter.getPositionInSectionForPosition(pos - 2);
			// mAdapter.getChildPosition(pos)
			if (child >=0 && caipiaoList.mList.get(section).mQuizList.get(child).ubid != 0) {

				Intent in = new Intent(MyCaipiaoListActivity.this,
						CoinInfoActivity.class);
				in.putExtra(
						"ubid",
						caipiaoList.mList.get(section).mQuizList.get(child).ubid);
				in.putExtra("info_type", HuPuRes.REQ_METHOD_CAIPIAO_DETAIL);
				startActivity(in);
			}
			
			//点击的是
			if (child < 0 && !caipiaoList.mList.get(section).scheme.equals("") && !caipiaoList.mList.get(section).lid.equals("")) {
				treatScheme(caipiaoList.mList.get(section).scheme, Integer.parseInt(caipiaoList.mList.get(section).lid));
			}

		}

	}

}
