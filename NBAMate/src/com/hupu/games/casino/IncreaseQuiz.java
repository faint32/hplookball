package com.hupu.games.casino;

import java.io.Serializable;
import java.util.ArrayList;

import com.hupu.games.data.IncreaseEntity;


public class IncreaseQuiz implements Serializable {

	public ArrayList<IncreaseEntity> increaseList;

	public ArrayList<IncreaseEntity> getIncreaseList() {
		return increaseList;
	}

	public void setIncreaseList(ArrayList<IncreaseEntity> increaseList) {
		this.increaseList = increaseList;
	}
}
