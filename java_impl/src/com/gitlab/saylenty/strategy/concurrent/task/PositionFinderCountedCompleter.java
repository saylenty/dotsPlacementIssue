package com.gitlab.saylenty.strategy.concurrent.task;

import com.gitlab.saylenty.generator.ICoordinatesGenerator;
import com.gitlab.saylenty.entity.Point;
import com.gitlab.saylenty.strategy.PointsFinderStrategy;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class PositionFinderCountedCompleter extends CountedCompleter<List<List<Point>>> implements PointsFinderStrategy {

    private int[][] matrix;
    private final ICoordinatesGenerator generator;
    private final int pointNumber;
    private final List<Point> localResult;
    private final AtomicReference<List<List<Point>>> result;

    public PositionFinderCountedCompleter(@NotNull int[][] matrix, @NotNull ICoordinatesGenerator generator) {
        this.matrix = matrix;
        this.generator = generator;
        this.pointNumber = 0;
        this.localResult = new ArrayList<>();
        this.result = new AtomicReference<>(new ArrayList<>());
    }

    private PositionFinderCountedCompleter(PositionFinderCountedCompleter p, int[][] matrix, int pointNumber,
                                           ICoordinatesGenerator generator,
                                           List<Point> localResult, AtomicReference<List<List<Point>>> result) {
        super(p);
        this.matrix = matrix;
        this.pointNumber = pointNumber;
        this.generator = generator;
        this.localResult = localResult;
        this.result = result;
    }


    @Override
    public void compute() {
        if (pointNumber >= matrix.length) return;
        int distance = matrix[0][pointNumber]; // distance to 0 dot
        int[] pnMatrix = this.matrix[pointNumber]; // distances to check for current point and others
        Iterator<Point> iterator = generator.generate(distance);
        while (iterator.hasNext()) {
            // get next possible dot
            Point p = iterator.next();
            int candidateX = p.getX();
            int candidateY = p.getY();
            // if all distances are correct between current dot and all previous
            if (IntStream.range(0, pointNumber).
                    allMatch(i -> {
                        Point accepted = localResult.get(i);
                        int acceptedX = accepted.getX();
                        int acceptedY = accepted.getY();
                        return PointsFinderStrategy.absDistance(candidateX, candidateY, acceptedX, acceptedY)
                                == pnMatrix[i];
                    })) {
                // create a copy of current solution list for new threads
                List<Point> localResultCopy = new ArrayList<>(localResult);
                // add current found position to that list
                localResultCopy.add(p);

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
                PositionFinderCountedCompleter positionFinderTask = new PositionFinderCountedCompleter(this, matrix,
                        pointNumber + 1, generator, localResultCopy, result);
                positionFinderTask.fork();
            }
        }
        propagateCompletion();
    }

    @Override
    public List<List<Point>> getRawResult() {
        return result.get();
    }

    @Override
    public List<List<Point>> findSolution(@NotNull int[][] matrix) {
        this.matrix = matrix;
        return this.invoke();
    }
}
