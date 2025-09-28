package com.github.newterios.sort;

import com.github.newterios.metrics.SortMetrics;

public class MergeSort {
    private final SortMetrics metrics;
    private int[] tempArray;

    public MergeSort() {
        this.metrics = new SortMetrics();
    }

    public void sort(int[] array) {
        metrics.reset();
        if (array == null || array.length <= 1) {
            return;
        }

        tempArray = new int[array.length];
        sort(array, 0, array.length - 1);
    }

    private void sort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            sort(array, left, mid);
            sort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        System.arraycopy(array, left, tempArray, left, right - left + 1);
        metrics.addAssignments(right - left + 1);

        int i = left, j = mid + 1, k = left;

        while (i <= mid && j <= right) {
            metrics.incrementComparisons();
            if (tempArray[i] <= tempArray[j]) {
                array[k] = tempArray[i];
                metrics.incrementAssignments();
                i++;
            } else {
                array[k] = tempArray[j];
                metrics.incrementAssignments();
                j++;
            }
            k++;
        }

        while (i <= mid) {
            array[k] = tempArray[i];
            metrics.incrementAssignments();
            i++;
            k++;
        }
    }

    public SortMetrics getMetrics() {
        return metrics;
    }
}