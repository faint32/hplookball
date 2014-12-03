package com.hupu.games.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.hupu.games.R;


/**
 * @author songzhenhua
 *
 * TODO礼物相关提醒
 */
public class GiftTipsDialog extends Dialog {

	private Context mContext;
	
	private TextView txt_pay;
	
	private View.OnClickListener onClickListener;
	
	private String content;
	
	private int type;
	
	public static final int ONE_OK_BTN = 1;
	
	public static final int ONE_CANCLE_BTN = 2;
	
	public static final int DEFAULT = 0;
	
	public static final int BTN_OK_ID = R.id.btn_confirm;
	
	public static final int BTN_CANCLE_ID = R.id.btn_cancel;
	
	private View view;
	
	public GiftTipsDialog(Context context)
	{
		super(context, R.style.MyWebDialog);
		view= LayoutInflater.from(context).inflate(R.layout.dialog_wallet_pay,
				null);
	}
	
	public GiftTipsDialog(Context context,View.OnClickListener onClickListener,String content,int type) {
		super(context, R.style.MyWebDialog);
		this.mContext = context;
		this.onClickListener = onClickListener;
		this.content = content;
		this.type = type;
		view= LayoutInflater.from(mContext).inflate(R.layout.dialog_wallet_pay,
				null);
		initView();
	}

	public void initContentView(View.OnClickListener onClickListener,String content,int type)
	{
		this.onClickListener = onClickListener;
		this.content = content;
		this.type = type;

		initView();
	}
	public void initData(String content,int type)
	{
		this.content = content;
		this.type = type;
		txt_pay.setText(content);
	}
	public void initBtn(int type,String btn_content) {
		if(type==ONE_OK_BTN)
		{
			Button btn_ok1 = (Button) view.findViewById(R.id.btn_confirm);
			btn_ok1.setText(btn_content);
		}
		else if(type==ONE_OK_BTN)
		{
			Button btn_ok2 = (Button) view.findViewById(R.id.btn_cancel);
			btn_ok2.setText(btn_content);
		}
		
	}
	
	public void initBtn(String btn_ok,String btn_cancle) {

		Button btn_ok1 = (Button) view.findViewById(R.id.btn_confirm);
		btn_ok1.setText(btn_ok);
		Button btn_ok2 = (Button) view.findViewById(R.id.btn_cancel);
		btn_ok2.setText(btn_cancle);

	}
	
	private void initView() {

		txt_pay = (TextView) view.findViewById(R.id.txt_pay);

		txt_pay.setText(content);

		Button btn_ok = (Button) view.findViewById(R.id.btn_confirm);
		Button btn_cancle = (Button) view.findViewById(R.id.btn_cancel);
		
		if(type==DEFAULT)
		{
			btn_ok.setVisibility(View.VISIBLE);
			btn_ok.setOnClickListener(onClickListener);
			
			btn_cancle.setVisibility(View.VISIBLE);
			btn_cancle.setOnClickListener(onClickListener);
		}

		setContentView(view);
		getWindow().setGravity(Gravity.CENTER);
	}

	/**
	 * 显示对话框
	 * */
	@SuppressWarnings("deprecation")
	public void goShow() {
		show();
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
	}
}
