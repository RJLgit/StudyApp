package com.example.android.studyapp.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TaskViewModel extends AndroidViewModel {
    private LiveData<List<Task>> completedTasks;
    private LiveData<List<Task>> uncompletedTasks;
    private LiveData<List<Task>> postponedTasks;
    private TaskDatabase database;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        database = TaskDatabase.getInstance(getApplication());
        completedTasks = database.taskDao().getByCompleted(true);
        uncompletedTasks = database.taskDao().getByCompleted(false);
        postponedTasks = database.taskDao().getByPostponed(true);
    }

    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }

    public LiveData<List<Task>> getUncompletedTasks() {
        return uncompletedTasks;
    }

    public LiveData<List<Task>> getPostponedTasks() {
        return postponedTasks;
    }

    public void insertTask(final Task task) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.taskDao().insert(task);
            }
        });
    }

    public void deleteTask(final Task task) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.taskDao().delete(task);
            }
        });
    }

    public void updateTask(final Task task) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                database.taskDao().update(task);
            }
        });
    }
}
