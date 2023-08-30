package com.sixtyninefourtwenty.common.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface MyTextWatcher extends TextWatcher {
    @Override
    default void afterTextChanged(Editable s) {}
    
    @Override
    void onTextChanged(@NonNull CharSequence s, int start, int before, int count);

    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}
