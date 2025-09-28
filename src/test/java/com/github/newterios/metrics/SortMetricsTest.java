package com.github.newterios.metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SortMetricsTest {
    private SortMetrics metrics;

    @BeforeEach
    void setUp() {
        metrics = new SortMetrics();
    }

    @Test
    @DisplayName("Should initialize with zero values")
    void shouldInitializeWithZeroValues() {
        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getAssignments());
        assertEquals(0, metrics.getTotalOperations());
    }

    @Test
    @DisplayName("Should increment comparisons correctly")
    void shouldIncrementComparisons() {
        metrics.incrementComparisons();
        assertEquals(1, metrics.getComparisons());

        metrics.incrementComparisons();
        metrics.incrementComparisons();
        assertEquals(3, metrics.getComparisons());
    }

    @Test
    @DisplayName("Should increment assignments correctly")
    void shouldIncrementAssignments() {
        metrics.incrementAssignments();
        assertEquals(1, metrics.getAssignments());

        metrics.incrementAssignments();
        metrics.incrementAssignments();
        assertEquals(3, metrics.getAssignments());
    }

    @Test
    @DisplayName("Should add multiple comparisons and assignments")
    void shouldAddMultipleOperations() {
        metrics.addComparisons(5);
        metrics.addAssignments(3);

        assertEquals(5, metrics.getComparisons());
        assertEquals(3, metrics.getAssignments());
        assertEquals(8, metrics.getTotalOperations());
    }

    @Test
    @DisplayName("Should reset metrics correctly")
    void shouldResetMetrics() {
        metrics.incrementComparisons();
        metrics.incrementAssignments();
        metrics.startTimer();
        try { Thread.sleep(1); } catch (InterruptedException e) {}
        metrics.stopTimer();

        metrics.reset();

        assertEquals(0, metrics.getComparisons());
        assertEquals(0, metrics.getAssignments());
        assertEquals(0, metrics.getTotalOperations());
    }

    @Test
    @DisplayName("Should measure execution time")
    void shouldMeasureExecutionTime() {
        metrics.startTimer();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        metrics.stopTimer();

        assertTrue(metrics.getExecutionTimeNanos() > 0);
        assertTrue(metrics.getExecutionTimeMillis() > 0);
    }

    @Test
    @DisplayName("Should provide meaningful string representation")
    void shouldProvideMeaningfulStringRepresentation() {
        metrics.incrementComparisons();
        metrics.incrementAssignments();
        metrics.startTimer();
        metrics.stopTimer();

        String representation = metrics.toString();
        assertTrue(representation.contains("SortMetrics"));
        assertTrue(representation.contains("comparisons=1"));
        assertTrue(representation.contains("assignments=1"));
        assertTrue(representation.contains("totalOperations=2"));
    }
}