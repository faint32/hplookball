package com.hupu.games.data;

import java.io.Serializable;
import java.util.List;

import org.json.JSONObject;

import com.hupu.games.common.HupuLog;
import com.hupu.games.common.HupuScheme;

import android.net.Uri;
/**推送bean*/
public class NotificationEntity extends BaseEntity implements Serializable{

   public String strTitle;
   public String strContent;
   
   /**0 url ,1 nba 2 other 6unknow*/
   public int i_type;
      
   public String strUrl;
   
   public String strSound;
   
   public int i_badge;

   public HupuScheme mScheme;
   
   public int i_id;
	@Override
	public void paser(JSONObject json) throws Exception {
		HupuLog.d("json======"+json);
		JSONObject aps =json.getJSONObject("aps");
		JSONObject alert =aps.getJSONObject("alert");
		strContent=alert.optString("body","");
		 strTitle =alert.optString("title","");
		strSound = aps.optString("sound",null);
		
        strUrl= json.optString("url",null);
       
        if(strUrl!=null)
        {
        	paserUrl(strUrl);
        }
	}

	private final static String NBA_HOME="NBA_HOME";
	private final static String NBA_PLAYBYPLAY="NBA_PLAYBYPLAY";
	private final static String NBA_BOXSCORE="NBA_BOXSCORE";
	private final static String NBA_RECAP="NBA_RECAP";
	private final static String NBA_NEWS="NBA_NEWS";
	private final static String NBA="nba";//    篮球-nba，需兼容老版本的URL
	
	private void paserUrl(String s)
	{
		try {
			Uri uri=Uri.parse(s);
			String scheme =uri.getScheme();
			if("app".equalsIgnoreCase(scheme))
			{
				
				String host =uri.getHost();
				mScheme =new HupuScheme();
				
				if(NBA_HOME.equals(host))
					mScheme.mode="games";
				else if(NBA_PLAYBYPLAY.equals(host))
					mScheme.mode="live";
				else if(NBA_BOXSCORE.equals(host))
					mScheme.mode="stats";
				else if(NBA_RECAP.equals(host))
					mScheme.mode="recap";
				else if(NBA_NEWS.equals(host))
					mScheme.mode="news";
				if(mScheme.mode!=null)
				{
					HupuLog.e("papa", "mScheme.mode==="+mScheme.mode);
					i_type=1;//为了兼容老的格式NBA	
					mScheme.template =NBA;
					mScheme.game =NBA;
					List<String> list =uri.getPathSegments();
					if (list != null && list.size() > 0) {
						mScheme.id =Integer.parseInt(list.get(0));
					}
				}
				else
				{
					i_type=2;//除了nba的其他类型比赛
					mScheme.paser(uri);
				}	
				i_id =mScheme.id ;
			}
			else if("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme) )
			{
				i_type=0;//http格式
			}
			else//未知格式
				i_type=6;
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

	}
	
	
}
