package com.github.newterios.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeterministicSelectTest {
    private DeterministicSelect selector;

    @BeforeEach
    void setUp() {
        selector = new DeterministicSelect();
    }

    @Test
    @DisplayName("Should find k-th smallest element")
    void shouldFindKthSmallest() {
        int[] array = {3, 1, 4, 1, 5, 9, 2, 6};

        assertEquals(1, selector.select(array, 0)); // 0th smallest (min)
        assertEquals(1, selector.select(array, 1)); // 1st smallest
        assertEquals(2, selector.select(array, 2)); // 2nd smallest
        assertEquals(3, selector.select(array, 3)); // 3rd smallest
        assertEquals(9, selector.select(array, 7)); // 7th smallest (max)
    }

    @Test
    @DisplayName("Should match Arrays.sort results")
    void shouldMatchArraysSort() {
        int trials = 100;
        for (int trial = 0; trial < trials; trial++) {
            int[] array = generateRandomArray(100, 1, 1000);
            int k = (int) (Math.random() * array.length);

            int expected = getKthSmallestWithSort(array, k);
            int actual = selector.select(array, k);

            assertEquals(expected, actual,
                    String.format("Trial %d: k=%d, expected=%d, actual=%d",
                            trial, k, expected, actual));
        }
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