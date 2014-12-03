package com.hupu.games.data;

import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class AbstratsBaseEntity {

	public final static String KEY_RESULT="result";
	public final static String KEY_LIST="list";
	public final static String KEY_DATE="date";
	public final static String KEY_DIREC="direc";
	public final static String KEY_TEAM_ID="tid";
	public final static String KEY_TEAM_IDS="tids";
	public final static String KEY_NAME="name";
	public final static String KEY_EN_NAME="ename";
	public final static String KEY_COLOR="color";
	public final static String KEY_UNFOLLOW="unfollow";
	public final static String KEY_ISFOLLOW="is_follow";
	public final static String KEY_LOGO="logo";
	public final static String KEY_INFO="info";
	
	public final static String KEY_MATCH_STATS="matchStats";
	public final static String KEY_GLOSSARY="glossary";
	public final static String KEY_SECTION1="section1";
	public final static String KEY_SECTION2="section2";
	public final static String KEY_SECTION3="section3";
	public final static String KEY_SECTION4="section4";
	
	public final static String KEY_OVER_TIME="overtime";
	public final static String KEY_HOME_START_PLAYER="homeStartPlayer";
	public final static String KEY_HOME_RESERVE_PLAYER="homeReservePlayer";
	public final static String KEY_AWAY_START_PLAYER="awayStartPlayer";
	public final static String KEY_AWAY_RESERVE_PLAYER="awayReservePlayer";
	public final static String KEY_PLAY_TYPE="player_type";
	public final static String KEY_PLAY_ID="player_id";
	public final static String KEY_SEASON="season";
	public final static String KEY_MINS="mins";
	public final static String KEY_PTS="pts";
	public final static String KEY_FGA="fga";
	public final static String KEY_FGM="fgm";
	public final static String KEY_TPA="tpa";
	public final static String KEY_TPM="tpm";
	public final static String KEY_FTA="fta";
	public final static String KEY_FTM="ftm";
	public final static String KEY_DREB="dreb";
	public final static String KEY_OREB="oreb";
	public final static String KEY_REB="reb";
	public final static String KEY_ASTS="asts";
	public final static String KEY_STL="stl";
	public final static String KEY_BLK="blk";
	public final static String KEY_TO="to";
	public final static String KEY_PF="pf";
	public final static String KEY_NET_POINTS="net_points";
	public final static String KEY_POSITION="position";
	public final static String KEY_DNP="dnp";
	
	
	
	
	public final static String KEY_GAME_ID="gid";
	public final static String KEY_TODAY="today";
	public final static String KEY_PREV="prev";
	public final static String KEY_NEXT="next";
	public final static String KEY_DATE_TIME="date_time";
	public final static String KEY_BEGIN_TIME="begin_time";
	public final static String KEY_HOME_TID="home_tid";
	public final static String KEY_HOME="home";
	public final static String KEY_HOME_NAME="home_name";
	public final static String KEY_HOME_SCORE="home_score";
	public final static String KEY_AWAY="away";
	public final static String KEY_AWAY_TID="away_tid";
	public final static String KEY_AWAY_NAME="away_name";
	public final static String KEY_AWAY_SCORE="away_score";
	public final static String KEY_MATCH_TYPE="match_type";
	public final static String KEY_PROCESS="process";
	public final static String KEY_STATUS="status";
	
	public final static String KEY_TITLE="title";
	public final static String KEY_CONTENT="content";
	public final static String KEY_IMG="img";
	public final static String KEY_VIDEO_LINK="video_link";
	
	public final static String KEY_PAGE="page";
	
	public final static String KEY_PAGE_COUNT="pagecount";
	public final static String KEY_DATA="data";
	public final static String KEY_ALL_COUNT="allcount";
	public final static String KEY_EVENT="event";
	public final static String KEY_SECTION="section";
	public final static String KEY_END_TIME="end_time";
	public final static String KEY_VS="vs";
	public final static String KEY_TEAM="team";
	
	public final static String KEY_NUM="num";
	public final static String KEY_SORT="sort";
	
	public final static String KEY_PID="pid";
	public final static String KEY_SCORE_BOARD="scoreboard";
	public final static String KEY_FOLLOW="follow";
	
	
	public final static String KEY_TV_LINK="tvlink";
	
	public final static String KEY_VERTICAL="vertical";
	
	public final static String KEY_DESC="desc";
	
	
	
	
//	public abstract void paser(String json) throws Exception;
	
	public  abstract void paser(JSONObject json) throws Exception ;

//	public  abstract void paser(JSONArray arr) throws Exception ;
	
    public String err;

	public static  void paserKeys(JSONArray arr,LinkedHashMap<String, String> map)	throws Exception
	{	
	
		
		JSONArray keys=arr.getJSONArray(0);
		JSONArray values=arr.getJSONArray(1);
		int size =keys.length();
//
//		Iterator< String> it=obj.keys();
//		while(it.hasNext())
//		{
//			System.out.println("Iterator key="+it.next());
//		}
//		String ss=obj.toString();
//		System.out.println("json 2 ="+ss);
//		ss=ss.substring(1, ss.lastIndexOf('}'));
//		System.out.println("ss="+ss);
//		String sarr[]=ss.split(":");
//		System.out.println("ss size="+sarr.length);
		//{"mins":"\u65f6\u95f4","pts":"\u5f97\u5206","reb":"\u7bee\u677f","asts":"\u52a9\u653b","stl":"\u62a2\u65ad","blk":"\u5c01\u76d6","to":"\u5931\u8bef","pf":"\u72af\u89c4","net_points":"+\/-","fg":"\u6295\u7bee","tp":"3\u5206","ft":"\u7f5a\u7403","oreb":"\u524d\u677f"}}}
		for(int i=0;i<size;i++)
		{
//			System.out.println("json key="+keys.getString(i));
			map.put(keys.getString(i), values.getString(i));
		}
	}
}
