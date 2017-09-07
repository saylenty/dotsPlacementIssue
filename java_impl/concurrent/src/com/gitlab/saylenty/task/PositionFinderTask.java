package com.gitlab.saylenty.task;

import com.gitlab.saylenty.generator.ICoordinatesGenerator;
import com.gitlab.saylenty.infrastructure.Point;
import com.gitlab.saylenty.strategy.PointsFinderStrategy;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class PositionFinderTask extends RecursiveTask<List<List<Point>>> implements PointsFinderStrategy {

    private int[][] matrix;
    private final ICoordinatesGenerator generator;
    private final int pointNumber;
    private final List<Point> localResult;
    private final List<List<Point>> result;

    public PositionFinderTask(@NotNull int[][] matrix, @NotNull ICoordinatesGenerator generator) {
        this.matrix = matrix;
        this.generator = generator;
        this.pointNumber = 0;
        this.localResult = new ArrayList<>();
        this.result = new ArrayList<>();
    }

    private PositionFinderTask(int[][] matrix, int pointNumber, ICoordinatesGenerator generator,
                               List<Point> localResult, List<List<Point>> result) {
        this.matrix = matrix;
        this.pointNumber = pointNumber;
        this.generator = generator;
        this.localResult = localResult;
        this.result = result;
    }

    @Override
    protected List<List<Point>> compute() {
        int distance = matrix[0][pointNumber]; // distance to 0 dot
        Iterator<Point> iterator = generator.generate(distance);
        Stack<PositionFinderTask> subTasks = new Stack<>();
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
                    synchronized (result){
                        result.add(localResultCopy);
                    }
                    return result;
                }
                // run next dot calculation
                PositionFinderTask positionFinderTask = new PositionFinderTask(matrix, pointNumber + 1,
                        generator, localResultCopy, result);
                subTasks.push(positionFinderTask);
                positionFinderTask.fork();
            }
        }
        subTasks.forEach(ForkJoinTask::join);
        return result;
    }

    @Override
    public List<List<Point>> findSolution(@NotNull int[][] matrix) {
        this.matrix = matrix;
        // run task and wait until complete
        this.invoke();
        List<List<Point>> result = null;
        try {
            result = this.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
