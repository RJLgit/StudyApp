package com.example.android.studyapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
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

    //Constructor without id before that is auto generated
    public Task(String title, String description, int priority, String category) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
    }
    //Ignores this constructor
    @Ignore
    public Task(int id, String title, String description, int priority, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
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
}
