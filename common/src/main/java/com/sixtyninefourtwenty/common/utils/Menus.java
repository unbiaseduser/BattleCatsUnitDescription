package com.sixtyninefourtwenty.common.utils;

import static java.util.Objects.requireNonNull;

import android.view.MenuItem;
import android.view.View;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public final class Menus {

    private Menus() {}

    public static <T extends View> T requireActionView(MenuItem item, Class<T> clazz) {
        return requireNonNull(clazz.cast(item.getActionView()), "This menu item doesn't have an action view");
    }
}
