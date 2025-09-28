package com.github.newterios.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ClosestPairValidationTest {
    private ClosestPair closestPair;

    @BeforeEach
    void setUp() {
        closestPair = new ClosestPair();
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 500, 1000, 2000})
    @DisplayName("Should match brute force results for n ≤ 2000")
    void shouldMatchBruteForceForSmallN(int size) {
        ClosestPair.Point[] points = generateRandomPoints(size, -1000, 1000);

        ClosestPair.Pair expected = closestPair.findClosestPairBruteForce(points);
        ClosestPair.Pair actual = closestPair.findClosestPair(points);

        assertEquals(expected.distance, actual.distance, 1e-10,
                String.format("For size %d: expected distance %.6f, got %.6f",
                        size, expected.distance, actual.distance));
    }

    @Test
    @DisplayName("Should handle large n efficiently")
    void shouldHandleLargeNEfficiently() {
        ClosestPair.Point[] points = generateRandomPoints(10000, -10000, 10000);

        long startTime = System.nanoTime();
        ClosestPair.Pair result = closestPair.findClosestPair(points);
        long endTime = System.nanoTime();

        assertNotNull(result);
        assertTrue(result.distance >= 0);

        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Closest pair for 10000 points took %.2f ms%n", executionTimeMs);

        assertTrue(executionTimeMs < 2000,
                "Algorithm should complete in under 2 seconds for 10000 points");
    }

    @Test
    @DisplayName("Should handle edge cases")
    void shouldHandleEdgeCases() {
        ClosestPair.Point[] twoPoints = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(1, 1)
        };
        ClosestPair.Pair result = closestPair.findClosestPair(twoPoints);
        assertEquals(Math.sqrt(2), result.distance, 1e-10);

        ClosestPair.Point[] threePoints = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 3),
                new ClosestPair.Point(1, 1) // This should be closest to (0,0)
        };
        result = closestPair.findClosestPair(threePoints);
        assertEquals(Math.sqrt(2), result.distance, 1e-10);

        ClosestPair.Point[] duplicatePoints = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(2, 2)
        };
        result = closestPair.findClosestPair(duplicatePoints);
        assertEquals(0.0, result.distance, 1e-10);
    }

    @Test
    @DisplayName("Should verify O(n log n) behavior")
    void shouldShowNLogNBehavior() {
        int[] sizes = {100, 1000, 10000};
        double[] times = new double[sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            ClosestPair.Point[] points = generateRandomPoints(sizes[i], -10000, 10000);

            long startTime = System.nanoTime();
            closestPair.findClosestPair(points);
            long endTime = System.nanoTime();

            times[i] = (endTime - startTime) / 1_000_000.0;
            System.out.printf("Size %d: %.2f ms%n", sizes[i], times[i]);
        }


        double ratio = times[2] / times[0];
        System.out.printf("Time ratio from 100 to 10000: %.2f%n", ratio);

        assertTrue(ratio > 5 && ratio < 1000,
                String.format("Time ratio %.2f should be between 5 and 1000 for O(n log n)", ratio));
    }

    private ClosestPair.Point[] generateRandomPoints(int count, double min, double max) {
        ClosestPair.Point[] points = new ClosestPair.Point[count];
        for (int i = 0; i < count; i++) {
            double x = min + Math.random() * (max - min);
            double y = min + Math.random() * (max - min);
            points[i] = new ClosestPair.Point(x, y);
        }
        return points;
    }
}