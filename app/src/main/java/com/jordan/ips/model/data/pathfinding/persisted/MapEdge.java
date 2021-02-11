package com.jordan.ips.model.data.pathfinding.persisted;

import java.io.Serializable;

public class MapEdge implements Serializable {

    private int node1Id;
    private int node2Id;
    private int id;
    private int mapId;

    public int getNode1Id() {
        return node1Id;
    }

    public void setNode1Id(int node1Id) {
        this.node1Id = node1Id;
    }

    public int getNode2Id() {
        return node2Id;
    }

    public void setNode2Id(int node2Id) {
        this.node2Id = node2Id;
    }

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
}
