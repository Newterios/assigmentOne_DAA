package com.github.newterios.cli;

import com.github.newterios.sort.*;
import com.github.newterios.geometry.ClosestPair;
import com.github.newterios.performance.ArrayGenerator;
import com.github.newterios.metrics.SortMetrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class AlgorithmRunner {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];
        switch (command) {
            case "run-all":
                runAllAlgorithms();
                break;
            case "test-sorting":
                testSortingAlgorithms();
                break;
            case "test-select":
                testSelectAlgorithm();
                break;
            case "test-closest":
                testClosestPairAlgorithm();
                break;
            case "generate-csv":
                generatePerformanceCSV();
                break;
            case "benchmark-sorting":
                runSortingBenchmark();
                break;
            case "benchmark-select":
                runSelectBenchmark();
                break;
            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java -cp target/classes com.github.newterios.cli.AlgorithmRunner <command>");
        System.out.println("Commands:");
        System.out.println("  run-all         - Run all algorithms with sample data");
        System.out.println("  test-sorting    - Test sorting algorithms");
        System.out.println("  test-select     - Test deterministic select algorithm");
        System.out.println("  test-closest    - Test closest pair algorithm");
        System.out.println("  generate-csv    - Generate CSV performance data");
        System.out.println("  benchmark-sorting - Run sorting benchmarks");
        System.out.println("  benchmark-select  - Run select benchmarks");
    }

    private static void runAllAlgorithms() {
        System.out.println("=== RUNNING ALL ALGORITHMS ===\n");

        testSortingAlgorithms();
        System.out.println();
        testSelectAlgorithm();
        System.out.println();
        testClosestPairAlgorithm();
    }

    private static void runSortingBenchmark() {
        com.github.newterios.benchmark.SortingBenchmark.runBenchmarks();
    }

    private static void runSelectBenchmark() {
        com.github.newterios.benchmark.SelectBenchmark.runBenchmarks();
    }

    private static void testSortingAlgorithms() {
        System.out.println("=== SORTING ALGORITHMS TEST ===");

        int[] sizes = {100, 1000, 10000};
        for (int size : sizes) {
            System.out.println("\n--- Testing with array size: " + size + " ---");

            int[] array = ArrayGenerator.generateRandomArray(size, 1, size * 10);
            int[] arrayCopy1 = Arrays.copyOf(array, array.length);
            int[] arrayCopy2 = Arrays.copyOf(array, array.length);
            int[] arrayCopy3 = Arrays.copyOf(array, array.length);

            // Hybrid Merge Sort
            HybridMergeSort hybridSorter = new HybridMergeSort();
            hybridSorter.getMetrics().startTimer();
            hybridSorter.sort(arrayCopy1);
            hybridSorter.getMetrics().stopTimer();
            SortMetrics hybridMetrics = hybridSorter.getMetrics();
            System.out.printf("Hybrid Merge Sort: %s%n", hybridMetrics);

            QuickSort quickSorter = new QuickSort();
            quickSorter.getMetrics().startTimer();
            quickSorter.sort(arrayCopy2);
            quickSorter.getMetrics().stopTimer();
            SortMetrics quickMetrics = quickSorter.getMetrics();
            System.out.printf("QuickSort: %s%n", quickMetrics);

            if (size <= 10000) {
                InsertionSort insertionSorter = new InsertionSort();
                insertionSorter.getMetrics().startTimer();
                insertionSorter.sort(arrayCopy3);
                insertionSorter.getMetrics().stopTimer();
                SortMetrics insertionMetrics = insertionSorter.getMetrics();
                System.out.printf("Insertion Sort: %s%n", insertionMetrics);
            }

            boolean allEqual = Arrays.equals(arrayCopy1, arrayCopy2);
            System.out.println("Results consistent: " + allEqual);

            double expectedMaxDepth = 2 * (Math.log(size) / Math.log(2));
            System.out.printf("QuickSort depth check: %d <= %.1f + 10: %s%n",
                    quickMetrics.getMaxRecursionDepth(), expectedMaxDepth,
                    quickMetrics.getMaxRecursionDepth() <= expectedMaxDepth + 10);
        }
    }

    private static void testSelectAlgorithm() {
        System.out.println("=== DETERMINISTIC SELECT TEST ===");

        int trials = 100;
        int correct = 0;

        for (int i = 0; i < trials; i++) {
            int size = 100 + RANDOM.nextInt(900); // 100-1000 elements
            int[] array = ArrayGenerator.generateRandomArray(size, 1, 1000);
            int k = RANDOM.nextInt(size);

            DeterministicSelect selector = new DeterministicSelect();
            int result = selector.select(array, k);

            // Verify against Arrays.sort
            int[] sorted = array.clone();
            Arrays.sort(sorted);
            int expected = sorted[k];

            if (result == expected) {
                correct++;
            } else {
                System.out.printf("Trial %d failed: expected %d, got %d%n", i, expected, result);
            }
        }

        System.out.printf("Select algorithm accuracy: %d/%d (%.1f%%)%n",
                correct, trials, (correct * 100.0 / trials));
    }

    private static void testClosestPairAlgorithm() {
        System.out.println("=== CLOSEST PAIR ALGORITHM TEST ===");

        for (int size : new int[]{10, 100, 1000}) {
            System.out.printf("\n--- Testing with %d points ---%n", size);

            ClosestPair.Point[] points = generateRandomPoints(size, -1000, 1000);

            ClosestPair closestPair = new ClosestPair();

            ClosestPair.Pair result = closestPair.findClosestPair(points);
            SortMetrics metrics = closestPair.getMetrics();
            System.out.printf("Closest Pair (D&C): %s%n", result);
            System.out.printf("Metrics: %s%n", metrics);

            if (size <= 2000) {
                ClosestPair.Pair bruteForceResult = closestPair.findClosestPairBruteForce(points);
                System.out.printf("Brute Force Validation: %s%n", bruteForceResult);
                System.out.printf("Distance match: %.6f%n", Math.abs(result.distance - bruteForceResult.distance));
            }
        }
    }

    private static void generatePerformanceCSV() {
        System.out.println("=== GENERATING PERFORMANCE CSV ===");

        String fileName = "performance_metrics.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Algorithm,Size,Comparisons,Assignments,TotalOperations,MaxRecursionDepth,TimeMillis");

            int[] sizes = {100, 500, 1000, 5000, 10000};
            int successfulTests = 0;

            for (int size : sizes) {
                System.out.println("Testing size: " + size);

                int[] array = ArrayGenerator.generateRandomArray(size, 1, size * 10);

                if (testAlgorithmCSV(writer, "HybridMergeSort", new HybridMergeSort(), array, size)) {
                    successfulTests++;
                }
                if (testAlgorithmCSV(writer, "QuickSort", new QuickSort(), array, size)) {
                    successfulTests++;
                }
                if (size <= 10000 && testAlgorithmCSV(writer, "InsertionSort", new InsertionSort(), array, size)) {
                    successfulTests++;
                }
            }

            System.out.printf("CSV file generated: %s (%d successful measurements)%n", fileName, successfulTests);
            System.out.println("File location: " + Paths.get(fileName).toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean testAlgorithmCSV(PrintWriter writer, String algorithmName, Object sorter, int[] array, int size) {
        int[] arrayCopy = Arrays.copyOf(array, array.length);
        SortMetrics metrics = null;

        try {
            if (sorter instanceof HybridMergeSort) {
                HybridMergeSort hybridSorter = (HybridMergeSort) sorter;
                hybridSorter.getMetrics().startTimer();
                hybridSorter.sort(arrayCopy);
                hybridSorter.getMetrics().stopTimer();
                metrics = hybridSorter.getMetrics();
            } else if (sorter instanceof QuickSort) {
                QuickSort quickSorter = (QuickSort) sorter;
                quickSorter.getMetrics().startTimer();
                quickSorter.sort(arrayCopy);
                quickSorter.getMetrics().stopTimer();
                metrics = quickSorter.getMetrics();
            } else if (sorter instanceof InsertionSort) {
                InsertionSort insertionSorter = (InsertionSort) sorter;
                insertionSorter.getMetrics().startTimer();
                insertionSorter.sort(arrayCopy);
                insertionSorter.getMetrics().stopTimer();
                metrics = insertionSorter.getMetrics();
            }

            if (metrics != null && isSorted(arrayCopy)) {
                writer.printf("%s,%d,%d,%d,%d,%d,%.3f%n",
                        algorithmName, size, metrics.getComparisons(), metrics.getAssignments(),
                        metrics.getTotalOperations(), metrics.getMaxRecursionDepth(),
                        metrics.getExecutionTimeMillis());
                writer.flush();
                return true;
            } else {
                System.err.printf("Algorithm %s with size %d failed validation%n", algorithmName, size);
                return false;
            }
        } catch (Exception e) {
            System.err.printf("Error testing %s with size %d: %s%n", algorithmName, size, e.getMessage());
            return false;
        }
    }

    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static ClosestPair.Point[] generateRandomPoints(int count, double min, double max) {
        ClosestPair.Point[] points = new ClosestPair.Point[count];
        for (int i = 0; i < count; i++) {
            double x = min + RANDOM.nextDouble() * (max - min);
            double y = min + RANDOM.nextDouble() * (max - min);
            points[i] = new ClosestPair.Point(x, y);
        }
        return points;
    }
}