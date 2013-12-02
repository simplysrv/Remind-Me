/*
 * Model class for SQLlite database table.
 * Author: Saurav Majumder
 */
package com.example.remindme;

public class todo {
	private long id; // Corresponds to id field in the database.
	private String task; // Corresponds to task field in the database.
	private String desc;
	private int completed = 0;
	private String date;
	private String category;
	private int priority = 0;
	private boolean selected; // Keep track of completed/not completed.

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	public String getDescription() {
		return desc;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}
	
	public boolean isCompleted() {
		if(this.completed == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return task;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
