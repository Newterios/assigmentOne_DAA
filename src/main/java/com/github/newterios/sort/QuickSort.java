package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;
import java.util.Random;

public class QuickSort {

    private final SortMetrics metrics;
    private final Random random;

    public QuickSort() {
        this.metrics = new SortMetrics();
        this.random = new Random();
    }

    public void sort(int[] array) {
        metrics.reset();
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(int[] array, int low, int high) {
        metrics.enterRecursion();
        try {
            while (low < high) {
                int pivotIndex = randomizedPartition(array, low, high);

                // Recurse on smaller partition, iterate on larger
                if (pivotIndex - low < high - pivotIndex) {
                    quickSort(array, low, pivotIndex - 1);
                    low = pivotIndex + 1;
                } else {
                    quickSort(array, pivotIndex + 1, high);
                    high = pivotIndex - 1;
                }
            }
        } finally {
            metrics.exitRecursion();
        }
    }

    private int randomizedPartition(int[] array, int low, int high) {
        int randomIndex = low + random.nextInt(high - low + 1);
        swap(array, randomIndex, high);
        return partition(array, low, high);
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            metrics.incrementComparisons();
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        metrics.incrementAssignments();
        metrics.incrementAssignments();
        metrics.incrementAssignments(); // 3 assignments per swap
    }

    public SortMetrics getMetrics() {
        return metrics;
    }
}