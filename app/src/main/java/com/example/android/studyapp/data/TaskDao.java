package com.example.android.studyapp.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY timeAdded")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE completed LIKE :b AND postponed LIKE :c ORDER BY timeAdded")
    LiveData<List<Task>> getByCompleted(boolean b, boolean c);

    @Query("SELECT * FROM tasks WHERE postponed LIKE :p ORDER BY timeAdded")
    LiveData<List<Task>> getByPostponed(boolean p);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);
}
