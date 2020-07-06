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
    //Holds all the tasks and the task list after they are filtered
    private List<Task> allMyTasks;
    private List<Task> myFilteredTasks;
    //The context
    Context mContext;
    //The array of task actions passed to the adapter, default is set here
    int spinnerArray = R.array.task_actions;
    //The activity is the listener in both these cases to listen for clicks on the viewholder and for clicks on the button within each viewholder.
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
    //Gets the task object from the myFilteredTasks list to populates the viewholder UI with the information of that task
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.title.setText(myFilteredTasks.get(position).getTitle());
        holder.category.setText(mContext.getString(R.string.task_adapter_category_string, myFilteredTasks.get(position).getCategory()));
        holder.priority.setText(mContext.getString(R.string.task_adapter_priority, myFilteredTasks.get(position).getPriority()));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                spinnerArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.action.setAdapter(adapter);
        //Sets the background color of the viewholder based on the category of the task object in that case
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
    //When the tasks change in the database then the adapter receives this message and updates the dataset being displayed
    public void setMyTasks(List<Task> myTasks) {
        this.allMyTasks = myTasks;
        this.myFilteredTasks.addAll(allMyTasks);
        notifyDataSetChanged();
    }
    //This method is triggered from the fragments when the sharedpreferences changes.
    //The string arguement defines the sort to be carried out on the tasks
    public void sortData(String sort) {
        if (sort.equals(mContext.getString(R.string.default_setting_sort))) {
            //Sorts using the comparator for time defined in the task object
            Collections.sort(myFilteredTasks, new Task.TimeComparator());
        } else if (sort.equals(mContext.getString(R.string.sort_priority))) {
            //Sorts using the comparator for priority defined in the task object
            Collections.sort(myFilteredTasks, new Task.PriorityComparator());
        }
        notifyDataSetChanged();
    }
    //This method is triggered from the fragments when the sharedpreferences changes.
    //The string set arguement defines the tasks to be shown after the filter
    public void filterData(Set<String> settings_key_category) {
        //Clears the myFilteredTasks list
        myFilteredTasks.clear();
        for (Task t : allMyTasks) {
            if (settings_key_category != null && settings_key_category.contains(t.getCategory())) {
                //Adds the tasks back to this list only if its category is in the set of strings send as an argument.
                myFilteredTasks.add(t);
            }
        }
        notifyDataSetChanged();
    }
    //The spinnerarray is set from each fragment based on which fragment is being shown as different spinner array values are needed for each fragment
    public void setSpinnerArray(int spinnerArray) {
        this.spinnerArray = spinnerArray;
    }

    

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //UI elements
        TextView title;
        TextView category;
        TextView priority;
        Spinner action;
        Button actionButton;
        ConstraintLayout constraintLayout;
        //Listener variables
        OnActionButtonPressed mViewHolderListener;
        OnTaskClicked mOnTaskClickedListener;


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
                    //When the action button is pressed when the relevent task and the selected action is obtained from the spinner and passed in this method to the listener (which is the main activity)
                    //The main activity can then carry out the action requested by the user on the task
                    mViewHolderListener.actionButtonPressed(myFilteredTasks.get(getAdapterPosition()), action.getSelectedItem().toString(), TaskViewHolder.this);
                }
            });
            this.mOnTaskClickedListener = mClickListener;
            //Sets the onclick listener for the viewholder as a whole
            itemView.setOnClickListener(this);
        }

        public void setBackgroundColor(int color) {
            constraintLayout.setBackgroundColor(color);
        }
        //When the RV item is clicked (anywhere on the viewholder) then the listener method is triggered. THis is handled in main activity with the task object being passed as an arguement
        //Task object is passed as arguement so the main activity handling the click knows which task was clicked
        @Override
        public void onClick(View view) {
            mOnTaskClickedListener.taskClicked(myFilteredTasks.get(getAdapterPosition()));
        }
    }
    //Interfaces to handle the click on both the RV item as a whole and on the action button in the viewholder.
    public interface OnActionButtonPressed {
        void actionButtonPressed(Task task, String s, TaskViewHolder taskViewHolder);
    }

    public interface OnTaskClicked {
        void taskClicked(Task task);
    }
}
