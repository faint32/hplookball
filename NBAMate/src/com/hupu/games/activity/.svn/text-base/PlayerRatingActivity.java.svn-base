/**
 * 
 */
package com.hupu.games.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.adapter.RatingByUserListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.PlayerRatingEntity;
import com.hupu.games.data.PlayersRatingByUserListResp;
import com.hupu.games.data.UserLikeEntity;
import com.hupu.games.data.UserRatingEntity;
import com.hupu.games.view.XListView;
import com.hupu.games.view.XListView.IXListViewListener;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author panyongjun 单球员的所有打分列表页
 */
public class PlayerRatingActivity extends HupuBaseActivity {

	/** 对该球员的打分及评论 */
	XListView mListView;

	/** 球员的打分总数据 */
	PlayerRatingEntity profile;

	/** 球员描述 */
	TextView txtDesc;
	/** 球员名字 */
	TextView txtName;
	/** 打分人数 */
	TextView txtNum;
	/** 分数 */
	TextView txtScore;

	TextView txtMemo;

	TextView btnMyRating;

	ImageView imgHeader;
	
	LinearLayout rateLayout;

	RatingByUserListAdapter mAdapter;

	public int obj_type;// 评分对象类别 1 球员 2 球队 0 其他
	public int obj_id;// 球员id
	public int oid;//评分id
    public int i_pid;
	/**哪个联赛的*/
	public String tag;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		profile = (PlayerRatingEntity) getIntent().getSerializableExtra(
				"profile");

		setContentView(R.layout.layout_player_rating);

		initHeadView();
		mListView = (XListView) findViewById(R.id.list_player);

		mListView.addHeaderView(header);
		mAdapter = new RatingByUserListAdapter(this, click);
		mListView.setAdapter(mAdapter);
		mListView.setPullLoadEnable(false, false);
		mListView.setFreshState();
		mListView.setXListViewListener(new pullListener());
		mListView.setHeaderBackground();
		setOnClickListener(R.id.btn_back);
		// 刷新数据
		
		setOnItemClick(mListView);		
		if(profile!=null)
		{
			obj_id = profile.obj_id;
			oid =profile.oid;
			obj_type = profile.obj_type;
			tag ="nba";
			setProfileData();
		}
		else
		{
			Intent in =getIntent();
			oid=in.getIntExtra("oid",0);
			obj_type=in.getIntExtra("obj_type",0);
			tag =in.getStringExtra("tag");
			obj_id=in.getIntExtra("pid",0);
		}
		reqRatingList(page = 1);
	}

	View header;

	float fontSize;
	float bigFontSize;

	private void initHeadView() {
		header = LayoutInflater.from(this).inflate(
				R.layout.item_player_rating_txt, null);
		header.setOnClickListener(click);
		header.setId(R.layout.item_player_rating_txt);
//		header.setBackgroundResource(R.drawable.list);
		txtDesc = (TextView) header.findViewById(R.id.txt_player_discription);
		txtName = (TextView) header.findViewById(R.id.txt_player_name);
		txtNum = (TextView) header.findViewById(R.id.txt_rating_num);
		txtScore = (TextView) header.findViewById(R.id.txt_player_score);
		imgHeader = (ImageView) header.findViewById(R.id.img_header);
		btnMyRating = (TextView) header.findViewById(R.id.btn_rate);
		rateLayout = (LinearLayout) header.findViewById(R.id.btn_rate_layout);
		btnMyRating.setGravity(Gravity.CENTER);
		txtMemo = (TextView) header.findViewById(R.id.txt_player_memo);
		fontSize = btnMyRating.getTextSize();
		bigFontSize = (float) (fontSize * 1.1);

		btnMyRating.setOnClickListener(click);
		rateLayout.setOnClickListener(click);
	}

	private void setProfileData() {

		txtName.setText(profile.name);
		if (profile.memo.equals(""))
			txtMemo.setText("");
		else
			txtMemo.setText("(" + profile.memo + ")");
		txtDesc.setText(profile.content);
		if (profile.user_num > 0) {
			txtScore.setText(profile.ratings + "分");
			txtNum.setText("/" + profile.user_num + "人评价");
		} else {
			txtScore.setText("");
			txtNum.setText(R.string.no_rating);

		}

		if (profile.my_rating == 0) {
			btnMyRating.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
			btnMyRating.setText(R.string.STR_RATE);
			btnMyRating.setTextColor(0xffffffff);
			btnMyRating.setBackgroundResource(R.drawable.btn_red1_selector);
		} else {
			btnMyRating.setTextSize(TypedValue.COMPLEX_UNIT_PX, bigFontSize);
			btnMyRating.setText("我的评分：" + profile.my_rating);
			btnMyRating.setOnClickListener(null);
			btnMyRating.setTextColor(0xff8b8b8b);
			btnMyRating.setBackgroundDrawable(null);
		}
        if(profile.obj_id>0)
        	obj_id = profile.obj_id;
		if (profile.header_img.equals("-1") || profile.header_img.equals("-3")) {
			// 无头像球员
			imgHeader.setImageResource(R.drawable.no_photo);
		} else if (profile.header_img.equals("-2")) {
			// 无头像裁判
			imgHeader.setImageResource(R.drawable.no_photo);
		} else
			UrlImageViewHelper.setUrlDrawable(imgHeader, profile.header_img,
					R.drawable.no_photo);

	}


	public static int REQ_MY_RATING = 531;

	UserRatingEntity ur;

	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		switch (methodId) {
		case HuPuRes.REQ_METHOD_RATING_DETAIL:
		case HuPuRes.REQ_METHOD_RATING_DETAIL_MORE:
			PlayersRatingByUserListResp data = (PlayersRatingByUserListResp) o;
			if (data.profile != null) {
				profile = data.profile;
				setProfileData();
			}

			if (methodId == HuPuRes.REQ_METHOD_RATING_DETAIL) {
				mAdapter.setData(data.mList);
				if (data.mList == null) {
					findViewById(R.id.txt_nodata).setVisibility(View.VISIBLE);
				}
				page = 1;
			} else {
				mAdapter.appendData(data.mList);
				page++;

			}
			if (data.hasMore)
				mListView.setPullLoadEnable(true, false);
			else
				mListView.setPullLoadEnable(false, false);

			mListView.stopRefresh();
			mListView.stopLoadMore();
			break;
		case HuPuRes.REQ_METHOD_RATING_LIKE:
			UserLikeEntity entity = (UserLikeEntity) o;
			int index = mAdapter.getIndex(entity.coid);
			if (index > -1) {
				mAdapter.getItem(index).like = entity.like;
				mAdapter.notifyDataSetChanged();
			}

			break;
		case HuPuRes.REQ_METHOD_RATING_RATE:
			ur = (UserRatingEntity) o;

			txtScore.setText(ur.ratings);
			txtNum.setText("/" + ur.user_num + "人评价");
			showToast("评分成功");
			reqRatingList(page = 1);
			findViewById(R.id.txt_nodata).setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (ur != null) {
				back();
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onErrResponse(Throwable error, int type) {
		if (type == HuPuRes.REQ_METHOD_RATING_RATE) {
			String content =error.toString();
			if (content != null) {
				try {
					JSONObject obj;
					obj = new JSONObject(content);
					showToast(obj.optString("text", ""));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && requestCode == REQ_MY_RATING) {
			// 需要更新的
			int score = data.getIntExtra("rating", -1);
			String desc = data.getStringExtra("desc");
			// btnMyRating.setText("我的评分:" + score);
			// profile.my_rating = score;
			btnMyRating.setOnClickListener(null);
			reqUserRating(score, desc);
		}
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch (id) {
		case R.id.btn_back:
			back();
			break;
		case R.id.btn_rate_layout:
		case R.id.btn_rate:
			if (profile.my_rating == 0) {
				Intent in = new Intent(this, UserRateActivity.class);
				in.putExtra("name", profile.name);
				in.putExtra("oid", profile.oid);
				startActivityForResult(in, REQ_MY_RATING);
			}
			break;
		case R.layout.item_player_rating_txt:
			if("nba".equals(tag))
			{
				if (obj_type == 1) {
					// qiuyuan
					sendUmeng(HuPuRes.UMENG_EVENT_NBAPLAYERS, HuPuRes.UMENG_KEY_ENTRANCE,HuPuRes.UMENG_VALUE_NBA_PLAYER_REVIEW);
					Intent in = new Intent(this, NBAPlayerInfoActivity.class);
					in.putExtra("pid", obj_id);
					startActivity(in);
				} else if (obj_type == 2) {
					sendUmeng(HuPuRes.UMENG_EVENT_NBA_TEAMS, HuPuRes.UMENG_KEY_ENTRANCE,HuPuRes.UMENG_VALUE_NBA_TEAM_REVIEW);
					// qiudui
					Intent in = new Intent(this, NBATeamActivity.class);
					in.putExtra("tid", obj_id);
					startActivity(in);
				}
			}
			else
			{
				//足球
				if (obj_type == 1) 
				{
					Intent in = new Intent(this, FootballPlayerInfoActivity.class);
					in.putExtra("pid", obj_id);
					in.putExtra("tag", tag);
					startActivity(in);
					
				}else if (obj_type ==3)
				{
					Intent in = new Intent(this, FootballCoachInfoActivity.class);
					in.putExtra("pid", obj_id);
					in.putExtra("tag", tag);
					startActivity(in);
				}
			}
			break;
		}
	}

	@Override
	public void treatClickEvent(View v) {
		int id = v.getId();
		if (id == R.id.btn_like) {
			int index = (Integer) v.getTag();
			reqLike(index);
			if (mAdapter.getItem(index).liked) {
				mAdapter.getItem(index).liked = false;
				mAdapter.getItem(index).like--;
			} else {
				mAdapter.getItem(index).liked = true;
				mAdapter.getItem(index).like++;
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	private void back() {
		if (ur == null) {
			setResult(RESULT_CANCELED);
		} else {
			Intent data = new Intent();
			data.putExtra("entity", ur);
			setResult(RESULT_OK, data);

		}
		finish();
	}

	/**
	 * 发送用户打分数据
	 * */
	private void reqUserRating(int rating, String desc) {

		mParams = initParameter();
		mParams.put("oid", "" + oid);
		mParams.put("type", tag);
		mParams.put("rating", "" + rating);
		mParams.put("desc", desc);
		String tk = SharedPreferencesMgr.getString("tk", null);
		if (tk != null)
			mParams.put("token", tk);
		sendRequest(HuPuRes.REQ_METHOD_RATING_RATE, mParams,
				new HupuHttpHandler(this));
	}

	private int page = 1;

	private void reqRatingList(int p) {
		initParameter();
		mParams.put("type", tag);
		mParams.put("oid", "" + oid);
		mParams.put("page", "" + p);
		// mParams.put("num", "" + 6);
		if (p == 1) {
			sendRequest(HuPuRes.REQ_METHOD_RATING_DETAIL, mParams,
					new HupuHttpHandler(this));
		} else {
			sendRequest(HuPuRes.REQ_METHOD_RATING_DETAIL_MORE, mParams,
					new HupuHttpHandler(this));
		}

	}

	private void reqLike(int index) {
		initParameter();
		mParams.put("type", tag);
		mParams.put("oid", "" + oid);
		mParams.put("coid", "" + mAdapter.getItem(index).coid);
		sendRequest(HuPuRes.REQ_METHOD_RATING_LIKE, mParams,
				new HupuHttpHandler(this));

	}

	/** 设置listview 上拉和下拉的监听 */
	class pullListener implements IXListViewListener {

		@Override
		public void onRefresh() {
			reqRatingList(page = 1);
			findViewById(R.id.txt_nodata).setVisibility(View.GONE);
		}

		@Override
		public void onLoadMore() {

			reqRatingList(page + 1);
		}

	}

}
