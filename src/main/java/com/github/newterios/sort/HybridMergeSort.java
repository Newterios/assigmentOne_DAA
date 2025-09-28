package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;

public class HybridMergeSort {
    private static final int INSERTION_SORT_THRESHOLD = 7;

    private final SortMetrics metrics;
    private int[] buffer;

    public HybridMergeSort() {
        this.metrics = new SortMetrics();
    }

    public void sort(int[] array) {
        metrics.reset();
        if (array == null || array.length <= 1) {
            return;
        }

        buffer = new int[array.length / 2 + 1];
        sort(array, 0, array.length - 1);
    }

    private void sort(int[] array, int left, int right) {
        if (right - left + 1 <= INSERTION_SORT_THRESHOLD) {
            insertionSort(array, left, right);
            return;
        }

        int mid = left + (right - left) / 2;
        sort(array, left, mid);
        sort(array, mid + 1, right);
        linearMerge(array, left, mid, right);
    }

    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];
            metrics.incrementAssignments();
            int j = i - 1;

            while (j >= left) {
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

    private void linearMerge(int[] array, int left, int mid, int right) {
        int leftSize = mid - left + 1;

        System.arraycopy(array, left, buffer, 0, leftSize);
        metrics.addAssignments(leftSize);

        int i = 0, j = mid + 1, k = left;

        while (i < leftSize && j <= right) {
            metrics.incrementComparisons();
            if (buffer[i] <= array[j]) {
                array[k] = buffer[i];
                metrics.incrementAssignments();
                i++;
            } else {
                array[k] = array[j];
                metrics.incrementAssignments();
                j++;
            }
            k++;
        }

        while (i < leftSize) {
            array[k] = buffer[i];
            metrics.incrementAssignments();
            i++;
            k++;
        }
    }

    public SortMetrics getMetrics() {
        return metrics;
    }
}