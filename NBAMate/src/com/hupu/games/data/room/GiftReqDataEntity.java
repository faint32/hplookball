package com.hupu.games.data.room;
import com.hupu.games.data.BaseEntity;
/**
 * 送礼物请求
 * @author zhenhua
 *
 */
public class GiftReqDataEntity extends BaseEntity {
	String uid;
	int giftid;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getGiftid() {
		return giftid;
	}
	public void setGiftid(int giftid) {
		this.giftid = giftid;
	}

	
	
}
