package com.hupu.games.activity;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.adapter.TeamsListAdapter;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.FollowResultEntity;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.TeamsEntity;
import com.hupu.games.db.HuPuDBAdapter;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class FollowTeamActivity extends HupuBaseActivity {

	private ExpandableListView expandableListView;
	private ExpandableAdapter viewAdapter;
	private ListView mLvTeams;
	private TeamsListAdapter mAdapterTeams;
	boolean bFromSetup;
	public static LinkedList<LeaguesEntity> leaguesEntities;
	private int pos = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_follow_team);
		init();
	}

	private void init() {
		leaguesEntities = new LinkedList<LeaguesEntity>();

		HuPuDBAdapter mDBAdapter = new HuPuDBAdapter(this);

		for (LeaguesEntity league : mDBAdapter.getAllLeagues()) {
			if (league.template.equals("nba") || league.template.equals("cba")
					|| league.template.equals("soccerleagues")
					|| league.template.equals("soccercupleagues")) {
				leaguesEntities.add(league);
			}
		}
		// leaguesEntities = mDBAdapter.getFollowLeagu();

		for (int i = 0; i < leaguesEntities.size(); i++) {
			leaguesEntities.get(i).mList = mDBAdapter
					.getTeamsByLid(leaguesEntities.get(i).lid);
		}

		expandableListView = (ExpandableListView) findViewById(R.id.list_team);
		viewAdapter = new ExpandableAdapter(LayoutInflater.from(this),
				leaguesEntities);
		expandableListView.setAdapter(viewAdapter);
		expandableListView.setOnChildClickListener(childClickListener);
		expandableListView.setOnGroupClickListener(onGroupClickListener);
		expandableListView.setGroupIndicator(null);

		setOnClickListener(R.id.btn_setup);
	}

	int expandFlag = -1;
	private OnGroupClickListener onGroupClickListener = new OnGroupClickListener() {

		@Override
		public boolean onGroupClick(ExpandableListView listView, View v,
				int groupPosition, long id) {

			if (expandFlag == -1) {
				// 展开被选的group
				listView.expandGroup(groupPosition);
				// 设置被选中的group置于顶端
				listView.setSelectedGroup(groupPosition);
				expandFlag = groupPosition;

			} else if (expandFlag == groupPosition) {
				listView.collapseGroup(expandFlag);
				expandFlag = -1;
			} else {
				listView.collapseGroup(expandFlag);
				// 展开被选的group
				listView.expandGroup(groupPosition);
				// 设置被选中的group置于顶端
				listView.setSelectedGroup(groupPosition);
				expandFlag = groupPosition;
			}

			return true;
		}
	};

	private OnChildClickListener childClickListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View view,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub
			if (leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow == 1) {
				((com.hupu.games.activity.FollowTeamActivity.ExpandableAdapter.Child) view
						.getTag()).chooseBox
						.setBackgroundResource(R.drawable.btn_menu_choose_up);
				leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow = 0;

			} else {
				((com.hupu.games.activity.FollowTeamActivity.ExpandableAdapter.Child) view
						.getTag()).chooseBox
						.setBackgroundResource(R.drawable.choose_btn_down);
				leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow = 1;

			}

			int leagueIndex = 0, teamIndex = 0;
			for (LeaguesEntity league : leaguesEntities) {
				teamIndex = 0;
				for (TeamsEntity team : league.mList) {
					 if
					 (team.name.equals(leaguesEntities.get(groupPosition).mList.get(childPosition).name)
					 &&
					 league.game_type.equals(leaguesEntities.get(groupPosition).game_type))
					 {
//					if (team.name
//							.equals(leaguesEntities.get(groupPosition).mList
//									.get(childPosition).name)) {
						leaguesEntities.get(leagueIndex).mList.get(teamIndex).is_follow = leaguesEntities
								.get(groupPosition).mList.get(childPosition).is_follow;
					}
					teamIndex++;
				}
				leagueIndex++;
			}

			viewAdapter.notifyDataSetChanged();
			return false;
		}
	};

	@Override
	public void onReqResponse(Object o, int methodId) {
		// TODO Auto-generated method stub
		super.onReqResponse(o, methodId);

		if (methodId == HuPuRes.REQ_METHOD_FOLLOW_ALL_TEAM) {
			FollowResultEntity result = (FollowResultEntity) o;
			if (result.result != 1) {
				SharedPreferencesMgr.setBoolean("follow_result", false);
			}
		}
		if (methodId == HuPuRes.REQ_METHOD_FOLLOW_LEAGUE) {
			FollowResultEntity result = (FollowResultEntity) o;
			if (result.result != 1) {
				SharedPreferencesMgr.setBoolean("follow_result", false);
			}
		}
	}

	public void Setup() {
		// 插入数据
		mApp.updateTeams(leaguesEntities);
		// 拉取 带顺序的数据
		// leaguesEntities = mApp.loadLeagues();

		mApp.followOnlyTeams(leaguesEntities);

		finish();
	}

	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_setup:
			// end();
			Setup();
			break;
		}
		super.treatClickEvent(id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			// end();
			Setup();
		}
		return false;
	}

	private void end() {
		mApp.followTeams(mAdapterTeams.getSelectList(), this);
		Intent in = new Intent();
		in.putExtra("follow",
				HuPuApp.getFollowTeamsNames(mAdapterTeams.getSelectList()));
		setResult(RESULT_OK, in);
		finish();
	}

	@Override
	public void treatItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
		mAdapterTeams.clickItem(pos);
	}

	private final int DIALOG_NOTIFY = 1136;

	@Override
	public void clickNegativeButton(int dialogId) {
		super.clickNegativeButton(dialogId);
		if (dialogId == DIALOG_NOTIFY) {
			if (mDialog != null)
				mDialog.cancel();
		}
	}

	@Override
	public void clickPositiveButton(int dialogId) {
		super.clickPositiveButton(dialogId);
		if (dialogId == DIALOG_NOTIFY) {
			// 打开通知
			if (mDialog != null)
				mDialog.cancel();
			mApp.setNotify(true);
			mAdapterTeams.clickItem();
		}
	}


	class ExpandableAdapter extends BaseExpandableListAdapter {
		// private Context context;
		LinkedList<LeaguesEntity> leagueListEntities;
		LayoutInflater layoutInflater;

		/*
		 * 构造函数: 参数1:context对象 参数2:一级列表数据源 参数3:二级列表数据源
		 */
		public ExpandableAdapter(LayoutInflater layoutInflater,
				LinkedList<LeaguesEntity> groups) {
			this.leagueListEntities = groups;
			this.layoutInflater = layoutInflater;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if (leagueListEntities.get(groupPosition).mList == null) {
				return "";
			} else {
				return leagueListEntities.get(groupPosition).mList
						.get(childPosition).name;
			}
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 获取二级列表的View对象
		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			@SuppressWarnings("unchecked")
			String text = (String) getChild(groupPosition, childPosition);
			// LayoutInflater layoutInflater = (LayoutInflater)
			// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 获取二级列表对应的布局文件, 并将其各元素设置相应的属性
			Child child;
			if (convertView == null) {
				convertView = (RelativeLayout) layoutInflater.inflate(
						R.layout.choose_teams_child, null);
				child = new Child();
				child.teamLogo = (ImageView) convertView
						.findViewById(R.id.team_logo);
				child.teamName = (TextView) convertView
						.findViewById(R.id.team_name);
				child.chooseBox = (ImageView) convertView
						.findViewById(R.id.child_checkbox);

				convertView.setTag(child);
			} else {
				child = (Child) convertView.getTag();
			}

			child.teamName.setText(text);

			UrlImageViewHelper.setUrlDrawable(child.teamLogo,
					leagueListEntities.get(groupPosition).mList
							.get(childPosition).logo, R.drawable.bg_home_nologo);
			if (leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow == 1) {
				child.chooseBox
						.setBackgroundResource(R.drawable.btn_menu_choose_down);
			} else {
				child.chooseBox.setBackgroundResource(R.drawable.btn_menu_choose_up);
			}

			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (leagueListEntities.get(groupPosition).mList == null) {
				return 0;
			} else {
				return leagueListEntities.get(groupPosition).mList.size();
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			return leagueListEntities.get(groupPosition).name;
		}

		@Override
		public int getGroupCount() {
			return leagueListEntities.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		// 获取一级列表View对象
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String text = leagueListEntities.get(groupPosition).name;

			Group group;

			if (convertView == null) {
				convertView = (RelativeLayout) layoutInflater.inflate(
						R.layout.choose_teams_group, null);
				group = new Group();
				group.leagueLogo = ((ImageView) convertView
						.findViewById(R.id.leagu_logo));
				group.leagueName = (TextView) convertView
						.findViewById(R.id.league_name);
				group.chooseNum = (TextView) convertView
						.findViewById(R.id.follow_team_num);
				group.groupDown = ((ImageView) convertView
						.findViewById(R.id.group_img));
				convertView.setTag(group);
			} else {
				group = (Group) convertView.getTag();
			}
			group.leagueName.setText(text);

			UrlImageViewHelper.setUrlDrawable(group.leagueLogo,
					leagueListEntities.get(groupPosition).logo, R.drawable.bg_home_nologo);
			int followNum = 0;
			for (TeamsEntity team : leagueListEntities.get(groupPosition).mList) {
				followNum = team.is_follow == 1 ? followNum + 1 : followNum;
			}
			if (followNum > 0) {
				group.chooseNum.setText("已关注 " + followNum + " 支");
			} else {
				group.chooseNum.setText("");
			}
			followNum = 0;

			if (isExpanded) {
				group.groupDown.setImageResource(R.drawable.group_up);
			} else {
				group.groupDown.setImageResource(R.drawable.group_down);
			}
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// Log.d("isChildSelectable",
			// "groupPosition="+groupPosition+"  ;childPosition="+childPosition);
			return true;
		}

		private class Group {
			TextView leagueName;
			ImageView leagueLogo;
			TextView chooseNum;
			ImageView groupDown;
		}

		private class Child {
			TextView teamName;
			ImageView teamLogo;
			ImageView chooseBox;
		}
	}
}
