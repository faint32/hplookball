package com.hupu.games.common;

import java.io.Serializable;
import java.util.List;

import android.net.Uri;

@SuppressWarnings("serial")
public class HupuScheme implements Serializable{

	public String template;//模板
	public String game;//联赛
	public String mode;//模块标签
	public String url;
	public int id;
	
	public String mUri;

	public void paser(Uri uri)
	{
		try {
			mUri =uri.toString();
			List<String> list =uri.getPathSegments();
			template=uri.getHost();
			game=list.size()>=1?list.get(0):"";
			mode=list.size()>=2?list.get(1):"";
			id=Integer.parseInt(list.size()>=3?list.get(2):"0");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getParameter(String key)
	{
		if(mUri!=null)
		{
			return Uri.parse(mUri).getQueryParameter(key);
		}
		return null;
	}
	
	public String getQuery()
	{
		if(mUri!=null)
		{
			return Uri.parse(mUri).getQuery();
		}
		return null;
	}
	
}
