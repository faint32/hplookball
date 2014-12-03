/**
 * 
 */
package com.hupu.games.casino;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.pay.HupuUserLoginActivity;
import com.hupu.games.pay.PayCallBack;
import com.hupu.games.pay.PhoneInputActivity;
import com.tencent.tauth.Tencent;

/**
 * @author papa
 * 
 */
public class AuthDialog extends Dialog {

	Context mContext;
	HupuBaseActivity mAct;
	LinearLayout lay_qq_channel, lay_phone_channel,lay_hupu_channel;
	TextView titleView,unBind;
	String DialogTitleStr;
	channelListener listener;
	public Tencent mTencent;

	public AuthDialog(Context context, HupuBaseActivity act, String title) {
		super(context, R.style.MyWebDialog);
		mContext = context;
		mAct = act;
		DialogTitleStr = title;
		initView();
	}

	private void initView() {
		View v = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_authorize, null);
		lay_qq_channel = (LinearLayout) v.findViewById(R.id.auth_qq_layout);
		lay_phone_channel = (LinearLayout) v.findViewById(R.id.auth_phone_layout);
		lay_hupu_channel = (LinearLayout) v.findViewById(R.id.auth_hupu_layout);
		unBind = (TextView) v.findViewById(R.id.un_login);
		unBind.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		lay_qq_channel.setOnClickListener(new channelListener());
		lay_phone_channel.setOnClickListener(new channelListener());
		lay_hupu_channel.setOnClickListener(new channelListener());
		unBind.setOnClickListener(new channelListener());
		titleView = (TextView) v.findViewById(R.id.txt_explanation);
		titleView.setText(DialogTitleStr);
		setContentView(v);

		WindowManager m = mAct.getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高

		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		//p.height = (int) (d.getHeight() * 1.0); // 高度设置为屏幕的1.0
		p.width = (int) (d.getWidth() * 0.92); // 宽度设置为屏幕的0.92
		// p.alpha = 1.0f; //设置本身透明度
		// p.dimAmount = 0.0f; //设置黑暗度

		getWindow().setAttributes(p); // 设置生效
		getWindow().setGravity(Gravity.CENTER);
	}

	/**
	 * 显示对话框
	 * */
	public void goShow() {
		show();
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	private class channelListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.auth_qq_layout:
				mTencent = Tencent.createInstance(mAct.mApp.QQ_APP_ID,
						mContext.getApplicationContext());
				mAct.onClickLogin(mTencent);
				dismiss();
				break;

			case R.id.auth_phone_layout:
				dismiss();
				Intent intent = new Intent(mContext, PhoneInputActivity.class);
				mAct.startActivityForResult(intent, 3333);
				dismiss();
				break;
				
			case R.id.auth_hupu_layout:
				Intent bindIntent = new Intent(mContext, HupuUserLoginActivity.class);
//				bindIntent.putExtra("isInit", false);
				bindIntent.putExtra("isBind", false);
				mAct.startActivity(bindIntent);
				dismiss();
				break;
			case R.id.un_login:
				dismiss();
				break;

			default:
				break;
			}
		}
	}
}
