package com.sixtyninefourtwenty.common.utils;

import static java.util.stream.Collectors.toList;

import java.util.List;

public final class Misc {

    private Misc() {}

    public static <T> List<T> cast(List<? extends T> list) {
        return list.stream().map(obj -> (T) obj).collect(toList());
    }

}
