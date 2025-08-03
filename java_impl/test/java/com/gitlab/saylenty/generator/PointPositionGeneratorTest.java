package com.gitlab.saylenty.generator;

import com.gitlab.saylenty.entity.Point;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PointPositionGeneratorTest {
    @Test
    public void testDistanceOneGeneratesFourUniquePoints() {
        Iterator<Point> iterator = new PointPositionGenerator().generate(1);
        Set<Point> points = new HashSet<>();
        while (iterator.hasNext()) {
            points.add(iterator.next());
        }
        Set<Point> expected = new HashSet<>();
        expected.add(new Point(0, 1));
        expected.add(new Point(0, -1));
        expected.add(new Point(1, 0));
        expected.add(new Point(-1, 0));
        assertEquals(expected, points);
    }

    @Test
    public void testZeroDistanceProducesOriginOnly() {
        Iterator<Point> iterator = new PointPositionGenerator().generate(0);
        assertTrue(iterator.hasNext());
        assertEquals(new Point(0, 0), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNegativeDistanceHasNoElements() {
        Iterator<Point> iterator = new PointPositionGenerator().generate(-1);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}