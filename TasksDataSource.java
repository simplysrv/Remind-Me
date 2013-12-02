/*
 * Data source class to communicate with the database.
 * Author: Saurav Majumder
 */
package com.example.remindme;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class TasksDataSource {
	// Database fields
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { 	MySQLiteHelper.COLUMN_ID, 
			  						  	MySQLiteHelper.COLUMN_TASK,
				  						MySQLiteHelper.COLUMN_DESC,
				  						MySQLiteHelper.COLUMN_DATE,
				  						MySQLiteHelper.COLUMN_CATEGORY,
				  						MySQLiteHelper.COLUMN_PRIORITY,
				  						MySQLiteHelper.COLUMN_COMPLETED,
		  						  	};
	
	  /* INTERNAL USE */
	  public TasksDataSource(Context context) {
	    dbHelper = new MySQLiteHelper(context);
	  }
	
	  /* Create database connection. */
	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }
	
	  /* Close database connection. */
	  public void close() {
	    dbHelper.close();
	  }
	  
	  /* Method for submitting new task into the database. */
	  public todo createTask(String task, String desc, String date, String category, Integer priority) {
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_TASK, task);
	    values.put(MySQLiteHelper.COLUMN_DESC, desc);
	    values.put(MySQLiteHelper.COLUMN_DATE, date);
	    values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
	    values.put(MySQLiteHelper.COLUMN_PRIORITY, priority);
	    values.put(MySQLiteHelper.COLUMN_COMPLETED, 0);
	    long insertId = database.insert(MySQLiteHelper.TABLE_TODO, null, values);
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODO, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
	    cursor.moveToFirst();
	    todo newTask = cursorToTask(cursor);
	    cursor.close();
	    return newTask;
	  }
	  
	  public void updateCompleted(String Task, Integer completed) {
		  this.open();
		  ContentValues values = new ContentValues();
		  values.put(MySQLiteHelper.COLUMN_COMPLETED, completed);    
		  database.update(MySQLiteHelper.TABLE_TODO, values, MySQLiteHelper.COLUMN_TASK+"=\""+Task+"\"",null);
		  this.close();
	  }
	  
	  public todo getTask(String Task) {
		  this.open();
		  todo task = new todo();
		  Cursor cursor = database.query(MySQLiteHelper.TABLE_TODO, allColumns, MySQLiteHelper.COLUMN_TASK+"=\""+Task+"\"", null, null, null, null);
		  cursor.moveToFirst();
		  this.close();
		  return cursorToTask(cursor);
	  }
	
	  /* Method for deleting old task from the database. */
	  public void deleteTask(String Task) {
		  this.open();
		  System.out.println("Deleting task: "+MySQLiteHelper.COLUMN_TASK+"=\""+Task+"\"");
		  database.delete(MySQLiteHelper.TABLE_TODO, MySQLiteHelper.COLUMN_TASK+"=\""+Task+"\"", null);
		  this.close();
	  }
	  
	  /* Fetch all tasks from the database to display in the homepage. */
	  public List<todo> getAllTask() {
	    List<todo> taskList = new ArrayList<todo>();
	
	    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODO, allColumns, null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      todo task = cursorToTask(cursor);
	      taskList.add(task);
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return taskList;
	  }
	  
	  public List<todo> getAllTaskByCategory(String category) {
		    List<todo> taskList = new ArrayList<todo>();
		
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_TODO, allColumns, MySQLiteHelper.COLUMN_CATEGORY+"=\""+category+"\"", null, null, null, null);
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      todo task = cursorToTask(cursor);
		      taskList.add(task);
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return taskList;
		  }
	  
	  /* INTERNAL USE */
	  private todo cursorToTask(Cursor cursor) {
	    todo todo_cr = new todo();
	    todo_cr.setId(cursor.getLong(0));
	    todo_cr.setTask(cursor.getString(1));
	    todo_cr.setDescription(cursor.getString(2));
	    todo_cr.setDate(cursor.getString(3));
	    todo_cr.setCategory(cursor.getString(4));
	    todo_cr.setPriority(cursor.getInt(5));
	    todo_cr.setCompleted(cursor.getInt(6));
	    return todo_cr;
	  }
}
