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
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, TaskAdapter.OnActionButtonPressed, TaskAdapter.OnTaskClicked {
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
        toolbar.setSubtitle("Click task to see more details");
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
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
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
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.fragment_container, new TasksFragment()).commit();
                break;
            case R.id.menu_done:
                if (findViewById(R.id.tasks_rv) == null) {
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);

                } else {
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
                }
                transaction.replace(R.id.fragment_container, new CompletedFragment()).commit();
                break;
            case R.id.menu_postponed:
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.fragment_container, new PostponedFragment()).commit();
                break;
            default:
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.fragment_container, new TasksFragment()).commit();
                break;
        }

        return true;
    }

    @Override
    public void actionButtonPressed(final Task task, String s, TaskAdapter.TaskViewHolder taskViewHolder) {
        final TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        if (s.equals("Set as completed")) {
            Log.d(TAG, "actionButtonPressed: completed");
            task.setCompleted(true);
            Log.d(TAG, "actionButtonPressed: completed" + task.isCompleted());
            viewModel.updateTask(task);
            Snackbar.make(taskViewHolder.itemView, "Task updated", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setCompleted(false);
                            viewModel.updateTask(task);
                        }
                    }).show();
        } else if (s.equals("Set as uncompleted")) {
            task.setCompleted(false);
            viewModel.updateTask(task);
            Snackbar.make(taskViewHolder.itemView, "Task updated", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setCompleted(true);
                            viewModel.updateTask(task);
                        }
                    }).show();
        } else if (s.equals("Set as postponed")) {
            task.setPostponed(true);
            viewModel.updateTask(task);
            Snackbar.make(taskViewHolder.itemView, "Task updated", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setPostponed(false);
                            viewModel.updateTask(task);
                        }
                    }).show();
        } else if (s.equals("Delete task")) {
            viewModel.deleteTask(task);
            Snackbar.make(taskViewHolder.itemView, "Task deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewModel.insertTask(task);
                        }
                    }).show();
        } else if (s.equals("Set as active")) {
            task.setPostponed(false);
            viewModel.updateTask(task);
            Snackbar.make(taskViewHolder.itemView, "Task updated", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setPostponed(true);
                            viewModel.updateTask(task);
                        }
                    }).show();
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
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void taskClicked(Task task) {
        Intent intent = new Intent(this, TaskDetails.class);
        intent.putExtra("Task clicked", task);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.tasks_rv) == null) {
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        } else {
            super.onBackPressed();
        }
    }
}