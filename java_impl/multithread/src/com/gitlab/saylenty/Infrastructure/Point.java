package com.gitlab.saylenty.Infrastructure;

public final class Point {
    private int x = 0;
    private int y = 0;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point){
            Point d = (Point)obj;
            return this.x == d.x && this.y == d.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return String.format("{x=%d, y=%d}", x, y);
    }
}
