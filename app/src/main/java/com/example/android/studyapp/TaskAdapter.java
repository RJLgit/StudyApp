package com.example.android.studyapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.studyapp.data.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private static final String TAG = "TaskAdapter";
    private List<Task> allMyTasks;
    private List<Task> myFilteredTasks;
    Context mContext;
    int spinnerArray = R.array.task_actions;
    OnActionButtonPressed mListener;
    OnTaskClicked mClickListener;


    public TaskAdapter(Context mContext, OnActionButtonPressed listener, OnTaskClicked clickListener) {
        this.mContext = mContext;
        this.mListener = listener;
        this.mClickListener = clickListener;
        allMyTasks = new ArrayList<>();
        myFilteredTasks = new ArrayList<>();

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tasks_item_view, parent, false);
        return new TaskViewHolder(view, mListener, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.title.setText(myFilteredTasks.get(position).getTitle());
        holder.category.setText("Category: " + myFilteredTasks.get(position).getCategory());
        holder.priority.setText("Priority: " + myFilteredTasks.get(position).getPriority());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                spinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.action.setAdapter(adapter);
        switch(myFilteredTasks.get(position).getCategory()) {
            case "Work":
                holder.setBackgroundColor(mContext.getColor(R.color.colorWork));
                break;
            case "Social":
                holder.setBackgroundColor(mContext.getColor(R.color.colorSocial));
                break;
            case "Housework":
                holder.setBackgroundColor(mContext.getColor(R.color.colorHousework));
                break;
            case "Study":
                holder.setBackgroundColor(mContext.getColor(R.color.colorStudy));
                break;
            case "Hobby":
                holder.setBackgroundColor(mContext.getColor(R.color.colorHobby));
                break;
            case "Other":
                holder.setBackgroundColor(mContext.getColor(R.color.colorOther));
                break;
            default:
                holder.setBackgroundColor(mContext.getColor(R.color.colorOther));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return myFilteredTasks.size();
    }

    public void setMyTasks(List<Task> myTasks) {
        this.allMyTasks = myTasks;
        this.myFilteredTasks.addAll(allMyTasks);
        notifyDataSetChanged();
    }

    public void sortData(String sort) {
        Log.d(TAG, "sortData: " + sort);
        if (sort.equals("Date added - Most recent to least recent")) {
            Collections.sort(myFilteredTasks, new Task.TimeComparator());
        } else if (sort.equals("Priority - high to low")) {
            Collections.sort(myFilteredTasks, new Task.PriorityComparator());
        }
        notifyDataSetChanged();
    }

    public void filterData(Set<String> settings_key_category) {
        Log.d(TAG, "filterData: " + settings_key_category);
        Log.d(TAG, "filterData: " + myFilteredTasks);
        Log.d(TAG, "filterData: " + allMyTasks);
        myFilteredTasks.clear();
        Log.d(TAG, "filterData: " + allMyTasks);

        for (Task t : allMyTasks) {
            Log.d(TAG, "filterData: " + t);
            if (settings_key_category != null && settings_key_category.contains(t.getCategory())) {
                Log.d(TAG, "filterData: added");
                myFilteredTasks.add(t);
            }
        }
        notifyDataSetChanged();
        Log.d(TAG, "filterData: " + settings_key_category);
    }

    public void setSpinnerArray(int spinnerArray) {
        this.spinnerArray = spinnerArray;
    }

    

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView category;
        TextView priority;
        Spinner action;
        Button actionButton;
        OnActionButtonPressed mViewHolderListener;
        OnTaskClicked mOnTaskClickedListener;
        ConstraintLayout constraintLayout;

        public TaskViewHolder(@NonNull View itemView, final OnActionButtonPressed viewHolderListener, OnTaskClicked mClickListener) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.container_item_view);
            title = itemView.findViewById(R.id.task_title_item_view);
            category = itemView.findViewById(R.id.task_category_item_view);
            priority = itemView.findViewById(R.id.task_priority_item_view);
            action = itemView.findViewById(R.id.action_spinner);
            actionButton = itemView.findViewById(R.id.action_button);
            mViewHolderListener = viewHolderListener;
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolderListener.actionButtonPressed(myFilteredTasks.get(getAdapterPosition()), action.getSelectedItem().toString());
                }
            });
            this.mOnTaskClickedListener = mClickListener;
            itemView.setOnClickListener(this);
        }

        public void setBackgroundColor(int color) {
            constraintLayout.setBackgroundColor(color);
        }

        @Override
        public void onClick(View view) {
            mOnTaskClickedListener.taskClicked(myFilteredTasks.get(getAdapterPosition()));
        }
    }

    public interface OnActionButtonPressed {
        void actionButtonPressed(Task task, String s);
    }

    public interface OnTaskClicked {
        void taskClicked(Task task);
    }
}
