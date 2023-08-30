package com.sixtyninefourtwenty.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sixtyninefourtwenty.common.utils.UniqueIntRandom;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class UniqueIntRandomTest {

    @Test
    void test() {
        final var random = new UniqueIntRandom();
        final var numbersToGenerate = 100;
        final var numbers = new int[numbersToGenerate];
        for (int i = 0; i < numbersToGenerate; i++) {
            numbers[i] = random.nextInt(Integer.MAX_VALUE);
        }
        //All numbers in array should be distinct
        assertEquals(numbersToGenerate, Arrays.stream(numbers).distinct().count());
    }

    @Test
    void multithreadedTest() throws InterruptedException {
        final var random = new UniqueIntRandom();
        final var numbersToGenerateForEachThread = 100;
        final var numbers = new int[numbersToGenerateForEachThread * 2];
        final var firstThread = new Thread(() -> {
            for (int i = 0; i < numbersToGenerateForEachThread; i++) {
                numbers[i] = random.nextInt(Integer.MAX_VALUE);
            }
        });
        final var secondThread = new Thread(() -> {
            for (int i = numbersToGenerateForEachThread; i < numbersToGenerateForEachThread * 2; i++) {
                numbers[i] = random.nextInt(Integer.MAX_VALUE);
            }
        });
        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();
        assertEquals(numbersToGenerateForEachThread * 2, Arrays.stream(numbers).distinct().count());
    }

}
