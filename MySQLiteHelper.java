/*----------------- Remind-Me : Android Gesture-based To-do application ------------------
 * 	Class Description.: INTERNAL USE: Database creation and upgradation class.. 
 * 	Author : Saurav Majumder
 * 	Last Modified : December 2, 2013
 ----------------------------------------------------------------------------------------*/
package com.example.remindme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_TODO = "todo_list";    // Specify table name.
	public static final String COLUMN_ID = "_id";			// Specify table column name: id (PRIMARY KEY).
	public static final String COLUMN_TASK = "task_title";		// Specify table column name: task (TEXT)
	public static final String COLUMN_DESC = "task_description";
	public static final String COLUMN_COMPLETED = "task_completed";
	public static final String COLUMN_DATE = "task_date";
	public static final String COLUMN_CATEGORY = "task_category";
	public static final String COLUMN_PRIORITY = "task_priority";

	private static final String DATABASE_NAME = "todo.db";	// Specify database name.
	private static final int DATABASE_VERSION = 1;			// Specify database version. (Default)
	
	/* Database creation SQL query	*/
	private static final String DATABASE_CREATE = "create table "
		      + TABLE_TODO + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + COLUMN_TASK
		      + " text not null, " + COLUMN_DESC
		      + " text, " + COLUMN_DATE
		      + " text, " + COLUMN_CATEGORY
		      + " text, " + COLUMN_PRIORITY
		      + " integer, " + COLUMN_COMPLETED
		      + " integer);";
	
	/* INTERNAL USE */
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* Create database if not exist on start up */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); 					// Creating database.
	}
	
	/* Upgrade database if not latest */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
	    onCreate(db);
	}
}
