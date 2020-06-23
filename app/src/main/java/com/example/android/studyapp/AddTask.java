package com.example.android.studyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.android.studyapp.data.Task;
import com.example.android.studyapp.data.TaskViewModel;

public class AddTask extends AppCompatActivity {
    Toolbar toolbar;
    Button addButton;
    NumberPicker numberPicker;
    Spinner spinner;
    EditText titleEditText;
    EditText descriptionEditText;
    private static final String TAG = "AddTask";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("Add a task");

        final TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);




        numberPicker = findViewById(R.id.priorityNumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);


        spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        titleEditText = findViewById(R.id.edit_text_title);
        descriptionEditText = findViewById(R.id.edit_text_description);

        addButton = findViewById(R.id.addTaskButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + spinner.getSelectedItem().toString());
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                int priority = numberPicker.getValue();
                String category = spinner.getSelectedItem().toString();
                boolean completed = false;
                boolean postponed = false;
                long time = System.currentTimeMillis();
                Task task = new Task(title, description, priority, category, completed, time, postponed);
                viewModel.insertTask(task);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}