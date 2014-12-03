package com.hupu.games.activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.hupu.games.R;
import com.hupu.games.casino.RechargeDialog;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.JsonPaserFactory;
import com.hupu.games.data.OrderPacEntity;
import com.hupu.games.data.RechargeMethodReq;
import com.hupu.games.data.RechargeReq;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.WeixinPayReq;
import com.hupu.games.data.account.UserWalletResp;
import com.hupu.games.pay.PayCallBack;
import com.hupu.games.pay.PostOrderActivity;
import com.hupu.games.pay.Result;
import com.hupu.http.HupuHttpHandler;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint("NewApi")
public class UserRechargeActivity extends HupuBaseActivity {

	/** 余额 */
	TextView txtBalance;
	Button btnSubmit;
	EditText rechargeEditText;
	private String balance = "0.00";
	private RechargeMethodReq rechargeMethod;
	private String postType = "";
	private long recharge = 0;
	int rechargeNum;
	private static final int RQF_PAY = 1;
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_wallet_pay);
		balance = getIntent().getStringExtra("balance");
		txtBalance = (TextView) findViewById(R.id.txt_coin_num);
		rechargeEditText = (EditText) findViewById(R.id.recharge_num);
		rechargeNum = getIntent().getIntExtra("recharge_num", 0);
		if (rechargeNum > 0) {
			rechargeEditText.setText(rechargeNum + "");
			rechargeEditText.setSelection(rechargeEditText.getText().toString()
					.length());
		}

		txtBalance.setText(balance != null ? balance : 0.00 + "元");
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		setOnClickListener(R.id.btn_back);
		btnSubmit.setOnClickListener(new PayClickListener());

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
		reqRechargeMethod();

		switch (mApp.wxMsgCode) {
		case 0:
			onPaySuccess(UserRechargeActivity.this, recharge + "");
			break;
		case -1:
			onPayFailure(UserRechargeActivity.this, recharge + "");
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
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_back:
			finish();
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
		if (requestCode == REQ_GO_POST_ORDER) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					onPaySuccess(UserRechargeActivity.this, recharge + "");
				} else {
					onPayFailure(UserRechargeActivity.this, recharge + "");
				}
			} else if (resultCode == RESULT_CANCELED) {
				onPayFailure(UserRechargeActivity.this, recharge + "");
			}
		}
	}

	/** 请求余额 */
	void reqBalance() {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			String sign = SSLKey.getSSLSign(mParams,
					SharedPreferencesMgr.getString("sugar", ""));// salt
																	// 值由init中的sugar给出。必须要有的。
			mParams.put("sign", sign);
			sendRequest(HuPuRes.REQ_METHOD_GET_WALLET_BALANCE, mParams,
					new HupuHttpHandler(this), false);
		}
	}

	/** 请求可用支付渠道 */
	void reqRechargeMethod() {
		if (mToken != null) {
			initParameter();
			String sign = SSLKey.getSSLSign(mParams,
					SharedPreferencesMgr.getString("sugar", ""));// salt
																	// 值由init中的sugar给出。必须要有的。
			mParams.put("sign", sign);
			sendRequest(HuPuRes.REQ_METHOD_GET_RECHARGEMETHOD, mParams,
					new HupuHttpHandler(this), true);
		}
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o != null) {
			switch (methodId) {
			case HuPuRes.REQ_METHOD_GET_WALLET_BALANCE:
				txtBalance.setText(((UserWalletResp) o).result + "元");
				break;
			case HuPuRes.REQ_METHOD_GET_CAIPIAO_RECHARGE:
				RechargeReq req = (RechargeReq) o;
				if (req.orderNo != null) {
					if (postType.equals("alipay_app")) {
						goSmsOrder(req.url);
					} else if (postType.equals("weixin")) {
						WeixinPayReq weixinEntityPayReq = (WeixinPayReq) JsonPaserFactory
								.paserObj(req.url,
										HuPuRes.REQ_METHOD_GET_WEIXIN_RECHARGE);
						api = WXAPIFactory.createWXAPI(this,
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
							HupuLog.d("papa", "wxPay=="
									+ weixinEntityPayReq.appId
									+ "---partnerid="
									+ weixinEntityPayReq.partnerId);
							api.sendReq(payReq);
						} else {
							showToast(getString(R.string.weixin_setup));
							Intent weixinIntent = new Intent();
							weixinIntent
									.setAction("android.intent.action.VIEW");
							Uri content_url = Uri
									.parse(getString(R.string.weixin_url));
							weixinIntent.setData(content_url);
							startActivity(weixinIntent);

						}
					}
				}
				break;
			case HuPuRes.REQ_METHOD_GET_RECHARGEMETHOD:
				rechargeMethod = (RechargeMethodReq) o;
				break;
			default:
				break;
			}
		}
	}

	void goSmsOrder(final String order_id) {
		if (postType.equals("alipay_app")) {
			new Thread() {
				public void run() {
					String result = new AliPay(UserRechargeActivity.this,
							mHandler).pay(order_id);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result.sResult = (String) msg.obj;
			switch (msg.what) {
			case RQF_PAY: {
				if (Result.getResult().equals("9000")) {
					onPaySuccess(UserRechargeActivity.this, recharge + "");
				} else {
					onPayFailure(UserRechargeActivity.this, recharge + "");
				}
			}
				break;
			default:
				break;
			}
		};
	};

	private class PayClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			recharge = rechargeEditText.getText().toString().equals("") ? 0
					: Long.parseLong(rechargeEditText.getText().toString());
			if (recharge > 0) {

				if (rechargeMethod != null
						&& rechargeMethod.channelList != null
						&& rechargeMethod.channelList.length > 0) {
					rechargeMethod.rechargeNum = recharge;
					RechargeDialog dialog = new RechargeDialog(
							UserRechargeActivity.this, new ChannelListener(),
							rechargeMethod);
					dialog.show();
				}

				if (rechargeNum > 0)
					sendUmeng(HuPuRes.UMENG_EVENT_LOTTERY,
							HuPuRes.UMENG_KEY_LOTTERY_BET,
							HuPuRes.UMENG_VALUE_NO_MONEY_ALERT_CHARGE_BTN);
				else
					sendUmeng(HuPuRes.UMENG_EVENT_MY_WALLET,
							HuPuRes.UMENG_KEY_CHARGE_BTN);

			} else {
				rechargeEditText.setText("");
				showToast(getString(R.string.recharge_not_zero));
			}
		}
	}

	private class ChannelListener implements PayCallBack {
		@Override
		public void onPayListener(Dialog dialog, OrderPacEntity entity,
				String channel) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onRechargeListener(Dialog dialog, RechargeMethodReq entity,
				String channel) {
			// TODO Auto-generated method stub

			dialog.dismiss();
			postOrder(entity, channel);

		}
	}

	/** 支付 请求 得到订单号 */
	void postOrder(RechargeMethodReq entity, String channel) {
		postType = channel;
		initParameter();
		mParams.put("token", mToken);
		mParams.put("type", postType);
		recharge = entity.rechargeNum;
		mParams.put("charge", recharge + "");

		// mParams.put("event", entity.id + "");
		// if (postType.equals("weipai")) {
		// sendRequest(HuPuRes.REQ_METHOD_GET_RECHARGE, mParams,
		// new HupuHttpHandler(HupuOrderActivity.this), true);
		// } else
		if (postType.equals("alipay_wap")) {
			Intent in = new Intent(this, PostOrderActivity.class);
			in.putExtra("method_id", HuPuRes.REQ_METHOD_GET_CAIPIAO_RECHARGE);
			in.putExtra("token", mToken);
			in.putExtra("type", "" + postType);
			in.putExtra("charge", recharge + "");
			startActivityForResult(in, REQ_GO_POST_ORDER);
		} else if (postType.equals("alipay_app")) {
			HupuLog.e("papa", "token=" + mToken + "-------type=" + postType
					+ "-----charg=" + recharge + "----postType" + postType);
			String sign = SSLKey.getSSLSign(mParams,
					SharedPreferencesMgr.getString("sugar", ""));// salt
																	// 值由init中的sugar给出。必须要有的。
			mParams.put("sign", sign);
			sendRequest(HuPuRes.REQ_METHOD_GET_CAIPIAO_RECHARGE, mParams,
					new HupuHttpHandler(UserRechargeActivity.this), true);
		} else if (postType.equals("weixin")) {
			sendRequest(HuPuRes.REQ_METHOD_GET_CAIPIAO_RECHARGE, mParams,
					new HupuHttpHandler(UserRechargeActivity.this), true);
		} else {
			Intent in = new Intent(this, PostOrderActivity.class);
			in.putExtra("method_id", HuPuRes.REQ_METHOD_GET_CAIPIAO_RECHARGE);
			in.putExtra("token", mToken);
			in.putExtra("type", "" + postType);
			in.putExtra("charge", recharge + "");
			startActivityForResult(in, REQ_GO_POST_ORDER);
		}
	}

	void onPaySuccess(final Context mContext, String number) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("恭喜您");
		builder.setMessage(this.getString(R.string.title_recharge_success));

		builder.setNegativeButton(R.string.title_confirm,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent in = new Intent();
						in.putExtra("success", 1);
						setResult(RESULT_OK, in);
						UserRechargeActivity.this.finish();
					}

				});

		builder.create().show();
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
