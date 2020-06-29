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
import android.widget.Toast;

import com.example.android.studyapp.data.Task;
import com.example.android.studyapp.data.TaskViewModel;

public class AddTask extends AppCompatActivity {
    //UI elements
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
        //Sets up the toolbar
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle(R.string.add_task_toolbar_subtitle);
        //Gets the Viewmodel instance
        final TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        //Sets the number picker to take values between 1 and 5
        numberPicker = findViewById(R.id.priorityNumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);
        //Sets up the spinner with the categories array of values
        spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        titleEditText = findViewById(R.id.edit_text_title);
        descriptionEditText = findViewById(R.id.edit_text_description);
        addButton = findViewById(R.id.addTaskButton);
        //When the add button is pressed the entries are checked for validity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + spinner.getSelectedItem().toString());
                if (checkValidEntry()) {
                    //If the entry is valid then a new task object is created from the data entered
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    int priority = numberPicker.getValue();
                    String category = spinner.getSelectedItem().toString();
                    boolean completed = false;
                    boolean postponed = false;
                    long time = System.currentTimeMillis();
                    Task task = new Task(title, description, priority, category, completed, time, postponed);
                    //This task object is inserted into the database.
                    viewModel.insertTask(task);
                    //The edit texts are cleared for the next entry
                    titleEditText.getText().clear();
                    descriptionEditText.getText().clear();
                } else {
                    //A toast is shown if the entry is not valid
                    Toast.makeText(AddTask.this, R.string.add_task_toast_not_valid, Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    //This helper method checks whether the user has entered a title and description
    //If they have then true is returned, otherwise it returns false.
    private boolean checkValidEntry() {
        if (titleEditText.getText().toString().trim().equals("") ||
                descriptionEditText.getText().toString().trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }
    //Implements the back button on the toolbar functionality.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Implements on back pressed functionality to use the animations
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
}