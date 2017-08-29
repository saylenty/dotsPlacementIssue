package com.gitlab.saylenty.Task;

import com.gitlab.saylenty.Generator.ICoordinatesGenerator;
import com.gitlab.saylenty.Infrastructure.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class PositionFinderTask extends RecursiveTask<List<List<Point>>> {

    private int[][] matrix;
    private ICoordinatesGenerator generator;
    private int pointNumber;
    private List<Point> localResult;
    private List<List<Point>> result = new ArrayList<>();

    public PositionFinderTask(int[][] matrix, ICoordinatesGenerator generator) {
        this.matrix = matrix;
        this.generator = generator;
        this.pointNumber = 0;
        this.localResult = new ArrayList<>();
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
                    result.add(localResultCopy);
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
}
