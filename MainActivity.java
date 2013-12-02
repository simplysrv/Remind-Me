/*----------------- Remind-Me : Android Gesture-based To-do application ------------------
 * 	Class Description.: Main activity class which launches the home screen ofthe application. 
 * 	Author : Saurav Majumder
 * 	Last Modified : December 2, 2013
 ----------------------------------------------------------------------------------------*/
package com.example.remindme;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureOverlayView;
import android.widget.AdapterView.OnItemClickListener;

import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;

public class MainActivity extends Activity implements OnGesturePerformedListener, unknownGestureFragement.NoticeDialogListener{
	public final static int ACTION_CODE = 1;
	protected static final String EXTRA_MESSAGE = "";
	protected int unknownGestureCounter = 0;
	
	private TasksDataSource datasource;			// Data source connection variable.
	private GestureLibrary gestureLibrary;

	/* Method that handles activities on initialization */
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	 
	   final ListView listView = (ListView) findViewById(R.id.listview);	// Instance variable for list view.
	    
	   datasource = new TasksDataSource(this);
	   datasource.open();
	   
	   //Get gesture library from res/raw/gestures
		gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		
		//Load the gesture library
		gestureLibrary.load();
		
		//Get the GestureOverlayView
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestureOverlay);
		//Add the Listener for when a gesture is performed
		gestures.addOnGesturePerformedListener(this);
	   	List<todo> tasks = datasource.getAllTask();	// Fetch all task from the database.
	   	
	    ArrayAdapter<todo> adapter = new InteractiveArrayAdapter(this,tasks);	// Create array adapter for display in list.
	    listView.setAdapter(adapter);											// Connect to list view.
	    
    	listView.setTextFilterEnabled(true);
	    
	    /* Task List on-click listener */
	    listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
				/* Creating intent to start task details activity */
				Intent intent = new Intent(listView.getContext(), ShowTask.class);
				String message=parent.getAdapter().getItem(position).toString();	// Select the task to be displayed.
			
				intent.putExtra(EXTRA_MESSAGE, message);
				startActivity(intent);	// Start task details activity.
			}
		});	
	}
	
	/* Method to handle item selection in menu */
	@Override
    public void onSelectItem(int position) {
		final ListView listView = (ListView) findViewById(R.id.listview);	
		List<todo> tasks;
		ArrayAdapter<todo> adapter;
		
		switch (position) {
	    case 0: // Start new task activity 
	    	unknownGestureCounter = 0;
			Intent intent = new Intent(this, EditTask.class);
		    startActivity(intent);;
		    break;
		    
	    case 1: // Filter tasks by Personal category.
	    	unknownGestureCounter = 0;
	    	tasks = datasource.getAllTaskByCategory("Personal");
			adapter = new InteractiveArrayAdapter(this,tasks);
			listView.setAdapter(adapter);
		    break;
		    
	    case 2:	// Filter tasks by Work category.
	    	unknownGestureCounter = 0;
	    	tasks = datasource.getAllTaskByCategory("Work");
			adapter = new InteractiveArrayAdapter(this,tasks);
			listView.setAdapter(adapter);
		    break;
		    
	    case 3: // Snooze alarm for 10 minutes.
	    	unknownGestureCounter = 0;
	    	Toast.makeText(this, "Alarm Snooze for 10 minutes", Toast.LENGTH_SHORT).show();
		    break;
		}
    }
	
	/* Method to handle performed gesture */
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		final ListView listView = (ListView) findViewById(R.id.listview);	
		List<todo> tasks;
		ArrayAdapter<todo> adapter;

		ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);		

		if (predictions.size() > 0) {			
			Prediction prediction = predictions.get(0);
			if (prediction.score > 1.0) {
				if(prediction.name.equals("add_00_cw") || prediction.name.equals("add_01_cw") || prediction.name.equals("add_10_cw") || 
						prediction.name.equals("add_11_cw") || prediction.name.equals("add_00_acw") || prediction.name.equals("add_01_acw") || 
						prediction.name.equals("add_10_acw") || prediction.name.equals("add_11_acw")) {	// Gesture for adding new task
					unknownGestureCounter = 0;
					Intent intent = new Intent(this, EditTask.class);
				    startActivity(intent);
				} else if(prediction.name.equals("personal_1") || prediction.name.equals("personal_2")) { // Gesture for filtering with Personal.
					Toast.makeText(this, "Personal Tasks", Toast.LENGTH_SHORT).show();
					tasks = datasource.getAllTaskByCategory("Personal");
					adapter = new InteractiveArrayAdapter(this,tasks);
					listView.setAdapter(adapter);
					unknownGestureCounter = 0;
				} else if(prediction.name.equals("work_1") || prediction.name.equals("work_2")) { // Gesture for filtering with Work.
					Toast.makeText(this, "Work Tasks", Toast.LENGTH_SHORT).show();
					tasks = datasource.getAllTaskByCategory("Work");
					adapter = new InteractiveArrayAdapter(this,tasks);
					listView.setAdapter(adapter);
					unknownGestureCounter = 0;
				} else if(prediction.name.equals("snooze_1") || prediction.name.equals("snooze_2")) { // Gesture for snoozing alarm.
					Toast.makeText(this, "Alarm Snooze for 10 minutes", Toast.LENGTH_SHORT).show();
					unknownGestureCounter = 0;
				} else {
					Toast.makeText(this, "Cannot recognize. Please try again.", Toast.LENGTH_SHORT).show();	// Handle unknown gesture.
					// Update task list with all the tasks.
					tasks = datasource.getAllTask();
			    	adapter = new InteractiveArrayAdapter(this,tasks);
			    	listView.setAdapter(adapter);
					unknownGestureCounter++;
			    	if(unknownGestureCounter >= 3) {
			    		DialogFragment dialog = new unknownGestureFragement();
			    		dialog.show(getFragmentManager(), "NoticeDialogFragment");
			    		unknownGestureCounter = 0;
			    	}
					
				}
			} else {
				Toast.makeText(this, "Cannot recognize. Please try again.", Toast.LENGTH_SHORT).show(); // Handle unknown gesture.
				// Update task list with all the tasks.
				tasks = datasource.getAllTask();
		    	adapter = new InteractiveArrayAdapter(this,tasks);
		    	listView.setAdapter(adapter);
				unknownGestureCounter++;
		    	if(unknownGestureCounter >= 3) {
		    		DialogFragment dialog = new unknownGestureFragement();
		    		dialog.show(getFragmentManager(), "NoticeDialogFragment");
		    		unknownGestureCounter = 0;
		    	}
			}
		}
		
	}
	
	
	/* INTERNAL USE: Create menu items */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* Event listerns for the menu items. */
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		final ListView listView = (ListView) findViewById(R.id.listview);	
		List<todo> tasks;
		ArrayAdapter<todo> adapter;
		
		switch (item.getItemId()) {
	    case R.id.add_new: // Add new task button
	    	Intent intent = new Intent(this, EditTask.class);
		    startActivity(intent);
		    break;
	      
	    case R.id.refresh: // Refresh tasks button
	      Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
		      
	      tasks = datasource.getAllTask();
	      adapter = new InteractiveArrayAdapter(this,tasks);
	      listView.setAdapter(adapter);
	      break;
		      
	    case R.id.action_settings: // Settings button
	      Toast.makeText(this, "Change settings", Toast.LENGTH_SHORT).show();
	      break;

	    default:
	      break;
	    }

	    return true;
	  }

  /* Activities to be performed when this activity resumes. */
  @Override
  protected void onResume() {
    datasource.open(); // Reopen datasource.
    super.onResume();
    
    final ListView listView = (ListView) findViewById(R.id.listview);	
	List<todo> tasks;
	ArrayAdapter<todo> adapter;
	
	// Update task list.
    tasks = datasource.getAllTask();
    adapter = new InteractiveArrayAdapter(this,tasks);
    listView.setAdapter(adapter);
  }

  /* Activities to be performed when activity pauses. */
  @Override
  protected void onPause() {
    datasource.close(); // Close datasource.
    super.onPause();
  }
  
  /* Open add new task activity */
  public void addNewTask(View view) {
	    Intent intent = new Intent(this, addTask.class);
	    startActivity(intent);
	}

}
