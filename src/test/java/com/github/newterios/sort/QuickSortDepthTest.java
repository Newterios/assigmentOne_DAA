package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;
import com.github.newterios.performance.ArrayGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class QuickSortDepthTest {
    private QuickSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new QuickSort();
    }

    @Test
    @DisplayName("QuickSort recursion depth should be bounded by ~2*log2(n) for randomized pivot")
    void shouldHaveBoundedRecursionDepth() {
        int[] sizes = {100, 1000, 10000, 50000};

        for (int size : sizes) {
            int[] array = ArrayGenerator.generateRandomArray(size, 1, size * 10);
            sorter.sort(array);

            SortMetrics metrics = sorter.getMetrics();
            int maxDepth = metrics.getMaxRecursionDepth();

            double expectedMaxDepth = 2 * (Math.log(size) / Math.log(2));
            double tolerance = 10;

            assertTrue(maxDepth <= expectedMaxDepth + tolerance,
                    String.format("For size %d: depth %d should be <= %.1f",
                            size, maxDepth, expectedMaxDepth + tolerance));

            System.out.printf("Size: %d, Max Depth: %d, Expected: %.1f%n",
                    size, maxDepth, expectedMaxDepth);
        }
    }

    @Test
    @DisplayName("Should handle adversarial arrays with good depth")
    void shouldHandleAdversarialArrays() {
        int[] sortedArray = ArrayGenerator.generateSortedArray(1000, 1);
        sorter.sort(sortedArray);

        SortMetrics metrics = sorter.getMetrics();
        int maxDepth = metrics.getMaxRecursionDepth();

        double expectedMaxDepth = 2 * (Math.log(1000) / Math.log(2));
        assertTrue(maxDepth <= expectedMaxDepth + 15);
    }
}