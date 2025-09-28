package com.github.newterios.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InsertionSortTest {
    private InsertionSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new InsertionSort();
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
    @DisplayName("Should handle single element")
    void shouldHandleSingleElement() {
        int[] array = {1};
        sorter.sort(array);
        assertArrayEquals(new int[]{1}, array);
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