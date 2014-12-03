package com.hupu.games.hupudollor;

import android.app.Dialog;

import com.hupu.games.data.RechargeMethodReq;
import com.hupu.games.hupudollor.data.OrderHupuDollorPacEntity;

public interface PayHupuDollorCallBack {
	void onPayListener(Dialog dialog,OrderHupuDollorPacEntity entity,String channel);
	void onRechargeListener(Dialog dialog,RechargeMethodReq entity,String channel);
}
