package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.HuPuApp;
import com.hupu.games.R;
import com.hupu.games.activity.HupuHomeActivity;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.news.NewsEntity;
import com.hupu.games.db.HuPuDBAdapter;
import com.hupu.games.fragment.NewsFragment;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * 比赛统计数据
 * */
public class NewsListAdapter extends BaseAdapter {

	private LinkedList<NewsEntity> mListData;

	private LayoutInflater mInflater;
	NewsFragment newFragment;
	boolean isMobleNet;
	Context mContext;
	HuPuDBAdapter mDbAdapter;
	HupuHomeActivity mAct;
	public NewsListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mDbAdapter = new HuPuDBAdapter(mContext);
		mContext = context;
	}
	public boolean checkNetIs2Gor3G() {
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(mContext.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
			// NETWORK_TYPE_EVDO_A是电信3G
			// NETWORK_TYPE_EVDO_A是中国电信3G的getNetworkType
			// NETWORK_TYPE_CDMA电信2G是CDMA
			// 移动2G卡 + CMCC + 2//type = NETWORK_TYPE_EDGE
			// 联通的2G经过测试 China Unicom 1 NETWORK_TYPE_GPRS

			// if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
			// || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
			// || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE){
			// System.out.println("mobile connected");
			// }
			// else{
			// System.out.println("type:"+info.getSubtype());
			// System.out.println("not mobile");
			// }
		} else
			return false;
	}

	public void setData(LinkedList<NewsEntity> data) {
		mListData = data;
		isMobleNet = checkNetIs2Gor3G();
		notifyDataSetChanged();
	}

	public void setAct(HupuHomeActivity act){
		mAct = act;
	}
	class Holder {
		RelativeLayout newsItemLayout;
		TextView txtContent;
		TextView txtTitle;
		ImageView newsImg;
		TextView txtNum;
		TextView lightNum;
		ImageView lightIc;
		TextView topType;
	}

	@Override
	public NewsEntity getItem(int position) {

		if (mListData == null)
			return null;
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getCount() {
		if (mListData == null)
			return 0;
		return mListData.size();
	}

	
	public void clear()
	{
		if (mListData != null)
			mListData.clear();
		mListData =null;
		notifyDataSetChanged();
	}
	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		NewsEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_news, null);
			item = new Holder();
			item.newsItemLayout = (RelativeLayout) contentView.findViewById(R.id.item_news_layout);
			item.txtContent = (TextView) contentView
					.findViewById(R.id.txt_content);
			item.txtTitle = (TextView) contentView.findViewById(R.id.txt_title);
			item.txtNum = (TextView) contentView.findViewById(R.id.txt_nums);
			item.lightNum = (TextView) contentView.findViewById(R.id.light_nums);
			item.newsImg = (ImageView) contentView.findViewById(R.id.news_img);
			item.lightIc = (ImageView) contentView.findViewById(R.id.light_ic);
			item.topType = (TextView) contentView.findViewById(R.id.txt_top);
			
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		item.txtTitle.setText(entity.title);
		if (mAct.mApp.getIsRead((int)entity.nid) == 1) {
			item.txtTitle.setTextColor(0xffaaaaaa);
		}else {
			item.txtTitle.setTextColor(Color.BLACK);
		}
		
		item.txtContent.setText(entity.summary);
		
		item.txtNum.setText(entity.replies+"");
		item.lightNum.setText(entity.lights);
		item.lightNum.setVisibility(entity.lights.equals("0") || entity.lights.equals("") ? View.GONE:View.VISIBLE);
		item.lightIc.setVisibility(entity.lights.equals("0") || entity.lights.equals("") ? View.GONE:View.VISIBLE);
		if (entity.topType != null) {
			if ("".equals(entity.topType)) {
				item.topType.setVisibility(View.GONE);
			}else {
				item.topType.setVisibility(View.VISIBLE);
				item.topType.setText(entity.topType);
			}
		}else {
			item.topType.setVisibility(View.GONE);
		}
        if (SharedPreferencesMgr.getBoolean("is_no_pic", true)) {
        	if (UrlImageViewHelper.isLocalFile(mContext, entity.newsImg)) {
        		UrlImageViewHelper.setUrlDrawable(item.newsImg,entity.newsImg,R.drawable.no_news_pic);
			}else {
				if (!isMobleNet) {
					UrlImageViewHelper.setUrlDrawable(item.newsImg,entity.newsImg,R.drawable.no_news_pic);
				}else
					item.newsImg.setImageResource(R.drawable.no_news_pic);
			}
			
		}else {
			UrlImageViewHelper.setUrlDrawable(item.newsImg,entity.newsImg,R.drawable.no_news_pic);
		}
        

		return contentView;
	}

}
