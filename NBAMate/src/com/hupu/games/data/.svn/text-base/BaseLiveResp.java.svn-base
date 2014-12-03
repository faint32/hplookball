package com.hupu.games.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.base.SimpleScoreboard;
import com.hupu.games.data.room.GiftEntity;
import com.hupu.games.data.room.RoomEntity;

import android.util.Log;


public class BaseLiveResp extends BaseEntity {

	public ArrayList<LiveEntity> dataList;

	public LinkedList<int[]> mListDel;

	public LinkedList<int[]> mListAdd;


	public LinkedList<LinkedList<LiveEntity>> mListMsg;

	/** 降序or升序 */
	private int i_type;
	private int TYPE_DESC = 0;
	private int TYPE_ASC = 1;

	public String preview;


	public int i_pId;

	public String tvLink;

	public boolean bHasData ;

	public int type;

	public ArrayList<CasinoStatus> casinoList;

	public CasinoInit casinoInit;



	public int follow;

	public SimpleScoreboard scoreBoard;
	public static class CasinoStatus {
		public int status;
		public int userCount;
		public int casino_id;
		public String desc;
		public int rightId;
	}

	public static class CasinoInit
	{
		/** 预留，展示状态（0：隐藏，1：显示）*/
		public int status;
		/**竞猜可下注金额，一般不少于5个  [数字1, 数字2, 数字3, ...]*/
		public int bets[];
	}

	//房间和礼物系统
	public int default_room_id;
	public int is_enter;

	public ArrayList<RoomEntity> roomList;
	public ArrayList<GiftEntity> giftList;


	@Override
	public void paser(JSONObject obj) throws Exception {

		preview=obj.optString("preview", null);
		default_room_id = obj.optInt("default_room_id");
		is_enter = obj.optInt("is_enter");
		JSONArray roomArr = obj.optJSONArray("room_list");
		if (roomArr != null) {
			roomList = new ArrayList<RoomEntity>();
			RoomEntity roomEntity;
			for (int i = 0; i < roomArr.length(); i++) {
				roomEntity = new RoomEntity();
				roomEntity.paser(roomArr.optJSONObject(i));
				roomList.add(roomEntity);
			}
		}
		JSONArray giftArr = obj.optJSONArray("gift_list");
		if (giftArr != null) {
			giftList = new ArrayList<GiftEntity>();
			GiftEntity giftEntity;
			for (int i = 0; i < giftArr.length(); i++) {
				giftEntity = new GiftEntity();
				giftEntity.paser(giftArr.optJSONObject(i));
				giftList.add(giftEntity);
			}
		}
		JSONArray arr = obj.optJSONArray(KEY_DATA);
		int size = 0;
		if (arr != null)
			size = arr.length();

		if (size > 0) {
			bHasData = true;
			LiveEntity entity;
			JSONObject dataObj;
			boolean bHttpMode = true;
			for (int i = 0; i < size; i++) {
				dataObj = arr.getJSONObject(i);
				if (i == 0) {
					if (dataObj.has("a")) {
						bHttpMode = false;
						mListDel = new LinkedList<int[]>();
						mListAdd = new LinkedList<int[]>();
						mListMsg = new LinkedList<LinkedList<LiveEntity>>();
					} else {
						dataList = new ArrayList<LiveEntity>();
					}
				}

				if (bHttpMode) {
					// http 的请求解析
					entity = new LiveEntity();
					entity.paser(dataObj);
					dataList.add(entity);
					type =entity.type;
				} else {
					paserSocketData(dataObj);
				}
			}
		}

		// 比分牌
		if (obj.has(KEY_SCORE_BOARD)) {
			JSONObject score = obj.getJSONObject(KEY_SCORE_BOARD);
			scoreBoard =new SimpleScoreboard();
			scoreBoard.paser(score);
		}

		JSONArray casino = obj.optJSONArray("casino_update");
		if (casino != null) {
			CasinoStatus status;
			JSONObject st;
			int ss = casino.length();
			casinoList =new ArrayList<CasinoStatus>();
			for (int i = 0; i < ss; i++) {
				status = new CasinoStatus();
				st = casino.getJSONObject(i);
				status.casino_id = st.optInt("casino_id");
				status.userCount = st.optInt("user_count");
				status.status = st.optJSONObject("status").optInt("id");
				status.desc = st.optJSONObject("status").optString("desc");
				status.rightId =st.optInt("right_answer");
				casinoList.add(status);
				//				Log.d("paser","id="+status.casino_id+" count="+status.userCount);
			}
		}

		//----
		JSONObject casinoIn =obj.optJSONObject("casino_init");
		if(casinoIn !=null)
		{
			casinoInit= new CasinoInit();
			casinoInit.status =casinoIn.optInt(KEY_STATUS);	
			JSONArray array= casinoIn.optJSONArray("bets");
			if(array !=null)
			{
				casinoInit.bets =new int[array.length()+1];
				for(int i =0 ;i<casinoInit.bets.length-1;i++)
					casinoInit.bets[i]=array.getInt(i);				
			}
		}
		
		//gift 
		if (obj.has("gift_update")) {
			JSONObject gift_update = obj.optJSONObject("gift_update");
			JSONArray updateArr = gift_update.optJSONArray("current");
			if (updateArr != null) {
				giftList = new ArrayList<GiftEntity>();
				GiftEntity giftEntity;
				for (int i = 0; i < updateArr.length(); i++) {
					giftEntity = new GiftEntity();
					giftEntity.paser(updateArr.optJSONObject(i));
					giftList.add(giftEntity);
				}
			}
		}
	}

	private void paserSocketData(JSONObject dataObj) throws Exception {
		// socket的请求解析
		JSONArray arr;
		LiveEntity entity;
		int tempSize;
		if (dataObj.has("d")) {
			// 删除项
			arr = dataObj.getJSONArray("d");
			tempSize = arr.length();
			if (tempSize > 0) {
				int[] tempArr = new int[tempSize];
				for (int j = 0; j < tempSize; j++) {
					tempArr[j] = arr.getInt(j);
					// Log.d("paserSocketData", "del index="+arr.getInt(j) );
				}
				mListDel.add(tempArr);
			}
		}
		if (dataObj.has("a")) {
			// 添加项
			arr = dataObj.getJSONArray("a");
			tempSize = arr.length();
			if (tempSize > 0) {
				int[] tempArr = new int[tempSize];
				LinkedList<LiveEntity> tempList2 = new LinkedList<LiveEntity>();
				JSONObject oo = null;
				for (int j = 0; j < tempSize; j++) {
					oo = arr.getJSONObject(j);
					tempArr[j] = oo.getInt("rowId");
					entity = new LiveEntity();
					entity.paser(oo.getJSONObject("content"));
					tempList2.add(entity);
				}

				mListAdd.add(tempArr);
				mListMsg.add(tempList2);
			}
		}
	}
}
