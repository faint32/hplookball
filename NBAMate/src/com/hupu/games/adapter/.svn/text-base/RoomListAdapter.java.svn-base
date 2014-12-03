package com.hupu.games.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.PrizeEntity;
import com.hupu.games.data.room.RoomEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.pyj.adapter.BaseListAdapter;
import com.tencent.a.b.r;

/**
 * 房间列表适配器
 * 
 * @author papa
 * 
 */
public class RoomListAdapter extends BaseListAdapter<RoomEntity> {

	OnClickListener mClick;
	int default_id = -1;

	public RoomListAdapter(Context context, OnClickListener click) {
		super(context);
		mClick = click;

	}

	@Override
	public void setData(ArrayList<RoomEntity> data) {
		// TODO Auto-generated method stub
		super.setData(data);
		if (data != null) {
			mListData = data;
		}
	}
	
	public void setDefaultId(int id){
		default_id = id;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mListData != null)
			return mListData.size();
		return 0;
	}

	@Override
	public RoomEntity getItem(int position) {
		// TODO Auto-generated method stub
		return mListData.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Room room = null;
		RoomEntity entity = mListData.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_room, null);
			room = new Room();
			room.roomName = (TextView) convertView.findViewById(R.id.room_name);
			room.roomAnchorTitle = (TextView) convertView.findViewById(R.id.anchor_title);
			room.roomAnchor = (TextView) convertView.findViewById(R.id.anchor_name);
			room.roomCount = (TextView) convertView.findViewById(R.id.room_count);
			convertView.setTag(room);
		} else {
			room = (Room) convertView.getTag();
		}

		room.roomAnchor.setText(entity.anchor_list);
		room.roomName.setText(entity.name);
		room.roomCount.setText(entity.count+"人");
		if (default_id == entity.id) 
			convertView.setBackgroundResource(R.drawable.bg_room_list_down);
		else 
			convertView.setBackgroundResource(R.drawable.bg_room_item_selector);
		
		if(entity.id == 0){
			room.roomAnchorTitle.setVisibility(View.INVISIBLE);
		}else{
			room.roomAnchorTitle.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	class Room {
		TextView roomName;
		TextView roomAnchorTitle;
		TextView roomAnchor;
		TextView roomCount;
	}
}
