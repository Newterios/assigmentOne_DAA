package com.github.newterios.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ClosestPairTest {
    private ClosestPair closestPair;

    @BeforeEach
    void setUp() {
        closestPair = new ClosestPair();
    }

    @Test
    @DisplayName("Should find closest pair in simple case")
    void shouldFindClosestPairSimple() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(3, 3),
                new ClosestPair.Point(1, 1.1) // Closest to (1,1)
        };

        ClosestPair.Pair result = closestPair.findClosestPair(points);
        assertNotNull(result);
        assertEquals(0.1, result.distance, 0.01); // Allow some tolerance

        boolean isValidPair =
                (result.p1.equals(new ClosestPair.Point(1, 1)) && result.p2.equals(new ClosestPair.Point(1, 1.1))) ||
                        (result.p1.equals(new ClosestPair.Point(1, 1.1)) && result.p2.equals(new ClosestPair.Point(1, 1)));
        assertTrue(isValidPair, "Should find the closest pair between (1,1) and (1,1.1)");
    }

    @Test
    @DisplayName("Should handle two points")
    void shouldHandleTwoPoints() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 4) // distance should be 5
        };

        ClosestPair.Pair result = closestPair.findClosestPair(points);
        assertNotNull(result);
        assertEquals(5.0, result.distance, 0.001);
    }

    @Test
    @DisplayName("Should handle three points")
    void shouldHandleThreePoints() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(3, 3)
        };

        ClosestPair.Pair result = closestPair.findClosestPair(points);
        assertNotNull(result);
        assertEquals(Math.sqrt(2), result.distance, 0.001);
    }
}