package com.example.nomorenagginginator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDBHelper extends SQLiteOpenHelper {
	
  private static final String DATABASE_NAME = "KidsChores.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String CREATE_TABLE_CHILDREN =
          "create table children (_id integer primary key autoincrement, "
                  + "contactname text not null,"
                  + "cellnumber text, "
                  + "birthday text);";
	private static final String CREATE_TABLE_CHORE =
			"create table chores (_id integer primary key autoincrement, "
					+ "chore text not null,"
					+ "duration text, "
					+ "assignedTo text, "
					+ "frequency text);";

	  public ContactDBHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
		    database.execSQL(CREATE_TABLE_CHILDREN);
		  database.execSQL(CREATE_TABLE_CHORE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(ContactDBHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS children");
	    onCreate(db);
	  }


}
