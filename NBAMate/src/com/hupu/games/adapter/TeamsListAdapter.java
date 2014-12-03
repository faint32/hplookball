package com.hupu.games.adapter;

import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.common.HuPuRes;
import com.pyj.activity.BaseActivity;

public class TeamsListAdapter extends BaseAdapter {
	// 填充数据的list
	private String[] mArrTeams;
	// 用来控制CheckBox的选中状况
	private static LinkedList<Boolean> mListIsSelected;

	// 用来导入布局
	private LayoutInflater inflater = null;

	private HupuBaseActivity mContext;
	/** 由于需要统计umeng数据，所以需要知道是加载页还是设置页 */

	// 构造器
	public TeamsListAdapter(Context context, int from) {
		mContext = (HupuBaseActivity) context;
		inflater = LayoutInflater.from(context);
		mListIsSelected = new LinkedList<Boolean>();
		mArrTeams = context.getResources().getStringArray(R.array.team_names);
		for (int i = 0; i < mArrTeams.length; i++) {
			mListIsSelected.add(false);
		}
	}

	public LinkedList<Integer> getSelectList() {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < mArrTeams.length; i++) {
			if (mListIsSelected.get(i))
				// 球队id从1开始
				list.add(i + 1);
		}
		return list;
	}

	// 初始化isSelected的数据
	public void initData(LinkedList<Integer> list) {

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				// id是从1开始的。
				mListIsSelected.set(list.get(i) - 1, true);
			}
			notifyDataSetChanged();
		}

	}

	@Override
	public int getCount() {
		return mArrTeams.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	boolean showOnce;
    private final int DIALOG_NOTIFY =1136;
    private int checkedPos;
    boolean bFirstNotify =true;
    
    public void clickItem()
    {
    	mListIsSelected.set(checkedPos, true);

		notifyDataSetChanged();
    }
	public void clickItem(int pos) {
		boolean is = mListIsSelected.get(pos);
		if (is){
		}else
		{			
			if( !HuPuApp.needNotify && bFirstNotify)
			{
				bFirstNotify =false;
				mContext.showCustomDialog(DIALOG_NOTIFY, R.string.push_title,
						R.string.push_open_notify, 3,	R.string.open_notify,
					 R.string.cancel);
				checkedPos =pos;
				return ;
			}
		}
		int size = getSelectList().size();
		if (size >= 5 && !showOnce && !is) {
			showOnce = true;
			// mContext.showToast("你已经关注了" + size
			// + "支球队，关注过多的球队会让你收到过多的推送通知，这可能会给你带来不必要的打扰");
			showDialog(size);
		}

		mListIsSelected.set(pos, !is);

		notifyDataSetChanged();
	}

	private void showDialog(int size) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setCancelable(true)
				.setTitle(null)
				.setMessage(
						"你已经关注了" + size
								+ "支球队，关注过多的球队会让你收到过多的推送通知，这可能会给你带来不必要的打扰")
				.setPositiveButton("确定", null);
		builder.create().show();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = inflater.inflate(R.layout.item_simple_team, null);
			holder.tv = (TextView) convertView.findViewById(R.id.txt_name);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb_follow);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}

		// 设置list中TextView的显示
		holder.tv.setText(mArrTeams[position]);
		// 根据isSelected来设置checkbox的选中状况
		holder.cb.setChecked(mListIsSelected.get(position));
		return convertView;
	}

	public final class ViewHolder {
		public TextView tv;
		public CheckBox cb;
	}

}
