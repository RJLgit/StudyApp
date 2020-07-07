package com.example.android.studyapp;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;


public class GeneralFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences sharedPreferences;
    TaskAdapter adapter;

    //When the fragment is attached the shared preference instance is created and the listener is registered
    @Override
    public void onAttach(@NonNull Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        super.onAttach(context);
    }
    //Need to ungregister the sharedpreference change listener when detached
    @Override
    public void onDetach() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDetach();
    }
    //Method triggered when shared preference changed, sends the new setting to the adapter when that happens to update the RV
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("settings_key_sort")) {
            adapter.sortData(sharedPreferences.getString(getString(R.string.settings_key_sort), getString(R.string.default_setting_sort)));
        } else if (s.equals("settings_key_category")) {
            adapter.filterData(sharedPreferences.getStringSet(getString(R.string.settings_key_categories), null));
        }
    }
}
