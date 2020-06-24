package com.example.android.studyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.studyapp.data.Task;
import com.example.android.studyapp.data.TaskViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CompletedFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    TaskAdapter adapter;

    public CompletedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_completed, container, false);

        recyclerView = v.findViewById(R.id.completed_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TaskAdapter(getActivity(), (TaskAdapter.OnActionButtonPressed) getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setSpinnerArray(R.array.completed_actions);

        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getCompletedTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setMyTasks(tasks);
                adapter.sortData(sharedPreferences.getString("settings_key_sort", "Date added"));
                adapter.filterData(sharedPreferences.getStringSet("settings_key_category", null));
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
            adapter.sortData(sharedPreferences.getString("settings_key_sort", "Date added"));
        } else if (s.equals("settings_key_category")) {
            adapter.filterData(sharedPreferences.getStringSet("settings_key_category", null));
        }
    }
}