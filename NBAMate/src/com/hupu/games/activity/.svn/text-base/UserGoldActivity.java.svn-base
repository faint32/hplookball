package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.casino.ShakeBoxActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BalanceReq;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.UserBoxEntity;
import com.hupu.games.data.personal.box.BoxBalanceEntity;
import com.hupu.games.pay.HupuOrderActivity;
import com.hupu.http.HupuHttpHandler;

@SuppressLint("NewApi")
public class UserGoldActivity extends HupuBaseActivity {

	/** 余额 */
	TextView txtBalance, txt_copper, txt_silver, txt_gold,txtMemo;
	
	String CoinNum = "0";
	private int money = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_usergold);
		txtBalance = (TextView) findViewById(R.id.txt_coin_num);
		txt_copper = (TextView) findViewById(R.id.txt_copper);
		txt_silver = (TextView) findViewById(R.id.txt_silver);
		txt_gold = (TextView) findViewById(R.id.txt_gold);
		txtMemo= (TextView) findViewById(R.id.txt_box_memo);
//		txt_copper.setText(String.format(this.getString(R.string.copper_mount, 0)));
//		txt_silver.setText(String.format(this.getString(R.string.silver_mount, 0)));
//		txt_gold.setText(String.format(this.getString(R.string.gold_mount, 0)));
//		reqMyBox();
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.lay_copper);
		setOnClickListener(R.id.lay_gold);
		setOnClickListener(R.id.lay_silver);
		setOnClickListener(R.id.layout_pay);
		setOnClickListener(R.id.layout_coin_info);
		
		if (SharedPreferencesMgr.getInt("show_mall", 0) == 0)
			findViewById(R.id.box_layout).setVisibility(View.GONE);
		else
			findViewById(R.id.box_layout).setVisibility(View.VISIBLE);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.lay_copper:
			sendUmeng(HuPuRes.UMENG_EVENT_MY_BOX,HuPuRes.UMENG_KEY_BRONZEBOX);
			if(entity!=null)
				startBox(2,copperNum,entity.memoCorpper);
			break;
		case R.id.lay_gold:
			sendUmeng(HuPuRes.UMENG_EVENT_MY_BOX, HuPuRes.UMENG_KEY_GOLDBOX);
			if(entity!=null)
				startBox(0,goldNum,entity.memoGold);
			break;
		case R.id.lay_silver:
			sendUmeng(HuPuRes.UMENG_EVENT_MY_BOX, HuPuRes.UMENG_KEY_SILVERBOX);
			if(entity!=null)
				startBox(1,sliperNum,entity.memoSliver);
			break;
		case R.id.layout_coin_info:
			Intent infoIntent = new Intent(this, CoinInfoActivity.class);
			infoIntent.putExtra("info_type", HuPuRes.REQ_METHOD_GET_COIN_INFO);
			startActivity(infoIntent);
			break;
		case R.id.layout_pay:
			
			Intent order = new Intent(this, HupuOrderActivity.class);
			order.putExtra("balance", money);
			startActivityForResult(order, REQ_GO_CHARGE);
			break;
		}
	}

	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reqBalance();
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
			reqBalance();
		}
	}

	/** 我的宝箱 */
	void reqMyBox() {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			sendRequest(HuPuRes.REQ_METHOD_GET_USERBOX, mParams,
					new HupuHttpHandler(this), false);
		}
	}

	/** 请求余额 */
	void reqBalance() {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
			mParams.put("sign", sign);
			sendRequest(HuPuRes.REQ_METHOD_GET_BOX_BALANCE, mParams,
					new HupuHttpHandler(this), false);
		}
	}
	
	void startBox(int type,int num,String memo){
		Intent in =new Intent(this,ShakeBoxActivity.class);
		in.putExtra("type", type);
		in.putExtra("num", num);
		if(memo!=null)
			in.putExtra("memo", memo);
		startActivityForResult(in, 3456);
	}
	
	int copperNum;
	int sliperNum;
	int goldNum;
	BoxBalanceEntity entity;
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_GET_BALANCE) {
			int money = ((BalanceReq) o).balance;
			txtBalance.setText(money + "");
		}else if(methodId == HuPuRes.REQ_METHOD_GET_USERBOX){
			UserBoxEntity entity = (UserBoxEntity)o;
			if(entity.copper>0)
				txt_copper.setText(Html.fromHtml(getString(R.string.copper_mount_red,"<font color='#ff0000'>"+entity.copper+"</font>")));
			else
				txt_copper.setText(getString(R.string.copper_mount, entity.copper));
			
			if(entity.sliper>0)
				txt_silver.setText(Html.fromHtml(getString(R.string.silver_mount_red, "<font color='#ff0000'>"+entity.sliper+"</font>")));
			else
				txt_silver.setText(getString(R.string.silver_mount, entity.sliper));
			if(entity.gold>0)
				txt_gold.setText(Html.fromHtml(getString(R.string.gold_mount_red, "<font color='#ff0000'>"+entity.gold+"</font>")));
			else
				txt_gold.setText(getString(R.string.gold_mount, entity.gold));
			copperNum = entity.copper;
			sliperNum = entity.sliper;
			goldNum =entity.gold;
		}
		else if(HuPuRes.REQ_METHOD_GET_BOX_BALANCE == methodId)
		{
			entity =(BoxBalanceEntity)o;
			if(entity.countCorpper>0)
				txt_copper.setText(Html.fromHtml(getString(R.string.copper_mount_red,"<font color='#ff0000'>"+entity.countCorpper+"</font>")));
			else
				txt_copper.setText(getString(R.string.copper_mount, entity.countCorpper));
			
			if(entity.countSliver>0)
				txt_silver.setText(Html.fromHtml(getString(R.string.silver_mount_red, "<font color='#ff0000'>"+entity.countSliver+"</font>")));
			else
				txt_silver.setText(getString(R.string.silver_mount, entity.countSliver));
			if(entity.countGold>0)
				txt_gold.setText(Html.fromHtml(getString(R.string.gold_mount_red, "<font color='#ff0000'>"+entity.countGold+"</font>")));
			else
				txt_gold.setText(getString(R.string.gold_mount, entity.countGold));
			
			copperNum = entity.countCorpper;
			sliperNum = entity.countSliver;
			goldNum =entity.countGold;
			txtBalance.setText(entity.balance + "");
			txtMemo.setText(entity.boxMemo);
			money = entity.balance;
			
			
			if (entity.balance == 0) {
				CoinNum ="0";
			}else if (entity.balance > 0 && entity.balance <10) {
				CoinNum ="<10";
			} else if (entity.balance >= 10 && entity.balance <100) {
				CoinNum ="10-100";
			} else if (entity.balance >= 100 && entity.balance <1000) {
				CoinNum ="100-1000";
			}else if (entity.balance >= 1000) {
				CoinNum =">1000";
			}
		}
	}

}
