/**
 * 
 */
package com.hupu.games.handler;

/**
 * @author panyongjun
 * webview长按处理
 */
public interface IWebViewLongClickEvent {
	/**当webview中的图片被长按时回调该函数
	 * @return data 点击图片的url*/
    void bitmapLongClick(String data);
}
