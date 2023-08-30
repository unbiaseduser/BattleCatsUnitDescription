package com.sixtyninefourtwenty.common.utils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public final class Validations {

    private Validations() {}

    public static final String NO_INFO = "-";

    public static boolean isValidInfoString(@Nullable String input) {
        return input != null && !input.isBlank() && !NO_INFO.equals(input);
    }

    public static boolean isValidInfoString(AppCompatEditText editText) {
        final var text = editText.getText();
        return isValidInfoString(text != null ? text.toString() : null);
    }

}
