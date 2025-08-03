package com.gitlab.saylenty.strategy.concurrent.task;

import com.gitlab.saylenty.entity.Point;
import com.gitlab.saylenty.generator.PointPositionGenerator;
import com.gitlab.saylenty.strategy.PointsFinderStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class PositionFinderTask extends RecursiveTask<List<Point[]>> implements PointsFinderStrategy {

    private static int[][] matrix;
    private final int pointNumber;
    private Point[] localResult;
    private final List<Point[]> result;

    public PositionFinderTask() {
        this.pointNumber = 0;
        this.result = new LinkedList<>();
    }

    private PositionFinderTask(int pointNumber, Point[] localResult, List<Point[]> result) {
        this.pointNumber = pointNumber;
        this.localResult = localResult;
        this.result = result;
    }

    @Override
    protected List<Point[]> compute() {
        int distance = matrix[0][pointNumber]; // distance to 0 dot
        int[] pnMatrix = matrix[pointNumber]; // distances to check for current point and others
        Iterator<Point> iterator = new PointPositionGenerator().generate(distance);
        ArrayDeque<PositionFinderTask> subTasks = new ArrayDeque<>();
        while (iterator.hasNext()) {
            // get the next possible dot
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
                // create a copy of the current solution list for new threads
                Point[] localResultCopy = localResult.clone();
                // add current found position to that list
                localResultCopy[pointNumber] = p;

                // if it's the last point
                if (pointNumber == matrix.length - 1) {
                    // we found the solution, add it to the final solution list
                    synchronized (result) {
                        result.add(localResultCopy);
                    }
                    return result;
                }
                // run next dot calculation
                PositionFinderTask positionFinderTask = new PositionFinderTask(pointNumber + 1, localResultCopy,
                        result);
                subTasks.push(positionFinderTask);
                positionFinderTask.fork();
            }
        }
        subTasks.forEach(ForkJoinTask::join);
        return result;
    }

    @Override
    public List<Point[]> findSolution(@NotNull int[][] matrix) {
        PositionFinderTask.matrix = matrix;
        this.localResult = new Point[matrix.length];
        // run task and wait until complete
        this.invoke();
        List<Point[]> result = null;
        try {
            result = this.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
