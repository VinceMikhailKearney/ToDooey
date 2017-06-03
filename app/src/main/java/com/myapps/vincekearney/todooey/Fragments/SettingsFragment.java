package com.myapps.vincekearney.todooey.Fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.myapps.vincekearney.todooey.R;
import com.myapps.vincekearney.todooey.ToDoApplication;

import java.util.prefs.Preferences;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";
    private static final String HIDE_ITEMS = "pref_key_hide";
    private static final String HIDE_ALL_ITEMS = "pref_key_hide_all";

    CheckBoxPreference parentHideItems;
    CheckBoxPreference hideAllItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        parentHideItems = (CheckBoxPreference) findPreference(HIDE_ITEMS);
        hideAllItems = (CheckBoxPreference) findPreference(HIDE_ALL_ITEMS);

        parentHideItems.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.i(TAG,"We got a preference change");
        switch (preference.getKey()) {
            case HIDE_ITEMS:
                Log.i(TAG, "Found the right pref");
                if((boolean) newValue == false) {
                    Log.i(TAG, "Are we even hitting this");
                    PreferenceManager.getDefaultSharedPreferences(ToDoApplication.getAppContext()).edit().putBoolean(HIDE_ALL_ITEMS,false).commit();
                    hideAllItems.setChecked(false);
                }
        }
        return true;
    }
}
