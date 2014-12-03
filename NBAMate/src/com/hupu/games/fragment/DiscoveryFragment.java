/**
 * 
 */
package com.hupu.games.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.DiscoveryWebViewActivity;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.DiscoveryEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * @author panyongjun 发现页面
 */
public class DiscoveryFragment extends BaseFragment {

	ExpandableListView mListView;

	ArrayList<ArrayList<DiscoveryEntity>> mDiscoverList;
	ArrayList<String> myRedPointList = null;
	LayoutInflater mInfInflater;

	public void setData(ArrayList<ArrayList<DiscoveryEntity>> discoverList) {
		mDiscoverList = discoverList;
	}
	
	//处理发现中的小红点信息
	public void setRedPointData(ArrayList<String> reddotList){
		myRedPointList = reddotList;
		mAdapter.notifyDataSetChanged();
		HupuLog.e("papa", "发现页设置小红点");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = initView( inflater);
		return v;
	}

	private View initView(LayoutInflater inflater)
	{
		View v = inflater.inflate(R.layout.fragment_discovery, null);
		mListView = (ExpandableListView) v.findViewById(R.id.list_discovery);
		mInfInflater = LayoutInflater.from(getActivity());
		mListView.setAdapter(mAdapter);
		for (int i = 0; i < mAdapter.getGroupCount(); i++) {
			mListView.expandGroup(i);
		}
		mListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		mListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				v.setBackgroundColor(mAct.getResources().getColor(R.color.dicovery_item_selector));
				switchToWebView(mDiscoverList.get(groupPosition).get(childPosition).mDefaultTab,
						mDiscoverList.get(groupPosition).get(childPosition).mEn);				
				return true;
			}
		});
		mListView.setGroupIndicator(null);
		return v;
	}
	
	private void switchToWebView(String url,String mEname)
	{
		Intent in =new Intent(getActivity(),DiscoveryWebViewActivity.class);
		in.putExtra("url", url);
		if(mEname.equalsIgnoreCase("caipiao")){
			in.putExtra("iscaipiao", true);
		}
		startActivity(in);
		 
	}
	BaseExpandableListAdapter mAdapter = new BaseExpandableListAdapter() {

		class Holder {
			TextView txtName;
			ImageView imgLogo;
			ImageView imgRedPoint;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			Holder holder = new Holder();
			convertView = mInfInflater.inflate(R.layout.item_discovery,
					null);
			holder.txtName = (TextView) convertView
					.findViewById(R.id.txt_name);
			holder.imgLogo = (ImageView) convertView
					.findViewById(R.id.img_logo);
			holder.imgRedPoint = (ImageView) convertView
					.findViewById(R.id.red_point);
			convertView.setTag(holder);
			 /*else {
				holder = (Holder) convertView.getTag();
			}*/
			DiscoveryEntity entity = (DiscoveryEntity) getChild(groupPosition,
					childPosition);
			holder.txtName.setText(entity.mName);
			UrlImageViewHelper.setUrlDrawable(holder.imgLogo, entity.mLogo,
					R.drawable.bg_home_nologo);
			if (myRedPointList != null) {
				for (String redStr:myRedPointList) {
					if (redStr.contains(entity.mEn) && redStr.contains("discovery")) {
						holder.imgRedPoint.setVisibility(View.VISIBLE);
						break;
					}
					//holder.imgRedPoint.setVisibility(redStr.contains(entity.mEn)?View.VISIBLE:View.GONE);
				}
			}else {
				holder.imgRedPoint.setVisibility(View.GONE);
			}
			return convertView;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = mInfInflater
						.inflate(R.layout.item_group_bg, null);
			View groupitemView = convertView.findViewById(R.id.discory_group_view);
			if(groupPosition==0)
			{
				groupitemView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, 33));
			}
			else
			{
				groupitemView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, 43));
			}
			return convertView;
		}

		@Override
		public int getGroupCount() {
			if (mDiscoverList != null)
				return mDiscoverList.size();
			return 0;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (mDiscoverList != null)
				return mDiscoverList.get(groupPosition).size();
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			if (mDiscoverList != null)
				return mDiscoverList.get(groupPosition);
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if (mDiscoverList != null)
				return mDiscoverList.get(groupPosition).get(childPosition);
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	};
}
