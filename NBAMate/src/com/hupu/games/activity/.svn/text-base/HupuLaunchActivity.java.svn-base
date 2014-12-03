package com.hupu.games.activity;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.InitResp;
import com.hupu.games.data.LeaguesEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;
import com.hupu.games.activity.HupuLaunchActivity.LeagusAdapter.League;
/**
 * 初次登录时球队的选择页面
 * */
public class HupuLaunchActivity extends HupuBaseActivity {



	private ListView mLvTeams;
	//private TeamsListAdapter mAdapterTeams;
	
	private InitResp leagueListEntity;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_follow_leagues_init);
		init();
	
	}

	private void init() {
	    leagueListEntity = new InitResp();
	    leagueListEntity.mList = new LinkedList<LeaguesEntity>();
	    
	    leagueListEntity.mList = mApp.loadLeagues();
	    
		mLvTeams = (ListView) findViewById(R.id.list_team);
		//mLvTeams.setAdapter(mAdapterTeams);
		mListAdapter = new LeagusAdapter(this);
		setOnClickListener(R.id.btn_done);
		//setOnItemClick(mLvTeams);
		
		mLvTeams.setAdapter(mListAdapter);
        mLvTeams.setOnItemClickListener(itemClickListener);
        
        
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int pos,
                long arg3)
        {
            // TODO Auto-generated method stub
            if (leagueListEntity.mList.get(pos).is_follow == 0)
            {
                leagueListEntity.mList.get(pos).is_follow = 1;
                ((League)view.getTag()).chooseImg.setImageResource(R.drawable.choose_btn_down);
            }else {
                leagueListEntity.mList.get(pos).is_follow = 0;
                ((League)view.getTag()).chooseImg.setImageResource(R.drawable.img_choose_selector);
            }
        }
    };
	
	/*public LeagueListEntity getLeagueListEntity() {
	    if (leagueListEntity.mList !=null)
        {
	        return leagueListEntity;
        }else {
            return null;
        }
	}*/

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			quit();
		}
		return false;
	}
	
	private void startHome()
	{
		Intent intent = new Intent(this, HupuHomeActivity.class);
		intent.putExtra("load", true);
		startActivity(intent);
		finish();
	}
	
	private void toNext() {
	   
	    //插入联赛数据到数据库
	    mApp.insertLeaguesFrist(leagueListEntity.mList);
	    Intent intent = new Intent(this, ChooseTeamsActivity.class);
        startActivity(intent);
        finish();
	}
	@Override
	public void treatClickEvent(int id) {
		switch (id) {
		case R.id.btn_done:
		    toNext();
			/*mApp.followTeams (mAdapterTeams.getSelectList(),this);
			// 传递关注消息
			startHome();
			MobclickAgent.onEvent(this,HuPuRes.UMENG_KEY_GUIDE_DONE) ;		*/
			break;

		}
		super.treatClickEvent(id);
	}


	
	LeagusAdapter mListAdapter;

    class LeagusAdapter extends BaseListAdapter<LeaguesEntity>
    {

        public LeagusAdapter(Context context)
        {
            super(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return leagueListEntity.mList.size();
        }
        @Override
        public View getView(int pos, View content, ViewGroup arg2)
        {
            LeaguesEntity entity = leagueListEntity.mList.get(pos);
            
            League league = null;
            if (content == null)
            {
                league = new League();
                content = initLeague(league);
            }
            else
            {
                league = (League) content.getTag();
            }
            
            //league.logo = entity.logo;
            league.name.setText(entity.name);
            //finaBitmap.configLoadingImage(R.drawable.nba_logo);
        
            UrlImageViewHelper.setUrlDrawable(league.logo,entity.logo);
            if (entity.is_follow == 1)
            {
                league.chooseImg.setImageResource(R.drawable.choose_btn_down);
            }else {
                league.chooseImg.setImageResource(R.drawable.img_choose_selector);
            }
            if (entity.template.equals("nba") || entity.template.equals("cba") || entity.template.equals("soccerleagues") ||entity.template.equals("soccercupleagues")) {
            	content.setVisibility(View.VISIBLE);
            	league.leagueLayout.setVisibility(View.VISIBLE);
            }else {
				content.setVisibility(View.GONE);
				league.leagueLayout.setVisibility(View.GONE);
				HupuLog.e("papa", "---发现-----"+entity.name);
			}
            return content;
        }

        private View initLeague(League league)
        {
            View content = mInflater.inflate(R.layout.item_league, null,
                    false);
            league.leagueLayout = (LinearLayout) content.findViewById(R.id.league_layout);
            league.logo = (ImageView) content.findViewById(R.id.leagu_logo);
            league.name = (TextView) content.findViewById(R.id.txt_name);
            league.chooseImg = (ImageView) content.findViewById(R.id.choose_box);
            content.setTag(league);
            return content;
        }
        class League
        {
        	LinearLayout leagueLayout;
            ImageView logo;
            TextView name;
            ImageView chooseImg;
        }

    }
	
}
