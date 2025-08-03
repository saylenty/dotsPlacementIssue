package com.gitlab.saylenty.strategy;

import com.gitlab.saylenty.entity.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RotationPointsFinder extends SimplePointsFinder {

    @Override
    public List<Point[]> findSolution(@NotNull int[][] matrix) {
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
            Point[] points = result.get(i);
            Point[][] rotated = new Point[3][points.length];
            int j = 0;
            for (Point point : points) {
                int x = point.getX();
                int y = point.getY();
                rotated[0][j] = new Point(-x, y);
                rotated[1][j] = new Point(x, -y);
                rotated[2][j++] = new Point(-x, -y);
            }
            Collections.addAll(result, rotated);
        }
        return result;
    }
}
