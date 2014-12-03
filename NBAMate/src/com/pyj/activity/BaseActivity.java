/**
 * 
 */
package com.pyj.activity;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hupu.games.R;
import com.hupu.games.common.DoubleClickUtil;
import com.hupu.games.common.HupuLog;
import com.pyj.BaseApplication;
import com.pyj.common.DialogRes;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 所有自定activity的基类，处理公用dialog，view，menu的事件。
 * 
 * @author peter.pan
 */
public class BaseActivity extends FragmentActivity {

	private BaseApplication mBaseApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mBaseApp = (BaseApplication) getApplication();
		mBaseApp.addActivitToStack(this);
		click = new Click();

	}

	@Override
	protected void onResume() {
		super.onResume();
		i_curState = STATE_START;
	}

	@Override
	protected void onStop() {
		if (i_curState == STATE_SHOW_DIALOG && mDialog.isShowing())
			mDialog.cancel();
		i_curState = STATE_STOP;
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mBaseApp.removeFromStack(this);
		super.onDestroy();
	}

	public void showToast(String s) {
		Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/** dialog中有一个按钮 */
	public static final int ONE_BUTTON = 1;
	/** dialog中有两个按钮 */
	public static final int TOW_BUTTONS = 3;
	/** dialog中有三个按钮 */
	public static final int THREE_BUTTONS = 7;
	/** 错误的信息的字符串 */
	private String str_err;
	/** 错误的信息的Res id */
	private int i_errRes;

	protected AlertDialog mDialog;

	@Override
	protected Dialog onCreateDialog(final int id) {
		if (id == DialogRes.DIALOG_ID_NET_CONNECT) {
			return mDialog = DialogRes.buildProgressDialog(this);
		}
		AlertDialog.Builder builder = DialogRes.getBuild(this, id, true);
		int flag = 0;
		switch (id) {
		case DialogRes.DIALOG_ID_ERR:
			// 显示错误信息
			if (str_err != null)
				builder.setMessage(str_err);
			else if (i_errRes > 0)
				builder.setMessage(i_errRes);
		case DialogRes.DIALOG_ERROR_PROMPT:
		case DialogRes.DIALOG_ID_NETWORK_NOT_AVALIABLE:
		case DialogRes.DIALOG_ID_NO_DATA:
		case DialogRes.DIALOG_ID_SDCARD_NOT_AVAILABLE:
		case DialogRes.DIALOG_ID_LOGINING_FAILD:// 登录失败
		case DialogRes.DIALOG_ID_NO_UPDATE:
		case DialogRes.DIALOG_REGIST_NOTIFY:
			flag = ONE_BUTTON;
			break;
		case DialogRes.DIALOG_ID_NET_CONNECT:
			break;
		case DialogRes.DIALOG_WAITING_FOR_DATA:// 正在获取数据...progressDialog
			break;
		case DialogRes.DIALOG_ID_AUTO_LOGINING:// 自动登录...
			break;
		case DialogRes.DIALOG_ID_LOGINING:// 正在登录...
			break;

		case DialogRes.DIALOG_ID_CHECK_USERNAME_ISVALID:// 检查用户名是否重复
			break;
		case DialogRes.DIALOG_SEND_REGISTER_REQUEST:// 正在发送请求
			break;
		case DialogRes.DIALOG_UPDATE_FOR_DATA:
			break;
		case DialogRes.DIALOG_ID_DOWNLOAD_FAILED:
		case DialogRes.DIALOG_ID_HAS_UPDATE:
		case DialogRes.DIALOG_QUIT_PROMPT:
		case DialogRes.DIALOG_ID_CANCEL_DOWNLOAD:
			flag = TOW_BUTTONS;
			break;
		case CUSTOM_DIA_TOW_BUTTONS:
			break;
		}
		if ((flag & 1) > 0) {
			builder.setPositiveButton(DialogRes.getPositiveTxt(id),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickPositiveButton(id);
						}
					});
		}
		if ((flag & 2) > 0) {
			builder.setNegativeButton(DialogRes.getNegativeTxt(id),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickNegativeButton(id);
						}
					});
		}
		if ((flag & 4) > 0) {
			builder.setNeutralButton(DialogRes.getNeutralTxt(id),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickNeutralButton(id);
						}
					});
		}
		mDialog = builder.create();
		mDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				onCancelDialog(id);
			}

		});

		return mDialog;
	}

	final int CUSTOM_DIA_TOW_BUTTONS = -2;

	/**
	 * 对话框取消时会调
	 * 
	 * @param dialogId
	 *            对话框ID
	 * */
	public void onCancelDialog(int dialogId) {
		if (dialogId == DialogRes.DIALOG_ID_NET_CONNECT
				&& i_curState == STATE_CONNECTING) {
			i_curState = STATE_START;
		}
	}

	/**
	 * 点击dialog左键 子类需要重载
	 * 
	 * @param dialogId
	 *            对话框ID
	 * */
	public void clickPositiveButton(int dialogId) {
		removeDialog(dialogId);
		if (dialogId == DialogRes.DIALOG_QUIT_PROMPT) {
			// 如果是退出
			quit();
		}

	}

	public void quit() {
		onQuit();
		mBaseApp.quit();
	}

	public void onQuit() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	/**
	 * 点击dialog右键 子类需要重载
	 * 
	 * @param dialogId
	 *            对话框ID
	 * */
	public void clickNegativeButton(int dialogId) {
		removeDialog(dialogId);
	}

	/**
	 * 点击dialog中间键 子类需要重载
	 * 
	 * @param dialogId
	 *            对话框ID
	 * */
	public void clickNeutralButton(int dialogId) {

	}

	/**
	 * 请求服务正常返回结果，子类需要覆盖此方法。
	 * 
	 * @param o
	 *            返回的数据对象
	 * @param methodId
	 *            请求的方法ID
	 * 
	 * */
	public void onReqResponse(Object o, int methodId) {

	}

	/**
	 * 请求服务正常返回结果，子类需要覆盖此方法。
	 * 
	 * @param o
	 *            返回的数据对象
	 * @param methodId
	 *            请求的方法ID
	 * 
	 * */
	public void onReqResponse(Object o, int methodId,int msgid1,int msgid2) {

	}
	
	/**
	 * 请求服务异常返回结果，子类可以覆盖此方法。
	 * 
	 * @param error
	 *            返回的错误对象
	 * @param content
	 *            返回的错误描述
	 * */
	public void onErrResponse(Throwable error, int type) {
		i_errRes = -1;
		str_err = null;
		if (error != null) {
			if (error instanceof ConnectException
					|| error instanceof IOException) {
				i_errRes = R.string.MSG_CONNECTION_ERR;
				//添加umeng自定义事件超时
				MobclickAgent.onEvent(this, "Http_Connec_Error_5");
			} else if (error instanceof TimeoutException) {
				MobclickAgent.onEvent(this, "Http_TimeOut_5");
				i_errRes = R.string.MSG_TIME_OUT;
			}
			error.printStackTrace();
		}
		if (i_errRes > 0)
			showDialog(DialogRes.DIALOG_ID_ERR);
	}

	public void onErrMsg(String msg, int type)
	{
		showToast(msg);
	}
	/**
	 * 请求服务异常返回结果，子类可以覆盖此方法。
	 * 
	 * @param error
	 *            返回的错误对象
	 * @param content
	 *            返回的错误描述
	 * @param isBackGroundThread
	 *            是否后台线程访问网络 true:是，false:否
	 * 
	 */

	public void onErrResponse(Throwable error, String content,
			boolean isBackGroundThread) {

	}

	public Click click;

	/** 设置控件点击监听器 */
	public void setOnClickListener(int id) {

		if (click == null)
			click = new Click();
		findViewById(id).setOnClickListener(click);
	}

	private class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			treatClickEvent(id);
			treatClickEvent(v);
		}
	}

	/** 处理点击事件 */
	public void treatClickEvent(int id) {
		if(DoubleClickUtil.isFastDoubleClick()){
			return;
		}
	}

	/** 处理点击事件 */
	public void treatClickEvent(View v) {

	}

	/** 设置listview点击监听器 */
	class ListClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
			treatItemClick(arg0, v, pos, arg3);
		}

	}

	ListClick listClick;

	public void setOnItemClick(ListView list) {
		if (listClick == null)
			listClick = new ListClick();
		list.setOnItemClickListener(listClick);
	}

	public void treatItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {

	}

	/**
	 * 记录当前的状态
	 * */
	public int i_curState;

	/**
	 * 当前Activity处于开始状态
	 * */
	public static final int STATE_START = 1;
	/**
	 * 当前Activity处于暂停状态
	 * */
	public static final int STATE_PAUSE = 2;
	/**
	 * 当前Activity处于连接数据状态
	 * */
	public static final int STATE_CONNECTING = 3;
	/**
	 * 当前Activity处于显示对话框状态
	 * */
	public static final int STATE_SHOW_DIALOG = 4;
	/**
	 * 当前Activity处于停止状态
	 * */
	public static final int STATE_STOP = 5;

	/** 当前请求的线程数 */
	public int i_curReqTimes;

	
	/**
	 * 保存图片
	 */
	
	public void savePic(String url){
		
	}
}
