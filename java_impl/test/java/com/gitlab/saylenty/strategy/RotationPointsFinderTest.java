package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.entity.Point;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class RotationPointsFinderTest {
    @Test
    public void testGeneratesRotatedSolutions() {
        int[][] matrix = {
                {0, 1, 2},
                {1, 0, 1},
                {2, 1, 0}
        };
        PointsFinderStrategy solver = new RotationPointsFinder();
        List<Point[]> solutions = solver.findSolution(matrix);
        assertTrue(solutions.size() >= 4, "Expected at least four solutions");
        for (Point[] coords : solutions) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    int dist = PointsFinderStrategy.absDistance(
                            coords[i].getX(), coords[i].getY(),
                            coords[j].getX(), coords[j].getY());
                    assertEquals(matrix[i][j], dist);
                }
            }
        }
        Set<List<Point>> unique = solutions.stream()
                .map(Arrays::asList)
                .collect(Collectors.toSet());
        assertTrue(unique.size() >= 4, "Solutions should be unique");
    }

    @Test
    public void testNoSolutionForInconsistentMatrix() {
        int[][] matrix = {
                {0, 1, 2},
                {1, 0, 5},
                {2, 5, 0}
        };
        PointsFinderStrategy solver = new RotationPointsFinder();
        List<Point[]> solutions = solver.findSolution(matrix);
        assertTrue(solutions.isEmpty(), "Expected no solutions");
    }
}