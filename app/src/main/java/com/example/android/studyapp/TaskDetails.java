package com.example.android.studyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.studyapp.data.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskDetails extends AppCompatActivity {
    //UI variables
    Toolbar toolbar;
    TextView title, date, priority, category, description, status;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        toolbar = findViewById(R.id.my_toolbar);
        title = findViewById(R.id.titleTextView);
        date = findViewById(R.id.dateTextView);
        priority = findViewById(R.id.priorityTextView);
        category = findViewById(R.id.categoryTextView);
        description = findViewById(R.id.descriptionTextView);
        status = findViewById(R.id.currentStatusTextView);
        editButton = findViewById(R.id.editTaskButton);
        //Sets up the toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Gets the task object
        final Task myTask = (Task) getIntent().getSerializableExtra(getString(R.string.edit_task_intent_extra));
        //Sets the title text view to the title of the task
        title.setText(myTask.getTitle());
        //Gets the date in the correct format
        String dateString = new SimpleDateFormat(getString(R.string.task_details_date_format)).format(new Date(myTask.getTimeAdded()));
        //This sets all the UI elements to hold the information about the task
        date.setText(getString(R.string.task_details_date_added, dateString));
        priority.setText(getString(R.string.task_details_priority, myTask.getPriority()));
        category.setText(getString(R.string.task_details_category, myTask.getCategory()));
        description.setText(myTask.getDescription());
        if (myTask.isPostponed()) {
            status.setText(R.string.task_details_postponed_status);
        } else {
            if (myTask.isCompleted()) {
                status.setText(R.string.task_details_completed_status);
            } else {
                status.setText(R.string.task_details_not_completed_status);
            }
        }
        toolbar.setSubtitle(getString(R.string.task_details_toolbar_subtitle, myTask.getTitle()));
        //Loads the edit task activity when edit task button pressed, passed the task object to this activity as an extra
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetails.this, EditTask.class);
                intent.putExtra(getString(R.string.edit_task_intent_extra), myTask);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            }
        });

    }
    //Implements when the back button on the toolbar pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Overrides onbackpressed to load mainactivity when pressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}