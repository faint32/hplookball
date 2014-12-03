package com.hupu.games.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.NBAPlayerInfoActivity.PlayerListViewTouchLinstener;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.basketball.NbaPlayerInfoReq;
import com.hupu.games.data.game.basketball.NbaPlayerInfoReq.NbaPlayerInfoEntity;
import com.hupu.games.view.HScrollView;
import com.hupu.games.view.HScrollView.ScrollViewObserver1;
import com.hupu.games.view.HupuSectionedBaseAdapter;

public class PlayerInfoListAdapter extends HupuSectionedBaseAdapter {
	private LayoutInflater mInflater;

	OnClickListener mClick;
	
	NbaPlayerInfoReq infoList;
	int txtWidth;
	int txtHeight;
	
	HScrollView headSrcrollView;
	Context mContext;
	PlayerListViewTouchLinstener mLinstener;
	public PlayerInfoListAdapter(Context context,PlayerListViewTouchLinstener linstener) {
		//mClick = click;
		mLinstener = linstener;
		mContext = context;
		mInflater = LayoutInflater.from(context);
		
		txtWidth=context.getResources().getDimensionPixelSize(
				R.dimen.txt_player);
		txtHeight =context.getResources().getDimensionPixelSize(
				R.dimen.txt_player_height);
		
		ob1 =new ScrollViewObserver1();
	}
	public void setData(NbaPlayerInfoReq info) {
		if (info == null) {
			infoList = null;
		} else {
			infoList = info;
		}
	}
	
	public NbaPlayerInfoEntity getItemAt(int pos) {
		if (infoList != null) {
			int section = getSectionForPosition(pos);
			int child = getPositionInSectionForPosition(pos);
			
			return getItem(section, child);

		}
		return null;
	}
	
    @Override
    public NbaPlayerInfoEntity getItem(int section, int position) {
        // TODO Auto-generated method stub
    	if (section == -1 || position == -1)
			return null;
    	if (infoList !=null) {
    		if (section == 0) {
    			return infoList.regularStats.get(position);
			}else {
				return infoList.playoffStats.get(position);
			}
		}
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSectionCount() {
    	if (infoList !=null) 
    		return infoList.pos;
    	else 
    		return 0;
		
		
    }

    @Override
    public int getCountForSection(int section) {
    	if (section == 0) {
			return infoList.regularStats.size();
		}else {
			return infoList.playoffStats.size();
		}
    }
    public ScrollViewObserver1 ob1;
    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
    	
    	Holder item = null;
    	HScrollView scrollView = null;
    	if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.item_nba_player_info, null);
			item = new Holder();
			item.txtData = new TextView[infoList.regularValues.length-2];
			LinearLayout container = (LinearLayout) convertView
					.findViewById(R.id.layout_containter);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(txtWidth, txtHeight);
			for (int i = 0; i < item.txtData.length; i++) {
				item.txtData[i] = buildTextView();
				container.addView(item.txtData[i],params);
			}
			item.txtDateName=(TextView)convertView.findViewById(R.id.txt_date_name);			
			item.txtTeamName=(TextView)convertView.findViewById(R.id.txt_team_name);			
			scrollView =(HScrollView)convertView.findViewById(R.id.hscrollview);	
			item.hScrollView = scrollView;
			if(section == 0&&position==0){
				mLinstener.setScrollView(scrollView);
			}
			convertView.setTag(item);
    	}else {
			item = (Holder) convertView.getTag();
		}
    	//HupuLog.e("papa", "个数=="+ob1.getListSize());
    	
    	item.hScrollView.setNoHeader(ob1);
    	if (section == 0 ) {
    		for (int i = 0; i < item.txtData.length; i++) {
    			item.txtData[i].setText(infoList.regularStats.get(position).values[i+2]);
    			item.txtDateName.setText(infoList.regularStats.get(position).values[0]);
    			item.txtTeamName.setText(infoList.regularStats.get(position).values[1]);
    		}
		}else {
			for (int i = 0; i < item.txtData.length; i++) {
    			item.txtData[i].setText(infoList.playoffStats.get(position).values[i+2]);
    			item.txtDateName.setText(infoList.playoffStats.get(position).values[0]);
    			item.txtTeamName.setText(infoList.playoffStats.get(position).values[1]);
    		}
		}
    	
    	if(position%2==0)
    		convertView.setBackgroundResource(R.drawable.bg_player_data_selector1);
		else
			convertView.setBackgroundResource(R.drawable.bg_player_data_selector2);
//    	convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				infoList.playoffKeys[1] = "1";
//				Intent teamAct = new Intent(mContext, PlayerAndTeamDataActivity.class);
//				if (section == 0 ) {
//					teamAct.putExtra("tid", infoList.regularStats.get(position).t_id);
//				}else {
//					teamAct.putExtra("tid", infoList.playoffStats.get(position).t_id);
//				}
//				mContext.startActivity(teamAct);
//			}
//		});
    	return convertView;

    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
    	Holder item = null;
    	HScrollView scrollView = null;
    	if (convertView == null) {
    		convertView = mInflater.inflate(R.layout.item_player_info_list_header, null);
			item = new Holder();
			item.txtData = new TextView[infoList.regularValues.length-2];
			LinearLayout container = (LinearLayout) convertView
					.findViewById(R.id.layout_containter);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(txtWidth, txtHeight);
			for (int i = 0; i < item.txtData.length; i++) {
				item.txtData[i] = buildTextView();
				container.addView(item.txtData[i],params);
			}
			item.txtDateName=(TextView)convertView.findViewById(R.id.txt_date_name);			
			item.txtTeamName=(TextView)convertView.findViewById(R.id.txt_team_name);	
			
			scrollView =(HScrollView)convertView.findViewById(R.id.hscrollview);
			item.hScrollView = scrollView;
			convertView.setTag(item);
    	}else {
			item = (Holder) convertView.getTag();
		}
    	
    	item.hScrollView.setNoHeader(ob1);
    	if (section == 0) {
			
    		item.txtDateName.setText(infoList.regularValues[0]);
    		item.txtTeamName.setText(infoList.regularValues[1]);
		}else {
			item.txtDateName.setText(infoList.playoffValues[0]);
    		item.txtTeamName.setText(infoList.playoffValues[1]);
		}
    	for (int i = 0; i < item.txtData.length; i++) {
			item.txtData[i].setText(infoList.regularValues[i+2]);
		}
    	
    	

    	return convertView;
    	
    }
    
    private TextView buildTextView() {
		TextView tv =(TextView)mInflater.inflate(R.layout.txt_player_data, null);		
		return tv;
	}
    class Holder {
		/** 数据 */
		TextView[] txtData;
		/** 球员名字 */
		TextView txtDateName;
		TextView txtTeamName;
		
		HScrollView hScrollView;
	}

}
