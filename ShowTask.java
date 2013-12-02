/*----------------- Remind-Me : Android Gesture-based To-do application ------------------
 * 	Class Description.: Class to display the details of a task. 
 * 	Author : Saurav Majumder
 * 	Last Modified : December 2, 2013
 ----------------------------------------------------------------------------------------*/
package com.example.remindme;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.gesture.*;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Color;

public class ShowTask extends Activity  implements OnGesturePerformedListener, unknownGestureFragementViewTask.NoticeDialogListener {
	private GestureLibrary gestureLibrary;
	private TasksDataSource datasource;	
	private int unknownGestureCounter = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_task);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE).trim();
		
		datasource = new TasksDataSource(this);
		todo task = datasource.getTask(message);
		// Create the text view
		TextView title = (TextView) findViewById(R.id.showTaskTitle);
		TextView desc = (TextView) findViewById(R.id.desc);
		TextView date = (TextView) findViewById(R.id.date);
		ImageView imgView = (ImageView)findViewById(R.id.done);
		ImageView priority = (ImageView)findViewById(R.id.priority);
		
		int prior = task.getPriority();
		if(prior <= 0) {
			priority.setImageResource(R.drawable.ic_action_not_important); 
	    } else if (prior == 1) {
	    	priority.setImageResource(R.drawable.ic_action_half_important);
	    } else {
	    	priority.setImageResource(R.drawable.ic_action_important);
	    }
		
		title.setText(task.getTask());
		desc.setText(task.getDescription());
		date.setText(task.getDate());
		
		if(task.isCompleted()){
			imgView.setVisibility(View.VISIBLE);
		}
		
		//Get gesture library from res/raw/gestures
		gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		
		gestureLibrary.load();
		
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestureOverlay);

		gestures.addOnGesturePerformedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.show_task, menu);
		return true;
	}
	
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

		ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);		

		if (predictions.size() > 0) {			

			Prediction prediction = predictions.get(0);

			if (prediction.score > 1.0) {
				datasource = new TasksDataSource(this);
				if(prediction.name.equals("tick") || prediction.name.equals("tick_opp")) {	
					unknownGestureCounter = 0;
					TextView textView = (TextView) findViewById(R.id.showTaskTitle);
					ImageView imgView = (ImageView)findViewById(R.id.done);
					String old_title = textView.getText().toString();
					datasource.updateCompleted(old_title, 1);
					imgView.setVisibility(View.VISIBLE);
					Toast.makeText(this, "Task Completed Successfully!", Toast.LENGTH_SHORT).show();

				} else if(prediction.name.equals("undo_cw_l") || prediction.name.equals("undo_cw_t") || prediction.name.equals("undo_cw_b") || prediction.name.equals("undo_cw_r")
						|| prediction.name.equals("undo_acw_l") || prediction.name.equals("undo_acw_t") || prediction.name.equals("undo_acw_b") || prediction.name.equals("undo_acw_r")) {	
					unknownGestureCounter = 0;
					TextView textView = (TextView) findViewById(R.id.showTaskTitle);
					ImageView imgView = (ImageView)findViewById(R.id.done);
					String old_title = textView.getText().toString();
					textView.setTextColor(Color.BLACK);
					datasource.updateCompleted(old_title, 0);
					imgView.setVisibility(View.INVISIBLE);
					Toast.makeText(this, "Task not completed!", Toast.LENGTH_SHORT).show();

				} else if(prediction.name.equals("delete_ltr") || prediction.name.equals("delete_rtl")) {	
					unknownGestureCounter = 0;
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

						alertDialogBuilder.setTitle("Delete Task");
			 
						alertDialogBuilder.setMessage("Do you really want to delete the task?").setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {	
								public void onClick(DialogInterface dialog,int id) {
									TextView textView = (TextView) findViewById(R.id.showTaskTitle);
									String old_title = textView.getText().toString().trim();
									datasource.deleteTask(old_title);
									finish();
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});			 
							AlertDialog alertDialog = alertDialogBuilder.create();
							alertDialog.show();
				} else {
					Toast.makeText(this, "Cannot recognize. Please try again.", Toast.LENGTH_SHORT).show();
					unknownGestureCounter++;
			    	if(unknownGestureCounter >= 3) {
			    		DialogFragment dialog = new unknownGestureFragementViewTask();
			    		dialog.show(getFragmentManager(), "unknownGestureFragementViewTask");
			    		unknownGestureCounter = 0;
			    	}
				}
			} else {
				Toast.makeText(this, "Cannot recognize. Please try again.", Toast.LENGTH_SHORT).show();
				unknownGestureCounter++;
		    	if(unknownGestureCounter >= 3) {
		    		DialogFragment dialog = new unknownGestureFragementViewTask();
		    		dialog.show(getFragmentManager(), "unknownGestureFragementViewTask");
		    		unknownGestureCounter = 0;
		    	}
			}
		}
		
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	    case R.id.back:
	    	finish();
		    break;

	    default:
	      break;
	    }

	    return true;
	  }
	
	@Override
    public void onSelectItem(int position) {
		TextView textView;
		ImageView imgView;
		String old_title;
		
		switch (position) {
	    case 0:
	    	unknownGestureCounter = 0;
			textView = (TextView) findViewById(R.id.showTaskTitle);
			imgView = (ImageView)findViewById(R.id.done);
			old_title = textView.getText().toString();
			datasource.updateCompleted(old_title, 1);
			imgView.setVisibility(View.VISIBLE);
			Toast.makeText(this, "Task Completed Successfully!", Toast.LENGTH_SHORT).show();
		    break;
		    
	    case 1:
	    	unknownGestureCounter = 0;
			textView = (TextView) findViewById(R.id.showTaskTitle);
			imgView = (ImageView)findViewById(R.id.done);
			old_title = textView.getText().toString();
			textView.setTextColor(Color.BLACK);
			datasource.updateCompleted(old_title, 0);
			imgView.setVisibility(View.INVISIBLE);
			Toast.makeText(this, "Task not completed!", Toast.LENGTH_SHORT).show();
		    break;
		    
	    case 2:
	    	unknownGestureCounter = 0;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

				alertDialogBuilder.setTitle("Delete Task");

				alertDialogBuilder.setMessage("Do you really want to delete the task?").setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {	
						public void onClick(DialogInterface dialog,int id) {
							TextView textView = (TextView) findViewById(R.id.showTaskTitle);
							String old_title = textView.getText().toString().trim();
							datasource.deleteTask(old_title);
							finish();
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
		    break;
		}
    }

}
