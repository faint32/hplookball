package com.hupu.games.activity;

import java.io.File;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.SendMsgResp;
import com.hupu.games.pay.AccountActivity;
import com.hupu.games.pay.PhoneInputActivity;
import com.hupu.games.util.FileUtils;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.UserInfo;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

@SuppressLint("NewApi")
public class SetupActivity extends HupuBaseActivity {

	private TextView txtNickName;
	
	private TextView txtVersion, txt_del;

	private ToggleButton tgNotify, tgNoPic;

	private final int DIALOG_NOTIFY = 1314;
	private final int DIALOG_CACHE_CLEAN = 1315;
	boolean byMan;
	Context mcontext;

	boolean isCheck;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setup_new);
		
		setOnClickListener(R.id.layout_account);
		setOnClickListener(R.id.layout_feedback);
		setOnClickListener(R.id.layout_contacts);
		
		setOnClickListener(R.id.layout_about);
		
		setOnClickListener(R.id.layout_about);
		setOnClickListener(R.id.layout_reward);
		setOnClickListener(R.id.layout_follow);
		setOnClickListener(R.id.layout_account);
		setOnClickListener(R.id.layout_check_version);
		setOnClickListener(R.id.btn_back);
//		setOnClickListener(R.id.layout_exchange);
		setOnClickListener(R.id.layout_honor_item);
		setOnClickListener(R.id.lay_clean);
		
		txtNickName = (TextView) findViewById(R.id.txt_nick_name);
		
		mcontext = this.getApplicationContext();
		txtVersion = (TextView) findViewById(R.id.txt_version);
		txt_del = (TextView) findViewById(R.id.txt_del);

		tgNotify = (ToggleButton) findViewById(R.id.toggle_notify);
		tgNotify.setChecked(SharedPreferencesMgr.getBoolean("is_push", true));
		tgNotify.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked && !byMan) {
					SharedPreferencesMgr.setBoolean("is_push", true);
					reqNotify(1);
				}
				if (!isChecked && !byMan) {
					showCustomDialog(DIALOG_NOTIFY, R.string.push_title,
							R.string.push_notify, 3, R.string.cancel,
							R.string.close_notify);
					buttonView.setChecked(true);
				}
				byMan = false;
			}
		});

		tgNoPic = (ToggleButton) findViewById(R.id.toggle_no_pic);
		tgNoPic.setChecked(SharedPreferencesMgr.getBoolean("is_no_pic", true));
		tgNoPic.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SharedPreferencesMgr.setBoolean("is_no_pic", isChecked);
			}
		});

		txtVersion.setText(mApp.getVerName());

		/**
		 * 需求：是vip才显示相关内容 不是 使用原先的样式
		 */
		new Thread() {
			@Override
			public void run() {
				long size;
				try {
					size = new FileUtils().getFileSize(new File(
							UrlImageViewHelper.getCahePath(mcontext)));
					Message msg = new Message();
					msg.what = 1;
					msg.obj = size;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	
	public Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				readCacheSize(msg.obj.toString());
				break;
			case 2:
				Toast.makeText(mcontext,
						mcontext.getString(R.string.clean_success_msg),
						Toast.LENGTH_SHORT).show();
				readCacheSize(msg.obj.toString());
				break;
			}
		}
	};

	private void readCacheSize(String size) {
		try {
			NumberFormat numFor = NumberFormat.getNumberInstance();
			numFor.setMaximumFractionDigits(1);
			String unit = " KB";
			String value="";
			if (Float.valueOf(size) > 1024 * 1024) {
				unit = " MB";
				value = numFor.format(Float.valueOf(size) / 1024 / 1024);
			} else if (Float.valueOf(size) > 1024 * 1024 * 1024) {
				unit = " GB";
				value = numFor.format(Float.valueOf(size) /1024/1024/1024);
			}else{
				value = numFor.format(Float.valueOf(size)/1024);
			}
			txt_del.setText(value.equals("0.0") ? "0" : value + unit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mApp.setNotify(tgNotify.isChecked());
	}

	@Override
	public void clickPositiveButton(int dialogId) {

		super.clickPositiveButton(dialogId);
		if (dialogId == DIALOG_NOTIFY) {
			mDialog.cancel();
//			tgNotify.setChecked(true);
//			SharedPreferencesMgr.setBoolean("is_push", true);
//			HupuPushInterface.setClosePush(SetupActivity.this,false);
		}
		if (dialogId == DIALOG_CACHE_CLEAN) {
			mDialog.cancel();
		}
	}

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
		mDialog.cancel();
		if (dialogId == DIALOG_NOTIFY) {
			mDialog.cancel();
			byMan = true;
			tgNotify.setChecked(false);
			SharedPreferencesMgr.setBoolean("is_push", false);
			reqNotify(0);
		}
		if (dialogId == DIALOG_CACHE_CLEAN) {
			mDialog.cancel();
			new Thread() {
				@Override
				public void run() {
					long size;
					try {
						FileUtils fileUtil = new FileUtils();
						fileUtil.deleteFolderFile(
								new File(UrlImageViewHelper
										.getCahePath(mcontext))
										.getAbsolutePath(), false);
						size = fileUtil.getFileSize(new File(UrlImageViewHelper
								.getCahePath(mcontext)));
						Message msg = new Message();
						msg.what = 2;
						msg.obj = size;
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.layout_follow:
			Intent in = new Intent(this, FollowTeamActivity.class);
			startActivityForResult(in, 10);
			break;
		case R.id.layout_about://关于我们
			startActivity(new Intent(this, AboutActivity.class));
			break;
		
		case R.id.layout_reward:
			try {
				// 打分		
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + getPackageName()));
				startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				showToast(getString(R.string.no_market));
				return;
			}
			

			break;
		case R.id.lay_clean:
			//清除缓存
			showCustomDialog(DIALOG_CACHE_CLEAN, R.string.title_clean, R.string.clean_content, 3,
					R.string.cancel, R.string.title_confirm);
			break;
		case R.id.btn_back:
			finish();
//			overridePendingTransition(R.anim.push_right_in,
//					R.anim.push_right_out);
			break;
		/*case R.id.layout_account:
			Intent inAccount = new Intent(this, NickNameActivity.class);
			// Intent inAccount = new Intent(this, UserInfoActivity.class);
			startActivity(inAccount);
			break;*/
		case R.id.layout_account://我的账户
			switchToAccountAct();
			break;
		case R.id.layout_contacts:
			// 联系客服
			Intent inContact = new Intent(this, ContactsActivity.class);
			startActivity(inContact);
//			init();
//			login();

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

		case R.id.layout_check_version:
			checkupdate();
			break;
//		case R.id.layout_exchange:
//			ExchangeDataService service = new ExchangeDataService();
//			new ExchangeViewManager(this, service).addView(
//					ExchangeConstants.type_list_curtain, null);
//			break;
		case R.id.layout_honor_item:

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
//			overridePendingTransition(R.anim.push_right_in,
//					R.anim.push_right_out);
		}
		return false;
	}

	void switchToPhoneBindAct() {
		Intent intent = new Intent(this, PhoneInputActivity.class);
		startActivityForResult(intent, REQ_GO_BIND_PHONE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 10) {
			if (resultCode == RESULT_OK) {
			}
		} else if (requestCode == 7777) {
			if (resultCode == RESULT_OK) {
			}
		}
	}

	public void onResume() {
		super.onResume();
		tgNotify.setChecked(SharedPreferencesMgr.getBoolean("is_push", true));
		resumeInfo();
	}


	/** 加载用户关注的球队列表 */
	/*
	 * private void loadFollowData() { mApp.loadFollowTeam(); if
	 * (mApp.loadFollowTeam().size() == 0) getTeamsFromWeb(); else
	 * setTeamNames();
	 * 
	 * }
	 */

	/*
	 * private void setTeamNames() {
	 * findViewById(R.id.pBar).setVisibility(View.GONE); canClick=true;
	 * txtFollows.setText(HuPuApp.getFollowTeamsNames(null)); }
	 */

	/** 从网络上拉取用户所关注的球队列表 */
	/*
	 * private void getTeamsFromWeb() { initParameter();
	 * sendRequest(HuPuRes.REQ_METHOD_GET_FOLLOW_TEAMS, mParams, new
	 * HupuHttpHandler(this)); }
	 */

	private void checkupdate() {
		//UmengUpdateAgent.update(this);
		UmengUpdateAgent.forceUpdate(this);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0: // has update
					
					UmengUpdateAgent.showUpdateDialog(SetupActivity.this,
							updateInfo);
					break;
				case 1: // has no update
					showToast("当前已经是最新版本了");
					break;
				/*
				 * case 2: // none wifi Toast.makeText(getActivity(),
				 * "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT) .show(); break;
				 */
				case 3: // time out
					showToast("获取最新版本超时");
					break;
				}
			}

		});
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
		//reqBetInfo();
		
	}

	void switchToAccountAct() {
		Intent intent;
		intent = new Intent(this, AccountActivity.class);
		startActivity(intent);
	}
	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();
		resumeInfo();
		HupuLog.e("papa", "loginSuccess");
	}
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		 if(methodId ==HuPuRes.REQ_METHOD_SET_NOTIFY)
		{
			SendMsgResp msg = (SendMsgResp) o;
			if(msg.pid ==0)
			{
				showToast("设置失败");
				byMan = true;
				isCheck =!isCheck;
				tgNotify.setChecked(isCheck); 
			}
		}
	}


	
	void reqNotify(int n)
	{
		isCheck =n==1; 
		initParameter();
		mParams.put("isnotific",""+n);
		sendRequest(HuPuRes.REQ_METHOD_SET_NOTIFY, mParams,
				new HupuHttpHandler(this), false);
	}
}
