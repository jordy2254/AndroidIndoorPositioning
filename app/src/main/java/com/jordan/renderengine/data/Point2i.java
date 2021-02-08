package com.jordan.renderengine.data;

public class Point2i {

    public int x,y;

    public Point2i(){}

    public Point2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2i(Point2d point) {
        this.x = (int)point.x;
        this.y = (int)point.y;
    }

    public Point2d add(Point2d point){
        double nx = point.x + this.x;
        double ny = point.y + this.y;
        return new Point2d(nx, ny);
    }

    public Point2d subtract(Point2d point){
        double nx = point.x - this.x;
        double ny = point.y - this.y;
        return new Point2d(nx, ny);
    }

    public Point2d divide(Point2d point){
        if (point.x == 0 || point.y == 0 || this.x == 0 || this.y == 0){
            throw new ArithmeticException("Cannot divide by 0");
        }
        double nx = point.x / this.x;
        double ny = point.y / this.y;
        return new Point2d(nx, ny);
    }

    public Point2d multiply(Point2d point){
        double nx = point.x * this.x;
        double ny = point.y * this.y;
        return new Point2d(nx, ny);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2i point2d = (Point2i) o;
        return point2d.x == x && point2d.y == y;
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
}
