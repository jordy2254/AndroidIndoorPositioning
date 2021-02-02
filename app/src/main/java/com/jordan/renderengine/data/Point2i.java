package com.jordan.renderengine.data;

public class Point2i {

    public int x,y;

    public Point2i(){

    }

    public Point2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2i(Point2d point) {
        this.x = (int)point.x;
        this.y = (int)point.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2i point2d = (Point2i) o;
        return point2d.x == x && point2d.y == y;
    }
}
