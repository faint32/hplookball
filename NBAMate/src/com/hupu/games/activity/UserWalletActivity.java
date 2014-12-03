package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BalanceReq;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.account.UserWalletResp;
import com.hupu.games.pay.HupuOrderActivity;
import com.hupu.http.HupuHttpHandler;

@SuppressLint("NewApi")
public class UserWalletActivity extends HupuBaseActivity {

	/** 余额 */
	TextView txtBalance, txt_copper, txt_silver, txt_gold,txtMemo;
	
	private String money = "0";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_wallet);
		txtBalance = (TextView) findViewById(R.id.txt_coin_num);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.layout_wallet_atm);
		setOnClickListener(R.id.layout_pay);
		setOnClickListener(R.id.layout_coin_info);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reqBalance();
	}

	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.layout_wallet_atm:  //提款
			sendUmeng(HuPuRes.UMENG_EVENT_MY_WALLET, HuPuRes.UMENG_KEY_WALLET_WITHDRAW);
			Intent atmIntent = new Intent(this, CoinInfoActivity.class);
			atmIntent.putExtra("info_type", HuPuRes.REQ_METHOD_GET_CAIPIAO_ATM);
			startActivity(atmIntent);
			break;
		case R.id.layout_coin_info:
			sendUmeng(HuPuRes.UMENG_EVENT_MY_WALLET, HuPuRes.UMENG_KEY_WALLET_ACCOUNT);
			Intent infoIntent = new Intent(this, CoinInfoActivity.class);
			infoIntent.putExtra("info_type", HuPuRes.REQ_METHOD_GET_CAIPIAO_COIN_INFO);
			startActivity(infoIntent);
			break;
		case R.id.layout_pay:    //充值
			sendUmeng(HuPuRes.UMENG_EVENT_MY_WALLET, HuPuRes.UMENG_KEY_WALLET_CHARGE);
			Intent order = new Intent(this, UserRechargeActivity.class);
			order.putExtra("balance", money);
			startActivity(order);
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




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_GO_BIND_PHONE) {
			
		}else if(requestCode == 3456 &&resultCode == RESULT_OK){
			//reqBalance();
		}
	}


	/** 请求余额 */
	void reqBalance() {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
			mParams.put("sign", sign);
			sendRequest(HuPuRes.REQ_METHOD_GET_WALLET_BALANCE, mParams,
					new HupuHttpHandler(this), false);
		}
	}
	
	
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o != null) {
			if (methodId == HuPuRes.REQ_METHOD_GET_WALLET_BALANCE) {
				money = ((UserWalletResp) o).result;
				txtBalance.setText(money+"元");
			}
		}
	}

}
