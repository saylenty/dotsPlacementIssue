package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.infrastructure.Point;
import com.sun.istack.internal.NotNull;

import java.util.List;

import static java.lang.Math.abs;

public interface PointsFinderStrategy {

    /**
     * Calculates the solutions for the problem
     * @param matrix contains all required distances between all points
     * @return all found solutions
     */
    List<List<Point>> findSolution(@NotNull int[][] matrix);

    /**
     * Calculates absolute distance between two points
     * @param x1 x coordinate of the first point
     * @param y1 y coordinate of the first point
     * @param x2 x coordinate of the second point
     * @param y2 y coordinate of the second point
     * @return absolute distance between two points
     */
    static int absDistance(int x1, int y1, int x2, int y2) {
        return abs(x1 - x2) + abs(y1 - y2);
    }
}
