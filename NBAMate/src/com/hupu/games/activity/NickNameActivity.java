/**
 * 
 */
package com.hupu.games.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.NickNameEntity;
import com.hupu.http.HupuHttpHandler;

/**
 * @author papa
 * 用户昵称设置
 */
public class NickNameActivity extends HupuBaseActivity {

	private EditText edtName;
	private String userName;
	private String reqName;
	InputMethodManager inputMethodManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		setContentView(R.layout.layout_nick_name);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_save);
		edtName =(EditText)findViewById(R.id.edt_nick_name);
		userName =SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "");
		
		
		if(userName.length()<3)
		{
			findViewById(R.id.txt_notify_nick).setVisibility(View.VISIBLE);			
		}
		else
			edtName.setText(userName);
//		edtName.moveCursorToVisibleOffset();
		edtName.setSelection(userName.length());
		showKeyBoard(edtName);
		
		((TextView)findViewById(R.id.txt_notify_nick)).setText(SharedPreferencesMgr.getString("accountNicknameIntro", getString(R.string.notity_nick_hint)));
		((TextView)findViewById(R.id.nikename_tips)).setText(SharedPreferencesMgr.getString("myAccountNicknameTips", getString(R.string.nick_hint)));
	}

	
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		
		if(methodId == HuPuRes.REQ_METHOD_SET_NICK_NAME)
		{
			NickNameEntity name =(NickNameEntity)o;
			if(name.name!=null&&name.name.equals("1"))
			{
				SharedPreferencesMgr.setString(HuPuRes.KEY_NICK_NAME, reqName);
				getIntent().putExtra("username", reqName);
				setResult(RESULT_OK, getIntent());
				finish();
			}			
		}
				
	}

	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		
		switch(id)
		{
		case R.id.btn_back:
			back();
			break;
		case R.id.btn_save:
			String ss = edtName.getEditableText().toString();
			if (ss != null) {
				
				ss=ss.trim();
				int length = ss.length();
				if (length < 3) {
//					showToast("昵称必须大于2个字");
					showToast("昵称长度应在3至10个字符");					
				} else if (length > 10) {
//					showToast("昵称必须小于10个字");
					showToast("昵称长度应在3至10个字符");
				} else {
					if (isRight(ss)) {
						if(userName.equals(ss) )
						{
							showToast("昵称正在使用");
							return;
						}
						else
						{
							reqName = ss;	
							//req http
							setNickName(reqName);
						}
						
					} else {
						showToast("只能输入汉字、字母、数字");
					}
				}
			}
			break;
		}
	}

	private void setNickName(String name)
	{
		initParameter();
		mParams.put("nickname", name);
		sendRequest(HuPuRes.REQ_METHOD_SET_NICK_NAME, mParams,
				new HupuHttpHandler(this), false);
//		 showDialog(DialogRes.DIALOG_ID_NET_CONNECT);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {		
			back();
			return true;
		}		
		return false;
	}
	
	private void back()
	{
		inputMethodManager.hideSoftInputFromWindow(
				edtName.getWindowToken(), 0);
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/** 名字是否只包含数字，字母，汉字 */
	public boolean isRight(String str) {
		char[] chars = str.toCharArray();
		char c;
		for (int i = 0; i < chars.length; i++) {
			c = chars[i];
			if (isGB(c) || isChar(c) || isNumeric(c))
				continue;
			return false;
		}
		return true;
	}

	/*** 判断是否为汉字 */
	public static boolean isGB(char c) {
		// byte[] bytes = ("" + c).getBytes();
		// if (bytes.length == 2) {
		// int[] ints = new int[2];
		// ints[0] = bytes[0] & 0xff;
		// ints[1] = bytes[1] & 0xff;
		// if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
		// && ints[1] <= 0xFE) {
		// return true;
		// }
		// }
		// return false;
		int v = (int) c;
		if (v >= 19968 && v <= 171941) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isChar(char c) {
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNumeric(char c) {
		if (c < 48 || c > 57)
			return false;
		return true;

	}
	
	private void showKeyBoard(View view)
	{
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
	}
	
}
