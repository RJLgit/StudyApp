package com.example.android.studyapp.data;

import java.util.Comparator;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "priority")
    public int priority;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "timeAdded")
    public long timeAdded;

    @ColumnInfo(name = "postponed")
    public boolean postponed;

    //Constructor without id before that is auto generated. Ignored as room uses the other constructor
    @Ignore
    public Task(String title, String description, int priority, String category, boolean completed, long timeAdded, boolean postponed) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.completed = completed;
        this.timeAdded = timeAdded;
        this.postponed = postponed;
    }

    public Task(int id, String title, String description, int priority, String category, boolean completed, long timeAdded, boolean postponed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.completed = completed;
        this.timeAdded = timeAdded;
        this.postponed = postponed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }

    public boolean isPostponed() {
        return postponed;
    }

    public void setPostponed(boolean postponed) {
        this.postponed = postponed;
    }

    public static class PriorityComparator implements Comparator<Task> {
        @Override
        public int compare(Task task, Task t1) {
            if (task.getPriority() > t1.getPriority()) {
                return -1;
            } else if (task.getPriority() < t1.getPriority()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    public static class TimeComparator implements Comparator<Task> {
        @Override
        public int compare(Task task, Task t1) {
            if (task.getTimeAdded() > t1.getTimeAdded()) {
                return -1;
            } else if (task.getTimeAdded() < t1.getTimeAdded()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
