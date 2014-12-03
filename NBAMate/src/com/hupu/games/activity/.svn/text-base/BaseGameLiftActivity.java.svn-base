package com.hupu.games.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseLiveResp;
import com.hupu.games.data.room.GiftEntity;
import com.hupu.games.data.room.GiftReqDataEntity;
import com.hupu.games.data.room.GiftRespResultEntity;
import com.hupu.games.dialog.GiftTipsDialog;
import com.hupu.games.dialog.TipsDialog;
import com.hupu.games.hupudollor.activity.HupuDollorOrderActivity;
import com.hupu.games.hupudollor.data.HupuDollorBalanceReq;
import com.hupu.games.livegift.animation.AnimationTool;
import com.hupu.games.livegift.animation.AutofitTextView;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.view.AutoTextView;
import com.hupu.http.HupuHttpHandler;

public class BaseGameLiftActivity extends BasePayActivity{
	public ArrayList<GiftEntity> giftList;
	GiftTipsDialog	tipsDialog;
	LinearLayout layGift;
	public int hupuDollor;
	AnimationTool animationTool;
	public ArrayList<View> giftTvList = new ArrayList<View>();
	boolean exit;
	ArrayList<GiftReqDataEntity> sendlist = new ArrayList<GiftReqDataEntity>();
	int interval=1;
	Handler mHandler;
	final int LETTER_MAX_LINE=3;//汉字一行最多数
	final int CHAR_MAR_LINE=6;//英文一行最多数量
	int push_gift_count_add_times=10;//1秒钟加的次数
	int push_gift_count_add_min=2;//push的count最小多少无需累加
	DealThread[] deal_gift_thread;
	BroadcastReceiver receiver=new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			reqBalance();
		}
	};

	@Override
	protected void onDestroy(){
		super.onDestroy();
		quitLive();
		this.unregisterReceiver(receiver);
	}


	int k=0;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		IntentFilter filter = new IntentFilter();
		filter.addAction("login");
		registerReceiver(receiver, filter);



//		new Thread(new Runnable(){
//			@Override
//			public void run() {
//
//				while(!exit){
//					if(giftList!=null){
//						ArrayList<GiftEntity> list = new ArrayList<GiftEntity>();
//						for(int i=0;i<giftList.size();i++){
//							GiftEntity gnew =new GiftEntity();
//							GiftEntity ge=giftList.get(i);
//							gnew.gift_name="name_"+k;
//							int rand=(new Random()).nextInt(10);
//							gnew.count=ge.count+rand;
//							list.add(gnew);
//							HupuLog.e("BaseGameLiftActivity", "add push====="+gnew.gift_name+","+ge.gift_name+","+rand);
//						}
//						// TODO Auto-generated method stub
//						pushUpdataGift(list);
//						k++;
//					}
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//
//		}).start();
	}


	void initGift(final BaseLiveResp data,int tab,boolean fromResp){
		if(layGift == null){
			layGift= (LinearLayout)findViewById(R.id.layout_gift);
		}
		if(fromResp){
			layGift.removeAllViews();
		}

		if(tab != INDEX_LIVE && tab != INDEX_LIVE_BY_MAN){//非直播tab
			//            quitLive();
			layGift.setVisibility(View.GONE);
			return;
		}
		if(data == null || data.giftList == null || data.giftList.size() == 0){
			return;
		}
		giftList = data.giftList;
		roomid = data.default_room_id;

		reqBalance();//获取虎扑币
		initThread();
		initGiftList();
		animationTool = new AnimationTool(this,0);
	}
	private void initThread() {
		HandlerThread thread = new HandlerThread("sendGiftService");
		thread.start();
		Looper looper = thread.getLooper();
		if(mHandler == null){
			mHandler = new Handler(looper);
		}
		mHandler.postDelayed(checkSer, interval);
	}
	private Runnable checkSer = new Runnable() {
		@Override
		public void run() {
			reqSendGift();
			if(!exit){
				mHandler.postDelayed(checkSer, interval);
			}
		}
	};
	class GiftClick implements OnClickListener {
		int index;
		GiftClick(int index){
			this.index = index;
		}
		@Override
		public void onClick(View v) {
			checkSendGift(giftList.get(index));
		}
	}
	void checkSendGift(GiftEntity gift){
		//是否登录
		if (mToken == null) {
			showBindDialog(SharedPreferencesMgr.getString("dialogBtnText",
					getString(R.string.bind_phone_dialog)));
			return;
		} 
		//是否第一次送礼
		if(!SharedPreferencesMgr.getBoolean("sendGift", false)){
			firstSendNotice(gift);
			SharedPreferencesMgr.setBoolean("sendGift", true);
			return;
		}

		//		hupuDollor=0;//test
		//虎扑币是否够
		if(gift.price > hupuDollor){
			hupuDollorLessNotice();
			return;
		}
		add2SendList(gift.gift_id,"",giftList.indexOf(gift));
	}
	private void add2SendList(int giftid,String uid,int giftindex){


		int color=0;
		if(giftindex == 0){
			color=R.color.txt_live_send_gift_bt_green;
		}else {
			color=R.color.txt_live_send_gift_bt_orange;
		}

		animationTool.showAnimation(giftList.get(giftindex).gift_name,color);
		this.hupuDollor-=giftList.get(giftindex).price;
		giftList.get(giftindex).count+=giftList.get(giftindex).price;


		HupuLog.e("add2SendList", "add2SendList sendMessage,"+giftindex);
		this.sendMessage(giftindex, giftList.get(giftindex));



		GiftReqDataEntity object = new GiftReqDataEntity();
		object.setGiftid(giftid);
		object.setUid(uid);
		sendlist.add(object);
	}
	private  String getSendList(){
		String result = "";
		JSONObject jsonObject;
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < sendlist.size(); i++) {
			jsonObject = new JSONObject();
			try {
				//jsonObject.put(leaguesEntities.get(i).lid + "", 1);
				//注释说明：之前提交是否关注的信息！
				jsonObject.put("giftid",sendlist.get(i).getGiftid());
				jsonObject.put("uid",sendlist.get(i).getUid());
				jsonArray.put(jsonObject);
				// Log.i("papa", jsonObject.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result = jsonArray.toString();
		sendlist.clear();
		return result;
	}
	private void initGiftList(){

		layGift.setVisibility(View.VISIBLE);
		layGift.removeAllViews();

		int len=giftList.size();
		for(int i=0;i<len;i++){
			GiftEntity gift = giftList.get(i);
			View giftview = LayoutInflater.from(this).inflate(R.layout.item_live_room_gift, null);
			AutofitTextView view=(AutofitTextView)giftview.findViewById(R.id.btn_gift_green);
			TextView txtSwitcher = (TextView)giftview.findViewById(R.id.gift_total_1);
			View outer = giftview.findViewById(R.id.btn_gift_green_layout);
			
			view.setMaxLines(2);
			view.setMaxTextSize(12);
			view.setMinTextSize(3);
		

			int type=0;
			if(gift.gift_name != null && gift.gift_name.length()>0){
				type = isChineseEnglish(gift.gift_name.charAt(0));
			}
			String bttxt=gift.gift_name;
			if(type == 0 && gift.gift_name.length()>LETTER_MAX_LINE){
				bttxt=gift.gift_name.substring(0, LETTER_MAX_LINE-1)+"\n"+gift.gift_name.substring(LETTER_MAX_LINE-1);
				
			}else if(type != 0 && gift.gift_name.length()>CHAR_MAR_LINE){
				bttxt=gift.gift_name.substring(0, CHAR_MAR_LINE-2)+"\n"+gift.gift_name.substring(CHAR_MAR_LINE-2);
			}else{
				view.setMaxLines(1);
				view.setMaxTextSize(12);
				view.setMinTextSize(3);
			}

			view.setText(bttxt);
			outer.setOnClickListener(new GiftClick(i));
			txtSwitcher.setText(gift.count+"");
			layGift.addView(giftview);
			if(i==1){
				outer.setBackgroundResource(R.drawable.btn_gift_orange_selector);
//				outer.setBackgroundResource(this.getResources().getColor(R.color.bg_news_item));
				view.setTextColor(this.getResources().getColor(R.color.txt_live_send_gift_bt_orange));
				txtSwitcher.setTextColor(this.getResources().getColor(R.color.txt_live_send_gift_bt_orange));
			}
		}
	}
	public int isChineseEnglish(char c) {  
		if (c >= 0 && c <= 9) {            
			// 是数字  
			return 4;//"是数字字符";  
		} else if ((c >= 'a' && c <= 'z')) { 
			// 是小写字母  
			return 3;//"是小写字母";  
		}else if ((c >= 'A' && c <= 'z')) {  
			// 是大写字母  
			return 2;//"是大写字母";  
		} else if (Character.isLetter(c)) {   
			// 是汉字  
			return 0;//"是汉字字符";  
		} else {                             
			// 是特殊符号  
			return 1;//"是特殊符号";  
		}  
	} 
	public void pushUpdataGift(ArrayList<GiftEntity> glist){
		for(int i=0;i<glist.size();i++){
			GiftEntity ge = glist.get(i);
			GiftEntity localGe = giftList.get(i);
			HupuLog.e("pushUpdataGift", "pushUpdataGift=================="+i+","+localGe.gift_name+","+ge.gift_name);
			if(!localGe.gift_name.equals(ge.gift_name)){//名字修改了
				localGe.gift_name=ge.gift_name;
				localGe.count=ge.count;
				this.sendMessage(i, localGe);
			}
			if(deal_gift_thread == null){
				deal_gift_thread = new DealThread[glist.size()];
			}
			if(deal_gift_thread[i]==null){
				deal_gift_thread[i] = new DealThread(ge.count-localGe.count, i);
				deal_gift_thread[i].start();
			}else{
				deal_gift_thread[i].setGift(ge.count-localGe.count, i);
			}
		}
	}
	public void sendMessage(int what, Object object) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = object;
		giftHandler.sendMessage(msg);
		HupuLog.e("sendMessage", what+","+((GiftEntity)object).gift_id+","+((GiftEntity)object).gift_name);
	}
	Handler giftHandler = new Handler(){
		public void handleMessage(Message msg) {
			HupuLog.e("handleMessage", msg.what+","+((GiftEntity)msg.obj).gift_id+","+((GiftEntity)msg.obj).gift_name);
			updateGiftShow(msg.obj,msg.what);
		}
	};

	private void reqBalance() {
		if (mToken != null) {
			initParameter();
			mParams.put("token", mToken);
			sendRequest(HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE, mParams,
					new HupuHttpHandler(this), false);
		}
	}
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		switch (methodId) {
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_SEND_GIFT:
			if (o != null) {
				GiftRespResultEntity gren = (GiftRespResultEntity)o;
				//				this.hupuDollor = gren.balance;
				this.interval = gren.interval;
			}
			break;
		case HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE:
			if (o != null) {
				HupuDollorBalanceReq info = (HupuDollorBalanceReq) o;
				this.hupuDollor=info.balance;
			}
			break;
		}

	}
	private void firstSendNotice(final GiftEntity gift)
	{	
		String content = String.format(getString(R.string.live_first_send_gift_notice), gift.price);
		tipsDialog = new GiftTipsDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				tipsDialog.dismiss();
				// TODO Auto-generated method stubW
				if(v.getId()==GiftTipsDialog.BTN_OK_ID)//确定
				{
					if(gift.price > hupuDollor){
						hupuDollorLessNotice();
					}else{
						add2SendList(gift.gift_id,"",giftList.indexOf(gift));
					}
				}

			}
		},content, GiftTipsDialog.DEFAULT);
		tipsDialog.initData(content, TipsDialog.DEFAULT);
		tipsDialog.initBtn(getString(R.string.title_confirm), getString(R.string.cancel));
		tipsDialog.show();
	}
	private void hupuDollorLessNotice()
	{	
		String content = this.getString(R.string.live_send_gift_hupudollr_insufficent);
		tipsDialog = new GiftTipsDialog(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				tipsDialog.dismiss();
				// TODO Auto-generated method stubW
				if(v.getId()==GiftTipsDialog.BTN_OK_ID)//确定
				{
					Intent it = new Intent(BaseGameLiftActivity.this,HupuDollorOrderActivity.class);
					it.putExtra("hupuDollor_balance", hupuDollor+"");
					startActivityForResult(it, REQ_GO_POST_ORDER);
				}

			}
		},content, GiftTipsDialog.DEFAULT);
		tipsDialog.initData(content, TipsDialog.DEFAULT);
		tipsDialog.initBtn(getString(R.string.title_confirm), getString(R.string.cancel));
		tipsDialog.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		HupuLog.e("papa", "resultId==" + resultCode);
		if (requestCode == REQ_GO_POST_ORDER) {
			if (resultCode == RESULT_OK) {
				reqBalance();

			}
		}
	}
	public void quitLive(){
		exit=true;
		if (animationTool !=null) {
			animationTool.destroy();
			animationTool=null;
		}

	}
	void reqSendGift(){
		if(sendlist.size()==0)
		{
			return;
		}
		initParameter();
		mParams.put("gid", gid + "");
		mParams.put("roomid", roomid+"");
		mParams.put("type", tag);
		mParams.put("data", getSendList());
		sendRequest(HuPuRes.REQ_METHOD_GET_PLAY_LIVE_SEND_GIFT, mParams,
				new HupuHttpHandler(BaseGameLiftActivity.this), false);
	}
	/**
	 * 收到push消息更新礼物按钮
	 */
	private void updateGiftShow(Object obj,int i){
		GiftEntity ge=(GiftEntity)obj;

		HupuLog.e("updateGiftShow", i+","+ge.gift_id+","+ge.gift_name);


		RelativeLayout giftview = (RelativeLayout)layGift.getChildAt(i);
		TextView txtSwitcher = (TextView)giftview.getChildAt(0);
		AutofitTextView  view =  (AutofitTextView)((FrameLayout)giftview.getChildAt(1)).getChildAt(0);

		int type=0;
		if(ge.gift_name != null && ge.gift_name.length()>0){
			type = isChineseEnglish(ge.gift_name.charAt(0));
		}

		view.setMaxLines(2);
		view.setMaxTextSize(12);
		view.setMinTextSize(3);
		
		String bttxt=ge.gift_name;
		if(type == 0 && ge.gift_name.length()>LETTER_MAX_LINE){
			bttxt=ge.gift_name.substring(0, LETTER_MAX_LINE-1)+"\n"+ge.gift_name.substring(LETTER_MAX_LINE-1);
		}else if(type != 0  && ge.gift_name.length()>CHAR_MAR_LINE){
			bttxt=ge.gift_name.substring(0, CHAR_MAR_LINE-2)+"\n"+ge.gift_name.substring(CHAR_MAR_LINE-2);
		}else{

			view.setMaxLines(1);
			view.setMaxTextSize(12);
			view.setMinTextSize(3);
		}

		view.setText(bttxt);
		txtSwitcher.setText(ge.count+"");
	}

	public class DealThread extends Thread {
		int gift_dvalue;
		int index;
		public DealThread(){}
		public DealThread(int dvalue,int index){
			setGift(dvalue,index);
		}

		public synchronized int getGift() {
			while (gift_dvalue == 0) {
				try {
					this.wait();
				} catch (InterruptedException ie) {
					return 0;
				}
			}
			int dvalue=gift_dvalue;
			gift_dvalue=0;
			HupuLog.e("DealThread", "getGift="+dvalue);
			return dvalue;
		}

		public synchronized void setGift(int dvalue,int index) {
			if(dvalue<=0){
				return;
			}
			HupuLog.e("DealThread", "setGift="+dvalue);
			this.gift_dvalue = dvalue;
			this.index=index;
			this.notify();
		}

		public void run() {
			while (!exit) {
				int dvalue = getGift();
				int times=1000/push_gift_count_add_times;
				int interz= dvalue/push_gift_count_add_times;
				int push_count_interadd =(interz>0?interz:1);
				while(dvalue>0 && !exit){
					GiftEntity localgift = giftList.get(index);
					dvalue-=push_count_interadd;
					localgift.count+=push_count_interadd;
					//					HupuLog.e("DealThread", "localgift.count="+localgift.count+",dvalue="+dvalue);
					if(dvalue<0){
						localgift.count+=dvalue;
					}
					sendMessage(index, localgift);
					try {
						Thread.sleep(times);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

