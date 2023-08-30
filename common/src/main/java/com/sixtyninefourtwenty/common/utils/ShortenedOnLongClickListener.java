package com.sixtyninefourtwenty.common.utils;

import android.view.View;

@FunctionalInterface
public interface ShortenedOnLongClickListener extends View.OnLongClickListener {

    @Override
    default boolean onLongClick(View v) {
        onLongClickCustom(v);
        return true;
    }

    void onLongClickCustom(View v);

    static View.OnLongClickListener shortened(ShortenedOnLongClickListener listener) {
        return listener;
    }

}
