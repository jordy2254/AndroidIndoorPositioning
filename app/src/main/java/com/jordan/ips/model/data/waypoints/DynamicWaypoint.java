package com.jordan.ips.model.data.waypoints;

import com.jordan.ips.model.LocationService;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.utils.PathFindingUtils;
import com.jordan.renderengine.data.Point2d;

public class DynamicWaypoint extends Waypoint<Point2d>{

    private boolean selected = false;
    private final Map map;

    public DynamicWaypoint(Point2d point, Map map) {
        super(point);
        this.pathNode = new PathNode(getLocation());
        this.map = map;
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
    public PathNode getPathNode() {
        //TODO dehardcode the floor
        return PathFindingUtils.createDynamicPathNode(map, getLocation(), 1);
    }

    @Override
    public String getName() {
        return "Point on map";
    }
}
