package com.github.newterios.performance;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayGenerator {

    public static int[] generateRandomArray(int size, int minValue, int maxValue) {
        if (size < 0) {
            throw new IllegalArgumentException("Array size cannot be negative");
        }

        int[] array = new int[size];
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        }

        return array;
    }

    public static int[] generateSortedArray(int size, int startValue) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = startValue + i;
        }
        return array;
    }

    public static int[] generateReverseSortedArray(int size, int startValue) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = startValue + size - i - 1;
        }
        return array;
    }

    public static int[] generateNearlySortedArray(int size, int startValue, double disorderPercentage) {
        int[] array = generateSortedArray(size, startValue);
        Random random = ThreadLocalRandom.current();
        int swaps = (int) (size * disorderPercentage);

        for (int i = 0; i < swaps; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }

        return array;
    }

    public static int[] generateArrayWithDuplicates(int size, int uniqueValues) {
        int[] array = new int[size];
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(uniqueValues);
        }

        return array;
    }

    public static int[] copyArray(int[] original) {
        return original.clone();
    }
}