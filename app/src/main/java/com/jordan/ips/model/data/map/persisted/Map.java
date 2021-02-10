package com.jordan.ips.model.data.map.persisted;

import com.google.gson.annotations.SerializedName;
import com.jordan.ips.model.data.pathfinding.persisted.PathNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Map implements Serializable {

    private long id;
    private String identifier;
    private String password;
    private String name;

    private List<Building> buildings;
    private PathNode pathRoot;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addBuilding(Building building) {
        if (buildings == null) {
            buildings = new ArrayList<>();
        }
        this.buildings.add(building);
    }

    public PathNode getPathRoot() {
        return pathRoot;
    }

    public void setPathRoot(PathNode pathRoot) {
        this.pathRoot = pathRoot;
    }
}
