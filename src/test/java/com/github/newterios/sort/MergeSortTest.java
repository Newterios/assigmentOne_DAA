package com.github.newterios.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MergeSortTest {
    private MergeSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new MergeSort();
    }

    @Test
    @DisplayName("Should sort basic array")
    void shouldSortBasicArray() {
        int[] array = {5, 2, 4, 6, 1, 3};
        int[] expected = {1, 2, 3, 4, 5, 6};

        sorter.sort(array);
        assertArrayEquals(expected, array);
    }

    @Test
    @DisplayName("Should handle large array")
    void shouldHandleLargeArray() {
        int[] array = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        sorter.sort(array);
        assertArrayEquals(expected, array);
    }

    @Test
    @DisplayName("Should collect metrics")
    void shouldCollectMetrics() {
        int[] array = {3, 1, 2};
        sorter.sort(array);

        assertTrue(sorter.getMetrics().getComparisons() > 0);
        assertTrue(sorter.getMetrics().getAssignments() > 0);
    }
}