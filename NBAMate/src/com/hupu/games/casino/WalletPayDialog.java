/**
 * 
 */
package com.hupu.games.casino;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.OrderPacEntity;

/**
 * @author papa
<<<<<<< .mine
 * Ǯ������𶹽ӿ�
=======
 * 钱包购买金币接口
>>>>>>> .r1416
 * 
 */
public class WalletPayDialog extends Dialog {

	Context mContext;
	View.OnClickListener mClick;
	OrderPacEntity entity;
	TextView txt_pay;
	Button btnCancel;
	public WalletPayDialog(Context context, View.OnClickListener click,
			OrderPacEntity entity) {
		super(context, R.style.MyWebDialog);
		mContext = context;
		mClick = click;
		this.entity = entity;
		initView();
	}
	public void init(View.OnClickListener click,
			OrderPacEntity entity){
		mClick = click;
		this.entity = entity;
		initView();
	}

	private void initView() {
		View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_wallet_pay,
				null);
		txt_pay = (TextView) v.findViewById(R.id.txt_pay);

		txt_pay.setText("确认使用"+entity.recharge+"元兑换"+entity.total+"金豆 ?");

		v.findViewById(R.id.btn_confirm).setOnClickListener(mClick);
		v.findViewById(R.id.btn_cancel).setOnClickListener(mClick);
		setContentView(v);
		getWindow().setGravity(Gravity.CENTER);
	}
	
	public OrderPacEntity getWalletOrder(){
		return entity;
	}
	
	

	public OrderPacEntity getEntity() {
		return entity;
	}

	public void setEntity(OrderPacEntity entity) {
		this.entity = entity;
	}

	/**
	 * 显示对话框
	 * */
	public void goShow() {
		show();
		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

}
