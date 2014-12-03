package com.hupu.games.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hupu.games.R;
import com.hupu.games.activity.HupuBaseActivity;
import com.hupu.games.activity.WebViewActivity;
import com.hupu.games.data.ChatEntity;

/**
 * 聊天数据列表
 * @author panyongjun
 * */
public class ChatListAdapter extends BaseAdapter {

	private LinkedList<ChatEntity> mListData;

	private LayoutInflater mInflater;

	HupuBaseActivity context;

	private String tag;
	private String prenick="— ";
	
//	private int [] emoji_ids;
	public ChatListAdapter(Context c,String TAG) {
		mInflater = LayoutInflater.from(c);
		context = (HupuBaseActivity)c;
		tag =TAG;
		//获取表情资源文件
//		Resources res=c.getResources();
//		String ss []=res.getStringArray(R.array.emoji_list);
//		emoji_ids =new int[ss.length]; 
//		String pack =c.getPackageName();
//		for(int i=0;i<ss.length;i++)
//		{
//			//反射
//			emoji_ids[i]=res.getIdentifier(ss[i],"drawable",pack);
//		}
	}

	public void setData(LinkedList<ChatEntity> data) {
		mListData = data;
		notifyDataSetChanged();
	}

	public void addData(ChatEntity data) {
		mListData.add(0, data);
		notifyDataSetChanged();
	}

	class Holder {
		TextView txtContent;
		TextView txtName;
		ImageView imgEmoji;
	}

	@Override
	public ChatEntity getItem(int position) {

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
		ChatEntity entity = mListData.get(pos);
		if (contentView == null) {
			contentView = mInflater.inflate(R.layout.item_chat_msg, null);
			item = new Holder();
			item.txtContent = (TextView) contentView
					.findViewById(R.id.txt_content);
			item.txtName = (TextView) contentView.findViewById(R.id.txt_name);

			item.txtContent.setMovementMethod(LinkMovementMethod.getInstance());
			item.imgEmoji = (ImageView) contentView.findViewById(R.id.img_emoji);
			contentView.setTag(item);
		} else {
			item = (Holder) contentView.getTag();
		}
		// int w3 = contentView.getMeasuredWidth();
		if(entity.emoji ==null)
		{
			item.txtContent.setVisibility(View.VISIBLE);
			
			if(entity.cgift!=null && entity.cgift.linkColor!=null){
				int color=Color.parseColor(entity.cgift.linkColor);
				item.txtContent.setTextColor(color);
			}else{
				item.txtContent.setTextColor(context.getResources().getColor(R.color.res_cor1));
			}
			item.txtContent.setText(Html.fromHtml(entity.content));
			item.imgEmoji.setImageDrawable(null);
			item.imgEmoji.setVisibility(View.GONE);
			addLink(item.txtContent);
			RelativeLayout.LayoutParams param =(RelativeLayout.LayoutParams)item.txtName.getLayoutParams();
			param.addRule(RelativeLayout.BELOW, R.id.txt_content);
			param.addRule(RelativeLayout.ALIGN_RIGHT, R.id.txt_content);
		}
		else
		{
			item.imgEmoji.setVisibility(View.VISIBLE);
			item.txtContent.setVisibility(View.GONE);
			RelativeLayout.LayoutParams param =(RelativeLayout.LayoutParams)item.txtName.getLayoutParams();
			param.addRule(RelativeLayout.BELOW, R.id.img_emoji);
			param.addRule(RelativeLayout.ALIGN_RIGHT, R.id.img_emoji);
			setEmoji(item.imgEmoji,entity.emoji );
		}

		// item.txtContent.setText(entity.content);
		item.txtName.setText(prenick + entity.username);
		setNickName(item.txtName ,entity.vip,entity.username);
		if (entity.username.equals(""))
			contentView.setVisibility(View.GONE);
		else
			contentView.setVisibility(View.VISIBLE);
		// int w =
		// View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		// int h =
		// View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		//
		// item.txtContent.measure(w, h);
		// item.txtName.measure(w, h);
		//
		// int w1= item.txtContent.getMeasuredWidth();
		// int w2 =item.txtName.getMeasuredWidth();
		//
		// Log.d("getview", "pos=="+pos+"  w1=="+w1+"  w2=="+w2+"  w3=="+w3);
		// if ( w1<w2)
		// {
		// item.txtContent.setWidth(w1+10);
		// }
		return contentView;
	}

	private void setNickName(TextView tv ,int vip,String name)
	{
		if(vip ==0)
		{
			tv.setText(prenick + name);
			tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			tv.setTextColor(0xff919191);
		}
		else
		{
			tv.setText(prenick + name);
			tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_vip1, 0);
			tv.setTextColor(0xffff0000);
		}
		
	}
	private void addLink(TextView tv) {
		CharSequence text = tv.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable sp = (Spannable) tv.getText();
			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder style = new SpannableStringBuilder(text);
			style.clearSpans();// should clear old spans
			for (URLSpan url : urls) {
				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
				style.setSpan(myURLSpan, sp.getSpanStart(url),
						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			tv.setText(style);
		}
	}

	private class MyURLSpan extends ClickableSpan {

		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			// Log.d("   text view", "murl" + mUrl);
			Intent in = new Intent(context, WebViewActivity.class);
			in.putExtra("url", mUrl);
			context.startActivity(in);
		}
	}
	
	private void setEmoji(ImageView img,String res)
	{
		int start =res.indexOf("_");
		int index =0;
		try {
			index =Integer.parseInt(res.substring(start+1));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}	

	}
}
