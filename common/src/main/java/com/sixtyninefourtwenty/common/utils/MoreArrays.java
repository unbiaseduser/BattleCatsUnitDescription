package com.sixtyninefourtwenty.common.utils;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@NonNullTypesByDefault
public final class MoreArrays {

    private MoreArrays() {}

    public static boolean contains(int[] ints, int elem) {
        for (final var i : ints) {
            if (elem == i) {
                return true;
            }
        }
        return false;
    }
}
