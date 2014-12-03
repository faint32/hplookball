package com.hupu.games.data;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.hupu.games.HuPuApp;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.account.BindInfoReq;
import com.hupu.games.data.account.PhoneBindReq;
import com.hupu.games.data.account.PhoneVerfyCodeReq;
import com.hupu.games.data.account.UserBetInfoReq;
import com.hupu.games.data.account.UserWalletResp;
import com.hupu.games.data.game.base.SimpleLiveResp;
import com.hupu.games.data.game.basketball.BasketballGameEntity;
import com.hupu.games.data.game.basketball.BasketballGamesResp;
import com.hupu.games.data.game.basketball.CBABoxScoreResp;
import com.hupu.games.data.game.basketball.NBALiveResp;
import com.hupu.games.data.game.basketball.NbaPlayerInfoReq;
import com.hupu.games.data.game.basketball.NbaPlayersDataReq;
import com.hupu.games.data.game.basketball.NbaTeamPlayerReq;
import com.hupu.games.data.game.basketball.NbaTeamReq;
import com.hupu.games.data.game.basketball.NbaTeamScheduleReq;
import com.hupu.games.data.game.football.FootballCoachInfoReq;
import com.hupu.games.data.game.football.FootballLeagueResp;
import com.hupu.games.data.game.football.FootballPlayerInfoReq;
import com.hupu.games.data.game.football.FootballStatisticResp;
import com.hupu.games.data.game.football.ScoreboardEntity;
import com.hupu.games.data.game.football.SoccerEventsResp;
import com.hupu.games.data.game.football.SoccerOutsReq;
import com.hupu.games.data.game.football.SoccerPlayerReq;
import com.hupu.games.data.game.football.SoccerTeamReq;
import com.hupu.games.data.game.football.SoccerTeamScheduleReq;
import com.hupu.games.data.game.quiz.QuizCommitResp;
import com.hupu.games.data.game.quiz.QuizResp;
import com.hupu.games.data.goldbean.ExchangeGoldBeanEntity;
import com.hupu.games.data.goldbean.GoldDallorRechargeEntity;
import com.hupu.games.data.goldbean.GoldEntity;
import com.hupu.games.data.news.NewsDetailEntity;
import com.hupu.games.data.news.NewsResp;
import com.hupu.games.data.personal.box.BoxBalanceEntity;
import com.hupu.games.data.personal.box.BoxInfoEntity;
import com.hupu.games.data.personal.box.BoxOpenEntity;
import com.hupu.games.data.personal.box.BoxScoreResp;
import com.hupu.games.data.room.GiftRespResultEntity;
import com.hupu.games.data.room.RoomListEntity;
import com.hupu.games.data.task.TaskRewardEntity;
import com.hupu.games.hupudollor.data.HupuDollorBalanceReq;
import com.hupu.games.hupudollor.data.HupuDollorRechargeReq;
import com.hupu.games.hupudollor.data.OrderHupuDollorPacEntity;

public class JsonPaserFactory {

	public synchronized static BaseEntity paserObj(String s, int type) {

		HupuLog.e("HUPUAPP", "return=="+s);
		BaseEntity entity = null;
		switch (type) {
		//虎扑币相关start
		case HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_PACKAGES:
			entity =new OrderHupuDollorPacEntity();
			break;
		case  HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_BALANCE:
			entity =new HupuDollorBalanceReq();
			break;
		case HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGE:
			entity = new HupuDollorRechargeReq();
			break;
		case HuPuRes.REQ_METHOD_GET_HUPUDOLLOR_RECHARGEBEANS:
			entity = new GoldDallorRechargeEntity();
			break;
		//虎扑币相关end
			
	    //礼物相关start
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_SEND_GIFT:
			entity =new GiftRespResultEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ROOMLIST:
			entity =new RoomListEntity();
			break;
		//礼物相关end
		case HuPuRes.REQ_METHOD_NBA_GAMES_BY_GID:
			entity =new BasketballGameEntity();
			break;
		case HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_DEFAULT:
		case HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_PRE:
		case HuPuRes.REQ_METHOD_BASKETBALL_GAMES_BY_NEXT:
		case HuPuRes.REQ_METHOD_NBA_GAMES_BY_DATE:
			entity = new BasketballGamesResp(type);
			break;
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ASC:
		case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_DESC:
			entity = new NBALiveResp();
			if(s.contains("gift_list")){
				HupuLog.d("NBA 直播contains giftlist");
			}
			
			break;
		case HuPuRes.REQ_METHOD_BOX_SCORE:
			entity = new BoxScoreResp();
			break;
		case HuPuRes.REQ_METHOD_CBA_BOX_SCORE:
			entity = new CBABoxScoreResp();
			break;
		case HuPuRes.REQ_METHOD_GET_RECAP:
		case HuPuRes.REQ_METHOD_GET_GAME_REPORT:
			entity = new Recap();
			break;
		case HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME:
		case HuPuRes.REQ_METHOD_FOLLOW_NBA_GAME_CANCEL:
		case HuPuRes.REQ_METHOD_FOLLOW_GAME:
		case HuPuRes.REQ_METHOD_FOLLOW_GAME_CANCEL:
			entity = new FollowResp();
			break;
		case HuPuRes.REQ_METHOD_GET_FOLLOW_TEAMS:
			entity = new GetFollowTeamsResp();
			break;
			//		case HuPuRes.REQ_METHOD_FOLLOW_TEAM_CANCEL:
			//		case HuPuRes.REQ_METHOD_FOLLOW_TEAM:
		case HuPuRes.REQ_METHOD_SET_FOLLOW_TEAMS:
		case HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM :
		case HuPuRes.REQ_METHOD_FOLLOW_ONE_TEAM_CANCEL :
			entity = new FollowResp();
			break;
		case HuPuRes.REQ_METHOD_REDIRECTOR:
			entity = new AdressEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_STANDINGS:
			entity = new StandingsResp();
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_VIDEO:
		case HuPuRes.REQ_METHOD_GET_NBA_VIDEO_GAME:
		case HuPuRes.REQ_METHOD_GET_NBA_VIDEO_GAME_NEXT:
		case HuPuRes.REQ_METHOD_GET_NBA_VIDEO_HOT:
		case HuPuRes.REQ_METHOD_GET_NBA_VIDEO_HOT_NEXT:

		case HuPuRes.REQ_METHOD_GET_CBA_VIDEO_GAME:
		case HuPuRes.REQ_METHOD_GET_CBA_VIDEO_GAME_NEXT:
			//		case HuPuRes.REQ_METHOD_GET_CBA_VIDEO_HOT:
			//		case HuPuRes.REQ_METHOD_GET_CBA_VIDEO_HOT_NEXT:
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_VIDEO:
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_VIDEO_NEXT:
			entity = new VideoResp();
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_NEWS:
		case HuPuRes.REQ_METHOD_GET_NBA_NEWS_NEXT:
		case HuPuRes.REQ_METHOD_GET_NBA_NEWS_PRE:

		case HuPuRes.REQ_METHOD_GET_CBA_NEWS:
		case HuPuRes.REQ_METHOD_GET_CBA_NEWS_NEXT:
		case HuPuRes.REQ_METHOD_GET_CBA_NEWS_PRE:
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_NEWS:
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_NEWS_NEXT:
			entity = new NewsResp();
			break;

		case HuPuRes.REQ_METHOD_GET_NEWS_DETAIL:
			entity = new NewsDetailEntity();
			break;

		case HuPuRes.REQ_METHOD_NBA_CHAT:
		case HuPuRes.REQ_METHOD_NBA_CHAT_MORE:
		case HuPuRes.REQ_METHOD_NBA_CHAT_NEW:
			entity = new ChatResp();
			break;

		case HuPuRes.REQ_METHOD_SENT_CHAT:
		case HuPuRes.REQ_METHOD_EN_SENT_CHAT:
			entity = new SendMsgResp();
			break;
		case HuPuRes.REQ_METHOD_SET_NICK_NAME:
		case HuPuRes.REQ_METHOD_GET_NICK_NAME:
			entity = new NickNameEntity();
			break;
		case  HuPuRes.REQ_METHOD_RATING_LIST :
			entity = new PlayersRatingListResp();
			break;

		case  HuPuRes.REQ_METHOD_RATING_DETAIL :
		case  HuPuRes.REQ_METHOD_RATING_DETAIL_MORE:
			entity =new PlayersRatingByUserListResp ();
			break;

		case  HuPuRes.REQ_METHOD_RATING_LIKE :
			entity =new UserLikeEntity ();
			break;

		case  HuPuRes.REQ_METHOD_RATING_RATE :
			entity =new UserRatingEntity();
			break;
		case HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE:
		case HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_NEXT:
		case HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_PREV:
		case HuPuRes.REQ_METHOD_FOOTBALL_LEAGUE_REFRESH:
			entity =new FootballLeagueResp();
			break;

		case HuPuRes.REQ_METHOD_USER_INIT:
			entity = new InitResp();
			break;
		case HuPuRes.REQ_METHOD_FOLLOW_LEAGUE:
		case HuPuRes.REQ_METHOD_FOLLOW_ALL_TEAM:
			entity = new FollowResultEntity();
			break;

		case HuPuRes.REQ_METHOD_GET_FOOTBALL_STATISTIC:
			entity =new FootballStatisticResp();
			break;
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_EVENTS:
			entity =new SoccerEventsResp();
			break;

		case HuPuRes.REQ_METHOD_GET_FOOTBALL_RANK:
			entity = new RanksResp();
			break;


		case  HuPuRes.REQ_METHOD_GET_VERIFY_CODE :
			entity =new PhoneVerfyCodeReq();
			break;
		case HuPuRes.REQ_METHOD_POST_PHONE_BIND:
		case HuPuRes.REQ_METHOD_USER_BIND:
		case HuPuRes.REQ_METHOD_POST_LOGIN_EMAIL:
			entity =new PhoneBindReq();
			break;
		case  HuPuRes.REQ_METHOD_GET_BALANCE:
			entity =new BalanceReq();
			break;
		case HuPuRes.REQ_METHOD_POST_BUY_ITEM:
		case HuPuRes.REQ_METHOD_BUY_EMOJI:
			entity =new BuyItemEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_PACKAGES:
			entity =new OrderPacEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_GOLD:
			entity =new GoldEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_EXCHANGEBEANS:
			entity =new ExchangeGoldBeanEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_TOTAL_HONOUR:
			entity =new HonourEntity();
			break;
		case HuPuRes.REQ_METHOD_CHECK_BAG:
			entity =new CheckBag();
			break;
		case HuPuRes.REQ_METHOD_GET_TOTAL_HONOUR_HISTORY:
			entity =new MyHonourEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_TOTAL_MATCH_RANKING:
			entity =new SingleHonourEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_TOTAL_MAY_BUY:
			entity =new MayBuyEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_CONTACTS:
			entity=new ContactsEntity();
			break;
		case  HuPuRes.REQ_METHOD_GET_OBTAINSTUFF:
			entity=new ObtainEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_BULLETIN_RANK :
			entity =new BulletinRankReq();
			break;
		case HuPuRes.REQ_METHOD_POST_ORDER :
			entity =new ReqSmsEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_LIVE_ASC:
		case HuPuRes.REQ_METHOD_GET_LIVE_DESC:
			entity =new SimpleLiveResp();
			break;
		case HuPuRes.REQ_METHOD_POST_INCREASE:
		case HuPuRes.REQ_METHOD_POST_LIGHT:
		case HuPuRes.REQ_METHOD_POST_LINK_LIGHT:
		case HuPuRes.REQ_METHOD_QUIZ_COMMIT:
		case HuPuRes.REQ_METHOD_QUIZ_LIST_COMMIT:
		case HuPuRes.REQ_METHOD_QUIZ_COMMIT_INCREASE:
		case HuPuRes.REQ_METHOD_POST_REPLY:
		case HuPuRes.REQ_METHOD_USER_LOGOUT:
		case HuPuRes.REQ_METHOD_USER_UNBIND:
		case HuPuRes.REQ_METHOD_POST_EXCHANGE:
		case HuPuRes.REQ_METHOD_CAIPIAO_COMMIT:
		case HuPuRes.REQ_METHOD_POST_LINK_REPLY:
		case HuPuRes.REQ_METHOD_POST_REGISTER_PASSPORT:
			entity =new QuizCommitResp();
			break;
		case HuPuRes.REQ_METHOD_GET_WALLET_BALANCE:
			entity = new UserWalletResp();
			break;
		case HuPuRes.REQ_METHOD_GET_RECHARGEMETHOD:
			entity = new RechargeMethodReq();
			break;

		case HuPuRes.REQ_METHOD_QUIZ_LIST:
			entity =new QuizResp();
			break;
		case HuPuRes.REQ_METHOD_MY_QUIZ_MORE_LIST: 
		case HuPuRes.REQ_METHOD_MY_QUIZ_LIST:
			entity = new MyQuizResp();
			break;
		case HuPuRes.REQ_METHOD_GET_GUESS_RANK:
			entity =new GuessTopResp();
			break;
		case  HuPuRes.REQ_METHOD_TAKE_PRIZE:
			entity =new CommitExchangeReq();
			break;
		case  HuPuRes.REQ_METHOD_GET_BETINFO:
			entity =new UserBetInfoReq();
			break;
		case  HuPuRes.REQ_METHOD_GET_TASKREWARD:
			entity =new TaskRewardEntity();
			break;
		case  HuPuRes.REQ_METHOD_GET_MY_PRIZE:
			entity =new MyPrizeResp();
			break;
		case HuPuRes.REQ_METHOD_GET_EXCHANGE_LIST:
			entity =new ExchangeResp();
			break;
		case HuPuRes.REQ_METHOD_GET_DOLE:
			entity =new QuizCommitResp();
			break;
		case HuPuRes.REQ_METHOD_GET_BOX_COUNT:
			entity =new BoxInfoEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_BOX_OPEN:
			entity =new BoxOpenEntity();
			break;

		case HuPuRes.REQ_METHOD_GET_RECHARGE:
		case HuPuRes.REQ_METHOD_GET_CAIPIAO_RECHARGE:
			entity =new RechargeReq();
			break;

		case HuPuRes.REQ_METHOD_GET_REPLY:
		case HuPuRes.REQ_METHOD_GET_MORE_REPLY:
		case HuPuRes.REQ_METHOD_GET_LINK_REPLY:
		case HuPuRes.REQ_METHOD_GET_MORE_LINK_REPLY:
			entity = new ReplyRespEntity();
			break;
		case HuPuRes.REQ_METHOD_BET_COINS:
			entity = new BitCoinReq();
			break;
		case HuPuRes.REQ_METHOD_GET_SIDEBAR:
			entity = new SidebarEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_USERBOX:
			entity = new UserBoxEntity();
			break;
		case HuPuRes.REQ_METHOD_SET_NOTIFY:
			entity =new SendMsgResp();
			break;
		case HuPuRes.REQ_METHOD_GET_BOX_BALANCE:
			entity =new BoxBalanceEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_TEAM_PROGRAM:
			entity =new NbaTeamReq();
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_TEAM_FULL_PROGRAM:
			entity =new NbaTeamScheduleReq();
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_TEAM_ROSTER:
			entity = new NbaTeamPlayerReq();
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_PLAYER_DATA:
			entity =new  NbaPlayersDataReq();
			break;
		case HuPuRes.REQ_METHOD_GET_NBA_PLAYER_INFO:
			entity =new  NbaPlayerInfoReq();
			break;
		case HuPuRes.REQ_METHOD_CBA_GAME_BY_GID:
			entity =new BasketballGameEntity();
			break;
		case HuPuRes.REQ_METHOD_FOOTBALL_GAME_BY_GID:
			entity =new ScoreboardEntity();
			break;
		case HuPuRes.REQ_METHOD_MY_CAIPIAO_LIST:
		case HuPuRes.REQ_METHOD_MY_CAIPIAO_MORE_LIST:
			entity = new MyCaipiaoResp();
			break;
		case HuPuRes.REQ_METHOD_GET_ROSTER:
			entity = new TeamLineupResp();
			break;
		case HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_PROGRAM:
			entity=new SoccerTeamReq();
			break;
		case HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_ROSTER:
			entity =new SoccerPlayerReq();
			break;

		case HuPuRes.REQ_METHOD_GET_FOOTBALL_PLAYERINFO:
			entity = new FootballPlayerInfoReq();
			break;
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_COACHINFO:
			entity = new FootballCoachInfoReq();
			break;
		case HuPuRes.REQ_METHOD_GET_GOMMENT_INFO:
			entity = new CommentInfoEntity();
			break;
		case HuPuRes.REQ_METHOD_GET_FOOTBALL_OUTS:
			entity =new SoccerOutsReq();
			break;
		case HuPuRes.REQ_METHOD_GET_SOCCER_TEAM_FULL_PROGRAM:
			entity =new SoccerTeamScheduleReq();
			break;
		case HuPuRes.REQ_METHOD_GET_WEIXIN_RECHARGE:
			entity = new WeixinPayReq();
			break;
		case HuPuRes.REQ_METHOD_USER_BIND_INFO:
			entity = new BindInfoReq();
			break;
		case HuPuRes.REQ_METHOD_USER_REDDOT:
			entity = new RedDotEntity();
			break;		
		}
		if (entity != null)
			try {
				if (type == HuPuRes.REQ_METHOD_REDIRECTOR) {
					((AdressEntity) entity).paser(s);
				} else {
					JSONObject jsonObject = new JSONObject(s);
					switch (type) {
					case HuPuRes.REQ_METHOD_GET_FOLLOW_TEAMS:
						entity.paser(jsonObject);
						break;
					case HuPuRes.REQ_METHOD_GET_WEIXIN_RECHARGE:
						entity.paser(jsonObject);
						break;
					case HuPuRes.REQ_METHOD_GET_PLAY_LIVE_ROOMLIST:
						entity.paser(jsonObject);
						break;
					default:
						//错误信息
						entity.err = isErr(jsonObject);
						if (entity.err  == null) {
							if (!isNull(jsonObject))// 非空的数据解析
								entity.paser(jsonObject);
						}
						break;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return entity;
	}

	private static String isErr(JSONObject jsonObject) {
		// 错误ID及错误说明（当其存在，result值无效）
		jsonObject = jsonObject.optJSONObject("error");
		if(jsonObject!=null)
			return jsonObject.optString("text", "");
		return null;
	}

	private static boolean isNull(JSONObject jsonObject) {
		String ss = jsonObject.optString(BaseEntity.KEY_RESULT, null);
		if (ss == null)
			return true;
		if (ss.equals("{}") || ss.equals("[]"))
			return true;
		return false;

	}

	public static BaseEntity paserObj(HttpEntity en, int type) {

		if (HuPuApp.isDebugMode&& en!= null)
			HupuLog.e("papa", "return=="+en.toString());
		BaseEntity entity = null;
		if(en!=null)
		{
			try {
				//				Value dynamic = MessagePack.unpack(EntityUtils.toByteArray(en));
				//				entity= paserObj(dynamic.toString(),  type); 
				entity= paserObj(EntityUtils.toString(en),  type); 
				//				Log.e("papa", "return=="+dynamic.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return entity;
	}

}
