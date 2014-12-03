package com.hupu.games.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.ViewPagerAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BuyItemEntity;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.pay.HupuOrderActivity;
import com.hupu.http.HupuHttpHandler;

/**
 * @author panyongjun 发送热线，当然也能发表情
 */
public class ChatInputActivity extends BasePayActivity {

	private String userName;
	/** 输入的文字信息编辑栏 */
	private EditText edtMsg;

	private int size = 140;
	/** 输入的文字 */
	private String content;
	/** 输入的文字数目 */
	private TextView txtNum;

	private String TAG;
	/** 翻页控件 */
	private ViewPager pager;
	/** 表情显示的图片控件 */
	private ImageView imgEmoji;

	LayoutInflater mInflater;
	/** 所有的表情的ids */
	int[] emoji_ids;
	int[] emoji_ids_1;
	/** 表情的页面数 */
	int pageSize;
	/** 每页最大显示的表情数 */
	final int MAX_PRE_PAGE = 8;
	/** 表情网格视图列表 */
	List<View> viewList;
	/** pager适配器 */
	ViewPagerAdapter pagerAdater;
	/** 选择的表情索引值 */
	private int emojiIndex;

	LinearLayout layoutIndicator;

	private int padding;

	private int inputMode;

	TextView tips;

	private int lastIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chat_input);
		TAG = getIntent().getStringExtra("tag");
		roomid = getIntent().getIntExtra("roomid", -1);
		userName = SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "");

		edtMsg = (EditText) findViewById(R.id.edt_msg);
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
		txtNum = (TextView) findViewById(R.id.txt_num);

//		pager = (ViewPager) findViewById(R.id.emoj_pager);
//		imgEmoji = (ImageView) findViewById(R.id.img_emoji);

		//initEmojiView();

		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.btn_sent);


		if (userName.length() < 3) {
			// 注册nickname
			switchToNickNameAct();
		} else {
			showKeyBoard(edtMsg);
		}
		mApp.doPost = false;
	}

	void switchToNickNameAct() {
		Intent intent = new Intent(this, NickNameActivity.class);
		startActivityForResult(intent, 5555);
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_sent:
			if (userName == null)
				switchToNickNameAct();
			else
				sent();
			break;
		
		}
	}

	/** 初始化表情 */
//	private void initEmojiView() {
//
//		if (!SharedPreferencesMgr.getBoolean("guide_emoji", false)) {
//			SharedPreferencesMgr.setBoolean("guide_emoji", true);
//			tips = (TextView) findViewById(R.id.txt_tips);
//			if (SharedPreferencesMgr.getInt("show_vip", 0) == 1) {
//				// 初次教程停留5秒
//				new Handler().postDelayed(new Runnable() {
//
//					@Override
//					public void run() {
//						if (tips != null)
//							tips.setVisibility(View.GONE);
//					}
//				}, 5000);
//				tips.setVisibility(View.VISIBLE);
//			} else {
//				tips.setVisibility(View.GONE);
//			}
//		}
//
//		Resources res = getResources();
//		String ss[] = res.getStringArray(R.array.emoji_list);
//		String ss_1[] = res.getStringArray(R.array.emoji_list_1);
//		emoji_ids = new int[ss.length];
//		emoji_ids_1 = new int[ss.length];
//		String pack = getPackageName();
//		for (int i = 0; i < ss.length; i++) {
//			// 反射
//			emoji_ids[i] = res.getIdentifier(ss[i], "drawable", pack);
//			emoji_ids_1[i] = res.getIdentifier(ss_1[i], "drawable", pack);
//		}
//		pageSize = emoji_ids.length / MAX_PRE_PAGE
//				+ (emoji_ids.length % MAX_PRE_PAGE == 0 ? 0 : 1);
//		// Log.d("initEmojiView",
//		// "pageSize="+pageSize+" draw0="+R.drawable.emoji_bobo);
//		mInflater = LayoutInflater.from(this);
//		viewList = new ArrayList<View>();
//		layoutIndicator = (LinearLayout) findViewById(R.id.layout_indicator);
//		padding = (int) getResources().getDisplayMetrics().density * 10;
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT);
//		params.rightMargin = padding;
//		for (int i = 0; i < pageSize; i++) {
//			GridView view = (GridView) mInflater.inflate(R.layout.emoji_grid,
//					null);
//			view.setAdapter(new GridAdapter(i));
//			view.setOnItemClickListener(new ItemOnClick(i));
//			viewList.add(view);
//			ImageView indicator = new ImageView(this);
//			indicator.setLayoutParams(params);
//			if (i == 0)
//				indicator.setBackgroundResource(R.drawable.point_1);
//			else
//				indicator.setBackgroundResource(R.drawable.point);
//			layoutIndicator.addView(indicator);
//		}
//		pagerAdater = new ViewPagerAdapter(viewList);
//		pager.setAdapter(pagerAdater);
//		pager.setOnPageChangeListener(new GuidePageChangeListener());
//
//	}

	/**
	 * 设置表情翻页的标识
	 * */
	private void setIndicator(int p) {
		layoutIndicator.getChildAt(lastIndicator).setBackgroundResource(
				R.drawable.point);
		layoutIndicator.getChildAt(p).setBackgroundResource(R.drawable.point_1);
		lastIndicator = p;
	}

	/**
	 * 显示表情
	 * */
//	private void showEmojiView() {
//		// Log.d("showEmojiView", "showEmojiView");
//		hideKeyBoard();
//		inputMode = 1;
//
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				findViewById(R.id.layout_edit).setVisibility(View.GONE);
//			
//				findViewById(R.id.layout_emoji).setVisibility(View.VISIBLE);
//				pager.setVisibility(View.VISIBLE);
//				layoutIndicator.setVisibility(View.VISIBLE);
//			}
//		}, 300);
//
//	}

	/**
	 * 显示文字编辑
	 * */
	private void showEditView() {
		// Log.d("showEditView", "showEditView");

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				showKeyBoard(edtMsg);

			}
		}, 200);
		inputMode = 0;
//		findViewById(R.id.layout_edit).setVisibility(View.VISIBLE);
//		findViewById(R.id.layout_emoji).setVisibility(View.GONE);
//	
//		pager.setVisibility(View.GONE);
//		layoutIndicator.setVisibility(View.GONE);
	}

	/**
	 * 响应请求反馈
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 5555) {
			// 有名称返回
			if (resultCode == RESULT_OK)
				userName = data.getStringExtra("username");
			else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		} else if (requestCode == 9988) {
			// 充值成功
			if (resultCode == RESULT_OK) {
				int success = 1;
				if (data != null)
					success = data.getIntExtra("success", 1);
				if (success == 1) {
					// 成功
					showToast("恭喜您成为VIP会员，现在就开始VIP之旅吧");
				} else {
					// 失败
					showFail();
					bagVerify(HuPuRes.ITEM_VIP, false);
				}
			} else if (resultCode == RESULT_CANCELED) {
				// 用户取消
				bagVerify(HuPuRes.ITEM_VIP, false);
			}

		}
	}

	/**
	 * 发送热线，目前两种逻辑，文字信息直接发送，如果是表情则去服务端验证bag有效性
	 * */
	private void sent() {
		if (inputMode == 0) {
			content = edtMsg.getEditableText().toString();

			if (content == null || (content.trim().length() == 0)) {
				showToast("请输入文字");
			} else if (size < 0) {
				showToast("字数超过140字");
			} else {
				Intent data = new Intent();
				data.putExtra("user", userName);
				data.putExtra("content", content);
				data.putExtra("roomid", roomid);
				HupuLog.e("chatInput-328", "send_chat_roomid="+roomid);
				setResult(RESULT_OK, data);
				finish();

			}
		}/* else {

			if (emojiIndex == 0)
				showToast("请选择表情");
			else {
				String tk = SharedPreferencesMgr.getString("tk", null);
				if (tk == null)
					showCustomDialog(DIALOG_BUY_VIP_CHARGE);
				else
					bagVerify(10004, true);
			}

		}*/
	}

	/**
	 * 发送表情
	 * */
	void sendEmoji() {
		Intent data = new Intent();
		data.putExtra("user", userName);
		data.putExtra("emoji", "A_" + emojiIndex);
		setResult(RESULT_OK, data);
		finish();
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

	private void showKeyBoard(View view) {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(view, 0);
	}

	private void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// -------表情网格-------//

	class GridAdapter extends BaseAdapter {

		private int page;
		private int startIndex;
		private int size;

		public GridAdapter(int p) {
			page = p;
			startIndex = page * MAX_PRE_PAGE;
			size = emoji_ids.length - startIndex;
			if (size > MAX_PRE_PAGE)
				size = MAX_PRE_PAGE;
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public Integer getItem(int position) {
			return startIndex + position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Log.d("GridAdapter", "getView" + position);
			if (convertView == null) {
				ImageView img = new ImageView(ChatInputActivity.this);
				img.setScaleType(ScaleType.FIT_CENTER);
				convertView = img;
			}
			((ImageView) convertView)
					.setImageResource(emoji_ids_1[getItem(position)]);
			// ((ImageView) convertView)
			// .setImageResource(R.drawable.emoji_bobo);
			return convertView;
		}

	}

	class ItemOnClick implements OnItemClickListener {

		int page;

		public ItemOnClick(int p) {
			page = p;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			emojiIndex = page * MAX_PRE_PAGE + arg2 + 1;
			imgEmoji.setImageResource(emoji_ids[emojiIndex - 1]);
			try {
				AnimationDrawable draw = (AnimationDrawable) imgEmoji
						.getDrawable();
				if (draw == null || draw.isRunning()) {

				} else {
					draw.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/** 指引页面改监**听**器 */
	class GuidePageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		// 这里可以做一些小动作，不如当你滑动到那一页的时候你要加个图标，文字啥的
		public void onPageSelected(int p) {
			setIndicator(p);
		}
	}

	// 1 有权限，-1 未登陆，-2 无权限，-3无权限且余额不够购买
	public void onCheckFinish(int state) {
		if (state == 1) {
			SharedPreferencesMgr.setBoolean("vip", true);
			sendEmoji();
		} else if (state == -2) {
			// 有钱购买
			showCustomDialog(DIALOG_BUY_VIP_DIRECT);
		} else {
			// 未登录或没钱
			showCustomDialog(DIALOG_BUY_VIP_CHARGE);

		}
	}

	/** 去支付页面 */
	private void toPay() {
		Intent in = new Intent(this, HupuOrderActivity.class);
		in.putExtra("buy_vip", "1");
		in.putExtra("pid", "" + EMOJI_ITEM_PID);
		startActivityForResult(in, 9988);
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (methodId == HuPuRes.REQ_METHOD_BUY_EMOJI) {
			BuyItemEntity item = (BuyItemEntity) o;
			if (item.state == 1 || item.state == -4) {
				showToast("恭喜您成为VIP会员，现在就开始VIP之旅吧");
				SharedPreferencesMgr.setBoolean("vip", true);
			} else if (item.state == -2) {
				showToast("您的购买出现异常，请重新提交。");
			} else {
				// 没钱
				toPay();
			}
		}
	}

	private final int DIALOG_BUY_VIP_DIRECT = 3322;
	private final int DIALOG_BUY_VIP_CHARGE = 3323;
	private int DIALOG_BUY_VIP_FAIL = 3325;
	private int EMOJI_ITEM_PID = 10007;

	void showCustomDialog(int d) {
		showCustomDialog(d, R.string.title_buy, R.string.buy_vip_content,
				TOW_BUTTONS, R.string.buy_vip, R.string.buy_later);
	}

	void showFail() {
		showCustomDialog(DIALOG_BUY_VIP_FAIL, R.string.title_buy,
				R.string.buy_fail_content, TOW_BUTTONS,
				R.string.buy_get_trouble, R.string.buy_success);
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		if (DIALOG_BUY_VIP_DIRECT == dialogId) {
			buy(EMOJI_ITEM_PID);

		} else if (DIALOG_BUY_VIP_CHARGE == dialogId) {
			toPay();

		} else if (DIALOG_BUY_VIP_FAIL == dialogId) {
			// 遇上麻烦了当然找客服mm帮忙啦。
			Intent inContact = new Intent(this, ContactsActivity.class);
			startActivity(inContact);
		}
		if (mDialog != null)
			mDialog.dismiss();
	}

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
		switch (dialogId) {
		case DIALOG_BUY_VIP_DIRECT:
		case DIALOG_BUY_VIP_CHARGE:
			break;
		}
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}

	void buy(int pid) {
		initParameter();
		String token = SharedPreferencesMgr.getString("tk", null);

		mParams.put("token", token);
		mParams.put("pid", "" + pid);
		// Log.d("buy", "token=" + token);
		sendRequest(HuPuRes.REQ_METHOD_BUY_EMOJI, mParams, new HupuHttpHandler(
				this), true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mApp.doPost) {
			//
			mApp.doPost = false;
			showFail();
		}
	}

}
