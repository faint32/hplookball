package com.hupu.games.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hupu.games.R;
import com.hupu.games.activity.NBAPlayerInfoActivity;
import com.hupu.games.common.HupuLog;
import com.hupu.games.data.game.basketball.NbaPlayersDataReq.PlayerDataEntity;
import com.hupu.games.view.HScrollView;
import com.hupu.games.view.HScrollView.OnScrollChangedListener;
import com.hupu.games.view.HScrollView.ScrollViewObserver1;
import com.pyj.adapter.BaseListAdapter;

/**
 * 
 * */
public class NbaTeamPlayersDataAdapter extends BaseListAdapter<PlayerDataEntity> {

	int txtCol;
	int txtWidth;
	int txtHeight;
//	HScrollView headSrcrollView;
	ScrollViewObserver1 ob;

	public NbaTeamPlayersDataAdapter(Context context ,ScrollViewObserver1 ob1) {
		super(context);
		txtCol = context.getResources().getColor(R.color.res_cor1);
//		headSrcrollView =srcrollView;
		txtWidth=context.getResources().getDimensionPixelSize(
				R.dimen.txt_player);
		txtHeight =context.getResources().getDimensionPixelSize(
				R.dimen.txt_player_height);
		ob =ob1;

	}

	class Holder {
		/** 数据 */
		TextView[] txtData;
		/** 球员名字 */
		TextView txtName;
	}


	@Override
	public View getView(final int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		PlayerDataEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_nba_player_data, null);
			item = new Holder();
			
			item.txtData = new TextView[entity.values.length-1];
			item.txtName=(TextView)contentView.findViewById(R.id.txt_player_name);
			float size =item.txtName.getTextSize()*0.9f;
			LinearLayout container = (LinearLayout) contentView
					.findViewById(R.id.layout_containter);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(txtWidth, txtHeight);
			for (int i = 0; i < item.txtData.length; i++) {
				item.txtData[i] = buildTextView();
				item.txtData[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
				container.addView(item.txtData[i],params);
			}
			
			
			HScrollView scrollView =(HScrollView)contentView.findViewById(R.id.hscrollview);
			scrollView.setNoHeader(ob);
//			scrollView.setOnTouchListener(touch);
//			headSrcrollView
//					.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
//							scrollView));
			contentView.setTag(item);
			
		} else {
			item = (Holder) contentView.getTag();
		}
		
//		contentView.setOnClickListener(new Click(pos) );
		item.txtName.setText(entity.values[0]);
		for (int i = 0; i < item.txtData.length; i++) {
			item.txtData[i].setText(entity.values[i+1]);
		}
		if(pos%2==0)
			contentView.setBackgroundResource(R.drawable.bg_player_data_selector1);
		else
			contentView.setBackgroundResource(R.drawable.bg_player_data_selector2);
		return contentView;
	}

	/**
	 * @param type
	 *            0 表示标题 1表示数据
	 * */
	private TextView buildTextView() {
		TextView tv =(TextView)mInflater.inflate(R.layout.txt_player_data, null);		
		return tv;
	}

	
	class Click implements OnClickListener {

		public int pos;
		public Click(int p)
		{
			pos =p;
		}
		@Override
		public void onClick(View v){
			PlayerDataEntity entity = getItem(pos);
			Intent in = new Intent(mContext, NBAPlayerInfoActivity.class);
			in.putExtra("pid", entity.player_id);
			mContext.startActivity(in);
		}

	}
}
