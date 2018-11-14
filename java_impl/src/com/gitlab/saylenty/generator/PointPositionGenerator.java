package com.gitlab.saylenty.generator;

import com.gitlab.saylenty.entity.Point;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PointPositionGenerator implements ICoordinatesGenerator {

    private int distance;

    @Override
    public Iterator<Point> generate(int distance) {
        this.distance = distance;
        return new PointPositionIterator();
    }

    private final class PointPositionIterator implements Iterator<Point> {

        private int n;
        private Iterator<Point> iterator = Collections.emptyIterator();

        /**
         * @return true if has more available coordinates, otherwise false
         */
        @Override
        public boolean hasNext() {
            return n <= distance || iterator.hasNext();
        }

        /**
         * @return next possible coordinate combination based on {@code distance} value
         */
        @Override
        public Point next() {
            if (iterator.hasNext()) {
                return iterator.next();
            }
            if (n > distance) {
                throw new NoSuchElementException("no more elements available");
            }
            HashSet<Point> buffer = new HashSet<>(4, 1.0f);
            buffer.add(new Point(n, distance - n));
            buffer.add(new Point(-n, distance - n));
            buffer.add(new Point(n, -(distance - n)));
            buffer.add(new Point(-n, -(distance - n)));
            iterator = buffer.iterator();
            ++n;
            return iterator.next();
        }
    }
}
