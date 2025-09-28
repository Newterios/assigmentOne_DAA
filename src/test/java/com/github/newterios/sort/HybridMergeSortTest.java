package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;
import com.github.newterios.performance.ArrayGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HybridMergeSortTest {
    private HybridMergeSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new HybridMergeSort();
    }

    @Test
    @DisplayName("Should handle null array")
    void shouldHandleNullArray() {
        assertDoesNotThrow(() -> sorter.sort(null));
    }

    @Test
    @DisplayName("Should handle empty array")
    void shouldHandleEmptyArray() {
        int[] array = {};
        sorter.sort(array);
        assertEquals(0, array.length);
    }

    @Test
    @DisplayName("Should handle single element array")
    void shouldHandleSingleElementArray() {
        int[] array = {5};
        sorter.sort(array);
        assertArrayEquals(new int[]{5}, array);

        SortMetrics metrics = sorter.getMetrics();
        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getAssignments());
    }

    @Test
    @DisplayName("Should sort small array with insertion sort")
    void shouldSortSmallArrayWithInsertionSort() {
        int[] array = {5, 2, 4, 6, 1, 3};
        int[] expected = {1, 2, 3, 4, 5, 6};

        sorter.sort(array);
        assertArrayEquals(expected, array);

        SortMetrics metrics = sorter.getMetrics();
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getAssignments() > 0);
    }

    @Test
    @DisplayName("Should sort already sorted array")
    void shouldSortAlreadySortedArray() {
        int[] array = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(array);
        assertArrayEquals(expected, array);
    }

    @Test
    @DisplayName("Should sort reverse sorted array")
    void shouldSortReverseSortedArray() {
        int[] array = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        sorter.sort(array);
        assertArrayEquals(expected, array);
    }

    @Test
    @DisplayName("Should sort array with duplicates")
    void shouldSortArrayWithDuplicates() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        int[] expected = {1, 1, 2, 3, 3, 4, 5, 5, 6, 9};

        sorter.sort(array);
        assertArrayEquals(expected, array);
    }

    @Test
    @DisplayName("Should sort array with negative numbers")
    void shouldSortArrayWithNegativeNumbers() {
        int[] array = {-5, 2, -3, 0, 4, -1};
        int[] expected = {-5, -3, -1, 0, 2, 4};

        sorter.sort(array);
        assertArrayEquals(expected, array);
    }

    @Test
    @DisplayName("Should use reusable buffer for merging")
    void shouldUseReusableBuffer() {
        int[] array = {8, 3, 6, 1, 9, 4, 2, 7, 5};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        sorter.sort(array);
        assertArrayEquals(expected, array);

        // Verify metrics are collected
        SortMetrics metrics = sorter.getMetrics();
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getAssignments() > 0);
        assertTrue(metrics.getExecutionTimeNanos() >= 0);
    }

    @ParameterizedTest
    @MethodSource("provideTestArrays")
    @DisplayName("Should sort various array types")
    void shouldSortVariousArrayTypes(int[] array, String description) {
        int[] sorted = ArrayGenerator.copyArray(array);
        sorter.sort(sorted);

        assertTrue(isSorted(sorted), "Array should be sorted: " + description);
    }

    private static Stream<Arguments> provideTestArrays() {
        return Stream.of(
                Arguments.of(ArrayGenerator.generateRandomArray(100, 1, 1000), "Random array 100 elements"),
                Arguments.of(ArrayGenerator.generateSortedArray(50, 1), "Sorted array 50 elements"),
                Arguments.of(ArrayGenerator.generateReverseSortedArray(50, 1), "Reverse sorted array 50 elements"),
                Arguments.of(ArrayGenerator.generateNearlySortedArray(100, 1, 0.1), "Nearly sorted array 100 elements"),
                Arguments.of(ArrayGenerator.generateArrayWithDuplicates(100, 10), "Array with many duplicates"),
                Arguments.of(ArrayGenerator.generateRandomArray(1000, -500, 500), "Random array with negatives 1000 elements"),
                Arguments.of(new int[]{1}, "Single element"),
                Arguments.of(new int[]{2, 1}, "Two elements"),
                Arguments.of(new int[]{3, 3, 3}, "All same elements"),
                Arguments.of(new int[]{1, 2, 3, 4, 5, 6, 7}, "Exactly insertion sort threshold")
        );
    }

    @Test
    @DisplayName("Should collect proper metrics")
    void shouldCollectProperMetrics() {
        int[] array = {5, 2, 4, 6, 1, 3};
        sorter.getMetrics().startTimer();
        sorter.sort(array);
        sorter.getMetrics().stopTimer();

        SortMetrics metrics = sorter.getMetrics();

        assertTrue(metrics.getComparisons() > 0, "Should have comparisons");
        assertTrue(metrics.getAssignments() > 0, "Should have assignments");
        assertTrue(metrics.getTotalOperations() > 0, "Should have total operations");
        assertTrue(metrics.getExecutionTimeNanos() >= 0, "Should have execution time");
    }

    private boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }
}