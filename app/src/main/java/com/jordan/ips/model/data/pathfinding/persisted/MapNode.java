package com.jordan.ips.model.data.pathfinding.persisted;

import com.jordan.renderengine.data.Point2d;

import java.io.Serializable;

public class MapNode  implements Serializable {

    private int id;
    private int mapId;
    private Point2d location;
    private boolean rootNode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public Point2d getLocation() {
        return location;
    }

    public void setLocation(Point2d location) {
        this.location = location;
    }

    public boolean isRootNode() {
        return rootNode;
    }

    public void setRootNode(boolean rootNode) {
        this.rootNode = rootNode;
    }
}
