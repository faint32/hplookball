package com.pyj.common;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.hupu.games.R;




/**
 * 对话相关资源
 * */
public class DialogRes {

	/** 网络连接时对话框 */
	public static final int DIALOG_ID_NET_CONNECT = 1;
	/** 取消下载对话框 */
	public static final int DIALOG_ID_CANCEL_DOWNLOAD = 2;
	/** 注销弹出框 */
	public static final int DIALOG_QUIT_PROMPT = 3;
	/** SD卡没准备好 */
	public static final int DIALOG_ID_SDCARD_NOT_AVAILABLE = 4;
	/** SD卡没有数据 */
	public static final int DIALOG_ID_NO_DATA = 5;

	/** 没有可以使用的网络 */
	public static final int DIALOG_ID_NETWORK_NOT_AVALIABLE = 100;
	/** 错误对话框 */
	public static final int DIALOG_ID_ERR = 101;
	/** 下载失败对话框 */
	public static final int DIALOG_ID_DOWNLOAD_FAILED = 102;
	/** 获取数据对话框 */
	public static final int DIALOG_WAITING_FOR_DATA = 103;
	/** 更新数据对话框 */
	public static final int DIALOG_UPDATE_FOR_DATA = 104;

	/** 自动登录对话框 */
	public static final int DIALOG_ID_AUTO_LOGINING = 105;
	/** 登录对话框 */
	public static final int DIALOG_ID_LOGINING = 106;
	/** 登录失败对话框 */
	public static final int DIALOG_ID_LOGINING_FAILD = 107;
	/** 检验用户名是否重复 */
	public static final int DIALOG_ID_CHECK_USERNAME_ISVALID = 108;

	public static final int DIALOG_ERROR_PROMPT = 129;
	/** 提示用户有升级 */
	public static final int DIALOG_ID_HAS_UPDATE = 230;
	/** 提示用户没有升级 */
	public static final int DIALOG_ID_NO_UPDATE = 231;
	/**
	 * 注册信息发送请求
	 */
	public static final int DIALOG_SEND_REGISTER_REQUEST = 109;
	/** 提示用户去注册 */
	public static final int DIALOG_REGIST_NOTIFY = 501;

	/**
	 * 获取对话框的标题
	 * 
	 * @param dialogID
	 *            对话框id
	 * @return 标题文字所对应的资源id
	 * */
	public static int getTitle(int dialogID) {
		switch (dialogID) {
		case DIALOG_ID_CANCEL_DOWNLOAD:

		case DIALOG_ID_SDCARD_NOT_AVAILABLE:
		case DIALOG_ID_NO_DATA:
		case DIALOG_ID_HAS_UPDATE:
		case DIALOG_ID_NO_UPDATE:
		case DIALOG_REGIST_NOTIFY:

			break;
		case DIALOG_QUIT_PROMPT:
			return R.string.TITLE_QUIT;
		case DIALOG_ID_ERR:
		case DIALOG_ID_NETWORK_NOT_AVALIABLE:
			return R.string.STR_ERR;
		case DIALOG_ID_NET_CONNECT:
			return R.string.TITLE_QUIT;
		}
		return -1;
	}

	/**
	 * 获取对话框的内容
	 * 
	 * @param dialogID
	 *            对话框id
	 * @return 对话框的内容文字所对应的资源id
	 * */
	public static int getMessage(final int dialogID) {
		switch (dialogID) {
		case DIALOG_QUIT_PROMPT:
			return R.string.MSG_QUIT;
		case DIALOG_ID_NET_CONNECT:
			return R.string.STR_CONNECTING;
		case DIALOG_ID_ERR:
		case DIALOG_ID_NETWORK_NOT_AVALIABLE:
			return R.string.MSG_CONNECTION_ERR;
		}
		return -1;
	}

	/**
	 * 获取对话框的左键文字
	 * 
	 * @param dialogID
	 *            对话框id
	 * @return 左键文字所对应的资源id
	 * */
	public static int getPositiveTxt(int dialogID) {
		switch (dialogID) {
		case DIALOG_QUIT_PROMPT:
			return R.string.STR_QUIT;
		case DIALOG_ID_ERR:
			return R.string.STR_CONFIRM;
		case DIALOG_ID_NETWORK_NOT_AVALIABLE:
			return R.string.STR_CONFIRM;
		}
		return -1;
	}

	/**
	 * 获取对话框的右键文字
	 * 
	 * @param dialogID
	 *            对话框id
	 * @return 右键文字所对应的资源id
	 * */
	public static int getNegativeTxt(int dialogID) {
		switch (dialogID) {
		case DIALOG_QUIT_PROMPT:
			return R.string.STR_CANCEL;
		}
		return -1;
	}

	/**
	 * 获取对话框的中间键文字
	 * 
	 * @param dialogID
	 *            对话框id
	 * @return 中间键文字所对应的资源id
	 * */
	public static int getNeutralTxt(int dialogID) {
		switch (dialogID) {
		}

		return -1;
	}

	/**
	 * 根据id创建一个对话框
	 * 
	 * @param c
	 *            对话框持有者
	 * @param id
	 *            对话框id
	 * @param canelable
	 *            是否可以按返回键退出
	 * @return 生成的对话框
	 * */
	public static AlertDialog.Builder getBuild(Context c, final int id,
			boolean canelable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c).setCancelable(
				canelable).setTitle(getTitle(id));
		if (id != DIALOG_ID_ERR)
		{
			if(id>0)
				builder.setMessage(getMessage(id));
			else
				builder.setMessage("");
		}
		return builder;
	}

	public static AlertDialog buildProgressDialog(Context c) {

		ProgressDialog p = new ProgressDialog(c);
		p.setMessage(c.getResources().getString(R.string.STR_CONNECTING));
		p.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		return p;
	}

}
