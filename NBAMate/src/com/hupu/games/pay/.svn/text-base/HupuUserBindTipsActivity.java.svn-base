package com.hupu.games.pay;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.hupu.games.R;

/**
 * @author songzhenhua 
 */


public class HupuUserBindTipsActivity extends BasePayActivity {
	public final static int BIND_TIPS_FROM_UNBIND=0;
	public final static int BIND_TIPS_FROM_SUCCESS=1;
	TextView tipTv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hupu_user_unbind);
		setOnClickListener(R.id.btn_yes);
		tipTv = (TextView)findViewById(R.id.txt_tips);
		Bundle bd = this.getIntent().getExtras();
		if(bd != null){
			int tips = bd.getInt("tipsfrom");
			if(tips == BIND_TIPS_FROM_UNBIND){
				tipTv.setText(R.string.hupu_unbind_tips);
			}else if(tips == BIND_TIPS_FROM_SUCCESS){
				tipTv.setText(R.string.hupu_bind_success_tips);
			}
		}
	}
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_yes:
			this.setResult(RESULT_OK);
			finish();
			break;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			this.setResult(RESULT_OK);
			finish();
		}
		return false;
	}
}
