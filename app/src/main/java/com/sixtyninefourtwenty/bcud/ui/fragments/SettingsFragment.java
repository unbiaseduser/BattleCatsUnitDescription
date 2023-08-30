package com.sixtyninefourtwenty.bcud.ui.fragments;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;

import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.sixtyninefourtwenty.bcud.R;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        setupAppearanceNavigationPref();
    }

    private void setupAppearanceNavigationPref() {
        final Preference navPref = requireNonNull(findPreference("navigation_appearance_prefs"), formatErrorMsg("navigate to appearance preference"));
        navPref.setOnPreferenceClickListener(preference -> {
            NavHostFragment.findNavController(this).navigate(SettingsFragmentDirections.actionNavSettingsToNavAppearanceSettings());
            return true;
        });
    }

    private String formatErrorMsg(String arg) {
        return String.format("Wrong key passed for %s", arg);
    }

}
