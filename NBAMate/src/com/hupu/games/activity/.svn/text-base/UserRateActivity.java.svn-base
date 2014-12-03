package com.hupu.games.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;

/**
 * 用户给球员评分页面
 * @author panyongjun
 * */

public class UserRateActivity extends HupuBaseActivity {

	private String playerName;
	
	private EditText edtMsg;
	
	private int size = 140;
	
	private String content;

	private TextView txtNum;
	
	private TextView txtName;

	private RatingBar ratingBar;
	
	private String userName;
	
	private int index;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_rate);
		userName = SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "");
		
		playerName =getIntent().getStringExtra("name");
		i_oid=getIntent().getIntExtra("oid", 0);
		index=getIntent().getIntExtra("index", -1);

		
		Log.d("UserRateActivity", "i_oid="+i_oid+" ;index="+index);
		
		txtName =(TextView)findViewById(R.id.txt_player_name);
		txtName.setText("给"+playerName+"评分：");
		
		txtNum = (TextView) findViewById(R.id.txt_nums);		
		ratingBar =(RatingBar)findViewById(R.id.ratingBar);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				txtName.setText("给"+playerName+"评分："+(int)rating+"分");
				
			}
		});
		
		edtMsg = (EditText) findViewById(R.id.edt_rating_text);		
		edtMsg.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2,
					int arg3) {
				size = 140 - (int) calculateWeiboLength(c);
				txtNum.setText(size + "字");
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	
		if (userName .length()<3) {
			//注册nickname
			switchToNickNameAct();
		} else {
			showKeyBoard(edtMsg);
		}
		
		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_send);
		
	
	}

	/**
	 * 注册昵称
	 * */
	private void switchToNickNameAct()
	{
		Intent intent =new Intent(this,NickNameActivity.class);
		startActivityForResult(intent, 5555);
	}
	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_send:
			if (userName == null)
				switchToNickNameAct();
			else
				sent();
			break;
		}
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode ==5555)
		{
			//有名称返回
			if(resultCode == RESULT_OK)
				userName = data.getStringExtra("username");
			else if(resultCode == RESULT_CANCELED)
				finish();
		}
	}

	private int i_oid;
	private void sent() {
		content = edtMsg.getEditableText().toString();
		int r =(int)ratingBar.getRating();
		if (size < 0) {
			showToast("字数超过140字");
		} else if(r<1)
		{
			showToast("请点击星星选择评分");
		}
		else {
			Intent data = new Intent();
			data.putExtra("index", index);
			data.putExtra("oid", i_oid);
			data.putExtra("rating", r);
			data.putExtra("desc", content.trim());
			setResult(RESULT_OK, data);
			finish();
		}

	}

	/**
	 * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
	 * 
	 * @param c
	 *            所要统计的字符序列
	 * @return 返回字符序列计算的长度
	 */
	public static long calculateWeiboLength(CharSequence c) {

		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int temp = (int) c.charAt(i);
			if (temp > 0 && temp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	
	
	private void showKeyBoard(View view)
	{
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
	}
}
