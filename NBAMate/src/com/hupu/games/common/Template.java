/**
 * 
 */
package com.hupu.games.common;

/**
 * @author panyongjun
 *  联赛模板
 */
public enum Template {

	TEMPLATE_SOCCER_LEAGUE("soccerleagues"),
	TEMPLATE_SOCCER_CUP_LEAGUE("soccercupleagues"),
	TEMPLATE_NBA("nba"),
	TEMPLATE_CBA("cba"),
	TEMPLATE_BROWSER ("browser"),
	TEMPLATE_BROWSER_NAV ("browser_no_nav");
	
	private String value;
	private Template(String v){
		value =v;
	}
	
}
