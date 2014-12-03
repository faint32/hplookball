package com.hupu.games.pay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.goldbean.GoldBeanWebViewActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BalanceReq;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.BitCoinReq;
import com.hupu.games.data.CheckBag;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.goldbean.GoldBeanManager;
import com.hupu.games.data.goldbean.GoldDataService;
import com.hupu.http.HupuHttpHandler;
import com.pyj.activity.BaseActivity;
import com.pyj.common.DeviceInfo;

public class BasePayActivity extends BaseGameActivity {

	public final static int DIALOG_NO_MONEY = 6682;

	private int reqCharge;
	private int reqNum;
	private int pid;

	public void showNoMoney(int p, int n) {
		pid = p;
		reqNum = n;
		showCustomDialog(DIALOG_NO_MONEY, R.string.title_charge,
				R.string.no_charge, TOW_BUTTONS, R.string.buy_at_once,
				R.string.buy_later);
	}

	public void payError() {
		showToast("您的购买出现异常，请重新提交。");
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {
		case DIALOG_NO_MONEY:
			// 去冲钱
			switchToPay(pid, reqNum, REQ_GO_CHARGE);
			reqNum = 0;
			pid = 0;
			break;
		case DIALOG_SHOW_BIND_PHONE:
			switchToPhoneBindAct();
			break;
		case DIALOG_SHOW_CHARGE_NOTIFY:
			
			switchToCharge();
			break;
		}
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
		switch (dialogId) {
		case DIALOG_NO_MONEY:
			reqNum = 0;

			break;
		case DIALOG_SHOW_BIND_PHONE:
			break;
		case DIALOG_EXCHANGE_PRIZE:
			break;
		case DIALOG_SHOW_CHARGE_NOTIFY:
			break;
		}
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_GO_CHARGE) {
			if (resultCode == RESULT_OK) {
				if (data != null)
				{
					onChargeSuccess(data.getIntExtra("success", 1) == 1 ? true
						: false);
				}
			}

		}
		if (requestCode == REQ_GO_BIND_PHONE) {
			if (resultCode == RESULT_OK) {
				onBindSuccess();
			}
		}
		if (requestCode == REQ_GO_POST_ORDER) {
			if(data!=null)
			{
				channelPayCallback(data);
			}
		}
	}
	
	public void channelPayCallback(Intent data)
	{
		
		int type = data.getIntExtra(GoldBeanWebViewActivity.TYPE_PATH, 0);
		
		if(type!=GoldDataService.DATA_CHARGE_PATH_QUZI)
		{
			return;
		}
		
		boolean isSuccess  = data.getIntExtra("success", 0) == 1 ? true
				: false;
		if(isSuccess)
		{
//			this.showToast(getString(R.string.hupudollar_quzitip_success));
			GoldBeanManager.getInstance().quziDollarSuccessTips(this,GoldBeanManager.quzi_gold_num,GoldBeanManager.quzi_goldbean_num,GoldBeanManager.status);
		}
        else 
		{
			this.showToast(getString(R.string.hupudollar_tip_failure));
		}
		if(GoldBeanManager.status.equals("2")||GoldBeanManager.status.equals("1"))
		{
			if(this.curFragmentIndex == BaseGameActivity.INDEX_LIVE)
			{
				if(this.mFragmentLive!=null)
				{
					isExchange = true;	
					this.mFragmentLive.sendQuizCommit();
				}
			}
			else if(this.curFragmentIndex == BaseGameActivity.INDEX_QUIZ)
			{
				if(this.mFragmentQuizList!=null)
				{
					isExchange = true;	
					this.mFragmentQuizList.sendQuizCommit();
				}
			}
		}
		else
		{

		}
	}

	/** 充值成功 */
	public void onChargeSuccess(boolean success) {

	}

	public void onBindSuccess() {

	}

	// 1 有权限，-1 未登陆，-2 无权限，-3无权限且余额不够购买
	public void onCheckFinish(int state) {

	}

	/** 检查背包 */
	public void bagVerify(int pid, boolean showDialog) {
		String tk = SharedPreferencesMgr.getString("tk", null);
		if (tk == null) {
			// showCustomDialog(DIALOG_BUY_VIP_CHARG);
		} else {
			initParameter();
			mParams.put("pid", "" + pid);
			mParams.put("token", tk);
			// Log.d("bagVerify", "token="+tk +" pid="+pid);
			String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
			mParams.put("sign", sign);
			sendRequest(HuPuRes.REQ_METHOD_CHECK_BAG, mParams,
					new HupuHttpHandler(this), showDialog);
		}
	}

	public int money;
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_CHECK_BAG) {
			CheckBag check = (CheckBag) o;
			onCheckFinish(check.result);
		}
		if (methodId ==HuPuRes.REQ_METHOD_BET_COINS)
		{
			BitCoinReq req =(BitCoinReq)o;
			if(req.balance ==-1)
			{
				showToast("token无效，请重新登录");
			}
			else
			{
				money=req.balance;			
				updateMoney(req.betCoins,req.balance);
			}
		}
		else if (methodId == HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS||methodId==HuPuRes.REQ_METHOD_GET_GOLD) {
			GoldBeanManager.getInstance().recharge(this, o);
		}
	}

	public void updateMoney(int betCoin,int balance)
	{
		
	}


	public void switchToPay(int pid, int num, int reqCode) {
		Intent in = new Intent(this, HupuOrderActivity.class);
		in.putExtra("pid", "" + pid);
		in.putExtra("number", "" + num);
		startActivityForResult(in, reqCode);
	}

	/**请求余额*/
	public void reqBitCoin(int qid)
	{		
		if(mToken != null)
		{
			initParameter();
			mParams.put("token", mToken);
			mParams.put("qid", ""+qid);
			sendRequest(HuPuRes.REQ_METHOD_BET_COINS, mParams, new HupuHttpHandler(
					this), false);
		}
		
	}
	

	/**
	 * 跳转到绑定手机
	 * */
	public void switchToPhoneBindAct() {
		Intent intent = new Intent(this, PhoneInputActivity.class);
		startActivityForResult(intent, HupuBaseActivity.REQ_GO_BIND_PHONE);
	}
	
	void switchToCharge()
	{
		Intent order = new Intent(this, HupuOrderActivity.class);
		startActivityForResult(order, REQ_GO_CHARGE);
	}
	
	
}
