package com.jordan.ips.model.data.waypoints;

import com.jordan.ips.model.LocationService;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.ips.model.utils.PathFindingUtils;
import com.jordan.renderengine.data.Point2d;

public class DynamicWaypoint extends Waypoint<Point2d>{

    private boolean selected = false;
    private final Map map;

    private PathNode pathNode;

    public DynamicWaypoint(Point2d point, Map map) {
        super(point);
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
        if(!this.selected && pathNode != null){
            PathFindingUtils.unlinkDynamicPathNode(pathNode);
        }
    }

    @Override
    public void toggleSelected() {
        this.selected = !selected;
        if(!this.selected && pathNode != null){
            PathFindingUtils.unlinkDynamicPathNode(pathNode);
        }
    }

    @Override
    public PathNode getPathNode() {
        if (pathNode == null) {
            //TODO dehardcode the floor
            pathNode = PathFindingUtils.createDynamicPathNode(map, getLocation(), 1);
        }
        return pathNode;
    }

    @Override
    public String getName() {
        return "Point on map";
    }
}
