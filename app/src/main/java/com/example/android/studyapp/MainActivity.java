package com.example.android.studyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
    //Ui variables
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Sets up the toolbar
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setSubtitle(R.string.main_activity_toolbar_subtitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Sets up the bottom nav view and sets the listener to the main activity
        bottomNavigationView = findViewById(R.id.bottom_navbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //Sets up the floating action button which loads the add task activity when it is clicked
        floatingActionButton = findViewById(R.id.add_task);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            }
        });
        //If there is not a saved instance state then the taskfragment is loaded into the fragment container. This is only done when no saved instance state
        //This means that when the phone is rotated, and this oncreate method is called, that the phone will not load this fragment and will instead continue to show
        //the fragment that was showing before the phone was rotated
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.fragment_container, new TasksFragment()).commit();
        }
    }
    //Method which handles when the navigation view items are clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //The correct fragment is loaded when each menu item is clicked
        switch (id) {
            case R.id.menu_todo:
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.fragment_container, new TasksFragment()).commit();
                break;
            case R.id.menu_done:
                //Checks to see from which fragment this fragment is loaded and uses the correct animation based on each case
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
    //Interface method which is called when the action button in the viewholder of the RV is pressed
    @Override
    public void actionButtonPressed(final Task task, String s, TaskAdapter.TaskViewHolder taskViewHolder) {
        final TaskViewModel viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        //Looks at which action was selected by the user and uses this to change the task in the selected way, and then to update the database with the changed task
        if (s.equals(getString(R.string.action_set_completed))) {
            //The task passed to this method is changed to be completed
            task.setCompleted(true);
            //The tasks is updated in the database
            viewModel.updateTask(task);
            //Snackbar is shown to allow the user to undo this action
            Snackbar.make(taskViewHolder.itemView, R.string.snackbar_task_updated, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //If the user undoes the action then the task is set to not completed and the database is updated again
                            task.setCompleted(false);
                            viewModel.updateTask(task);
                        }
                    }).show();
        } else if (s.equals(getString(R.string.action_set_uncompleted))) {
            task.setCompleted(false);
            viewModel.updateTask(task);
            Snackbar.make(taskViewHolder.itemView, R.string.snackbar_task_updated, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setCompleted(true);
                            viewModel.updateTask(task);
                        }
                    }).show();
        } else if (s.equals(getString(R.string.action_set_postponed))) {
            task.setPostponed(true);
            viewModel.updateTask(task);
            Snackbar.make(taskViewHolder.itemView, R.string.snackbar_task_updated, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setPostponed(false);
                            viewModel.updateTask(task);
                        }
                    }).show();
        } else if (s.equals(getString(R.string.action_delete_task))) {
            viewModel.deleteTask(task);
            Snackbar.make(taskViewHolder.itemView, R.string.snackbar_task_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewModel.insertTask(task);
                        }
                    }).show();
        } else if (s.equals(getString(R.string.action_set_active))) {
            task.setPostponed(false);
            viewModel.updateTask(task);
            Snackbar.make(taskViewHolder.itemView, R.string.snackbar_task_updated, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setPostponed(true);
                            viewModel.updateTask(task);
                        }
                    }).show();
        }
    }
    //Creates the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
    //When the settings option is pressed when the settings activity is loaded
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Interface method for when the task is clicked in the recycler view
    @Override
    public void taskClicked(Task task) {
        Intent intent = new Intent(this, TaskDetails.class);
        intent.putExtra(getString(R.string.edit_task_intent_extra), task);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
    //When back is pressed from main activity then tasks fragment is loaded unless it is already showing and then super is called
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(currentFragment instanceof TasksFragment)) {
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        } else {
            super.onBackPressed();
        }
    }
}