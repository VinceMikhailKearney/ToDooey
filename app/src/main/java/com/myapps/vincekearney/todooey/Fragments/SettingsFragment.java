package com.myapps.vincekearney.todooey.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.myapps.vincekearney.todooey.R;

public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
