package com.example.android.studyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class AddTask extends AppCompatActivity {
    Toolbar toolbar;
    Button addButton;
    NumberPicker numberPicker;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("Add a task");

        addButton = findViewById(R.id.addTaskButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        numberPicker = findViewById(R.id.priorityNumberPicker);
        spinner = findViewById(R.id.categorySpinner);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);

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