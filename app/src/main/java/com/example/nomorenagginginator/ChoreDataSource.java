package com.example.nomorenagginginator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

import java.util.ArrayList;

public class ChoreDataSource {

	private SQLiteDatabase database;
	private ContactDBHelper dbHelper;

	public ChoreDataSource(Context context) {
		dbHelper = new ContactDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean insertChore(Chores c)
	{
		boolean didSucceed = false;
		try {
			ContentValues initialValues = new ContentValues();

			initialValues.put("chore", c.getChore());

			initialValues.put("frequency", c.getFrequency());

			initialValues.put("duration", String.valueOf(c.getDuration().toMillis(false)));

			didSucceed = database.insert("chores", null, initialValues) > 0;

		}
		catch (Exception e) {
			//Do Nothing -will return false if there is an exception
		}
		return didSucceed;
	}

	public boolean updateChore(Chores c)
	{
		boolean didSucceed = false;
		try {
			Long rowId = Long.valueOf(c.getChoreID());
			ContentValues updateValues = new ContentValues();

			updateValues.put("chore", c.getChore());

			updateValues.put("frequency", c.getFrequency());

			updateValues.put("duration", String.valueOf(c.getDuration().toMillis(false)));

			didSucceed = database.update("chores", updateValues, "_id=" + rowId, null) > 0;
		}
		catch (Exception e) {
			//Do Nothing -will return false if there is an exception
		}
		return didSucceed;
	}
	
	public int getLastChoreId() {
		int lastId = -1;
		try
		{       	
			String query = "Select MAX(_id) from chores";
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

	public ArrayList<String> getChore() {
		ArrayList<String> choreArrayList = new ArrayList<String>();
		try {
			String query = "Select chore from chores";
			Cursor cursor = database.rawQuery(query, null);  

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				choreArrayList.add(cursor.getString(1));
				cursor.moveToNext();
			} 
			cursor.close();
		}
		catch (Exception e) {
			choreArrayList = new ArrayList<String>();
		}
		return choreArrayList;
	}

	public ArrayList<Chores> getChores(String sortField, String sortOrder) {
		ArrayList<Chores> chores = new ArrayList<Chores>();
		try {
			String query = "SELECT  * FROM chores ORDER BY " + sortField + " " + sortOrder;
			Cursor cursor = database.rawQuery(query, null);

			Chores newChore;
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				newChore = new Chores();
				newChore.setChoreID(cursor.getInt(0));
				newChore.setChore(cursor.getString(1));
				newChore.setFrequency(cursor.getString(2));

				Time t = new Time();
				t.set(Long.valueOf(cursor.getString(3)));
				newChore.setDuration(t);

				chores.add(newChore);
				cursor.moveToNext();

			}
			cursor.close();
		}
		catch (Exception e) {
			chores = new ArrayList<Chores>();
		}
		return chores;
	}

	public Chores getSpecificChore(int choreId) {
		Chores chore = new Chores();

			String query = "SELECT  * FROM chores WHERE _id =" + choreId;
			Cursor cursor = database.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			chore.setChoreID(cursor.getInt(0));
			chore.setChore(cursor.getString(1));

			chore.setFrequency(cursor.getString(2));

			Time t = new Time();
			t.set(Long.valueOf(cursor.getString(3)));
			chore.setDuration(t);
		}
		cursor.close();
			
		return chore;
	}

	public boolean deleteChore(int choreId) {
		boolean didDelete = false;
		try {
			didDelete = database.delete("chores", "_id=" + choreId, null) > 0;
		}
		catch (Exception e) {
			//Do nothing -return value already set to false
		}
		return didDelete;
	}
}
