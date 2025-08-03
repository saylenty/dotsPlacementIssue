package com.gitlab.saylenty;

import com.gitlab.saylenty.entity.Point;
import com.gitlab.saylenty.strategy.PointsFinderStrategy;
import com.gitlab.saylenty.strategy.concurrent.task.PositionFinderTask;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {0, 4, 3, 4, 3, 3, 6, 3, 4, 2},
                {4, 0, 3, 4, 7, 7, 6, 5, 4, 2},
                {3, 3, 0, 3, 6, 6, 9, 6, 7, 5},
                {4, 4, 3, 0, 3, 5, 10, 7, 8, 6},
                {3, 7, 6, 3, 0, 4, 9, 6, 7, 5},
                {3, 7, 6, 5, 4, 0, 5, 2, 7, 5},
                {6, 6, 9, 10, 9, 5, 0, 3, 4, 4},
                {3, 5, 6, 7, 6, 2, 3, 0, 5, 3},
                {4, 4, 7, 8, 7, 7, 4, 5, 0, 2},
                {2, 2, 5, 6, 5, 5, 4, 3, 2, 0}
        };
        // create a task
        PointsFinderStrategy positionFinderTask = new PositionFinderTask();
        // run task and wait until complete
        List<Point[]> result = positionFinderTask.findSolution(matrix);
        // print calculation result
        result.stream().map(Arrays::deepToString).forEach(System.out::println);
    }
}
