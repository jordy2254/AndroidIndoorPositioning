package com.jordan.ips.model.data.waypoints;

import com.jordan.ips.model.data.pathfinding.PathNode;
import com.jordan.renderengine.data.Point2d;

public abstract class Waypoint<T> {

    final T point;
    PathNode pathNode;

    public Waypoint(T point) {
        this.point = point;
    }

    public abstract Point2d getLocation();

    public abstract boolean isSelected();
    public abstract void setSelected(boolean selected);
    public abstract void toggleSelected();

    public T getPoint(){
        return point;
    }

    public PathNode getPathNode() {
        return pathNode;
    }

    public void setPathNode(PathNode pathNode) {
        this.pathNode = pathNode;
    }

    public abstract String getName();
}
