package com.hupu.games.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.fragment.BaseFragment;
import com.hupu.games.fragment.DataFragment;
import com.hupu.games.fragment.DiscoveryFragment;
import com.hupu.games.fragment.FootballRankFragment;
import com.hupu.games.fragment.BasketballGamesFragment;
import com.hupu.games.fragment.NbaStandingFragment;
import com.hupu.games.fragment.NewsFragment;
import com.hupu.games.fragment.SoccerGamesFragment;
import com.hupu.games.fragment.VideoFragment;
import com.hupu.games.fragment.WebViewFragment;

/**
 * 首页viewpager的adapter
 * 
 * @author panyongjun
 * */
public class HomePageAdapter extends FragmentStatePagerAdapter {

	/** key是联赛名+tab 例如nba新闻就是:nba_news */
	HashMap<String, BaseFragment> mMapFragments;

	/** 显示的模式，目前有NBA，篮球，足球，足球杯赛，浏览器模式 */
	public final static int MODE_NBA = 0;

	/** 目前CBA只显示三个子tag 赛程 新闻 视频 */
	public final static int MODE_BASKETBALL = 1;

	public final static int MODE_FOOTBALL = 2;

	public final static int MODE_CUP_CL = 3;

	public final static int MODE_BROWSER = 4;

	public final static int MODE_BROWSER_NAV = 5;

	public final static int MODE_DISCOVERY = 6;

	private final static int INDEX_GAME = 0;
	private final static int INDEX_NEWS = 1;
	private final static int INDEX_VIDEO = 2;
	private final static int INDEX_STANDINGS = 3;
	private final static int INDEX_NBA_DATA = 4;

	/** 当前页面的tab */
	private String mCurTab;

	LinkedList<LeaguesEntity> mListLeagues;

	private ArrayList<String> mTitles;

	private ArrayList<String> mTabList;

	HupuHomeActivity mAct;

	public HomePageAdapter(HupuHomeActivity act, FragmentManager fm,
			LinkedList<LeaguesEntity> listLeagues) {
		super(fm);		
		mAct = act;
		mTabList = new ArrayList<String>();
		mMapFragments = new HashMap<String, BaseFragment>();
		updateTitle(listLeagues,null);
	}

	 /**更新顶部导航栏的标题*/
	public void updateTitle(LinkedList<LeaguesEntity> listLeagues,String lastTitle) {
		mListLeagues = listLeagues;
		if (mTitles == null)
			mTitles = new ArrayList<String>();
		else
			mTitles.clear();
		int index=0;
		editedPos=0;
		for (LeaguesEntity entity : mListLeagues) {
			if(lastTitle!=null && lastTitle.equals(entity.name))
				editedPos=index;
//			HupuLog.d("fb","updateTitle="+entity.name);
			mTitles.add(entity.name);
			mTabList.add(entity.show_default_tab);
			index++;
		}
	}

	int editedPos;
	/**获取编辑后页面的索引*/
	public int getEditedPagePos()
	{
		return editedPos;
	}
	
	
	
	@Override
	public Fragment getItem(int index) {
//		HupuLog.d("fb","Fragment=" + index);
		return findFragment(index, mCurTab);
	}

	@Override
	public int getCount() {
		if (mTitles != null)
			return mTitles.size();
		return 0;
	}

	 /**顶部导航栏的标题*/
	@Override
	public CharSequence getPageTitle(int position) {
		if (mTitles != null)
			return mTitles.get(position);
		return "";
	}

	 /**根据下标获取该Fragment所对应的模板*/
	public String getTemplate(int position) {
		if (mListLeagues != null){
			try {
				return mListLeagues.get(position).template;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return "";
	}

	String cnTag;
	boolean isWebStanding;
	String curTag;
	int curLid;
    /**初始化创建Fragment所需要的参数*/
	private void initParam(LeaguesEntity en) {
		cnTag = en.name;
		curTag = en.en;
		curLid = en.lid;
		if ("web".equalsIgnoreCase(en.showStandings))
			isWebStanding = true;
		else
			isWebStanding = false;
	}

	/**
	 * 当tab是Data是需要判断该页面是否有Data页面 或者是webview的URL连接
	 * */
	private boolean isAvailibleTag(String tab, LeaguesEntity entity) {
		if (tab.equals("data") && !entity.template.equals("nba"))
			return false;
		if (entity.template.equals(HuPuRes.TEMPLATE_BROWSER)
				|| entity.template.equals(HuPuRes.TEMPLATE_BROWSER_NAV)) {
			if (!tab.startsWith("http"))
				return false;
		}
		return true;
	}

	private BaseFragment findFragment(int pos, String tab) {
		BaseFragment fragment = null;
		LeaguesEntity entity = mListLeagues.get(pos);
		if(entity.template.equals(HuPuRes.TEMPLATE_DISCOVERY))
			tab="";
		else if (tab == null ||"".equals(tab)||!isAvailibleTag(tab, entity))
		{
			tab = entity.show_default_tab;
		}
		
		if (mCurTab == null) {
			mCurTab = tab;
		}
		
		String key = entity.en + "_" + tab;
//		HupuLog.d("fb","findFragment key="+key);
		fragment = mMapFragments.get(key);
//		if(fragment!=null)
//			HupuLog.d("fb","findFragment !");
		if (fragment == null) {
			initParam(entity);
			fragment = buildNewFragment(pos, tab);
			if (fragment == null)
				fragment = buildNewFragment(pos, tab = entity.show_default_tab);
			mMapFragments.put(key, fragment);
		} else {
			checkParam(fragment, tab);
		}
//		HupuLog.d("fb","findFragment index="+pos+" tab="+tab);
		mTabList.set(pos, tab);
		return fragment;
	}

	private void checkParam(BaseFragment fragment, String tab) {
		if (fragment instanceof DataFragment) {
			int index = getIndexByTab(tab);
			DataFragment fragmentDatas = (DataFragment) fragment;
			if (index == INDEX_NBA_DATA)
				fragmentDatas.setUrl(HuPuRes.BASE_URL
						+ "nba/getPlayerData?client=" + mAct.getParam());
			else if (index == INDEX_STANDINGS) {
				if (fragmentDatas.mode == MODE_NBA)
					fragmentDatas.setUrl(HuPuRes.BASE_URL + curTag
							+ "/getStandings?client=" + mAct.getParam()
							+ "&type=web");
				else if (fragmentDatas.mode == MODE_BASKETBALL)
					fragmentDatas.setUrl(HuPuRes.BASE_URL
							+ "cba/getStandings?client=" + mAct.getParam());
			}
		}
	}

	private int getIndexByTab(String tab) {
		if (HuPuRes.TAB_GAMES.equals(tab))
			return INDEX_GAME;
		else if (HuPuRes.TAB_NEWS.equals(tab))
			return INDEX_NEWS;
		else if (HuPuRes.TAB_VIDEO.equals(tab))
			return INDEX_VIDEO;
		else if (HuPuRes.TAB_RANKS.equals(tab)) {
			return INDEX_STANDINGS;
		} else if (HuPuRes.TAB_DATA.equals(tab))
			return INDEX_NBA_DATA;
		return 0;
	}

	private BaseFragment buildNewFragment(int pos, String tab) {
		String template = getTemplate(pos);
		BaseFragment fragment = null;
		int mode = -1;
		int index = 0;
		if (HuPuRes.TEMPLATE_SOCCER_LEAGUE.equals(template)) {
			mode = MODE_FOOTBALL;
		} else if (HuPuRes.TEMPLATE_SOCCER_CUP_LEAGUE.equals(template)) {
			mode = MODE_CUP_CL;
		} else if (HuPuRes.TEMPLATE_NBA.equals(template)) {
			mode = MODE_NBA;
		} else if (HuPuRes.TEMPLATE_CBA.equals(template))
			mode = MODE_BASKETBALL;
		else if (HuPuRes.TEMPLATE_BROWSER.equals(template)) {
			mode = MODE_BROWSER;
		} else if (HuPuRes.TEMPLATE_BROWSER_NAV.equals(template)) {
			mode = MODE_BROWSER_NAV;
		} else if (HuPuRes.TEMPLATE_DISCOVERY.equals(template)) {
			DiscoveryFragment df = new DiscoveryFragment();
			df.setData(mListLeagues.get(pos).mDiscoverList);
			return df;
		}
		if (mode == MODE_DISCOVERY || mode == MODE_BROWSER) {
			WebViewFragment webviewFragment = new WebViewFragment(mode);
			webviewFragment.entry(curTag);
			return webviewFragment;
		}
		index = getIndexByTab(tab);
		Bundle bundle = new Bundle();
		bundle.putString("tag", curTag);
		bundle.putInt("lid", curLid);
		bundle.putInt("mode", mode);
		
		switch (index) {
		case INDEX_GAME:
			// if ((initIndicator & 0x1) == 0) {
			if (mode == MODE_NBA || mode == MODE_BASKETBALL) {
				fragment = new BasketballGamesFragment();
				fragment.setArguments(bundle);
			} else if (mode == MODE_FOOTBALL || mode == MODE_CUP_CL) {
				fragment = new SoccerGamesFragment();
				fragment.setArguments(bundle);
			}
			break;
		case INDEX_NEWS:
			fragment = new NewsFragment();
			bundle.putString("cnTag", cnTag);
			fragment.setArguments(bundle);
			break;
		case INDEX_VIDEO:
			fragment = new VideoFragment();
			fragment.setArguments(bundle);
			break;

		case INDEX_STANDINGS:
			if (isWebStanding) {
				// web页面模式 会和数据页面共用，so 要重新 new 不然会无反应
				// if (dataFragment == null)
				DataFragment fragmentDatas = new DataFragment();
				fragment = fragmentDatas;
				fragmentDatas.setUrl(HuPuRes.BASE_URL + curTag
						+ "/getStandings?client=" + mAct.getParam()
						+ "&type=web");
			} else {
				if (mode == MODE_NBA) {
					fragment = new NbaStandingFragment();
				} else if (mode == MODE_BASKETBALL) {
					DataFragment mFragmentDatas = new DataFragment();
					fragment = mFragmentDatas;
					mFragmentDatas.setUrl(HuPuRes.BASE_URL
							+ "cba/getStandings?client=" + mAct.getParam());
				} else {
					FootballRankFragment mFragmentSoccerStanding = new FootballRankFragment(
							curTag);
					fragment = mFragmentSoccerStanding;
				}
			}
			fragment.setArguments(bundle);
			break;
		case INDEX_NBA_DATA:
			DataFragment fragmentDatas = new DataFragment();
			fragmentDatas.setUrl(HuPuRes.BASE_URL + "nba/getPlayerData?client="
					+ mAct.getParam());
			fragment = fragmentDatas;
			fragment.setArguments(bundle);
			break;
		}
		return fragment;
	}

	public void clear() {

	}

	public BaseFragment getFragment(String league, String tab) {
		return mMapFragments.get(league + "_" + tab);
	}

	/**点击底部tab切换*/
	public void clickTab(int index, String tab,boolean bNotify) {
		mCurTab = tab;
		mTabList.set(index, tab);
//		HupuLog.d("fb","clickTab tab="+mCurTab);
		notifyDataSetChanged();
	}

	
	/**点击顶部tab或滑动页面切换*/
	public void updateCurTab(int index) {
		String tab = mTabList.get(index);
		if(tab!=null&&!tab.equals(""))
			mCurTab=tab;
//		HupuLog.d("fb","update tab="+mCurTab);
	}

	public String getCurTab() {
		return mCurTab;
	}

	public String getTabByIndex(int index) {		
		return mTabList.get(index);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}
}
