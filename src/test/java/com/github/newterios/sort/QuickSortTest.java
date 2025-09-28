package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {
    private QuickSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new QuickSort();
    }

    @Test
    @DisplayName("Should sort basic array with randomized pivot")
    void shouldSortBasicArray() {
        int[] array = {5, 2, 4, 6, 1, 3};
        int[] expected = {1, 2, 3, 4, 5, 6};

        sorter.sort(array);
        assertArrayEquals(expected, array);

        SortMetrics metrics = sorter.getMetrics();
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getMaxRecursionDepth() > 0);
    }

    @Test
    @DisplayName("Should handle already sorted array efficiently")
    void shouldHandleSortedArray() {
        int[] array = {1, 2, 3, 4, 5};
        sorter.sort(array);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, array);
    }

    @Test
    @DisplayName("Should verify recursion depth is bounded O(log n)")
    void shouldHaveBoundedRecursionDepth() {
        int[] array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 1000);
        }

        sorter.sort(array);
        SortMetrics metrics = sorter.getMetrics();
        int maxDepth = metrics.getMaxRecursionDepth();

        double expectedMaxDepth = 2 * (Math.log(array.length) / Math.log(2));
        assertTrue(maxDepth <= expectedMaxDepth + 10,
                String.format("Depth %d should be <= %f", maxDepth, expectedMaxDepth + 10));
    }
}