package com.hupu.games.hupudollor.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.hupu.games.R;
import com.hupu.games.activity.ContactsActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.JsonPaserFactory;
import com.hupu.games.data.RechargeMethodReq;
import com.hupu.games.data.WeixinPayReq;
import com.hupu.games.hupudollor.PayHupuDollorCallBack;
import com.hupu.games.hupudollor.data.HupuDollorBalanceReq;
import com.hupu.games.hupudollor.data.HupuDollorRechargeReq;
import com.hupu.games.hupudollor.data.OrderHupuDollorPacEntity;
import com.hupu.games.hupudollor.dialog.PayHupuDollorDialog;
import com.hupu.games.hupudollor.dialog.WalletPayHupuDollorDialog;
import com.hupu.games.pay.PhoneInputActivity;
import com.hupu.games.pay.Result;
import com.hupu.http.HupuHttpHandler;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author zhenhua 购买虎扑币
 */
public class HupuDollorOrderActivity extends HupuBaseActivity {

	TextView txtKanqiuMoney, txt_pay_contacts;
	private static final int RQF_PAY = 1;
	int money = 0;
	/** 由其他页面请求的充值金额 */
	//	private String tk;
	private String postType = "";
	private LayoutInflater inflater;
	private OrderHupuDollorPacEntity orderEntity;
	private int order = 0;
	//	private int recharge = 0;
	private int total = 0;
	private LinearLayout lay_pay;
	private IWXAPI api;

	WalletPayHupuDollorDialog wallDailog;
	String hupuDollor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hupudollor_order);
		//		tk = SharedPreferencesMgr.getString("tk", null);
		inflater = LayoutInflater.from(this);
		initView();
		reqPackages();
		hupuDollor = this.getIntent().getStringExtra("hupuDollor_balance");
	}

	void initView() {
		txtKanqiuMoney = (TextView) findViewById(R.id.txt_hupudollor);
		txt_pay_contacts = (TextView) findViewById(R.id.txt_pay_contacts);
		txt_pay_contacts.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		lay_pay = (LinearLayout) findViewById(R.id.lay_pay);
		Intent in = getIntent();
		txtKanqiuMoney.setText(String.format(
				this.getString(R.string.title_hupudollor_unit), "0"));
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.txt_pay_contacts);
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
	void switchToPhoneBindAct() {
		Intent intent = new Intent(this, PhoneInputActivity.class);
		startActivityForResult(intent, REQ_GO_BIND_PHONE);
	}
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_PACKAGES) {
			orderEntity = (OrderHupuDollorPacEntity) o;

			if(hupuDollor == null){//没有外部带入余额，则此处赋值
				txtKanqiuMoney.setText(String.format(
						this.getString(R.string.title_hupudollor_unit),
						orderEntity.hupuDollor_balance));
			}
			lay_pay.removeAllViews();
			if (orderEntity.list != null && orderEntity.list.size() > 0) {
				for (int i = 0; i < orderEntity.list.size(); i++) {
					OrderHupuDollorPacEntity entity = orderEntity.list.get(i);
					View view = this.getLayoutInflater().inflate(
							R.layout.item_hupudollor_pay, null);
					TextView txt_pay_title = (TextView) view
							.findViewById(R.id.txt_pay_title);
					Button btn_pay = (Button) view.findViewById(R.id.btn_pay);
					btn_pay.setText(getString(R.string.pay_currency_symbol)
							+ entity.recharge);
					txt_pay_title.setText(entity.title);
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					btn_pay.setTag(i);
					view.setTag(i);
					btn_pay.setOnClickListener(new PacClickListener());
					view.setOnClickListener(new PacClickListener());
					lay_pay.addView(view, param);
				}
			}
		}
		if (methodId == HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE) {
			HupuDollorBalanceReq req = (HupuDollorBalanceReq) o;
			if (req.balance == -1) {
				showToast("token无效，请重新登录");
			} else {
				// money=req.balance;
				txtKanqiuMoney.setText(String.format(
						this.getString(R.string.title_hupudollor_unit),
						Integer.toString(req.balance)));
			}
		}
		if (methodId == HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGE) {
			HupuDollorRechargeReq req = (HupuDollorRechargeReq) o;
			if (req.orderNo != null) {
				if (postType.equals("alipay_app")) {//支付宝快捷支付
					goSmsOrder(req.url);
				} else if (postType.equals("weixin")) {//微信支付
					WeixinPayReq weixinEntityPayReq = (WeixinPayReq) JsonPaserFactory
							.paserObj(req.url,HuPuRes.REQ_METHOD_GET_WEIXIN_RECHARGE);
					api = WXAPIFactory.createWXAPI(this,
							weixinEntityPayReq.appId, true);
					api.registerApp(weixinEntityPayReq.appId);
					if (api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT) {
						PayReq payReq = new PayReq();
						payReq.appId = weixinEntityPayReq.appId;
						payReq.partnerId = weixinEntityPayReq.partnerId;
						payReq.prepayId = weixinEntityPayReq.prepayId;
						payReq.nonceStr = weixinEntityPayReq.nonceStr;

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
				} else if (postType.equals("kanqiu_wallet_pay")) {//钱包支付
					if (req.url.equals("SUCCESS")) {
						//						showToast(getString(R.string.buy_success));
						//						reqPackages();
						onPaySuccess(HupuDollorOrderActivity.this, total + "");
					}
				}
			}
		}

	}
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.edt_bind_phone://?
			switchToPhoneBindAct();
			break;

		case R.id.btn_back:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.txt_pay_contacts://联系客服
			Intent intent = new Intent(this, ContactsActivity.class);
			startActivityForResult(intent, REQ_GO_BIND_PHONE);
			break;

		case R.id.btn_cancel://取消钱包支付
			if (wallDailog != null)
				wallDailog.dismiss();

			break;
		case R.id.btn_confirm://确认钱包支付
			if (wallDailog != null) {
				postType = "kanqiu_wallet_pay";
				OrderHupuDollorPacEntity entity = wallDailog.getWalletOrder();
				total = entity.total;
				initParameter();
				//				mParams.put("token", tk);
				mParams.put("type", postType);
				mParams.put("event", entity.id + "");
				mParams.put("charge ", entity.recharge + "");
				sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGE, mParams,
						new HupuHttpHandler(HupuDollorOrderActivity.this), true);
				wallDailog.dismiss();
			}

			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		switch (mApp.wxMsgCode) {
		case 0:
			onPaySuccess(HupuDollorOrderActivity.this, total + "");
			break;
		case -1:
			onPayFailure(HupuDollorOrderActivity.this, total + "");
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
					onPaySuccess(HupuDollorOrderActivity.this, total + "");
				} else {
					onPayFailure(HupuDollorOrderActivity.this, total + "");
				}
			} else if (resultCode == RESULT_CANCELED) {
				onPayFailure(HupuDollorOrderActivity.this, total + "");
			}
		}
	}

	void onPaySuccess(final Context mContext, String number) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("恭喜您");
		builder.setMessage(String.format(
				this.getString(R.string.title_order_hupudollor_success), number));

		builder.setNegativeButton(R.string.title_confirm,
				new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Intent in = new Intent();
				in.putExtra("success", 1);
				setResult(RESULT_OK, in);
				HupuDollorOrderActivity.this.finish();
			}
		});

		builder.create().show();
		//		reqPackages();//?
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
				//						finish();
			}

		});
		builder.create().show();
	}

	void goSmsOrder(final String order_id) {
		if (postType.equals("alipay_app")) {
			new Thread() {
				public void run() {
					String result = new AliPay(HupuDollorOrderActivity.this, mHandler)
					.pay(order_id);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		}
	}
	/** 请求套餐 */
	void reqPackages() {
		initParameter();
		//		mParams.put("token", tk);
		sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_PACKAGES, mParams,
				new HupuHttpHandler(this), true);
	}
	/** 支付 请求 得到订单号 */
	void potOrder(OrderHupuDollorPacEntity entity, String channel) {
		postType = channel;
		initParameter();
		//		mParams.put("token", tk);
		mParams.put("type", postType);
		mParams.put("event", entity.id + "");

		//		recharge = entity.recharge;
		total = entity.total;

		HupuLog.d("papa", "token=   " + mToken + "     type==" + postType
				+ "   event=" + entity.id);

		if (postType.equals("alipay_app") || postType.equals("weixin")) {
			mParams.put("charge", entity.recharge+"");
			sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGE, mParams,
					new HupuHttpHandler(HupuDollorOrderActivity.this), true);

		}
		//		 else if (postType.equals("weixin")) {
		//			mParams.put("charge", entity.recharge+"");
		//			sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGE, mParams,
		//					new HupuHttpHandler(HupuDollorOrderActivity.this), true);
		//		}
		//		else if (postType.equals("alipay_wap")) {
		//			Intent in = new Intent(this, HupuDollorPostOrderActivity.class);
		//			in.putExtra("token", tk);
		//			in.putExtra("type", "" + postType);
		//			in.putExtra("event", entity.id + "");
		//			 in.putExtra("charge", "" + entity.recharge + "");
		//			startActivityForResult(in, REQ_GO_POST_ORDER);
		//		}
		else {
			Intent in = new Intent(this, HupuDollorPostOrderActivity.class);
			in.putExtra("token", mToken);
			in.putExtra("type", "" + postType);
			in.putExtra("event", entity.id + "");
			in.putExtra("charge",  entity.recharge + "");
			startActivityForResult(in, REQ_GO_POST_ORDER);
		}
	}

	private class PacClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Integer key = (Integer) v.getTag();
			if (orderEntity.list != null && orderEntity.list.size() > 0
					&& orderEntity.list.size() > key) {

				if (orderEntity.list.get(key).channel.contains("kanqiu_wallet_pay")) {//钱包支付
					if (wallDailog == null)
						wallDailog = new WalletPayHupuDollorDialog(
								HupuDollorOrderActivity.this, click,
								orderEntity.list.get(key));
					else
						wallDailog.init(click, orderEntity.list.get(key));

					wallDailog.show();
				} else {//第三方支付
					PayHupuDollorDialog dialog = new PayHupuDollorDialog(HupuDollorOrderActivity.this,
							new ChannelListener(), orderEntity.list.get(key));
					dialog.show();
				}
			}
		}
	}

	// private String wayChannel;
	private class ChannelListener implements PayHupuDollorCallBack {
		@Override
		public void onPayListener(Dialog dialog, OrderHupuDollorPacEntity entity,
				String channel) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			potOrder(entity, channel);
			// wayChannel = channel;
		}

		@Override
		public void onRechargeListener(Dialog dialog, RechargeMethodReq entity,
				String channel) {
			// TODO Auto-generated method stub

		}
	}
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result.sResult = (String) msg.obj;
			switch (msg.what) {
			case RQF_PAY: {
				if (Result.getResult().equals("9000")) {
					onPaySuccess(HupuDollorOrderActivity.this, total + "");
				} else {
					onPayFailure(HupuDollorOrderActivity.this, total + "");
				}
			}
			break;
			default:
				break;
			}
		};
	};
}