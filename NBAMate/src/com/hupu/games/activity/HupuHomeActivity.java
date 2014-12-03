package com.hupu.games.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.adapter.HomePageAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.BaseGameEntity;
import com.hupu.games.data.FollowResp;
import com.hupu.games.data.JsonPaserFactory;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.SendMsgResp;
import com.hupu.games.data.SidebarEntity;
import com.hupu.games.data.game.basketball.BasketBallGamesBlock;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.dialog.EditDialog;
import com.hupu.games.fragment.BaseFragment;
import com.hupu.games.fragment.BaseGameFragment;
import com.hupu.games.fragment.BasketballGamesFragment;
import com.hupu.games.fragment.DiscoveryFragment;
import com.hupu.games.fragment.SoccerGamesFragment;
import com.hupu.games.hupudollor.activity.UserHupuDollorInfoActivity;
import com.hupu.games.pay.AccountActivity;
import com.hupu.games.pay.PhoneInputActivity;
import com.hupu.http.HupuHttpHandler;
import com.pyj.common.DeviceInfo;
import com.pyj.common.DialogRes;
import com.pyj.http.AsyncHttpResponseHandler;
import com.pyj.http.RequestParams;
import com.slidingmenu.lib.SlidingMenu;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.update.UmengUpdateAgent;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 看球首页
 * 
 * @author panyongjun
 * */
public class HupuHomeActivity extends HupuBaseActivity {

	private LinkedList<LeaguesEntity> leagueList;

	private String TABS[] = { HuPuRes.TAB_GAMES, HuPuRes.TAB_NEWS,
			HuPuRes.TAB_VIDEO, HuPuRes.TAB_RANKS, HuPuRes.TAB_DATA };

	private final static int INDEX_GAME = 0;
	private final static int INDEX_NEWS = 1;
	private final static int INDEX_VIDEO = 2;
	private final static int INDEX_STANDINGS = 3;
	private final static int INDEX_NBA_DATA = 4;
	private int pageIndex;

	/** 左菜单 */
	private SlidingMenu mSideMenu;

	private static String SORRY_NOTIFY = "抱歉，%s vs %s闹钟设置失败";

	private static String SUCCESS_NOTIFY = "闹钟设置成功，您将会收到%s vs %s的推送通知";

	private static String CANCEL_NOTIFY = "闹钟取消成功";

	/** 左侧菜单适配器 */
	// LeftMenuAdapter menuAdapter;
	/** 当前选中碎片的索引 */
	private int mCurTabIndex = -1;

	/** 接口需要的参数 */
	private String curTag;

	ImageButton mBtnGame;
	ImageButton mBtnNews;
	ImageButton mBtnVideo;
	ImageButton mBtnStandings;
	ImageButton mBtnData;
	/**判断是否从scheme跳转进入了其他页面*/
	boolean bScheme;

	LinearLayout showMoreLayout, menuTopLayout;

	/** 提示用户关注球队需要打开接收通知对话框 */
	private final int DIALOG_NOTIFY = 1311;

	BaseGameEntity followEntity;

	BaseGameFragment followFragment;

	BaseFragment curFragment;

	/** 是否显示了帮助 */
	boolean showGuide;

	int delay = 1000;

	/*
	 * 是否初始化了赛程页，由于会有外部跳转的可能，因此可能没有得到初始化页面就直接进入了
	 */
	boolean bInitGameTag;
	/** 如果是从外部搜索source跳转进入，则需要在返回时退回到搜索app */
	boolean bQuitBySource;

	HupuScheme scheme;

	View mBottomBar;

	Handler mHandler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null)
			savedInstanceState.clear();
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		mHandler = new Handler();
//		init();
		Intent in = getIntent();
		scheme = (HupuScheme) in.getSerializableExtra("scheme");
//		scheme =new HupuScheme();
//		scheme .paser(Uri.parse("app://nba/nba/news/1345504"));
		if (scheme == null) {
			// 默认进入左侧菜单第一个行的联赛
			startDefaultFragment();
		} else {
			// treatScheme();
		}
		checkUMengUpdate();
	}

	private void checkUMengUpdate() {
		// umeng
		UmengUpdateAgent.update(this);
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
	}

	/** 处理scheme的跳转问题 */
	private void treatScheme() {
		// scheme =new HupuScheme();
		// scheme .paser(Uri.parse("app://soccercupleagues/worldcup/stats/59"));
		HupuLog.d("treatScheme====" + scheme.template+"----tab="+scheme.mode);
		boolean bFromSource = scheme.getParameter("r") != null;

		HupuLog.d(scheme.mUri + " nquit=" + bFromSource);

		// umeng事件，提交来源
		if (bFromSource) {
			sendUmeng(HuPuRes.UMENG_EVENT_VISIT, scheme.mode,
					scheme.getParameter("r"));
		}

		if (HuPuRes.TAB_NEWS.equalsIgnoreCase(scheme.mode)) {

			if (scheme.id > 0) {
				// 跳转到新闻页详情
				Intent in = new Intent(this, NewsDetailActivity.class);
				in.putExtra("nid", (long) scheme.id);
				in.putExtra("reply", 0);
				in.putExtra("tag", scheme.game);
				startActivity(in);
				bQuitBySource = bFromSource;
				bScheme=true;
			} else {
				// 第一级的TAB
				startFragment( scheme.template, scheme.game, scheme.mode);
			}
			return;
		}

		if (HuPuRes.SUB_TAB_CASINO.equals(scheme.mode)) {
			if (scheme.id > 0) {
				Intent in = null;
				if (HuPuRes.TEMPLATE_SOCCER_LEAGUE
						.equalsIgnoreCase(scheme.template)
						|| HuPuRes.TEMPLATE_SOCCER_CUP_LEAGUE
								.equalsIgnoreCase(scheme.template)) {
					in = new Intent(this, FootballGameActivity.class);
				} else if (scheme.template.equals(HuPuRes.TEMPLATE_NBA)) {
					in = new Intent(this, NBAGameActivity.class);
				} else if (scheme.template.equals(HuPuRes.TEMPLATE_CBA)) {
					in = new Intent(this, BasketballActivity.class);
				}

				in.putExtra("gid", scheme.id);
				in.putExtra("tag", scheme.game);
				in.putExtra("tab", BaseGameActivity.TAB_GUESS);
				startActivity(in);
				bScheme=true;
			} else {
				// 第一级的TAB
				startFragment(scheme.template, scheme.game, scheme.mode);
			}
			return;
		}

		if ( scheme.mode == null)// 找不到这个联赛，默认跳转到第一个
			startDefaultFragment();
		else {
			if (HuPuRes.TAB_GAMES.equalsIgnoreCase(scheme.mode)
					|| HuPuRes.TAB_VIDEO.equalsIgnoreCase(scheme.mode)
					|| HuPuRes.TAB_RANKS.equalsIgnoreCase(scheme.mode)) {
				// 第一级的TAB
				startFragment(scheme.template, scheme.game, scheme.mode);
			} else {
				if (scheme.id <= 0)// 为了应付董大师的高端测试
				{
					startDefaultFragment();
					return;
				}
				// 第二级的TAB
				Intent in = null;
				if (HuPuRes.TEMPLATE_NBA.equalsIgnoreCase(scheme.template)) {
					// NBA
					if (HuPuRes.SUB_TAB_PLAYER.equalsIgnoreCase(scheme.mode)) {
						// 球员数据
						in = new Intent(this, NBAPlayerInfoActivity.class);
						in.putExtra("pid", scheme.id);
						startActivity(in);
						bQuitBySource = bFromSource;
						return;
					} else if (HuPuRes.SUB_TAB_TEAM
							.equalsIgnoreCase(scheme.mode)) {
						// 球队
						in = new Intent(this, NBATeamActivity.class);
						in.putExtra("tid", scheme.id);
						startActivity(in);
						bQuitBySource = bFromSource;
						return;
					} else
						in = new Intent(this, NBAGameActivity.class);

				} else if (HuPuRes.TEMPLATE_CBA
						.equalsIgnoreCase(scheme.template)) {
					// CBA
					in = new Intent(this, BasketballActivity.class);
				} else if (HuPuRes.TEMPLATE_SOCCER_LEAGUE
						.equalsIgnoreCase(scheme.template)
						|| HuPuRes.TEMPLATE_SOCCER_CUP_LEAGUE
								.equalsIgnoreCase(scheme.template)) {
					// 足球
					in = new Intent(this, FootballGameActivity.class);
				}
				if (in != null) {
					// 必须有gid，tag,lid
					HupuLog.d("scheme jump", "tag=" + scheme.game + " gid=" + scheme.id);
					in.putExtra("gid", scheme.id);
					in.putExtra("tag", scheme.game);
					if (scheme.mode != null)
						in.putExtra("tab", scheme.mode);
					startActivity(in);
					bQuitBySource = bFromSource;
					bScheme=true;
				} else
					startDefaultFragment();
			}
		}
	}

	HomePageAdapter mPageAdapter;
	ViewPager mPager;
	TabPageIndicator indicator;

	public String getParam() {
		return DeviceInfo.getDeviceInfo(this) + "&token=" + mToken;
	}

	
	/** 初始化ViewPager */
	private void initViewPager() {
		mPageAdapter = new HomePageAdapter(this, getSupportFragmentManager(),
				leagueList);
		// mPageAdapter.setReqParam(DeviceInfo.getDeviceInfo(this) + "&token="
		// + mToken);

		mPager = (ViewPager) findViewById(R.id.view_pager);
		mPager.setAdapter(mPageAdapter);

		mPager.setOnPageChangeListener(new PageChangeListener());
		mPager.setOffscreenPageLimit(1);
		indicator = (TabPageIndicator) findViewById(R.id.page_indicator);
		indicator.setOnPageChangeListener(new PageChangeListener());
		indicator.setViewPager(mPager);
	}
	
	FrameLayout.LayoutParams txtLp; 
	FrameLayout.LayoutParams clp;
	/**添加小红点到指定栏目*/
	private void addRedPoint(int index){
		//获取导航的tabview，并移除导航
		ViewGroup group = (ViewGroup)indicator.getChildAt(0);
		View target =group.getChildAt(index);
		LinearLayout.LayoutParams tlp=(LinearLayout.LayoutParams)target.getLayoutParams();
		group.removeView(target);
		//自建容器
		FrameLayout container = new FrameLayout(this);
		if(txtLp ==null)
			txtLp=new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		container.addView(target,txtLp);
		//加入小红点
		if(clp==null)
		{
			clp= new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			clp.gravity = Gravity.RIGHT | Gravity.TOP;
			int topPadding = (int)getResources().getDimension(R.dimen.top_bar_point_top_padding);
			int rightPadding = (int)getResources().getDimension(R.dimen.top_bar_point_right_padding);
			clp.setMargins(0,topPadding ,rightPadding, 0);
		}
		ImageView tt=new ImageView(this);
		tt.setImageResource(R.drawable.icon_home_red_point);
		container.addView(tt,clp);
		tt.setId(1000+index);
		//导航中加入容器
		group.addView(container, index, tlp);	
	}
	/**显示指定栏目的小红点*/
	private void showRedPoint(int index)
	{
		View v=indicator.findViewById(1000+index);
		if(v==null)
			 addRedPoint( index);
		else
			v.setVisibility(View.VISIBLE);
	}
	/**隐藏指定栏目的小红点*/
	private void hideRedPoint(int index){
		//获取导航的tabview，并移除导航
		View v=indicator.findViewById(1000+index);
		if(v!=null)
			v.setVisibility(View.INVISIBLE);	
	}
	
	/** 默认进入第一页 */
	private void startDefaultFragment() {
		init();
		setDefaultTab();
		mPager.setCurrentItem(0, false);
		updateSideInfo();
	}

	private void startFragment(String template, String game, String mode) {
		init();
		updateSideInfo();
		int index = 0;
		for (LeaguesEntity entity : leagueList) {
			if (entity.en.equals(game))
				break;
			index++;
		}
		mPageAdapter.clickTab(index, mode, false);
		mPager.setCurrentItem(index, false);
		
		
		//将mode 转换为老版本可认的   tabindex；解决点击通知  底部tab不对应的bug
		int tabIndex = 0;
		if (mode.equalsIgnoreCase(HuPuRes.TAB_GAMES)) {
			tabIndex = INDEX_GAME;
		}else if (mode.equalsIgnoreCase(HuPuRes.TAB_NEWS)) {
			tabIndex = INDEX_NEWS;
		}else if (mode.equalsIgnoreCase(HuPuRes.TAB_VIDEO)) {
			tabIndex = INDEX_VIDEO;
		}else if (mode.equalsIgnoreCase(HuPuRes.TAB_RANKS)) {
			tabIndex = INDEX_STANDINGS;
		}else if (mode.equalsIgnoreCase(HuPuRes.TAB_DATA)) {
			tabIndex = INDEX_NBA_DATA;
		}else{
			tabIndex = INDEX_GAME;
		}
		setBackground(tabIndex);
	}

	class PageChangeListener implements OnPageChangeListener {
		int curState;

		/**
		 * arg0==1正在滑动， arg0==2滑动完毕了， arg0==0什么都没做
		 * */
		@Override
		public void onPageScrollStateChanged(int arg) {
			// HupuLog.d("onPageScrollStateChanged=" + arg);
			if (curState == 0) {
				if (arg == 2) {
					// 点击tab，隐藏tab
					hideTab();
				} else if (arg == 1) {
					// 手动滑动tab，隐藏tab
					// hideTab();
				}
			}
			if (curState != 0 && arg == 0) {
				// 显示tab
				// showTab();
			}
			curState = arg;
		}

		/**
		 * arg0 :当前页面，及你点击滑动的页面 arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
		 * */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// HupuLog.d("onPageScrolled=" + arg0);
		}

		@Override
		public void onPageSelected(int index) {
			pageIndex = index;
			updatePage(index);
			if (index == 0) {
				mSideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else {// 切换到中间（非头尾）的ViewPager
				mSideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			}
			mSideMenu.setSlidingEnabled(index == 0?true:false);
		}

	}

	private int getTabIndex(String tab) {
		if (HuPuRes.TAB_GAMES.equals(tab))
			return INDEX_GAME;
		else if (HuPuRes.TAB_NEWS.equals(tab))
			return INDEX_NEWS;
		else if (HuPuRes.TAB_VIDEO.equals(tab))
			return INDEX_VIDEO;
		else if (HuPuRes.TAB_RANKS.equals(tab)) {
			return INDEX_STANDINGS;
		} else if (HuPuRes.TAB_DATA.equals(tab)) {
			return INDEX_NBA_DATA;
		}
		return 0;
	}

	/** 初始化默认的tab按钮 */
	private void setDefaultTab() {
		// String tab=mPageAdapter.getCurTab();
		String tab = null;
		if (leagueList != null && leagueList.size() > 0)
			tab = leagueList.get(0).show_default_tab;
		mSideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		showTab(0);// 显示第一个联赛的tab
		setBackground(getTabIndex(tab));

	}

	/** 不同模板间的底部菜单可能完全不同，因此切换时需要先隐藏，再显示 */
	private void hideTab() {
		mBottomBar.setVisibility(View.GONE);// 需要设置动画
	}

	/**
	 * 显示底部tab
	 * 
	 * @param index顶部tab的索引
	 * */
	private void showTab(int index) {
		String template = mPageAdapter.getTemplate(index);
		if (HuPuRes.TEMPLATE_BROWSER.equals(template)
				|| HuPuRes.TEMPLATE_BROWSER_NAV.equals(template)) {
			mBottomBar.setVisibility(View.GONE);
		} else if (HuPuRes.TEMPLATE_DISCOVERY.equals(template)) {
			mBottomBar.setVisibility(View.GONE);
			setRedPoint(mRedList);
		} else {
			if (HuPuRes.TEMPLATE_NBA.equals(template)) {
				mBtnData.setVisibility(View.VISIBLE);
			} else {
				mBtnData.setVisibility(View.GONE);
			}
			mBottomBar.setVisibility(View.VISIBLE);// 需要设置动画
			String tab = mPageAdapter.getCurTab();
			// HupuLog.d("fb","show tab="+tab);
			// HupuLog.d("fb","show cur tab="+mCurTabIndex);
			int tabIndex = getTabIndex(tab);
			if (tabIndex != mCurTabIndex) {
				setBackground(tabIndex);
			}

		}
	}

	/**
	 * 当页面切换时需要对当前的比赛
	 * */
	private void updatePage(int index) {
		mPageAdapter.updateCurTab(index);
		showTab(index);
		String template = mPageAdapter.getTemplate(index);
		updateLeagueIcon(template);
		if (!HuPuRes.TAB_GAMES.equals(mPageAdapter.getCurTab())
				|| (!HuPuRes.TEMPLATE_CBA.equals(template) && !HuPuRes.TEMPLATE_NBA
						.equals(template))) {
			// 如果不是比赛tab离开房间
			leaveRoom();
		}
		
		if (HuPuRes.TAB_GAMES.equals(mPageAdapter.getCurTab())&&(!HuPuRes.TEMPLATE_CBA.equals(template) && !HuPuRes.TEMPLATE_NBA
						.equals(template))) {
			BaseFragment fragment = (BaseFragment) mPageAdapter.getItem(index);
			fragment.runTimer();
		}

	}

	/** 当页是否是活动页 */
	public boolean isActiveFragment(BaseFragment fragment) {
		int index = mPager.getCurrentItem();
		if (fragment == mPageAdapter.getItem(index)) {
//			HupuLog.d("fb","active="+fragment.getClass().getSimpleName());
			return true;
		}
		return false;
	}

	/** 初始化左菜单 */
	private void initMenu() {
		// configure the SlidingMenu
		mSideMenu = new SlidingMenu(this);
		mSideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSideMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSideMenu.setFadeEnabled(true);
		mSideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSideMenu.setFadeDegree(0.35f);
		mSideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSideMenu.setMenu(R.layout.menu_frame_left);

		// 打开menu
		mSideMenu.showMenu();
		showMenu();
		mSideMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
			@Override
			public void onClosed() {
				mSideMenu.setSlidingEnabled(pageIndex == 0 ? true:false);
			}
			
			
		});

		mSideMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {

			@Override
			public void onOpen() {
				// 菜单打开需要请求金币数

			}
		});

		leagueList = mApp.loadLeagues();
		setMenuItem();
	}

	/**
	 * 设置左侧导航 的项是否显示
	 */
	private void setMenuItem() {
		if (mToken != null) {
			mSideMenu.findViewById(R.id.menu_my_caipiao).setVisibility(
					View.VISIBLE);
			// mSideMenu.findViewById(R.id.my_coin_info).setVisibility(View.VISIBLE);
			mSideMenu.findViewById(R.id.menu_my_caipiao_line).setVisibility(
					View.VISIBLE);
			// mSideMenu.findViewById(R.id.my_coin_info_line).setVisibility(View.VISIBLE);
			
			mSideMenu.findViewById(R.id.menu_my_hupucoin_info).setVisibility(
					View.VISIBLE);
			mSideMenu.findViewById(R.id.menu_my_hupucoin_line).setVisibility(
					View.VISIBLE);
			
		} else {
			mSideMenu.findViewById(R.id.menu_my_caipiao).setVisibility(
					View.GONE);
			// mSideMenu.findViewById(R.id.my_coin_info).setVisibility(View.GONE);
			mSideMenu.findViewById(R.id.menu_my_caipiao_line).setVisibility(
					View.GONE);
			// mSideMenu.findViewById(R.id.my_coin_info_line).setVisibility(View.GONE);
			mSideMenu.findViewById(R.id.menu_my_hupucoin_info).setVisibility(
					View.GONE);
			mSideMenu.findViewById(R.id.menu_my_hupucoin_line).setVisibility(
					View.GONE);
		}
	}

	private void getSideBar(boolean isShowMenu) {
		initParameter();
		mParams.put("token", mToken);
//		String sign = SSLKey.getSSLSign(mParams,
//				SharedPreferencesMgr.getString("sugar", ""));// salt
		// 值由init中的sugar给出。必须要有的。
//		mParams.put("sign", sign);
		sendRequest(HuPuRes.REQ_METHOD_GET_SIDEBAR, mParams,
				new HupuHttpHandler(this), false);
		if (!SharedPreferencesMgr.getString(HuPuRes.KEY_NICK_NAME, "").equals(
				"")) {
			((TextView) findViewById(R.id.menu_nick_name))
					.setText(SharedPreferencesMgr.getString(
							HuPuRes.KEY_NICK_NAME, ""));
		} else {
			((TextView) findViewById(R.id.menu_nick_name))
					.setText(getString(R.string.my_userinfo));
		}
		if(isShowMenu)
			mSideMenu.showMenu();

	}
	

	private void init() {
		setContentView(R.layout.layout_content_frame);
		HuPuRes.setClient(mDeviceId);

		showMoreLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.menu_show_more, null);
		
		// menuTopLayout = (LinearLayout) LayoutInflater.from(this).inflate(
		// R.layout.item_menu_top, null);

		initMenu();
		mBtnGame = (ImageButton) findViewById(R.id.btn_game);
		mBtnNews = (ImageButton) findViewById(R.id.btn_news);
		mBtnVideo = (ImageButton) findViewById(R.id.btn_video);
		mBtnStandings = (ImageButton) findViewById(R.id.btn_standings);
		mBtnData = (ImageButton) findViewById(R.id.btn_data);
		mBottomBar = findViewById(R.id.layout_bottom);
		// btnShare = findViewById(R.id.btn_edit);

		setOnClickListener(R.id.btn_setup);
		setOnClickListener(R.id.btn_menu);
		// setOnClickListener(R.id.show_more);
		setOnClickListener(R.id.btn_game);
		setOnClickListener(R.id.btn_standings);
		setOnClickListener(R.id.btn_video);
		setOnClickListener(R.id.btn_news);
		setOnClickListener(R.id.btn_data);
		setOnClickListener(R.id.menu_name_info);
		setOnClickListener(R.id.menu_my_caipiao);
		setOnClickListener(R.id.menu_my_hupucoin_info);
		setOnClickListener(R.id.my_coin_info);
//		setOnClickListener(R.id.menu_recommend);
		setOnClickListener(R.id.menu_set);
		setOnClickListener(R.id.menu_rank_info);
		setOnClickListener(R.id.btn_edit);
		initViewPager();
	}

	/** 初始化标记位 */
	private int curMode;

	public void setTouchModeAbove(boolean bMargin) {
		if (bMargin)
			mSideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		else
			mSideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	/**
	 * 延迟显示菜单，因为产品设计上需要第一次加载的时候让用户看到左侧有导航 之后的加载由于fragment加载会需要一点时间，所以延迟200ms
	 */

	private void showMenu() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mSideMenu.showContent(true);
			}
		}, delay);
		delay = 200;
	}

	@Override
	protected void onStop() {

		if (!mApp.isAppOnForeground()) {
			leaveRoom();
		}
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (scheme != null) {
			treatScheme();
			scheme = null;
			// hasResume = true;
			return;
		} else {
			if (bQuitBySource) {
				//如果是从搜索引擎进入看球的app，将直接退出应用返回到搜索app
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						quit();// 退出到搜索app
					}
				}, 100);
				return;
			}
			else if(bScheme)
			{
				//如果是初次是scheme跳转到其他页面，返回时需要初始化页面，并跳转到默认页面
				bScheme=false;
				startDefaultFragment();			
			}
			else
			{
				//每次进入时都需要刷新下侧边栏的数据
				updateSideInfo();
			}
		}
	}
	
	/**更新侧边栏的数据*/
	private void updateSideInfo()
	{
		setMenuItem();
		// 更新侧边栏 用户信息
		getSideBar(false);
		if (mApp.loginSuccess == 1) {
			leagueList = mApp.loadLeagues();
			updateTopTitle();
			mApp.loginSuccess = 0;
		}
	}
	
	private void clickTab(int index) {
		mPageAdapter.clickTab(mPager.getCurrentItem(), TABS[index], true);
		setBackground(index);
	}

	@SuppressLint("NewApi")
	@Override
	public void treatClickEvent(int id) {

		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_menu:
			sendUmeng(HuPuRes.UMENG_EVENT_NAV, HuPuRes.UMENG_KEY_NAV_SUM,
					HuPuRes.UMENG_VALUE_MENU_BTN);
			mSideMenu.setSlidingEnabled(true);
			getSideBar(true);
			break;
		case R.id.btn_setup:
			startActivity(new Intent(this, SetupActivity.class));
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.btn_video:
			clickTab(INDEX_VIDEO);
			break;
		case R.id.btn_news:
			clickTab(INDEX_NEWS);
			break;

		case R.id.btn_game:
			clickTab(INDEX_GAME);
			break;
		case R.id.btn_standings:
			clickTab(INDEX_STANDINGS);
			break;
		case R.id.btn_data:
			clickTab(INDEX_NBA_DATA);
			break;
		case R.id.menu_name_info:
			sendUmeng(HuPuRes.UMENG_EVENT_NAV, HuPuRes.UMENG_KEY_NAV_SUM,
					HuPuRes.UMENG_VALUE_MY_ACCOUNT);
			Intent userInfoIntent = new Intent(this, AccountActivity.class);
			startActivity(userInfoIntent);
			break;
		case R.id.menu_my_caipiao:
			Intent myCaipiao = new Intent(this, MyCaipiaoListActivity.class);
			startActivity(myCaipiao);
			break;
		case R.id.menu_my_hupucoin_info:
			Intent hupucoinIntent = new Intent(this, UserHupuDollorInfoActivity.class);
			startActivity(hupucoinIntent);

			break;
		case R.id.my_coin_info:
			Intent coinIntent = new Intent(this, UserGoldInfoActivity.class);
			startActivity(coinIntent);

			break;
//		case R.id.menu_recommend:
//			break;
		case R.id.menu_set:
			Intent setintent = new Intent(this, SetupActivity.class);
			startActivity(setintent);
			break;
		case R.id.menu_rank_info:
			sendUmeng(HuPuRes.UMENG_EVENT_NAV, HuPuRes.UMENG_KEY_NAV_SUM,
					HuPuRes.UMENG_VALUE_QUIZ_RANK);
			Intent intent = new Intent(this, GuessRankActivity.class);
			intent.putExtra("rank_type", 1);
			startActivity(intent);
			break;
		case R.id.btn_share:
			// if (mFragmentWebview != null) {
			// String title = mFragmentWebview.getShareContent();
			// if (title != null)
			// showShareView("虎扑看球", mFragmentWebview.getShareUrl(), title,
			// true);
			// }
			break;
		case R.id.btn_edit:
			lastTitle = mPageAdapter.getPageTitle(mPager.getCurrentItem())
					.toString();
			showEditDialog();
			break;
		}
		// hideAction(id);
	}

	// public void hideAction(int id)
	// {
	// if(id!=R.id.btn_edit)
	// onCancle();
	// }

	String lastTitle;

	private synchronized void showEditDialog() {
		final EditDialog edialog = new EditDialog(this, leagueList);

		// edialog.setOnDismissListener(new OnDismissListener() {
		//
		// @Override
		// public void onDismiss(DialogInterface dialog) {
		//
		// if(edialog.getIsChanged())
		// {
		// HupuLog.d("fb","lastTitle="+lastTitle);
		// mApp.insertLeagues(leagueList);
		// mPageAdapter.updateTitle(leagueList,lastTitle);
		// indicator.notifyDataSetChanged();
		// HupuLog.d("fb","pos="+mPageAdapter.getEditedPagePos());
		// int pos=mPageAdapter.getEditedPagePos();
		// mPageAdapter.notifyDataSetChanged();
		// if(pos!=mPager.getCurrentItem())
		// mPager.setCurrentItem(pos);
		// }
		// }
		// });
		edialog.goShow();
		// action_TopToBottom(edialog);
	}

	// public void action_TopToBottom(final EditDialog edialog)
	// {
	// // TODO Auto-generated method stub
	// final LinearLayout selection = (LinearLayout)
	// findViewById(R.id.action_bar_shuttle_container);
	// View mian_ui_ = findViewById(R.id.btn_edit);
	// mian_ui_.setEnabled(false);
	// selection.setLayoutParams(new
	// AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT,
	// LayoutParams.FILL_PARENT, 0, 0));
	// //
	// selection.setBackgroundColor(R.color.abs__bright_foreground_disabled_holo_dark);
	// selection.addView(edialog);
	// Animation showAnim=AnimationUtils.loadAnimation(this,
	// R.anim.fade_in_top);
	//
	// edialog.startAnimation(showAnim);
	//
	// showAnim.setAnimationListener(new AnimationListener(){
	//
	// @Override
	// public void onAnimationEnd(Animation animation)
	// {
	//
	// if (edialog.getIsChanged()) {
	// HupuLog.d("fb", "lastTitle=" + lastTitle);
	// mApp.insertLeagues(leagueList);
	// mPageAdapter.updateTitle(leagueList, lastTitle);
	// indicator.notifyDataSetChanged();
	// HupuLog.d("fb", "pos=" + mPageAdapter.getEditedPagePos());
	// int pos = mPageAdapter.getEditedPagePos();
	// mPageAdapter.notifyDataSetChanged();
	// if (pos != mPager.getCurrentItem())
	// mPager.setCurrentItem(pos);
	// }
	// edialog.clearAnimation();
	//
	// }
	// @Override
	// public void onAnimationRepeat(Animation animation)
	// {
	// // TODO Auto-generated method stub
	// }
	// @Override
	// public void onAnimationStart(Animation animation)
	// {
	// // TODO Auto-generated method stub
	//
	// }});
	//
	// }

	/** 关注和取消比赛 */
	public void setFollowGame(BaseGameEntity entity, BaseGameFragment fragment) {
		if (mApp.needNotify || entity.bFollow > 0) {
			// byte unFollow = (byte) (entity.i_isFollow > 0 ? 0 : 1);

			byte unFollow = (byte) entity.bFollow;
			if (fragment instanceof BasketballGamesFragment) {
				((BasketballGamesFragment) fragment).updateFollow(entity);
			} else if (fragment instanceof SoccerGamesFragment)
				((SoccerGamesFragment) fragment)
						.updateFollow((ScoreboardEntity) entity);

			int lid = findLid(fragment.mTag);
			followGame(lid, entity.i_gId, unFollow, new FollowHandler(entity,
					fragment));
		} else {
			followFragment = fragment;
			followEntity = entity;
			showCustomDialog(DIALOG_NOTIFY, R.string.push_title,
					R.string.push_open_notify, 3, R.string.open_notify,
					R.string.cancel);
		}
	}

	private void setBackground(int index) {

		HupuLog.d("setbg", "findex=" + mCurTabIndex);
		switch (mCurTabIndex) {// 点击前
		case INDEX_GAME:
			// if (curMode == HomePageAdapter.MODE_FOOTBALL
			// || curMode == HomePageAdapter.MODE_CUP_CL)
			// mBtnGame.setImageResource(R.drawable.btn_football_game_up);
			// else
			// mBtnGame.setImageResource(R.drawable.btn_nba_game_up);
			mBtnGame.setBackgroundColor(0x00000001);
			break;
		case INDEX_NEWS:
			mBtnNews.setImageResource(R.drawable.btn_news_up);
			mBtnNews.setBackgroundColor(0x00000001);
			break;
		case INDEX_VIDEO:
			mBtnVideo.setImageResource(R.drawable.btn_video);
			mBtnVideo.setBackgroundColor(0x00000001);
			break;
		case INDEX_STANDINGS:
			mBtnStandings.setImageResource(R.drawable.btn_standings);
			mBtnStandings.setBackgroundColor(0x00000001);
			break;
		case INDEX_NBA_DATA:
			mBtnData.setImageResource(R.drawable.btn_data_up);
			mBtnData.setBackgroundColor(0x00000001);
			break;
		}
		switch (index) {// 点击后
		case INDEX_GAME:
			// if (curMode == HomePageAdapter.MODE_FOOTBALL
			// || curMode == HomePageAdapter.MODE_CUP_CL)
			// mBtnGame.setImageResource(R.drawable.btn_football_game_down);
			// else
			// mBtnGame.setImageResource(R.drawable.btn_nba_game_down);
			mBtnGame.setBackgroundResource(R.drawable.bg_bottom_hover);
			break;
		case INDEX_NEWS:
			mBtnNews.setImageResource(R.drawable.btn_news_down);
			mBtnNews.setBackgroundResource(R.drawable.bg_bottom_hover);
			break;

		case INDEX_VIDEO:
			mBtnVideo.setImageResource(R.drawable.btn_video_hover);
			mBtnVideo.setBackgroundResource(R.drawable.bg_bottom_hover);
			break;

		case INDEX_STANDINGS:
			mBtnStandings.setImageResource(R.drawable.btn_standings_hover);
			mBtnStandings.setBackgroundResource(R.drawable.bg_bottom_hover);
			break;
		case INDEX_NBA_DATA:
			mBtnData.setImageResource(R.drawable.btn_data_down);
			mBtnData.setBackgroundResource(R.drawable.bg_bottom_hover);
			break;
		}
		mCurTabIndex = index;

		String template = mPageAdapter.getTemplate(mPager.getCurrentItem());
		updateLeagueIcon(template);
	}

	private void updateLeagueIcon(String template) {
		// 更换比赛背景
		if (HuPuRes.TEMPLATE_CBA.equals(template)
				|| HuPuRes.TEMPLATE_NBA.equals(template)) {
			if (mCurTabIndex == INDEX_GAME) {
				mBtnGame.setImageResource(R.drawable.btn_nba_game_down);
			} else {
				mBtnGame.setImageResource(R.drawable.btn_nba_game_up);
			}
		} else {
			if (mCurTabIndex == INDEX_GAME) {
				mBtnGame.setImageResource(R.drawable.btn_football_game_down);
			} else {
				mBtnGame.setImageResource(R.drawable.btn_football_game_up);
			}
		}
	}

	private ArrayList<String> mRedList;
	private void setRedPoint(ArrayList<String> redList) {
		
		
		mRedList = redList;
		mSideMenu.findViewById(R.id.caipiao_red_point).setVisibility(View.GONE);
		mSideMenu.findViewById(R.id.coin_red_point).setVisibility(View.GONE);
		mSideMenu.findViewById(R.id.account_red_point).setVisibility(View.GONE);
		mSideMenu.findViewById(R.id.rank_red_point).setVisibility(View.GONE);
//		mSideMenu.findViewById(R.id.recommend_red_point).setVisibility(View.GONE);
		mSideMenu.findViewById(R.id.set_red_point).setVisibility(View.GONE);
		findViewById(R.id.user_red_point).setVisibility(View.GONE);
		
		//隐藏导航小红点
		for(int i = 0;i<leagueList.size();i++){
			hideRedPoint(i);
		}
		
		

		if (redList != null) {

			for (String red : redList) {
				if (red.contains("caipiao"))
					mSideMenu.findViewById(R.id.caipiao_red_point)
							.setVisibility(View.VISIBLE);

				if (red.contains("caisno") || red.contains("store")
						|| red.contains("prize") || red.contains("task"))
					mSideMenu.findViewById(R.id.coin_red_point).setVisibility(
							View.VISIBLE);

				if (red.contains("account"))
					mSideMenu.findViewById(R.id.account_red_point)
							.setVisibility(View.VISIBLE);

				if (red.contains("casinorank"))
					mSideMenu.findViewById(R.id.rank_red_point).setVisibility(
							View.VISIBLE);

//				if (red.contains("apps"))
//					mSideMenu.findViewById(R.id.recommend_red_point)
//							.setVisibility(View.VISIBLE);

				if (red.contains("settings"))
					mSideMenu.findViewById(R.id.set_red_point).setVisibility(
							View.VISIBLE);
				if (red.contains("caipiao") || red.contains("coin")
						|| red.contains("casinorank")
						|| red.contains("account") || red.contains("apps")
						|| red.contains("settings"))
					findViewById(R.id.user_red_point).setVisibility(
							View.VISIBLE);
				
				
				//设置导航小红点
				for(int i = 0;i<leagueList.size();i++){
					if (red.contains(leagueList.get(i).en))
						showRedPoint(i);
				}
			}
		}
		
		//发现的小红点操作
		int index = mPager.getCurrentItem();
		String template = mPageAdapter.getTemplate(index);
		if (HuPuRes.TEMPLATE_DISCOVERY.equals(template)) {
			if ((DiscoveryFragment)mPageAdapter.getItem(index) != null) {
				((DiscoveryFragment)mPageAdapter.getItem(index)).setRedPointData(redList);
			}
		}
	}

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		if (o == null)
			return;
		switch (methodId) {

		case HuPuRes.REQ_METHOD_GET_SIDEBAR:
			SidebarEntity sidebarEntity = (SidebarEntity) o;
			if (sidebarEntity != null) {
				// 小红点
				setRedPoint(sidebarEntity.reddotList);

				if (sidebarEntity.balance != null) {
					// 转百万
					// if (sidebarEntity.balance.length() > 6) {
					//
					// ((TextView) findViewById(R.id.gold_num))
					// .setText(sidebarEntity.balance.substring(0,
					// sidebarEntity.balance.length() - 4)
					// + " 万金币");
					// } else {
					// }
					TextView huputv = (TextView) findViewById(R.id.hupucoin_num);
					if(mToken!=null){
						huputv.setVisibility(View.VISIBLE);
						huputv.setText(sidebarEntity.hupuDollor_balance + getString(R.string.hupudollor_bean));
					}else{
						huputv.setVisibility(View.GONE);
					}
					
					((TextView) findViewById(R.id.gold_num))
							.setText(sidebarEntity.balance + getString(R.string.gold_bean));


					((TextView) findViewById(R.id.rank_info))
							.setText(sidebarEntity.rankInfo);
				}

				if (sidebarEntity.walletBalance != null) {
					((TextView) findViewById(R.id.wallet_info))
							.setText(sidebarEntity.walletBalance + "元");
				}

			}
			break;
		case HuPuRes.REQ_METHOD_SET_NOTIFY:
			SendMsgResp msg = (SendMsgResp) o;
			if (msg.pid == 0) {
				// showToast("设置失败");
			} else {
				boolean t = SharedPreferencesMgr.getBoolean("is_push", true);
				SharedPreferencesMgr.setBoolean("is_push", !t);
			}
			break;

		}
	}

	@Override
	public void onErrResponse(Throwable error, int type) {
		super.onErrResponse(error, type);
	}

	public boolean sendAppRequest(int reqType, String tag,
			RequestParams params, AsyncHttpResponseHandler handler) {
		return mApp.sendHttpRequest(reqType, tag, params, handler);
	}

	public boolean sendRequest(int reqType, RequestParams params) {
		return sendRequest(reqType, curTag, params, new HupuHttpHandler(this),
				false);
	}

	/**
	 * 
	 * */
	public boolean sendTagRequest(int reqType, RequestParams params) {

		return sendRequest(reqType, curTag, params, new HupuHttpHandler(this),
				false);
	}

	public RequestParams getHttpParams(boolean init) {
		if (init)
			initParameter();
		return mParams;

	}

	/**
	 * 切换到实时页面
	 * */
	public void switchToLive(BasketballGameEntity en, int pos, boolean matchDay,String tag) {
		HuPuApp.hasTeam(en.i_home_tid, en.str_home_name);
		HuPuApp.hasTeam(en.i_away_tid, en.str_away_name);
		Intent in = new Intent(this, NBAGameActivity.class);
		in.putExtra("game", en);
		in.putExtra("match", matchDay);
		in.putExtra("pos", pos);
		in.putExtra("tag", tag);
		startActivity(in);
		// startActivity(in);
	}

	long quit_time;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			// 新增逻辑，按两次返回退出
			long curtime = System.currentTimeMillis();
			if (quit_time == 0 || curtime - quit_time > 3000) {
				showToast(getString(R.string.quit_app));
			} else {
				quit();
			}
			quit_time = System.currentTimeMillis();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
//			sendUmeng(HuPuRes.UMENG_EVENT_NAV, HuPuRes.UMENG_KEY_NAV_SUM,
//					HuPuRes.UMENG_VALUE_MENU_BTN);
//			if (mSideMenu.isMenuShowing()) {
//				// menu.showContent();
//			} else
//				getSideBar(true);
		}
		return false;
	}

	/**
	 * 处理点击事件
	 * */
	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
		if (mDialog != null)
			mDialog.cancel();
	}

	
	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		super.onLoginSuccess();
		leagueList = mApp.loadLeagues();
		updateTopTitle();
	}
	
	/**
	 * 处理点击事件
	 * */
	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		if (DialogRes.DIALOG_QUIT_PROMPT == dialogId)
			quit();
		else if (dialogId == DIALOG_NOTIFY) {
			// 打开通知
			mApp.setNotify(true);
			setFollowGame(followEntity, followFragment);
			reqNotify(1);
		} else if (dialogId == DIALOG_SHOW_BIND_PHONE) {
			// 去绑定手机
			Intent in = new Intent(this, PhoneInputActivity.class);
			startActivity(in);

		}
		if (mDialog != null)
			mDialog.cancel();
	}

	class FollowHandler extends AsyncHttpResponseHandler {

		private BaseGameEntity mEntity;
		BaseFragment mFragment;

		public FollowHandler(BaseGameEntity entity, BaseFragment fragment) {
			mEntity = entity;
			mFragment = fragment;
		}

		@Override
		public void onSuccess(String content, int reqType) {
			super.onSuccess(content, reqType);

			FollowResp resp = (FollowResp) JsonPaserFactory.paserObj(
					content, reqType);
			if (resp == null || resp.i_success == 0) {
				showToast(String.format(SORRY_NOTIFY,
						mEntity.str_home_name, mEntity.str_away_name));

				if (mFragment instanceof BasketballGamesFragment)
					((BasketballGamesFragment) mFragment)
							.updateFollow((BasketballGameEntity) mEntity);
				else if (mFragment instanceof SoccerGamesFragment)
					((SoccerGamesFragment) mFragment)
							.updateFollow((ScoreboardEntity) mEntity);

			} else {
				if (reqType == HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME
						|| reqType == HuPuRes.REQ_METHOD_FOLLOW_GAME) {
					showToast(String.format(SUCCESS_NOTIFY,
							mEntity.str_home_name, mEntity.str_away_name));
				}
				if (reqType == HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME_CANCEL
						|| reqType == HuPuRes.REQ_METHOD_FOLLOW_GAME_CANCEL) {
					showToast(CANCEL_NOTIFY);
				}
			}

		}

	}

	@Override
	public void onSocketError(Exception socketIOException) {

		// 发生错误重连。
		// if (mFragmentNbaGames != null && mFragmentNbaGames.bMatchDay)
		// reconnect(false);
	}

	@Override
	public void onSocketResp(JSONObject obj) {
		HupuLog.d("fb", "onSocketResp" + obj);
		if (obj != null) {

			String room = obj.optString("room");
			try {
				BasketballGamesFragment fragment = null;
				if (HuPuRes.ROOM_NBA_HOME.equals(room)) {
					fragment = (BasketballGamesFragment) mPageAdapter
							.getFragment("nba", HuPuRes.TAB_GAMES);
				} else if (HuPuRes.ROOM_CBA_HOME.equals(room)) {
					fragment = (BasketballGamesFragment) mPageAdapter
							.getFragment("cba", HuPuRes.TAB_GAMES);
				}
				if (fragment != null) {
					BasketBallGamesBlock entity = new BasketBallGamesBlock();
					entity.paser(obj);
					// if(entity.mGames!=null)
					// HupuLog.d("fb","status" +
					// entity.mGames.get(0).byt_status);
					fragment.updateData(entity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onSocketConnect() {
		// 连接成功了，加入room。
		joinRoom();
	}

	/**
	 * n=1 follow
	 * */
	public void reqNotify(int n) {

		initParameter();
		mParams.put("isnotific", "" + n);
		sendRequest(HuPuRes.REQ_METHOD_SET_NOTIFY, mParams,
				new HupuHttpHandler(this), false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mController != null) {
			/** 使用SSO授权必须添加如下代码 */
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
					requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
		}
	}

	public void finishShuddle() {
		// TODO Auto-generated method stub
		HupuLog.d("fb", "lastTitle=" + lastTitle);
		mApp.insertLeagues(leagueList);
		mApp.followLeague(leagueList);
		updateTopTitle();
	}
	
    /**联赛顺序更新后，需要刷新顶部TAB*/
	private void updateTopTitle()
	{
		mPageAdapter.updateTitle(leagueList, lastTitle);
		indicator.notifyDataSetChanged();
		HupuLog.d("fb", "pos=" + mPageAdapter.getEditedPagePos());
		int pos = mPageAdapter.getEditedPagePos();
		mPageAdapter.notifyDataSetChanged();
		if (pos != mPager.getCurrentItem())
			mPager.setCurrentItem(pos);
	}
	
	// public void onCancle()
	// {
	// final LinearLayout selection = (LinearLayout)
	// findViewById(R.id.action_bar_shuttle_container);
	//
	// final EditDialog edialog = (EditDialog) selection.getChildAt(0);
	// if(edialog==null)
	// {
	// return;
	// }
	//
	// Animation hiddenAnim=AnimationUtils.loadAnimation(this,
	// R.anim.fade_out_top);
	// edialog.startAnimation(hiddenAnim);
	//
	// hiddenAnim.setAnimationListener(new AnimationListener(){
	//
	// @Override
	// public void onAnimationEnd(Animation animation)
	// {
	//
	// edialog.clearAnimation();
	// edialog.setLayoutParams(new
	// AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, 200,
	// 0, -490){});
	// selection.removeAllViewsInLayout();
	// View mian_ui_ = findViewById(R.id.btn_edit);
	// mian_ui_.setEnabled(true);
	//
	// HupuLog.d("fb","lastTitle="+lastTitle);
	// mApp.insertLeagues(leagueList);
	// mPageAdapter.updateTitle(leagueList,lastTitle);
	// indicator.notifyDataSetChanged();
	// HupuLog.d("fb","pos="+mPageAdapter.getEditedPagePos());
	// int pos=mPageAdapter.getEditedPagePos();
	// mPageAdapter.notifyDataSetChanged();
	// if(pos!=mPager.getCurrentItem())
	// mPager.setCurrentItem(pos);
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation)
	// {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationStart(Animation animation)
	// {
	// // TODO Auto-generated method stub
	//
	// }});
	//
	//
	// }
}
