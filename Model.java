/*----------------- Remind-Me : Android Gesture-based To-do application ------------------
 * 	Class Description.: Database model class. 
 * 	Author : Saurav Majumder
 * 	Last Modified : December 2, 2013
 ----------------------------------------------------------------------------------------*/
package com.example.remindme;

public class Model {

	  private String name;
	  private boolean selected;

	  public Model(String name) {
	    this.name = name;
	    selected = false;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public boolean isSelected() {
	    return selected;
	  }

	  public void setSelected(boolean selected) {
	    this.selected = selected;
	  }

} 
