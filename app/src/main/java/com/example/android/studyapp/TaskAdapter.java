package com.example.android.studyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.studyapp.data.Task;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> myTasks = new ArrayList<>();
    Context mContext;
    int spinnerArray = R.array.task_actions;
    OnActionButtonPressed mListener;


    public TaskAdapter(Context mContext, OnActionButtonPressed listener) {
        this.mContext = mContext;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tasks_item_view, parent, false);
        return new TaskViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.title.setText(myTasks.get(position).getTitle());
        holder.category.setText(myTasks.get(position).getCategory());
        holder.priority.setText(myTasks.get(position).getPriority() + "");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                spinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.action.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return myTasks.size();
    }

    public void setMyTasks(List<Task> myTasks) {
        this.myTasks = myTasks;
        notifyDataSetChanged();
    }

    public void sortData(String sort) {

    }

    public void setSpinnerArray(int spinnerArray) {
        this.spinnerArray = spinnerArray;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView category;
        TextView priority;
        Spinner action;
        Button actionButton;
        OnActionButtonPressed mViewHolderListener;

        public TaskViewHolder(@NonNull View itemView, final OnActionButtonPressed viewHolderListener) {
            super(itemView);
            title = itemView.findViewById(R.id.task_title_item_view);
            category = itemView.findViewById(R.id.task_category_item_view);
            priority = itemView.findViewById(R.id.task_priority_item_view);
            action = itemView.findViewById(R.id.action_spinner);
            actionButton = itemView.findViewById(R.id.action_button);
            mViewHolderListener = viewHolderListener;
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolderListener.actionButtonPressed(myTasks.get(getAdapterPosition()), action.getSelectedItem().toString());
                }
            });
        }
    }

    public interface OnActionButtonPressed {
        void actionButtonPressed(Task task, String s);
    }
}
