/**
 * 
 */
package com.hupu.games.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.SSLKey;
import com.hupu.games.data.account.PhoneBindReq;
import com.hupu.games.data.account.PhoneVerfyCodeReq;
import com.hupu.http.HupuHttpException;
import com.hupu.http.HupuHttpHandler;

/**
 * @author panyongjun 手机绑定页面,分为第一次绑定和换绑定
 */
public class PhoneBindActivity extends BasePayActivity {

	EditText edtPhone;



	TextView txtErr,txt_send_verfy,txtMsg;
    TextView txt_title;
	String phone;


	String verfyCode;

	Button btnGetVerify,btn_go_bind;
	Context mcontext;
	
	boolean bChange;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_phone_bind);
		mcontext=this.getApplicationContext();
		phone=this.getIntent().getExtras().getString("phone");
		edtPhone = (EditText) findViewById(R.id.edt_phone);
		txtErr = (TextView) findViewById(R.id.txt_bind_err);
		txt_send_verfy= (TextView) findViewById(R.id.txt_send_verfy);
		btnGetVerify = (Button) findViewById(R.id.btn_get_verify);
		btn_go_bind = (Button) findViewById(R.id.btn_go_bind);
		txt_title= (TextView) findViewById(R.id.txt_title);
		txt_send_verfy.setText(phone.substring(0, 3)+"-"+phone.substring(3, 7)+"-"+phone.substring(7, phone.length()));
		reqVerify();
		txtErr.setText(R.string.bind_verfy_msg);
		//updateTime();
//		expire = 60;
		btnGetVerify.setEnabled(false);
		btnGetVerify.setBackgroundResource(R.drawable.btn_msg_bg);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_go_bind);
		setOnClickListener(R.id.btn_get_verify);
	}

	int expire;

	
	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {
		case DIALOG_ERROR:
			Intent errorIntent = new Intent(this,PhoneInputActivity.class);
			startActivity(errorIntent);
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		switch (methodId) {
		case HuPuRes.REQ_METHOD_GET_VERIFY_CODE:
			PhoneVerfyCodeReq code = (PhoneVerfyCodeReq) o;
			if (code.status == 0) {
				showToast("验证码获取失败");
				btnGetVerify.setEnabled(true);
				btnGetVerify.setBackgroundResource(R.drawable.btn_red2_selector);
			} else {
				updateTime();
				expire = code.expire;
			}
			break;
		case HuPuRes.REQ_METHOD_POST_PHONE_BIND:
			PhoneBindReq bind = (PhoneBindReq) o;
			//btn_go_bind.setEnabled(true);
			updateBindInfo(bind);
			//SharedPreferencesMgr.setBoolean("vip",bind.isVip==1?true:false);
//			Intent p = new Intent();
//			p.putExtra("phone", phone);
//			setResult(RESULT_OK, p);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() { 
					finish();
				}
			}, 1000);
			break;
		}
	}

	@Override
	public void onErrResponse(Throwable error, int type) {
		//btn_go_bind.setEnabled(true);
		String content =error.toString();
		if(error instanceof HupuHttpException){
			showCustomDialog(DIALOG_ERROR,content,
					BaseGameActivity.ONE_BUTTON, R.string.title_confirm,R.string.title_confirm);	
		}else {
			showToast(getString(R.string.http_error_str));
		}
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
//			Intent p = new Intent();
//			setResult(RESULT_OK, p);
			finish();
			break;
		case R.id.btn_go_bind:
			if (phone == null && phone.length() < 10) {
				showToast("请输入手机号");
				return;
			}
			verfyCode = edtPhone.getEditableText().toString();
			if (verfyCode != null && verfyCode.length() > 1){
				reqBind();
			}
			else{
				showToast("请输入验证码");
			}
			break;
		case R.id.btn_get_verify:
			//verfyCode = edtPhone.getEditableText().toString();
			if (phone != null && phone.length() > 1) {
				reqVerify();
				btnGetVerify.setEnabled(false);
				btnGetVerify.setBackgroundResource(R.drawable.btn_msg_bg);
			} else
				showToast("请输入手机号");
			break;
		}
	}

	void reqVerify() {
		
		initParameter();
		mParams.put("mobile", phone);
		String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_GET_VERIFY_CODE, mParams,
				new HupuHttpHandler(this), false);
	}

	void reqBind() {
		
		initParameter();
		//btn_go_bind.setEnabled(false);
		btnGetVerify.setBackgroundResource(R.drawable.btn_msg_bg);
		mParams.put("mobile", phone);
		mParams.put("code", verfyCode);
		String sign = SSLKey.getSSLSign(mParams, SharedPreferencesMgr.getString("sugar", ""));//salt 值由init中的sugar给出。必须要有的。
		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_POST_PHONE_BIND, mParams,
				new HupuHttpHandler(this), false);
	}

	Handler handler;
	UpdateTime run;

	void updateTime() {
		if (handler == null)
			handler = new Handler();
		run = new UpdateTime();
		handler.postDelayed(run, 1000);
	}

	class UpdateTime implements Runnable {
		public void run() {
			if (expire > 0) {
				expire--;
			}
			if (expire == 0) {
				handler.removeCallbacks(this);
				run = null;
				btnGetVerify.setEnabled(true);
				btnGetVerify.setBackgroundResource(R.drawable.btn_red2_selector);
				btnGetVerify.setShadowLayer(1, 0, 1, R.color.res_cor2);
				txtErr.setText("");
			} else {
				btnGetVerify.setBackgroundResource(R.drawable.btn_msg_bg);
				btnGetVerify.setEnabled(false);
				btnGetVerify.setShadowLayer(0, 0, 0, R.color.btn_msg_color);
				txtErr.setText("您将在" + expire + "秒内收到验证码");
				handler.postDelayed(this, 1000);
			}
		}
	}
	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (handler != null && run != null) {
			handler.removeCallbacks(run);
		}
	}
	


}
