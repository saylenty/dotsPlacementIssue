package com.gitlab.saylenty.generator;

import com.gitlab.saylenty.entity.Point;

import java.util.HashSet;
import java.util.Iterator;

public class PointPositionGenerator implements ICoordinatesGenerator {

    /**
     * Create a bunch of coordinates that satisfy the following rule
     * @implNote The distance is not the weight of the line which directly connects two dots => lines are angular
     * like a line that connects two dots following only the edges of a squares in the copy-book
     */
    @Override
    public Iterator<Point> generate(int distance){
        HashSet<Point> buffer = new HashSet<>(4 * distance, 1.0f);
        for (int j = 0; j <= distance; j++) {
            buffer.add(new Point(j, distance - j));
            buffer.add(new Point(-j, distance - j));
            buffer.add(new Point(j, -(distance - j)));
            buffer.add(new Point(-j, -(distance - j)));
        }
        return buffer.iterator();
    }
}
