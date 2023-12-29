package com.sixtyninefourtwenty.common.utils;

import java.util.function.Function;

import lombok.SneakyThrows;

public interface ThrowingFunction<T, R> extends Function<T, R> {

    @Override
    @SneakyThrows
    default R apply(T t) {
        return applyThrowing(t);
    }

    R applyThrowing(T t) throws Throwable;

    static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> function) {
        return function;
    }

}
