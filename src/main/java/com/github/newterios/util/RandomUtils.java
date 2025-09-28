package com.github.newterios.util;

import java.util.Random;

public class RandomUtils {
    private static final Random RANDOM = new Random();

    public static Random getRandom() {
        return RANDOM;
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    public static void shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            swap(array, i, j);
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}