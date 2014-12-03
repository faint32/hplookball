package com.hupu.games.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.BaseGameActivity;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.adapter.LiveDataListAdapter;
import com.hupu.games.casino.CasinoDialog;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuSchemeProccess;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseLiveResp;
import com.hupu.games.data.BaseLiveResp.CasinoStatus;
import com.hupu.games.data.IncreaseEntity;
import com.hupu.games.data.LiveEntity;
import com.hupu.games.data.LiveEntity.Answer;
import com.hupu.games.data.goldbean.ExchangeGoldBeanEntity;
import com.hupu.games.data.goldbean.GoldBeanManager;
import com.hupu.games.pay.BasePayActivity;
import com.hupu.games.pay.PhoneInputActivity;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;

/**
 * 直播fragment
 * */
@SuppressLint("ValidFragment")
public class LiveFragment extends BaseFragment {

	private XListView mLvLive;

	private LiveDataListAdapter mListAdapter;

	private int i_homeId;
	private int i_awayId;

	private View mProgressBar;

	private ArrayList<LiveEntity> mListMsg;

	BaseGameActivity mAct;


	// View nodata;

	WebView noData;
	private String mPreview;

	View imgNew;

	private HashMap<Integer, LiveEntity> mapCasino;

	// private HashMap<Integer,Integer> mapCommit;

	private boolean bGetData;

	private int[] bets;

	View casinoBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAct = (BaseGameActivity) getActivity();
		mapCasino = new HashMap<Integer, LiveEntity>();
		// mapCommit= new HashMap<Integer, Integer>();
	}

	@SuppressLint("ValidFragment")
	public LiveFragment(int h, int a, boolean isStart) {
		super();
		i_homeId = h;
		i_awayId = a;
		isStart(isStart);
	}

	public LiveFragment() {
		super();
		isStart(false);
	}

	boolean start;
	String url;

	public void setURL(String u) {
		url = u;
		//前瞻不和比赛状态绑定
//		if (!start && noData != null & url != null)
//			noData.loadDataWithBaseURL(null, getContent(url), mimeType,
//					encoding, null);
		
		if (noData != null && url != null){
			noData.setVisibility(View.VISIBLE);
			noData.loadDataWithBaseURL(null, getContent(url), mimeType,
					encoding, null);
		}
	}
	
	public void setPreview(String previewStr){
		mPreview = previewStr;
		if (noData!= null) {
			noData.setVisibility(View.GONE);
		}
	}

	public void setURLForCBA(String u) {
		url = u;
		mPreview = url;
		if (noData != null && url != null){
			noData.setVisibility(View.VISIBLE);
			noData.loadDataWithBaseURL(null, getContent(url), mimeType,
					encoding, null);
		}
	}
	/** 比赛是否开始了 */
	public void isStart(boolean s) {
		start = s;
		if (start && noData != null)
			noData.setVisibility(View.GONE);
		if (!start && noData != null & url != null)
			noData.loadUrl(url);
	}


	private static final String mimeType = "text/html";
	private static final String encoding = "utf-8";
	private String getContent(String c) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<head>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
				+ "<title></title>" + "</head>" + "<body>" + c
				+ "</body></html>");
		return sb.toString();
	}

	Click click;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {



		View v = inflater.inflate(R.layout.fragment_live, container, false);
		mProgressBar = v.findViewById(R.id.probar);
		noData = (WebView) v.findViewById(R.id.webview_no_data);
		if (mPreview != null&&!"".equals(mPreview))
			noData.setVisibility(View.VISIBLE);
		else 
			noData.setVisibility(View.GONE);
		
		noData.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent in = new Intent(getActivity(), WebViewActivity.class);
				in.putExtra("url", url);
				startActivity(in);
				return true;
			}
		});
		if ((bGetData && mProgressBar != null) || !start)
			mProgressBar.setVisibility(View.GONE);
		if (start)
			noData.setVisibility(View.GONE);
		else if (url != null)
			noData.loadDataWithBaseURL(null, getContent(url), mimeType,
					encoding, null);
		click = new Click();
		mListAdapter = new LiveDataListAdapter(getActivity(), i_homeId,
				i_awayId, click);
		mLvLive = (XListView) v.findViewById(R.id.list_live);
		mLvLive.setXListViewListener(new pullListener());
		mLvLive.setPullLoadEnable(false, false);
		mLvLive.setAdapter(mListAdapter);

		if (mListMsg != null || bGetData) {
			mListAdapter.setData(mListMsg);
		}
		return v;
	}

	

	public void setData(ArrayList<LiveEntity> gameList) {
		bGetData = true;
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		mListMsg = gameList;
		if (mListMsg != null) {
			int size = mListMsg.size();
			LiveEntity en;
			for (int i = 0; i < size; i++) {
				en = mListMsg.get(i);
				if (en.type == 1) {
					mapCasino.put(en.answers[0].casino_id, en);
				}
			}
		}
		if (mListAdapter != null) {
			mListAdapter.setData(mListMsg);
		}
	}

	public void setBets(int[] b) {
		bets = b;
	}

	/** 添加到列表尾部 */
	public void appendData1(ArrayList<LiveEntity> gameList) {
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		if (gameList != null) {
			int size = gameList.size();
			LiveEntity en;
			for (int i = 0; i < size; i++) {
				en = gameList.get(i);
				if (en.type == 1) {
					mapCasino.put(en.answers[0].casino_id, en);
				}
			}
		}
		if (mListAdapter != null)
			mListMsg = mListAdapter.appendData(gameList);
	}

	/** 添加到列表头部 */
	public void addData1(ArrayList<LiveEntity> gameList) {
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		if (gameList != null) {
			int size = gameList.size();
			LiveEntity en;
			for (int i = 0; i < size; i++) {
				en = gameList.get(i);
				if (en.type == 1) {
					mapCasino.put(en.answers[0].casino_id, en);
				}
			}
		}
		if (mListAdapter != null)
			mListMsg = mListAdapter.addDataToHead(gameList);

	}

	public void addData(boolean b) {
		bGetData = b;
		if (bGetData && mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
	}

	public void updateData(BaseLiveResp data) {

		bGetData = true;
		if (mProgressBar != null)
			mProgressBar.setVisibility(View.GONE);
		if (data.casinoList != null) {
			Log.d("updateData", "size=" + data.casinoList.size());
			updateData(data.casinoList);
		}
		if (data.mListDel == null && data.mListAdd == null) {
			// addData(data.dataList );
			return;
		}

		int delSize = data.mListDel.size();
		int addSize = data.mListAdd.size();
		int rowSize = delSize > addSize ? delSize : addSize;

		int colunmSize = 0;
		int[] indexArr;
		if (rowSize > 0) {
			LinkedList<LiveEntity> entityList;
			LiveEntity en;
			if (mListMsg == null)
				mListMsg = new ArrayList<LiveEntity>();
			for (int i = 0; i < rowSize; i++) {
				if (i < delSize) {
					indexArr = data.mListDel.get(i);
					colunmSize = indexArr.length;
					// 删除
					for (int j = colunmSize - 1; j > -1; j--) {
						mListMsg.remove(indexArr[j]);
					}
				}
				if (i < addSize) {
					// 添加
					indexArr = data.mListAdd.get(i);
					entityList = data.mListMsg.get(i);
					colunmSize = indexArr.length;
					int msgSize = 0;
					for (int j = 0; j < colunmSize; j++) {
						msgSize = mListMsg.size();
						en = entityList.get(j);
						if (indexArr[j] > msgSize)
							mListMsg.add(en);
						else
							mListMsg.add(indexArr[j], en);

						if (en.type == 1)
							mapCasino.put(en.answers[0].casino_id, en);

					}
				}
			}
			mListAdapter.setData(mListMsg);
		}

	}

	private void refresh() {
		if (mListAdapter != null && mListAdapter.getCount() > 0)
			mAct.reqFresh();
		else
			stopLoad();
	}

	public void stopLoad() {
		if (mLvLive != null)
			mLvLive.stopRefresh();
	}

	class pullListener implements IXListViewListener {

		Handler handler = new Handler();

		@Override
		public void onRefresh() {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			}, 800);
		}

		@Override
		public void onLoadMore() {

		}
	}

	Dialog mImgDialog;
	WebView webview;
	Handler mImgHandler;
	View progress;

	/** 放大图片 */
	public void showImgDialog(final String url) {
		if (url == null)
			return;
		if (mImgHandler == null)
			mImgHandler = new Handler();

		if (mImgDialog != null && mImgDialog.isShowing()) {
			mImgDialog.dismiss();
		}
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_img, null);
		progress = v.findViewById(R.id.probar);

		v.findViewById(R.id.mask).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mImgDialog.cancel();
			}
		});
		// gifView = (GifView) v.findViewById(R.id.btn_close);
		// gifView.setGifImageType(GifImageType.WAIT_FINISH);

		webview = (WebView) v.findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.setBackgroundColor(Color.argb(0, 0, 0, 0));
		webview.setVisibility(View.INVISIBLE);
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress >= 100) {
					progress.setVisibility(View.GONE);
					webview.setVisibility(View.VISIBLE);
				}
			}

		});
		mImgHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				webview.loadDataWithBaseURL(null, String.format(IMG_URL, url),
						"text/html", "utf-8", null);
			}
		}, 300);

		mImgDialog = new Dialog(getActivity(), R.style.MyWebDialog);
		mImgDialog.setContentView(v);
		// mImgDialog.setCanceledOnTouchOutside(true);
		// mImgDialog.setCancelable(true);
		mImgDialog.getWindow().setGravity(Gravity.CENTER);
		mImgDialog.show();
		mImgDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

	}
/**
 * 下注对话框
 * **/
	CasinoDialog mCasinoDialog;

	private Answer mAnswer;

	/** 显示下注框 */
	public void showCasinoDialog(final Answer answer, int blc) {

		if(mCasinoDialog!=null && mCasinoDialog.isShowing())
		{
			return ;
		}
		if (bets != null && bets.length > 4) {
			
			
			mAnswer = answer;
			bets[bets.length-1] = mapCasino.get(answer.casino_id).max_bet;// 代表后台返回最大下注金额，0代表无限
			mCasinoDialog = new CasinoDialog(getActivity(), click, bets,
					mapCasino.get(mAnswer.casino_id).isCasino > 0);
			mCasinoDialog.goShow(mAnswer);
			if (mAct instanceof BasePayActivity) {
				((BasePayActivity) mAct).reqBitCoin(answer.casino_id);
			}
			
			//直播流投注 友盟事件
//			if (mapCasino.get(mAnswer.casino_id).isCasino > 0)  				 //加注
//				mAct.sendUmeng(HuPuRes.UMENG_EVENT_LIVE_RAISE_LINK);
//			else 															 //投注
//				mAct.sendUmeng(HuPuRes.UMENG_EVENT_LIVE_ANSWER_LINK,HuPuRes.UMENG_KEY_ANSWER_POS,answer.answer_id ==1 ? "Left":"Right");
		}
	}



	/** 显示绑定对话框 */
	private void showBindNotify() {
		mAct.showBindDialog(SharedPreferencesMgr.getString("dialogQuize", getString(R.string.casino_notify)));
//		mAct.showCustomDialog(mAct.DIALOG_SHOW_BIND_PHONE,
//				SharedPreferencesMgr.getString("dialogQuize", getString(R.string.casino_notify)), BaseGameActivity.TOW_BUTTONS,
//				R.string.bind_phone, R.string.cancel);
	}

	private void showChargeNotify(int bet) {
		String s =mAct.getResources().getString(R.string.no_charge1);
		mAct.showCustomDialog(mAct.DIALOG_SHOW_CHARGE_NOTIFY, 
				s.format(s, bet), BaseGameActivity.TOW_BUTTONS,
				R.string.buy_at_once,R.string.cancel);
	}


	/** 绑定*/
	private void switchToBindPhone() {
		Intent intent = new Intent(getActivity(), PhoneInputActivity.class);
		startActivityForResult(intent, HupuBaseActivity.REQ_GO_BIND_PHONE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == HupuBaseActivity.REQ_GO_BIND_PHONE) {
			// 绑定手机
			if (resultCode == Activity.RESULT_OK) {

			}
		} else if (requestCode == HupuBaseActivity.REQ_SHOW_QUIZLIST) {
			// 同步竞猜页面的操作
			if (resultCode == Activity.RESULT_OK) {
				
			}

		}
	}


	public void updateBet(ArrayList<IncreaseEntity> list )
	{
		HupuLog.e("papa", "更新下注");
		if (list != null) {
			LiveEntity en;
			for (IncreaseEntity ien : list) {
				HupuLog.d("qid", "="+ien.qid);
				en = mapCasino.get(ien.qid);
				if (en != null) {
					en.isCasino = ien.answerId;
				}
			}
			mListAdapter.notifyDataSetChanged();
		}
	}
	

	public void updateMoney(int betCoin, int balance) {
		if (mCasinoDialog != null && mCasinoDialog.isShowing()) {
			mCasinoDialog.setBet(betCoin, balance);
		}
	}

	boolean isIncrease;

	public void sendQuizCommit() {
		// --
		try {
			if (mCasinoDialog.getInputCoin() > 0) {
				// 加注
				if (mapCasino.get(mAnswer.casino_id) != null) {
					isIncrease = mapCasino.get(mAnswer.casino_id).isCasino > 0;
					// Log.d("sendQuizCommit", "increase=" + increase);
				}
				mAct.sendQuizCommit(mAnswer, mCasinoDialog.getInputCoin(),
						isIncrease);
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}

	/**余额和下注金额的差额*/
	int dif;
	public void showNoMoney(ExchangeGoldBeanEntity glodn)
	{
		if(glodn==null)
		{
			return;
		}
//		showChargeNotify(glodn.content,glodn.status);//没钱提示去冲钱  
//		GoldBeanManager.getInstance().onExchangeGoldBeanDiaog(mAct, glodn);
		if(!mAct.isExchange)
		{
			GoldBeanManager.getInstance().onExchangeGoldBeanDiaog((BasePayActivity) mAct, glodn);
		}
	}
	class Click implements OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_cancel:
				if (mCasinoDialog != null)
					mCasinoDialog.dismiss();
				break;
			case R.id.btn_confirm:
//				cb =mCasinoDialog.canBet();
				dif=mCasinoDialog.getBetDif();
				mAct.isExchange = false;
				sendQuizCommit();
//                if(cb>0 )
//                	sendQuizCommit();
//                else if(cb==-1)
//                	showChargeNotify(mCasinoDialog.getBetDif());//没钱提示去冲钱                
				if (mCasinoDialog != null)
					mCasinoDialog.dismiss();
				break;

			default:

				if (v instanceof Button) {
					// 从列表点击猜题
					if (mAct.mToken == null||mAct.mToken.length()<8) {
						// 先要弹个高科技的提示框，提示用户充钱
						showBindNotify();
					} else {
						showCasinoDialog((Answer) v.getTag(), 0);
					}
				} else if (v instanceof TextView) {
					
					String url = (String) v.getTag();
					HupuLog.d("live url="+url);
					if (url != null) {
						Uri uri = Uri.parse(url);
						if ("kanqiu".equalsIgnoreCase(uri.getScheme())) {
							HupuSchemeProccess.proccessScheme(getActivity(), uri);
						}else
						{
							Intent in = new Intent(getActivity(),
									WebViewActivity.class);
							in.putExtra("url", url);
							startActivity(in);
						}
						
					}

				} else if (v instanceof ImageView) {
					String url = (String) v.getTag();
					showImgDialog(url);
				}
				break;
			}

		}

	}

	public final static String IMG_URL = "<div style=\"display:table\" id=\"JPicWrap\">"
			+ "<div style=\"display:table-cell;text-align:center;vertical-align:middle;horizontal-align:middle\">"
			+ "<img src=\"%s\" alt=\"\">"
			+ "</div>"
			+ "</div>"
			+ "<script type=\"text/javascript\">"
			+ "window.onload = function(){"
			+ "clientH = document.documentElement.clientHeight||document.body.clientHeight;"
			+ "document.getElementById('JPicWrap').style.height = clientH+'px';"
			+ "clientW = document.documentElement.clientWidth||document.body.clientWidth;"
			+ "document.getElementById('JPicWrap').style.width = clientW+'px';"
			+ "}" + "</script>";

	public class ImagePreviewDialog extends Dialog {
		private ImageView mImageView;

		public ImagePreviewDialog(Context context, int theme) {
			super(context, theme);
			Window win = getWindow();
			WindowManager.LayoutParams wAttrs = win.getAttributes();
			win.setFlags(0, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			win.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
			setCanceledOnTouchOutside(true);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			Context ctxt = getContext();
			mImageView = new ImageView(ctxt);
			mImageView.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			setContentView(mImageView);
		}
	}
	/** 更新竞猜状态 */
	public void updateBetData(ArrayList<IncreaseEntity> list)
	{
		if (list != null) {
			LiveEntity en;
			for (IncreaseEntity ien : list) {
				HupuLog.d("qid", "="+ien.qid);
				en = mapCasino.get(ien.qid);
				if (en != null) {
					en.isCasino = ien.answerId;
				}
			}
			mListAdapter.notifyDataSetChanged();
		}
	}
	/** 更新数据 */
	private void updateData(ArrayList<CasinoStatus> casino) {
		int size = casino.size();
		CasinoStatus cs;
		LiveEntity en;
		for (int i = 0; i < size; i++) {
			cs = casino.get(i);
			// Log.d("updateData", "id=" + cs.casino_id + " count=" +
			// cs.userCount);
			en = mapCasino.get(cs.casino_id);
			if (en != null) {
				en.userCount = cs.userCount;
				en.quizStatus = cs.status;
				en.quizStr = cs.desc;
				en.rightId = cs.rightId;
				// Log.d("updateData en", "str=" + en.quizStr + " count="
				// + en.userCount);
			}
		}
		mListAdapter.notifyDataSetChanged();
	}

	/** 更新数据 */
	public void updateCommit(int qid, int aid) {
		LiveEntity en = mapCasino.get(qid);
		if (en != null) {
			en.isCasino = aid;
			mListAdapter.notifyDataSetChanged();
		}
	}
	
	public JSONArray getQids()
	{
		Iterator <Integer> keys=mapCasino.keySet().iterator();
		if(keys==null)
			return null;
		
		int qid;
		LiveEntity en;
		JSONArray arr=null;
		while(keys.hasNext())
		{
			qid =keys.next();
			en = mapCasino.get(qid);
			if(en!=null)
			{
				Log.d("getQids", "en="+en.quizStatus);
				if(en.quizStatus  ==1 ||en.quizStatus  ==2)
				{
					if(arr == null)
						arr =new JSONArray();
					arr.put(qid);
					Log.d("getQids", "qid="+qid);
				}
			}
		}
		return arr;
	}
}
