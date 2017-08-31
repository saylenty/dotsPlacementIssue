package com.gitlab.saylenty.task;

import com.gitlab.saylenty.generator.ICoordinatesGenerator;
import com.gitlab.saylenty.infrastructure.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class PositionFinderCountedCompleter extends CountedCompleter<List<List<Point>>> {

    private final int[][] matrix;
    private final ICoordinatesGenerator generator;
    private final int pointNumber;
    private final List<Point> localResult;
    private final AtomicReference<List<List<Point>>> result;

    public PositionFinderCountedCompleter(int[][] matrix, ICoordinatesGenerator generator) {
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
        Iterator<Point> iterator = generator.generate(distance);
        while (iterator.hasNext()) {
            // get next possible dot
            Point p = iterator.next();
            // if all distances are correct between current dot and all previous
            if (IntStream.range(0, pointNumber).
                    allMatch(i -> abs(p.getX() - localResult.get(i).getX()) + abs(p.getY() - localResult.get(i).getY())
                            == matrix[pointNumber][i])) {
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
}
