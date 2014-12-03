package com.hupu.games.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hupu.games.common.HupuLog;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.DiscoveryEntity;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.TeamsEntity;

public class HuPuDBAdapter {

	// Database open/upgrade helper
	private HuPuDBHelp dbHelper;
	Context context;
	final int DATABASE_VERSION = 8;
	static final int MAX_RECORD = 20;

	public HuPuDBAdapter(Context _context) {
		context = _context;
		dbHelper = new HuPuDBHelp(context, DATABASE_VERSION);
	}

	SQLiteDatabase db;

	public SQLiteDatabase open() {
		if (db != null && db.isOpen())
			return db;
		try {
			db = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return db;
	}

	public void close() {
		if (db != null && db.isOpen())
			db.close();
	}

	public long insertTeam(int teamId, String teamName) {
		open();
		ContentValues contentValues = new ContentValues();
		contentValues.put(HuPuDBHelp.KEY_TEAM_ID, teamId);
		if (teamName != null)
			contentValues.put(HuPuDBHelp.KEY_TEAM_NAME, teamName);
		long l = db.insert(HuPuDBHelp.TABLE_TEAM_FOLLOW, null, contentValues);
		db.close();
		return l;
	}

	public void insertTeams(LinkedList<Integer> list) {
		open();
		db.beginTransaction(); // 手动设置开始事务
		// 数据插入操作循环
		for (int i = 0; i < list.size(); i++) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(HuPuDBHelp.KEY_TEAM_ID, list.get(i));
			db.insert(HuPuDBHelp.TABLE_TEAM_FOLLOW, null, contentValues);
		}
		db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
		db.endTransaction(); // 处理完成
		db.close();
	}

	/**
	 * 插入新用户联赛数据
	 * 
	 * @param list
	 */
	public void insertLeagueFrist(LinkedList<LeaguesEntity> list) {
		open();
		db.beginTransaction();
		String[] order = SharedPreferencesMgr.getString("league_order",
				"1,2,3,4,5,6,7,8,9").split(",");
		for (int i = 0; i < order.length; i++) {
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).lid == Integer.parseInt(order[i])) {
					ContentValues contentValues = new ContentValues();
					contentValues.put(HuPuDBHelp.KEY_PRIMARY_ID,
							list.get(j).lid + "_" + list.get(j).en);
					contentValues.put(HuPuDBHelp.KEY_LID, list.get(j).lid);
					contentValues.put(HuPuDBHelp.KEY_EN_NAME, list.get(j).en);
					contentValues.put(HuPuDBHelp.KEY_NAME, list.get(j).name);
					contentValues.put(HuPuDBHelp.KEY_LOGO, list.get(j).logo);
					contentValues.put(HuPuDBHelp.KEY_IS_FOLLOW,
							list.get(j).is_follow);
					contentValues.put(HuPuDBHelp.KEY_TEMPLATE,
							list.get(j).template);
					contentValues.put(HuPuDBHelp.KEY_DEL_TAB,
							list.get(j).show_default_tab);
					contentValues.put(HuPuDBHelp.KEY_GAME_TYPY,
							list.get(j).game_type);
					contentValues.put(HuPuDBHelp.KEY_SHOW_NEW,
							list.get(j).is_new);
					contentValues.put(HuPuDBHelp.KEY_SHOW_STANDINGS_TYPE,
							list.get(j).showStandings);
					db.insert(HuPuDBHelp.TABLE_LEAGUE02, null, contentValues);
					
					break;
				}
			}
		}
		
		db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
		db.endTransaction(); // 处理完成
		db.close();
	}
	/**
	 * 插入联赛数据
	 * 
	 * @param list
	 */
	public void insertLeague(LinkedList<LeaguesEntity> list) {
		open();
		db.beginTransaction();
		 for (int j = 0; j < list.size(); j++) {
		 ContentValues contentValues = new ContentValues();
		 contentValues.put(HuPuDBHelp.KEY_PRIMARY_ID, list.get(j).lid + "_"
		 + list.get(j).en);
		 contentValues.put(HuPuDBHelp.KEY_LID, list.get(j).lid);
		 contentValues.put(HuPuDBHelp.KEY_EN_NAME, list.get(j).en);
		 contentValues.put(HuPuDBHelp.KEY_NAME, list.get(j).name);
		 contentValues.put(HuPuDBHelp.KEY_LOGO, list.get(j).logo);
		 contentValues.put(HuPuDBHelp.KEY_IS_FOLLOW, list.get(j).is_follow);
		 contentValues.put(HuPuDBHelp.KEY_TEMPLATE, list.get(j).template);
		 contentValues.put(HuPuDBHelp.KEY_DEL_TAB,
		 list.get(j).show_default_tab);
		 contentValues.put(HuPuDBHelp.KEY_GAME_TYPY, list.get(j).game_type);
		 contentValues.put(HuPuDBHelp.KEY_SHOW_NEW, list.get(j).is_new);
		 contentValues.put(HuPuDBHelp.KEY_SHOW_STANDINGS_TYPE,
		 list.get(j).showStandings);
		 db.insert(HuPuDBHelp.TABLE_LEAGUE02, null, contentValues);
		 }
		db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
		db.endTransaction(); // 处理完成
		db.close();
	}

	public void delLeagues() {
		open();
		db.execSQL("DELETE  FROM " + HuPuDBHelp.TABLE_LEAGUE02);
		close();
	}

	/**
	 * 插入球队数据
	 * 
	 * @param list
	 */
	public void insertTeam(LinkedList<TeamsEntity> list, int lid) {
		open();
		db.beginTransaction();
		for (int i = 0; i < list.size(); i++) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(HuPuDBHelp.KEY_PRIMARY_ID,
					lid + "_" + list.get(i).tid);
			contentValues.put(HuPuDBHelp.KEY_TEAM_ID, list.get(i).tid);
			contentValues.put(HuPuDBHelp.KEY_LID, lid);
			contentValues.put(HuPuDBHelp.KEY_EN_NAME, list.get(i).enName);
			contentValues.put(HuPuDBHelp.KEY_NAME, list.get(i).name);
			contentValues.put(HuPuDBHelp.KEY_LOGO, list.get(i).logo);
			contentValues.put(HuPuDBHelp.KEY_COLOR, list.get(i).color);
			contentValues.put(HuPuDBHelp.KEY_IS_FOLLOW, list.get(i).is_follow);

			db.insert(HuPuDBHelp.TABLE_TEAM, null, contentValues);

		}

		db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
		db.endTransaction(); // 处理完成
		db.close();

	}

	/**
	 * 插入发现数据
	 * 
	 * @param list
	 */
	public void insertDiscoveryData(
			ArrayList<ArrayList<DiscoveryEntity>> discoverList) {
		if (discoverList == null)
			return;
		open();
		db.beginTransaction();
		int size = discoverList.size();
		ArrayList<DiscoveryEntity> list;
		DiscoveryEntity entity;
		for (int i = 0; i < size; i++) {
			list = discoverList.get(i);
			int size2 = list.size();
			for (int j = 0; j < size2; j++) {
				entity = list.get(j);
				ContentValues contentValues = new ContentValues();
				contentValues.put(HuPuDBHelp.KEY_BLOCK, i);
				contentValues.put(HuPuDBHelp.KEY_LID, entity.mLid);
				contentValues.put(HuPuDBHelp.KEY_EN_NAME, entity.mEn);
				contentValues.put(HuPuDBHelp.KEY_NAME, entity.mName);
				contentValues.put(HuPuDBHelp.KEY_LOGO, entity.mLogo);
				contentValues.put(HuPuDBHelp.KEY_TEMPLATE, entity.mTemplate);
				contentValues.put(HuPuDBHelp.KEY_DEFAULT_TAB,
						entity.mDefaultTab);
				HupuLog.d("discovery insert=" + entity.mName);
				db.insert(HuPuDBHelp.TABLE_DISCOVERY, null, contentValues);
			}
		}

		db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
		db.endTransaction(); // 处理完成
		db.close();
	}

	/**
	 * 插入发现数据
	 * 
	 * @param list
	 */
	public ArrayList<ArrayList<DiscoveryEntity>> loadDiscoveryData() {
		ArrayList<ArrayList<DiscoveryEntity>> discoverList = new ArrayList<ArrayList<DiscoveryEntity>>();
		open();
		Cursor cursor = db.rawQuery("select * from "
				+ HuPuDBHelp.TABLE_DISCOVERY + " order by "
				+ HuPuDBHelp.KEY_BLOCK + "," + HuPuDBHelp.KEY_ID, null);
		try {

			if (cursor.getCount() > 0) {
				HupuLog.d("discovery count=" + cursor.getCount());
				DiscoveryEntity entity;
				ArrayList<DiscoveryEntity> list = new ArrayList<DiscoveryEntity>();
				int block = 0;
				int lastBlock = -1;
				while (cursor.moveToNext()) {
					entity = new DiscoveryEntity();
					entity.mLid = cursor.getInt(cursor
							.getColumnIndex(HuPuDBHelp.KEY_LID));
					entity.mEn = cursor.getString(cursor
							.getColumnIndex(HuPuDBHelp.KEY_EN_NAME));
					entity.mName = cursor.getString(cursor
							.getColumnIndex(HuPuDBHelp.KEY_NAME));
					entity.mLogo = cursor.getString(cursor
							.getColumnIndex(HuPuDBHelp.KEY_LOGO));
					entity.mTemplate = cursor.getString(cursor
							.getColumnIndex(HuPuDBHelp.KEY_TEMPLATE));
					entity.mDefaultTab = cursor.getString(cursor
							.getColumnIndex(HuPuDBHelp.KEY_DEFAULT_TAB));

					block = cursor.getInt(cursor
							.getColumnIndex(HuPuDBHelp.KEY_BLOCK));
					if (lastBlock != block) {
						list = new ArrayList<DiscoveryEntity>();
						discoverList.add(list);
						lastBlock = block;
					}
					list.add(entity);
					HupuLog.d("discovery load=" + entity.mName);
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			cursor.close();
		}
		close();
		return discoverList;
	}

	/**
	 * 删除球队表
	 */
	public void delTeam() {
		open();
		db.execSQL("DELETE  FROM " + HuPuDBHelp.TABLE_TEAM);
		close();
	}

	/**
	 * 删除球队表
	 */
	public void delDiscovery() {
		open();
		db.execSQL("DELETE  FROM " + HuPuDBHelp.TABLE_DISCOVERY);
		close();
	}

	public LinkedList<LeaguesEntity> getAllLeagues() {
		LinkedList<LeaguesEntity> leagues = new LinkedList<LeaguesEntity>();
		open();
		Cursor cursor = db.rawQuery("select * from "
				+ HuPuDBHelp.TABLE_LEAGUE02, null);
		if (cursor.getCount() > 0) {
			LeaguesEntity league;
			LeaguesEntity discovery = null;
			while (cursor.moveToNext()) {
				league = new LeaguesEntity();
				league.lid = cursor.getInt(0);
				league.en = cursor.getString(1);
				league.name = cursor.getString(2);
				league.logo = cursor.getString(3);
				league.is_follow = cursor.getInt(4);
				league.template = cursor.getString(5);
				league.show_default_tab = cursor.getString(6);
				league.game_type = cursor.getString(7);
				league.is_new = cursor.getInt(8);
				league.showStandings = cursor.getString(cursor
						.getColumnIndex(HuPuDBHelp.KEY_SHOW_STANDINGS_TYPE));
				if (league.en.equals("discovery")) {
					discovery = league;
				}
				leagues.add(league);
			}
			cursor.close();
			if (discovery != null) {
				discovery.mDiscoverList = loadDiscoveryData();
			}
		}
		close();
		return leagues;
	}

	public int getIsRead(int nid) {
		int isRead = 0;
		open();
		Cursor cursor = db.rawQuery("select * from "
				+ HuPuDBHelp.TABLE_READ_NEWS + " where nid = " + nid, null);

		try {
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					isRead = cursor.getInt(1);

				}
				cursor.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			cursor.close();
		}
		close();
		return isRead;
	}

	public void insertIsRead(int nid) {
		open();
		db.beginTransaction();
		ContentValues contentValues = new ContentValues();
		contentValues.put(HuPuDBHelp.KEY_NEWS_ID, nid);
		contentValues.put(HuPuDBHelp.KEY_IS_READ, 1);
		db.insert(HuPuDBHelp.TABLE_READ_NEWS, null, contentValues);

		db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
		db.endTransaction(); // 处理完成
		db.close();
	}

	public LinkedList<TeamsEntity> getTeamsByLid(int lid) {
		LinkedList<TeamsEntity> teams = new LinkedList<TeamsEntity>();
		open();
		Cursor cursor = db.rawQuery("select * from " + HuPuDBHelp.TABLE_TEAM
				+ " where lid = " + lid, null);
		try {

			if (cursor.getCount() > 0) {
				TeamsEntity team;
				while (cursor.moveToNext()) {
					team = new TeamsEntity();
					team.tid = cursor.getInt(1);
					team.enName = cursor.getString(3);
					team.name = cursor.getString(4);
					team.logo = cursor.getString(5);
					team.color = cursor.getString(6);
					team.is_follow = cursor.getInt(7);

					teams.add(team);
				}
				cursor.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			cursor.close();
		}
		close();
		return teams;
	}

	public long insertGame(String gId, String startTime) {
		open();
		ContentValues contentValues = new ContentValues();
		contentValues.put(HuPuDBHelp.KEY_GAME_ID, gId);
		contentValues.put(HuPuDBHelp.KEY_GAME_START_TIME, startTime);
		long l = db.insert(HuPuDBHelp.TABLE_GAME_FOLLOW, null, contentValues);
		db.close();
		return l;
	}

	// public void insertIsFirst() {
	// open();
	// ContentValues contentValues = new ContentValues();
	// contentValues.put(HuPuDBHelp.KEY_IS_FIRST, 1);
	// db.insert(HuPuDBHelp.TABLE_VERSION, null, contentValues);
	// db.close();
	// }

	// public boolean isFirst()
	// {
	// open();
	// Cursor cs=getAllEntries(HuPuDBHelp.TABLE_VERSION) ;
	// int size=cs.getCount();
	// db.close();
	// return size==0;
	// }

	// 得到所有记录
	public Cursor getAllEntries(String table_name) {
		return db.query(table_name, null, null, null, null, null, null);
	}

	public int delTeam(int tId) {
		open();
		int i = db.delete(HuPuDBHelp.TABLE_TEAM_FOLLOW, HuPuDBHelp.KEY_TEAM_ID
				+ "='" + tId + "'", null);
		close();
		return i;
	}

	public void delTeams() {
		open();
		db.execSQL("DELETE  FROM " + HuPuDBHelp.TABLE_TEAM_FOLLOW);
		close();
	}

	public int delGame(String gId) {
		open();
		int i = db.delete(HuPuDBHelp.TABLE_GAME_FOLLOW, HuPuDBHelp.KEY_GAME_ID
				+ "='" + gId + "'", null);
		close();
		return i;
	}

	public LinkedList<Integer> getFollowTeams() {
		LinkedList<Integer> teams = new LinkedList<Integer>();
		open();
		Cursor c = db.rawQuery("select * from " + HuPuDBHelp.TABLE_TEAM_FOLLOW,
				null);
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				teams.add(c.getInt(0));
			}
			c.close();
		}
		close();
		return teams;
	}

	public LinkedList<Integer> getFollowGames() {
		LinkedList<Integer> teams = new LinkedList<Integer>();
		open();
		Cursor c = db.rawQuery("select * from " + HuPuDBHelp.TABLE_GAME_FOLLOW,
				null);
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				teams.add(c.getInt(0));
			}
			c.close();
		}
		close();
		return teams;
	}

	public boolean isFollowTeam(int lid, int tid) {
		open();
		Cursor c = db.rawQuery("select isFollow from " + HuPuDBHelp.TABLE_TEAM
				+ " where lid=" + lid + " and tId=" + tid, null);
		boolean isFollow = false;
		if (c != null) {
			if (c.moveToFirst())
				isFollow = c.getInt(0) > 0;
			c.close();
		}

		close();
		return isFollow;
	}

	public void setFollowTeam(int lid, int tid, int follow) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("isFollow", follow);
		db.update(HuPuDBHelp.TABLE_TEAM, cv, "lid=" + lid + " and tId=" + tid,
				null);
		// Cursor c = db.rawQuery("Update " +
		// HuPuDBHelp.TABLE_TEAM+" set isFollow＝"+follow+" where lid="+lid+" and tId="+tid,
		// null);
		close();
	}

	public Integer delOverTimeGames(String date) {
		open();
		int i = db.delete(HuPuDBHelp.TABLE_GAME_FOLLOW,
				HuPuDBHelp.KEY_GAME_START_TIME + "<" + date, null);
		close();
		return i;
	}

	public HashMap<Integer, Integer> getTeamColor(int leagueId) {

		open();
		Cursor cursor = db.rawQuery("select " + HuPuDBHelp.KEY_TEAM_ID + ","
				+ HuPuDBHelp.KEY_COLOR + " from " + HuPuDBHelp.TABLE_TEAM
				+ " where lid = " + leagueId, null);
		HashMap<Integer, Integer> map = null;
		if (cursor.getCount() > 0) {
			map = new HashMap<Integer, Integer>();
			while (cursor.moveToNext()) {

				int c = Integer.parseInt(cursor.getString(1), 16);
				c |= 0xff000000;
				map.put(cursor.getInt(0), c);
			}
			cursor.close();
		}
		close();
		return map;
	}

}
