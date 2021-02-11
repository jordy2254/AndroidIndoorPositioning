package com.jordan.ips.model.data.waypoints;

import com.jordan.ips.model.data.map.persisted.Room;
import com.jordan.renderengine.data.Point2d;

public class RoomWaypoint extends Waypoint<Room>{

    public RoomWaypoint(Room point) {
        super(point);
    }

    @Override
    public Point2d getLocation() {
        return point.getLocation();
    }

    @Override
    public boolean isSelected() {
        return point.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        point.setSelected(selected);
    }

    @Override
    public void toggleSelected() {
        point.setSelected(!point.isSelected());
    }
}
