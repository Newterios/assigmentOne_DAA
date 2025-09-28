package com.github.newterios.geometry;

import com.github.newterios.metrics.SortMetrics;
import java.util.*;

public class ClosestPair {
    private final SortMetrics metrics;

    public ClosestPair() {
        this.metrics = new SortMetrics();
    }

    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double distanceTo(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static class Pair {
        public final Point p1;
        public final Point p2;
        public final double distance;

        public Pair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = p1.distanceTo(p2);
        }

        @Override
        public String toString() {
            return String.format("Pair{%s, %s, distance=%.4f}", p1, p2, distance);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair pair = (Pair) obj;
            return Double.compare(pair.distance, distance) == 0 &&
                    ((p1.equals(pair.p1) && p2.equals(pair.p2)) ||
                            (p1.equals(pair.p2) && p2.equals(pair.p1)));
        }
    }

    public Pair findClosestPair(Point[] points) {
        metrics.reset();
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        Point[] pointsByX = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsByX, (a, b) -> Double.compare(a.x, b.x));
        Point[] pointsByY = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsByY, (a, b) -> Double.compare(a.y, b.y));

        metrics.startTimer();
        Pair result = findClosestPairRecursive(pointsByX, pointsByY);
        metrics.stopTimer();

        return result;
    }

    private Pair findClosestPairRecursive(Point[] pointsByX, Point[] pointsByY) {
        metrics.enterRecursion();
        try {
            int n = pointsByX.length;

            if (n <= 3) {
                return bruteForceClosest(pointsByX);
            }

            int mid = n / 2;
            Point midPoint = pointsByX[mid];

            Point[] leftPointsX = Arrays.copyOfRange(pointsByX, 0, mid);
            Point[] rightPointsX = Arrays.copyOfRange(pointsByX, mid, n);

            List<Point> leftPointsY = new ArrayList<>();
            List<Point> rightPointsY = new ArrayList<>();

            for (Point point : pointsByY) {
                if (point.x <= midPoint.x) {
                    leftPointsY.add(point);
                } else {
                    rightPointsY.add(point);
                }
            }

            Pair leftClosest = findClosestPairRecursive(leftPointsX, leftPointsY.toArray(new Point[0]));
            Pair rightClosest = findClosestPairRecursive(rightPointsX, rightPointsY.toArray(new Point[0]));

            double minDistance = Math.min(leftClosest.distance, rightClosest.distance);
            Pair minPair = leftClosest.distance < rightClosest.distance ? leftClosest : rightClosest;

            Pair stripClosest = findClosestInStrip(pointsByY, midPoint, minDistance);
            if (stripClosest != null && stripClosest.distance < minDistance) {
                return stripClosest;
            }

            return minPair;
        } finally {
            metrics.exitRecursion();
        }
    }

    private Pair bruteForceClosest(Point[] points) {
        double minDistance = Double.MAX_VALUE;
        Pair closestPair = null;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                    closestPair = new Pair(points[i], points[j]);
                }
            }
        }

        return closestPair;
    }

    private Pair findClosestInStrip(Point[] pointsByY, Point midPoint, double minDistance) {
        List<Point> strip = new ArrayList<>();
        for (Point point : pointsByY) {
            if (Math.abs(point.x - midPoint.x) < minDistance) {
                strip.add(point);
            }
        }

        double closestDistance = minDistance;
        Pair closestPair = null;

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < minDistance; j++) {
                double dist = strip.get(i).distanceTo(strip.get(j));
                if (dist < closestDistance) {
                    closestDistance = dist;
                    closestPair = new Pair(strip.get(i), strip.get(j));
                }
            }
        }

        return closestPair;
    }

    public Pair findClosestPairBruteForce(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        double minDistance = Double.MAX_VALUE;
        Pair closestPair = null;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < minDistance) {
                    minDistance = dist;
                    closestPair = new Pair(points[i], points[j]);
                }
            }
        }

        return closestPair;
    }

    public SortMetrics getMetrics() {
        return metrics;
    }
}