package com.hupu.games.wxapi;



import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends HupuBaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_weixin_pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, wx_app_id);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		mApp.wxMsgCode = resp.errCode;
		finish();
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage("支付结果："+String.valueOf(resp.errCode));
//			builder.show();
			
			
//			switch (resp.errCode) {
//			case 0:
//				Intent in =new Intent();
//				in.putExtra("success", 1);
//				setResult(RESULT_OK,in);
//				finish();;
//			case -1:
//			case -2:
//				setResult(RESULT_CANCELED);
//				finish();	
//				break;
//
//			default:
//				break;
//			}
			
		}
	}
}