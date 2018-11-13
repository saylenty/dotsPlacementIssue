package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.entity.Point;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RotationPointsFinder extends SimplePointsFinder {

    public RotationPointsFinder() {
        super();
    }

    @Override
    public List<Point[]> findSolution(int[][] matrix) {
        this.matrix = matrix;
        int distance = matrix[0][1]; // distance to 0 dot
        // calculate available placement variants for dot #1 but only in I sector
        Stream<Point> placementVariants = IntStream.rangeClosed(0, distance)
                .mapToObj(j -> new Point(j, distance - j)).distinct();

        placementVariants.forEach(point -> {
            Point[] points = new Point[matrix.length];
            points[0] = new Point(0, 0);
            points[1] = point;
            findRecursively(2, points);
        });

        // rotate the answer over III other sectors of Cartesian coordinate plane
        for (int i = 0, size = result.size(); i < size; i++) {
            Point[] points1 = result.get(i);
            int length = points1.length;
            Point[][] rotated = new Point[3][length];
            for (int j = 0; j < length; j++) {
                Point point1 = points1[j];
                rotated[0][j] = new Point(-point1.getX(), point1.getY());
                rotated[1][j] = new Point(point1.getX(), -point1.getY());
                rotated[2][j] = new Point(-point1.getX(), -point1.getY());
            }
            Collections.addAll(result, rotated);
        }
        return result;
    }
}
