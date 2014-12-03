/**
 * 
 */
package com.hupu.games.pay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.common.HuPuRes;

/**
 * @author papa 
 */
public class PhoneInputActivity extends HupuBaseActivity {

	private TextView edt_nick_name;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_phone_input);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_next);
		edt_nick_name=(TextView) this.findViewById(R.id.edt_nick_name);
	}

	

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			
			finish();
			break;
		case R.id.btn_next:
			
			switchToPhoneBindAct();
			break;
		}
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == REQ_GO_BIND_PHONE) {
//			// 绑定手机
//			if (resultCode == RESULT_OK) {
//				this.finish();
//			}
//		} 
//	}
	/**
	 * binder phone
	 * 
	 */
	void switchToPhoneBindAct() {
		if(!edt_nick_name.getText().toString().trim().equals("")){
			if (isPhoneNumberValid(edt_nick_name.getText().toString())) {
				phoneDialog(this,edt_nick_name.getText().toString());
			}else
				showToast(getString(R.string.error_phone_number));
		}else{
			showToast(getString(R.string.no_phone_number));
		}
	}
	
	private void phoneDialog(final Context mContext,String number){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("确认手机号");
		builder.setMessage(number+"\n\r\n\r"+getString(R.string.verify_phone_number));
		builder.setPositiveButton(getString(R.string.cancel), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton(R.string.title_confirm, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				dialog.dismiss();
				Intent intent = new Intent(mContext, PhoneBindActivity.class);
				intent.putExtra("phone", edt_nick_name.getText().toString().trim());
				startActivity(intent);
				finish();
			}
		});
		
		builder.create().show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return false;
	}
	public static boolean isPhoneNumberValid(String phoneNumber)  
	{  
	   boolean isValid = false;  
	  
	   String expression = "^[1][0-9]{10}$";  
	  
	   String expression2 ="^[1][0-9]{10}$";  
	  
	   CharSequence inputStr = phoneNumber;  
	  
	   Pattern pattern = Pattern.compile(expression);  
	  
	   Matcher matcher = pattern.matcher(inputStr);  
	  
	   Pattern pattern2 =Pattern.compile(expression2);  
	  
	   Matcher matcher2= pattern2.matcher(inputStr);
	   if(matcher.matches()||matcher2.matches())  
	   {  
	   isValid = true;  
	   }  
	   return isValid;   
	 } 

}
