/**
 * 
 */
package com.hupu.games.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.goldbean.GoldBeanWebViewActivity;
import com.hupu.games.adapter.QuizListingAdapter;
import com.hupu.games.casino.CasinoDialog;
import com.hupu.games.casino.MyBoxActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.BitCoinReq;
import com.hupu.games.data.IncreaseEntity;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.game.quiz.QuizEntity;
import com.hupu.games.data.game.quiz.QuizListResp;
import com.hupu.games.data.game.quiz.QuizResp;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.view.PinnedHeaderXListView;
import com.hupu.games.view.PinnedHeaderXListView.IXListViewListener;
import com.hupu.http.HupuHttpHandler;
import com.pyj.http.RequestParams;

/**
 * @author papa 上墙榜 需要lid和gid
 */
public class QuizListActivity extends BasePayActivity {

	PinnedHeaderXListView mListView;

	QuizListingAdapter mAdapter;
	Intent in;
	LayoutInflater mInflater;
	View v;
	TextView userInfo, coinNum ,boxNum;
	int openQid = 0;
	private View topInfo;
	private boolean isIncrease = false;
	
	private int []bets;
	
	String CoinNum = "0";

	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                 case 1: 
                	 getQuizList();
                	 break;   
             }   
             super.handleMessage(msg);   
        }   
   }; 
   
   //关闭定时器
//   TimerTask task = new TimerTask(){  
//	          public void run() {  
//	          Message message = new Message();      
//	          message.what = 1;      
//	          myHandler.sendMessage(message);    
//	       }  
//	    }; 
	    
	//private Timer timer = new Timer(true);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_quiz_list);
		coinNum = (TextView) findViewById(R.id.coin_num);
		boxNum = (TextView) findViewById(R.id.box_num);

		mInflater = LayoutInflater.from(this);

		initParameter();
		in = getIntent();
		mParams.put("lid", "" + in.getIntExtra("lid", 1));
		mParams.put("gid", "" + in.getIntExtra("gid", 0));
		mParams.put("token", mToken);
		bets = in.getIntArrayExtra("bets");
		
		openQid = in.getIntExtra("qid", 0);
		//openQid = 10;
		mListView = (PinnedHeaderXListView) findViewById(R.id.list_player);
		
		topInfo = LayoutInflater.from(this).inflate(
				R.layout.quiz_list_head, null);
		userInfo = (TextView) topInfo.findViewById(R.id.user_info);
		mListView.addHeaderView(topInfo);
		
		mListView.setPullLoadEnable(false, false);
		
		mListView.setXListViewListener(new pullListener());
		
		mAdapter = new QuizListingAdapter(this, click);

		mListView.setAdapter(mAdapter);

		setOnClickListener(R.id.btn_back);
		setOnClickListener(R.id.user_quiz_info);
		setOnClickListener(R.id.gold_num);
		//timer.schedule(task,1, 30000); //延时1000ms后执行，1000ms执行一次

	}
	
	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			//加载最新竞猜数据
			//mListView.setFreshState();
			getQuizList();
		}

		@Override
		public void onLoadMore() {
			
		}

	}

	private Answer answer;
	private QuizResp quizList;
	
	CasinoDialog mCasinoDialog;
	
	/**  */
	public void showCasinoDialog(final Answer answer, int blc,boolean isB) {

		reqBitCoin(answer.casino_id);
		bets[6] = answer.max_bet;
 		mCasinoDialog = new CasinoDialog(this, click,bets,isB);
		mCasinoDialog.goShow(answer);
	}
	
	
	
	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);

		switch (id) {
		case R.id.btn_back:
			if (quizList != null && quizList.list !=null) {
				getJoinQuiz();
			}
			Intent quizzedIntent = new  Intent();
			quizzedIntent.putExtra("increase", (Serializable)increaseList);
			setResult(RESULT_OK,quizzedIntent);
			
			finish();
			break;
		case R.id.user_quiz_info:
			
			Intent intent = new Intent(this, GuessRankActivity.class);
			intent.putExtra("lid", in.getIntExtra("lid", 1));
			intent.putExtra("gid", in.getIntExtra("gid", 0));
			intent.putExtra("rank_type", 0);
			startActivity(intent);
			break;
		case R.id.gold_num:
				
				Intent info = new Intent(this, UserInfoActivity.class);
				startActivity(info);
			
			break;
		case R.id.btn_cancel:
			isIncrease = false;
			if (mCasinoDialog != null)
				mCasinoDialog.dismiss();
			
			break;
		case R.id.btn_confirm:
			
			int cb =mCasinoDialog.canBet();
            if(cb>0 )
            {
            	isExchange = false;
            	sendQuizCommit(answer, mCasinoDialog.getInputCoin());
            }
            else if(cb==-1)
            	showChargeNotify(mCasinoDialog.getBetDif());//没钱提示去冲钱       
            
			if (mCasinoDialog != null)
				mCasinoDialog.dismiss();
			
			break;

		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_GO_POST_ORDER) {
			if(data!=null)
			{
				isExchange = true;	
			 	sendQuizCommit(answer, mCasinoDialog.getInputCoin());
			}
		}
	}



	@Override
	public void treatClickEvent(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_answer1:
			if (mToken == null) {
				// 需要登录
				switchToPhoneBindAct();
			} else {
				
				answer = (Answer) view.getTag();
				if (answer != null) {
					if (!mCasinoDialog.isShowing()) {
						showCasinoDialog(answer, 0,false);
					}
				}
			}
			break;
		case R.id.btn_answer2:
			if (mToken == null) {
				// 需要登录
				switchToPhoneBindAct();

			} else {
				
				answer = (Answer) view.getTag();
				if (answer != null) {
					if (!mCasinoDialog.isShowing()) {
						showCasinoDialog(answer, 0,false);
					}
				}
			}
			break;
		case R.id.open_result:
			if (mToken == null) {
				// 需要登录
				switchToPhoneBindAct();
			} else {
				
				Intent info = new Intent(this, UserGoldActivity.class);
				startActivity(info);
			}
			break;
//		case R.id.box_img:
//			Intent box = new Intent(this, MyBoxActivity.class);
//			startActivityForResult(box, REQ_SHOW_BOX);
//			break;
		case R.id.pop_box_toast:
			Intent pop = new Intent(this, MyBoxActivity.class);
			startActivityForResult(pop, REQ_SHOW_BOX);
			break;
		case R.id.btn_increase:
			if (mToken == null) {
				// 需要登录
				switchToPhoneBindAct();

			} else {
				
				isIncrease = true;
				answer = (Answer) view.getTag();
				if (answer != null) {
					showCasinoDialog(answer, 0,true);
				}
			}
			break;
		
		default:
			break;
		}
		
	}

	public void switchToPhoneBindAct() {
		//dialog(QuizListActivity.this);
	}

	public void getQuizList() {
		initParameter();
		mParams.put("lid", "" + in.getIntExtra("lid", 1));
		mParams.put("gid", "" + in.getIntExtra("gid", 0));
		mParams.put("token", mToken);
		sendRequest(HuPuRes.REQ_METHOD_QUIZ_LIST, mParams, new HupuHttpHandler(
				this), false);
	}

	private void setSelection(QuizResp list) {
		int size = 0;
		int i = 0;
		for (QuizListResp quizList : list.list) {
			
			if (quizList.status == 2) {
				
				for (QuizEntity entity : quizList.mQuizList) {
					if (entity.qid == openQid) {
						size =size + i;
						break;
					}
					i++;
				}
				break;
			}else
				size = size + quizList.mQuizList.size();
		}
		size = size +list.list.size()+1;
		if (size >0 &&size < mAdapter.getCount()) {
			//mListView.setSelection(size);
			mListView.setSelection(size);
		}
		openQid = 0;
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		//super.onReqResponse(o, methodId);
		//
		if (methodId == HuPuRes.REQ_METHOD_QUIZ_LIST) {

			if (o != null) {
				quizList = (QuizResp) o;
				mAdapter = new QuizListingAdapter(this, click);

				//mAdapter.setData(quizList.list);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				
				mListView.stopRefresh();
				mListView.stopLoadMore();
				
				//直播列表传来的
				if (openQid != 0) {
					setSelection(quizList);
				}
				
				int winCoin = quizList.coin > 0 ? quizList.coin
						: 0;
				if (quizList.join > 0) {
					topInfo.setVisibility(View.VISIBLE);
				}

				userInfo.setText(Html.fromHtml("参与" + quizList.join
						+ "场竞猜，猜赢" + quizList.win + (quizList.win <=0 ? "场，暂无排名":"场，排名第"+ quizList.winRank)
						 + "，总奖金" + winCoin + (winCoin <= 0 ? "金豆，暂无排名" : "金豆，排名第" + quizList.coinRank)));
				
				//宝箱数 未0 宝箱图标隐藏 数字不显示0
				findViewById(R.id.box_img).setVisibility(quizList.boxNum > 0 ? View.VISIBLE : View.GONE);
				boxNum.setText(quizList.boxNum > 0 ? quizList.boxNum+"" : "");
				boxNum.setVisibility(quizList.boxNum > 0 ? View.VISIBLE : View.GONE);
				coinNum.setText("" + (quizList.balance >1000000 ? (quizList.balance/10000)+"万":quizList.balance));
				
				if (quizList.balance == 0) {
					CoinNum ="0";
				}else if (quizList.balance > 0 && quizList.balance <10) {
					CoinNum ="<10";
				} else if (quizList.balance >= 10 && quizList.balance <100) {
					CoinNum ="10-100";
				} else if (quizList.balance >= 100 && quizList.balance <1000) {
					CoinNum ="100-1000";
				}else if (quizList.balance >= 1000) {
					CoinNum =">1000";
				}
			}

		} else if (methodId == HuPuRes.REQ_METHOD_QUIZ_COMMIT || methodId == HuPuRes.REQ_METHOD_POST_INCREASE) {
			// 提交竞猜
			QuizCommitResp entity = (QuizCommitResp) o;
			if (entity.result == -1)
				showToast("您已经参加过该竞猜了");
			else if (entity.result == 1){
				getQuizList();
				showToast("参与成功");
			}
			else if (entity.result == 2) {
				getQuizList();
				// 通知领救济金
				showCustomDialog(DIALOG_SHOW_GET_DOLE, 0,
						R.string.get_now_info, ONE_BUTTON, R.string.title_confirm,
						0);
			} else if (entity.result == -2) {

//				showToast("金豆余额不足");
				
				if(!isExchange)
				{
					mFragmentLive.showNoMoney(entity.eGoldBean);
				}
			} else if (entity.result == -4) {
				showToast("该竞猜已关闭");
			}else{
				showToast("投注金额超出限额");
			}
		}else if (methodId ==HuPuRes.REQ_METHOD_BET_COINS)
		{
			BitCoinReq req =(BitCoinReq)o;
			if(req.balance ==-1)
			{
				showToast("token无效，请重新登录");
			}
			else
			{
				updateMoney(req.betCoins,req.balance);
			}
		}
	}
	

	// 更新余额
	public void updateMoney(int betCoin,int balance){
		if (mCasinoDialog != null && mCasinoDialog.isShowing()) {
			mCasinoDialog.setBet(betCoin, balance);
		}
	}

	/** 请求余额 */
	public void reqBitCoin(int qid)
	{
		if(mToken != null)
		{
			initParameter();
			mParams.put("token", mToken);
			mParams.put("qid", ""+qid);
			sendRequest(HuPuRes.REQ_METHOD_BET_COINS, mParams, new HupuHttpHandler(
					this), false);
		}
	}


	public void sendQuizCommit(Answer answer, int coin) {
		//
		RequestParams p = new RequestParams();
		p.clear();
		p.put("client", mDeviceId);
		p.put("qid", "" + answer.casino_id);
		p.put("answer", "" + answer.answer_id);
		p.put("coin", "" + coin);
		p.put("roomid", roomid+"");
		if (mToken != null && coin > 0)
			p.put("token", mToken);
		if (isIncrease) {
			isIncrease = false;
			sendRequest(HuPuRes.REQ_METHOD_POST_INCREASE, p, new HupuHttpHandler(
					QuizListActivity.this), false);
		}else {
			p.put("lid", "" + in.getIntExtra("lid", 1));
			p.put("gid", "" + in.getIntExtra("gid", 0));
			sendRequest(HuPuRes.REQ_METHOD_QUIZ_COMMIT, p, new HupuHttpHandler(
					QuizListActivity.this), false);
		}
	}
	
	private void showChargeNotify(int bet) {
		String s =getResources().getString(R.string.no_charge1);
		showCustomDialog(DIALOG_SHOW_CHARGE_NOTIFY, 
				s.format(s, bet), BaseGameActivity.TOW_BUTTONS,
				R.string.buy_at_once,R.string.cancel);
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getQuizList();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (quizList != null && quizList.list !=null) {
				getJoinQuiz();
			}
			Intent quizzedIntent = new  Intent();
			quizzedIntent.putExtra("increase", (Serializable)increaseList);
			setResult(RESULT_OK,quizzedIntent);
			
			finish();
		}
		return false;
	}
	
	public ArrayList<IncreaseEntity> increaseList;
	private void getJoinQuiz(){
		IncreaseEntity increase;
		increaseList = new ArrayList<IncreaseEntity>();
		for (QuizListResp listResp:quizList.list) {
			if (listResp.status == 0) {
				for(QuizEntity entity : listResp.mQuizList){
					if (entity.is_user_join == 1) {
						increase = new IncreaseEntity();
						increase.qid = entity.qid;
						increase.answerId = entity.user_choose;
						increaseList.add(increase);
					}
				}
			}
		}
	}

}
