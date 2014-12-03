/**
 * 
 */
package com.hupu.games.hupudollor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.hupudollor.PayHupuDollorCallBack;
import com.hupu.games.hupudollor.data.OrderHupuDollorPacEntity;
import com.hupu.games.pay.PayCallBack;

/**
 * @author zhenhua
 * 
 */
public class PayHupuDollorDialog extends Dialog {

	Context mContext;
	PayHupuDollorCallBack mClick;
	OrderHupuDollorPacEntity entity;
	TextView txt_pay;
	LinearLayout lay_pay_channel;
    String[] chanelArr;
	public PayHupuDollorDialog(Context context, PayHupuDollorCallBack click,
			OrderHupuDollorPacEntity entity) {
		super(context, R.style.MyWebDialog);
		mContext = context;
		mClick = click;
		this.entity = entity;
		initView();
	}

	private void initView() {
		View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_pay,
				null);
		lay_pay_channel = (LinearLayout) v.findViewById(R.id.lay_pay_channel);
		txt_pay = (TextView) v.findViewById(R.id.txt_pay);

		txt_pay.setText(entity.recharge+"元兑换"+entity.total+"虎扑币");

		chanelArr = entity.channel.split(",");
		for (int i = 0; i < chanelArr.length; i++) {
			View payItem = LayoutInflater.from(mContext).inflate(
					R.layout.item_pay_dialog, null);
			ImageView img_pay = (ImageView) payItem.findViewById(R.id.img_pay);
			TextView txt_pay_title = (TextView) payItem
					.findViewById(R.id.txt_pay_title);
			TextView txt_pay_des = (TextView) payItem
					.findViewById(R.id.txt_pay_des);
			if (chanelArr[i].toString().trim().equals("alipay_app")) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				param.topMargin = 5;
				img_pay.setBackgroundResource(R.drawable.icon_alipay);
				txt_pay_title.setText(R.string.title_pay_alipay);
				txt_pay_des.setText(R.string.title_pay_alipay_des);
				payItem.setTag(i);
				lay_pay_channel.addView(payItem, param);
			} else if (chanelArr[i].toString().trim().equals("alipay_wap")) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				param.topMargin = 5;
				img_pay.setBackgroundResource(R.drawable.icon_alipay);
				txt_pay_title.setText(R.string.title_pay_wap);
				txt_pay_des.setText(R.string.title_pay_alipay_des);
				payItem.setTag(i);
				lay_pay_channel.addView(payItem, param);
			}
			else if (chanelArr[i].toString().trim()
					.equals("alipay_creditcard")) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				param.topMargin = 5;
				img_pay.setBackgroundResource(R.drawable.icon_visa);
				txt_pay_title.setText(R.string.title_pay_visa);
				txt_pay_des.setText(R.string.title_pay_visa_des);
				payItem.setTag(i);
				lay_pay_channel.addView(payItem, param);
			} else if (chanelArr[i].toString().trim()
					.equals("alipay_debitcard")) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				param.topMargin = 5;
				img_pay.setBackgroundResource(R.drawable.icon_unionpay);
				txt_pay_title.setText(R.string.title_pay_unionpay);
				txt_pay_des.setText(R.string.title_pay_unionpay_des);
				payItem.setTag(i);
				lay_pay_channel.addView(payItem, param);
			} else if (chanelArr[i].toString().trim().equals("weixin")) {
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				param.topMargin = 5;
				img_pay.setBackgroundResource(R.drawable.icon_weixin);
				txt_pay_title.setText(R.string.title_pay_weixin);
				txt_pay_des.setText(R.string.title_pay_weixin_des);
				payItem.setTag(i);
				lay_pay_channel.addView(payItem, param);
			}
			payItem.setOnClickListener(new RadiobuttonListener());
		}
		setContentView(v);
		getWindow().setGravity(Gravity.CENTER);
	}

	/**
	 * 显示对话框
	 * */
	public void goShow() {
		show();
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	

	private class RadiobuttonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
//			int id = v.getId();
			Integer tag=(Integer) v.getTag();
			mClick.onPayListener(PayHupuDollorDialog.this,entity,chanelArr[tag]);
		}
	}
}
