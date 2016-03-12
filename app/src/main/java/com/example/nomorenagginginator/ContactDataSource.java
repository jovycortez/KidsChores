package com.example.nomorenagginginator;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

public class ContactDataSource {
	
	private SQLiteDatabase database;
	private ContactDBHelper dbHelper;

	public ContactDataSource(Context context) {
		dbHelper = new ContactDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean insertContact(Contact c) 
	{
		boolean didSucceed = false;
		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put("contactname", c.getContactName());

			initialValues.put("cellnumber", c.getCellNumber());    

			initialValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));

			didSucceed = database.insert("children", null, initialValues) > 0;

		}
		catch (Exception e) {
			//Do Nothing -will return false if there is an exception
		}
		return didSucceed;
	}

	public boolean updateContact(Contact c) 
	{
		boolean didSucceed = false;
		try {
			Long rowId = Long.valueOf(c.getContactID());
			ContentValues updateValues = new ContentValues();

			updateValues.put("contactname", c.getContactName());

			updateValues.put("cellnumber", c.getCellNumber());    

			updateValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));

			didSucceed = database.update("children", updateValues, "_id=" + rowId, null) > 0;
		}
		catch (Exception e) {
			//Do Nothing -will return false if there is an exception
		}
		return didSucceed;
	}
	
	public int getLastContactId() {
		int lastId = -1;
		try
		{       	
			String query = "Select MAX(_id) from children";
			Cursor cursor = database.rawQuery(query, null);  

			cursor.moveToFirst();
			lastId = cursor.getInt(0);
			cursor.close();
		}
		catch (Exception e) {
			lastId = -1;
		}
		return lastId;
	}

	public ArrayList<String> getContactName() {
		ArrayList<String> contactNames = new ArrayList<String>();
		try {
			String query = "Select contactname from children";
			Cursor cursor = database.rawQuery(query, null);  

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				contactNames.add(cursor.getString(1));
				cursor.moveToNext();
			} 
			cursor.close();
		}
		catch (Exception e) {
			contactNames = new ArrayList<String>();
		}
		return contactNames;
	}

	public ArrayList<Contact> getContacts(String sortField, String sortOrder) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		try {
			String query = "SELECT  * FROM children ORDER BY " + sortField + " " + sortOrder;
			Cursor cursor = database.rawQuery(query, null);

			Contact newContact;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				newContact = new Contact();
				newContact.setContactID(cursor.getInt(0));
				newContact.setContactName(cursor.getString(1));
				newContact.setCellNumber(cursor.getString(2));

				Time t = new Time();
				t.set(Long.valueOf(cursor.getString(3)));
				newContact.setBirthday(t);

				contacts.add(newContact);
				cursor.moveToNext();

			}
			cursor.close();
		}
		catch (Exception e) {
			contacts = new ArrayList<Contact>();
		}
		return contacts;
	}

	public Contact getSpecificContact(int contactId) {
		Contact contact = new Contact();

			String query = "SELECT  * FROM children WHERE _id =" + contactId;
			Cursor cursor = database.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			contact.setContactID(cursor.getInt(0));
			contact.setContactName(cursor.getString(1));

			contact.setCellNumber(cursor.getString(2));

			Time t = new Time();
			t.set(Long.valueOf(cursor.getString(3)));
			contact.setBirthday(t);
		}
		cursor.close();
			
		return contact;
	}

	public boolean deleteContact(int contactId) {
		boolean didDelete = false;
		try {
			didDelete = database.delete("children", "_id=" + contactId, null) > 0;
		}
		catch (Exception e) {
			//Do nothing -return value already set to false
		}
		return didDelete;
	}
}
