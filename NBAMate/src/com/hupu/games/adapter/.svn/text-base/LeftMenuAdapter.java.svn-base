package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.data.LeaguesEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;

public class LeftMenuAdapter extends BaseListAdapter<LeaguesEntity> {

	LayoutInflater inflater;
	LinkedList<LeaguesEntity> leagues;
	boolean isMgr = false;
	boolean lookMore = false;
	int position = 0;
	Context context;


//	private int height;
	
	public LeftMenuAdapter(Context context) {
		super(context);
		this.context = context;
	}

	public void setData(LinkedList<LeaguesEntity> list) {
	    leagues = new LinkedList<LeaguesEntity>();
	    if (lookMore)
        {
	        leagues = list;
            
        }else {
            for (LeaguesEntity league:list)
            {
                if (league.is_follow == 1)
                {
                    leagues.add(league);
                }
            }
        }
	}
	
	public void setStyle(boolean isMgr,boolean lookMore) {
	    this.isMgr = isMgr;
	    this.lookMore = lookMore;
	}

	public void setClickItemPos(int pos) {
	    this.position = pos;
	}
	
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return leagues.size();
    }


    @Override
    public LeaguesEntity getItem(int position)
    {
        // TODO Auto-generated method stub
        return leagues.get(position);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        
        League league = null;
        
		if (convertView == null)
        {
		    league = new League();
		    convertView = initLeague(league);
        }else {
            league = (League) convertView.getTag();
        }
		convertView.setBackgroundResource(position == this.position ? R.drawable.leagu_item_bg_down : R.drawable.item_league_selector);
		
		league.name.setText(leagues.get(position).name);
		

        UrlImageViewHelper.setUrlDrawable(league.logo,leagues.get(position).logo,R.drawable.bg_home_nologo);
		if (isMgr)
        {
		    league.drag.setVisibility(View.VISIBLE);
		    league.drag.setEnabled(true);
		    league.chooseImg.setVisibility(View.VISIBLE);
		    
        }else {
            league.drag.setVisibility(View.GONE);
            league.drag.setEnabled(false);
            league.chooseImg.setVisibility(View.GONE);
        }
		
		league.newLeague.setVisibility(leagues.get(position).is_new == 1 ? View.VISIBLE:View.GONE);
		
		if (leagues.get(position).is_follow == 1)
        {
            league.chooseImg.setImageResource(R.drawable.btn_menu_choose_down);
        }else {
            league.chooseImg.setImageResource(R.drawable.btn_menu_choose_up);
        }
		
		return convertView;
	}
    private View initLeague(League league)
    {
        View content = mInflater.inflate(R.layout.item_menu, null,
                false);
        league.drag = (ImageView) content.findViewById(R.id.drag_img);
        league.logo = (ImageView) content.findViewById(R.id.leagu_logo);
        league.name = (TextView) content.findViewById(R.id.txt_name);
        league.newLeague = (ImageView) content.findViewById(R.id.new_league);
        league.chooseImg = (ImageView) content.findViewById(R.id.choose_box);
        content.setTag(league);
        return content;
    }

    class League
    {
        ImageView drag;
        ImageView logo;
        TextView name;
        ImageView newLeague;
        ImageView chooseImg;
    }
}
