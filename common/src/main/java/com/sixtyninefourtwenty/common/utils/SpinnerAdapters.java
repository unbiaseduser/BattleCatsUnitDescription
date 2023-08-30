package com.sixtyninefourtwenty.common.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.ArrayRes;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public final class SpinnerAdapters {
    private SpinnerAdapters() {}

    public static <T> ArrayAdapter<T> createWithAndroidResources(Context context, T[] items) {
        final var adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static ArrayAdapter<CharSequence> createWithAndroidResources(Context context, @ArrayRes int items) {
        final var adapter = ArrayAdapter.createFromResource(context, items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

}
