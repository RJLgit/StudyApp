package com.example.android.studyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class PostponedFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    TaskAdapter adapter;
    private static final String TAG = "PostponedFragment";

    public PostponedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_postponed, container, false);

        recyclerView = v.findViewById(R.id.postponed_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TaskAdapter(getActivity(), (TaskAdapter.OnActionButtonPressed) getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setSpinnerArray(R.array.postponed_actions);

        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getPostponedTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setMyTasks(tasks);
                adapter.filterData(sharedPreferences.getStringSet("settings_key_category", null));
                adapter.sortData(sharedPreferences.getString("settings_key_sort", "Date added"));
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
        Log.d(TAG, "onSharedPreferenceChanged: ");
        if (s.equals("settings_key_sort")) {
            Log.d(TAG, "onSharedPreferenceChanged: sort");
            adapter.sortData(sharedPreferences.getString("settings_key_sort", "Date added"));
        } else if (s.equals("settings_key_category")) {
            Log.d(TAG, "onSharedPreferenceChanged: filter");
            adapter.filterData(sharedPreferences.getStringSet("settings_key_category", null));
        }
    }
}