/**
 * 
 */
package com.hupu.games.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.ContactsActivity;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.MD5Util;
import com.hupu.games.data.account.PhoneBindReq;
import com.hupu.http.HupuHttpHandler;

/**
 * @author papa 
 */
public class HupuUserLoginActivity extends BasePayActivity {
	private TextView userNameTxt,passwordTxt;
	Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hupu_user_logo);
		userNameTxt = (TextView) findViewById(R.id.username_text);
		passwordTxt = (TextView) findViewById(R.id.password_text);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_submit);
		setOnClickListener(R.id.btn_contacts);
		intent = getIntent();
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			onComplete();
			break;
		case R.id.btn_submit:
//			progressbar.setVisibility(View.VISIBLE);
			showLoadingDialog();
			sendUserInfo();
//			 onResponse();//debug
			break;
		case R.id.btn_contacts:
			Intent contact = new Intent(this, ContactsActivity.class);
			startActivity(contact);
			break;
		}
	}
	
	private void getDialog(){
		showCustomDialog(DIALOG_RENOUNCE_BIND,this.getResources().getString(R.string.dialog_hupu_bind_giveup_tips),
				BaseGameActivity.TOW_BUTTONS, R.string.dialog_hupu_bind_renounce,
				R.string.cancel);
	}
	
	private void onComplete(){
		if (intent.getBooleanExtra("isBind", false)) {
			getDialog();
		}else {
			finish();
		}
	}
	boolean bindsuccess;//绑定成功，避免网络数据多次返回导致多次显示成功页面
	private void onResponse(){
		if (intent.getBooleanExtra("isBind", false)) {
			if(bindsuccess){
				return;
			}
			bindsuccess=true;
//			intent = new Intent(this, HupuHomeActivity.class);
//			startActivity(intent);
			Intent intent = new Intent(this, HupuUserBindTipsActivity.class);
			Bundle bd = new Bundle();
			bd.putInt("tipsfrom",  HupuUserBindTipsActivity.BIND_TIPS_FROM_SUCCESS);//绑定成功
			intent.putExtras(bd);
			startActivityForResult(intent,0);
		}else {
			finish();
		}
		this.hideLoadingDialog();
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			this.setResult(RESULT_OK);
			finish();
		}
	}
	
	private void postRegister(){
		
		intent = new Intent(this, HupuHomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {
		case DIALOG_RENOUNCE_BIND:
//			finish();
			unbind();
			break;

		default:
			break;
		}
	}
	private void unbind(){
		initParameter();
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_POST_REGISTER_PASSPORT, mParams, new HupuHttpHandler(this), false);
		
		
		Intent intent = new Intent(this, HupuUserBindTipsActivity.class);
		Bundle bd = new Bundle();
		bd.putInt("tipsfrom",  HupuUserBindTipsActivity.BIND_TIPS_FROM_UNBIND);//不绑定
		intent.putExtras(bd);
		startActivityForResult(intent,0);
	}
	@Override
	public void onReqResponse(Object o, int methodId) {
		// TODO Auto-generated method stub
		super.onReqResponse(o, methodId);
		if (o!=null) {
			switch (methodId) {
			case HuPuRes.REQ_METHOD_POST_LOGIN_EMAIL:
				if (intent.getBooleanExtra("isBind", false)) {//绑定
					
				}else {//登陆
					PhoneBindReq bind = (PhoneBindReq) o;
					updateBindInfo(bind);
				/*	new Handler().postDelayed(new Runnable() {
						@Override
						public void run() { 
							onResponse();
						}
					}, 1000);*/
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() { 
						onResponse();
					}
				}, 1000);
				
				
				
				break;

			default:
				break;
			}
			
		}
	}
	
	
	private void sendUserInfo(){
		initParameter();
		mParams.put("username", userNameTxt.getText().toString());
		mParams.put("password", MD5Util.md5(passwordTxt.getText().toString()).toLowerCase());
		sendRequest(HuPuRes.REQ_METHOD_POST_LOGIN_EMAIL, mParams, new HupuHttpHandler(this), false);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			onComplete();
		}
		return false;
	}

}
