/*----------------- Remind-Me : Android Gesture-based To-do application ------------------
 * 	Class Description.: Activity to add new task. 
 * 	Author : Saurav Majumder
 * 	Last Modified : December 2, 2013
 ----------------------------------------------------------------------------------------*/
package com.example.remindme;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.DatePicker;

import android.gesture.*;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;

public class EditTask extends Activity implements OnGesturePerformedListener{

	private EditText mTitleText;
    private EditText mBodyText;
    private DatePicker  mDate;
    private RatingBar mBar;
    private RadioGroup rg;

    private TasksDataSource datasource;
    private GestureLibrary gestureLibrary;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_task);
		
		mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mDate = (DatePicker ) findViewById(R.id.datePicker1);
        mBar = (RatingBar) findViewById(R.id.ratingbar);
        rg = (RadioGroup) findViewById(R.id.category);
        
        datasource = new TasksDataSource(this);
 	   	datasource.open();

		gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		
		gestureLibrary.load();
		
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestureOverlay);

		gestures.addOnGesturePerformedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.edit_task, menu);
		return true;
	}
	
	/** Called when the user clicks the Send button */
	public void saveTask(View view) {
		String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        int day = mDate.getDayOfMonth();
        int month = mDate.getMonth();
        int year = mDate.getYear();
        int rating = (int)mBar.getRating() - 1;
        String task_category = "Others";
        
        switch (rg.getCheckedRadioButtonId())
        {
        case R.id.radio_personal:
        task_category = "Personal";
        break;
         
        case R.id.radio_corporate:
    	task_category = "Work";
        break;
        
        case R.id.radio_others:
    	task_category = "Others";
        break;
        }
        
        String date = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year);
        
        
        if(title.isEmpty()) {
        	Toast.makeText(this, "Title field is empty", Toast.LENGTH_SHORT).show();
        	return;
        }
        datasource.createTask(title,body,date,task_category,rating);
		finish();
	}
	
	/* Method to handle gesture events */
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

		ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);		

		if (predictions.size() > 0) {			

			Prediction prediction = predictions.get(0);

			if (prediction.score > 1.0) {
				if(prediction.name.equals("tick") || prediction.name.equals("tick_opp")) {	// Tick gesture to submit task.
					String title = mTitleText.getText().toString();
			        String body = mBodyText.getText().toString();
			        int day = mDate.getDayOfMonth();
			        int month = mDate.getMonth();
			        int year = mDate.getYear();
			        int rating = (int)mBar.getRating() - 1;
			        String date = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year);
			        
			        String task_category = "Others";
			        
			        switch (rg.getCheckedRadioButtonId())
			        {
			        case R.id.radio_personal:
			        task_category = "Personal";
			        break;
			         
			        case R.id.radio_corporate:
			    	task_category = "Work";
			        break;
			        
			        case R.id.radio_others:
			    	task_category = "Others";
			        break;
			        }
			        
			        if(title.isEmpty()) {
			        	Toast.makeText(this, "Title field is empty", Toast.LENGTH_SHORT).show();
			        	return;
			        }
			        
			        datasource.createTask(title,body,date,task_category,rating);
					finish();
				}  else if(prediction.name.equals("delete_ltr") || prediction.name.equals("delete_rtl")) {	// Delete task.
					finish();
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
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
}
