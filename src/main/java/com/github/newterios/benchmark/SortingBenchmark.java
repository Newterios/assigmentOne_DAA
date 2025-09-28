package com.github.newterios.benchmark;

import com.github.newterios.sort.*;
import com.github.newterios.performance.ArrayGenerator;
import java.util.concurrent.TimeUnit;

public class SortingBenchmark {

    public static void main(String[] args) throws Exception {
        runBenchmarks();
    }

    public static void runBenchmarks() {
        System.out.println("=== SORTING ALGORITHMS BENCHMARK ===\n");

        int[] sizes = {100, 1000, 10000, 50000};

        for (int size : sizes) {
            System.out.println("--- Array size: " + size + " ---");

            int[] array = ArrayGenerator.generateRandomArray(size, 1, size * 10);

            benchmarkAlgorithm("HybridMergeSort", new HybridMergeSort(), array.clone());

            benchmarkAlgorithm("QuickSort", new QuickSort(), array.clone());

            if (size <= 10000) {
                benchmarkAlgorithm("InsertionSort", new InsertionSort(), array.clone());
            }

            System.out.println();
        }
    }

    private static void benchmarkAlgorithm(String name, Object sorter, int[] array) {
        long startTime = System.nanoTime();

        try {
            if (sorter instanceof HybridMergeSort) {
                ((HybridMergeSort) sorter).sort(array);
            } else if (sorter instanceof QuickSort) {
                ((QuickSort) sorter).sort(array);
            } else if (sorter instanceof InsertionSort) {
                ((InsertionSort) sorter).sort(array);
            }
        } catch (Exception e) {
            System.err.println("Error in " + name + ": " + e.getMessage());
            return;
        }

        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;

        System.out.printf("%-15s: %.3f ms%n", name, timeMs);
    }
}