package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;
import java.util.Arrays;

public class DeterministicSelect {

    private final SortMetrics metrics;

    public DeterministicSelect() {
        this.metrics = new SortMetrics();
    }

    public int select(int[] array, int k) {
        metrics.reset();
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k must be between 0 and array.length - 1");
        }
        return select(array, 0, array.length - 1, k);
    }

    private int select(int[] array, int left, int right, int k) {
        metrics.enterRecursion();
        try {
            if (left == right) {
                return array[left];
            }

            int pivotIndex = medianOfMediansPartition(array, left, right);

            if (k == pivotIndex) {
                return array[k];
            } else if (k < pivotIndex) {
                return select(array, left, pivotIndex - 1, k);
            } else {
                return select(array, pivotIndex + 1, right, k);
            }
        } finally {
            metrics.exitRecursion();
        }
    }

    private int medianOfMediansPartition(int[] array, int left, int right) {
        int pivotValue = medianOfMedians(array, left, right);

        for (int i = left; i <= right; i++) {
            metrics.incrementComparisons();
            if (array[i] == pivotValue) {
                swap(array, i, right);
                break;
            }
        }

        return partition(array, left, right);
    }

    private int medianOfMedians(int[] array, int left, int right) {
        int n = right - left + 1;
        if (n <= 5) {
            return medianOfFive(array, left, right);
        }

        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);
            medians[i] = medianOfFive(array, groupLeft, groupRight);
        }

        return select(medians, 0, medians.length - 1, medians.length / 2);
    }

    private int medianOfFive(int[] array, int left, int right) {
        int[] group = Arrays.copyOfRange(array, left, right + 1);
        insertionSort(group);
        return group[group.length / 2];
    }

    private void insertionSort(int[] array) {
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

    private int partition(int[] array, int left, int right) {
        int pivot = array[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            metrics.incrementComparisons();
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, right);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        metrics.incrementAssignments();
        metrics.incrementAssignments();
        metrics.incrementAssignments();
    }

    public SortMetrics getMetrics() {
        return metrics;
    }
}