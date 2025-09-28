package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;

public class InsertionSort {
    private final SortMetrics metrics;

    public InsertionSort() {
        this.metrics = new SortMetrics();
    }

    public void sort(int[] array) {
        metrics.reset();
        if (array == null || array.length <= 1) {
            return;
        }

        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            metrics.incrementAssignments();
            int j = i - 1;

            while (j >= 0) {
                metrics.incrementComparisons();
                if (array[j] > key) {
                    array[j + 1] = array[j];
                    metrics.incrementAssignments();
                    j--;
                } else {
                    break;
                }
            }
            array[j + 1] = key;
            metrics.incrementAssignments();
        }
    }

    public SortMetrics getMetrics() {
        return metrics;
    }
}