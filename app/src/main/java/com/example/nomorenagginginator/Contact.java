package com.example.nomorenagginginator;

import android.text.format.Time;

public class Contact {
	private int contactID;
	private String contactName;

	private String cellNumber;

	private Time birthday;
	
	public Contact() {
		contactID = -1;
		Time t = new Time();
		t.setToNow();
		birthday = t;
	}

	public int getContactID() {
		return contactID;
	}
	public void setContactID(int i) {
		contactID = i;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String s) {
		contactName = s;
	}
	public Time getBirthday() {
		return birthday;
	}
	public void setBirthday(Time t) {
		birthday = t;
	}

	public void setCellNumber(String s) {
		cellNumber = s;
	}
	public String getCellNumber() {
		return cellNumber;
	}

}