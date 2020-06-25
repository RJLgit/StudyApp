package com.example.android.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.studyapp.data.Task;

public class TaskDetails extends AppCompatActivity {
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

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Set title of task here");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Task myTask = (Task) getIntent().getSerializableExtra("Task clicked");

        title.setText(myTask.getTitle());
        date.setText("Date Added: " + myTask.getTimeAdded());
        priority.setText("Priority: " + myTask.getPriority());
        category.setText("Category: " + myTask.getCategory());
        description.setText(myTask.getDescription());
        if (myTask.isPostponed()) {
            status.setText("Status: postponed");
        } else {
            if (myTask.isCompleted()) {
                status.setText("Status: completed");
            } else {
                status.setText("Status: not completed");
            }
        }





    }
}