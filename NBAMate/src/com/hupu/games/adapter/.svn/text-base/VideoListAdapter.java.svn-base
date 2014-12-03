package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.common.SharedPreferencesMgr;
import com.hupu.games.data.VideoEntity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * 比赛统计数据
 * */
public class VideoListAdapter extends BaseAdapter {

	private LinkedList<VideoEntity> mListData;

	private LayoutInflater mInflater;
	private Context mContext; 
	public VideoListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	
	public void setData(LinkedList<VideoEntity> data) {
//		if(mListData!=null)
//			mListData.clear();
		mListData = data;
//		Log.d("VideoListAdapter", "total size ="+mListData.size());
		notifyDataSetChanged();
	}

	class Holder {
		TextView txtContent;
		ImageView img;
		TextView txtTime;

	}

	@Override
	public VideoEntity getItem(int position) {

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

	@Override
	public View getView(int pos, View contentView, ViewGroup arg2) {
		Holder item = null;
		VideoEntity entity =mListData.get(pos);
		if (contentView == null) {
			contentView =mInflater.inflate(R.layout.item_video, null);
			item = new Holder();
			item.txtContent=(TextView)contentView.findViewById(R.id.txt_content);
			item.img=(ImageView)contentView.findViewById(R.id.img_video);
			item.txtTime =(TextView)contentView.findViewById(R.id.txt_time);
			contentView.setTag(item);
		} else {
			item=(Holder)contentView.getTag();
		}
		item.txtContent.setText(entity.title);
		
		//视频缩略图支持  无图模式
		if (SharedPreferencesMgr.getBoolean("is_no_pic", true)) {
			if (UrlImageViewHelper.isLocalFile(mContext, entity.cover)) {
				UrlImageViewHelper.setUrlDrawable(item.img, entity.cover,R.drawable.bg_video_default);
			}else {
				if (!checkNetIs2Gor3G()) {
					UrlImageViewHelper.setUrlDrawable(item.img, entity.cover,R.drawable.bg_video_default);
				}else
					item.img.setImageResource(R.drawable.bg_video_default);
			}
		}else {
			UrlImageViewHelper.setUrlDrawable(item.img, entity.cover,R.drawable.bg_video_default);
		}
		
		item.txtTime.setText(""+entity.playtime);
		
		return contentView;
	}

	
	public boolean checkNetIs2Gor3G() {
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(mContext.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
			Log.e("papa", "type:" + info.getSubtype());
			return true;
		} else
			return false;
	}
	
}
