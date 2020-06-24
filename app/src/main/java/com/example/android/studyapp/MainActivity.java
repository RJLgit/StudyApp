package com.example.android.studyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.studyapp.data.Task;
import com.example.android.studyapp.data.TaskViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, TaskAdapter.OnActionButtonPressed {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    String[] categoriesPreference;
    String sortPreference;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle("Manage your study tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bottomNavigationView = findViewById(R.id.bottom_navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        floatingActionButton = findViewById(R.id.add_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, new TasksFragment()).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (id) {
            case R.id.menu_todo:
                transaction.replace(R.id.fragment_container, new TasksFragment()).commit();
                break;
            case R.id.menu_done:
                transaction.replace(R.id.fragment_container, new CompletedFragment()).commit();
                break;
            case R.id.menu_postponed:
                transaction.replace(R.id.fragment_container, new PostponedFragment()).commit();
                break;
            default:
                transaction.replace(R.id.fragment_container, new TasksFragment()).commit();
                break;
        }

        return true;
    }

    @Override
    public void actionButtonPressed(Task task, String s) {
        TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        if (s.equals("Set as completed")) {
            Log.d(TAG, "actionButtonPressed: completed");
            task.setCompleted(true);
            Log.d(TAG, "actionButtonPressed: completed" + task.isCompleted());
            viewModel.updateTask(task);
        } else if (s.equals("Set as uncompleted")) {
            task.setCompleted(false);
            viewModel.updateTask(task);
        } else if (s.equals("Set as postponed")) {
            task.setPostponed(true);
            viewModel.updateTask(task);
        } else if (s.equals("Delete task")) {
            viewModel.deleteTask(task);
        } else if (s.equals("Set as active")) {
            task.setPostponed(false);
            viewModel.updateTask(task);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}