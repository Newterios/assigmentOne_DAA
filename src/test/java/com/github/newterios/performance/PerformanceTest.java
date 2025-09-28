package com.github.newterios.performance;

import com.github.newterios.metrics.SortMetrics;
import com.github.newterios.sort.HybridMergeSort;
import com.github.newterios.sort.InsertionSort;
import com.github.newterios.sort.MergeSort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PerformanceTest {
    private HybridMergeSort hybridSorter;
    private InsertionSort insertionSorter;
    private MergeSort mergeSorter;

    @BeforeEach
    void setUp() {
        hybridSorter = new HybridMergeSort();
        insertionSorter = new InsertionSort();
        mergeSorter = new MergeSort();
    }

    @Test
    @DisplayName("Should handle different array sizes efficiently")
    void shouldHandleDifferentArraySizes() {
        int[] smallArray = ArrayGenerator.generateRandomArray(10, 1, 100);
        int[] mediumArray = ArrayGenerator.generateRandomArray(100, 1, 1000);
        int[] largeArray = ArrayGenerator.generateRandomArray(1000, 1, 10000);

        assertDoesNotThrow(() -> {
            hybridSorter.sort(ArrayGenerator.copyArray(smallArray));
        });

        assertDoesNotThrow(() -> {
            hybridSorter.sort(ArrayGenerator.copyArray(mediumArray));
        });

        assertDoesNotThrow(() -> {
            hybridSorter.sort(ArrayGenerator.copyArray(largeArray));
        });
    }

    @Test
    @DisplayName("Should demonstrate hybrid advantage on mixed arrays")
    void shouldDemonstrateHybridAdvantage() {
        int[] array = ArrayGenerator.generateRandomArray(100, 1, 1000);
        int[] arrayCopy1 = ArrayGenerator.copyArray(array);
        int[] arrayCopy2 = ArrayGenerator.copyArray(array);

        hybridSorter.sort(array);
        insertionSorter.sort(arrayCopy1);
        mergeSorter.sort(arrayCopy2);

        SortMetrics hybridMetrics = hybridSorter.getMetrics();
        SortMetrics insertionMetrics = insertionSorter.getMetrics();
        SortMetrics mergeMetrics = mergeSorter.getMetrics();

        assertTrue(hybridMetrics.getTotalOperations() <= insertionMetrics.getTotalOperations() ||
                hybridMetrics.getExecutionTimeNanos() <= insertionMetrics.getExecutionTimeNanos());
    }

    @Test
    @DisplayName("Should work with different data patterns")
    void shouldWorkWithDifferentDataPatterns() {
        int[][] testArrays = {
                ArrayGenerator.generateSortedArray(50, 1),
                ArrayGenerator.generateReverseSortedArray(50, 1),
                ArrayGenerator.generateNearlySortedArray(50, 1, 0.2),
                ArrayGenerator.generateArrayWithDuplicates(50, 5)
        };

        for (int[] array : testArrays) {
            int[] copy = ArrayGenerator.copyArray(array);
            assertDoesNotThrow(() -> hybridSorter.sort(copy));
            assertTrue(isSorted(copy), "Array should be sorted");
        }
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