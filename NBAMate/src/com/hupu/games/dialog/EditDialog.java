/**
 * 
 */
package com.hupu.games.dialog;

import java.util.LinkedList;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.data.LeaguesEntity;
import com.hupu.games.view.dragGridView.DragGridBaseAdapter;
import com.hupu.games.view.dragGridView.DragGridView;
import com.hupu.games.view.dynamicgrid.BaseDynamicGridAdapter;
import com.hupu.games.view.dynamicgrid.DynamicGridView;
import com.umeng.socialize.controller.impl.v;

/**
 * @author panyongjun
 * 包含了两种方案，一种是对换式的，一种是排序的
 */
public class EditDialog extends Dialog {

	DynamicGridView gridView;

	LinkedList<LeaguesEntity> mList;

	DragGridView gridView1;

	Button submitBt;
	LinearLayout cancelBt;

	RelativeLayout layout;
	Window window;
	private HupuHomeActivity context;
	
	private static final int COLUM = 3;
	
	private static final int HOR_SPACE_PADDING = 20;
	/**
	 * 
	 * */
	public EditDialog(Context context, LinkedList<LeaguesEntity> leagueList) {
		super(context, R.style.MyWebDialog);
		this.context = (HupuHomeActivity) context;
		mList =leagueList;
		//		View v = LayoutInflater.from(context).inflate(
		//				R.layout.view_league_edit, null);
		//		gridView = (DynamicGridView) v.findViewById(R.id.dynamic_grid);
		//		setGridView( context);
		View v = LayoutInflater.from(context).inflate(R.layout.view_league_edit_t, null);
		layout = (RelativeLayout)v.findViewById(R.id.layout_order_outer);
		submitBt = (Button)v.findViewById(R.id.btn_submit);
		cancelBt = (LinearLayout)v.findViewById(R.id.layout_btn_cancel);
		gridView1 = (DragGridView) v.findViewById(R.id.dynamic_grid);
		setGridView1(context,v);
		Click click = new Click();
		submitBt.setOnClickListener(click);
		cancelBt.setOnClickListener(click);
		getWindow().setGravity(Gravity.CENTER);
		setContentView(v);
	}
	private class Click implements View.OnClickListener {
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
			case R.id.btn_submit:
			case R.id.layout_btn_cancel:
//				EditDialog.this.dismiss();
				onCancle();
				break;
			}
		}
	}

	EditAdapter1 mAdapter;
	private void setGridView1(Context context,View v)
	{
		mAdapter=new EditAdapter1();
		gridView1.setAdapter(mAdapter);
		//		mList.add(to, mList.remove(from));
		gridView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gridView1.removeItemAnimation(position);
			}
		});
	}

	/**
	 * 显示对话框
	 * */
	public void goShow() {

		show();
		getWindow().setGravity(Gravity.TOP);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		action_TopToBottom();
	}



	private void setGridView(Context context) {

		gridView.setAdapter(new EditAdapter(context,mList,3));
		gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
			@Override
			public void onDragStarted(int position) {

			}

			@Override
			public void onDragPositionsChanged(int oldPosition, int newPosition) {

				LeaguesEntity old =mList.get(oldPosition);
				LeaguesEntity n =mList.get(newPosition);
				mList.set(oldPosition, n);
				mList.set(newPosition, old);
			}
		});
		gridView.startEditMode();
	}



	private class ViewHolder {
		private Button titleText;

		private ViewHolder(View view) {
			titleText = (Button) view.findViewById(R.id.item_title);
		}

		void build(String title) {
			titleText.setText(title);
		}
	}
	boolean isChanged;
	public boolean getIsChanged()
	{
		return isChanged;
	}

	public class EditAdapter1 extends BaseAdapter implements DragGridBaseAdapter
	{

		private int mHidePosition = -1;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_league_edit, null);
			
			holder = new ViewHolder(convertView);
			
			if(position == mHidePosition){
				convertView.setVisibility(View.INVISIBLE);
			}
			holder.build(mList.get(position).name);
			return convertView;
		}



		@Override
		public int getCount() {
			return mList.size();
		}



		@Override
		public Object getItem(int position) {
			return null;
		}



		@Override
		public long getItemId(int position) {
			return 0;
		}



		@Override
		public void reorderItems(int oldPosition, int newPosition) {
			isChanged =true;
			mList.add(newPosition, mList.remove(oldPosition));
		}



		@Override
		public void setHideItem(int hidePosition) {
			this.mHidePosition = hidePosition; 
			notifyDataSetChanged();
		}



		@Override
		public void removeItem(int removePosition) {

		}

	}

	public class EditAdapter extends BaseDynamicGridAdapter {
		public EditAdapter(Context context, LinkedList<LeaguesEntity> items,
				int columnCount) {
			super(context, items, columnCount);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_league_edit, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.build(mList.get(position).name);
			return convertView;
		}
	}
	
	public void action_TopToBottom()
	{
		// TODO Auto-generated method stub
		final LinearLayout selection = (LinearLayout) findViewById(R.id.layout_shuddle_action);
		selection.setVisibility(View.VISIBLE);

		Animation showAnim=AnimationUtils.loadAnimation(context, R.anim.fade_in_top);

		selection.startAnimation(showAnim);

		showAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation)
			{

				selection.clearAnimation();

			}
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub

			}});

	}
	
	public void onCancle()
	{
		final LinearLayout selection = (LinearLayout) findViewById(R.id.layout_shuddle_action);

		Animation hiddenAnim=AnimationUtils.loadAnimation(context, R.anim.fade_out_top);
		selection.startAnimation(hiddenAnim);
		
		hiddenAnim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation)
			{
				selection.clearAnimation();
				selection.setVisibility(View.INVISIBLE);
				if(context!=null)
				{
					context.finishShuddle();
				}
				EditDialog.this.hide();
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
			}});


	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		onCancle();
	}
	
	
}
