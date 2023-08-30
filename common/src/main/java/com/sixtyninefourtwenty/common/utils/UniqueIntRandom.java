package com.sixtyninefourtwenty.common.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class UniqueIntRandom {

    private final AtomicInteger current = new AtomicInteger(-1);

    public int nextInt(int bound) {
        final var random = ThreadLocalRandom.current();
        int newInt = random.nextInt(bound);
        while (current.get() == newInt) {
            newInt = random.nextInt(bound);
        }
        current.set(newInt);
        return newInt;
    }

}
