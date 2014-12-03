/**
 * 
 */
package com.hupu.games.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.hupu.games.R;
import com.hupu.games.activity.ContactsActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.http.HupuHttpHandler;

/**
 * @author papa 
 */


public class HupuUserBindActivity extends BasePayActivity {
	//	Intent intent;
	Bundle bundle;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hupu_user_bind);
		setOnClickListener(R.id.btn_yes);
		setOnClickListener(R.id.btn_no);
		setOnClickListener(R.id.btn_contacts);
		bundle = this.getIntent().getExtras();
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		switch (dialogId) {
		case DIALOG_RENOUNCE_BIND:
			if (mToken != null) {
				unbind();
			}else {
				Intent intent = new Intent(this, HupuHomeActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}
	private void unbind(){
		initParameter();
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_POST_REGISTER_PASSPORT, mParams, new HupuHttpHandler(this), false);

		/*		
		intent = new Intent(this, HupuHomeActivity.class);
		startActivity(intent);
		finish();*/
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			HupuScheme mScheme = (HupuScheme) bundle.getSerializable("scheme");
			boolean isInit = bundle.getBoolean("isInit");
			if(isInit){//true第一次登陆，false某个功能处登陆
				Intent intent2 = new Intent(this, HupuHomeActivity.class);
				if (mScheme != null) {
					intent2.putExtra("scheme", mScheme);
				}
				startActivity(intent2);
			}
			finish();

			/*if (requestCode == REQ_GO_CHARGE) {
				if (data != null)
					onChargeSuccess(data.getIntExtra("success", 1) == 1 ? true
							: false);

			}
			 */
		}
	}
	@Override
	public void onReqResponse(Object o, int methodId) {
		// TODO Auto-generated method stub
		super.onReqResponse(o, methodId);
		if (o!=null) {
			QuizCommitResp entity = (QuizCommitResp) o;
			if (entity.result == 1) {				//注册成功
				
				Intent intent = new Intent(this, HupuUserBindTipsActivity.class);
				Bundle bd = new Bundle();
				bd.putInt("tipsfrom",  HupuUserBindTipsActivity.BIND_TIPS_FROM_UNBIND);//不绑定
				intent.putExtras(bd);
				startActivityForResult(intent,0);
			}
		}
		this.hideLoadingDialog();
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_yes:
			Intent intent = new Intent(this, HupuUserLoginActivity.class);
		   intent.putExtra("isBind", true);
			startActivityForResult(intent,0);
			//			finish();
			break;
		case R.id.btn_no:
			this.showLoadingDialog();
			unbind();
			break;
		case R.id.btn_contacts:
			Intent contact = new Intent(this, ContactsActivity.class);
			startActivity(contact);
			break;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			unbind();
		}
		return false;
	}

}
