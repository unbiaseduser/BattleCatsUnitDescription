package com.sixtyninefourtwenty.common.utils;

import com.google.common.util.concurrent.FutureCallback;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class FutureCallbackLambdas<T> implements FutureCallback<T> {

    private final Consumer<T> onSuccess;
    private final Consumer<@NonNull Throwable> onFailure;

    @Override
    public void onSuccess(T result) {
        onSuccess.accept(result);
    }

    @Override
    public void onFailure(@NonNull Throwable t) {
        onFailure.accept(t);
    }
}
