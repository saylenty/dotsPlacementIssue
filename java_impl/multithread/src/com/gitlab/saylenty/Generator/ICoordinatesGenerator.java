package com.gitlab.saylenty.Generator;

import com.gitlab.saylenty.Infrastructure.Point;

import java.util.Iterator;

public interface ICoordinatesGenerator {

    /**
     * @param distance distance between two dots
     * @return a set of all possible coordinates combination based on {@code distance} value
     */
    Iterator<Point> generate(int distance);
}
