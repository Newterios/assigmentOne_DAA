package com.github.newterios.metrics;

public class SortMetrics {
    private long comparisons;
    private long assignments;
    private long startTime;
    private long endTime;
    private int currentRecursionDepth;
    private int maxRecursionDepth;

    public void reset() {
        comparisons = 0;
        assignments = 0;
        startTime = 0;
        endTime = 0;
        currentRecursionDepth = 0;
        maxRecursionDepth = 0;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public void incrementComparisons() {
        comparisons++;
    }

    public void incrementAssignments() {
        assignments++;
    }

    public void addComparisons(long count) {
        comparisons += count;
    }

    public void addAssignments(long count) {
        assignments += count;
    }

    public void enterRecursion() {
        currentRecursionDepth++;
        maxRecursionDepth = Math.max(maxRecursionDepth, currentRecursionDepth);
    }

    public void exitRecursion() {
        currentRecursionDepth--;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getAssignments() {
        return assignments;
    }

    public long getTotalOperations() {
        return comparisons + assignments;
    }

    public long getExecutionTimeNanos() {
        return endTime - startTime;
    }

    public double getExecutionTimeMillis() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public int getCurrentRecursionDepth() {
        return currentRecursionDepth;
    }

    @Override
    public String toString() {
        return String.format(
                "SortMetrics{comparisons=%d, assignments=%d, totalOperations=%d, maxDepth=%d, time=%.3fms}",
                comparisons, assignments, getTotalOperations(), maxRecursionDepth, getExecutionTimeMillis()
        );
    }
}