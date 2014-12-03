package com.hupu.games.data;

import org.json.JSONObject;

public class RPEntity extends BaseEntity {

	public int status;//    展示状态（0：隐藏，1：显示+可点，2：显示+不可点）
	public String notice;//    攒人品提示文字
	/**    主队颜色*/
	public int home_color;
	/**  客队颜色*/
	public int away_color  ;
	/** 主队人品数值*/
	public int  home_rp ;  
	/**客队人品数值*/
	public int  away_rp;    
	/**进度值*/
	public float  progress;    
	@Override
	public void paser(JSONObject json) throws Exception {
		
		status=json.optInt(KEY_STATUS, 0);
		notice=json.optString("notice", null);
		/**    主队颜色*/
		String color=json.optString("home_color", "");
		if(color.length()>5)
		{
			home_color=Integer.parseInt(color, 16);
			home_color|=0xff000000;
		}
		/**  客队颜色*/
		color=json.optString("away_color", "");
		if(color.length()>5)
		{
			away_color=Integer.parseInt(color, 16);
			away_color|=0xff000000;
		}
		/** 主队人品数值*/
		home_rp=json.optInt("home_rp", 0);
		/**客队人品数值*/
		away_rp=json.optInt("away_rp", 0);
		/**进度值*/
		progress=(float)json.optDouble("progress", 0);
	}

}
