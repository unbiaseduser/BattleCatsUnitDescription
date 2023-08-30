package com.sixtyninefourtwenty.common.utils;

import com.google.common.util.concurrent.FutureCallback;

import lombok.SneakyThrows;

@FunctionalInterface
public interface FutureCallbackIgnoreExceptions<T> extends FutureCallback<T> {

    @Override
    @SneakyThrows
    default void onFailure(Throwable t) {
        throw t;
    }

}
