package com.example.nomorenagginginator;

import android.text.format.Time;

public class Chores {
	private int choreID;
	private String chore;

	private String frequency;

	private Time duration;

	public Chores() {
		choreID = -1;
		Time t = new Time();
		t.setToNow();
		duration = t;
	}

	public int getChoreID() {
		return choreID;
	}
	public void setChoreID(int i) {
		choreID = i;
	}
	public String getChore() {
		return chore;
	}
	public void setChore(String s) {
		chore = s;
	}
	public Time getDuration() {
		return duration;
	}
	public void setDuration(Time t) {
		duration = t;
	}

	public void setFrequency(String s) {
		frequency = s;
	}
	public String getFrequency() {
		return frequency;
	}

}