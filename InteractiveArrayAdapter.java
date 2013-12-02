/*----------------- Remind-Me : Android Gesture-based To-do application ------------------
 * 	Class Description.: Custom adapter class for the task list in homepage. 
 * 	Author : Saurav Majumder
 * 	Last Modified : December 2, 2013
 ----------------------------------------------------------------------------------------*/
package com.example.remindme;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InteractiveArrayAdapter extends ArrayAdapter<todo> {

  private final List<todo> list;
  private final Activity context;
  private TasksDataSource datasource;

  public InteractiveArrayAdapter(Activity context, List<todo> list) {
    super(context, R.layout.rowbuttonlayout, list);
    this.context = context;
    this.list = list;
  }

  static class ViewHolder {
	// Items to be displayed in a list element.
    protected TextView text;		// Title of the task.
    protected TextView category;	// Category of the task.
    protected TextView date;		// Date of the task.
    protected CheckBox checkbox;	// Check-box to complete the task.
    protected ImageView mImageView;	// Image to denote priority of the task.
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = null;
    if (convertView == null) {
      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.rowbuttonlayout, null);
      
      final ViewHolder viewHolder = new ViewHolder();
      viewHolder.text = (TextView) view.findViewById(R.id.label);
      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
      viewHolder.mImageView = (ImageView) view.findViewById(R.id.icon);
      viewHolder.category = (TextView) view.findViewById(R.id.category);
      viewHolder.date = (TextView) view.findViewById(R.id.date);
      
      // Listener for the checkbox to check if the task is completed.
      viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              todo element = (todo) viewHolder.checkbox.getTag();
              element.setSelected(buttonView.isChecked());
            }
      });
      
      view.setTag(viewHolder);
      viewHolder.checkbox.setTag(list.get(position));
    } else {
      view = convertView;
      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
    }
    
    final ViewHolder holder = (ViewHolder) view.getTag();
    holder.text.setText(list.get(position).getTask());
    holder.category.setText(list.get(position).getCategory());
    holder.date.setText(list.get(position).getDate());
    holder.checkbox.setChecked(list.get(position).isCompleted());
    
    // Display Completed once the checkbox has been marked.
    if(list.get(position).isCompleted()) {
    	holder.text.setText(holder.text.getText() + " (Completed)");
    	holder.text.setTextColor(Color.parseColor("#CCCCCC"));
    	holder.category.setTextColor(Color.parseColor("#CCCCCC"));
    }
    
    // Set priority image as per the priority of the task.
    int priority = list.get(position).getPriority();
    if(priority <= 0) {
    	holder.mImageView.setImageResource(R.drawable.ic_action_not_important); 
    } else if (priority == 1) {
    	holder.mImageView.setImageResource(R.drawable.ic_action_half_important);
    } else {
    	holder.mImageView.setImageResource(R.drawable.ic_action_important);
    }
    
    // Method to handle change in the checkbox event. */
    holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        	datasource = new TasksDataSource(context);
          	if(buttonView.isChecked()){
	    	  String set_task = holder.text.getText().toString();
	    	  holder.text.setText(set_task + " (Completed)");
	    	  holder.text.setTextColor(Color.parseColor("#CCCCCC"));
	    	  holder.category.setTextColor(Color.parseColor("#CCCCCC"));
	    	  datasource.updateCompleted(set_task, 1);
          	} else {
        	  String existing_label = holder.text.getText().toString();
        	  String new_label = existing_label.replace(" (Completed)","").trim();
        	  holder.text.setText(new_label);
        	  holder.text.setTextColor(Color.BLACK);
        	  holder.category.setTextColor(Color.BLACK);
        	  datasource.updateCompleted(new_label, 0);
          	}
        }
  });
    return view;
  }
}
