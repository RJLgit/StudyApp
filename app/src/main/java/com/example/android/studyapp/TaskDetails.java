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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Task myTask = (Task) getIntent().getSerializableExtra(getString(R.string.edit_task_intent_extra));

        title.setText(myTask.getTitle());
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(myTask.getTimeAdded()));
        date.setText("Date Added: " + dateString);
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
        toolbar.setSubtitle("Task: " + myTask.getTitle());

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}