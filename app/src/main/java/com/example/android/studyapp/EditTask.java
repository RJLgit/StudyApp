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

public class EditTask extends AppCompatActivity {
    //UI variables
    Toolbar toolbar;
    Button editButton;
    NumberPicker numberPicker;
    Spinner spinner;
    EditText titleEditText;
    EditText descriptionEditText;
    //The task to be changed
    Task myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        //Sets up the toolbar
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle(R.string.edit_task_toolbar_subtitle);

        final TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        //Gets the task clicked from the task details activity
        myTask = (Task) getIntent().getSerializableExtra(getString(R.string.edit_task_intent_extra));

        //Sets up the number picker from 1-5 and sets initial value to that in the task object
        numberPicker = findViewById(R.id.priorityNumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);
        numberPicker.setValue(myTask.getPriority());
        //Sets up the category spinner and sets the initial value to be that of the task object
        spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(myTask.getCategory());
        spinner.setSelection(spinnerPosition);

        //Sets 2 edit texts to hold the initial value for the task title and description
        titleEditText = findViewById(R.id.edit_text_title);
        titleEditText.setText(myTask.getTitle());
        descriptionEditText = findViewById(R.id.edit_text_description);
        descriptionEditText.setText(myTask.getDescription());

        editButton = findViewById(R.id.editButton);
        //When the edit button is clicked the entry is checked for validity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidEntry()) {
                    //If valid then the task object is updated with the new information
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    int priority = numberPicker.getValue();
                    String category = spinner.getSelectedItem().toString();
                    myTask.setTitle(title);
                    myTask.setDescription(description);
                    myTask.setPriority(priority);
                    myTask.setCategory(category);
                    //Update task is called on the view model to change the task object in the database
                    viewModel.updateTask(myTask);
                } else {
                    //A toast message is shown if the entry isn't valid
                    Toast.makeText(EditTask.this, R.string.add_task_toast_not_valid, Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    //Checks valid and returns true if so. Is valid if there is a string for both the title and description
    private boolean checkValidEntry() {
        if (titleEditText.getText().toString().trim().equals("") ||
                descriptionEditText.getText().toString().trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }
    //When the back pressed button on the toolbar is pressed it takes the user back
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
    //Overrides on back pressed so it takes the user back to the task details page when the user clicks back
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditTask.this, TaskDetails.class);
        intent.putExtra(getString(R.string.edit_task_intent_extra), myTask);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}