/**
 * 
 */
package com.hupu.games.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.CoinInfoActivity;
import com.hupu.games.activity.NickNameActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.account.BindInfoReq;
import com.hupu.games.data.account.QqLoginEntity;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.http.HupuHttpException;
import com.hupu.http.HupuHttpHandler;
import com.tencent.tauth.Tencent;

/**
 * @author papa 用户登陆绑定act
 */
public class AccountActivity extends BasePayActivity {

	private TextView txtNickName, qqName,phoneNum,bindInfo,hupuName;
	private Button btn_submit;
	public  Tencent mTencent;
	private Button bindQQ,bindPhone;
	private int channel;
	ProgressBar qqBar,phoneBar,hupuBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_unbind_phone);
		qqName = (TextView) findViewById(R.id.qq_login_text);
		phoneNum = (TextView) findViewById(R.id.phone_login_text);
		hupuName = (TextView) findViewById(R.id.hupu_login_text);
		txtNickName = (TextView) findViewById(R.id.txt_nick_name);
		bindInfo = (TextView) findViewById(R.id.bind_info);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		
		bindQQ = (Button) findViewById(R.id.btn_qq_bind);
		bindPhone = (Button) findViewById(R.id.btn_phone_bind);
		
		qqBar = (ProgressBar) findViewById(R.id.qq_Porgress);
		phoneBar = (ProgressBar) findViewById(R.id.phone_Porgress);
		hupuBar = (ProgressBar) findViewById(R.id.hupu_Porgress);
		
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.layout_account);
		setOnClickListener(R.id.btn_submit);
		setOnClickListener(R.id.layout_phone);
		setOnClickListener(R.id.layout_hupu);
		setOnClickListener(R.id.layout_qq_login);
		
		setOnClickListener(R.id.btn_qq_bind);
		setOnClickListener(R.id.btn_phone_bind);
		setBindInfo();
		
	}

	/**
	 * 处理绑定和登陆 等状态  比较乱（赶arena)。。需要改动可@papa
	 */
	private void setBindInfo(){
		qqBar.setVisibility(View.GONE);
		phoneBar.setVisibility(View.GONE);
		hupuBar.setVisibility(View.GONE);
		if (mToken == null) { //tonken 为null  说明均未绑定
			bindInfo.setText(Html.fromHtml(SharedPreferencesMgr.getString("loginTipsGrey", getString(R.string.bind_phone_prompt))+"<font color=\"#FF0000\">"+SharedPreferencesMgr.getString("loginTipsRed", "")));
			
			findViewById(R.id.phone_arrow).setVisibility(View.VISIBLE);
			findViewById(R.id.qq_arrow).setVisibility(View.VISIBLE);
			findViewById(R.id.hupu_arrow).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_hupu).setVisibility(View.VISIBLE);
			bindQQ.setVisibility(View.GONE);
			bindPhone.setVisibility(View.GONE);
			findViewById(R.id.layout_qq_login).setEnabled(true);
			findViewById(R.id.layout_phone).setEnabled(true);
			qqName.setText(getString(R.string.qq_login_text));
			phoneNum.setText(getString(R.string.phone_login));
			findViewById(R.id.layout_hupu).setVisibility(View.VISIBLE);
			hupuName.setText(getString(R.string.hupu_login));
		}else {//表示至少绑定了一个
			
			//String phone = SharedPreferencesMgr.getString("bp", "");
			bindInfo.setText(Html.fromHtml(SharedPreferencesMgr.getString("bindTips", getString(R.string.bind_phone_prompt))));

			findViewById(R.id.layout_qq_login).setEnabled(false);
			findViewById(R.id.layout_phone).setEnabled(false);
				
			btn_submit.setVisibility(View.VISIBLE);
			
			boolean isAllBind = (SharedPreferencesMgr.getInt("channel1", 0) == 1 && SharedPreferencesMgr.getInt("channel2", 0) == 1 && SharedPreferencesMgr.getInt("channel3", 0) == 1)?true:false;
			findViewById(R.id.phone_arrow).setVisibility(View.GONE);
			findViewById(R.id.qq_arrow).setVisibility(View.GONE);
			if (isAllBind) {
				//都绑定了出现2个解除绑定
				bindQQ.setVisibility(View.VISIBLE);
				bindQQ.setTextColor(0xFF808080);
				bindPhone.setVisibility(View.VISIBLE);
				bindPhone.setTextColor(0xFF808080);
			}else {
				//bindPhone.setTextColor(SharedPreferencesMgr.getInt("channel2", 0) == 1 ?0xFF808080:0xFFba0000);
				//只有一个绑定了视为登陆  只有另外一个可以出现绑定的按钮     通过对方是否绑定 决定自己是否显示  微博绑定加入后  逻辑需要变更
				bindQQ.setVisibility((SharedPreferencesMgr.getInt("channel2", 0) == 1 &&SharedPreferencesMgr.getInt("channel1", 0) == 0) &&SharedPreferencesMgr.getInt("channel3", 0) == 0 ?View.GONE:View.VISIBLE);
				bindPhone.setVisibility(View.VISIBLE);
//				bindPhone.setVisibility(SharedPreferencesMgr.getInt("channel2", 0) == 1 ? View.VISIBLE:View.GONE);
			}
			bindPhone.setTextColor(SharedPreferencesMgr.getInt("channel1", 0) == 0 ?0xFFba0000:0xFF808080);
			bindQQ.setTextColor(SharedPreferencesMgr.getInt("channel2", 0) == 0 ?0xFFba0000:0xFF808080);
			bindQQ.setText(SharedPreferencesMgr.getInt("channel2", 0) == 0 ? getString(R.string.bind):getString(R.string.unbind));
			bindQQ.setBackgroundResource(SharedPreferencesMgr.getInt("channel2", 0) == 0 ?R.drawable.btn_binding_selector:R.drawable.btn_unbinding_selector);
			qqName.setText(SharedPreferencesMgr.getInt("channel2", 0) == 0 ? getString(R.string.qq_bind_text):"QQ ("+SharedPreferencesMgr.getString("qq_name", getString(R.string.qq_bind_text))+")");
			bindPhone.setText(SharedPreferencesMgr.getInt("channel1", 0) == 0 ? getString(R.string.bind):getString(R.string.update_phone));
			bindPhone.setBackgroundResource(SharedPreferencesMgr.getInt("channel1", 0) == 0 ?R.drawable.btn_binding_selector:R.drawable.btn_unbinding_selector);
			phoneNum.setText(SharedPreferencesMgr.getInt("channel1", 0) == 0 ? getString(R.string.phone_bind):"手机号 ("+SharedPreferencesMgr.getString("bp", getString(R.string.phone_bind))+")");
			//虎扑账号逻辑   没有这个渠道就不显示出来
			if (!"".equals(SharedPreferencesMgr.getString("hupu_name", ""))) {
				findViewById(R.id.layout_hupu).setVisibility(View.VISIBLE);
				hupuName.setText(SharedPreferencesMgr.getInt("channel3", 0) == 0 ? getString(R.string.hupu_login):SharedPreferencesMgr.getString("hupu_name", getString(R.string.phone_bind)));
				findViewById(R.id.hupu_arrow).setVisibility(View.GONE);
			}else {
				findViewById(R.id.layout_hupu).setVisibility(View.GONE);
				findViewById(R.id.hupu_arrow).setVisibility(View.VISIBLE);
			}
			
			
			if (!SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "").equals("")) {
				txtNickName.setText(SharedPreferencesMgr.getString(
						HuPuRes.KEY_NICK_NAME, getString(R.string.title_unsett_nick)));
			}else {
				txtNickName.setText(getString(R.string.title_unsett_nick));
			}
		}
	}
	
	
	
	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {
		case DIALOG_REMOVE_BIND:
			UnBind();
			break;
		case DIALOG_UNBOUND:
			unBind(channel);
			break;

		default:
			break;
		}
		
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.layout_qq_login:
			mTencent = Tencent.createInstance(mApp.QQ_APP_ID, this);
			onClickLogin(mTencent);
			break;
		case R.id.layout_account:
			UMENG_MAP.clear();
			
			switchToAccountAct();
			break;
		case R.id.btn_submit:
			
			showCustomDialog(DIALOG_REMOVE_BIND,SharedPreferencesMgr.getString("logoutAlert", getString(R.string.unbind_phone_txt)),
					BaseGameActivity.TOW_BUTTONS, R.string.title_unbind_phone,
					R.string.cancel);
			break;
		case R.id.layout_phone:
			UMENG_MAP.clear();
			
			switchToPhoneBindAct();
			break;
		case R.id.layout_hupu:
			if ("".equals(SharedPreferencesMgr.getString("hupu_name", "")) || mToken == null) {
				Intent intent = new Intent(this, HupuUserLoginActivity.class);
				startActivityForResult(intent, REQ_GO_HUPU_LOGIN);
			}
			
			break;
		case R.id.btn_qq_bind:
			channel = 2;
			HupuLog.e("papa", "----bind_tencent");
			if (SharedPreferencesMgr.getInt("channel"+channel, 0) == 0) {
				mTencent = Tencent.createInstance(mApp.QQ_APP_ID, this.getApplicationContext());
				onClickLogin(mTencent);
			}else {
				showCustomDialog(DIALOG_UNBOUND,SharedPreferencesMgr.getString("unboundAlert", getString(R.string.unbind_phone_txt)),
						BaseGameActivity.TOW_BUTTONS, R.string.title_confirm,
						R.string.cancel);
			}
			break;
		case R.id.btn_phone_bind:
			channel = 1;
			if (SharedPreferencesMgr.getInt("channel"+channel, 0) == 0) {
				UMENG_MAP.clear();
				switchToPhoneBindAct();
			}else {
//				showCustomDialog(DIALOG_UNBOUND,SharedPreferencesMgr.getString("unboundAlert", getString(R.string.unbind_phone_txt)),
//						BaseGameActivity.TOW_BUTTONS, R.string.title_confirm,
//						R.string.cancel);
				
				Intent updateIntent = new Intent(this, CoinInfoActivity.class);
				updateIntent.putExtra("info_type", HuPuRes.REQ_METHOD_CHANGE_MOBILE);
				startActivity(updateIntent);
				
			}
			break;
		}
	}

	/**
	 * setting nick name
	 */
	void switchToAccountAct() {
		Intent intent;
		intent = new Intent(this, NickNameActivity.class);
		startActivity(intent);
	}

	
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		switch (methodId) {
		case HuPuRes.REQ_METHOD_USER_LOGOUT:
			
			QuizCommitResp code = (QuizCommitResp) o;
			if (code.result == 0) {
				showToast("解绑失败");
			} else {
				SharedPreferencesMgr.setString("bp", "");
				SharedPreferencesMgr.setString("bp", "");
				SharedPreferencesMgr.setString("tk", null);
				SharedPreferencesMgr.setString("is_login", null);
				SharedPreferencesMgr.setString(HuPuRes.KEY_NICK_NAME, null);
				SharedPreferencesMgr.setString("hupu_name", "");
				this.mToken = null;
				this.uid = -1;
				btn_submit.setVisibility(View.INVISIBLE);
				showToast(getString(R.string.title_unbind_phone_success));
				findViewById(R.id.phone_arrow).setVisibility(View.VISIBLE);
				findViewById(R.id.layout_phone).setEnabled(true);
				txtNickName.setText(getString(R.string.title_unsett_nick));
				setBindInfo();
			}
		
			break;
		case HuPuRes.REQ_METHOD_USER_BIND:
//			if (o!=null) {
//				PhoneBindReq entity = (PhoneBindReq)o;
//				//HupuLog.e("papa", entity.token);
//				updateBindInfo(entity);
//			}
			setBindInfo();
			break;
		case HuPuRes.REQ_METHOD_USER_UNBIND:
			QuizCommitResp unbind = (QuizCommitResp) o;
			if (unbind.result == 0) {
				showToast("已绑定的帐号只有一个了，不能再解绑!");
			}else {
				//解绑成功后  缓存的值改变
				SharedPreferencesMgr.setInt("channel"+channel, 0);
				setBindInfo();
			}
			break;
		case HuPuRes.REQ_METHOD_USER_BIND_INFO:
			if (o != null) {
				BindInfoReq entity = (BindInfoReq)o;
				if (entity.isLogin == 0) {
					checkToken(entity.isLogin);
				}
				setBindInfo();
			}
			break;

		default:
			break;
		}

	}
	
	

	@Override
	public void onErrResponse(Throwable error, int type) {
		String content =error.toString();
		if(error instanceof HupuHttpException){
			showCustomDialog(DIALOG_ERROR,content,
					BaseGameActivity.ONE_BUTTON, R.string.title_confirm,R.string.title_confirm);	
		}
		
		qqBar.setVisibility(View.GONE);
		phoneBar.setVisibility(View.GONE);
		if (mToken == null) { //tonken 为null  说明均未绑定
			bindQQ.setVisibility(View.GONE);
			qqName.setText(getString(R.string.qq_login_text));
		}else {
			boolean isAllBind = (SharedPreferencesMgr.getInt("channel1", 0) == 1 && SharedPreferencesMgr.getInt("channel2", 0) == 1)?true:false;
			//只有一个绑定了视为登陆  只有另外一个可以出现绑定的按钮     通过对方是否绑定 决定自己是否显示  微博绑定加入后  逻辑需要变更
			if (!isAllBind) {
				bindQQ.setVisibility(SharedPreferencesMgr.getInt("channel1", 0) == 1 ? View.VISIBLE:View.GONE);
			}
			qqName.setText(SharedPreferencesMgr.getInt("channel2", 0) == 0 ? getString(R.string.qq_bind_text):"QQ ("+SharedPreferencesMgr.getString("qq_name", getString(R.string.qq_bind_text))+")");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		txtNickName.setText(SharedPreferencesMgr.getString(
				HuPuRes.KEY_NICK_NAME, "").equals("") ? this
				.getString(R.string.title_unsett_nick) : SharedPreferencesMgr
				.getString(HuPuRes.KEY_NICK_NAME, ""));
		setBindInfo();
		if (mToken != null) {
			getBindInfo();
		}
	}

	
	
	@Override
	public void sendQqEntity(QqLoginEntity entity, int channel) {
		// TODO Auto-generated method stub
		super.sendQqEntity(entity, channel);
		
		findViewById(R.id.layout_qq_login).setEnabled(false);
		findViewById(R.id.layout_phone).setEnabled(false);
		
		bindPhone.setVisibility(View.GONE);
		bindQQ.setVisibility(View.GONE);
		
		
		if(mToken == null){
			if (channel == 1) {
				phoneNum.setText(getString(R.string.login_loading));
				phoneBar.setVisibility(View.VISIBLE);
			}else if (channel == 2) {
				qqBar.setVisibility(View.VISIBLE);
				qqName.setText(getString(R.string.login_loading));
				findViewById(R.id.qq_arrow).setVisibility(View.GONE);
			}
		}
		else{
			
			if (channel == 1) {
				phoneBar.setVisibility(View.VISIBLE);
				phoneNum.setText(getString(R.string.bind_loading));
			}else if (channel == 2) {
				qqBar.setVisibility(View.VISIBLE);
				qqName.setText(getString(R.string.bind_loading));
			}
		}
	}

	/**
	 * binder phone
	 * 
	 */
	public void switchToPhoneBindAct() {
		Intent intent = new Intent(this, PhoneInputActivity.class);
		startActivityForResult(intent, REQ_GO_BIND_PHONE);
	}

	private void UnBind() {
		initParameter();
		mParams.put("token", mToken);
		String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_USER_LOGOUT, mParams,
				new HupuHttpHandler(this), false);
	}
	
	private void getBindInfo(){
		initParameter();
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_USER_BIND_INFO, mParams,
				new HupuHttpHandler(this), false);
	}
	
}
