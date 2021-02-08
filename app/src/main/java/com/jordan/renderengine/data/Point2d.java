package com.jordan.renderengine.data;

import java.io.Serializable;

public class Point2d implements Serializable {

    public double x,y;

    public Point2d(){}

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2d(Point2d point) {
        this.x = point.x;
        this.y =point.y;
    }

    public Point2i getIntValue(){
        return new Point2i(((int) x), ((int) y));
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
        Point2d point2d = (Point2d) o;
        return Double.compare(point2d.x, x) == 0 &&
                Double.compare(point2d.y, y) == 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
