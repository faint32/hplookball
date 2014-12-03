package com.hupu.games.casino;

import u.aly.ba;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.AndroidCharacter;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.MyPrizeListActivity;
import com.hupu.games.activity.NewsDetailActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.personal.box.BoxOpenEntity;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.common.DeviceInfo;

public class ShakeBoxActivity extends HupuBaseActivity {

	View bg;

	int type;

	int num;

	ShakeListener mShakeListener;

	long lastTime;

	Vibrator vibrator = null;

	boolean hasData;

	boolean reqData;

	Integer time;

	String ss[] = { "金宝箱", "银宝箱", "铜宝箱" };
	int res[] = { R.drawable.icon_box_gold_s, R.drawable.icon_box_silver_s,R.drawable.icon_box_copper_s };
	int bgRes[] = {R.drawable.bg_box_gold , R.drawable.bg_box_silver,R.drawable.bg_box_cooper};
	int txtRes[] = { R.string.box_gold_description, R.string.box_slive_description,R.string.box_corper_description };
	TextView txtNum;
	
	TextView txtDesc;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_shake_box);
		
		Intent in = getIntent();
		type = in.getIntExtra("type", 0);
		num = in.getIntExtra("num", 0);
		String memo =in.getStringExtra("memo");
		if(memo!=null)
		{
			txtDesc=(TextView)findViewById(R.id.txt_desc);
			txtDesc.setText(memo);
		}
		init();
		initParameter();
		mParams.put("name", ss[type]);
		
		setOnClickListener(R.id.img_bg);
	}

	private void init()
	{
		mShakeListener = new ShakeListener(this);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		setOnClickListener(R.id.btn_back);
		bg = findViewById(R.id.img_bg);
//		int height = getWindowManager().getDefaultDisplay().getWidth()*908/720;
//		bg.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.FILL_PARENT, height));
		bg.setBackgroundResource(bgRes[type]);
		txtNum =(TextView)findViewById(R.id.txt_num);
		txtNum.setCompoundDrawablesWithIntrinsicBounds(res[type], 0, 0, 0);
		txtNum.setText(""+num);
	}
	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.btn_get_now:
			// 跳转
			Intent in =new Intent(this,MyPrizeListActivity.class);
			startActivity(in);
			break;
		case R.id.btn_cancel:
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			break;
		case R.id.btn_close:
			if(dialog!=null && dialog.isShowing())
				dialog.dismiss();
			break;
		case R.id.img_bg:
			onShake();
			break;
		}
	}

	boolean canShow;

	void showDialog() {
		//--不再摇，并且有数据显示宝箱
		Log.d("showDialog", "canshow="+canShow+"  hasData="+hasData);
		if (canShow && hasData) {
			reqData = false;
			hasData = false;
			canShow =false;
//			entity.type=2;
//			entity.name="球鞋";
			mShakeListener.stop();
			if (entity.type == 1) {
				//
				showCoinDialog();
			} else {
				//
				showBonusDialog();
			}
			if(entity.img_url!=null && imgBonus!=null)
				UrlImageViewHelper.setUrlDrawable(imgBonus,entity.img_url);
			num--;
			txtNum.setText(""+num);
		}

	}

	BoxOpenEntity entity;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		/** 1 宝箱开启成功, -1 宝箱名非法, -2 用户未登陆, -3 用户没有该宝箱 */
		if (methodId == HuPuRes.REQ_METHOD_GET_BOX_OPEN) {

			entity = (BoxOpenEntity) o;
			hasData=true;
			reqData=false;
			switch (entity.status) {
			case -1:
				showToast("宝箱名非法");
				break;
			case -2:
				showToast("未登录用户");
				break;
			case -3:
				showToast("无该宝箱");
				break;
			case 1:				
				showDialog();
				
				break;
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mShakeListener.start();
		bStop = false;
	}

	boolean bStop;

	@Override
	protected void onStop() {
		super.onStop();
		mShakeListener.stop();
		bStop = true;
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private void reqData() {
		time = 2;
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				try {
					while (time > 0) {
						synchronized (time) {
							time--;
							if (time <= 0) {
								mShakeListener.stop();
								canShow = true;
								showDialog();
//								Log.d("run", "show");
							}
//							Log.d("run", "time="+time);
						}
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		sendRequest(HuPuRes.REQ_METHOD_GET_BOX_OPEN, mParams,
				new HupuHttpHandler(this));
	}


	public void onShake() {

		long currentTime = System.currentTimeMillis();
		long inteval = currentTime - lastTime;
//	    Log.d("onshake", "s");
		if (num <= 0  ) {
			if(inteval > 3000)
				showToast("没有可以开的宝箱");
			lastTime = currentTime;
			return;
		}
		if (!reqData) {
			// 请求开宝箱
			
			if (dialog != null && dialog.isShowing()) 
			{
				return;
			}
			else
			{
				reqData = true;
				hasData = false;
				canShow = false;
				reqData();
				switch (type) {
				case 0:
					sendUmeng(HuPuRes.UMENG_EVENT_MY_BOX, HuPuRes.UMENG_KEY_SHAKE_GOLDBOX);
					break;
				case 1:
					sendUmeng(HuPuRes.UMENG_EVENT_MY_BOX, HuPuRes.UMENG_KEY_SHAKE_SILVERBOX);
					break;
				case 2:
					sendUmeng(HuPuRes.UMENG_EVENT_MY_BOX, HuPuRes.UMENG_KEY_SHAKE_BRONZEBOX);
					break;

				default:
					break;
				}
			}
			
		}
		synchronized (time) {
			if (time < 1)
				time = 1;
		}
		vibrator.vibrate(500); // 开始震动
	    HupuLog.d("onshake", "e"+time);
	}

	Dialog dialog;
	TextView txtBonus;
	TextView txtGetNow;
	ImageView imgBonus;
	String bonus;
	String getNow;
    boolean showDialog;
	public void showBonusDialog() {

		showDialog =true;
		if (bonus == null) {
			bonus = getResources().getString(R.string.bonus);
			getNow = getResources().getString(R.string.get_now_txt);
		}
		View v = LayoutInflater.from(this).inflate(R.layout.dialog_big_bonus,
				null);
		txtBonus = (TextView) v.findViewById(R.id.txt_bonus_info);
		txtGetNow = (TextView) v.findViewById(R.id.txt_get_now);
		imgBonus= (ImageView) v.findViewById(R.id.img_logo);
		v.findViewById(R.id.btn_get_now).setOnClickListener(click);

		v.findViewById(R.id.btn_cancel).setOnClickListener(click);

		txtBonus.setText(Html.fromHtml(String.format(bonus, entity.name)));
		txtGetNow.setText(Html.fromHtml(getNow));

		dialog = new Dialog(this, R.style.MyWebDialog);
		dialog.setContentView(v);
		// mImgDialog.setCanceledOnTouchOutside(true);
		// mImgDialog.setCancelable(true);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.show();
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (!bStop)
					mShakeListener.start();
				imgBonus=null;
				showDialog =false;
			}
		});
	}

	TextView txtCoin;
	TextView txtBalance;
	String coinBonus;
	String balance;

	public void showCoinDialog() {
		showDialog =true;
		if (coinBonus == null) {
			coinBonus = getResources().getString(R.string.coin_bonus);
			balance = getResources().getString(R.string.coin_balance);
		}
		View v = LayoutInflater.from(this).inflate(R.layout.dialog_coin_bonus,
				null);
		txtCoin = (TextView) v.findViewById(R.id.txt_coin_bonus);
		txtBalance = (TextView) v.findViewById(R.id.txt_balance);

		v.findViewById(R.id.btn_close).setOnClickListener(click);

		txtCoin.setText(Html.fromHtml(String.format(coinBonus, entity.coin)));
		txtBalance.setText(String.format(balance, entity.balance));

		dialog = new Dialog(this, R.style.MyWebDialog);
		dialog.setContentView(v);
		// mImgDialog.setCanceledOnTouchOutside(true);
		// mImgDialog.setCancelable(true);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.show();
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (!bStop)
					mShakeListener.start();
				showDialog =false;
			}
		});
	}

	class ShakeListener implements SensorEventListener {
		String TAG = "ShakeListener";
		// 速度阈值，当摇晃速度达到这值后产生作用
		private static final int SPEED_SHRESHOLD = 500;
		// 两次检测的时间间隔
		private static final int UPTATE_INTERVAL_TIME = 50;
		// 传感器管理器
		private SensorManager sensorManager;
		// 传感器
		private Sensor sensor;

		// 上下文
		private Context mContext;
		// 手机上一个位置时重力感应坐标
		private float lastX;
		private float lastY;
		private float lastZ;
		// 上次检测时间
		private long lastUpdateTime;

		// 构造器
		public ShakeListener(Context c) {
			// 获得监听对象
			mContext = c;
		}

		// 开始
		public void start() {
			// 获得传感器管理器
			sensorManager = (SensorManager) mContext
					.getSystemService(Context.SENSOR_SERVICE);
			if (sensorManager != null) {
				// 获得重力传感器
				sensor = sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			}
			// 注册
			if (sensor != null) {
				sensorManager.registerListener(this, sensor,
						SensorManager.SENSOR_DELAY_GAME);
			}
		}

		// 停止检测
		public void stop() {
//			Log.d("xxx", "stop");
			sensorManager.unregisterListener(this);
		}

		// 重力感应器感应获得变化数据
		public void onSensorChanged(SensorEvent event) {
			// 现在检测时间
			long currentUpdateTime = System.currentTimeMillis();
			// 两次检测的时间间隔
			long timeInterval = currentUpdateTime - lastUpdateTime;
			// 判断是否达到了检测时间间隔
			if (timeInterval < UPTATE_INTERVAL_TIME)
				return;
			int sensorType = event.sensor.getType();  
	        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴  
	        float[] values = event.values;  
	        if(sensorType == Sensor.TYPE_ACCELEROMETER){              
	            if((Math.abs(values[0])>18||Math.abs(values[1])>18||Math.abs(values[2])>18)){  
	                onShake();
	            }  
	  
	  
	        }  
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(!showDialog)
			{
				setResult(RESULT_OK);
				finish();
				return true;
			}
		}
		return false;
	}
}
