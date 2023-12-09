package com.sixtyninefourtwenty.bcud.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.Preferences;
import com.sixtyninefourtwenty.stuff.preferences.StringPreferenceValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@NonNullTypesByDefault
public final class AppPreferences {

    public static AppPreferences get(Context context) {
        return MyApplication.get(context).getPrefs();
    }

    public static void initializePreferences(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
    }

    private final SharedPreferences preferences;

    public AppPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @AllArgsConstructor
    @Getter
    public enum ListViewMode implements StringPreferenceValue {
        LIST("list"),
        GRID("grid");
        private final String value;
        public static final ImmutableList<ListViewMode> VALUES = ImmutableList.copyOf(values());
    }

    public ListViewMode getListViewMode() {
        return Preferences.getStringValue(preferences, "list_mode", ListViewMode.VALUES, ListViewMode.GRID);
    }

    public void setListViewMode(ListViewMode value) {
        Preferences.putStringValue(preferences.edit(), "list_mode", value).apply();
    }

    @AllArgsConstructor
    @Getter
    public enum AdventViewMode implements StringPreferenceValue {
        TABS("tabs"),
        TEXT("texts");
        private final String value;
        public static final ImmutableList<AdventViewMode> VALUES = ImmutableList.copyOf(values());
    }

    public AdventViewMode getAdventViewMode() {
        return Preferences.getStringValue(preferences, "advent_view_mode", AdventViewMode.VALUES, AdventViewMode.TABS);
    }

    public void setAdventViewMode(AdventViewMode value) {
        Preferences.putStringValue(preferences.edit(), "advent_view_mode", value).apply();
    }

    public boolean getUseToc() {
        return preferences.getBoolean("use_toc", true);
    }

    public boolean getFirstLaunch() {
        return preferences.getBoolean("first_launch", true);
    }

    public void setFirstLaunch(boolean value) {
        preferences.edit().putBoolean("first_launch", value).apply();
    }

    @SuppressWarnings("unused")
    public boolean resetSetting(String key) {
        if (preferences.contains(key)) {
            preferences.edit().remove(key).apply();
            return true;
        }
        return false;
    }


}
