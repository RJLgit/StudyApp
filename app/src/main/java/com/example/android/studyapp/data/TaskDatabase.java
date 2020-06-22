package com.example.android.studyapp.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    //Singleton pattern
    private static final String TAG = TaskDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "taskdatabase";
    private static TaskDatabase mInstance;

    //Get instance method to ensure only a single database instance exists
    public static TaskDatabase getInstance(Context context) {
        if (mInstance == null) {
            //Stops 2 db objects being made at the same time
            synchronized (LOCK) {
                Log.d(TAG, "Creating new db instance");
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TaskDatabase.class, TaskDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting db instance");
        return mInstance;
    }


    public abstract TaskDao taskDao();
}
