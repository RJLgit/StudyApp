package com.example.android.studyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.studyapp.data.Task;
import com.example.android.studyapp.data.TaskViewModel;

import java.util.List;

public class TasksFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    TaskAdapter taskAdapter;

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

        taskAdapter = new TaskAdapter(getActivity(), (TaskAdapter.OnActionButtonPressed) getActivity(), (TaskAdapter.OnTaskClicked) getActivity());
        taskAdapter.setSpinnerArray(R.array.task_actions);
        recyclerView.setAdapter(taskAdapter);

        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getUncompletedTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.setMyTasks(tasks);
                taskAdapter.filterData(sharedPreferences.getStringSet("settings_key_category", null));
                taskAdapter.sortData(sharedPreferences.getString("settings_key_sort", "Date added - Most recent to least recent"));
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDetach();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("settings_key_sort")) {
            taskAdapter.sortData(sharedPreferences.getString("settings_key_sort", "Date added - Most recent to least recent"));
        } else if (s.equals("settings_key_category")) {
            taskAdapter.filterData(sharedPreferences.getStringSet("settings_key_category", null));
        }
    }
}