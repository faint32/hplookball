package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.ContactsEntity;
import com.hupu.http.HupuHttpHandler;

public class ContactsActivity extends HupuBaseActivity {

	TextView txt_contact;
	Button btn_contact;
	String qq_val;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contacts);
		txt_contact = (TextView) this.findViewById(R.id.txt_contact);
		btn_contact=(Button)this.findViewById(R.id.btn_contact);
		btn_contact.setEnabled(false);
		btn_contact.setVisibility(View.GONE);
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_contact);
		reqBalance();
	}

	@SuppressLint("NewApi")
	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_contact:
			txtCopy(qq_val);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onStop();
	}

	void txtCopy(String txt) {
		ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setText(txt);
		showToast("已成功复制QQ号，现在就去添加 QQ 号联系客服吧");
	}

	
	
	
	
	@Override
	public void onReqResponse(Object o, int methodId) {
		// TODO Auto-generated method stub
		super.onReqResponse(o, methodId);
		if(methodId==HuPuRes.REQ_METHOD_GET_CONTACTS){
			btn_contact.setEnabled(true);
			ContactsEntity entity=(ContactsEntity) o;
			txt_contact.setText(Html.fromHtml(entity.msg.toString()));
			qq_val=entity.num;
			btn_contact.setVisibility(View.VISIBLE);
		}
	}

	/** 获取客服信息 */
	void reqBalance() {
		initParameter();
		sendRequest(HuPuRes.REQ_METHOD_GET_CONTACTS, null,
				new HupuHttpHandler(this), false);
	}
}
