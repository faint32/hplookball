package com.hupu.games.service;

import java.util.List;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.hupu.games.R;
import com.hupu.games.activity.LaunchActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.NotificationEntity;
import com.hupu.http.BaseHttpClient;
import com.pyj.common.DeviceInfo;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/** 小米推送接受类 */
public class XiaoMiReceiver extends PushMessageReceiver {
	private String mRegId;
	private long mResultCode = -1;
	private String mReason;
	private String mCommand;
	private String mMessage;
	private String mTopic;
	private String mAlias;
	private String mStartTime;
	private String mEndTime;

	@Override
	public void onReceiveMessage(Context context, MiPushMessage message) {

//		if (!TextUtils.isEmpty(message.getTopic())) {
//			mTopic = message.getTopic();
//		} else if (!TextUtils.isEmpty(message.getAlias())) {
//			mAlias = message.getAlias();
//			HupuLog.d( "onReceiveMessage mAlias>>>>>>:::::" +mAlias);
//			paser(context, message);
//
//		}
		HupuLog.d( "onReceiveMessage >>>>>>:::::" +message.getContent());
		if(message.getMessageType()==MiPushMessage.MESSAGE_TYPE_ALIAS)
		{
			mMessage = message.getContent();
			//HupuLog.d( "onReceiveMessage >>>>>>:::::" +message.getContent());
			paser(context, message);
		}

	}

	private void paser(Context context, MiPushMessage message) {
		NotificationEntity entity = new NotificationEntity();
		JSONObject json;
		try {
			json = new JSONObject(message.getContent());
			entity.paser(json);
			showNotification(context, entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			HupuLog.d( "onReceiveMessage >>>>>>:::::" +e.toString());
			e.printStackTrace();
		}

	}

	/***
	 * 显示提醒通知
	 * 
	 */
	private void showNotification(Context context, NotificationEntity entity) {

//		HupuLog.d( "notificationId   >>>>>>:::::" + entity.i_id);
		HupuLog.d("notificatiion     >>>>>>:::::"+ entity.strUrl);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Resources resource = context.getResources();
		Notification notification = new Notification(R.drawable.icon_notify,
				resource.getString(R.string.app_name),
				System.currentTimeMillis());
		// notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// notification.flags |= Notification.FLAG_NO_CLEAR;

		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 5000;
		notification.tickerText = entity.strContent;
		if (entity.strSound == null || entity.strSound.equals("")
				|| entity.strSound.equals("0"))
			notification.defaults = Notification.DEFAULT_LIGHTS;
		else
			notification.defaults |= Notification.DEFAULT_SOUND;
		Intent intent = null;

		if (entity.i_type == 0) {
			// 用浏览器打开
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(entity.strUrl));
		} else {
			intent = new Intent(context.getApplicationContext(),
					LaunchActivity.class);
			intent.putExtra("click", true);

			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setAction(Intent.ACTION_MAIN);
			// new task会启动多个实例
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
					| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FILL_IN_DATA
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("scheme", entity.mScheme);
		}

		notificationManager.cancel(entity.i_id);
		PendingIntent contentItent = PendingIntent.getActivity(context,
				entity.i_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, entity.strTitle,
				entity.strContent, contentItent);
		notificationManager.notify(entity.i_id, notification);
		// sendUmeng(HuPuRes.UMENG_EVENT_NAV, HuPuRes.UMENG_KEY_NAV_SUM,
		// HuPuRes.UMENG_VALUE_QUIZ_RANK);
		MobclickAgent.onEvent(context, HuPuRes.UMENG_EVENT_NOTIFICATION,
				HuPuRes.UMENG_KEY_SEND);
	}

	@Override
	public void onCommandResult(Context context, MiPushCommandMessage message) {
		String command = message.getCommand();
		mResultCode = message.getResultCode();
		mReason = message.getReason();
		List<String> arguments = message.getCommandArguments();
//		HupuLog.d(  "command   >>>>>>:::::" + command);
		if (arguments != null) {
			if (MiPushClient.COMMAND_REGISTER.equals(command)
					&& arguments.size() == 1) {
				mRegId = arguments.get(0);
//				HupuLog.d(  "mRegId   >>>>>>:::::" + mRegId);
				if (mResultCode == ErrorCode.SUCCESS)
					setALIAS(context);
			} else if ((MiPushClient.COMMAND_SET_ALIAS.equals(command) || MiPushClient.COMMAND_UNSET_ALIAS
					.equals(command)) && arguments.size() == 1) {
				mAlias = arguments.get(0);
				HupuLog.d(  "mAlias   >>>>>>:::::" + mAlias);
			} else if ((MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command) || MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC
					.equals(command)) && arguments.size() == 1) {
				mTopic = arguments.get(0);
			} else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)
					&& arguments.size() == 2) {
				mStartTime = arguments.get(0);
				mEndTime = arguments.get(1);
			}
		}

	}

	/** 设置别名 */
	void setALIAS(Context context) {
		getDeviceID(context);
//		HupuLog.d(  "setALIAS   >>>>>>:::::"+ mDeviceId);
		MiPushClient.setAlias(context, mDeviceId, null);
//		for(int i=1;i<11;i++)
//			MiPushClient.setAlias(context, ""+i, null);
	}

	private String mDeviceId;

	public String getDeviceID(Context context) {
		if (mDeviceId == null) {
			mDeviceId = DeviceInfo.getDeviceInfo(context);
			if (mDeviceId == null || mDeviceId.length() < 2) {
				mDeviceId = SharedPreferencesMgr.getString("clientid", null);
				if (mDeviceId == null) {
					mDeviceId = DeviceInfo.getUUID();
					SharedPreferencesMgr.setString("clientid", mDeviceId);
				}
			}
		}
		return mDeviceId;
	}


}
