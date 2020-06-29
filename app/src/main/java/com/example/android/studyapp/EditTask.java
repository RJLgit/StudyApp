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

    Toolbar toolbar;
    Button editButton;
    NumberPicker numberPicker;
    Spinner spinner;
    EditText titleEditText;
    EditText descriptionEditText;
    private static final String TAG = "EditTask";
    Task myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("Edit task");

        final TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        myTask = (Task) getIntent().getSerializableExtra("Task clicked");



        numberPicker = findViewById(R.id.priorityNumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);
        numberPicker.setValue(myTask.getPriority());


        spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(myTask.getCategory());
        spinner.setSelection(spinnerPosition);


        titleEditText = findViewById(R.id.edit_text_title);
        titleEditText.setText(myTask.getTitle());
        descriptionEditText = findViewById(R.id.edit_text_description);
        descriptionEditText.setText(myTask.getDescription());


        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidEntry()) {
                    Log.d(TAG, "onClick: " + spinner.getSelectedItem().toString());
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    int priority = numberPicker.getValue();
                    String category = spinner.getSelectedItem().toString();
                    myTask.setTitle(title);
                    myTask.setDescription(description);
                    myTask.setPriority(priority);
                    myTask.setCategory(category);

                    viewModel.updateTask(myTask);
                } else {
                    Toast.makeText(EditTask.this, "Please enter text for both the title and description", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private boolean checkValidEntry() {
        if (titleEditText.getText().toString().trim().equals("") ||
                descriptionEditText.getText().toString().trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditTask.this, TaskDetails.class);
        intent.putExtra("Task clicked", myTask);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }
}