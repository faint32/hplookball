package com.hupu.games.hupudollor.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.hupudollor.data.HupuDollorBalanceReq;
import com.hupu.http.HupuHttpHandler;

/**
 *  @author zhenhua 我的虎扑币
 * */
@SuppressLint("NewApi")
public class UserHupuDollorInfoActivity extends HupuBaseActivity{

	private TextView txtBoxNum, guessResult;
	boolean byMan;
	private int money = 0;
	String CoinNum = "0";
	int resumeNum = 0;
	TextView txtBalance;
	//	LinearLayout prizeLayout, taskLayout;
	View mView;
	int viewWidth = 0, viewHeight = 0;
	ProgressBar goldBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hupucoin_info);

		setOnClickListener(R.id.btn_back);

		setOnClickListener(R.id.layout_hupucoin_log);
		setOnClickListener(R.id.layout_payhupu);

		txtBoxNum = (TextView) findViewById(R.id.txt_box_num);
		txtBalance = (TextView) findViewById(R.id.txt_coin_num);
		goldBar = (ProgressBar) findViewById(R.id.gold_Porgress);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		mView = getLayoutInflater().inflate(R.layout.item_coin_prize, null);
		viewWidth = mView.getWidth();
		viewHeight = mView.getHeight();
	}

	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.layout_payhupu://购买虎扑币
			if (mToken == null) {
				showBindDialog(SharedPreferencesMgr.getString("dialogBtnText",
						getString(R.string.bind_phone_dialog)));
			} else {
				Intent order = new Intent(this, HupuDollorOrderActivity.class);
//				order.putExtra("balance", money);
//				startActivityForResult(order, REQ_GO_CHARGE);
				startActivity(order);
			}

			break;
		case R.id.layout_hupucoin_log://消费明细
			if (mToken == null) {
				showBindDialog(SharedPreferencesMgr.getString("dialogBtnText",
						getString(R.string.bind_phone_dialog)));
			} else {
				Intent infoIntent = new Intent(this, HupuDollorLogActivity.class);
				//			infoIntent.putExtra("info_type", HuPuRes.REQ_METHOD_GET_COIN_INFO);
				startActivity(infoIntent);
			}
			break;


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

	public void onResume() {
		super.onResume();
		reqBalance();
	}
	void reqBalance() {
		if (mToken == null) {
			showBindDialog(SharedPreferencesMgr.getString("dialogBtnText",
					getString(R.string.bind_phone_dialog)));
		} else {
			initParameter();
			mParams.put("token", mToken);
			// mParams.put("pid", pid);
			sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE, mParams,
					new HupuHttpHandler(this), false);
		}
	}

	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();
		reqBalance();
		HupuLog.e("UserHupuDollorInfoActivity", "loginSuccess");
	}
	//
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	//	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE) {
			if (o != null) {
				HupuDollorBalanceReq info = (HupuDollorBalanceReq) o;


				txtBalance.setText(info.balance + "");
				findViewById(R.id.gold_icon).setVisibility(View.VISIBLE);
				goldBar.setVisibility(View.GONE);

				if (resumeNum == 0)
					checkToken(1);
				HupuLog.e("papa", "-----" + resumeNum);
				resumeNum++;
			}
		}
	}
}
