package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.entity.Point;
import com.gitlab.saylenty.generator.PointPositionGenerator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class SimplePointsFinder implements PointsFinderStrategy {

    protected int[][] matrix;
    protected final List<Point[]> result;

    public SimplePointsFinder() {
        result = new LinkedList<>();
    }

    @Override
    public List<Point[]> findSolution(int[][] matrix) {
        this.matrix = matrix;
        findRecursively(0, new Point[matrix.length]);
        return result;
    }

    protected void findRecursively(int pointNumber, Point[] localResult) {
        int distance = matrix[0][pointNumber]; // distance to 0 dot
        int[] pnMatrix = this.matrix[pointNumber]; // distances to check for current point and others
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
                // add current found position to that list
                localResult[pointNumber] = p;
                // if it's the last point
                if (pointNumber == matrix.length - 1) {
                    // we found the solution, add it to the final solution list
                    result.add(localResult.clone());
                    localResult[pointNumber] = null;
                    return;
                }
                // run next dot calculation
                findRecursively(pointNumber + 1, localResult);
                localResult[pointNumber] = null;
            }
        }
    }
}
