package com.hupu.games.data;
import com.hupu.games.R;

public class TeamValueEntity {

	/** 球队名字 */
	public String str_name;
	public String str_name_en;
	/** 球队ID */
	public int i_tid;
	/** 球队颜色 */
	public int i_color;
	/** 球队图标 */
	public int i_logo;
	public int i_logo_small;
	public static final int[] ICON_RES = { R.drawable.logo1,
			R.drawable.logo2, R.drawable.logo3, R.drawable.logo4,
			R.drawable.logo5, R.drawable.logo6, R.drawable.logo7,
			R.drawable.logo8, R.drawable.logo9, R.drawable.logo10,
			R.drawable.logo11, R.drawable.logo12, R.drawable.logo13,
			R.drawable.logo14, R.drawable.logo15, R.drawable.logo16,
			R.drawable.logo17, R.drawable.logo18, R.drawable.logo19,
			R.drawable.logo20, R.drawable.logo21, R.drawable.logo22,
			R.drawable.logo23, R.drawable.logo24, R.drawable.logo25,
			R.drawable.logo26, R.drawable.logo27, R.drawable.logo28,
			R.drawable.logo29, R.drawable.logo30,
	};
	
	public static final int[] ICON_RES_SMALL = { R.drawable.logo1_88px,
		R.drawable.logo2_88px, R.drawable.logo3_88px, R.drawable.logo4_88px,
		R.drawable.logo5_88px, R.drawable.logo6_88px, R.drawable.logo7_88px,
		R.drawable.logo8_88px, R.drawable.logo9_88px, R.drawable.logo10_88px,
		R.drawable.logo11_88px, R.drawable.logo12_88px, R.drawable.logo13_88px,
		R.drawable.logo14_88px, R.drawable.logo15_88px, R.drawable.logo16_88px,
		R.drawable.logo17_88px, R.drawable.logo18_88px, R.drawable.logo19_88px,
		R.drawable.logo20_88px, R.drawable.logo21_88px, R.drawable.logo22_88px,
		R.drawable.logo23_88px, R.drawable.logo24_88px, R.drawable.logo25_88px,
		R.drawable.logo26_88px, R.drawable.logo27_88px, R.drawable.logo28_88px,
		R.drawable.logo29_88px, R.drawable.logo30_88px,
};
	public static final int[] ICON_RES_LITE = { R.drawable.lite_logo1_88px,
		R.drawable.lite_logo2_88px, R.drawable.lite_logo3_88px, R.drawable.lite_logo4_88px,
		R.drawable.lite_logo5_88px, R.drawable.lite_logo6_88px, R.drawable.lite_logo7_88px,
		R.drawable.lite_logo8_88px, R.drawable.lite_logo9_88px, R.drawable.lite_logo10_88px,
		R.drawable.lite_logo11_88px, R.drawable.lite_logo12_88px, R.drawable.lite_logo13_88px,
		R.drawable.lite_logo14_88px, R.drawable.lite_logo15_88px, R.drawable.lite_logo16_88px,
		R.drawable.lite_logo17_88px, R.drawable.lite_logo18_88px, R.drawable.lite_logo19_88px,
		R.drawable.lite_logo20_88px, R.drawable.lite_logo21_88px, R.drawable.lite_logo22_88px,
		R.drawable.lite_logo23_88px, R.drawable.lite_logo24_88px, R.drawable.lite_logo25_88px,
		R.drawable.lite_logo26_88px, R.drawable.lite_logo27_88px, R.drawable.lite_logo28_88px,
		R.drawable.lite_logo29_88px, R.drawable.lite_logo30_88px,
	};
	public static TeamValueEntity getDefault(int tid)
	{
		TeamValueEntity tv=new TeamValueEntity();
		tv.i_tid =tid;
		tv. i_logo=R.drawable.logo0;
		tv. i_color=0xffd7d7d7;
		tv.i_logo_small=R.drawable.bg_home_nologo;
		return tv;
	}
}
