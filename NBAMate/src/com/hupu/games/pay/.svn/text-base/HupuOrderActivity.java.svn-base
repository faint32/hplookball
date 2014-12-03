/**
 * 
 */
package com.hupu.games.pay;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.hupu.games.R;
import com.hupu.games.activity.ContactsActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.goldbean.GoldBeanWebViewActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.JsonPaserFactory;
import com.hupu.games.data.OrderPacEntity;
import com.hupu.games.data.RechargeMethodReq;
import com.hupu.games.data.WeixinPayReq;
import com.hupu.games.data.goldbean.ExchangeGoldBeanEntity;
import com.hupu.games.data.goldbean.GoldBeanManager;
import com.hupu.games.data.goldbean.GoldDallorRechargeEntity;
import com.hupu.games.data.goldbean.GoldEntity;
import com.hupu.games.dialog.GoldBeanDollorCallBack;
import com.hupu.games.dialog.GoldBeanPayDialog;
import com.hupu.games.dialog.TipsDialog;
import com.hupu.games.hupudollor.activity.HupuDollorOrderActivity;
import com.hupu.http.HupuHttpHandler;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author papa 虎扑币兑换金豆
 */
public class HupuOrderActivity extends HupuBaseActivity {

	protected static final int RQF_PAY = 1;
	private TextView current_gold;
	private EditText exchange_goldbean;

	/** 由其他页面请求的充值金额 */
	private String tk;

	private GoldEntity orderEntity;
	
	private TipsDialog tipsDialog;
	
	private int goldnum;
	
	public int goldbean_num;  // 竞猜金豆数
	
	private String status;

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RQF_PAY: {
				Result.sResult = (String) msg.obj;
				if (Result.getResult().equals("9000")) {
					onPaySuccess(HupuOrderActivity.this, goldnum + "");
				} else {
					onPayFailure(HupuOrderActivity.this, goldnum + "");
				}

			}
			default:
				break;
			}
		}
	};
	private String postType;
	private int total;
	private int recharge;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_order_new);
		tk = SharedPreferencesMgr.getString("tk", null);

		initView();

	}

	private void initView() {
		current_gold = (TextView) findViewById(R.id.txt_kanqiu_money);
		exchange_goldbean = (EditText) findViewById(R.id.txt_wallet);

		setOnClickListener(R.id.btn_exchange);
		setOnClickListener(R.id.btn_back);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			setResult(RESULT_OK);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_GET_GOLD) {
			orderEntity = (GoldEntity) o;

			current_gold.setText(orderEntity.gold_num);
		}
		if (methodId == HuPuRes.REQ_METHOD_GET_EXCHANGEBEANS) {
			ExchangeGoldBeanEntity goldBeanEntity = (ExchangeGoldBeanEntity) o;
			if(goldBeanEntity!=null)
			{
				if (!goldBeanEntity.isLogin.equals("1")) {
					showToast("token无效，请重新登录");
				} else {
					try {
						status = goldBeanEntity.status;
						goldbean_num = (goldBeanEntity.eventDataBean!=null?Integer.parseInt(goldBeanEntity.eventDataBean.total):0) + Integer.parseInt(current_gold.getText().toString()) - goldnum;
					} catch (Exception e) {
						e.printStackTrace();
					}
					reqPay(goldBeanEntity);
				}
			}
			else
			{
				showToast(getString(R.string.hupudollar_tip_failure));
			}
		
		}
		if (methodId == HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS) {
			GoldDallorRechargeEntity goldBeanEntity = (GoldDallorRechargeEntity) o;
			if (goldBeanEntity.url.equals("SUCCESS")) {
//				showToast(getString(R.string.hupudollar_tip_success));
				GoldBeanManager.getInstance().quziDollarSuccessTips(this, goldnum,goldbean_num,status);
				exchange_goldbean.setText("");
				reqCurrentGold();
			} else {

				if (goldBeanEntity.orderNo != null) {
					if (postType.equals("alipay_app")) {
						goSmsOrder(goldBeanEntity.url);
					} else if (postType.equals("weixin")) {
						WeixinPayReq weixinEntityPayReq = (WeixinPayReq) JsonPaserFactory
								.paserObj(goldBeanEntity.url,
										HuPuRes.REQ_METHOD_GET_WEIXIN_RECHARGE);
						IWXAPI api = WXAPIFactory.createWXAPI(this,
								weixinEntityPayReq.appId, true);
						api.registerApp(weixinEntityPayReq.appId);
						if (api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT) {
							PayReq payReq = new PayReq();
							payReq.appId = weixinEntityPayReq.appId;
							payReq.partnerId = weixinEntityPayReq.partnerId;
							payReq.prepayId = weixinEntityPayReq.prepayId;
							payReq.nonceStr = weixinEntityPayReq.nonceStr;
							;
							payReq.timeStamp = weixinEntityPayReq.timeStamp;
							payReq.packageValue = weixinEntityPayReq.packageValue;
							payReq.sign = weixinEntityPayReq.sign;
							HupuLog.d("papa", "wxPay==" + weixinEntityPayReq.appId
									+ "---partnerid="
									+ weixinEntityPayReq.partnerId);
							api.sendReq(payReq);
						} else {
							showToast(getString(R.string.weixin_setup));
							Intent weixinIntent = new Intent();
							weixinIntent.setAction("android.intent.action.VIEW");
							Uri content_url = Uri
									.parse(getString(R.string.weixin_url));
							weixinIntent.setData(content_url);
							startActivity(weixinIntent);

						}
					} 
					else if (postType.equals("kanqiu_wallet_pay")) {
						if (goldBeanEntity.url.equals("SUCCESS")) {
							showToast(getString(R.string.buy_success));
							reqPackages();
						}
					}
					else
					{
						this.showToast(getString(R.string.hupudollar_tip_failure));
					}
				}
				
			}
		}

	}

	/** 请求套餐 */
	void reqPackages() {
		initParameter();
		mParams.put("token", tk);
		sendRequest(HuPuRes.REQ_METHOD_GET_PACKAGES, mParams,
				new HupuHttpHandler(this), true);
	}
	private void switchToPhoneBindAct() {
		Intent intent = new Intent(this, PhoneInputActivity.class);
		startActivityForResult(intent, REQ_GO_BIND_PHONE);
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.edt_bind_phone:
			switchToPhoneBindAct();
			break;

		case R.id.btn_back:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.btn_exchange:
			goldnum = parseStringtoInt(exchange_goldbean.getText().toString());
			if(goldnum>0)
			{
				reqexchangeBeans(goldnum+"");
			}
	
			break;
		case R.id.btn_cancel:
		

			break;
		case R.id.btn_confirm:


		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reqCurrentGold();
		switch (mApp.wxMsgCode) {
		case 0:
			onPaySuccess(HupuOrderActivity.this, goldnum + "");
			break;
		case -1:
			onPayFailure(HupuOrderActivity.this, goldnum + "");
			break;
		case -2:
			showToast(getString(R.string.cancel_pay));
			break;

		default:
			break;
		}
		mApp.wxMsgCode = 213;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		HupuLog.e("papa", "resultId==" + resultCode);
		
		if (requestCode == REQ_GO_POST_ORDER) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					onPaySuccess(HupuOrderActivity.this, total + "");
				} else {
					onPayFailure(HupuOrderActivity.this, total + "");
				}
			} else if (resultCode == RESULT_CANCELED) {
				onPayFailure(HupuOrderActivity.this, total + "");
			}
		}
		
		if(exchange_goldbean!=null)
		{
			exchange_goldbean.setText("");
		}
	}

	/**
	 * TODO  虎扑钱包  购买虎扑币
	 *
	 *
	 * @param goldBeanEntity
	 * @return void
	 */
	private void onExchangeGoldBeanDiaog(final ExchangeGoldBeanEntity goldBeanEntity)
	{	
		tipsDialog = new TipsDialog(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubW
				if(v.getId()==TipsDialog.BTN_OK_ID)
				{
					if(goldBeanEntity.channel.size()>0)
					{
						postType = goldBeanEntity.channel.get(0);
					}
					else
					{
						postType = "hupu_dollar_pay";
					}

					String event_id = goldBeanEntity.eventDataBean==null?"":goldBeanEntity.eventDataBean.id;
					initParameter();
					mParams.put("token", tk);
					mParams.put("type", postType);
					//						mParams.put("charge", goldBeanEntity.rechargeAmount);
					mParams.put("event", event_id + "");
					mParams.put("exchangeAmount", goldnum + "");
					sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS, mParams,
							new HupuHttpHandler(HupuOrderActivity.this), true);
					tipsDialog.dismiss();
				}
				else
				{
					tipsDialog.dismiss();
				}
				tipsDialog.dismiss();
			}
		},goldBeanEntity.content, TipsDialog.DEFAULT);
		tipsDialog.initData(goldBeanEntity.content, TipsDialog.DEFAULT);
		tipsDialog.initBtn(getString(R.string.dialog_ecgoldbtn_name), getString(R.string.cancel));
		tipsDialog.show();
	}

	/**
	 * TODO  请求金币
	 *
	 *
	 * @return void
	 */
	private void reqCurrentGold()
	{
		initParameter();
		mParams.put("token", tk);
		sendRequest(HuPuRes.REQ_METHOD_GET_GOLD, mParams,
				new HupuHttpHandler(this), true);
	}
	
	private void reqexchangeBeans(String amount)
	{
		initParameter();
		mParams.put("token", tk);
		mParams.put("amount", amount);
		
		sendRequest(HuPuRes.REQ_METHOD_GET_EXCHANGEBEANS, mParams,
				new HupuHttpHandler(this), true);
	}
	
	/**
	 * TODO 虎扑 币不足 请求购买虎扑币 
	 *
	 *
	 * @param goldBeanEntity
	 * @return void
	 */
	private void reqPay(ExchangeGoldBeanEntity goldBeanEntity)
	{
		
		if(goldBeanEntity==null)
		{
			return;
		}
		if(goldBeanEntity.status.equals("1"))
		{
			onExchangeGoldBeanDiaog(goldBeanEntity);
		}
		else if(goldBeanEntity.status.equals("2"))
		{
			onExchangeGoldBeanDiaog(goldBeanEntity);
		}
		else if(goldBeanEntity.status.equals("3"))
		{
			GoldBeanPayDialog dialog = new GoldBeanPayDialog(HupuOrderActivity.this,
					new ChannelListener(), goldBeanEntity);
			dialog.initdata(goldBeanEntity.channel, goldBeanEntity.content);
			dialog.show();
		
		}
		else 
		{
			Intent order = new Intent(this, HupuDollorOrderActivity.class);
			startActivity(order);
		}
	}
	
	private class ChannelListener implements GoldBeanDollorCallBack {

		@Override
		public void onPayListener(Dialog dialog, BaseEntity entity,
				String channel) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			ExchangeGoldBeanEntity  goldBeanEntity = (ExchangeGoldBeanEntity) entity;
			reqChannelPay(goldBeanEntity,channel);
		}

		@Override
		public void onRechargeListener(Dialog dialog, RechargeMethodReq entity,
				String channel) {
			// TODO Auto-generated method stub
			
		}

	}

	/**
	 * TODO  第三方支付 购买虎扑币
	 *
	 *
	 * @param goldBeanEntity
	 * @return void
	 */
	private synchronized void reqChannelPay(ExchangeGoldBeanEntity goldBeanEntity,String channel)
	{

		postType = channel;
		initParameter();
		mParams.put("token", tk);
		mParams.put("type", postType);

		String eventid = goldBeanEntity.eventDataBean==null?"":goldBeanEntity.eventDataBean.id;
		HupuLog.d("papa", "token=   " + tk + "     type==" + postType
				+ "   event=" + eventid);
		mParams.put("event", eventid + "");
		mParams.put("exchangeAmount", goldnum + "");
		
		if (postType.equals("alipay_wap")) {
			Intent in = new Intent(this, GoldBeanWebViewActivity.class);
			in.putExtra(GoldBeanWebViewActivity.EXTRA_INTEN_METHODID, HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS);
			in.putExtra("token", tk);
			in.putExtra("type", "" + postType);
			in.putExtra("event", eventid + "");
			in.putExtra("exchangeAmount", goldnum+"");
			startActivityForResult(in, REQ_GO_POST_ORDER);
		} else if (postType.equals("alipay_app")) {
			sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS, mParams,
					new HupuHttpHandler(HupuOrderActivity.this), true);

		} else if (postType.equals("weixin")) {
			sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS, mParams,
					new HupuHttpHandler(HupuOrderActivity.this), true);
		} else {
			Intent in = new Intent(this, GoldBeanWebViewActivity.class);
			in.putExtra(GoldBeanWebViewActivity.EXTRA_INTEN_METHODID, HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS);
			in.putExtra("token", tk);
			in.putExtra("type", "" + postType);
			in.putExtra("event", eventid + "");
			in.putExtra("exchangeAmount", goldnum+"");
			startActivityForResult(in, REQ_GO_POST_ORDER);
		}
	}
	
	private int parseStringtoInt(String source)
	{
		int outdata = 0;
		
		if(source==null||source.equals(""))
		{
			return -1;
		}
		
		try {
			outdata = Integer.parseInt(source);
		} catch (Exception e) {
			e.printStackTrace();
			outdata = -1;
		}
		
		return outdata;
	}
	
	void goSmsOrder(final String order_id) {
		if (postType.equals("alipay_app")) {
			new Thread() {
				public void run() {
					String result = new AliPay(HupuOrderActivity.this, mHandler)
							.pay(order_id);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		}
	}
	void onPaySuccess(final Context mContext, String number) {
//		AlertDialog.Builder builder = new Builder(this);
//		builder.setTitle("恭喜您");
//		builder.setMessage(String.format(
//				this.getString(R.string.title_pay_success), number));
//
//		builder.setNegativeButton(R.string.title_confirm,
//				new android.content.DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//						Intent in = new Intent();
//						in.putExtra("success", 1);
//						setResult(RESULT_OK, in);
//						HupuOrderActivity.this.finish();
//					}
//
//				});
//
//		builder.create().show();
		GoldBeanManager.getInstance().quziDollarSuccessTips(this, goldnum,goldbean_num,status);

	}

	void onPayFailure(final Context mContext, String number) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(R.string.title_pay_failure);
		builder.setPositiveButton(R.string.title_pay_failure_left,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dialog.dismiss();
					}

				});
		builder.setNegativeButton(R.string.title_pay_failure_right,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dialog.dismiss();
						Intent intent = new Intent(mContext,
								ContactsActivity.class);
						startActivityForResult(intent, REQ_GO_BIND_PHONE);
						finish();
					}

				});
		builder.create().show();
	}
	
}