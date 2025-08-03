package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.entity.Point;
import com.gitlab.saylenty.strategy.concurrent.task.PositionFinderCountedCompleter;
import com.gitlab.saylenty.strategy.concurrent.task.PositionFinderTask;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentStrategiesTest {

    private final int[][] validMatrix = {
            {0, 1, 2},
            {1, 0, 3},
            {2, 3, 0}
    };

    private final int[][] invalidMatrix = {
            {0, 1, 2},
            {1, 0, 5},
            {2, 5, 0}
    };

    @Test
    public void taskStrategyFindsSolution() {
        PointsFinderStrategy solver = new PositionFinderTask();
        List<Point[]> solutions = solver.findSolution(validMatrix);
        assertFalse(solutions.isEmpty(), "Expected a solution");
    }

    @Test
    public void taskStrategyReturnsEmptyOnFailure() {
        PointsFinderStrategy solver = new PositionFinderTask();
        List<Point[]> solutions = solver.findSolution(invalidMatrix);
        assertTrue(solutions.isEmpty(), "Expected no solution");
    }

    @Test
    public void countedCompleterFindsSolution() {
        PointsFinderStrategy solver = new PositionFinderCountedCompleter();
        List<Point[]> solutions = solver.findSolution(validMatrix);
        assertFalse(solutions.isEmpty(), "Expected a solution");
    }

    @Test
    public void countedCompleterReturnsEmptyOnFailure() {
        PointsFinderStrategy solver = new PositionFinderCountedCompleter();
        List<Point[]> solutions = solver.findSolution(invalidMatrix);
        assertTrue(solutions.isEmpty(), "Expected no solution");
    }
}