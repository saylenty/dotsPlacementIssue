package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.infrastructure.Point;
import com.sun.istack.internal.NotNull;

import java.util.List;

public interface PointsFinderStrategy {
    List<List<Point>> findSolution(@NotNull int[][] matrix);
}
