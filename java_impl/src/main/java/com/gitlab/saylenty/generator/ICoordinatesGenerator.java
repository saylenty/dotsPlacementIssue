package com.gitlab.saylenty.generator;

import com.gitlab.saylenty.entity.Point;

import java.util.Iterator;

public interface ICoordinatesGenerator {

    /**
     * @param distance distance between two dots
     * @return a bunch of possible coordinates combination based on {@code distance} value
     */
    Iterator<Point> generate(int distance);
}
