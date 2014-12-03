package com.hupu.games.asynctask;

import com.hupu.games.db.HuPuDBAdapter;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 查询历史记录
 * **/
public class FollowTask extends AsyncTask<String, Integer, Object> {

	private DBTaskCallback callback;
	/**
	 *0添加, 1 删除，2删除过期, 3 加载所有
	 */
	private int reqType;

   private HuPuDBAdapter mDBAdpater;
	public FollowTask(Context context, int type, DBTaskCallback call) {

		callback = call;
		reqType = type;
		mDBAdpater =new HuPuDBAdapter(context);
	}

	

	@Override
	protected void onPreExecute() {
		// showDialog(Util.DIALOG_WAITING_FOR_DATA);
	}

	@Override
	protected Object doInBackground(String... params) {

		try {
			switch(reqType)
			{
			case 0:
				return mDBAdpater.insertGame(params[0], params[1]);
			case 1:
				return mDBAdpater.delGame(params[0]);
				 
			case 2:
				return mDBAdpater.delOverTimeGames(params[0]);
			case 3:
				return mDBAdpater.getFollowGames();
			
			case 4:
				return mDBAdpater.insertGame(params[0], params[1]);
			case 5:
				return mDBAdpater.delGame(params[0]);
				 
			case 6:
				return mDBAdpater.delOverTimeGames(params[0]);
			case 7:
				return mDBAdpater.getFollowGames();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result instanceof Exception) {
			if (callback != null)
				callback.onDBFailed((Exception) result, reqType);
			return;
		}
		if (callback != null)
			callback.onDBSuccess(result, reqType);
	}
}
