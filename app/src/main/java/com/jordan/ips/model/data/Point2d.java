package com.jordan.ips.model.data;

import java.io.Serializable;

public class Point2d implements Serializable {

    public double x,y;

    public Point2d(){

    }

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2d(Point2d point) {
        this.x = point.x;
        this.y =point.y;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2d point2d = (Point2d) o;
        return Double.compare(point2d.x, x) == 0 &&
                Double.compare(point2d.y, y) == 0;
    }
}
