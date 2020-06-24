package com.example.android.studyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.studyapp.data.Task;
import com.example.android.studyapp.data.TaskViewModel;

import java.util.List;

public class TasksFragment extends Fragment {
    RecyclerView recyclerView;

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = v.findViewById(R.id.tasks_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final TaskAdapter taskAdapter = new TaskAdapter(getActivity(), (TaskAdapter.OnActionButtonPressed) getActivity());
        taskAdapter.setSpinnerArray(R.array.task_actions);
        recyclerView.setAdapter(taskAdapter);

        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getUncompletedTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.setMyTasks(tasks);
            }
        });

        return v;
    }
}