package com.sixtyninefourtwenty.common.utils;

import android.content.Context;
import android.os.Build;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public final class Formatting {

    private Formatting() {}

    public static String formatANumber(Context context, int number) {
        final var configuration = context.getResources().getConfiguration();
        final var locale = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? configuration.getLocales().get(0) : configuration.locale;
        return String.format(locale, "%d", number);
    }
}
