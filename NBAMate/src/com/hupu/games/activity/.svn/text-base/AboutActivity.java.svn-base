package com.hupu.games.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.hupu.games.R;

public class AboutActivity extends HupuBaseActivity {

	TextView txt_versition;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_set_about);
		setOnClickListener(R.id.btn_back);
		txt_versition = (TextView)findViewById(R.id.txt_version);
		txt_versition.setText("版本："+mApp.getVerName());
	}

	@SuppressLint("NewApi")
	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		}
	}
}
