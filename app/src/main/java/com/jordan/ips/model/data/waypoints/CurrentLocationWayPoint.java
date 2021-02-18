package com.jordan.ips.model.data.waypoints;

import com.jordan.ips.model.LocationService;
import com.jordan.renderengine.data.Point2d;

public class CurrentLocationWayPoint extends Waypoint<LocationService>{

    public CurrentLocationWayPoint(LocationService point) {
        super(point);
    }

    @Override
    public Point2d getLocation() {
        return null;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public void setSelected(boolean selected) {

    }

    @Override
    public void toggleSelected() {

    }

    @Override
    public String getName() {
        return "Current Location";
    }
}
