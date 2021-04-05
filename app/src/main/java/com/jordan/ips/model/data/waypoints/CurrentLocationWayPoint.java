package com.jordan.ips.model.data.waypoints;

import com.jordan.ips.model.locationTracking.LocationService;
import com.jordan.ips.model.utils.PathFindingUtils;
import com.jordan.ips.model.data.map.persisted.Map;
import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.renderengine.data.Point2d;

public class CurrentLocationWayPoint extends Waypoint<LocationService>{

    private boolean selected = false;
    private final Map map;
    private PathNode pathNode;

    Point2d cLoc = new Point2d(170,290);

    public CurrentLocationWayPoint(LocationService point, Map map) {
        super(point);
        this.map = map;
    }

    @Override
    public Point2d getLocation() {
        cLoc = cLoc.add(new Point2d(1,0));
        return cLoc;
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
        if(pathNode == null){
            pathNode = PathFindingUtils.createDynamicPathNode(map, getLocation(), 1);
        }else{
            PathFindingUtils.unlinkDynamicPathNode(pathNode, false);
            pathNode.setLocation(getLocation());
            PathFindingUtils.linkDynamicPathNode(map, pathNode, 1);
        }
        return pathNode;
    }

    @Override
    public String getName() {
        return "Current Location";
    }
}
