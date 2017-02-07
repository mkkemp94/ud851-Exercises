package android.example.com.visualizerpreferences;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

// TODO (1) Implement OnSharedPreferenceChangeListener
public class SettingsFragment extends PreferenceFragmentCompat
        implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_visualizer);

        // TODO (3) Get the preference screen, get the number of preferences and iterate through
        // all of the preferences if it is not a checkbox preference, call the setSummary method
        // passing in a preference and the value of the preference

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        // Go through all preferences
        for (int i = 0; i < count; i++) {

            // Get this particular preference
            Preference p = preferenceScreen.getPreference(i);

            // Check if it's a checkbox pref or not
            if (!(p instanceof CheckBoxPreference)) {

                // This isn't, so we need to give it a summary manually

                // Get its value
                String value = sharedPreferences.getString(p.getKey(), "");

                // And set the summary below
                setPreferenceSummary(p, value);
            }
        }
    }

    // TODO (4) Override onSharedPreferenceChanged and, if it is not a checkbox preference,
    // call setPreferenceSummary on the changed preference

    // Whenever a preference changes this happens
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        // Get this particular preference
        Preference preference = findPreference(key);

        // If it exists
        if (preference != null) {

            // And its not a check box preference
            if (!(preference instanceof CheckBoxPreference)) {

                // Get the value of THIS preference
                String value = sharedPreferences.getString(preference.getKey(), "");

                // And set the summary
                setPreferenceSummary(preference, value);
            }
        }

    }

    // TODO (2) Create a setPreferenceSummary which takes a Preference and String value as parameters.
    // This method should check if the preference is a ListPreference and, if so, find the label
    // associated with the value. You can do this by using the findIndexOfValue and getEntries methods
    // of Preference.
    private void setPreferenceSummary(Preference preference, String value) {

        // If this preference is of type list preference
        if (preference instanceof ListPreference) {

            // Cast it as so so that we can use its methods
            ListPreference listPreference = (ListPreference) preference;

            // We have the value for this preference. Find its index.
            int prefIndex = listPreference.findIndexOfValue(value);

            // If this is a valid index
            if (prefIndex >= 0) {

                // Now use that index to find its label
                String label = (String) listPreference.getEntries() [prefIndex];

                // Sand set a summary to this preference
                listPreference.setSummary(label);

            }
        }
    }

    // TODO (5) Register and unregister the OnSharedPreferenceChange listener (this class) in
    // onCreate and onDestroy respectively.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}