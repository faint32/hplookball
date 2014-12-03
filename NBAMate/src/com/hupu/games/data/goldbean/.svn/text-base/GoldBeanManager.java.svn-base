package com.hupu.games.data.goldbean;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.alipay.android.app.sdk.AliPay;
import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.goldbean.GoldBeanWebViewActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseEntity;
import com.hupu.games.data.JsonPaserFactory;
import com.hupu.games.data.RechargeMethodReq;
import com.hupu.games.data.WeixinPayReq;
import com.hupu.games.dialog.GoldBeanDollorCallBack;
import com.hupu.games.dialog.GoldBeanPayDialog;
import com.hupu.games.dialog.TipsDialog;
import com.hupu.games.hupudollor.activity.HupuDollorOrderActivity;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.pay.Result;
import com.hupu.http.HupuHttpHandler;
import com.pyj.activity.BaseActivity;
import com.pyj.http.RequestParams;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author wangjianjun
 * Nov 6, 2014 1:21:39 PM
 *
 * TODO
 */
public class GoldBeanManager {
	
	
	protected static final int RQF_PAY = 1;
	private static GoldBeanManager gBeanManager ;
	
	private Context context;
	
	public static String status = "1";
	
	public static int quzi_gold_num;  // 竞猜金豆数
	
	public static int quzi_goldbean_num; // 剩余虎扑币数量
	
	private GoldBeanManager()
	{
		
	}
	
	public synchronized static GoldBeanManager getInstance()
	{
		if(gBeanManager==null)
		{
			gBeanManager = new GoldBeanManager();
		}
		
		return gBeanManager;
	}
	
	/**
	 * TODO  虎扑币兑换金豆请求
	 *
	 *
	 * @param baseActivity
	 * @param mParams
	 * @param amount
	 * @return void
	 */
	public synchronized void reqexchangeBeans(HupuBaseActivity baseActivity,RequestParams mParams,String amount)
	{
		baseActivity.initParameter();
		mParams.put("token", getToken());
		mParams.put("amount", amount);
		
		baseActivity.sendRequest(HuPuRes.REQ_METHOD_GET_EXCHANGEBEANS, mParams,
				new HupuHttpHandler(baseActivity), true);
	}
	
	/**
	 * TODO  钱包购买虎扑币
	 *
	 *
	 * @param baseActivity
	 * @param mParams
	 * @param goldBeanEntity
	 * @return void
	 */
	public synchronized void reqPayHupuWallet(BaseGameActivity baseActivity,ExchangeGoldBeanEntity goldBeanEntity)
	{
		if(goldBeanEntity==null||null==baseActivity)
		{
			return;
		}
		
		String postType;
		if(goldBeanEntity.channel.size()>0)
		{
			postType = goldBeanEntity.channel.get(0);
		}
		else
		{
			postType = "hupu_dollar_pay";
		}

		String event_id = goldBeanEntity.eventDataBean==null?"":goldBeanEntity.eventDataBean.id;
		total = goldBeanEntity.coin;
		RequestParams mParams = baseActivity.initParameter();
		mParams.put("token", getToken());
		mParams.put("type", postType);
		mParams.put("event", event_id + "");
		mParams.put("exchangeAmount", goldBeanEntity.coin + "");
		baseActivity.sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS, mParams,
				new HupuHttpHandler(baseActivity), true);
	}
	
	/**
	 * TODO  虎扑钱包  购买虎扑币
	 *
	 *
	 * @param goldBeanEntity
	 * @return void
	 */
	public void onExchangeGoldBeanDiaog(final BasePayActivity baseActivity,final ExchangeGoldBeanEntity goldBeanEntity)
	{	
		if(baseActivity==null||goldBeanEntity==null)
		{
			return;
		}
//		reqCurrentGold(baseActivity);
		status = goldBeanEntity.status;
		quzi_gold_num = Integer.parseInt(goldBeanEntity.coin);
		try {
			quzi_goldbean_num = Integer.parseInt(goldBeanEntity.eventDataBean.total) - quzi_gold_num ;
		} catch (Exception e) {
			e.printStackTrace();
		}

		String btnString = "";
		if(status.equals("2"))
		{
			btnString = baseActivity.getString(R.string.dialog_ecgoldbtn_name);
		}
		else if(status.equals("3"))
		{
			btnString = baseActivity.getString(R.string.dialog_ecgoldbtn_quziname);
		}
		else if(status.equals("4"))
		{
			btnString = baseActivity.getString(R.string.dialog_ecgoldbtn_quziname);
		}
		else
		{
			btnString = baseActivity.getString(R.string.dialog_ecgoldbtn_name);
		}
		TipsDialog tipsDialog = new TipsDialog(baseActivity);
		tipsDialog.initContentView(new GoldBeanClick(tipsDialog,baseActivity,goldBeanEntity),goldBeanEntity.content, TipsDialog.DEFAULT);
		tipsDialog.initBtn(btnString, baseActivity.getString(R.string.cancel));
		tipsDialog.show();
	}
	
	private String getToken()
	{
		return SharedPreferencesMgr.getString("tk", null);
	}
	
	public class GoldBeanClick implements OnClickListener {

		TipsDialog tipsDialog;
		
		ExchangeGoldBeanEntity goldBeanEntity;
		BasePayActivity baseActivity;

		public GoldBeanClick(TipsDialog tipsDialog,BasePayActivity baseActivity,ExchangeGoldBeanEntity goldBeanEntity)
		{
			this.tipsDialog = tipsDialog;
			this.goldBeanEntity = goldBeanEntity;
			this.baseActivity = baseActivity;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(goldBeanEntity==null||baseActivity==null)
			{
				if(tipsDialog!=null)
				{
					tipsDialog.dismiss();
				}
				return;
			}
			if(v.getId()==TipsDialog.BTN_OK_ID)
			{
				if(goldBeanEntity.status.equals("2"))
				{
					reqPayHupuWallet(baseActivity, goldBeanEntity);
				}
				else if(goldBeanEntity.status.equals("3"))
				{
					showChannelPay(baseActivity, goldBeanEntity);
				}
				else if(goldBeanEntity.status.equals("4"))
				{
					Intent order = new Intent(baseActivity, HupuDollorOrderActivity.class);
					baseActivity.startActivity(order);
				}
				else
				{
					reqPayHupuWallet(baseActivity, goldBeanEntity);
				}
			}
			else
			{

			}
			if(tipsDialog!=null)
			{
				tipsDialog.dismiss();
			}
	
		}
		
	}
	
	/**
	 * TODO 展示第三方支付界面
	 *
	 *
	 * @param baseActivity
	 * @param goldBeanEntity
	 * @return void
	 */
	private void showChannelPay(BasePayActivity baseActivity,ExchangeGoldBeanEntity goldBeanEntity)
	{
		GoldBeanPayDialog dialog = new GoldBeanPayDialog(baseActivity,
				new ChannelListener(baseActivity), goldBeanEntity);
		dialog.initdata(goldBeanEntity.channel, goldBeanEntity.content);
		dialog.show();
	}
	
	
	/**
	 * TODO  兑换金豆成功  提示
	 *
	 *
	 * @param baseActivity
	 * @param tquzi_gold_num
	 * @param tquzi_goldbean_num
	 * @param cstatus
	 * @return void
	 */
	public void quziDollarSuccessTips(HupuBaseActivity baseActivity,int tquzi_gold_num,int tquzi_goldbean_num,String cstatus)
	{
		if(baseActivity==null||cstatus==null)
		{
			return ;
		}
		
		if(cstatus.equals("1"))
		{
			baseActivity.showToast(String.format(baseActivity.getString(R.string.hupudollar_quzitip_success_tips1),tquzi_gold_num));
		}
		else if(cstatus.equals("2")||cstatus.equals("3"))
		{
			baseActivity.showToast(String.format(baseActivity.getString(R.string.hupudollar_quzitip_success_tips2),tquzi_gold_num,tquzi_goldbean_num));
		}
		else if(cstatus.equals("4"))
		{
			
		}
	}
	
	private String postType;
	protected String total;
	
	public void recharge(BasePayActivity baseActivity,Object o)
	{
		if(o instanceof GoldDallorRechargeEntity)
		{
			GoldDallorRechargeEntity goldBeanEntity = (GoldDallorRechargeEntity) o;
			if (goldBeanEntity.url.equals("SUCCESS")) {
				quziDollarSuccessTips(baseActivity,quzi_gold_num,quzi_goldbean_num,status);
				if(status.equals("2")||status.equals("1"))
				{
					if(baseActivity.curFragmentIndex == BaseGameActivity.INDEX_LIVE)
					{
						if(baseActivity.mFragmentLive!=null)
						{
							baseActivity.mFragmentLive.sendQuizCommit();
						}
					}
					else if(baseActivity.curFragmentIndex == BaseGameActivity.INDEX_QUIZ)
					{
						if(baseActivity.mFragmentQuizList!=null)
						{
							baseActivity.mFragmentQuizList.sendQuizCommit();
						}
					}
				}
			} else {

				if (goldBeanEntity.orderNo != null) {
					if (postType.equals("alipay_app")) {
						goSmsOrder(baseActivity,goldBeanEntity.url);
					} else if (postType.equals("weixin")) {
						WeixinPayReq weixinEntityPayReq = (WeixinPayReq) JsonPaserFactory
								.paserObj(goldBeanEntity.url,
										HuPuRes.REQ_METHOD_GET_WEIXIN_RECHARGE);
						IWXAPI api = WXAPIFactory.createWXAPI(baseActivity,
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
							baseActivity.showToast(baseActivity.getString(R.string.weixin_setup));
							Intent weixinIntent = new Intent();
							weixinIntent.setAction("android.intent.action.VIEW");
							Uri content_url = Uri
									.parse(baseActivity.getString(R.string.weixin_url));
							weixinIntent.setData(content_url);
							baseActivity.startActivity(weixinIntent);

						}
					} 
					else if (postType.equals("kanqiu_wallet_pay")) {
						if (goldBeanEntity.url.equals("SUCCESS")) {
							baseActivity.showToast(baseActivity.getString(R.string.buy_success));
						}
					}
					else
					{
						baseActivity.showToast(baseActivity.getString(R.string.hupudollar_tip_failure));
					}
				}
			}
		}
		else if(o instanceof GoldEntity)
		{
			GoldEntity orderEntity = (GoldEntity) o;
			if(orderEntity!=null)
			{
				quzi_goldbean_num = Integer.parseInt(orderEntity.gold_num) - quzi_gold_num;
			}
		}
	}
	
	/**
	 * TODO 支付宝充值
	 *
	 *
	 * @param baseActivity
	 * @param order_id
	 * @return void
	 */
	private void goSmsOrder(final BasePayActivity baseActivity,final String order_id) {
		context = baseActivity;
		if (postType.equals("alipay_app")) {
			new Thread() {
				public void run() {
					String result = new AliPay(baseActivity, mHandler)
							.pay(order_id);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case RQF_PAY: {
				Result.sResult = (String) msg.obj;
				if (Result.getResult().equals("9000")) {
					((BaseActivity) context).showToast(String.format(
							context.getString(R.string.title_pay_success), total));
				} else {
					((BaseActivity) context).showToast(context.getString(R.string.title_pay_failure));
				}

			}
			default:
				break;
			}
		}
	};

	
	private class ChannelListener implements GoldBeanDollorCallBack {

		BasePayActivity baseActivity;
		public ChannelListener(BasePayActivity baseActivity)
		{
			this.baseActivity = baseActivity;
		}
		@Override
		public void onPayListener(Dialog dialog, BaseEntity entity,
				String channel) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			ExchangeGoldBeanEntity  goldBeanEntity = (ExchangeGoldBeanEntity) entity;
			reqChannelPay(baseActivity,goldBeanEntity,channel);
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
	private synchronized void reqChannelPay(BasePayActivity baseActivity,ExchangeGoldBeanEntity goldBeanEntity,String channel)
	{
		if(baseActivity==null||goldBeanEntity==null)
		{
			return;
		}
		
		postType = channel;
		RequestParams mParams = baseActivity.initParameter();
		mParams.put("token", getToken());
		mParams.put("type", postType);

		String eventid = goldBeanEntity.eventDataBean==null?"":goldBeanEntity.eventDataBean.id;

		mParams.put("event", eventid + "");
		mParams.put("exchangeAmount", goldBeanEntity.coin + "");
		
		if (postType.equals("alipay_wap")) {
			Intent in = new Intent(baseActivity, GoldBeanWebViewActivity.class);
			in.putExtra(GoldBeanWebViewActivity.EXTRA_INTEN_METHODID, HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS);
			in.putExtra("token", getToken());
			in.putExtra("type", "" + postType);
			in.putExtra("event", eventid + "");
			in.putExtra("exchangeAmount", goldBeanEntity.coin + "");
			in.putExtra(GoldBeanWebViewActivity.TYPE_PATH, GoldDataService.DATA_CHARGE_PATH_QUZI);
			baseActivity.startActivityForResult(in, HupuBaseActivity.REQ_GO_POST_ORDER);
		} else if (postType.equals("alipay_app")) {
			baseActivity.sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS, mParams,
					new HupuHttpHandler(baseActivity), true);

		} else if (postType.equals("weixin")) {
			baseActivity.sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS, mParams,
					new HupuHttpHandler(baseActivity), true);
		} else {
			Intent in = new Intent(baseActivity, GoldBeanWebViewActivity.class);
			in.putExtra(GoldBeanWebViewActivity.EXTRA_INTEN_METHODID, HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS);
			in.putExtra("token", getToken());
			in.putExtra("type", "" + postType);
			in.putExtra("event", eventid + "");
			in.putExtra("exchangeAmount", goldBeanEntity.coin + "");
			in.putExtra(GoldBeanWebViewActivity.TYPE_PATH, GoldDataService.DATA_CHARGE_PATH_QUZI);
			baseActivity.startActivityForResult(in, HupuBaseActivity.REQ_GO_POST_ORDER);
		}
	}
	
}
