package com.jordan.ips.model.data.waypoints;

import com.jordan.ips.model.LocationService;
import com.jordan.ips.model.utils.PathFindingUtils;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.renderengine.data.Point2d;

public class CurrentLocationWayPoint extends Waypoint<LocationService>{

    private boolean selected = false;
    private Map map;

    public CurrentLocationWayPoint(LocationService point, Map map) {
        super(point);
        this.pathNode = new PathNode(getLocation());
        this.map = map;
    }

    @Override
    public Point2d getLocation() {
        return new Point2d(150,250);
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
    public PathNode getPathNode() {
        return PathFindingUtils.createDynamicPathNode(map, getLocation(), 0);
    }

    @Override
    public String getName() {
        return "Current Location";
    }
}
