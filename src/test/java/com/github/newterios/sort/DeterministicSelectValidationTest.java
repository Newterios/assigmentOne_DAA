package com.github.newterios.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class DeterministicSelectValidationTest {
    private DeterministicSelect selector;

    @BeforeEach
    void setUp() {
        selector = new DeterministicSelect();
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 500, 1000})
    @DisplayName("Should match Arrays.sort results across 100 random trials for each size")
    void shouldMatchArraysSortAcrossTrials(int size) {
        int trials = 100;
        int correctCount = 0;

        for (int trial = 0; trial < trials; trial++) {
            int[] array = generateRandomArray(size, 1, size * 10);
            int k = (int) (Math.random() * size);

            int expected = getKthSmallestWithSort(array, k);
            int actual = selector.select(array, k);

            if (expected == actual) {
                correctCount++;
            } else {
                System.out.printf("Trial %d failed: size=%d, k=%d, expected=%d, actual=%d%n",
                        trial, size, k, expected, actual);
            }
        }

        assertEquals(trials, correctCount,
                String.format("Should get 100/100 correct for size %d, but got %d/%d",
                        size, correctCount, trials));
    }

    @Test
    @DisplayName("Should handle duplicate values correctly")
    void shouldHandleDuplicates() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4};

        for (int k = 0; k < array.length; k++) {
            int expected = getKthSmallestWithSort(array, k);
            int actual = selector.select(array, k);
            assertEquals(expected, actual,
                    String.format("k=%d should be %d but got %d", k, expected, actual));
        }
    }

    @Test
    @DisplayName("Should handle edge cases")
    void shouldHandleEdgeCases() {
        assertEquals(5, selector.select(new int[]{5}, 0));

        assertEquals(1, selector.select(new int[]{2, 1}, 0));
        assertEquals(2, selector.select(new int[]{2, 1}, 1));

        assertEquals(3, selector.select(new int[]{3, 3, 3, 3}, 2));
    }

    private int[] generateRandomArray(int size, int min, int max) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = min + (int) (Math.random() * (max - min + 1));
        }
        return array;
    }

    private int getKthSmallestWithSort(int[] array, int k) {
        int[] copy = array.clone();
        Arrays.sort(copy);
        return copy[k];
    }
}