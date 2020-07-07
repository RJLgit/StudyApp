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

public class PostponedFragment extends GeneralFragment {
    //Recyclerview variables
    RecyclerView recyclerView;

    public PostponedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_postponed, container, false);
        //Sets up the RV
        recyclerView = v.findViewById(R.id.postponed_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //Sets up the adapter, sending the activity as the context and on click listeners
        adapter = new TaskAdapter(getActivity(), (TaskAdapter.OnActionButtonPressed) getActivity(), (TaskAdapter.OnTaskClicked) getActivity());
        recyclerView.setAdapter(adapter);
        //Sends array of options to populate the drop down menu in each item
        adapter.setSpinnerArray(R.array.postponed_actions);
        //Gets the postponed tasks and populates the adapter with these tasks, sends the sort and filter settings obtained from the shared preferences instance
        TaskViewModel viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getPostponedTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setMyTasks(tasks);
                adapter.filterData(sharedPreferences.getStringSet(getString(R.string.settings_key_categories), null));
                adapter.sortData(sharedPreferences.getString(getString(R.string.settings_key_sort), getString(R.string.default_setting_sort)));
            }
        });
        return v;
    }

}