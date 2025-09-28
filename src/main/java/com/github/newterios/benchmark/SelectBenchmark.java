package com.github.newterios.benchmark;

import com.github.newterios.sort.DeterministicSelect;
import com.github.newterios.performance.ArrayGenerator;
import java.util.Arrays;
import java.util.Random;

public class SelectBenchmark {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        runBenchmarks();
    }

    public static void runBenchmarks() {
        System.out.println("=== SELECT ALGORITHMS BENCHMARK ===\n");

        int[] sizes = {100, 1000, 10000, 50000};

        for (int size : sizes) {
            System.out.println("--- Array size: " + size + " ---");

            int[] array = ArrayGenerator.generateRandomArray(size, 1, size * 10);
            int k = RANDOM.nextInt(size);


            benchmarkSelect("DeterministicSelect", array.clone(), k);

            benchmarkArraysSort("ArraysSort", array.clone(), k);

            System.out.println();
        }
    }

    private static void benchmarkSelect(String name, int[] array, int k) {
        long startTime = System.nanoTime();

        try {
            DeterministicSelect selector = new DeterministicSelect();
            int result = selector.select(array, k);

            long endTime = System.nanoTime();
            double timeMs = (endTime - startTime) / 1_000_000.0;

            System.out.printf("%-15s: %.3f ms (result: %d)%n", name, timeMs, result);
        } catch (Exception e) {
            System.err.println("Error in " + name + ": " + e.getMessage());
        }
    }

    private static void benchmarkArraysSort(String name, int[] array, int k) {
        long startTime = System.nanoTime();

        try {
            Arrays.sort(array);
            int result = array[k];

            long endTime = System.nanoTime();
            double timeMs = (endTime - startTime) / 1_000_000.0;

            System.out.printf("%-15s: %.3f ms (result: %d)%n", name, timeMs, result);
        } catch (Exception e) {
            System.err.println("Error in " + name + ": " + e.getMessage());
        }
    }
}