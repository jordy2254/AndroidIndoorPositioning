package com.jordan.ips.model.data.waypoints;

import com.jordan.renderengine.data.Point2d;

public class MapWaypoint extends Waypoint<Point2d> {

    boolean selected = false;

    public MapWaypoint(Point2d point) {
        super(point);
    }

    @Override
    public Point2d getLocation() {
        return point;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void toggleSelected() {
        this.selected = !selected;
    }

    @Override
    public String getName() {
        return "Point on map";
    }
}
