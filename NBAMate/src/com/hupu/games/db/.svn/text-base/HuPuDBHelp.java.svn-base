package com.hupu.games.db;

import com.hupu.games.common.SharedPreferencesMgr;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HuPuDBHelp extends SQLiteOpenHelper {

	public static final String DB_NAME = "hupu.db";

	public static final String TABLE_VERSION = "t_version";
	public static final String TABLE_TEAM_FOLLOW = "team_follow";
	public static final String TABLE_GAME_FOLLOW = "game_follow";
	public static final String TABLE_FOLLOW_TASK = "follow_task";
	public static final String TABLE_LEAGUE = "t_league";
	public static final String TABLE_LEAGUE02 = "t_league02";
	public static final String TABLE_TEAM = "t_team";
	public static final String TABLE_READ_NEWS = "read_news";
	public static final String TABLE_DISCOVERY = "t_discovery";

	public static final String KEY_TEAM_ID = "tId";
	public static final String KEY_PRIMARY_ID = "primaryId";

	public static final String KEY_TEAM_NAME = "tName";
	/** 比赛id */
	public static final String KEY_GAME_ID = "gId";
	/** 比赛开始时间 */
	public static final String KEY_GAME_START_TIME = "gTime";

	public static final String KEY_ID = "_ID";
	/** 0比赛，1球队 */
	public static final String KEY_TYPE = "_type";
	/** 0关注，1取消 */
	public static final String KEY_IS_FOLLOW = "isFollow";

	public static final String KEY_IS_FIRST = "isFist";

	public static final String KEY_LID = "lid";
	public static final String KEY_EN_NAME = "eName";
	public static final String KEY_NAME = "name";
	public static final String KEY_LOGO = "logo";
	public static final String KEY_TEMPLATE = "showTemplate";
	public static final String KEY_DEFAULT_TAB = "showDefaultTab";
	public static final String KEY_DEL_TAB = "delTab";

	public static final String KEY_COLOR = "color";
	public static final String KEY_GAME_TYPY = "game_type";
	public static final String KEY_SHOW_NEW = "show_new";

	public static final String KEY_NEWS_ID = "nid";
	public static final String KEY_IS_READ = "is_read";
	
	public static final String KEY_BLOCK = "_block";

	public static final String KEY_SHOW_STANDINGS_TYPE = "show_standings_type";

	private static final String CREATE_TEAM_FOLLOW = "create table "
			+ TABLE_TEAM_FOLLOW + " (  " + KEY_TEAM_ID + " INTEGER, "
			+ KEY_TEAM_NAME + "  VARCHAR(25), " + "PRIMARY KEY(" + KEY_TEAM_ID
			+ "));";

	private static final String CREATE_GAME_FOLLOW = "create table "
			+ TABLE_GAME_FOLLOW + " (  " + KEY_GAME_ID + " INTEGER, "
			+ KEY_GAME_START_TIME + " integer, " + "PRIMARY KEY(" + KEY_GAME_ID
			+ "));";

	private static final String CREATE_FOLLOW_TASK = "create table "
			+ TABLE_FOLLOW_TASK + " (  " + KEY_ID + " INTEGER, " + KEY_TYPE
			+ " integer, " + KEY_IS_FOLLOW + " integer, " + "PRIMARY KEY("
			+ KEY_ID + "));";

	private static final String CREATE_VERSION = "create table "
			+ TABLE_VERSION + " (  " + KEY_IS_FIRST + " integer" + ");";

	private static final String CREATE_lEAGUE = "create table " + TABLE_LEAGUE
			+ " ( " + KEY_LID + " INTEGER, " + KEY_EN_NAME + " VARCHAR(25), "
			+ KEY_NAME + " VARCHAR(25), " + KEY_LOGO + " TEXT," + KEY_IS_FOLLOW
			+ " INTEGER, " + KEY_TEMPLATE + " VARCHAR(25), " + KEY_DEL_TAB
			+ " VARCHAR(25), " + KEY_PRIMARY_ID + " VARCHAR(25), "
			+ "PRIMARY KEY(" + KEY_PRIMARY_ID + "));";

	private static final String CREATE_lEAGUE02 = "create table "
			+ TABLE_LEAGUE02 + " ( " + KEY_LID + " INTEGER, " + KEY_EN_NAME
			+ " VARCHAR(25), " + KEY_NAME + " VARCHAR(25), " + KEY_LOGO
			+ " TEXT," + KEY_IS_FOLLOW + " INTEGER, " + KEY_TEMPLATE
			+ " VARCHAR(25), " + KEY_DEL_TAB + " VARCHAR(25), " + KEY_GAME_TYPY
			+ " VARCHAR(25), " + KEY_SHOW_NEW + " INTEGER, " + KEY_PRIMARY_ID
			+ " VARCHAR(25), " + KEY_SHOW_STANDINGS_TYPE + " VARCHAR(16), "
			+ "PRIMARY KEY(" + KEY_PRIMARY_ID + "));";

	private static final String CREATE_TEAM = "create table " + TABLE_TEAM
			+ " ( " + KEY_PRIMARY_ID + " VARCHAR(25), " + KEY_TEAM_ID
			+ " INTEGER," + KEY_LID + " INTEGER," + KEY_EN_NAME
			+ " VARCHAR(25), " + KEY_NAME + " VARCHAR(25), " + KEY_LOGO
			+ " TEXT," + KEY_COLOR + " VARCHAR(25), " + KEY_IS_FOLLOW
			+ " INTEGER, " + "PRIMARY KEY(" + KEY_PRIMARY_ID + "));";

	private static final String CREATE_READ_NEWS = "create table "
			+ TABLE_READ_NEWS + " (  " + KEY_NEWS_ID + " INTEGER, "
			+ KEY_IS_READ + " integer, " + "PRIMARY KEY(" + KEY_NEWS_ID + "));";

	private static final String CREATE_DICOVERY = "create table "
			+ TABLE_DISCOVERY + " ( " +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_BLOCK+ " INTEGER, "+KEY_LID + " INTEGER," + KEY_EN_NAME
			+ " VARCHAR(25), " + KEY_NAME + " VARCHAR(25), " + KEY_LOGO
			+ " TEXT," + KEY_TEMPLATE + " VARCHAR(25), " + KEY_DEFAULT_TAB
			+ " TEXT " +  ");";

	HuPuDBHelp(Context context, int version) {
		super(context, DB_NAME, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db, CREATE_TEAM_FOLLOW);
		createTable(db, CREATE_GAME_FOLLOW);
		createTable(db, CREATE_FOLLOW_TASK);
		// createTable( db,CREATE_VERSION);
		createTable(db, CREATE_lEAGUE);
		createTable(db, CREATE_TEAM);

		createTable(db, CREATE_lEAGUE02);

		createTable(db, CREATE_READ_NEWS);
		createTable(db, CREATE_DICOVERY);
	}

	private void createTable(SQLiteDatabase db, String name) {
		if (!tabIsExist(db, name))
			try {
				db.execSQL(name);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	private void updateLeagueTable(SQLiteDatabase db) {

		try {
			if (!tabIsExist(db, TABLE_LEAGUE02)) {
				db.execSQL(CREATE_lEAGUE02);
			} else {
				Log.d("LeagueTable", "insert");
				db.execSQL("ALTER TABLE " + TABLE_LEAGUE02 + " ADD COLUMN "
						+ KEY_SHOW_STANDINGS_TYPE + " VARCHAR(16)");
			}
			if (!tabIsExist(db, TABLE_LEAGUE02)) {
				db.execSQL(CREATE_DICOVERY);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		switch (newVersion) {
		case 3:
		case 4:
			createTable(db, CREATE_lEAGUE);
			createTable(db, CREATE_TEAM);
			break;
		case 6:
			createTable(db, CREATE_READ_NEWS);
		case 5:
			createTable(db, CREATE_TEAM);
			createTable(db, CREATE_lEAGUE02);
			break;
		case 8:
			createTable(db, CREATE_DICOVERY);
		case 7:
			createTable(db, CREATE_TEAM);
			updateLeagueTable(db);
			createTable(db, CREATE_READ_NEWS);
			break;
		}

	}

	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */
	public boolean tabIsExist(String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */
	public boolean tabIsExist(SQLiteDatabase db, String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}

		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
}
