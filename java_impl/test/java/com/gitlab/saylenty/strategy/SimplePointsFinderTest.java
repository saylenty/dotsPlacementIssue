package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.entity.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimplePointsFinderTest {
    @Test
    public void testSimpleTriangulation() {
        int[][] matrix = {
                {0, 1, 2},
                {1, 0, 3},
                {2, 3, 0}
        };
        PointsFinderStrategy solver = new SimplePointsFinder();
        List<Point[]> solutions = solver.findSolution(matrix);
        assertFalse(solutions.isEmpty(), "Expected at least one solution");
        Point[] coords = solutions.get(0);
        assertEquals(new Point(0, 0), coords[0]);
        int n = matrix.length;
        assertEquals(n, coords.length);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int dist = PointsFinderStrategy.absDistance(
                        coords[i].getX(), coords[i].getY(),
                        coords[j].getX(), coords[j].getY());
                assertEquals(matrix[i][j], dist);
            }
        }
    }

    @Test
    public void testNoSolutionForInconsistentMatrix() {
        int[][] matrix = {
                {0, 1, 2},
                {1, 0, 5},
                {2, 5, 0}
        };
        PointsFinderStrategy solver = new SimplePointsFinder();
        List<Point[]> solutions = solver.findSolution(matrix);
        assertTrue(solutions.isEmpty(), "Expected no solutions for inconsistent matrix");
    }
}