package com.hupu.games.casino;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.common.HuPuRes;
import com.hupu.games.data.personal.box.BoxInfoEntity;
import com.hupu.games.pay.PhoneBindActivity;
import com.hupu.http.HupuHttpHandler;

public class MyBoxActivity extends HupuBaseActivity {

	private ListView mList;
	private boolean reqNow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mybox);
		mList =(ListView)findViewById(R.id.list_box);
		adapter =new BoxAdapter();
		mList.setAdapter(adapter);
		setOnClickListener(R.id.btn_back);
		reqNow =true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(reqNow)
			reqBoxInfo();
	}

	@Override
	public void treatClickEvent(int id) {
		super.treatClickEvent(id);
		switch(id)
		{
		case R.id.btn_back:
			finish();
			break;

		}
	}

	@Override
	public void treatClickEvent(View view) 
	{
		int id =view.getId();
		if(id == R.id.btn_convert)
		{
			reqNow=true;
			int pos =Integer.parseInt((String)view.getTag());
			Intent in =new Intent(this,ShakeBoxActivity.class);
			in.putExtra("type", pos);
			in.putExtra("num", entity.boxInfo[2-pos]);
			startActivityForResult(in, 3456);
		}
	}
	
	void switchToPhoneBindAct() {
		Intent intent = new Intent(this, PhoneBindActivity.class);
		startActivityForResult(intent, HupuBaseActivity.REQ_GO_BIND_PHONE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Log.d("HupuDataActivity", "requestCode=" + requestCode);
		if (REQ_GO_BIND_PHONE == requestCode && resultCode == RESULT_OK) {
			reqBoxInfo();
		}
	}
	private void reqBoxInfo()
	{
		reqNow =false;
		if(mToken==null)
		{
			//
			switchToPhoneBindAct();
		}
		else
		{
			//
			initParameter();
			sendRequest(HuPuRes.REQ_METHOD_GET_BOX_COUNT, mParams, new HupuHttpHandler(this));
		}
	}
	
	@Override
	public void onReqResponse(Object o, int methodId) {
		super.onReqResponse(o, methodId);
		switch(methodId)
		{
		case HuPuRes.REQ_METHOD_GET_BOX_COUNT:
			entity =(BoxInfoEntity)o;
			adapter.notifyDataSetChanged();
			break;
		}
	}
	
	BoxAdapter adapter;
	BoxInfoEntity entity;
	class BoxAdapter extends BaseAdapter
	{

		private final  int [] imgRes={R.drawable.icon_box_gold,R.drawable.icon_box_silver,R.drawable.icon_box_copper};
		private   String [] txtType;
		private   String [] txtDesc;
		BoxAdapter()
		{
			txtType =getResources().getStringArray(R.array.box_info);
			txtDesc=getResources().getStringArray(R.array.box_info_description);
		}
		
		@Override
		public int getCount() {
			
			return 3;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			BoxHolder item = null;
	
			if (convertView == null) {
				convertView =LayoutInflater.from(MyBoxActivity.this).inflate(R.layout.item_box, null);
				item =new BoxHolder();
				item.imgBox =(ImageView)convertView.findViewById(R.id.img_box);
				item.txtBoxType =(TextView)convertView.findViewById(R.id.txt_box_type);
				item.txtBoxDescrption =(TextView)convertView.findViewById(R.id.txt_box_description);
				item.btnConvert=(Button)convertView.findViewById(R.id.btn_convert);
				convertView.setTag(item);				
			} else {
				item = (BoxHolder) convertView.getTag();
			}

			item.imgBox.setImageResource(imgRes[position]);
			if(entity==null)
			{
				item.txtBoxType.setText(String.format(txtType[position],0 ));
				item.btnConvert.setBackgroundResource(R.drawable.dialog_gray_selector);
				item.btnConvert.setEnabled(false);
			}
			else
			{
				item.txtBoxType.setText(String.format(txtType[position], entity.boxInfo[2-position]));
				if(entity.boxInfo[2-position]==0)
				{
					item.btnConvert.setBackgroundResource(R.drawable.dialog_gray_selector);
					item.btnConvert.setEnabled(false);
				}
				else
				{
					item.btnConvert.setBackgroundResource(R.drawable.dialog_red_selector);
					item.btnConvert.setEnabled(true);
					item.btnConvert.setOnClickListener(click);
					item.btnConvert.setTag(""+position);
				}

			}
			item.txtBoxDescrption.setText(txtDesc[position]);
			return convertView;
		}
		
		class BoxHolder
		{
			ImageView imgBox;
			TextView txtBoxType;
			TextView txtBoxDescrption;
			Button btnConvert;
		}
	}
	
}
