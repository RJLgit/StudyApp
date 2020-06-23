package com.example.android.studyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.studyapp.data.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> myTasks = new ArrayList<>();
    Context mContext;

    public TaskAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tasks_item_view, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.title.setText(myTasks.get(position).getTitle());
        holder.category.setText(myTasks.get(position).getCategory());
        holder.priority.setText(myTasks.get(position).getPriority() + "");
    }

    @Override
    public int getItemCount() {
        return myTasks.size();
    }

    public void setMyTasks(List<Task> myTasks) {
        this.myTasks = myTasks;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView category;
        TextView priority;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title_item_view);
            category = itemView.findViewById(R.id.task_category_item_view);
            priority = itemView.findViewById(R.id.task_priority_item_view);
        }
    }
}
