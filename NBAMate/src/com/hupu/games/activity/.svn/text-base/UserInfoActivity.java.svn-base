package com.hupu.games.activity;

import java.util.HashMap;
import java.util.Map;

import org.adver.score.scorewall.ScoreWallSDK;
import org.adver.score.sdk.YjfSDK;
import org.adver.score.sdk.widget.UpdateScordNotifier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BalanceReq;
import com.hupu.games.data.PrizeEntity;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.account.TaskEntity;
import com.hupu.games.data.account.UserBetInfoReq;
import com.hupu.games.data.task.TaskRewardEntity;
import com.hupu.games.pay.AccountActivity;
import com.hupu.games.pay.HupuOrderActivity;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.UserInfo;

/**
 * “我”的页面
 * */
@SuppressLint("NewApi")
public class UserInfoActivity extends HupuBaseActivity implements UpdateScordNotifier{

	private TextView txtNickName;
	private TextView txtUserName;
	private TextView txtBoxNum, guessResult;
	// ImageView img_nick;
	boolean byMan;
	private int money = 0;
	String CoinNum = "0";
	int resumeNum = 0;
	/** 余额 */
	TextView txtBalance,txtCaipiaoBalance;
	LinearLayout prizeLayout, taskLayout;
	View mView;
	int viewWidth = 0,viewHeight = 0;
	ProgressBar guessBar, goldBar,walletBar,caipiaoBar;
	String guessMark = "";
	private ProgressBar getRewardBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_userinfo);

		setOnClickListener(R.id.layout_account);
		setOnClickListener(R.id.btn_back);

		setOnClickListener(R.id.layout_wallet);
		setOnClickListener(R.id.layout_guess_result);
		setOnClickListener(R.id.btn_setup);
		setOnClickListener(R.id.layout_coin);
		setOnClickListener(R.id.coin_prize);
		setOnClickListener(R.id.my_prize_txt);
		setOnClickListener(R.id.today_task_layout);
		setOnClickListener(R.id.layout_feedback);
		setOnClickListener(R.id.layout_contacts);
		setOnClickListener(R.id.layout_caipiao_result);

		txtNickName = (TextView) findViewById(R.id.txt_nick_name);
		txtUserName = (TextView) findViewById(R.id.user_name);
		txtBoxNum = (TextView) findViewById(R.id.txt_box_num);
		prizeLayout = (LinearLayout) findViewById(R.id.coin_prize);
		taskLayout = (LinearLayout) findViewById(R.id.today_task_layout);

		guessResult = (TextView) findViewById(R.id.guess_result);

		txtBalance = (TextView) findViewById(R.id.txt_coin_num);
		txtCaipiaoBalance = (TextView) findViewById(R.id.wallet_result);

		guessBar = (ProgressBar) findViewById(R.id.guess_Porgress);
		goldBar = (ProgressBar) findViewById(R.id.gold_Porgress);
		walletBar = (ProgressBar) findViewById(R.id.wallet_Porgress);
		getRewardBar = (ProgressBar) findViewById(R.id.load_progress);
		getRewardBar.setVisibility(View.GONE);

		if (mToken == null){
			findViewById(R.id.user_info_layout).setVisibility(View.GONE);
			findViewById(R.id.user_caipiao_layout).setVisibility(View.GONE);
		}else{
			findViewById(R.id.user_info_layout).setVisibility(View.VISIBLE);
		}

		if (SharedPreferencesMgr.getInt("show_mall", 0) == 0)
			findViewById(R.id.show_mall).setVisibility(View.GONE);
		else
			findViewById(R.id.show_mall).setVisibility(View.VISIBLE);

	}

	@Override
	protected void onStop() {
		super.onStop();
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
		case R.id.layout_account:

			switchToAccountAct();
			break;
		case R.id.layout_pay:

			Intent order = new Intent(this, HupuOrderActivity.class);
			order.putExtra("balance", money);
			startActivityForResult(order, REQ_GO_CHARGE);
			break;
			//		case R.id.layout_box:
			//			Intent box = new Intent(this, MyBoxActivity.class);
			//			startActivityForResult(box, REQ_SHOW_BOX);
			//			break;
		case R.id.my_prize_txt:// 我的奖品
			if (mToken == null) {
				showBindDialog(SharedPreferencesMgr.getString("dialogMyPrize", getString(R.string.bind_phone_dialog)));
				//dialog(this,SharedPreferencesMgr.getString("dialogMyPrize", getString(R.string.bind_phone_dialog)));
			} else {
				sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_MY_AWARD, HuPuRes.UMENG_VALUE_FROM_MY_PAGE);

				Intent myPrize = new Intent(this, MyPrizeListActivity.class);
				startActivity(myPrize);
			}
			break;
		case R.id.btn_setup:

			Intent intent = new Intent(this, SetupActivity.class);
			startActivity(intent);
			break;
		case R.id.coin_prize:
			sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_LIST, HuPuRes.UMENG_VALUE_FROM_MY_PAGE);
			Intent coinPrize = new Intent(this, ExchangeListActivity.class);
			startActivity(coinPrize);
			break;
		case R.id.layout_coin:
			Intent coinIntent = new Intent(this, UserGoldActivity.class);
			startActivity(coinIntent);

			break;

		case R.id.layout_guess_result:
			if (mToken == null) {
				showBindDialog(SharedPreferencesMgr.getString("dialogQuize", getString(R.string.bind_phone_dialog)));
				//dialog(this,SharedPreferencesMgr.getString("dialogQuize", getString(R.string.bind_phone_dialog)));
			} else {

				Intent myGuessIntent = new Intent(this,
						MyQuizListActivity.class);
				myGuessIntent.putExtra("guess_mark", guessMark);
				startActivity(myGuessIntent);
			}
			break;
		case R.id.today_task_layout:
			if (mToken == null) {
				showBindDialog(SharedPreferencesMgr.getString("dialogTask", getString(R.string.bind_phone_dialog)));
				//dialog(this,SharedPreferencesMgr.getString("dialogTask", getString(R.string.bind_phone_dialog)));
			}
			break;
		case R.id.layout_feedback:

			// 反馈
			FeedbackAgent agent = new FeedbackAgent(this);
			UserInfo info = agent.getUserInfo();
			if (info == null)
				info = new UserInfo();
			Map<String, String> contact = info.getContact();
			if (contact == null)
				contact = new HashMap<String, String>();
			//          	contact.put("uid", "1234567");
			HupuLog.d("Uid=====", ""+uid);
			if(uid>0)//用户码
				contact.put("uid", ""+uid);
			else 
				contact.put("uid", null);

			if(mDeviceId!=null)//设备码
				contact.put("cid", mDeviceId);
			else
				contact.put("cid", null);

			if(SharedPreferencesMgr.getString(
					HuPuRes.KEY_NICK_NAME, null)!=null)//昵称
				contact.put("nick", SharedPreferencesMgr.getString(
						HuPuRes.KEY_NICK_NAME, null));
			else
				contact.put("nick", null);
			//增加用户网络类型
			//            contact.put("network", DeviceInfo.getNetWorkName(this));

			SharedPreferencesMgr.getString(
					HuPuRes.KEY_NICK_NAME, "");

			info.setContact(contact);
			agent.setUserInfo(info);
			agent.startFeedbackActivity();			
			break;
		case R.id.layout_contacts:
			// 联系客服
			Intent inContact = new Intent(this, ContactsActivity.class);
			startActivity(inContact);
			//			init();
			//			login();

			break;
		case R.id.layout_wallet:
			// 钱包
			Intent inWallet = new Intent(this, UserWalletActivity.class);
			startActivity(inWallet);

			break;
		case R.id.layout_caipiao_result:
			// 我的彩票
			sendUmeng(HuPuRes.UMENG_EVENT_LOTTERY, HuPuRes.UMENG_KEY_MY_LOTTERY);
			Intent myCaipiao = new Intent(this, MyCaipiaoListActivity.class);
			startActivity(myCaipiao);

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

	void switchToAccountAct() {
		Intent intent;
		intent = new Intent(this, AccountActivity.class);
		startActivity(intent);
	}

	public void onResume() {
		super.onResume();
		resumeInfo();
	}

	private void resumeInfo(){
		if (mToken == null) {
			txtNickName.setText(getString(R.string.title_unbind_phone_num));

		}else{
			if (!SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "").equals("")) {
				txtNickName.setText(SharedPreferencesMgr.getString(
						HuPuRes.KEY_NICK_NAME, getString(R.string.title_unsett_nick)));
			}else {
				txtNickName.setText(getString(R.string.title_unsett_nick));
			}
		}

		reqBetInfo();

		if (mToken == null){
			findViewById(R.id.user_info_layout).setVisibility(View.GONE);
			findViewById(R.id.user_caipiao_layout).setVisibility(View.GONE);
		}
		else{
			findViewById(R.id.user_info_layout).setVisibility(View.VISIBLE);
		}

	}
	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();
		resumeInfo();
		HupuLog.e("papa", "loginSuccess");
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//reqBetInfo();
	}

	@SuppressWarnings("unused")
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_GET_BETINFO) {
			if (o != null) {
				UserBetInfoReq info = (UserBetInfoReq) o;
				if (info.box != 0) {
					txtBoxNum.setText("" + info.box);
					findViewById(R.id.user_box_icon).setVisibility(View.VISIBLE);
				} else {
					txtBoxNum.setText("");
					findViewById(R.id.user_box_icon).setVisibility(View.GONE);
				}

				guessMark = info.win != 0 || info.lose != 0 ? info.win
						+ "胜 " + info.lose + "负" : "";
				guessResult.setText(guessMark);

				txtBalance.setText(info.balance + "");
				if (info.walletBalance == null || info.walletBalance.equals("")) {
					findViewById(R.id.user_caipiao_layout).setVisibility(View.GONE);
				}else {
					findViewById(R.id.user_caipiao_layout).setVisibility(View.VISIBLE);
				}
				txtCaipiaoBalance.setText(info.walletBalance+"元");
				findViewById(R.id.gold_icon).setVisibility(View.VISIBLE);

				if (info.balance == 0) {
					CoinNum ="0";
				}else if (info.balance > 0 && info.balance <10) {
					CoinNum ="<10";
				} else if (info.balance >= 10 && info.balance <100) {
					CoinNum ="10-100";
				} else if (info.balance >= 100 && info.balance <1000) {
					CoinNum ="100-1000";
				}else if (info.balance >= 1000) {
					CoinNum =">1000";
				}

				guessBar.setVisibility(View.GONE);
				goldBar.setVisibility(View.GONE);
				walletBar.setVisibility(View.GONE);

				if (info.prizeList != null && info.prizeList.size() > 0) {
					prizeLayout.removeAllViews();
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							viewWidth, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
					lp.setMargins(0, 0, 12, 0);

					for (int i = 0; i < info.prizeList.size(); i++) {
						final PrizeEntity prize = info.prizeList.get(i);
						View mView = getLayoutInflater().inflate(
								R.layout.item_coin_prize, null);
						switch (prize.type) {
						case 0:
							mView.findViewById(R.id.prize_status).setVisibility(
									View.GONE);
							break;
						case 1:
							((ImageView) mView.findViewById(R.id.prize_status))
							.setBackgroundResource(R.drawable.icon_myaccount_new);
							break;
						case 2:
							((ImageView) mView.findViewById(R.id.prize_status))
							.setBackgroundResource(R.drawable.icon_myaccount_hot);
							break;
						case 3:
							((ImageView) mView.findViewById(R.id.prize_status))
							.setBackgroundResource(R.drawable.icon_myaccount_limit);
							break;

						default:
							break;
						}

						UrlImageViewHelper.setUrlDrawable(
								((ImageView) mView.findViewById(R.id.prize_img)),
								prize.img_small, R.drawable.bg_mall_no_pic_s);
						((TextView) mView.findViewById(R.id.prize_name))
						.setText(prize.shortname);
						((TextView) mView.findViewById(R.id.coin_num))
						.setText(prize.coin + getString(R.string.gold_bean));


						// 设置间距
						mView.setLayoutParams(lp);
						prizeLayout.addView(mView);
						//					if (i == info.prizeList.size()-1) {
						//						
						//						View moreView = getLayoutInflater().inflate(
						//								R.layout.item_coin_prize_more, null);
						//						LinearLayout.LayoutParams lpMore = new LinearLayout.LayoutParams(
						//								viewWidth, viewHeight, 1);
						//						moreView.setLayoutParams(lpMore);
						//						prizeLayout.addView(moreView);
						//					}

						mView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								sendUmeng(HuPuRes.UMENG_EVENT_MALL, HuPuRes.UMENG_KEY_AWARD_LIST, HuPuRes.UMENG_VALUE_AWARD_NUM);

								Intent coinIntent = new Intent(
										UserInfoActivity.this,
										ExchangeListActivity.class);
								startActivity(coinIntent);
							}
						});
					}
				}

				if (info.taskList != null && info.taskList.size() > 0) {
					taskLayout.removeAllViews();
					for (int i = 0; i < info.taskList.size(); i++) {
						final TaskEntity task = info.taskList.get(i);
						View taskView = getLayoutInflater().inflate(
								R.layout.item_task, null);
						((TextView) taskView.findViewById(R.id.task_name))
						.setText(task.name);
						//积分墙 特殊逻辑
						if (task.id >= 999999999) 
							((TextView) taskView.findViewById(R.id.task_coin))
							.setText("每日最多+" + task.coin+getString(R.string.gold_bean));
						else
							((TextView) taskView.findViewById(R.id.task_coin))
							.setText("+" + task.coin+getString(R.string.gold_bean));

						if (task.status == 1)
						{

							TextView taskItemstatus = ((TextView) taskView.findViewById(R.id.task_status));
							taskItemstatus.setWidth(136);
							taskItemstatus.setText(Html.fromHtml("<font color=\"#E61A1A\"> 领取 </font>"));
							taskItemstatus.setBackgroundResource(R.drawable.btn_binding_selector);
						} 
						else if(task.status == 2)
						{

							TextView taskItemstatus = ((TextView) taskView.findViewById(R.id.task_status));
							taskItemstatus.setWidth(136);
							taskItemstatus.setText(Html.fromHtml("<font color=\"#808080\"> 已领取</font>"));
							taskItemstatus.setBackgroundResource(R.drawable.btn_unbindling_down);

						}
						else {
							TextView taskItemstatus = ((TextView) taskView.findViewById(R.id.task_status));
							taskItemstatus.setText(task.process_text);
							taskItemstatus.setBackgroundColor(Color.TRANSPARENT);

						}
						taskView.setTag(task.id);
						taskLayout.addView(taskView);

						if (mToken != null) {
							taskView.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									taskGoto(task);
								}
							});
						}
						
						TextView taskStatus = (TextView) taskView.findViewById(R.id.task_status);
						boolean isEnable = task.status==2?false:true;
						taskStatus.setEnabled(isEnable);
						taskStatus.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if(task.status == 1)
								{
									getRewardBar.setVisibility(View.VISIBLE);
									reqGetTaskReward(task.id+"");
								}
								else
								{
									taskGoto(task);
								}
							}
						});
					}
				}
				//校验token是否失效
				if (resumeNum == 0) 
					checkToken(info.isLogin);
				HupuLog.e("papa", "-----"+resumeNum);
				resumeNum ++;
			}
		} else if (methodId == HuPuRes.REQ_METHOD_GET_BALANCE) {
			money = ((BalanceReq) o).balance;
			txtBalance.setText(money + "");
		}
		else if (methodId == HuPuRes.REQ_METHOD_GET_TASKREWARD) {
			getRewardBar.setVisibility(View.GONE);
			if (o != null) {
				TaskRewardEntity info = (TaskRewardEntity) o;
				if(info!=null)
				{
					if(info.id.equals("1"))
					{
						reqBetInfo();
						Toast.makeText(this, info.text, Toast.LENGTH_LONG).show();
					}
					else if(info.id.equals("-1"))
					{
						Toast.makeText(this, info.text, Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(this, 
								this.getString(R.string.title_taskreward_failure), Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(this, 
							this.getString(R.string.title_taskreward_failure), Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	/** 请求余额 */
	void reqBalance() {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			sendRequest(HuPuRes.REQ_METHOD_GET_BALANCE, mParams,
					new HupuHttpHandler(this), true);
		}
	}

	private void taskGoto(final TaskEntity task)
	{
		if(task==null)
		{
			return;
		}
		
		if (task.id >= 999999999) {
			//初始化,当Activity第一次创建时调用
			YjfSDK.getInstance(UserInfoActivity.this, UserInfoActivity.this).initInstance("72108","EMLQU08GF2LWNYW1AM7OEU2F8CV0TG0JCS","81553","sdk 4.0.0");
			if (mToken!= null) {
				YjfSDK.getInstance(UserInfoActivity.this, null).setCoopInfo(mToken);
			}
			ScoreWallSDK.getInstance(UserInfoActivity.this).showScoreWall();
		}else {

			Intent taskIntent = new Intent(
					UserInfoActivity.this,
					TaskActivity.class);
			taskIntent.putExtra("id", task.id);
			startActivity(taskIntent);
		}
	}
	
	private void reqBetInfo() {
		guessBar.setVisibility(View.VISIBLE);
		goldBar.setVisibility(View.VISIBLE);
		walletBar.setVisibility(View.VISIBLE);

		initParameter();
		mParams.put("token", mToken);
		String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);

		//		HupuLog.e("papa", "---token=="+mToken+"-----------sign=="+sign);
		sendRequest(HuPuRes.REQ_METHOD_GET_BETINFO, mParams,
				new HupuHttpHandler(this), false);
	}

	/**
	 * TODO  获取任务奖励
	 *
	 * @param taskid
	 * @return void
	 */
	public void reqGetTaskReward(String taskid)
	{

		initParameter();
		mParams.put("token", mToken);
		mParams.put("id", taskid);

		sendRequest(HuPuRes.REQ_METHOD_GET_TASKREWARD, mParams,
				new HupuHttpHandler(this), false);
	}
	/**
	 * 
	 * 方法: updateScoreSuccess 描述:
	 *  TODO 参数:
	 *  @param type 0初始化 1查询积分 2消费积分 
	 *  @param current 当前的积分
	 *  @param change 变更的积分
	 *  @param unit 单位 
	 */

	@Override
	public void updateScoreFailed(int type, int code, String error) {
		// TODO Auto-generated method stub
		Toast.makeText(this,"错误代码:"+code+",内容:"+error ,Toast.LENGTH_SHORT).show();
	}


	/**
	 * 
	 * 方法: updateScoreSuccess 描述:
	 *  TODO 参数:
	 *  @param type 0初始化 1查询积分 2消费积分 
	 *  @param current 当前的积分
	 *  @param change 变更的积分
	 *  @param unit 单位 
	 */
	@Override
	public void updateScoreSuccess(int type, int current, int change, String unit) {
		// TODO Auto-generated method stub
		Toast.makeText(this,"当前为:"+current+",单位:"+unit ,Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onErrResponse(Throwable error, int type) {
		// TODO Auto-generated method stub
		super.onErrResponse(error, type);
		getRewardBar.setVisibility(View.GONE);
	}

	@Override
	public void onErrMsg(String msg, int type) {
		// TODO Auto-generated method stub
		super.onErrMsg(msg, type);
		getRewardBar.setVisibility(View.GONE);
	}
}
