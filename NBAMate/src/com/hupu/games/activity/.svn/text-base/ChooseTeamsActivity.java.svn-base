package com.hupu.games.activity;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hupu.games.R;
import com.hupu.games.activity.HupuLaunchActivity.LeagusAdapter.League;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.FollowResultEntity;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.data.TeamsEntity;
import com.hupu.games.db.HuPuDBAdapter;
import com.hupu.http.HupuHttpHandler;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ChooseTeamsActivity extends HupuBaseActivity
{

    private ExpandableListView expandableListView;
    private ExpandableAdapter viewAdapter;
    private LinkedList<LeaguesEntity> leaguesEntities;
    HupuLaunchActivity hupuLeague;
    Intent intent;
    HuPuDBAdapter mDBAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_follow_teams_init);
        expandableListView = (ExpandableListView) findViewById(R.id.list_team);
        leaguesEntities = new LinkedList<LeaguesEntity>();

        // 获取联赛 集合信息
        /*for (LeaguesEntity league : mApp.loadLeagues()) {
			if (league.template.equals("nba") || league.template.equals("cba") || league.template.equals("soccerleagues") ||league.template.equals("soccercupleagues")) {
				leaguesEntities.add(league);
			}
		}*/
        leaguesEntities = mApp.loadLeagues();

        viewAdapter = new ExpandableAdapter(LayoutInflater.from(this),
                                            leaguesEntities);
        expandableListView.setAdapter(viewAdapter);
        expandableListView.expandGroup(0);
        expandableListView.setOnChildClickListener(childClickListener);
        expandableListView.setOnGroupClickListener(onGroupClickListener);
        expandableListView.setGroupIndicator(null);

        setOnClickListener(R.id.btn_done);
        setOnClickListener(R.id.btn_previous);

    }

    

    int expandFlag = 0;
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
                int groupPosition, int childPosition, long id)
        {
            // TODO Auto-generated method stub
            
            if (leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow == 1)
            {
                ((com.hupu.games.activity.ChooseTeamsActivity.ExpandableAdapter.Child) view.getTag()).chooseBox.setBackgroundResource(R.drawable.btn_menu_choose_up);
                leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow = 0;
            }
            else
            {
                ((com.hupu.games.activity.ChooseTeamsActivity.ExpandableAdapter.Child) view.getTag()).chooseBox.setBackgroundResource(R.drawable.choose_btn_down);
                leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow = 1;
                leaguesEntities.get
                (groupPosition).is_follow = 1;

            }
            
            int leagueIndex = 0, teamIndex = 0;
			for (LeaguesEntity league : leaguesEntities) {
				teamIndex = 0;
				boolean isFollowTeam = false;
				for (TeamsEntity team : league.mList) {
					if (team.is_follow == 1) {
						isFollowTeam = true;
					}
					if (team.name.equals(leaguesEntities.get(groupPosition).mList.get(childPosition).name) && league.game_type.equals(leaguesEntities.get(groupPosition).game_type)) {
					//if (team.name.equals(leaguesEntities.get(groupPosition).mList.get(childPosition).name) ) {	
					leaguesEntities.get(leagueIndex).mList.get(teamIndex).is_follow = leaguesEntities
								.get(groupPosition).mList.get(childPosition).is_follow;
					}
					teamIndex++;
				}
				if (leaguesEntities.get(leagueIndex).is_follow == 0) {
					leaguesEntities.get(leagueIndex).is_follow = isFollowTeam?1:0;
				}
				leagueIndex++;
			}
            viewAdapter.notifyDataSetChanged();
            return false;
        }
    };


    @Override
    public void treatClickEvent(int id)
    {
        // TODO Auto-generated method stub
        switch (id)
        {
            case R.id.btn_done:
                
                int followNum = 0;
                for (LeaguesEntity league : leaguesEntities)
                {
                    if (league.is_follow == 1)
                        followNum ++;
                }
                
                if (followNum == 0)
                {
                    for (int i = 0; i < leaguesEntities.size(); i++)
                    {
                        leaguesEntities.get(i).is_follow = 1;
                    }
                }
                // 插入数据
                mApp.insertLeaguesFrist(leaguesEntities);
                
                mApp.updateTeams(leaguesEntities);
                // 拉取 带顺序的数据
                leaguesEntities = mApp.loadLeagues();
                
                mApp.followLeague(leaguesEntities);
                mApp.followOnlyTeams(leaguesEntities);

                intent = new Intent(this, HupuHomeActivity.class);
                intent.putExtra("byIcon", true);// 标识用户是点击进入的。
                intent.putExtra("load", true);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_previous:               
                intent = new Intent(this, HupuLaunchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        super.treatClickEvent(id);

    }

    class ExpandableAdapter extends BaseExpandableListAdapter
    {
        // private Context context;
        LinkedList<LeaguesEntity> leagueListEntities;
        LayoutInflater layoutInflater;

        /*
         * 构造函数: 参数1:context对象 参数2:一级列表数据源 参数3:二级列表数据源
         */
        public ExpandableAdapter(LayoutInflater layoutInflater,
                LinkedList<LeaguesEntity> groups)
        {
            this.leagueListEntities = groups;
            this.layoutInflater = layoutInflater;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            if (leagueListEntities.get(groupPosition).mList == null)
            {
                return "";
            }
            else
            {
                return leagueListEntities.get(groupPosition).mList.get(childPosition).name;
            }
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        // 获取二级列表的View对象
        @Override
        public View getChildView(final int groupPosition,
                final int childPosition, boolean isLastChild, View convertView,
                ViewGroup parent)
        {
            @SuppressWarnings("unchecked")
            String text = (String) getChild(groupPosition, childPosition);
            // LayoutInflater layoutInflater = (LayoutInflater)
            // context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 获取二级列表对应的布局文件, 并将其各元素设置相应的属性
            Child child;
            if (convertView == null)
            {
                convertView = (RelativeLayout) layoutInflater.inflate(
                        R.layout.choose_teams_child, null);
                child = new Child();
                child.teamLogo = (ImageView) convertView.findViewById(R.id.team_logo);
                child.teamName = (TextView) convertView.findViewById(R.id.team_name);
                child.chooseBox = (ImageView) convertView.findViewById(R.id.child_checkbox);

                convertView.setTag(child);
            }
            else
            {
                child = (Child) convertView.getTag();
            }

            child.teamName.setText(text);
    		UrlImageViewHelper.setUrlDrawable(child.teamLogo,leagueListEntities.get(groupPosition).mList.get(childPosition).logo);

            if (leaguesEntities.get(groupPosition).mList.get(childPosition).is_follow == 1)
            {
                child.chooseBox.setBackgroundResource(R.drawable.btn_menu_choose_down);
            }
            else
            {
                child.chooseBox.setBackgroundResource(R.drawable.btn_menu_choose_up);
            }
            // child.chooseBox.setBackgroundResource(R.drawable.choose_btn);
            /*
             * convertView.setOnClickListener(new OnClickListener() {
             * 
             * @Override
             * public void onClick(View v)
             * {
             * // TODO Auto-generated method stub
             * 
             * Log.w("ccc", "xxxxx");
             * 
             * }
             * });
             */

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            if (leagueListEntities.get(groupPosition).mList == null)
            {
                return 0;
            }
            else
            {
                return leagueListEntities.get(groupPosition).mList.size();
            }
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            return leagueListEntities.get(groupPosition).name;
        }

        @Override
        public int getGroupCount()
        {
            return leagueListEntities.size();
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        // 获取一级列表View对象
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent)
        {
            String text = leagueListEntities.get(groupPosition).name;

            Group group;

            if (convertView == null)
            {
                convertView = (RelativeLayout) layoutInflater.inflate(
                        R.layout.choose_teams_group, null);
                group = new Group();
                group.leagueLayout = ((RelativeLayout) convertView.findViewById(R.id.league_layout));
                group.leagueLogo = ((ImageView) convertView.findViewById(R.id.leagu_logo));
                group.leagueName = (TextView) convertView.findViewById(R.id.league_name);
                group.chooseNum = (TextView) convertView.findViewById(R.id.follow_team_num);
                group.groupDown = ((ImageView) convertView.findViewById(R.id.group_img));
                convertView.setTag(group);
            }
            else
            {
                group = (Group) convertView.getTag();
            }
            group.leagueName.setText(text);
            UrlImageViewHelper.setUrlDrawable(group.leagueLogo,
                    leagueListEntities.get(groupPosition).logo);            
            int followNum = 0;
            for (TeamsEntity team: leagueListEntities.get(groupPosition).mList)
            {
                followNum = team.is_follow == 1 ? followNum + 1:followNum;
            }
            if (followNum >0)
            {
                group.chooseNum.setText("已关注 "+followNum+" 支");
            }else {
                group.chooseNum.setText("");
            }
            followNum = 0;

            if (isExpanded)
            {
                group.groupDown.setImageResource(R.drawable.group_up);
            }
            else
            {
                group.groupDown.setImageResource(R.drawable.group_down);
            }
            
            
            if (leagueListEntities.get(groupPosition).template.equals("nba") || leagueListEntities.get(groupPosition).template.equals("cba") || leagueListEntities.get(groupPosition).template.equals("soccerleagues") ||leagueListEntities.get(groupPosition).template.equals("soccercupleagues")) {
            	convertView.setVisibility(View.VISIBLE);
            	group.leagueLayout.setVisibility(View.VISIBLE);
            }else {
            	convertView.setVisibility(View.GONE);
            	group.leagueLayout.setVisibility(View.GONE);
			}
            return convertView;
        }

        @Override
        public boolean hasStableIds()
        {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            // Log.d("isChildSelectable",
            // "groupPosition="+groupPosition+"  ;childPosition="+childPosition);
            return true;
        }

        private class Group
        {
        	RelativeLayout leagueLayout;
            TextView leagueName;
            ImageView leagueLogo;
            TextView chooseNum;
            ImageView groupDown;
        }

        private class Child
        {
            TextView teamName;
            ImageView teamLogo;
            ImageView chooseBox;
        }

    }
}
