package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.generator.PointPositionGenerator;
import com.gitlab.saylenty.infrastructure.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class SimplePointsFinder implements PointsFinderStrategy {

    private int[][] matrix;
    private List<List<Point>> result;
    private PointPositionGenerator generator;

    public SimplePointsFinder(PointPositionGenerator generator) {
        this.generator = generator;
        result = new ArrayList<>();
    }

    @Override
    public List<List<Point>> findSolution(int[][] matrix) {
        this.matrix = matrix;
        findRecursively(0, new ArrayList<>(matrix.length));
        return result;
    }

    private void findRecursively(int pointNumber, List<Point> localResult){
        int distance = matrix[0][pointNumber]; // distance to 0 dot
        Iterator<Point> iterator = generator.generate(distance);
        while (iterator.hasNext()) {
            // get next possible dot
            Point p = iterator.next();
            // if all distances are correct between current dot and all previous
            if (IntStream.range(0, pointNumber).
                    allMatch(i -> abs(p.getX() - localResult.get(i).getX()) + abs(p.getY() - localResult.get(i).getY())
                            == matrix[pointNumber][i])) {
                // add current found position to that list
                localResult.add(p);
                // if it's the last point
                if (pointNumber == matrix.length - 1) {
                    // we found the solution, add it to the final solution list
                    result.add(new ArrayList<>(localResult));
                    localResult.remove(p);
                    return;
                }
                // run next dot calculation
                findRecursively(pointNumber + 1, localResult);
                localResult.remove(p);
            }
        }
    }
}
