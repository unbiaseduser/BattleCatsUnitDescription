package com.sixtyninefourtwenty.common.utils;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import java.util.concurrent.Executor;

@NonNullTypesByDefault
public final class MoreFutures {

    private MoreFutures() {}

    public static <V> void addIgnoreExceptionsCallback(ListenableFuture<V> future, FutureCallbackIgnoreExceptions<V> callback, Executor executor) {
        Futures.addCallback(future, callback, executor);
    }

}
