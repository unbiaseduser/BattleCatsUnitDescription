package com.sixtyninefourtwenty.bcud.utils;

import static java.util.Objects.requireNonNull;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import kotlin.collections.CollectionsKt;
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

    private String getString(String key, String defaultValue) {
        return requireNonNull(preferences.getString(key, defaultValue));
    }

    private interface StringValueEnum {
        String getValue();
    }

    private <E extends Enum<E> & StringValueEnum> E getStringValueEnum(Iterable<E> values, String key, E defaultValue) {
        final var prefValue = getString(key, defaultValue.getValue());
        return CollectionsKt.first(values, value -> value.getValue().equals(prefValue));
    }

    private interface IntValueEnum {
        int getValue();
    }

    @SuppressWarnings("unused")
    private <E extends Enum<E> & IntValueEnum> E getIntValueEnum(Iterable<E> values, String key, E defaultValue) {
        final var prefValue = preferences.getInt(key, defaultValue.getValue());
        return CollectionsKt.first(values, value -> value.getValue() == prefValue);
    }

    @AllArgsConstructor
    @Getter
    public enum ListViewMode implements StringValueEnum {
        LIST("list"),
        GRID("grid");
        private final String value;
        public static final ImmutableList<ListViewMode> VALUES = ImmutableList.copyOf(values());
    }

    public ListViewMode getListViewMode() {
        return getStringValueEnum(ListViewMode.VALUES, "list_mode", ListViewMode.GRID);
    }

    public void setListViewMode(ListViewMode value) {
        preferences.edit().putString("list_mode", value.getValue()).apply();
    }

    @AllArgsConstructor
    @Getter
    public enum AdventViewMode implements StringValueEnum {
        TABS("tabs"),
        TEXT("texts");
        private final String value;
        public static final ImmutableList<AdventViewMode> VALUES = ImmutableList.copyOf(values());
    }

    public AdventViewMode getAdventViewMode() {
        return getStringValueEnum(AdventViewMode.VALUES, "advent_view_mode", AdventViewMode.TABS);
    }

    public void setAdventViewMode(AdventViewMode value) {
        preferences.edit().putString("advent_view_mode", value.getValue()).apply();
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
