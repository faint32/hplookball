package com.hupu.games.common;


public class HuPuRes {


//	public final static String BASE_URL = "http://games.mobileapi.hupu.com/1/6.2.0/";
//	public final static String REQ_URL_REDIRECTOR = "http://games.mobileapi.hupu.com/redirector/";
	
	public final static String BASE_URL = "http://games-beta.mobileapi.hupu.com/1/6.2.0/";
	public final static String REQ_URL_REDIRECTOR = "http://games-beta.mobileapi.hupu.com/redirector/";

//	public final static String BASE_URL = "http://test.mobileapi.hupu.com/1/6.2.0/";
//	public final static String REQ_URL_REDIRECTOR = "http://test.mobileapi.hupu.com/redirector/";
	//	public final static String BASE_HTTPS_URL = "https://test.mobileapi.hupu.com/1/6.0.4/";

	/** 求上墙 */
	//	public final static int ITEM_ON_WALL = 10001;
	/** 求上墙 赞 */
	public final static int ITEM_ON_WALL_LIKE = 10002;
	/** 求上墙 拆 */
	public final static int ITEM_ON_WALL_HATE = 10003;
	/** 表情 */
	public final static int ITEM_EMOJI = 10004;
	public final static int ITEM_EXCITING_LIKE = 10005;
	public final static int ITEM_EXCITING_HATE = 10006;
	public final static int ITEM_VIP = 10007;
	/** 攒人品 */
	public final static int ITEM_RP = 10008;

	public final static String REQ_URL_GET_TEAMS = BASE_URL + "nba/getTeam";
	public final static int REQ_METHOD_GET_TEAMS = 1;

	public final static String REQ_URL_FOLLOW_TEAM = BASE_URL
			+ "nba/followTeam";

	public final static int REQ_METHOD_SET_FOLLOW_TEAMS = 3;
	public final static String REQ_URL_SET_FOLLOW_TEAMS = BASE_URL
			+ "nba/followBatchTeam";

	public final static int REQ_METHOD_BOX_SCORE = 4;
	public final static String REQ_URL_BOX_SCORE = BASE_URL + "nba/getBoxscore";

	public final static int REQ_METHOD_NBA_GAMES_BY_GID = 5;

	/** 指定比赛日期的比赛数据,并返回前后七天的日期 */
	public final static int REQ_METHOD_NBA_GAMES_BY_DATE = 9;
	public final static int REQ_METHOD_GAMES_REAL_TIME = 10;
	public final static String REQ_URL_GET_NBA_GAMES = BASE_URL
			+ "nba/getGames";

	/** 由服务器返回最近的一天的比赛数据,并返回前后七天的日期 */
	public final static int REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT = 6;
	/** 指定比赛日期的比赛数据,并返回前七天的日期 */
	public final static int REQ_METHOD_BASKETBALL_GAMES_BY_PRE = 7;
	/** 指定比赛日期的比赛数据,并返回后七天的日期 */
	public final static int REQ_METHOD_BASKETBALL_GAMES_BY_NEXT = 8;
	public final static String REQ_URL_GET_BASKETBALL_GAMES = BASE_URL
			+ "%s/getGames";

	public final static int REQ_METHOD_GET_PLAY_LIVE_ASC = 11;
	public final static int REQ_METHOD_GET_PLAY_LIVE_DESC = 12;
	public final static String REQ_URL_GET_PLAY_LIVE = BASE_URL
			+ "nba/getPlaybyplay";



	public final static int REQ_METHOD_GET_RECAP = 21;
	public final static String REQ_URL_GET_RECAP = BASE_URL + "nba/getRecap";

	public final static int REQ_METHOD_FOLLOW_NBA_GAME = 31;
	public final static int REQ_METHOD_FOLLOW_NBA_GAME_CANCEL = 32;
	public final static String REQ_URL_FOLLOW_NBA_GAME = BASE_URL
			+ "nba/followGame";

	public final static int REQ_METHOD_GET_FOLLOW_TEAMS = 41;
	public final static String REQ_URL_GET_FOLLOW_TEAMS = BASE_URL
			+ "nba/getUserFollowTeams";

	public final static int REQ_METHOD_REDIRECTOR = 51;
	// public final static int REQ_METHOD_FOLLOW_TEAM = 52;
	// public final static int REQ_METHOD_FOLLOW_TEAM_CANCEL = 53;

	public final static int REQ_METHOD_GET_STANDINGS = 61;
	public final static String REQ_URL_GET_STANDINGS = BASE_URL
			+ "nba/getStandings";
	public final static int REQ_METHOD_GET_NBA_VIDEO = 70;
	public final static int REQ_METHOD_GET_NBA_VIDEO_GAME = 71;
	// public final static int REQ_METHOD_GET_NBA_VIDEO_GAME_PRE= 72;
	public final static int REQ_METHOD_GET_NBA_VIDEO_GAME_NEXT = 73;
	public final static int REQ_METHOD_GET_NBA_VIDEO_HOT = 74;
	public final static int REQ_METHOD_GET_NBA_VIDEO_HOT_NEXT = 75;
	// public final static int REQ_METHOD_GET_VIDEO_HOT_PRE= 76;
	public final static String REQ_URL_GET_NBA_VIDEO = BASE_URL
			+ "nba/getVideo";

	public final static int REQ_METHOD_GET_CBA_VIDEO_GAME = 76;
	public final static int REQ_METHOD_GET_CBA_VIDEO_GAME_NEXT = 77;
	// public final static int REQ_METHOD_GET_CBA_VIDEO_HOT= 78;
	// public final static int REQ_METHOD_GET_CBA_VIDEO_HOT_NEXT= 79;
	public final static String REQ_URL_GET_CBA_VIDEO = BASE_URL
			+ "cba/getVideo";

	public final static int REQ_METHOD_GET_NBA_NEWS = 81;
	public final static int REQ_METHOD_GET_NBA_NEWS_NEXT = 82;
	public final static int REQ_METHOD_GET_NBA_NEWS_PRE = 83;
	public final static String REQ_URL_GET_NBA_NEWS = BASE_URL + "nba/getNews";

	public final static int REQ_METHOD_GET_CBA_NEWS = 85;
	public final static int REQ_METHOD_GET_CBA_NEWS_NEXT = 86;
	public final static int REQ_METHOD_GET_CBA_NEWS_PRE = 87;
	public final static String REQ_URL_GET_CBA_NEWS = BASE_URL + "cba/getNews";

	public final static int REQ_METHOD_CBA_GAME_BY_GID = 88;
	public final static int REQ_METHOD_FOOTBALL_GAME_BY_GID = 89;
	public final static String REQ_URL_GET_GAME_BY_GID = BASE_URL
			+ "%s/getGames";

	public final static int REQ_METHOD_BASKETBALL_NEXT = 90;
	public final static int REQ_METHOD_BASKETBALL_PREV = 91;
	public final static String REQ_URL_GET_BASKETBALL = BASE_URL
			+ "cba/getGames";

	public final static int REQ_METHOD_CBA_BOX_SCORE = 93;
	public final static String REQ_URL_CBA_BOX_SCORE = BASE_URL
			+ "cba/getBoxscore";

	public final static int REQ_METHOD_NBA_CHAT = 100;
	public final static int REQ_METHOD_NBA_CHAT_MORE = 101;
	public final static int REQ_METHOD_NBA_CHAT_NEW = 102;
	public final static String REQ_URL_NBA_CHAT = BASE_URL + "nba/getChat";

	public final static int REQ_METHOD_GET_NICK_NAME = 104;
	public final static String REQ_URL_GET_NICK_NAME = BASE_URL
			+ "status/getNickname";

	public final static int REQ_METHOD_RATING_LIST = 106;
	public final static String REQ_URL_RATING_LIST = BASE_URL + "rating/list";

	public final static int REQ_METHOD_RATING_DETAIL = 107;
	public final static int REQ_METHOD_RATING_DETAIL_MORE = 109;
	public final static String REQ_URL_RATING_DETAIL = BASE_URL
			+ "rating/detail";

	public final static int REQ_METHOD_USER_INIT = 112;
	public final static String REQ_URL_USER_INIT = BASE_URL + "status/init";

	public final static int REQ_METHOD_GET_BULLETIN_RANK = 123;
	public final static String REQ_URL_GET_BULLETIN_RANK = BASE_URL
			+ "product/getBulletinRank";

	public final static int REQ_METHOD_GET_INTRODUCE_VIP = 124;
	public final static String REQ_URL_GET_INTRODUCE_VIP = BASE_URL
			+ "product/introduceVip";

	public final static int REQ_METHOD_GET_REPLY = 184;
	public final static int REQ_METHOD_GET_MORE_REPLY = 185;
	public final static int REQ_METHOD_GET_LINK_REPLY = 186;
	public final static int REQ_METHOD_GET_MORE_LINK_REPLY = 187;
	public final static String REQ_URL_GET_lINK_REPLY = BASE_URL
			+ "link/getComments";
	public final static String REQ_URL_GET_REPLY = BASE_URL
			+ "%s/getNewsComments";

	public final static int REQ_METHOD_GET_TOTAL_HONOUR = 201;
	public final static String REQ_URL_GET_TOTAL_HONOUR = BASE_URL
			+ "honour/MyRankingList";
	public final static int REQ_METHOD_GET_TOTAL_HONOUR_HISTORY = 204;
	public final static String REQ_URL_GET_TOTAL_HONOUR_HISTORY = BASE_URL
			+ "honour/MyHistoryRankingList";
	public final static int REQ_METHOD_GET_TOTAL_MATCH_RANKING = 205;
	public final static String REQ_URL_GET_TOTAL_MATCH_RANKING = BASE_URL
			+ "honour/MatchRankingList";

	public final static int REQ_METHOD_GET_TOTAL_MAY_BUY = 206;
	public final static String REQ_URL_GET_TOTAL_MAY_BUY = BASE_URL
			+ "honour/MatchRankingList";
	public final static int REQ_METHOD_GET_CONTACTS = 207;
	public final static String REQ_URL_GET_CONTACTS = BASE_URL
			+ "product/contactservice";

	public final static int REQ_METHOD_GET_OBTAINSTUFF = 209;
	public final static String REQ_URL_GET_OBTAINSTUFF = BASE_URL
			+ "product/getobtainstuff";

	public final static int REQ_METHOD_GET_FOOTBALL_RANK = 900;
	public final static String REQ_URL_GET_FOOTBALL_RANK = BASE_URL
			+ "%s/getRank";


	public final static int REQ_METHOD_FOOTBALL_LEAGUE = 589;
	public final static int REQ_METHOD_FOOTBALL_LEAGUE_NEXT = 590;
	public final static int REQ_METHOD_FOOTBALL_LEAGUE_PREV = 591;
	public final static int REQ_METHOD_FOOTBALL_LEAGUE_REFRESH = 592;

	public final static String REQ_URL_GET_FOOTBALL_LEAGUE = BASE_URL
			+ "%s/getGames";

	public final static int REQ_METHOD_GET_FOOTBALL_VIDEO = 571;
	public final static int REQ_METHOD_GET_FOOTBALL_VIDEO_NEXT = 572;
	public final static String REQ_URL_GET_FOOTBALL_VIDEO = BASE_URL
			+ "%s/getVideo";

	public final static int REQ_METHOD_GET_FOOTBALL_STATISTIC = 580;
	public final static String REQ_URL_GET_FOOTBALL_STATISTIC = BASE_URL
			+ "%s/getStats";

	public final static int REQ_METHOD_GET_FOOTBALL_EVENTS = 582;
	public final static String REQ_URL_GET_FOOTBALL_EVENTS = BASE_URL
			+ "%s/getEvents";

	public final static int REQ_METHOD_GET_FOOTBALL_OUTS = 583;
	public final static String REQ_URL_GET_FOOTBALL_OUTS = BASE_URL
			+ "%s/getOuts";

	public final static int REQ_METHOD_GET_GAME_REPORT = 584;
	public final static String REQ_URL_GET_GAME_REPORT = BASE_URL
			+ "%s/getRecap";

	public final static int REQ_METHOD_GET_FOOTBALL_NEWS = 585;
	public final static int REQ_METHOD_GET_FOOTBALL_NEWS_NEXT = 586;

	public final static String REQ_URL_GET_FOOTBALL_NEWS = BASE_URL
			+ "%s/getNews";

	public final static int REQ_METHOD_GET_NEWS_DETAIL = 588;
	public final static String REQ_URL_GET_NEWS_DETAIL = BASE_URL
			+ "%s/getNewsDetail";

	public final static int REQ_METHOD_GET_FOOTBALL_EVENT = 613;
	public final static String REQ_URL_GET_FOOTBALL_EVENT = BASE_URL
			+ "%s/getEvents";

	public final static int REQ_METHOD_GET_LIVE_ASC = 630;
	public final static int REQ_METHOD_GET_LIVE_DESC = 631;
	public final static String REQ_URL_GET_LIVE = BASE_URL + "%s/getPlaybyplay";

	public final static int REQ_METHOD_SET_NOTIFY = 660;
	public final static String REQ_URL_SET_NOTIFY = BASE_URL
			+ "status/setNotificStatus";

	public final static int REQ_METHOD_GET_NBA_TEAM_PROGRAM = 665;
	public final static String REQ_URL_GET_NBA_TEAM_PROGRAM = BASE_URL
			+ "nba/teamInfo";

	public final static int REQ_METHOD_GET_NBA_TEAM_FULL_PROGRAM = 666;
	public final static String REQ_URL_GET_NBA_TEAM_FULL_PROGRAM = BASE_URL
			+ "nba/teamSchedule";

	public final static int REQ_METHOD_GET_NBA_TEAM_ROSTER = 667;
	public final static String REQ_URL_GET_NBA_TEAM_ROSTE = BASE_URL
			+ "nba/teamRoster";

	public final static int REQ_METHOD_GET_NBA_PLAYER_DATA = 668;
	public final static String REQ_URL_GET_NBA_PLAYER_DATA = BASE_URL
			+ "nba/teamStats";

	public final static int REQ_METHOD_GET_NBA_PLAYER_INFO = 669;
	public final static String REQ_URL_GET_NBA_PLAYER_INFO = BASE_URL
			+ "nba/playerInfo";

	public final static int REQ_METHOD_GET_SOCCER_TEAM_PROGRAM = 700;
	public final static String REQ_URL_GET_SOCCER_TEAM_PROGRAM = BASE_URL
			+ "%s/teamInfo";

	public final static int REQ_METHOD_GET_SOCCER_TEAM_ROSTER = 701;
	public final static String REQ_URL_GET_SOCCER_TEAM_ROSTE = BASE_URL
			+ "%s/teamRoster";

	public final static int  REQ_METHOD_GET_GOMMENT_INFO=702;
	public final static String REQ_URL_GET_GOMMENT_INFO = BASE_URL
			+ "link/getInfo";

	public final static int REQ_METHOD_GET_SOCCER_TEAM_FULL_PROGRAM = 710;
	public final static String REQ_URL_GET_SOCCER_TEAM_FULL_PROGRAM = BASE_URL
			+ "%s/teamSchedule";

	public final static int REQ_METHOD_POST = 100000;

	public final static int REQ_METHOD_QUIZ_COMMIT = 100001;
	public final static String REQ_URL_QUIZ_COMIT = BASE_URL + "quiz/bet";
	public final static int REQ_METHOD_QUIZ_LIST_COMMIT = 110001;

	public final static int REQ_METHOD_QUIZ_LIST = 100002;
	public final static String REQ_URL_QUIZ_LIST = BASE_URL
			+ "quiz/getListByGame";


	public final static int REQ_METHOD_GET_EXCHANGE_LIST = 100005;
	public final static String REQ_URL_GET_EXCHANGE_LIST = BASE_URL
			+ "prize/getList";

	public final static int REQ_METHOD_GET_BOX_COUNT = 100007;
	public final static String REQ_URL_GET_BOX_COUNT = BASE_URL
			+ "box/userDetail";

	public final static int REQ_METHOD_POST_INCREASE = 100018;
	public final static String REQ_URL_POST_INCREASE = BASE_URL
			+ "quiz/increase";

	public final static int REQ_METHOD_POST_REPLY = 100084;
	public final static int REQ_METHOD_POST_LINK_REPLY = 100086;
	public final static String REQ_URL_POST_LINK_REPLY = BASE_URL
			+ "link/sendComment";
	public final static String REQ_URL_POST_REPLY = BASE_URL
			+ "%s/sendNewsComment";

	public final static int REQ_METHOD_POST_LIGHT = 100085;
	public final static int REQ_METHOD_POST_LINK_LIGHT = 100087;
	public final static String REQ_URL_POST_LINK_LIGHT = BASE_URL
			+ "link/lightComment";
	public final static String REQ_URL_POST_LIGHT = BASE_URL
			+ "%s/lightNewsComment";

	public final static int REQ_METHOD_SENT_CHAT = 100103;
	public final static String REQ_URL_SENT_CHAT = BASE_URL + "chat/sendChat";

	public final static int REQ_METHOD_SET_NICK_NAME = 100105;
	public final static String REQ_URL_SET_NICK_NAME = BASE_URL
			+ "status/setNickname";

	public final static int REQ_METHOD_RATING_LIKE = 100110;
	public final static String REQ_URL_RATING_LIKE = BASE_URL + "rating/like";

	public final static int REQ_METHOD_RATING_RATE = 100111;
	public final static String REQ_URL_RATING_RATE = BASE_URL + "rating/rate";

	public final static int REQ_METHOD_FOLLOW_LEAGUE = 100112;
	public final static String REQ_URL_FOLLOW_LEAGUE = BASE_URL
			+ "status/followLeagues";

	public final static int REQ_METHOD_FOLLOW_ALL_TEAM = 100113;
	public final static String REQ_URL_FOLLOW_ALL_TEAM = BASE_URL
			+ "status/followTeams";

	public final static int REQ_METHOD_FOLLOW_GAME = 100114;
	public final static int REQ_METHOD_FOLLOW_GAME_CANCEL = 100115;
	public final static String REQ_URL_FOLLOW_GAME = BASE_URL
			+ "status/followGame";

	public final static int REQ_METHOD_FOLLOW_ONE_TEAM = 100116;
	public final static int REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL = 100117;
	public final static String REQ_URL_FOLLOW_ONE_TEAM = BASE_URL
			+ "status/followOneTeam";

	public final static int REQ_METHOD_GET_BALANCE = 100121;
	public final static String REQ_URL_GET_BALANCE = BASE_URL
			+ "pay/getBalance";

	public final static int REQ_METHOD_USER_LOGOUT = 113;
	public final static String REQ_URL_USER_LOGOUT = BASE_URL + "user/logout";


	public final static int REQ_METHOD_GET_DOLE = 100006;
	public final static String REQ_URL_GET_DOLE = BASE_URL + "user/getDole";

	public final static int REQ_METHOD_GET_VERIFY_CODE = 100118;
	public final static String REQ_URL_GET_VERIFY_CODE = BASE_URL
			+ "user/getMobileCode";

	public final static int REQ_METHOD_POST_PHONE_BIND = 100119;
	public final static String REQ_URL_POST_PHONE_BIND = BASE_URL
			+ "user/verifyMobileCode";

	public final static int REQ_METHOD_POST_ORDER = 100120;
	public final static String REQ_URL_POST_ORDER = BASE_URL + "user/pay";

	public final static int REQ_METHOD_POST_BUY_ITEM = 100122;
	public final static String REQ_URL_POST_BUY_ITEM = BASE_URL + "user/buy";

	public final static int REQ_METHOD_CHECK_BAG = 100210;
	public final static String REQ_URL_CHECK_BAG = BASE_URL + "user/checkBag";

	public final static int REQ_METHOD_USER_UNBIND = 100799;
	public final static String REQ_URL_USER_UNBIND = BASE_URL + "user/unbind";

	public final static int REQ_METHOD_USER_BIND = 100800;
	public final static String REQ_URL_USER_BIND = BASE_URL + "user/bind";

	public final static int REQ_METHOD_GET_BOX_BALANCE = 100302;
	public final static String REQ_URL_GET_BOX_BALANCE = BASE_URL
			+ "user/myCoin";

	public final static int REQ_METHOD_GET_BETINFO = 100704;
	public final static String REQ_URL_GET_BETINFO = BASE_URL + "user/betInfo";

	public final static int REQ_METHOD_GET_TASKREWARD = 107006;
	public final static String REQ_URL_GET_TASKREWARD = BASE_URL + "task/getTaskReward";

	public final static int REQ_METHOD_GET_SIDEBAR = 100738;
	public final static String REQ_URL_GET_SIDEBAR = BASE_URL
			+ "user/sidebarInfo";

	public final static int REQ_METHOD_QUIZ_COMMIT_INCREASE = 100131;
	public final static String REQ_URL_QUIZ_COMIT_INCREASE = BASE_URL
			+ "quiz/increase";

	public final static int REQ_METHOD_GET_PACKAGES = 100200;
	public final static String REQ_URL_GET_PACKAGES = BASE_URL
			+ "pay/getEvents";

	public final static int REQ_METHOD_GET_GOLD = 100201;
	public final static String REQ_URL_GET_GOLD = BASE_URL
			+ "hupuDollor/getBalance";

	public final static int REQ_METHOD_GET_EXCHANGEBEANS = 100202;
	public final static String REQ_URL_GET_EXCHANGEBEANS = BASE_URL
			+ "hupuDollor/exchangeBeans";

	public final static int REQ_METHOD_BUY_EMOJI = 100212;
	public final static String REQ_URL_BUY_EMOJI = BASE_URL
			+ "product/buyOnSale";

	public final static int REQ_METHOD_GET_RECHARGE = 100300;
	public final static String REQ_URL_GET_RECHARGE = BASE_URL + "pay/recharge";

	public final static int REQ_METHOD_GET_USERBOX = 100301;
	public final static String REQ_URL_GET_USERBOX = BASE_URL
			+ "box/userDetail";

	public final static int REQ_METHOD_EN_SENT_CHAT = 100603;
	public final static String REQ_URL_EN_SENT_CHAT = BASE_URL + "%s/sendChat";

	public final static int REQ_METHOD_POST_EXCHANGE = 100703;
	public final static String REQ_URL_POST_EXCHANGE = BASE_URL
			+ "prize/exchange";

	public final static int REQ_METHOD_GET_MY_PRIZE = 100705;
	public final static String REQ_URL_GET_MY_PRIZE = BASE_URL + "quiz/myAward";

	public final static int REQ_METHOD_TAKE_PRIZE = 100708;
	public final static String REQ_URL_TAKE_PRIZE = BASE_URL + "prize/take";

	public final static int REQ_METHOD_GET_BOX_OPEN = 100726;
	public final static String REQ_URL_GET_BOX_OPEN = BASE_URL + "box/open";

	public final static int REQ_METHOD_BET_COINS = 100737;
	public final static String REQ_URL_BET_COINS = BASE_URL + "quiz/betCoins";

	public final static int REQ_METHOD_GET_GUESS_RANK = 100740;
	public final static String REQ_URL_GET_GUESS_RANK = BASE_URL
			+ "quiz/ranking";

	public final static int REQ_METHOD_GET_GUESS_ALL_RANK = 100741;
	public final static String REQ_URL_GET_GUESS_ALL_RANK = BASE_URL
			+ "quiz/rankingmore";

	public final static int REQ_METHOD_GET_COIN_INFO = 100742;
	public final static String REQ_URL_GET_COIN_INFO = BASE_URL
			+ "quiz/payDetail";

	public final static int REQ_METHOD_MY_QUIZ_LIST = 100743;
	public final static int REQ_METHOD_MY_QUIZ_MORE_LIST = 100744;
	public final static String REQ_URL_MY_QUIZ_LIST = BASE_URL + "quiz/getMy";
	public final static int REQ_METHOD_TASK = 100745;
	public final static String REQ_URL_TASK = BASE_URL + "task/task";

	public final static int REQ_METHOD_CAIPIAO_COMMIT = 100801;
	public final static String REQ_URL_CAIPIAO_COMIT = BASE_URL + "caipiao/bet";

	public final static int REQ_METHOD_CAIPIAO_AGREEMENT = 100807;
	public final static String REQ_URL_CAIPIAO_AGREEMENT = BASE_URL
			+ "caipiao/agreement";

	public final static int REQ_METHOD_CAIPIAO_FAQ = 100808;
	public final static String REQ_URL_CAIPIAO_FAQ = BASE_URL + "caipiao/faq";

	public final static int REQ_METHOD_CAIPIAO_DETAIL = 100809;
	public final static String REQ_URL_CAIPIAO_DETAIL = BASE_URL
			+ "caipiao/detail";

	public final static int REQ_METHOD_MY_CAIPIAO_LIST = 1804;
	public final static int REQ_METHOD_MY_CAIPIAO_MORE_LIST = 1805;
	public final static String REQ_URL_MY_CAIPIAO_LIST = BASE_URL
			+ "caipiao/mybet";

	public final static int REQ_METHOD_GET_CAIPIAO_COIN_INFO = 100804;
	public final static String REQ_URL_GET_CAIPIAO_COIN_INFO = BASE_URL
			+ "quiz/caipiaoPayDetail";

	public final static int REQ_METHOD_GET_WALLET_BALANCE = 100802;
	public final static String REQ_URL_WALLET_BALANCE = BASE_URL
			+ "wallet/getBalance";

	public final static int REQ_METHOD_GET_RECHARGEMETHOD = 100803;
	public final static String REQ_URL_RECHARGEMETHOD = BASE_URL
			+ "wallet/rechargeMethodList";

	public final static int REQ_METHOD_GET_CAIPIAO_RECHARGE = 100805;
	public final static String REQ_URL_GET_CAIPIAO_RECHARGE = BASE_URL
			+ "wallet/recharge";

	public final static int REQ_METHOD_GET_CAIPIAO_ATM = 100806;
	public final static String REQ_URL_GET_CAIPIAO_ATM = BASE_URL
			+ "wallet/drawingsPage";

	public final static int REQ_METHOD_POST_TASK_SHARE = 100810;
	public final static String REQ_URL_POST_TASK_SHARE = BASE_URL
			+ "task/shareCallback";

	public final static int REQ_METHOD_GET_ROSTER = 530;
	public final static String REQ_URL_GET_ROSTER = BASE_URL
			+ "%s/getRoster";

	public final static int REQ_METHOD_GET_FOOTBALL_PLAYERINFO = 531;
	public final static String REQ_URL_GET_FOOTBALL_PLAYERINFO = BASE_URL
			+ "%s/playerInfo";

	public final static int REQ_METHOD_GET_FOOTBALL_COACHINFO = 532;
	public final static String REQ_URL_GET_FOOTBALL_COACHINFO = BASE_URL
			+ "%s/coachInfo";

	public final static int REQ_METHOD_GET_WEIXIN_RECHARGE = 101000;

	//6.0.0
	public final static int REQ_METHOD_POST_REGISTER_PASSPORT = 106000;
	public final static String REQ_URL_POST_REGISTER_PASSPORT = BASE_URL
			+ "user/registerPassport";

	public final static int REQ_METHOD_POST_LOGIN_EMAIL = 106001;
	public final static String REQ_URL_POST_LOGIN_EMAIL = BASE_URL
			+ "user/loginUsernameEmail";

	public final static int REQ_METHOD_CHANGE_MOBILE = 106002;
	public final static String REQ_URL_CHANGE_MOBILE = BASE_URL
			+ "user/changeMobile";

	public final static int REQ_METHOD_USER_BIND_INFO = 106003;
	public final static String REQ_URL_USER_BIND_INFO = BASE_URL
			+ "user/getUserBind";

	public final static int REQ_METHOD_USER_REDDOT = 106004;
	public final static String REQ_URL_USER_REDDOT = BASE_URL
			+ "status/reddots";


	public final static int REQ_METHOD_GET_HUPUDOLLOR_PACKAGES = 100900;
	public final static String REQ_URL_GET_HUPUDOLLOR_PACKAGES = BASE_URL
			+ "hupuDollor/getEvents";


	public final static int REQ_METHOD_GET_HUPUDOLLOR_BALANCE = 100901;
	public final static String REQ_URL_GET_HUPUDOLLOR_BALANCE = BASE_URL
			+ "hupuDollor/getBalance";
	public final static int REQ_METHOD_GET_HUPUDOLLOR_RECHARGE = 100902;
	public final static String REQ_URL_GET_HUPUDOLLOR_RECHARGE = BASE_URL + "hupuDollor/recharge";

	public final static int REQ_METHOD_GET_HUPUDOLLOR_INFO = 100903;
	public final static String REQ_URL_GET_HUPUDOLLOR_INFO = BASE_URL
			+ "hupuDollor/payDetail";

	public final static int REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS = 100904;
	public final static String REQ_URL_GET_HUPUDOLLOR_RECHARGEBEANS = BASE_URL + "hupuDollor/rechargeBeans";


	public final static int REQ_METHOD_GET_PLAY_LIVE_SEND_GIFT = 100910;
	public final static String REQ_URL_GET_PLAY_LIVE_SEND_GIFT = BASE_URL
			+ "gift/send";
	
	public final static int REQ_METHOD_GET_PLAY_LIVE_ROOMLIST = 10091;
	public final static String REQ_URL_GET_PLAY_LIVE_ROOMLIST = BASE_URL+"room/getList";


	// public final static int REQ_METHOD_USER_TEST = 222;
	// public final static String REQ_URL_USER_TEST =
	// "https://graph.qq.com/user/get_simple_userinfo";

	public static String getUrl(int mId) {
		switch (mId) {
		//虎扑币相关start
		case REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS:
			return REQ_URL_GET_HUPUDOLLOR_RECHARGEBEANS;
		case REQ_METHOD_GET_HUPUDOLLOR_PACKAGES:
			return REQ_URL_GET_HUPUDOLLOR_PACKAGES;
		case REQ_METHOD_GET_HUPUDOLLOR_BALANCE:
			return REQ_URL_GET_HUPUDOLLOR_BALANCE;
		case REQ_METHOD_GET_HUPUDOLLOR_RECHARGE:
			return REQ_URL_GET_HUPUDOLLOR_RECHARGE;
		case REQ_METHOD_GET_HUPUDOLLOR_INFO:
			return REQ_URL_GET_HUPUDOLLOR_INFO;
			//虎扑币end
			//礼物相关start
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_SEND_GIFT:
			return REQ_URL_GET_PLAY_LIVE_SEND_GIFT;
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ROOMLIST:
			return REQ_URL_GET_PLAY_LIVE_ROOMLIST;
		case REQ_METHOD_GET_TEAMS:
			return REQ_URL_GET_TEAMS;

			// case REQ_METHOD_FOLLOW_TEAM:
			// case REQ_METHOD_FOLLOW_TEAM_CANCEL:
			// return REQ_URL_FOLLOW_TEAM;

		case REQ_METHOD_SET_FOLLOW_TEAMS:
			return REQ_URL_SET_FOLLOW_TEAMS;

		case REQ_METHOD_BOX_SCORE:
			return REQ_URL_BOX_SCORE;

		case REQ_METHOD_NBA_GAMES_BY_GID:

		case REQ_METHOD_NBA_GAMES_BY_DATE:
			return REQ_URL_GET_NBA_GAMES;
		default:
			return null;

		case REQ_METHOD_GET_STANDINGS:
			return REQ_URL_GET_STANDINGS;

		case REQ_METHOD_GET_PLAY_LIVE_ASC:
		case REQ_METHOD_GET_PLAY_LIVE_DESC:
			return REQ_URL_GET_PLAY_LIVE;

		case REQ_METHOD_GET_RECAP:
			return REQ_URL_GET_RECAP;

		case REQ_METHOD_FOLLOW_NBA_GAME:
		case REQ_METHOD_FOLLOW_NBA_GAME_CANCEL:
			return REQ_URL_FOLLOW_NBA_GAME;

		case REQ_METHOD_GET_FOLLOW_TEAMS:
			return REQ_URL_GET_FOLLOW_TEAMS;
		case REQ_METHOD_REDIRECTOR:
			return REQ_URL_REDIRECTOR;

		case REQ_METHOD_GET_NBA_VIDEO:
		case REQ_METHOD_GET_NBA_VIDEO_GAME:
		case REQ_METHOD_GET_NBA_VIDEO_GAME_NEXT:
		case REQ_METHOD_GET_NBA_VIDEO_HOT:
		case REQ_METHOD_GET_NBA_VIDEO_HOT_NEXT:
			return REQ_URL_GET_NBA_VIDEO;

		case REQ_METHOD_GET_CBA_VIDEO_GAME:
		case REQ_METHOD_GET_CBA_VIDEO_GAME_NEXT:
			return REQ_URL_GET_CBA_VIDEO;

		case REQ_METHOD_GET_NBA_NEWS:
		case REQ_METHOD_GET_NBA_NEWS_NEXT:
		case REQ_METHOD_GET_NBA_NEWS_PRE:
			return REQ_URL_GET_NBA_NEWS;

		case REQ_METHOD_GET_CBA_NEWS:
		case REQ_METHOD_GET_CBA_NEWS_NEXT:
		case REQ_METHOD_GET_CBA_NEWS_PRE:
			return REQ_URL_GET_CBA_NEWS;

		case REQ_METHOD_BASKETBALL_NEXT:
		case REQ_METHOD_BASKETBALL_PREV:
			return REQ_URL_GET_BASKETBALL;

		case REQ_METHOD_CBA_BOX_SCORE:
			return REQ_URL_CBA_BOX_SCORE;

		case REQ_METHOD_NBA_CHAT:
		case REQ_METHOD_NBA_CHAT_MORE:
		case REQ_METHOD_NBA_CHAT_NEW:
			return REQ_URL_NBA_CHAT;

		case REQ_METHOD_SENT_CHAT:
			return REQ_URL_SENT_CHAT;

		case REQ_METHOD_GET_NICK_NAME:
			return REQ_URL_GET_NICK_NAME;

		case REQ_METHOD_SET_NICK_NAME:
			return REQ_URL_SET_NICK_NAME;

		case REQ_METHOD_RATING_LIST:
			return REQ_URL_RATING_LIST;

		case REQ_METHOD_RATING_DETAIL:
		case REQ_METHOD_RATING_DETAIL_MORE:
			return REQ_URL_RATING_DETAIL;

		case REQ_METHOD_RATING_LIKE:
			return REQ_URL_RATING_LIKE;

		case REQ_METHOD_RATING_RATE:
			return REQ_URL_RATING_RATE;

		case REQ_METHOD_USER_INIT:
			return REQ_URL_USER_INIT;

		case REQ_METHOD_USER_LOGOUT:
			return REQ_URL_USER_LOGOUT;

		case REQ_METHOD_USER_UNBIND:
			return REQ_URL_USER_UNBIND;

		case REQ_METHOD_FOLLOW_LEAGUE:
			return REQ_URL_FOLLOW_LEAGUE;

		case REQ_METHOD_FOLLOW_ALL_TEAM:
			return REQ_URL_FOLLOW_ALL_TEAM;

		case REQ_METHOD_FOLLOW_GAME:
		case REQ_METHOD_FOLLOW_GAME_CANCEL:
			return REQ_URL_FOLLOW_GAME;

		case REQ_METHOD_GET_VERIFY_CODE:
			return REQ_URL_GET_VERIFY_CODE;

		case REQ_METHOD_POST_PHONE_BIND:
			return REQ_URL_POST_PHONE_BIND;

		case REQ_METHOD_POST_ORDER:
			return REQ_URL_POST_ORDER;
		case REQ_METHOD_GET_BALANCE:
			return REQ_URL_GET_BALANCE;

		case REQ_METHOD_POST_BUY_ITEM:
			return REQ_URL_POST_BUY_ITEM;
		case REQ_METHOD_GET_PACKAGES:
			return REQ_URL_GET_PACKAGES;

		case REQ_METHOD_GET_GOLD:
			return REQ_URL_GET_GOLD;

		case REQ_METHOD_GET_EXCHANGEBEANS:
			return REQ_URL_GET_EXCHANGEBEANS;

		case REQ_METHOD_GET_TOTAL_HONOUR:
			return REQ_URL_GET_TOTAL_HONOUR;

		case REQ_METHOD_CHECK_BAG:
			return REQ_URL_CHECK_BAG;
		case REQ_METHOD_GET_TOTAL_HONOUR_HISTORY:
			return REQ_URL_GET_TOTAL_HONOUR_HISTORY;

		case REQ_METHOD_BUY_EMOJI:
			return REQ_URL_BUY_EMOJI;
		case REQ_METHOD_GET_TOTAL_MATCH_RANKING:
			return REQ_URL_GET_TOTAL_MATCH_RANKING;
		case REQ_METHOD_GET_TOTAL_MAY_BUY:
			return REQ_URL_GET_TOTAL_MAY_BUY;
		case REQ_METHOD_GET_CONTACTS:
			return REQ_URL_GET_CONTACTS;
		case REQ_METHOD_GET_OBTAINSTUFF:
			return REQ_URL_GET_OBTAINSTUFF;
		case REQ_METHOD_GET_BULLETIN_RANK:
			return REQ_URL_GET_BULLETIN_RANK;

		case REQ_METHOD_GET_INTRODUCE_VIP:
			return REQ_URL_GET_INTRODUCE_VIP;
			// --征途
		case REQ_METHOD_QUIZ_COMMIT:
		case REQ_METHOD_QUIZ_LIST_COMMIT:
			return REQ_URL_QUIZ_COMIT;

		case REQ_METHOD_QUIZ_LIST:
			return REQ_URL_QUIZ_LIST;

		case REQ_METHOD_MY_QUIZ_LIST:
		case REQ_METHOD_MY_QUIZ_MORE_LIST:
			return REQ_URL_MY_QUIZ_LIST;

		case REQ_METHOD_GET_GUESS_RANK:
			return REQ_URL_GET_GUESS_RANK;

		case REQ_METHOD_GET_GUESS_ALL_RANK:
			return REQ_URL_GET_GUESS_ALL_RANK;

		case REQ_METHOD_GET_EXCHANGE_LIST:
			return REQ_URL_GET_EXCHANGE_LIST;

		case REQ_METHOD_POST_EXCHANGE:
			return REQ_URL_POST_EXCHANGE;

		case REQ_METHOD_GET_BETINFO:
			return REQ_URL_GET_BETINFO;

		case REQ_METHOD_GET_TASKREWARD:
			return REQ_URL_GET_TASKREWARD;

		case REQ_METHOD_GET_MY_PRIZE:
			return REQ_URL_GET_MY_PRIZE;

		case REQ_METHOD_TAKE_PRIZE:
			return REQ_URL_TAKE_PRIZE;

		case REQ_METHOD_GET_DOLE:
			return REQ_URL_GET_DOLE;
		case REQ_METHOD_GET_BOX_COUNT:
			return REQ_URL_GET_BOX_COUNT;
		case REQ_METHOD_POST_INCREASE:
			return REQ_URL_POST_INCREASE;

		case REQ_METHOD_GET_BOX_OPEN:
			return REQ_URL_GET_BOX_OPEN;
		case REQ_METHOD_QUIZ_COMMIT_INCREASE:
			return REQ_URL_QUIZ_COMIT_INCREASE;

		case REQ_METHOD_GET_RECHARGE:
			return REQ_URL_GET_RECHARGE;

		case REQ_METHOD_GET_CAIPIAO_RECHARGE:
			return REQ_URL_GET_CAIPIAO_RECHARGE;

		case REQ_METHOD_BET_COINS:
			return REQ_URL_BET_COINS;

		case REQ_METHOD_GET_SIDEBAR:
			return REQ_URL_GET_SIDEBAR;

		case REQ_METHOD_GET_USERBOX:
			return REQ_URL_GET_USERBOX;
		case REQ_METHOD_SET_NOTIFY:
			return REQ_URL_SET_NOTIFY;
		case REQ_METHOD_GET_COIN_INFO:
			return REQ_URL_GET_COIN_INFO;
		case REQ_METHOD_TASK:
			return REQ_URL_TASK;
		case REQ_METHOD_GET_BOX_BALANCE:
			return REQ_URL_GET_BOX_BALANCE;
		case REQ_METHOD_USER_BIND:
			return REQ_URL_USER_BIND;
		case REQ_METHOD_GET_NBA_TEAM_PROGRAM:
			return REQ_URL_GET_NBA_TEAM_PROGRAM;
		case REQ_METHOD_GET_NBA_TEAM_FULL_PROGRAM:
			return REQ_URL_GET_NBA_TEAM_FULL_PROGRAM;
		case REQ_METHOD_GET_NBA_TEAM_ROSTER:
			return REQ_URL_GET_NBA_TEAM_ROSTE;

		case REQ_METHOD_GET_NBA_PLAYER_DATA:
			return REQ_URL_GET_NBA_PLAYER_DATA;

		case REQ_METHOD_GET_NBA_PLAYER_INFO:
			return REQ_URL_GET_NBA_PLAYER_INFO;
			// case REQ_METHOD_USER_TEST:
			// return REQ_URL_USER_TEST;
		case REQ_METHOD_FOLLOW_ONE_TEAM:
		case REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL:
			return REQ_URL_FOLLOW_ONE_TEAM;

		case REQ_METHOD_CAIPIAO_COMMIT:
			return REQ_URL_CAIPIAO_COMIT;

		case REQ_METHOD_GET_WALLET_BALANCE:
			return REQ_URL_WALLET_BALANCE;

		case REQ_METHOD_GET_RECHARGEMETHOD:
			return REQ_URL_RECHARGEMETHOD;

		case REQ_METHOD_MY_CAIPIAO_LIST:
		case REQ_METHOD_MY_CAIPIAO_MORE_LIST:
			return REQ_URL_MY_CAIPIAO_LIST;

		case REQ_METHOD_GET_CAIPIAO_COIN_INFO:
			return REQ_URL_GET_CAIPIAO_COIN_INFO;

		case REQ_METHOD_GET_CAIPIAO_ATM:
			return REQ_URL_GET_CAIPIAO_ATM;

		case REQ_METHOD_CAIPIAO_AGREEMENT:
			return REQ_URL_CAIPIAO_AGREEMENT;

		case REQ_METHOD_CAIPIAO_FAQ:
			return REQ_URL_CAIPIAO_FAQ;

		case REQ_METHOD_CAIPIAO_DETAIL:
			return REQ_URL_CAIPIAO_DETAIL;

		case REQ_METHOD_POST_TASK_SHARE:
			return REQ_URL_POST_TASK_SHARE;

		case REQ_METHOD_GET_GOMMENT_INFO:
			return REQ_URL_GET_GOMMENT_INFO;
		case REQ_METHOD_GET_LINK_REPLY:
		case REQ_METHOD_GET_MORE_LINK_REPLY:
			return REQ_URL_GET_lINK_REPLY;
		case REQ_METHOD_POST_LINK_REPLY:
			return REQ_URL_POST_LINK_REPLY;
		case REQ_METHOD_POST_LINK_LIGHT:
			return REQ_URL_POST_LINK_LIGHT;
		case REQ_METHOD_POST_REGISTER_PASSPORT:
			return REQ_URL_POST_REGISTER_PASSPORT;
		case REQ_METHOD_POST_LOGIN_EMAIL:
			return REQ_URL_POST_LOGIN_EMAIL;
		case REQ_METHOD_CHANGE_MOBILE:
			return REQ_URL_CHANGE_MOBILE;
		case REQ_METHOD_USER_BIND_INFO:
			return REQ_URL_USER_BIND_INFO;
		case REQ_METHOD_USER_REDDOT:
			return REQ_URL_USER_REDDOT;
		}
	}

	/**
	 * 请求地址需要格式化的
	 * */
	public static String getUrl(int mId, String format) {
		switch (mId) {
		case REQ_METHOD_FOOTBALL_LEAGUE:
		case REQ_METHOD_FOOTBALL_LEAGUE_NEXT:
		case REQ_METHOD_FOOTBALL_LEAGUE_PREV:
		case REQ_METHOD_FOOTBALL_LEAGUE_REFRESH:
			return String.format(REQ_URL_GET_FOOTBALL_LEAGUE, format);
		case REQ_METHOD_GET_FOOTBALL_VIDEO:
		case REQ_METHOD_GET_FOOTBALL_VIDEO_NEXT:
			return String.format(REQ_URL_GET_FOOTBALL_VIDEO, format);
		case REQ_METHOD_GET_FOOTBALL_RANK:
			return String.format(REQ_URL_GET_FOOTBALL_RANK, format);

		case REQ_METHOD_GET_FOOTBALL_STATISTIC:
			return String.format(REQ_URL_GET_FOOTBALL_STATISTIC, format);
		case REQ_METHOD_GET_FOOTBALL_EVENTS:
			return String.format(REQ_URL_GET_FOOTBALL_EVENTS, format);
		case REQ_METHOD_GET_GAME_REPORT:
			return String.format(REQ_URL_GET_GAME_REPORT, format);
		case REQ_METHOD_GET_FOOTBALL_NEWS:
		case REQ_METHOD_GET_FOOTBALL_NEWS_NEXT:
			return String.format(REQ_URL_GET_FOOTBALL_NEWS, format);
		case REQ_METHOD_GET_NEWS_DETAIL:
			return String.format(REQ_URL_GET_NEWS_DETAIL, format);
		case REQ_METHOD_EN_SENT_CHAT:
			return String.format(REQ_URL_EN_SENT_CHAT, format);
		case REQ_METHOD_GET_LIVE_ASC:
		case REQ_METHOD_GET_LIVE_DESC:
			return String.format(REQ_URL_GET_LIVE, format);
			// case REQ_METHOD_GET_FOOTBALL_EVENT:
			// return String.format( REQ_URL_GET_FOOTBALL_EVENT, format);
		case REQ_METHOD_POST_REPLY:
			return String.format(REQ_URL_POST_REPLY, format);
		case REQ_METHOD_POST_LIGHT:
			return String.format(REQ_URL_POST_LIGHT, format);
		case REQ_METHOD_GET_REPLY:
		case REQ_METHOD_GET_MORE_REPLY:
			return String.format(REQ_URL_GET_REPLY, format);
		case REQ_METHOD_CBA_GAME_BY_GID:
		case REQ_METHOD_FOOTBALL_GAME_BY_GID:
			return String.format(REQ_URL_GET_GAME_BY_GID, format);

		case REQ_METHOD_GET_ROSTER:
			return String.format(REQ_URL_GET_ROSTER, format);
		case REQ_METHOD_GET_SOCCER_TEAM_PROGRAM:
			return String.format(REQ_URL_GET_SOCCER_TEAM_PROGRAM, format);

		case REQ_METHOD_GET_SOCCER_TEAM_ROSTER:
			return String.format(REQ_URL_GET_SOCCER_TEAM_ROSTE, format);
		case REQ_METHOD_GET_SOCCER_TEAM_FULL_PROGRAM :
			return String.format(REQ_URL_GET_SOCCER_TEAM_FULL_PROGRAM,format);
		case REQ_METHOD_GET_FOOTBALL_OUTS :
			return String.format(REQ_URL_GET_FOOTBALL_OUTS,format);

		case REQ_METHOD_GET_FOOTBALL_PLAYERINFO:
			return String.format(REQ_URL_GET_FOOTBALL_PLAYERINFO,format);

		case REQ_METHOD_GET_FOOTBALL_COACHINFO:
			return String.format(REQ_URL_GET_FOOTBALL_COACHINFO,format);
		case REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT:
		case REQ_METHOD_BASKETBALL_GAMES_BY_PRE:
		case REQ_METHOD_BASKETBALL_GAMES_BY_NEXT:
			return String.format(REQ_URL_GET_BASKETBALL_GAMES,format);
		default:
			return getUrl(mId);
		}
	}

	public static final String SOCKET_SERVER_DEFAULT = "http://192.168.8.49:8080/nba_v1";

	public static final String ROOM_NBA_HOME = "NBA_HOME";

	public static final String ROOM_CBA_HOME = "CBA_HOME";

//	public static final String ROOM_PLAYBYPLAY = "NBA_PLAYBYPLAY";
	public static final String ROOM_PLAYBYPLAY = "NBA_PLAYBYPLAY_CASINO";

	public static final String ROOM_NBA_PLAYBYPLAY_CASINO = "NBA_PLAYBYPLAY_CASINO";
//	public static final String ROOM_NBA_PLAYBYPLAY_CASINO = "NBA_PLAYBYPLAY";

	public static final String ROOM_NBA_CASINO = "NBA_CASINO";

//	public static final String ROOM_CBA_PLAYBYPLAY = "CBA_PLAYBYPLAY";
	public static final String ROOM_CBA_PLAYBYPLAY = "CBA_PLAYBYPLAY_CASINO";

	public static final String ROOM_CBA_BOXSCORE = "CBA_BOXSCORE";

//	public static final String ROOM_CBA_PLAYBYPLAY_CASINO = "CBA_PLAYBYPLAY_CASINO";
	public static final String ROOM_CBA_PLAYBYPLAY_CASINO = "CBA_PLAYBYPLAY_CASINO";

	public static final String ROOM_NBA_BOXSCORE = "NBA_BOXSCORE";

	public static final String ROOM_CBA_CASINO = "CBA_CASINO";

	public static final String ROOM_CASINO = "_CASINO";

	public static final String ROOM_NBA_CHAT = "CHAT";

	public static final String ROOM_USER_NOTIFY = "USER_NOTIFY";

	// -------------for xiaoming-------------//
	public static final String TEMPLATE_SOCCER_LEAGUE = "soccerleagues";
	public static final String TEMPLATE_SOCCER_CUP_LEAGUE = "soccercupleagues";
	public static final String TEMPLATE_NBA = "nba";
	public static final String TEMPLATE_CBA = "cba";
	public static final String TEMPLATE_BROWSER = "browser";
	public static final String TEMPLATE_BROWSER_NAV = "browser_no_nav";
	public static final String TEMPLATE_DISCOVERY = "discovery";
	public static final String TEMPLATE_ACCOUNT = "account";

	public static final String TAB_GAMES = "games";
	public static final String TAB_NEWS = "news";
	public static final String TAB_VIDEO = "video";
	public static final String TAB_RANKS = "ranks";
	public static final String TAB_DATA = "data";

	public static final String SUB_TAB_RECAP = "recap";
	public static final String SUB_TAB_STATS = "stats";
	public static final String SUB_TAB_EVENTS = "events";
	public static final String SUB_TAB_LIVE = "live";
	public static final String SUB_TAB_RATINGS = "ratings";
	public static final String SUB_TAB_RATING = "rating";
	public static final String SUB_TAB_CASINO = "casino";
	public static final String SUB_TAB_CHAT = "chat";

	public static final String SUB_TAB_PLAYER = "player";
	public static final String SUB_TAB_TEAM = "team";
	public static final String SUB_TAB_NEWS = "news";
	public static final String SUB_TAB_ACCOUNT = "account";
	public static final String SUB_TAB_NICKNAME = "nickname";

	public static void setServer(String s1, String s2) {
		if (s1 != null && s1.indexOf("http") < 0) {
			socket_server = "http://" + s1 + "/nba_v1";
			socket_backup = "http://" + s2 + "/nba_v1";
		}
	}

	static String strClient = "xxx";
	static String strToken = "";

	public static String getDefaultServer() {
		if (socket_server != null)
			return socket_server;
		if (socket_server == null
				&& SharedPreferencesMgr.getString("default_server", null) != null) {
			socket_server = "http://"
					+ SharedPreferencesMgr.getString("default_server", null)
					+ "/nba_v1";
			return socket_server;
		}
		return null;
	}

	public static String getBackUpServer() {
		if (socket_backup != null)
			return null;
		if (socket_backup == null
				&& SharedPreferencesMgr.getString("backup_server", null) != null) {
			socket_backup = "http://"
					+ SharedPreferencesMgr.getString("backup_server", null)
					+ "/nba_v1";
			return socket_backup;
		}
		return null;
	}

	private static String socket_server;
	private static String socket_backup;

	public static String getParameter() {
		return "?client=" + strClient + "&t=" + System.currentTimeMillis()
				/ 1000 + "&type=1";
	}

	public static void setClient(String client) {
		strClient = client;
	}

	public static String KEY_NICK_NAME = "nickname";

	// 4.6 重新定义umeng 自定义事件
	public static String UMENG_EVENT_NOTIFICATION = "notification";
	public static String UMENG_KEY_SEND = "send";
	public static String UMENG_KEY_CLICK = "click";
	// 开机
	public static String UMENG_EVENT_PROMOTION = "promotion";
	public static String UMENG_KEY_SPLASH = "splash";
	public static String UMENG_VALUE_SPLASH = "display";
	public static String UMENG_VALUE_CLICKBTN = "clickBtn";
	public static String UMENG_VALUE_PAGEFINISHED = "pageFinished";

	// 公共KEY
	public static String UMENG_VALUE_GAMES = "games";
	public static String UMENG_VALUE_NEWS = "news";
	public static String UMENG_VALUE_VIDEOS = "videos";
	public static String UMENG_VALUE_RANKINGS = "rankings";
	public static String UMENG_VALUE_STATS = "stats";
	public static String UMENG_KEY_STATS = "stats";
	public static String UMENG_KEY_NAV_SUM = "nav_Sum";
	public static String UMENG_EVENT_NAV = "nav";
	public static String UMENG_EVENT_VISIT = "visit";
	public static String UMENG_VALUE_MENU_BTN = "menuBtn";
	public static String UMENG_VALUE_VIEW_MORE = "viewMore";
	public static String UMENG_VALUE_FOLD_MORE = "foldMore";
	public static String UMENG_VALUE_MY_ACCOUNT = "myAccount";
	public static String UMENG_VALUE_QUIZ_RANK = "quizRanking";
	// nab
	// public static String UMENG_EVENT_NBA = "NBA";
	// public static String UMENG_KEY_NAV_NBA = "nav_NBA";
	// public static String UMENG_EVENT_LIGA = "LIGA";
	// public static String UMENG_KEY_NAV_LIGA = "nav_LIGA";
	// public static String UMENG_EVENT_CBA = "CBA";
	// public static String UMENG_KEY_NAV_CBA = "nav_CBA";
	// public static String UMENG_EVENT_CHLG = "CHLG";
	// public static String UMENG_KEY_NAV_CHLG = "nav_CHLG";
	// public static String UMENG_EVENT_CSL = "CSL";
	// public static String UMENG_KEY_NAV_CSL = "nav_CSL";
	// public static String UMENG_EVENT_EPL = "EPL";
	// public static String UMENG_KEY_NAV_EPL = "nav_EPL";
	// public static String UMENG_EVENT_BUND = "BUND";
	// public static String UMENG_KEY_NAV_BUND = "nav_BUND";
	// public static String UMENG_EVENT_SERI = "SERI";
	// public static String UMENG_KEY_NAV_SERI = "nav_SERI";
	// public static String UMENG_EVENT_FRAN = "FRAN";
	// public static String UMENG_KEY_NAV_FRAN = "nav_FRAN";

	public static String UMENG_EVENT_NBA_TEAMS = "nbaTeams";
	public static String UMENG_KEY_TEAMS = "teams";
	public static String UMENG_KEY_FULL_SCHEDULE = "fullSchedule";
	public static String UMENG_KEY_ROSTER = "roster";
	public static String UMENG_VALUE_NBA_RANK = "nbaRanking";
	public static String UMENG_VALUE_NBA_SCORE_CARD = "nbaScoreCard";
	public static String UMENG_VALUE_NBA_TEAM_REVIEW = "nbaTeamReview";
	public static String UMENG_VALUE_TEAMS_TAB = "teamsTab";
	public static String UMENG_VALUE_FOLLOW = "follow";
	public static String UMENG_VALUE_UNFOLLOW_CONFIRM = "unfollow_confirm";
	public static String UMENG_VALUE_UNFOLLOW_CANCEL = "unfollow_cancel";
	public static String UMENG_VALUE_FULLSCHEDULE = "fullschedule";
	public static String UMENG_VALUE_TAP_ONE_GAME = "tapOneGame";

	public static String UMENG_VALUE_ROSTER_TAB = "rosterTab";
	public static String UMENG_VALUE_TAP_ONE_PLAYER = "tapOnePlayer";
	public static String UMENG_VALUE_STATS_TAB = "statsTab";

	public static String UMENG_KEY_ENTRANCE = "entrance";
	public static String UMENG_EVENT_NBAPLAYERS = "nbaPlayers";
	public static String UMENG_EVENT_PLAYER_DETAIL = "playerDetail";
	public static String UMENG_VALUE_NBA_STATS = "nbaStats";
	public static String UMENG_VALUE_NBA_BOX_SCORE = "nbaBoxscore";
	public static String UMENG_VALUE_NBA_PLAYER_REVIEW = "nbaPlayerReview";
	public static String UMENG_VALUE_TAP_ONE_TEAM = "tapOneTeam";

	public static String UMENG_EVENT_MALL = "mall";
	public static String UMENG_KEY_AWARD_LIST = "awardList";
	public static String UMENG_KEY_AWARD_DETAIL = "awardDetail";
	public static String UMENG_KEY_MY_AWARD = "myaward";
	public static String UMENG_VALUE_FROM_MY_PAGE = "fromMyPage";
	public static String UMENG_VALUE_TAP_ONE_ITEM = "tapOneItem";
	public static String UMENG_VALUE_AWARD_NUM = "AwardNum";
	public static String UMENG_VALUE_EXCHANGE = "exchange";
	public static String UMENG_VALUE_EXCHANGE_CONFIRM = "exchange_confirm";
	public static String UMENG_VALUE_EXCHANGE_CANCEL = "exchange_cancel";
	public static String UMENG_VALUE_EXCHANGE_SUCCESS = "exchange_success";
	public static String UMENG_VALUE_NO_COIN_ALERT_CONFIRM = "noCoinAlert_confirm";
	public static String UMENG_VALUE_NO_COIN_ALERT_CANCEL = "noCoinAlert_cancel";
	public static String UMENG_VALUE_FROM_MALL_PAGE = "fromMallPage";
	public static String UMENG_VALUE_CONTACE_SERVICE = "contactService";

	public static String UMENG_EVENT_MY_BOX = "myBox";
	public static String UMENG_KEY_GOLDBOX = "goldBox";
	public static String UMENG_KEY_SHAKE_GOLDBOX = "shakeGoldBox";
	public static String UMENG_KEY_SILVERBOX = "silverBox";
	public static String UMENG_KEY_SHAKE_SILVERBOX = "shakeSilverBox";
	public static String UMENG_KEY_BRONZEBOX = "bronzeBox";
	public static String UMENG_KEY_SHAKE_BRONZEBOX = "shakeBronzeBox";

	public static String UMENG_EVENT_LOTTERY = "lottery";
	public static String UMENG_KEY_MY_LOTTERY = "myLottery";
	public static String UMENG_KEY_ANSWER_BTN = "answerBtn";
	public static String UMENG_KEY_LOTTERY_BET = "lotteryBet";
	public static String UMENG_VALUE_LEFT_ANSWER = "leftAnswer";
	public static String UMENG_VALUE_RIGHT_ANSWER = "rightAnswer";
	public static String UMENG_VALUE_CONFIRM_BTN = "confirmBtn";
	public static String UMENG_VALUE_CANCEL_BTN = "cancelBtn";
	public static String UMENG_VALUE_NO_MONEY_ALERT_CONFIRM = "noMoneyAlertConfirm";
	public static String UMENG_VALUE_NO_MONEY_ALERT_CHARGE_BTN = "noMoneyAlertChargeBtn";
	public static String UMENG_VALUE_DOUBLE_CHECK_CONFIRM = "doubleCheckConfirm";
	public static String UMENG_VALUE_DOUBLE_CHECK_CANCEL = "doubleCheckCancel";
	public static String UMENG_EVENT_MY_WALLET = "myWallet";
	public static String UMENG_KEY_WALLET_ACCOUNT = "walletAccount";
	public static String UMENG_KEY_WALLET_CHARGE = "walletCharge";
	public static String UMENG_KEY_CHARGE_BTN = "chargeBtn";
	public static String UMENG_KEY_WALLET_WITHDRAW = "walletWithdraw";

}
