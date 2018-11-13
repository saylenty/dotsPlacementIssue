package com.gitlab.saylenty.strategy.concurrent.task;

import com.gitlab.saylenty.entity.Point;
import com.gitlab.saylenty.generator.PointPositionGenerator;
import com.gitlab.saylenty.strategy.PointsFinderStrategy;
import com.sun.istack.internal.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class PositionFinderCountedCompleter extends CountedCompleter<List<Point[]>> implements PointsFinderStrategy {

    private static int[][] matrix;
    private final int pointNumber;
    private Point[] localResult;
    private final AtomicReference<List<Point[]>> result;

    public PositionFinderCountedCompleter() {
        this.pointNumber = 0;
        this.result = new AtomicReference<>(new LinkedList<>());
    }

    private PositionFinderCountedCompleter(PositionFinderCountedCompleter p, int pointNumber,
                                           Point[] localResult, AtomicReference<List<Point[]>> result) {
        super(p);
        this.pointNumber = pointNumber;
        this.localResult = localResult;
        this.result = result;
    }


    @Override
    public void compute() {
        if (pointNumber >= matrix.length) {
            return;
        }
        int distance = matrix[0][pointNumber]; // distance to 0 dot
        int[] pnMatrix = matrix[pointNumber]; // distances to check for current point and others
        Iterator<Point> iterator = new PointPositionGenerator(distance);
        while (iterator.hasNext()) {
            // get next possible dot
            Point p = iterator.next();
            int candidateX = p.getX();
            int candidateY = p.getY();
            // if all distances are correct between current dot and all previous
            if (IntStream.range(0, pointNumber).
                    allMatch(i -> {
                        Point accepted = localResult[i];
                        int acceptedX = accepted.getX();
                        int acceptedY = accepted.getY();
                        return PointsFinderStrategy.absDistance(candidateX, candidateY, acceptedX, acceptedY)
                                == pnMatrix[i];
                    })) {
                // create a copy of current solution list for new threads
                Point[] localResultCopy = localResult.clone();
                // add current found position to that list
                localResultCopy[pointNumber] = p;

                // if it's the last point
                if (pointNumber == matrix.length - 1) {
                    // we found the solution, add it to the final solution list
                    result.getAndUpdate(lists -> {
                        lists.add(localResultCopy);
                        return lists;
                    });
                    propagateCompletion();
                    return;
                }

                addToPendingCount(1);
                // run next dot calculation
                PositionFinderCountedCompleter positionFinderTask = new PositionFinderCountedCompleter(this,
                        pointNumber + 1, localResultCopy, result);
                positionFinderTask.fork();
            }
        }
        propagateCompletion();
    }

    @Override
    public List<Point[]> getRawResult() {
        return result.get();
    }

    @Override
    public List<Point[]> findSolution(@NotNull int[][] matrix) {
        PositionFinderCountedCompleter.matrix = matrix;
        this.localResult = new Point[matrix.length];
        return this.invoke();
    }
}
